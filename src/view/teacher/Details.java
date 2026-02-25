package view.teacher;
import utils.*;
import model.*;
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
import java.time.LocalDate;

public class Details{
    private TableView<Map<String,Object>> detailstable;
    
    private ComboBox<String> registrationcombo;
    private TextField firstName;
    private TextField secondName;
    private TextField surname;
    private ComboBox<String> gendercombo;
    private DatePicker dateofregistration;
    private TextField phone;
    private TextField username;
    private PasswordField password;
    private PasswordField con_password;
    private TextField email_address;
    private TextArea address;
    private VBox minor_layout;
    

    private ComboBox<String> criteria;
    private TextField search_entry;



    public Details(){
        GridPane layout = new GridPane();

        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        Label title = new Label("PERSONAL DETAILS MANAGEMENT");
        title.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
        title.setStyle("-fx-text-fill: white");
        header.getChildren().add(title);
        
        layout.setPadding(new Insets(5));
        layout.setAlignment(Pos.TOP_CENTER);
       
        
        Pane formlayout = new VBox();
        GridPane form = new GridPane();
        
        form.setVgap(5);
        form.setPadding(new Insets(0,20,10,5));
        form.setAlignment(Pos.TOP_LEFT);

        registrationcombo = new ComboBox<>();
        registrationcombo.setPromptText("Select Reg No");
        for(int i = 1; i < 1000; i++){
            registrationcombo.getItems().add("TCH0"+i);
        }
        firstName = new TextField();
        secondName = new TextField();
        surname = new TextField();
        gendercombo = new ComboBox<>();
        gendercombo.setPromptText("Gender");
        gendercombo.getItems().addAll("Male","Female");

        dateofregistration = new DatePicker();
        phone = new TextField();
        email_address = new TextField();
        username = new PasswordField();
        password = new PasswordField();
        con_password = new PasswordField();
        address = new TextArea();
        address.setPrefWidth(200);
        address.setPrefRowCount(2);

        TextField[] fields = {firstName,secondName,surname,phone,email_address,username,password,con_password};
        for (int i = 0; i < fields.length; i++){
            fields[i].setPrefWidth(200);
        }
        registrationcombo.setPrefWidth(200);
        gendercombo.setPrefWidth(200);
        dateofregistration.setPrefWidth(200);
        

        form.add(new Label("Reg No"),0,0);
        form.add(registrationcombo,1,0);

        form.add(new Label("First Name"),0,1);
        form.add(firstName,1,1);

        form.add(new Label("Second Name"),0,2);
        form.add(secondName,1,2);

        form.add(new Label("Surname"),0,3);
        form.add(surname,1,3);

        form.add(new Label("Gender"),0,4);
        form.add(gendercombo,1,4);

        form.add(new Label("Date of Registration"),0,5);
        form.add(dateofregistration,1,5);

        form.add(new Label("Phone"),0,6);
        form.add(phone, 1,6);

        form.add(new Label("Email Address"),0,7);
        form.add(email_address,1,7);


        form.add(new Label("username"),0,8);
        form.add(username,1,8);

        form.add(new Label("Password"),0,9);
        form.add(password,1,9);

        form.add(new Label("Confirm Password"),0,10);
        form.add(con_password,1,10);

        form.add(new Label("Address"),0,11);
        form.add(address,1,11);


        Button registerbtn = new Button("Register");
        registerbtn.setPrefWidth(200);
        registerbtn.setOnAction(e -> register());
        registerbtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 14px");
        
        form.add(registerbtn,1,12);
        formlayout.getChildren().addAll(form);
        Pane detailslayout = new VBox(5);
        detailslayout.setMaxWidth(Double.MAX_VALUE);

        DBConnection db = new DBConnection();
        detailstable = new TableView<>();

        List<Map<String,Object>> data = db.fetchAll("select registration_no as RegNo,concat_ws(' ',first_name,second_name,surname) as Name,gender as Gender, address as Address from students_teacherinfo");
        TableViewHelper.populateTable(detailstable,data);

        detailstable.setOnMouseClicked(event -> loadData());

//        Image icon = new Image(getClass().getResourceAsStream("/resources/icons/search.png"));
//        ImageView imageview = new ImageView(icon);

        GridPane searchPane = new GridPane();
        searchPane.setHgap(10);
        
        criteria = new ComboBox<>();
        criteria.setPromptText("Select Criteria");
        criteria.getItems().addAll("Reg No","First Name","Second Name","Surname","Grade","Stream");
        search_entry = new TextField();
        Button search_btn = new Button("Search");
//        search_btn.setGraphic(imageview);
        search_btn.setOnAction(e ->search_data());
//        imageview.setFitWidth(16);
//        imageview.setFitHeight(16);
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
        
    

        
        minor_layout = new VBox(10);
        
        minor_layout.getChildren().addAll(header,layout);
        
        layout.add(formlayout,0,0);
        layout.add(detailslayout,1,0);
    }

        
    
