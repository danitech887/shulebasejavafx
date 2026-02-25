package view.student;

import model.DBConnection;
import utils.TableViewHelper;
import utils.Alerthelper;
import charts.AttendanceDashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class PupilAttendance{
    private VBox mainLayout;
    private StackPane attendanceLayout;
    private ComboBox<String> termCombo;
    private String regno;
    private ComboBox<String> reason_combo;
    private TextField otherReasonEntry;
    private TextField phoneEntry;

    private String term;
    private DatePicker approval_date;
    private TableView<Map<String,Object>> statementtable;
    private TextField search_entry;
    private ComboBox<String> criteriacombo;

    private int school_id;

    Date date = new Date();
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
    SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss");
    private String year = yearformat.format(date);
    private String time = timeformat.format(date);

    

    public PupilAttendance(){
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.TOP_CENTER);
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        Label title = new Label("ATTENDANCE MANAGEMENT");
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,24));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);

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
        attendanceLayout = new StackPane();
        attendanceLayout.getChildren().clear();

        DBConnection db = new DBConnection();
        year_combo.setOnAction(e ->{
            String year = year_combo.getValue();
            String term = term_combo.getValue();
            int full_attendance = db.getCount("select count(*) from students_studentattendance where status = 'Present' and term = '"+term+"' and year = '"+year+"'");
            int no_of_students = db.getCount("select count(*) from students_studentinfo");
            int pr = (full_attendance * 100) / no_of_students;

            int male_attendance = db.getCount("select count(s.gender)  from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where a.status = 'Present' and s.gender = 'Male' and a.term = '"+term+"' and a.year = '"+year+"'");
            int male_no = db.getCount("select count(*) from students_studentinfo where gender = 'Male'");
            double present_male = (male_attendance * 100) / male_no;

            int female_attendance = db.getCount("select count(s.gender)  from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where a.status = 'Present' and s.gender = 'Female' and a.term = '"+term+"' and a.year = '"+year+"' ");
            int female_no = db.getCount("select count(*) from students_studentinfo where gender = 'Female'");
            double present_female = (female_attendance * 100) / female_no;

            int absentees = no_of_students - (male_attendance - female_attendance);
        
            check_layout.getChildren().clear();
            check_layout.getChildren().addAll(term_combo,year_combo);

            AttendanceDashboard attendanceDashboard = new AttendanceDashboard(pr,present_male,present_female,absentees,term,year,check_layout);
            attendanceLayout.getChildren().clear();
            attendanceLayout.getChildren().add(attendanceDashboard.getLayout());
        });

        int full_attendance = db.getCount("select count(*) from students_studentattendance where status = 'Present' and year = '"+year+"'");
        int no_of_students = db.getCount("select count(*) from students_studentinfo");
        int pr = (full_attendance * 100) / no_of_students;

    
        int male_attendance = db.getCount("select count(s.gender)  from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where a.status = 'Present' and s.gender = 'Male' and a.year = '"+year+"'");
        int male_no = db.getCount("select count(*) from students_studentinfo where gender = 'Male'");
        double present_male = (male_attendance * 100) / male_no;

        int female_attendance = db.getCount("select count(s.gender)  from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where a.status = 'Present' and s.gender = 'Female' and a.year = '"+year+"' ");
        int female_no = db.getCount("select count(*) from students_studentinfo where gender = 'Female'");
        double present_female = (female_attendance * 100) / female_no;

        int absentees = no_of_students - (male_attendance - female_attendance);
    
        attendanceLayout.getChildren().clear();
        AttendanceDashboard attendanceDashboard = new AttendanceDashboard(pr,present_male,present_female,absentees,null,year,check_layout);
        attendanceLayout.getChildren().add(attendanceDashboard.getLayout());


        GridPane navLayout = new GridPane();
        navLayout.setHgap(30);
        navLayout.setPadding(new Insets(10));
        navLayout.setAlignment(Pos.CENTER);
        Button attendancestatementbtn = new Button("ATTENDANCE STATEMENT");
        attendancestatementbtn.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px;");
        attendancestatementbtn.setOnAction(e -> attendanceStatement());
        Button leavestatementbtn = new Button("LEAVE STATEMENT");
        // leavestatementbtn.setOnAction(e -> leaveStatement());
        leavestatementbtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px;");
        Button fullleavestatementbtn = new Button("FULL LEAVE STATEMENT");
        fullleavestatementbtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 10px;");

        Button[] buttons = {attendancestatementbtn,leavestatementbtn,fullleavestatementbtn};
        for(int i = 0; i < buttons.length; i++){
            buttons[i].setPrefWidth(150);
        }
        navLayout.add(attendancestatementbtn,0,1);
        navLayout.add(leavestatementbtn,1,1);
    
        mainLayout.getChildren().addAll(header,navLayout,attendanceLayout);
    }


  
    private void search(String column,String regno,String term){
        DBConnection db = new DBConnection();
        String search_e = search_entry.getText();
        List<Map<String,Object>> attendancedata = db.fetchAll("select id as reason as Reason, other_reason as OtherReason,date_of_attendance as DateOfattendance,return_date as ReturnDate from attendance_management where "+column+" = '"+search_e+"' and registration_no = '"+regno+"' and term = '"+term+"'");
        TableViewHelper.populateTable(statementtable,attendancedata);
        System.out.println("data: "+ attendancedata);
    }
    private void search_attendance_data(String regno,String term){
        String criteria = criteriacombo.getValue();

        if(criteria.equals("Reason")){
            search("reason",regno,term);
        
        }else if(criteria.equals("Date of attendance")){
            search("date_of_attendance",regno,term);
        }else if(criteria.equals("Other Reason")){
            search("other_reason",regno,term);
        }
    }
    private void attendanceStatement(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();

        VBox attendancestatementLayout = new VBox();
        attendancestatementLayout.setAlignment(Pos.CENTER);
        attendancestatementLayout.setPadding(new Insets(10));

        Label top_label = new Label("ATTENDANCE STATEMENT");
        top_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,18));
        attendancestatementLayout.getChildren().add(top_label);

        GridPane combo_layout = new GridPane();
        combo_layout.setHgap(20);
        combo_layout.setPadding(new Insets(10));
        ComboBox<String> grade_combo = new ComboBox<>();
        grade_combo.setPromptText("Select Grade");
        grade_combo.getItems().addAll("PP1","PP2");
        for(int i = 1; i<10; i++){
            grade_combo.getItems().add("Grade "+i);
        }
        ComboBox<String> stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select Stream");
        stream_combo.getItems().addAll("Stream 1","Stream 2","Stream 3","Stream 4");

        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");


        ComboBox[] combos = {grade_combo,stream_combo,term_combo};
        for(int i = 0; i < 3; i++){
            combos[i].setPrefWidth(170);
        }

        Button filter_btn = new Button("Filter");
        
        combo_layout.add(grade_combo,1,1);
        combo_layout.add(stream_combo,2,1);
        combo_layout.add(term_combo,3,1);
   
        combo_layout.add(filter_btn,4,1);
        

        attendancestatementLayout.getChildren().add(combo_layout);

       
        TableView<Map<String,Object>> fullstatementTable = new TableView<>();
        attendancestatementLayout.getChildren().add(fullstatementTable);

    
        attendanceLayout.getChildren().clear();
        attendanceLayout.getChildren().add(attendancestatementLayout);
        List<Map<String,Object>> fullstatement = db.fetchAll("select a.registration_no_id as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream,a.date_of_attendance as DateOfattendance, a.term as Term from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id");
        TableViewHelper.populateTable(fullstatementTable,fullstatement);
        filter_btn.setOnAction(e ->{
            String grade = grade_combo.getValue();
            String stream = stream_combo.getValue();
            String term = term_combo.getValue();
        

            List<Map<String,Object>> attendance_data = db.fetchAll("select a.registration_no_id as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream,a.date_of_attendance as DateOfattendance, a.term as Term from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where s.grade = '"+grade+"' and s.stream = '"+stream+"' and a.term = '"+term+"'");
            TableViewHelper.populateTable(fullstatementTable,attendance_data);

        });
    }
    public VBox getLayout(){
        return mainLayout;
    }
}