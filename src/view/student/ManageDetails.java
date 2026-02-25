package view.student;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import model.DBConnection;
import utils.TableViewHelper;
import utils.Alerthelper;
import utils.StreamFetch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;




public class ManageDetails {

    private TableView<Map<String,Object>> detailstable;
    private VBox mainlayout;
    private ComboBox<String> registrationcombo;
    private TextField firstName;
    private TextField secondName;
    private TextField surname;
    private ComboBox<String> gendercombo;
    private DatePicker dateofbirth;
    private ComboBox<String> gradecombo;
    private ComboBox<String> streamcombo;
    private DatePicker dateofregistration;
    private TextField parentphone;
    private TextArea address;

    private ComboBox<String> criteria;
    private TextField search_entry;




    public List<String> streamFetch(String grade,DBConnection db){
        List<String> streams = new ArrayList<>();
        List<Map<String, Object>> stream_select = db.fetchAll("select stream from students_gradestream where grade = '"+grade+"'");
        for(Map<String,Object> stream_sel : stream_select){
            streams.add(stream_sel.get("stream").toString());
            System.out.println("Streams: "+streams);
        }
        return streams;

    }


    public ManageDetails(){
        mainlayout = new VBox();
        mainlayout.setAlignment(Pos.CENTER);
        GridPane layout = new GridPane();

        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        Label title = new Label("PERSONAL DETAILS PANEL");
        title.setFont(Font.font("Arial Black",FontWeight.BOLD,24));
        title.setStyle("-fx-text-fill: white");
        header.getChildren().add(title);
        
        layout.setPadding(new Insets(5));
        layout.setAlignment(Pos.TOP_CENTER);
       
        
        Pane formlayout = new VBox();
        GridPane form = new GridPane();
        
        form.setVgap(5);
        form.setPadding(new Insets(0,20,10,5));
        form.setAlignment(Pos.TOP_LEFT);
        DBConnection conn = new DBConnection();
        registrationcombo = new ComboBox<>();
        List<Map<String, Object>> reg_fetch = conn.fetchAll("select registration_no from students_studentinfo");
        List<String> regs = new ArrayList<>();
        for (int i = 1; i<=10000; i++){
            regs.add("REG0"+i);
        }
        registrationcombo.getItems().addAll(regs);



        firstName = new TextField();
        secondName = new TextField();
        surname = new TextField();
        gendercombo = new ComboBox<>();
        gendercombo.getItems().addAll("Male","Female");
        dateofbirth = new DatePicker();
        gradecombo = new ComboBox<>();
        gradecombo.setPromptText("Select Grade");


        streamcombo = new ComboBox<>();
        streamcombo.setPromptText("Select Stream");


        DBConnection db =new DBConnection();
        StreamFetch fetch = new StreamFetch(db);


        List<String> grades = fetch.populateGrades();
        gradecombo.getItems().addAll(grades);
        gradecombo.setOnAction(e ->{
            String grade = gradecombo.getValue();

            List<String> streams = fetch.populateStreams(grade);
            streamcombo.getItems().addAll(streams);
        });





        dateofregistration = new DatePicker();
        parentphone = new TextField();
        address = new TextArea();
        address.setPrefWidth(200);
        address.setPrefRowCount(2);

        TextField[] fields = {firstName,secondName,surname,parentphone};
        for (int i = 0; i < fields.length; i++){
            fields[i].setPrefWidth(200);
        }
        ComboBox[] combos = {registrationcombo,gendercombo,gradecombo,streamcombo};
        for (int i = 0; i < combos.length; i++){
            combos[i].setPrefWidth(200);
        }
        DatePicker[] dates  = {dateofbirth,dateofregistration};
        for (int i = 0; i<dates.length;i++){
            dates[i].setPrefWidth(200);
        }

        form.add(new Label("Reg No"),0,0);
        form.add(registrationcombo,1,0);

        form.add(new Label("First Name"),0,1);
        form.add(firstName,1,1);

        form.add(new Label("Second Name"),0,2);
        form.add(secondName,1,2);

        form.add(new Label("Surname"),0,3);
        form.add(surname,1,3);

        form.add(new Label("Date of Birth"),0,4);
        form.add(dateofbirth,1,4);

        form.add(new Label("Gender"),0,5);
        form.add(gendercombo,1,5);

        form.add(new Label("Grade"),0,6);
        form.add(gradecombo,1,6);

        form.add(new Label("Stream"),0,7);
        form.add(streamcombo, 1,7);

        form.add(new Label("Date of Registration"),0,8);
        form.add(dateofregistration,1,8);


        form.add(new Label("Parent Phone"),0,10);
        form.add(parentphone,1,10);

        form.add(new Label("Address"),0,11);
        form.add(address,1,11);

        Button registerbtn = new Button("Register");
        registerbtn.setPrefWidth(200);
        registerbtn.setOnAction(e -> register());
        registerbtn.setStyle("-fx-background-color: red -fx-text-fill: white; -fx-font-size: 14px");
        
        form.add(registerbtn,1,12);
        formlayout.getChildren().addAll(form);
        Pane detailslayout = new VBox(5);
        detailslayout.setMaxWidth(Double.MAX_VALUE);

        DBConnection database = new DBConnection();
        detailstable = new TableView<>();

        List<Map<String,Object>> data = database.fetchAll("select registration_no as RegNo,concat_ws(' ',first_name,second_name,surname) as Name, grade as Grade,stream as Stream,gender as Gender from students_studentinfo");
        TableViewHelper.populateTable(detailstable,data);

        detailstable.setOnMouseClicked(event -> loadData());

//        Image icon = new Image(getClass().getResourceAsStream("/resources/icons/search.png"));

        GridPane searchPane = new GridPane();
        searchPane.setHgap(10);
        
        criteria = new ComboBox<>();
        criteria.setPromptText("Select Criteria");
        criteria.getItems().addAll("Reg No","First Name","Second Name","Surname","Grade","Stream");
        search_entry = new TextField();
        Button search_btn = new Button("Search");
        search_btn.setOnAction(e ->search_data());

        searchPane.add(criteria,0,0);
        searchPane.add(search_entry,1,0);
        searchPane.add(search_btn,2,0);


        GridPane buttonlayout = new GridPane();
        buttonlayout.setPadding(new Insets(0,5,0,5));
        buttonlayout.setHgap(20);
        Button updatebtn = new Button("Update Details");
        updatebtn.setStyle("-fx-background-color: royalblue; -fx-padding: 5px; -fx-margin-right: 5px; -fx-text-fill: white;");
        updatebtn.setOnAction(e ->updateDetails());
        Button deleteonebtn = new Button("Delete One");
        deleteonebtn.setStyle("-fx-background-color: red; -fx-padding: 5px; -fx-margin-right: 5px; -fx-text-fill: white;");
        deleteonebtn.setOnAction(e ->deleteRecord());
        Button deleteall = new Button("Delete All");
        deleteall.setStyle("-fx-background-color: darkred; -fx-padding: 5px; -fx-margin-right: 5px; -fx-text-fill: white;");
        deleteall.setOnAction(e -> deleteRecords());
    
        Button[] buttons = {updatebtn,deleteonebtn,deleteall};
        for(int i = 0; i< buttons.length;i++){
            buttons[i].setFont(Font.font("Times New Roman",14));
            buttons[i].setPrefWidth(120);
            buttonlayout.add(buttons[i],i,0);
        }
        GridPane.setHgrow(buttonlayout,Priority.ALWAYS);
        detailslayout.getChildren().addAll(searchPane,detailstable,buttonlayout);

        
        GridPane.setHgrow(detailslayout,Priority.ALWAYS);
        
        
        mainlayout.getChildren().addAll(header,layout);

        layout.add(formlayout,0,0);
        layout.add(detailslayout,1,0);
        
    }
    public VBox getLayout(){
        return mainlayout;
    }
    public void display_data(){
        DBConnection db = new DBConnection();
        List<Map<String,Object>> data = db.fetchAll("select registration_no as RegNo,concat_ws(' ',first_name,second_name,surname) as Name,grade as Grade, stream as Stream,gender as Gender from students_studentinfo");
        TableViewHelper.populateTable(detailstable,data);
    }
    public void register(){
        String reg_no = registrationcombo.getValue();
        String first_name = firstName.getText();
        String second_name = secondName.getText();
        String srname = surname.getText();
        String gender = gendercombo.getValue();
        String dob = dateofbirth.getValue().toString();
        String grade = gradecombo.getValue();
        String stream = streamcombo.getValue();
        String doa = dateofregistration.getValue().toString();
        String phone = parentphone.getText();
        String add = address.getText();
        Alerthelper alert = new Alerthelper();
        
        DBConnection db = new DBConnection();
        TextField[] entries = {firstName,secondName,surname,parentphone};
        for(int i = 0; i< entries.length; i++){
            if(entries[i].getText() == null || entries[i].getText() == ""){
                alert.showError(entries[i] +" is empty please fill in to continue");
            }
        }

        
        String query = "insert into students_studentinfo (registration_no,first_name,second_name,surname,gender,date_of_birth,grade,stream,date_of_registration,phone,address) values('"+ reg_no +"','"+ first_name +"','"+ second_name +"','"+ srname +"','"+ gender +"','"+ dob +"','"+ grade +"','"+ stream +"','"+ doa +"','"+ phone +"','"+ add +"',1)";

        if (db.storeData(query)){
            alert.showSuccess("'"+ first_name +"' registered successfully");
            address.setText("");
            for (int i = 0; i <entries.length; i++){
                entries[i].setText("");
            }
            display_data();
        }else{
            alert.showError("Failed to register");
        }
        
    }

  
    public void search(String column_name){
            String entry = search_entry.getText();
            DBConnection db = new DBConnection();
            List<Map<String,Object>> data =db.fetchAll("select registration_no as RegNo,concat_ws(' ',first_name,second_name,surname) as Name,grade as Grade,stream as Stream,gender as Gender from students_studentinfo where "+column_name+" = '"+entry+"'");
            // if(data != null){
                // List<Map<String,Object>> data = new ArrayList<>();
                // data.add(row);
                TableViewHelper.populateTable(detailstable,data);
                Alerthelper alert = new Alerthelper();
                if (data.isEmpty()){
                    alert.showError("No record found");
                }
            // }
    }
    public void search_data(){
        String crit = criteria.getValue();
        if (crit.equals("Reg No")){
            search("registration_no");
        }else if(crit.equals("First Name")){
            search("first_name");
        }else if(crit.equals("Second Name")){
            search("second_name");
        }else if(crit.equals("Surname")){
            search("surname");
        }else if(crit.equals("Grade")){
            search("grade");
        }else if(crit.equals("Stream")){
            search("stream");
        }

    }
    String regNo;
    public void loadData(){
        Map<String,Object> selectedRow = detailstable.getSelectionModel().getSelectedItem();
        if(selectedRow != null){
            Object regnovalue = selectedRow.get("RegNo");
            if (regnovalue != null){
                regNo = regnovalue.toString().trim();
                DBConnection db = new DBConnection();
                Map<String,Object> row = db.fetchOne("select registration_no,first_name,second_name,surname,gender,date_of_birth,grade,stream,date_of_registration,phone,address from students_studentinfo where registration_no = '"+ regNo +"'");
                registrationcombo.setValue(row.get("registration_no").toString());
                firstName.setText(row.get("first_name").toString());
                secondName.setText(row.get("second_name").toString());
                surname.setText(row.get("surname").toString());
                gendercombo.setValue(row.get("gender").toString());
                String dobString = row.get("date_of_birth").toString();
                LocalDate dob = LocalDate.parse(dobString);
                dateofbirth.setValue(dob);
                gradecombo.setValue(row.get("grade").toString());
                streamcombo.setValue(row.get("stream").toString());
                String doaString = row.get("date_of_registration").toString();
                LocalDate doa = LocalDate.parse(doaString);

                dateofregistration.setValue(doa);
                parentphone.setText(row.get("phone").toString());
                address.setText(row.get("address").toString());




            }
        }
    }

