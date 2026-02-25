package view.teacher;

import utils.*;
import model.*;
import pdf.GeneratePdf;

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
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.io.File;


class TeacherRow{
    String reg_no;
    String name;
    RadioButton present;
    RadioButton absent;
    ToggleGroup toggleGroup;
    ComboBox<String> status_combo;


    public TeacherRow(String reg_no,String name){
        this.reg_no = reg_no;
        this.name = name;
    }
    public GridPane getRow(){
        
        present = new RadioButton("Present");
        absent = new RadioButton("Absent");
        toggleGroup = new ToggleGroup();
        present.setToggleGroup(toggleGroup);
        absent.setToggleGroup(toggleGroup);

        status_combo = new ComboBox<>();
        status_combo.getItems().addAll("Normal","Late","On Leave","Sick");
        status_combo.setValue("Normal");

        GridPane layout = new GridPane();
        layout.setHgap(20);
        Label reg_label = new Label(reg_no);
        Label name_label = new Label(name);
        reg_label.setMinWidth(150);
        name_label.setMinWidth(150);
        layout.add(reg_label,0,0);
        layout.add(name_label,2,0);
        layout.add(present,3,0);
        layout.add(absent,4,0);
        
        layout.setStyle("-fx-padding: 3px;");
        GridPane.setHgrow(layout, Priority.ALWAYS);
       
        return layout;
    
    }
    public String getAttendanceStatus(){
        if(present.isSelected()){
            return "Present";
        } else if(absent.isSelected()){
            return "Absent";
        }
        return "Unmarked";

    }

    public void restrictSubmission(String reg_no,LocalDate date,Button button){
        DBConnection db = new DBConnection();
        int reg_count = db.getCount("select count(*) from students_teacherattendance where registration_no = '"+reg_no+"' and date_of_attendance = '"+date+"'");
        if(reg_count > 0){
            present.setDisable(true);
            absent.setDisable(true);
            button.setDisable(true);
        }
    }
}


public class Attendance{
    private TableView<Map<String,Object>> attendance_table;
    private ComboBox<String> criteria_combo;
    private TextField search_entry;


    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private ComboBox<String> term_combo;
    private ComboBox<String> term_combo2;
    

    private List<TeacherRow> teacherRows = new ArrayList<>();
    private VBox container = new VBox();
    private VBox attendance_layout;
    private DBConnection db = new DBConnection();
    private Alerthelper alert = new Alerthelper();
    Date date = new Date();
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
    private String year = yearformat.format(date);