    public VBox getLayout(){
        return minor_layout;
    }
    public void display_data(){
        DBConnection db = new DBConnection();
        List<Map<String,Object>> data = db.fetchAll("select registration_no as RegNo,concat_ws(' ',first_name,second_name,surname) as Name,gender as Gender, address as Address from students_teacherinfo");
        TableViewHelper.populateTable(detailstable,data);
    }
    public boolean validatePassword(){
        String pass = password.getText();
        String con_pass = con_password.getText();

        if(pass.equals(con_pass)){
            return true;
        } else {
            return false;
        }

    }
    public void validateEmail(){
        String email = email_address.getText();

        if(email.equals("")){

        }
    }
    public int checkUser(){
        DBConnection db = new DBConnection();
        String user = username.getText();

        int user_exist = db.getCount("select count(*) from login_details where username = '"+user+"'");

        return user_exist;


    }
    public void register(){

        String reg_no = registrationcombo.getValue();
        String first_name = firstName.getText();
        String second_name = secondName.getText();
        String srname = surname.getText();
        String gender = gendercombo.getValue();
       
        String doa = dateofregistration.getValue().toString();
        String phon = phone.getText();
        String email = email_address.getText();
        String user = username.getText();
        String pass = con_password.getText();
        String add = address.getText();
        Alerthelper alert = new Alerthelper();
        
        DBConnection db = new DBConnection();
        TextField[] entries = {firstName,secondName,surname,phone,email_address,username,password,con_password};
        for(int i = 0; i< entries.length; i++){
            if(entries[i].getText() == null || entries[i].getText() == ""){
                alert.showError(entries[i] +" is empty please fill in to continue");
            }
        }
        boolean equals = validatePassword();
        int user_exist = checkUser();
        if(user_exist > 0){
            alert.showError("Username in user");
        } else if(equals){
            String query = "insert into students_teacherinfo (registration_no,first_name,second_name,surname,gender,date_of_registration,phone,email,address,school_id) values('"+ reg_no +"','"+ first_name +"','"+ second_name +"','"+ srname +"','"+ gender +"','"+ doa +"','"+ phon +"','"+email+"','"+ add +"',1)";

            if (db.storeData(query)){
                db.storeData("create user '"+user+"'@'%' identified by '"+pass+"'");
                db.storeData("insert into students_login_details(username,type_of_user,teacher_no,email)values('"+user+"','Teacher','"+reg_no+"','"+email+"')");
                db.storeData("grant all privileges on pupil.* to '"+user+"'@'%'");
                db.storeData("grant create, update,alter on mysql.* to '"+user+"'@'%'");

                alert.showSuccess("'"+ first_name +"' registered successfully");
                address.setText("");
                for (int i = 0; i <entries.length; i++){
                    entries[i].setText("");
                }
                display_data();
            }else{
                alert.showError("Failed to register");
            }
        }else if(!equals){
            alert.showError("Password does not match");
        }
        
    }

  
    public void search(String column_name){
            String entry = search_entry.getText();
            DBConnection db = new DBConnection();
            List<Map<String,Object>> data =db.fetchAll("select registration_no as RegNo,concat_ws(' ',first_name,second_name,surname) as Name,gender as Gender, address as Address from students_teacherinfo where "+column_name+" = '"+entry+"'");
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
                Map<String,Object> row = db.fetchOne("select registration_no,first_name,second_name,surname,gender,date_of_registration,phone,email,address from students_teacherinfo where registration_no = '"+ regNo +"'");
                registrationcombo.setValue(row.get("registration_no").toString());
                firstName.setText(row.get("first_name").toString());
                secondName.setText(row.get("second_name").toString());
                surname.setText(row.get("surname").toString());
                gendercombo.setValue(row.get("gender").toString());
                String doaString = row.get("date_of_registration").toString();
                LocalDate doa = LocalDate.parse(doaString);
                System.out.println(doa);

                dateofregistration.setValue(doa);
                phone.setText(row.get("phone").toString());
                email_address.setText(row.get("email").toString());
                address.setText(row.get("address").toString());



                // if (row != null){
                //     List<Map<String,Object>> data = new ArrayList<>();
                //     data.add(row);
                //     Alerthelper alert = new Alerthelper();
                //     alert.showSuccess("data: "+ row);

                // }
            }
        }
    }

    public void updateDetails(){
        String reg_no = registrationcombo.getValue();
        String first_name = firstName.getText();
        String second_name = secondName.getText();
        String srname = surname.getText();
        String gender = gendercombo.getValue();
 
        String doa = dateofregistration.getValue().toString();
        String phon = phone.getText();
        String email = email_address.getText();
        String add = address.getText();
        Alerthelper alert = new Alerthelper();

        DBConnection db = new DBConnection();

        String query = "update students_teacherinfo set first_name = '"+first_name+"' ,second_name = '"+second_name+"',surname = '"+srname+"',gender = '"+gender+"',date_of_registration = '"+doa+"',phone = '"+phon+"',email = '"+email+"',address = '"+add+"' where registration_no = '"+reg_no+"'";

        if (db.storeData(query)){
            alert.showSuccess("Details for "+first_name+ " has been successfully updated !");
            display_data();
            TextField[] entries = {firstName,secondName,surname,phone,email_address};
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
                if(db.delete("delete from students_teacherinfo where registration_no = '"+regno+"'")){
                    alert.showSuccess("Records for teacher with reg no: "+regno+" has been deleted");
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
                if(password.equals("daenicel"))
                if(db.delete("delete from students_teacherinfo")){
                    alert.showSuccess("All student data has been deleted");
                }else{
                    alert.showError("Failed to delete data");
                }
            });
        }
    }
}