    public void updateDetails(){
        String reg_no = registrationcombo.getValue();
        String first_name = firstName.getText();
        String second_name = secondName.getText();
        String srname = surname.getText();
        String gender = gendercombo.getValue();
        String dob = dateofbirth.getValue().toString();
        String grade = gradecombo.getValue();
        String stream = streamcombo.getValue();
        String doa = dateofregistration.getValue().toString();
        String phone = parentphone.getText();
        String add = address.getText();
        Alerthelper alert = new Alerthelper();

        DBConnection db = new DBConnection();

        String query = "update students_studentinfo set first_name = '"+first_name+"' ,second_name = '"+second_name+"',surname = '"+srname+"',gender = '"+gender+"',date_of_birth = '"+dob+"',grade = '"+grade+"',stream = '"+stream+"',date_of_registration = '"+doa+"',phone = '"+phone+"',address = '"+add+"' where registration_no = '"+reg_no+"'";

        if (db.storeData(query)){
            alert.showSuccess("Details for "+first_name+ " has been successfully updated !");
            display_data();
            TextField[] entries = {firstName,secondName,surname,parentphone};
            address.setText("");
            for(int i = 0; i < entries.length; i++){
                entries[i].setText("");
            }

        }
    }
    public void deleteRecord(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText("Enter registration number");
        dialog.setContentText("Reg No");
        Optional<String> regNo = dialog.showAndWait();
        boolean confirmation = alert.showConfirmation("Delete record for student with reg no: "+regNo+"?");
        
        if(confirmation){
            regNo.ifPresent(regno -> {
                if(db.delete("delete from students_studentinfo where registration_no = '"+regno+"'")){
                    alert.showSuccess("Records for student reg no: "+regno+" has been deleted");
                    display_data();
                }else{
                    alert.showError("Failed to deleted data");
                }
            });
        }
    }
    public void deleteRecords(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Authentication Required");
        dialog.setHeaderText("Enter password");
        dialog.setContentText("Password");
        Optional<String> pass = dialog.showAndWait();
        boolean confirm = alert.showConfirmation("Are you sure you want to delete all data?");
        if(confirm){
            pass.ifPresent(password ->{
                if(db.delete("delete from students_studentinfo")){
                    alert.showSuccess("All student data has been deleted");
                }else{
                    alert.showError("Failed to delete data");
                }
            });
        }
    }
}