    public Attendance(){
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        Label title = new Label("ATTENDANCE MANAGEMENT");
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,15));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);

        Label title2 = new Label("Teacher Attendance Form");
        title2.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");
        HBox topControls = new HBox(10,new Label("Date:",datePicker),term_combo);
        List<Map<String,Object>> teachers = db.fetchAll("select registration_no as TeacherNo,concat_ws(' ',first_name,second_name,surname) as Name from students_teacherinfo");
        Button markAllPresent = new Button("Mark All Present");
        container.getChildren().add(markAllPresent);
        container.setStyle("-fx-overflow: scroll;");
        for(Map<String,Object> teacher : teachers){
            String reg_no = (String) teacher.get("TeacherNo");
            String name = teacher.get("Name").toString();
            TeacherRow tr = new TeacherRow(reg_no,name);
            teacherRows.add(tr);
            container.getChildren().add(tr.getRow());
            tr.restrictSubmission(reg_no,datePicker.getValue(),markAllPresent);

        }
        markAllPresent.setOnAction(e ->{
            for(TeacherRow tr : teacherRows){
                tr.present.setSelected(true);
            }
        });
        HBox btn_layout = new HBox(10);
        Button submitbtn = new Button("Submit Attendance");
        submitbtn.setOnAction(e ->{
            submitAttendance();
            markAllPresent.setDisable(true);
        });
        term_combo2 = new ComboBox<>();
        term_combo2.setPromptText("Select Term");
        term_combo2.getItems().addAll("All Terms","Term 1","Term 2","Term 3");
        Button generatebtn = new Button("Generate Attendance Report");
        generatebtn.setOnAction(e -> generateTeacherAttendance());
        btn_layout.getChildren().addAll(submitbtn,term_combo2,generatebtn);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox lay = new VBox(15, title2,topControls,new Separator(),scrollPane,btn_layout);
        lay.setPadding(new Insets(0,40,0,0));

        VBox attendance_data_layout = new VBox();
        attendance_data_layout.setPadding(new Insets(0,10,10,20));
        Label attendance_label = new Label("Teachers Attendance Records");
        attendance_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,15));
        GridPane search_panel = new GridPane();
        search_panel.setPadding(new Insets(10));
        search_panel.setHgap(10);

        criteria_combo = new ComboBox<>();
        criteria_combo.setPromptText("--Search By--");
        criteria_combo.getItems().addAll(
            "Teacher No","Status","Date of Attendance"
        );
        search_entry = new TextField();
        Button search_button = new Button("Search");
        search_button.setOnAction(e ->searchAttendanceData());
        search_panel.add(criteria_combo,0,0);
        search_panel.add(search_entry,1,0);
        search_panel.add(search_button,2,0);

        attendance_table = new TableView<>();

        attendance_data_layout.getChildren().addAll(attendance_label,search_panel,attendance_table);
        HBox layout = new HBox();
        layout.getChildren().addAll(lay,attendance_data_layout);


        attendance_layout = new VBox();
        attendance_layout.getChildren().addAll(header,layout);
        teachersAttendanceData();
        
        
    }
    public VBox getLayout(){
        return attendance_layout;
    }

    public void teachersAttendanceData(){
        DBConnection db = new DBConnection();
        List<Map<String,Object>> approved_teachers = db.fetchAll("select a.registration_no as TeacherNo,concat_ws(' ',t.first_name,t.second_name,t.surname) as Name,a.status as Status,a.date_of_attendance as DateofAttendance,a.term as Term, a.time_in as TimeIn from students_teacherattendance a join students_teacherinfo t on t.registration_no = a.registration_no");
        TableViewHelper.populateTable(attendance_table,approved_teachers);

    }
    public void search(String column){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();
        String search = search_entry.getText().trim();
        if(search.equals("")){
            alert.showSuccess("Please type something in the search box");
        } else{
            List<Map<String,Object>> roles_data = db.fetchAll("select a.registration_no as TeacherNo, concat_ws(' ',t.first_name,t.second_name,t.surname) as Name,a.status as Status, a.date_of_attendance as DateOfAttendance, a.time_in as TimeIn, a.term as Term from students_teacherattendance a join students_teacherinfo t on t.registration_no = a.registration_no where "+column+" = '"+search+"' ");
            TableViewHelper.populateTable(attendance_table,roles_data);
        }
    }
    public void searchAttendanceData(){
        Alerthelper alert = new Alerthelper();
        String criteria = criteria_combo.getValue().toString();
        if(criteria.equals("")){
            alert.showError("Please select a search criteria");
        } else{
            if(criteria.equals("Teacher No")){
                search("a.registration_no");
            }else if (criteria.equals("Status")){
                search("a.status");
            } else if(criteria.equals("Date of Attendance")){
                search("a.date_of_attendance");
            }
        }
    }
    public void submitAttendance(){
        LocalDate date = datePicker.getValue();
        Date currentDate = new Date();
        LocalDate now = LocalDate.now();
        // String time_in = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(currentDate);
        String time_in = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        List<String> unmarked = new ArrayList<>();
        List<String> marked = new ArrayList<>();
        String term = term_combo.getValue();
        boolean confirm = alert.showConfirmation("of this attendance records");
        if(term != null){
            if (confirm){
                for(TeacherRow tr : teacherRows){
                    String reg_no = tr.reg_no;
                    String status = tr.getAttendanceStatus();
                    
                    if(status.equals("Unmarked")){
                        unmarked.add(reg_no);
    
                    } else if(status.equals("Present") || status.equals("Absent")){
                        marked.add(reg_no);
                        
                        try{
                            if(status.equals("Present")){
                                db.storeData("insert into students_teacherattendance (registration_no,date_of_attendance,status,term,time_in,year) values ('"+reg_no+"','"+date+"','"+status+"','"+term+"','"+time_in+"','"+year+"')");
                                tr.present.setSelected(false);
                                tr.absent.setSelected(false);
                                tr.present.setDisable(true);
                                tr.absent.setDisable(true);
                            }else if (status.equals("Absent")) {
                                db.storeData("insert into students_teacherattendance (registration_no,date_of_attendance,status,term,year) values ('"+reg_no+"','"+date+"','"+status+"','"+term+"','"+year+"')");
                                tr.present.setSelected(false);
                                tr.absent.setSelected(false);
                                tr.present.setDisable(true);
                                tr.absent.setDisable(true);
                            }
                        } catch (Exception e){
                            e.printStackTrace();   
                        }
                    }
                }
            } else {
                alert.showWarning("Please confirm well before submitting");
            }
        } else {
            alert.showWarning("Please select term");
        }
        if(!unmarked.isEmpty()){
            alert.showWarning("Some teachers were not marked: "+String.join(", ",unmarked));
            teachersAttendanceData();
        } else if(marked.isEmpty()){
            alert.showError("No Attendance data was recorded");
            teachersAttendanceData();
        } else if(!marked.isEmpty()){
            alert.showSuccess("Attendance recorded successfully");
            teachersAttendanceData();
        }
    }

    public void generateTeacherAttendance(){
        String term = term_combo2.getValue();
        if(term == null){
            alert.showWarning("Please select term to proceed");
        }else{
            List<Map<String,Object>> attendance_data = db.fetchAll("select a.registration_no as reg_no,concat_ws(' ',t.first_name,t.second_name,t.surname) as name,a.date_of_attendance as date_of_attendance,a.time_in as time_in,a.status as status from students_teacherattendance a join students_teacherinfo t on t.registration_no = a.registration_no where a.term = '"+term+"' and a.year = '"+year+"'");
      
            File base_dir = new File("ShuleBase Files"+File.separator+"Teachers Attendance Files");
            if(!base_dir.exists()){
                base_dir.mkdirs();
            }
            DirectoryUtil dir = new DirectoryUtil();
            String path = dir.createFileName(base_dir.toString(),term+" teachers attendance report.pdf");
            try{
                boolean confirm = alert.showConfirmation("you want to generate teachers attendance report for"+term);
                if(confirm){
                    GeneratePdf pdf = new GeneratePdf();
                    pdf.generateTeachersAttendanceReport(term,year,attendance_data,path);
                } else {
                    alert.showWarning("Confirm before proceeding");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
