package view.student;

import model.DBConnection;
import utils.TableViewHelper;
import utils.Alerthelper;
import charts.AcademicDashboard;

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
import java.util.Date;
import java.text.SimpleDateFormat;

public class AcademicView{
    private VBox mainLayout;
    private StackPane AcademicLayout;
    private ComboBox<String> termCombo;

    private TableView<Map<String,Object>> statementtable;
    private TextField search_entry;
    private ComboBox<String> criteriacombo;
    private DBConnection db = new DBConnection();

    private List<Map<String,Object>> exam_data;

    Date date = new Date();
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
    String year = yearformat.format(date);

    private int school_id;

    

    public AcademicView(){
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.TOP_CENTER);
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        Label title = new Label("ACADEMIC MANAGEMENT");
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,24));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);

        HBox nav_layout = new HBox(10);
        nav_layout.setPadding(new Insets(10));
        Button dashboard_btn = new Button("Dashboard");
        Button performance_btn = new Button("Peformance");
        
        nav_layout.getChildren().addAll(dashboard_btn,performance_btn);
    
        AcademicLayout = new StackPane();
        performance_btn.setOnAction(e ->{
            AcademicLayout.getChildren().clear();
            AcademicStatement();
        });
        
        mainLayout.getChildren().addAll(header,nav_layout,AcademicLayout);
        // AcademicStatement();
        HBox check_layout = new HBox(10);
        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");
        ComboBox<String> year_combo = new ComboBox<>();
        year_combo.setPromptText("Select Year");
        for(int i = 5; i<=9;i++){
            year_combo.getItems().add("202"+i);
        }
        ComboBox<String> exam_type_combo = new ComboBox<>();
        exam_type_combo.setPromptText("Type of Exam");
        exam_type_combo.getItems().addAll("Opener","Mid Term","End Term");
        check_layout.getChildren().addAll(exam_type_combo,year_combo,term_combo);
        
        term_combo.setOnAction(e ->{
            String exam_type = exam_type_combo.getValue();
            String term = term_combo.getValue();
            String year = year_combo.getValue();
            exam_data = db.fetchAll(
                "select * from (select concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,m.mean_marks as marks,m.term as term,m.year as year,m.type_of_exam as exam_type ,row_number() over (partition by s.grade order by m.mean_marks desc) as position from marks m join studentinfo s on s.registration_no = m.registration_no) as ranked where position <= 3 and exam_type = '"+exam_type+"' and term = '"+term+"' and year = '"+year+"'"
            );
            int ee = db.getCount("select count(*) from marks where mean_marks > 80 and term = '"+term+"' and year = '"+year+"'");

            
            check_layout.getChildren().clear();
            check_layout.getChildren().addAll(exam_type_combo,year_combo,term_combo);
            AcademicLayout.getChildren().clear();
            AcademicDashboard dashboard = new AcademicDashboard(exam_data,exam_type,term,year,check_layout);
            AcademicLayout.getChildren().add(dashboard.getLayout());
        });
        exam_data = db.fetchAll(
            "select * from (select concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,m.mean_marks as marks,m.term as term,m.year as year,row_number() over (partition by s.grade order by m.mean_marks desc) as position from marks m join studentinfo s on s.registration_no = m.registration_no) as ranked where position <= 3 and year = '"+year+"'"
        );
        AcademicDashboard dashboard = new AcademicDashboard(exam_data,null,null,year,check_layout);
        AcademicLayout.getChildren().clear();
        AcademicLayout.getChildren().add(dashboard.getLayout());
        dashboard_btn.setOnAction(e ->{
            exam_data = db.fetchAll("select * from (select concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream ,m.mean_marks as marks,m.term,m.year as year,row_number() over (partition by s.grade order by m.mean_marks desc) as position from marks m join studentinfo s on s.registration_no = m.registration_no) as ranked where position <= 3 and year = '"+year+"'");
            check_layout.getChildren().clear();
            check_layout.getChildren().addAll(exam_type_combo,year_combo,term_combo);
            AcademicLayout.getChildren().clear();
            AcademicDashboard  dash = new AcademicDashboard(exam_data,null,null,year,check_layout);
            AcademicLayout.getChildren().add(dash.getLayout());
        });
        
    }


  
    private void search(String column,String regno,String term){
        DBConnection db = new DBConnection();
        String search_e = search_entry.getText();
        List<Map<String,Object>> Academicdata = db.fetchAll("select id as reason as Reason, other_reason as OtherReason,date_of_Academic as DateOfAcademic,return_date as ReturnDate from Academic_management where "+column+" = '"+search_e+"' and registration_no = '"+regno+"' and term = '"+term+"'");
        TableViewHelper.populateTable(statementtable,Academicdata);
        System.out.println("data: "+ Academicdata);
    }
    private void search_Academic_data(String regno,String term){
        String criteria = criteriacombo.getValue();

        if(criteria.equals("Reason")){
            search("reason",regno,term);
        
        }else if(criteria.equals("Date of Academic")){
            search("date_of_Academic",regno,term);
        }else if(criteria.equals("Other Reason")){
            search("other_reason",regno,term);
        }
    }
    private void AcademicStatement(){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();

        VBox AcademicstatementLayout = new VBox();
        AcademicstatementLayout.setAlignment(Pos.CENTER);
        AcademicstatementLayout.setPadding(new Insets(10));

        Label top_label = new Label("ACADEMIC PERFORMANCE");
        top_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,18));
        AcademicstatementLayout.getChildren().add(top_label);

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

        ComboBox<String> exam_type_combo = new ComboBox<>();
        exam_type_combo.setPromptText("Select Exam Type");
        exam_type_combo.getItems().addAll("Opener","Mid Term","End Term");

        ComboBox<String> term_combo = new ComboBox<>();
        term_combo.setPromptText("Select Term");
        term_combo.getItems().addAll("Term 1","Term 2","Term 3");


        ComboBox[] combos = {grade_combo,stream_combo,term_combo,exam_type_combo};
        for(int i = 0; i < 3; i++){
            combos[i].setPrefWidth(170);
        }

        Button filter_btn = new Button("Filter");
        
        combo_layout.add(grade_combo,1,1);
        combo_layout.add(stream_combo,2,1);
        combo_layout.add(term_combo,3,1);
        combo_layout.add(exam_type_combo,4,1);
   
        combo_layout.add(filter_btn,5,1);
        

        AcademicstatementLayout.getChildren().add(combo_layout);

       
        TableView<Map<String,Object>> fullstatementTable = new TableView<>();
        AcademicstatementLayout.getChildren().add(fullstatementTable);

    
        AcademicLayout.getChildren().clear();
        AcademicLayout.getChildren().add(AcademicstatementLayout);
        
        List<Map<String,Object>> fullstatement = db.fetchAll("select m.registration_no as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,s.grade as Grade,s.stream as Stream,m.type_of_exam as TypeOfExam,m.total_marks as TotalMarks, m.mean_marks as MeanMarks from marks m join studentinfo s on s.registration_no = m.registration_no group by m.registration_no,m.total_marks,m.mean_marks, m.grade,m.stream,m.type_of_exam,m.term");
        TableViewHelper.populateTable(fullstatementTable,fullstatement);
        filter_btn.setOnAction(e ->{
            String grade = grade_combo.getValue();
            String stream = stream_combo.getValue();
            String term = term_combo.getValue();
            String exam_type = exam_type_combo.getValue();
            
            if(grade.equals("Grade 1") || grade.equals("Grade 2") || grade.equals("Grade 3")){
                List<Map<String,Object>> Academic_data = db.fetchAll("select m.registration_no as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,m.mathematics as Math,m.english as Eng,m.kiswahili as Kisw,integrated_creative as IntegratedCreative,environmental_activities as EnvActivities, rank() over(partition by m.grade order by m.mean_marks desc) as Position from marks m join students_studentinfo s on s.registration_no = m.registration_no where s.grade = '"+grade+"' and s.stream = '"+stream+"' and m.term = '"+term+"' and m.type_of_exam = '"+exam_type+"'");
                TableViewHelper.populateTable(fullstatementTable,Academic_data);
            } else if(grade.equals("Grade 4") || grade.equals("Grade 5") || grade.equals("Grade 6")){
                List<Map<String,Object>> Academic_data = db.fetchAll("select m.registration_no as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,m.mathematics as Math,m.english as Eng,m.kiswahili as Kisw,m.science_technology as SciTechnology,m.social_studies as SocialStudies, m.cre as CRE,m.sst_cre as SSTCRE, m.creative_arts as CreativeArts,m.agri_nutrition as AgriNutrition, rank() over(partition by m.grade order by m.mean_marks desc) as Position from marks m join studentinfo s on s.registration_no = m.registration_no where m.grade = '"+grade+"' and m.stream = '"+stream+"' and m.term = '"+term+"' and m.type_of_exam = '"+exam_type+"'");
                TableViewHelper.populateTable(fullstatementTable, Academic_data);
            } else if(grade.equals("Grade 7") || grade.equals("Grade 8") || grade.equals("Grade 9")){
                List<Map<String,Object>> Academic_data = db.fetchAll("select m.registration_no as RegNo,concat_ws(' ',s.first_name,s.second_name,s.surname) as Name,m.mathematics as Math,m.english as Eng,m.kiswahili as Kisw,m.integrated_science as IntegratedSci,m.social_studies as SocialStudies, m.cre as CRE,m.sst_cre as SSTCRE, m.creative_arts as CreativeArts,m.agri_nutrition as AgriNutrition, m.pretech_bs_computer as PretechBsComputer , rank() over(partition by m.grade order by m.mean_marks desc) as Position from marks m join studentinfo s on s.registration_no = m.registration_no where m.grade = '"+grade+"' and m.stream = '"+stream+"' and m.term = '"+term+"' and m.type_of_exam = '"+exam_type+"'");
                TableViewHelper.populateTable(fullstatementTable, Academic_data);
            }

        });
    }
    public VBox getLayout(){
        return mainLayout;
    }
}
