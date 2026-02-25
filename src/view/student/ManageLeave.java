package view.student;

import model.DBConnection;
import utils.TableViewHelper;
import utils.*;
import charts.LeaveDashboard;
import pdf.GeneratePdf;
import utils.DirectoryUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManageLeave{
    private VBox mainLayout;
    private StackPane leaveLayout;
    private ComboBox<String> termCombo;
    private String regno;
    private ComboBox<String> reasoncombo;
    private TextField otherReasonEntry;
    private TextField phoneEntry;

    private String term;
    private DatePicker approval_date;
    private TableView<Map<String,Object>> statementtable;
    private TextField search_entry;
    private ComboBox<String> criteriacombo;

    Date date = new Date();
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
    SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss");
    private String year = yearformat.format(date);
    private String time = timeformat.format(date);

    private int school_id;



    

    public ManageLeave(){
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.TOP_CENTER);
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        Label title = new Label("LEAVE MANAGEMENT");
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,24));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);
        
        leaveLayout = new StackPane();
        HBox check_layout = new HBox(10);
        check_layout.setPadding(new Insets(10));
        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");

        ComboBox<String> year_combo = new ComboBox<>();
        year_combo.setPromptText("Select Year");
        for(int i = 5;i<= 9; i++){
            year_combo.getItems().add("202"+i);
        }

        check_layout.getChildren().addAll(term_combo,year_combo);
        leaveLayout.getChildren().clear();
        DBConnection db = new DBConnection();
        year_combo.setOnAction(e ->{
            String term = term_combo.getValue();
            String year = year_combo.getValue();
            int total = db.getCount("select count(*) from students_leavemanagement where term = '"+term+"' and year = '"+year+"'");
            int male = db.getCount("select count(*) from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where s.gender = 'Male' and l.term = '"+term+"' and l.year = '"+year+"'");
            int female = db.getCount("select count(*) from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where s.gender = 'Female' and l.term = '"+term+"' and l.year = '"+year+"'");
            int on_leave = db.getCount("select count(*) from students_leavemanagement where return_date = 'none' and term = '"+term+"' and year = '"+year+"'");
            int school_fees = db.getCount("select count(*) from students_leavemanagement where reason = 'School Fees' and term = '"+term+"' and year = '"+year+"'");
            int other_reason = db.getCount("select count(*) from students_leavemanagement where reason = 'Other Reason' and term = '"+term+"' and year = '"+year+"'");
            check_layout.getChildren().clear();
            check_layout.getChildren().addAll(term_combo,year_combo);
            LeaveDashboard leaveDashboard = new LeaveDashboard(total,male,female,on_leave,school_fees,other_reason,term,year,check_layout);
            leaveLayout.getChildren().clear();
            leaveLayout.getChildren().add(leaveDashboard.getLayout());
        });
        int total = db.getCount("Select count(*) from students_leavemanagement where year = '"+year+"'");
        int male = db.getCount("select count(*) from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where s.gender = 'Male' and l.year = '"+year+"'");
        int female = db.getCount("select count(*) from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where s.gender = 'Female' and l.year = '"+year+"'");
        int on_leave = db.getCount("select count(*) from students_leavemanagement where return_date = 'none' and year = '"+year+"'");
        int school_fees = db.getCount("select count(*) from students_leavemanagement where reason = 'School Fees' and year = '"+year+"'");
        int other_reason = db.getCount("select count(*) from students_leavemanagement where reason = 'Other Reason' and year = '"+year+"'");
        LeaveDashboard leave = new LeaveDashboard(total,male,female,on_leave,school_fees,other_reason,null,year,check_layout);
        leaveLayout.getChildren().clear();
        leaveLayout.getChildren().add(leave.getLayout());
        GridPane navLayout = new GridPane();
        navLayout.setHgap(30);
        navLayout.setPadding(new Insets(10));
        navLayout.setAlignment(Pos.CENTER);
        Button leaveapprovalbtn = new Button("LEAVE APPROVALS");
        leaveapprovalbtn.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px;");
        leaveapprovalbtn.setOnAction(e -> LeaveApproval());
        Button leavestatementbtn = new Button("LEAVE STATEMENT");
        leavestatementbtn.setOnAction(e -> leaveStatement());
        leavestatementbtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px;");
        Button fullleavestatementbtn = new Button("FULL LEAVE STATEMENT");
        fullleavestatementbtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px;");

        Button[] buttons = {leaveapprovalbtn,leavestatementbtn,fullleavestatementbtn};
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setPrefWidth(150);
        }
        navLayout.add(leaveapprovalbtn,0,1);
        navLayout.add(leavestatementbtn,1,1);
        navLayout.add(fullleavestatementbtn,2,1);
        mainLayout.getChildren().addAll(header,navLayout,leaveLayout);
    }


    
    public void LeaveApproval(){
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
                        String actualName = data.get("Name").toString();
                        String actualGrade = data.get("grade").toString();
                        String actualStream = data.get("stream").toString();
                        leaveLayout.getChildren().clear();
                        VBox layout = new VBox();

                        GridPane details = new GridPane();
                        details.setHgap(30);
                        details.setAlignment(Pos.CENTER);
                        Label name = new Label("Name:");
                        name.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,15));
                        Label nameLabel = new Label(actualName);

                        
                        Label grade = new Label("Grade");
                        grade.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        Label grade_label = new Label(actualGrade);


                        Label stream = new Label("Stream");
                        stream.setFont(Font.font("Times New Roman",FontWeight.BOLD,15));
                        Label stream_label = new Label(actualStream);


                        details.add(name,0,0);
                        details.add(nameLabel,1,0);
                        details.add(grade,2,0);
                        details.add(grade_label,3,0);
                        details.add(stream,4,0);
                        details.add(stream_label,5,0);


                        HBox approvalLayout = new HBox();
                        approvalLayout.setAlignment(Pos.TOP_CENTER);
                        layout.setAlignment(Pos.TOP_CENTER);
                        Label title = new Label("LEAVE APPROVAL");
                        title.setPadding(new Insets(10));
                        title.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,18));

                        GridPane recordleaveLayout = new GridPane();
                        recordleaveLayout.setAlignment(Pos.CENTER);
                        // recordleaveLayout.setPadding(new Insets(10));
                        recordleaveLayout.setVgap(20);
                        Label recordLabel = new Label("Approve Leave");

                        recordLabel.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        reasoncombo = new ComboBox<>();
                    
                        reasoncombo.setPadding(new Insets(10));
                        reasoncombo.setPromptText("Reason for Leave");
                        reasoncombo.getItems().addAll("School Fees","Other Reason");
                        approval_date = new DatePicker();
                    
                        otherReasonEntry = new TextField();
                        otherReasonEntry.setFont(Font.font("Times New Roman",12));
                        otherReasonEntry.setPromptText("Other Reason");
                        otherReasonEntry.setPadding(new Insets(10));
                        phoneEntry = new TextField();
                        phoneEntry.setFont(Font.font("Times New Roman",12));
                        phoneEntry.setPromptText("Phone Number");
                        phoneEntry.setPadding(new Insets(10));
                        Button recordbtn = new Button("Approve Leave");
                        recordbtn.setFont(Font.font("Times New Roman",15));
                        recordbtn.setPadding(new Insets(10));
                        recordbtn.setStyle("-fx-background-color: blue; -fx-text-fill: white");
                        recordbtn.setOnAction(e ->recordleave());
                        recordleaveLayout.add(recordLabel,0,0);
                        recordleaveLayout.add(reasoncombo,0,1);
                        recordleaveLayout.add(otherReasonEntry,0,2);
                        recordleaveLayout.add(phoneEntry,0,3);
                        recordleaveLayout.add(approval_date,0,4);
                    
                        recordleaveLayout.add(recordbtn,0,5);

                       
                        int leave_no = db.getCount("select count(*) from students_leavemanagement where registration_no_id = '"+regno+"' and term = '"+term+"'");
        
                        

                        

                        GridPane statementLayout = new GridPane();
                        statementLayout.setAlignment(Pos.CENTER);
                        // statementLayout.setPadding(new Insets(10));
                        statementLayout.setVgap(10);

                        Label statementLabel = new Label("Leave Statement");
                        statementLabel.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
                        statementLayout.add(statementLabel,0,0);

                        GridPane searchPanel = new GridPane();
                        GridPane.setHgrow(searchPanel,Priority.ALWAYS);
                        criteriacombo = new ComboBox<>();
                        criteriacombo.setPromptText("Select Criteria");
                        criteriacombo.getItems().addAll("Reason","Other Reason","Date of Leave");
                        search_entry = new TextField();
                        search_entry.setPromptText("Type your search here");
                        Button searchbtn = new Button("Search");
                        searchbtn.setOnAction(e ->search_leave_data(regno,term));


                        searchPanel.add(criteriacombo,0,0);
                        searchPanel.add(search_entry,1,0);
                        searchPanel.add(searchbtn,2,0);


                        statementLayout.add(searchPanel,0,1);
                        statementtable = new TableView<>();
                        statementtable.setOnMouseClicked(event -> loadLeave());

                        List<Map<String,Object>> leavedata = db.fetchAll("select id as LeaveId, reason as Reason,other_reason as OtherReason,date_of_leave as DateOfLeave,return_date as ReturnDate from students_leavemanagement where registration_no_id = '"+regno+"' and term = '"+term+"'");
                        TableViewHelper.populateTable(statementtable,leavedata);
                        statementtable.setPrefHeight(200);
                        statementLayout.add(statementtable,0,2);
                        
                        
                        HBox.setHgrow(recordleaveLayout,Priority.ALWAYS);
                        HBox.setHgrow(statementLayout,Priority.ALWAYS);

                        approvalLayout.getChildren().addAll(recordleaveLayout,statementLayout);
                        layout.getChildren().addAll(title,details,approvalLayout);
                        leaveLayout.getChildren().add(layout);
                    }else{
                        alert.showError("You have not entered reg no");
                    }
                });

            }else {
                alert.showError("You have not entered term");
            }
        });
        
    }
    public void displayleaveData(String regno,String term){
        DBConnection db = new DBConnection();

        List<Map<String,Object>> leavedata = db.fetchAll("select id as LeaveId, reason as Reason,other_reason as OtherReason,date_of_leave as DateOfLeave,return_date as ReturnDate from students_leavemanagement where registration_no_id = '"+regno+"' and term = '"+term+"'");
        TableViewHelper.populateTable(statementtable,leavedata);
    }
    public void recordleave(){
        DBConnection db = new DBConnection();
        Map<String,Object> student_details = db.fetchOne("select concat_ws(' ',first_name,second_name,surname) as name,grade,stream from students_studentinfo where registration_no = '"+regno+"'");
        String name = student_details.get("name").toString();
        String grade = student_details.get("grade").toString();
        String stream = student_details.get("stream").toString();

        double balance = db.getAmount("select 10000 - sum(amount) from students_fee where registration_no_id = '"+regno+"' and term = '"+term+"' and year = '"+year+"'");
        
        System.out.println("Year: "+year+" term: "+term);
        Alerthelper alert = new Alerthelper();
        String reason = reasoncombo.getValue().toString();
        String other = otherReasonEntry.getText();
        String date = approval_date.getValue().toString();
        String phone = phoneEntry.getText();
        int no_leave = db.getCount("select count(*) from students_leavemanagement where registration_no_id = '"+regno+"' and return_date = NULL");
        File base_dir = new File("ShuleBase Files"+File.separator+"Leave Receipts"+File.separator+grade+File.separator+stream);
        if(!base_dir.exists()){
            base_dir.mkdirs();
        }
        int leave_no = db.getCount("select count(*) from students_leavemanagement where registration_no_id = '"+regno+"'");
        DirectoryUtil dir = new DirectoryUtil();
        String filepath = dir.createFileName(base_dir.toString(),regno +" leave no "+leave_no+1+" .pdf");
        if(no_leave == 0){
            if(reason.equals("School Fees")){
                if (db.storeData("insert into students_leavemanagement(registration_no_id,reason,other_reason,date_of_leave,time,term,year,phone,school_id) values('"+regno+"','"+reason+"','No Other Reason','"+date+"','"+time+"','"+term+"','"+year+"','"+phone+"',1)")){
                    alert.showSuccess("Leave record for student with reg no "+regno+" has been recorded");
                    displayleaveData(regno,term);
                    try{
                        GeneratePdf pdf = new GeneratePdf();
                        pdf.generateLeaveReceipt(regno,grade,stream,name,reason,"No Other Reason",balance,term,date,time,filepath);
                        alert.showSuccess("Leave receipt for student with reg no "+regno+" has been generated and saved to path "+filepath);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    otherReasonEntry.setText("");
                }

            }else if (reason.equals("Other Reason")){
                    if (db.storeData("insert into students_leavemanagement(registration_no_id,reason,other_reason,date_of_leave,term,year,phone,school_id) values('"+regno+"','"+reason+"','"+other+"','"+date+"','"+term+"','"+year+"','"+phone+"',1)")){
                        alert.showSuccess("Leave record for student with reg no "+regno+" has been recorded");
                        displayleaveData(regno,term);
                        try{
                            GeneratePdf pdf = new GeneratePdf();
                            pdf.generateLeaveReceipt(regno,grade,stream,name,reason,other,balance,term,date,time,filepath);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        otherReasonEntry.setText("");
                    }
            }
        }else{
            alert.showError("Student with "+regno+" is already on leave");
        }
    }

    public void loadLeave(){
        String date = LocalDate.now().toString();
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();
        Map<String,Object> selected_row = statementtable.getSelectionModel().getSelectedItem();
        if(selected_row != null){
            Object id = selected_row.get("LeaveId");
            boolean confirmation = alert.showConfirmation("Are you sure you want to update this leave statement ?");
            if(confirmation){
                String id_value = id.toString().trim(); 
                if(db.storeData("update students_leavemanagement set return_date = '"+date+"' where id = '"+id_value+"'")){
                    alert.showSuccess("Selected leave statement has been successfully updated");
                    displayleaveData(regno,term);
                }
            }
        }  
    }


    public void search(String column,String regno,String term){
        DBConnection db = new DBConnection();
        String search_e = search_entry.getText();
        List<Map<String,Object>> leavedata = db.fetchAll("select id as LeaveId, reason as Reason, other_reason as OtherReason,date_of_leave as DateOfLeave,return_date as ReturnDate from students_leavemanagement where "+column+" = '"+search_e+"' and registration_no_id = '"+regno+"' and term = '"+term+"'");
        TableViewHelper.populateTable(statementtable,leavedata);
        System.out.println("data: "+ leavedata);
    }
    public void search_leave_data(String regno,String term){
        String criteria = criteriacombo.getValue();

        if(criteria.equals("Reason")){
            search("reason",regno,term);
        
        }else if(criteria.equals("Date of Leave")){
            search("date_of_leave",regno,term);
        }else if(criteria.equals("Other Reason")){
            search("other_reason",regno,term);
        }
    }
    public void leaveStatement(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();

        VBox leavestatementLayout = new VBox();
        leavestatementLayout.setAlignment(Pos.CENTER);
        leavestatementLayout.setPadding(new Insets(10));

        Label top_label = new Label("LEAVE STATEMENT");
        top_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,18));
        leavestatementLayout.getChildren().add(top_label);

        StreamFetch fetch = new StreamFetch(db);
        List <String> grades = fetch.populateGrades();
        

        GridPane combo_layout = new GridPane();
        combo_layout.setHgap(20);
        combo_layout.setPadding(new Insets(10));
        ComboBox<String> grade_combo = new ComboBox<>();
        grade_combo.setPromptText("Select Grade");
        grade_combo.getItems().addAll(grades);

        
        
        ComboBox<String> stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select Stream");
        for (String grade__ : grades){
            List <String> streams = fetch.populateStreams(grade__);
            stream_combo.getItems().clear();
            stream_combo.getItems().addAll(streams);
        }

        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");

        ComboBox<String> reason_combo = new ComboBox<>();
        reason_combo.setPromptText("Select Reason");
        reason_combo.getItems().addAll("School Fees","Other Raeson");
        ComboBox[] combos = {grade_combo,stream_combo,term_combo,reason_combo};
        for(int i = 0; i < 3; i++){
            combos[i].setPrefWidth(170);
        }

        Button filter_btn = new Button("Filter");
        
        combo_layout.add(grade_combo,1,1);
        combo_layout.add(stream_combo,2,1);
        combo_layout.add(term_combo,3,1);
        combo_layout.add(reason_combo,4,1);
        combo_layout.add(filter_btn,5,1);
        

        leavestatementLayout.getChildren().add(combo_layout);

       
        TableView<Map<String,Object>> fullstatementTable = new TableView<>();
        leavestatementLayout.getChildren().add(fullstatementTable);

    
        leaveLayout.getChildren().clear();
        leaveLayout.getChildren().add(leavestatementLayout);
        List<Map<String,Object>> fullstatement = db.fetchAll("select l.registration_no_id as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream, l.reason as Reason,l.other_reason as OtherReason,l.date_of_leave as DateOfLeave,l.return_date as ReturnDate, l.term as Term from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id");
        TableViewHelper.populateTable(fullstatementTable,fullstatement);
        filter_btn.setOnAction(e ->{
            String grade = grade_combo.getValue();
            String stream = stream_combo.getValue();
            String term = term_combo.getValue();
            String reason = reason_combo.getValue();

            List<Map<String,Object>> leave_data = db.fetchAll("select l.registration_no_id as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream, l.reason as Reason,l.other_reason as OtherReason,l.date_of_leave as DateOfLeave,l.return_date as ReturnDate, l.term as Term from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where s.grade = '"+grade+"' and s.stream = '"+stream+"' and l.term = '"+term+"' and reason = '"+reason+"'");
            TableViewHelper.populateTable(fullstatementTable,leave_data);

        });
    }
    public VBox getLayout(){
        return mainLayout;
    }
}