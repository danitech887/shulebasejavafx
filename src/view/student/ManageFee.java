package view.student;

import model.DBConnection;
import utils.TableViewHelper;
import utils.*;
import charts.FeeDashboard;
import pdf.GeneratePdf;
import utils.DirectoryUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.nio.file.Paths;

public class ManageFee{
    private VBox mainLayout;
    private StackPane feeLayout;
    private ComboBox<String> termCombo;
    private String regno;
    private ComboBox<String> mode_of_paymentcombo;
    private TextField transactioncodeEntry;
    private TextField amountEntry;
    private String term;
    private DatePicker payment_date;
    private String actualName;

    private Label amountpaidLabel;
    private Label balanceLabel;

    private TableView<Map<String,Object>> statementtable;
    private TextField search_entry;
    private ComboBox<String> criteriacombo;

    private Label totalpaidLabel;
    private Label totalbalanceLabel;

    private static final Date current_date = new Date();
    private static final Alerthelper alert = new Alerthelper();

    private int school_id;
    

    public ManageFee(){
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.TOP_CENTER);
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        Label title = new Label("FEE MANAGEMENT");
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,24));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);
        
        feeLayout = new StackPane();

        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");
        ComboBox<String> year_combo = new ComboBox<>();
        year_combo.setPromptText("Select year");
        for(int i= 5; i<9; i++){
            year_combo.getItems().add("202"+i);
        }
        
        HBox combo_layout =  new HBox(10);
        combo_layout.setAlignment(Pos.CENTER);
        combo_layout.setPadding(new Insets(10));
        combo_layout.getChildren().addAll(term_combo,year_combo);
        DBConnection db = new DBConnection();
        year_combo.setOnAction(e ->{
                String term = term_combo.getValue();
                String year = year_combo.getValue();
                if(term != null && year != null){
                    int cash = db.getCount("select sum(amount) from students_fee where mode_of_payment = 'Cash' and term = '"+term+"' and  year = '"+year+"'");
                    int mpesa = db.getCount("select sum(amount) from students_fee where mode_of_payment = 'Mpesa' and term = '"+term+"' and year = '"+year+"'");
                    int no = db.getCount("select count(*) from students_studentinfo");

                    int pending = (no * 10000) - (cash + mpesa);
                    int total = mpesa + cash;

                    feeLayout.getChildren().clear();
                    combo_layout.getChildren().clear();
                    combo_layout.getChildren().addAll(term_combo,year_combo);

                    FeeDashboard feeDashboard = new FeeDashboard(total,mpesa,cash,pending,term,year,combo_layout);
                    
                    feeLayout.getChildren().add(feeDashboard.getLayout());
                } else {
                    alert.showError("Invalid term or year selected");
                }
            
        });
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy");
        String year = dateformat.format(date);

        int cash = db.getCount("select sum(amount) from students_fee where mode_of_payment = 'Cash' and year = '"+year+"'");
        int mpesa = db.getCount("Select sum(amount) from students_fee where mode_of_payment = 'Mpesa' and year = '"+year+"'");
        int no = db.getCount("Select count(*) from students_studentinfo");
        int pending = (no * 10000) - (cash + mpesa);
        int total = mpesa + cash;
        FeeDashboard fee = new FeeDashboard(total,mpesa,cash,pending,null,year,combo_layout);
        feeLayout.getChildren().clear();
        feeLayout.getChildren().add(fee.getLayout());

        
        GridPane navLayout = new GridPane();
        navLayout.setHgap(30);
        navLayout.setPadding(new Insets(10));
        navLayout.setAlignment(Pos.CENTER);
        Button feePaymentbtn = new Button("FEE PAYMENT");
        feePaymentbtn.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px");
        feePaymentbtn.setOnAction(e -> feePayment());
        Button feestatementbtn = new Button("FEE STATEMENT");
        feestatementbtn.setOnAction(e -> feeStatement());
        feestatementbtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px");
        Button fullfeestatementbtn = new Button("FULL FEE STATEMENT");
        fullfeestatementbtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px");

        Button[] buttons = {feePaymentbtn,feestatementbtn,fullfeestatementbtn};
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setPrefWidth(150);
        }
        navLayout.add(feePaymentbtn,0,1);
        navLayout.add(feestatementbtn,1,1);
        navLayout.add(fullfeestatementbtn,2,1);
        mainLayout.getChildren().addAll(header,navLayout,feeLayout);
    }


    
    public void feePayment(){
            DBConnection db = new DBConnection();
            Alerthelper alert = new Alerthelper();
            TextInputDialog termDialog = new TextInputDialog();
            termDialog.setTitle("Input Required");
            termDialog.setHeaderText("Enter term like, 1,2,3..");
            termDialog.setContentText("Term");
            Optional<String> termresult = termDialog.showAndWait();
            
            termresult.ifPresent(trm ->{
            if (trm != null){
                term = "Term "+trm;
                TextInputDialog regDialog = new TextInputDialog();
                regDialog.setTitle("Input Required");
                regDialog.setHeaderText("Enter reg no");
                regDialog.setContentText("Reg No");
                Optional<String> regresult = regDialog.showAndWait();
                regresult.ifPresent(regNo ->{
                    if(regNo != null){
                        regno = regNo;
                        Map<String,Object> data = db.fetchOne("select concat_ws(' ',first_name,second_name,surname) as Name,grade,stream from students_studentinfo where registration_no = '"+regno+"'");
                        actualName = data.get("Name").toString();
                        String actualGrade = data.get("grade").toString();
                        String actualStream = data.get("stream").toString();
                        feeLayout.getChildren().clear();
                        VBox layout = new VBox();
                        GridPane details = new GridPane();
                        details.setHgap(30);
                        details.setAlignment(Pos.CENTER);
                        Label name = new Label("Name:");
                        name.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,15));
                        Label nameLabel = new Label(actualName);
                        nameLabel.setFont(Font.font("Times New Roman",FontWeight.BOLD,15));
                        Label grade = new Label(actualGrade);
                        grade.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        Label stream = new Label(actualStream);
                        stream.setFont(Font.font("Times New Roman",FontWeight.BOLD,15));

                        details.add(name,0,0);
                        details.add(nameLabel,1,0);
                        details.add(grade,2,0);
                        details.add(stream,3,0);


                        HBox paymentLayout = new HBox();
                        paymentLayout.setAlignment(Pos.TOP_CENTER);
                        layout.setAlignment(Pos.TOP_CENTER);
                        Label title = new Label("FEE PAYMENT");
                        title.setPadding(new Insets(10));
                        title.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,18));

                        GridPane recordFeeLayout = new GridPane();
                        recordFeeLayout.setAlignment(Pos.CENTER);
                        // recordFeeLayout.setPadding(new Insets(10));
                        recordFeeLayout.setVgap(20);
                        Label recordLabel = new Label("Record Payments");
                        

                        recordLabel.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        mode_of_paymentcombo = new ComboBox<>();
                    
                        mode_of_paymentcombo.setPadding(new Insets(10));
                        mode_of_paymentcombo.setPromptText("Mode of Payment");
                        mode_of_paymentcombo.getItems().addAll("Mpesa","Cash");
                        payment_date = new DatePicker();
                        transactioncodeEntry = new TextField();
                        transactioncodeEntry.setFont(Font.font("Times New Roman",12));
                        transactioncodeEntry.setPadding(new Insets(10));
                        transactioncodeEntry.setPromptText("Transaction Code");
                        amountEntry = new TextField();
                        amountEntry.setFont(Font.font("Times New Roman",12));
                        amountEntry.setPromptText("Amount Paid");
                        amountEntry.setPadding(new Insets(10));
                        Button recordbtn = new Button("Record Payments");
                        recordbtn.setFont(Font.font("Times New Roman",15));
                        recordbtn.setPadding(new Insets(10));
                        recordbtn.setStyle("-fx-background-color: blue; -fx-text-fill: white");
                        recordbtn.setOnAction(e ->recordFee());
                        recordFeeLayout.add(recordLabel,0,0);
                        recordFeeLayout.add(mode_of_paymentcombo,0,1);
                        recordFeeLayout.add(payment_date,0,2);
                        recordFeeLayout.add(transactioncodeEntry,0,3);
                        recordFeeLayout.add(amountEntry,0,4);
                        recordFeeLayout.add(recordbtn,0,5);

                        GridPane feeDetails = new GridPane();
                        // feeDetails.setPadding(new Insets(10));

                        int actualamountPaid = db.getCount("select sum(amount) as paid from students_fee where registration_no_id = '"+regno+"' and term = '"+term+"'");
                        int actualbalance = 10000 - actualamountPaid;
                        

                        feeDetails.setVgap(20);
                        feeDetails.setAlignment(Pos.CENTER);
                        Label topLabel = new Label("Fee Details");
                        topLabel.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        Label amountBilled = new Label("Amount Billed:");
                        Label amountBilledLabel = new Label("10,000.00");

                        Label amountPaid = new Label("Amount Paid:");
                        amountpaidLabel = new Label("'"+actualamountPaid+"'");

                        Label balance = new Label("Balance:");
                        balanceLabel = new Label("'"+actualbalance+"'");

                        Label[] labels = {amountBilled,amountBilledLabel,amountPaid,amountpaidLabel,balance,balanceLabel};
                        for(int i =0; i < labels.length; i++){
                            labels[i].setFont(Font.font("Times New Roman",FontWeight.EXTRA_BOLD,15));
                            labels[i].setPadding(new Insets(10));
                        }
                        feeDetails.add(topLabel,0,0);

                        feeDetails.add(amountBilled,0,1);
                        feeDetails.add(amountBilledLabel,1,1);

                        feeDetails.add(amountPaid,0,2);
                        feeDetails.add(amountpaidLabel,1,2);

                        feeDetails.add(balance,0,3);
                        feeDetails.add(balanceLabel,1,3);

                        GridPane statementLayout = new GridPane();
                        statementLayout.setAlignment(Pos.CENTER);
                        // statementLayout.setPadding(new Insets(10));
                        statementLayout.setVgap(10);

                        Label statementLabel = new Label("Fee Statement");
                        statementLabel.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        statementLayout.add(statementLabel,0,0);

                        GridPane searchPanel = new GridPane();
                        criteriacombo = new ComboBox<>();
                        criteriacombo.setPromptText("Select Criteria");
                        criteriacombo.getItems().addAll("Mode of Payment","Date of Payment");
                        search_entry = new TextField();
                        search_entry.setPromptText("Type your search here");
                        Button searchbtn = new Button("Search");
                        searchbtn.setOnAction(e ->search_fee_data(regno,term));


                        searchPanel.add(criteriacombo,0,0);
                        searchPanel.add(search_entry,1,0);
                        searchPanel.add(searchbtn,2,0);


                        statementLayout.add(searchPanel,0,1);
                        statementtable = new TableView<>();

                        List<Map<String,Object>> feedata = db.fetchAll("select amount as Amount,mode_of_payment as ModeOfPayment,date_of_payment as DateOfPayment from students_fee where registration_no_id = '"+regno+"' and term = '"+term+"'");
                        TableViewHelper.populateTable(statementtable,feedata);
                        statementtable.setPrefHeight(200);
                        statementLayout.add(statementtable,0,2);

                        HBox.setHgrow(recordFeeLayout,Priority.ALWAYS);
                        HBox.setHgrow(feeDetails,Priority.ALWAYS);
                        HBox.setHgrow(statementLayout,Priority.ALWAYS);

                        paymentLayout.getChildren().addAll(recordFeeLayout,feeDetails,statementLayout);

                        layout.getChildren().addAll(title,details,paymentLayout);
                        feeLayout.getChildren().add(layout);
                    }else{
                        alert.showError("You have not entered reg no");
                    }
                });

            }else {
                alert.showError("You have not entered term");
            }
        });
        
    }
    public void displayFeeData(String regno,String term){
        DBConnection db = new DBConnection();

        int amountPaid = db.getCount("select sum(amount) from students_fee where registration_no_id = '"+regno+"' and term = '"+term+"'");
        double balance = 10000 - amountPaid;
        amountpaidLabel.setText("'"+amountPaid+"'");
        balanceLabel.setText("'"+balance+"'");

        
        List<Map<String,Object>> feedata = db.fetchAll("select amount as Amount,mode_of_payment as ModeOfPayment,date_of_payment as DateOfPayment,time as TimePaid from students_fee where registration_no_id = '"+regno+"' and term = '"+term+"'");
        TableViewHelper.populateTable(statementtable,feedata);

        Alerthelper alert = new Alerthelper();
        alert.showSuccess("regno: "+regno+" term: "+term+" amountPaid: "+amountPaid+" /nbalance: "+balance);

    }

    public void recordFee(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();
        String mode_of_payment = mode_of_paymentcombo.getValue().toString();
        String transaction_code = transactioncodeEntry.getText();
        
        double amount = Double.parseDouble(amountEntry.getText());
        String amount_paid = amountEntry.getText();
        
        Map<String,Object> details = db.fetchOne("select grade,stream from students_studentinfo where registration_no = '"+regno+"'");
        String grade = details.get("grade").toString();
        String stream = details.get("stream").toString();
        String date = payment_date.getValue().toString();
        String time = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(current_date);

        File base_dir = new File("ShuleBase Files"+File.separator+"Fee Receipts"+File.separator +grade+ File.separator +stream);
        if(!base_dir.exists()){
            base_dir.mkdirs();
        }

        DirectoryUtil dir = new DirectoryUtil();
        int payment_no = db.getCount("select count(*) from students_fee where registration_no_id = '"+regno+"'");
        int pays = payment_no + 1;
        String filepath = dir.createFileName(base_dir.toString(),regno + " "+term+" receipt "+pays+" .pdf");
        // new File(filepath).getParentFile().mkdirs();
        if(mode_of_payment.equals("Mpesa")){
            if (db.storeData("insert into students_fee(registration_no_id,mode_of_payment,transaction_code,amount,date_of_payment,term,school_id,status) values('"+regno+"','"+mode_of_payment+"','"+transaction_code+"','"+amount+"','"+date+"','"+term+"',1,'Confirmed')"));
                alert.showSuccess("Fee for student with reg no "+regno+" has been recorded");
                displayFeeData(regno,term);
                int balance = db.getCount("select 10000 - sum(amount) from students_fee where registration_no_id = '"+regno+"' and term ='"+term+"'");
                try {
                    GeneratePdf pdf = new GeneratePdf();
                    
                    pdf.generateFeeReceipt(filepath,regno,actualName,grade,stream,term,date,time,amount,balance,mode_of_payment);
                        alert.showSuccess("Receipt generated");
                } catch (Exception e){
                    e.printStackTrace();
                }
                
                transactioncodeEntry.setText("");
                amountEntry.setText("");


        }else if (mode_of_payment.equals("Cash")){
                db.storeData("insert into students_fee(registration_no_id,mode_of_payment,amount,date_of_payment,term,school_id,status) values('"+regno+"','"+mode_of_payment+"','"+amount+"','"+date+"','"+term+"',1,'Confirmed')");
                alert.showSuccess("Fee for student with reg no "+regno+" has been recorded");
                displayFeeData(regno,term);
                int balance = db.getCount("select 10000 - sum(amount) from students_fee where registration_no_id = '"+regno+"' and term = '"+term+"' ");
                try {
                    GeneratePdf pdf = new GeneratePdf();
                    pdf.generateFeeReceipt(filepath,regno,actualName,grade,stream,term,date,time,amount,balance,mode_of_payment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                transactioncodeEntry.setText("");
                amountEntry.setText("");
        }
    }
    public void search(String column,String regno,String term){
        DBConnection db = new DBConnection();
        String search_e = search_entry.getText();
        List<Map<String,Object>> feedata = db.fetchAll("select amount as Amount,mode_of_payment as ModeOfPayment,date_of_payment as DateOfPayment,time as TimePaid from students_fee where "+column+" = '"+search_e+"' and registration_no_id = '"+regno+"' and term = '"+term+"'");
        TableViewHelper.populateTable(statementtable,feedata);
        System.out.println("data: "+ feedata);
    }
    public void search_fee_data(String regno,String term){
        String criteria = criteriacombo.getValue();

        if(criteria.equals("Mode of Payment")){
            search("mode_of_payment",regno,term);
        
        }else if(criteria.equals("Date of Payment")){
            search("date_of_payment",regno,term);
        }
    }
    public void feeStatement(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();

        VBox feestatementLayout = new VBox();
        feestatementLayout.setAlignment(Pos.CENTER);
        feestatementLayout.setPadding(new Insets(10));

        Label top_label = new Label("FEE STATEMENT");
        top_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,18));
        feestatementLayout.getChildren().add(top_label);

        GridPane combo_layout = new GridPane();
        combo_layout.setHgap(20);
        combo_layout.setPadding(new Insets(10));
        ComboBox<String> grade_combo = new ComboBox<>();
        StreamFetch fetchdata = new StreamFetch(db);
        List <String> grades = fetchdata.populateGrades();
        
        grade_combo.setPromptText("Select Grade");
        
        for(String grade : grades){
            grade_combo.getItems().add(grade);
        }
        
        ComboBox<String> stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select Stream");
        
        
        grade_combo.setOnAction(e ->{
            String grade = grade_combo.getValue();
            stream_combo.getItems().clear();
            List <String> streams = fetchdata.populateStreams(grade);
            stream_combo.getItems().addAll(streams);
        });

        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");
        ComboBox[] combos = {grade_combo,stream_combo,term_combo};
        for(int i = 0; i < 3; i++){
            combos[i].setPrefWidth(150);
        }

        Button filter_btn = new Button("Filter");
        
        combo_layout.add(grade_combo,1,1);
        combo_layout.add(stream_combo,2,1);
        combo_layout.add(term_combo,3,1);
        combo_layout.add(filter_btn,4,1);
        

        feestatementLayout.getChildren().add(combo_layout);

       
        TableView<Map<String,Object>> fullstatementTable = new TableView<>();
        feestatementLayout.getChildren().add(fullstatementTable);

        GridPane labelLayout = new GridPane();
        labelLayout.setHgap(20);
        Label totalpaidLabel1 = new Label("Total Amount Paid:");
        totalpaidLabel = new Label();
        Label totalbalanceLabel1 = new Label("Total Balance:");
        totalbalanceLabel = new Label();
        labelLayout.add(totalpaidLabel1,0,0);
        labelLayout.add(totalpaidLabel,1,0);
        labelLayout.add(totalbalanceLabel1,0,1);
        labelLayout.add(totalbalanceLabel,1,1);
        feestatementLayout.getChildren().add(labelLayout);
        Label[] labels = {totalpaidLabel1,totalpaidLabel,totalbalanceLabel1,totalbalanceLabel};
        for (int i = 0; i < labels.length;i++){
            labels[i].setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,20));
        }
        feeLayout.getChildren().clear();
        feeLayout.getChildren().add(feestatementLayout);
        List<Map<String,Object>> fullstatement = db.fetchAll("select f.registration_no_id as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream, f.amount as AmountPaid,f.mode_of_payment as ModeOfPayment,f.date_of_payment as DateOfPayment,time as TimePaid,f.transaction_code as TransactionCode, f.term as Term from students_fee f join students_studentinfo s on s.registration_no = f.registration_no_id");
        TableViewHelper.populateTable(fullstatementTable,fullstatement);
        int amount_paid = db.getCount("select sum(amount) from students_fee");
        int no_ = 0;
        float expected_fee = 0;
        float balance_ = 0;
        
        List <String> available_grades = fetchdata.populateGrades();
        for (String available_grade : available_grades){
            float expected_grade_fee = db.getCount("select sum(expected_fee) from students_grade where school_id = 1 and name = '"+available_grade+"'");
            no_ = db.getCount("select count(*) from students_studentinfo where grade = '"+available_grade+"' and school_id = 1");
            expected_fee += expected_grade_fee;
            balance_ = (expected_fee * no_) - amount_paid;
            

        }
        
        totalpaidLabel.setText(String.valueOf(amount_paid));
        totalbalanceLabel.setText(String.valueOf(balance_));
        filter_btn.setOnAction(e ->{
            String grade = grade_combo.getValue();
            String stream = stream_combo.getValue();
            String term = term_combo.getValue();

            List<Map<String,Object>> feeData = db.fetchAll("select f.registration_no_id as RegNo, concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream, sum(f.amount) as AmountPaid,10000 - sum(f.amount) as Balance,f.term as Term from students_fee f join students_studentinfo s on f.registration_no_id = s.registration_no where s.grade = '"+grade+"' and s.stream = '"+stream+"' and f.term = '"+term+"' group by f.registration_no_id,s.stream,s.grade,f.term");
            TableViewHelper.populateTable(fullstatementTable,feeData);
            int amountPaid = db.getCount("select sum(f.amount) from students_fee f join students_studentinfo s on s.registration_no = f.registration_no_id  where s.grade = '"+grade+"' and s.stream = '"+stream+"' and f.term = '"+term+"'");
            totalpaidLabel.setText(String.valueOf(amountPaid));
            int no = db.getCount("select count(*) from students_studentinfo where grade = '"+grade+"' and stream = '"+stream+"'");
            int balance = (no * 10000) - amountPaid;
            totalbalanceLabel.setText(String.valueOf(balance));
        });
    }
    public VBox getLayout(){
        return mainLayout;
    }
}