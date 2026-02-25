package charts;
import utils.Alerthelper;
import utils.DirectoryUtil;
import model.DBConnection;
import pdf.GeneratePdf;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
public class AttendanceDashboard {
    private VBox layout;
    private String term;
    private String year;
    private DBConnection db = new DBConnection();
    private Alerthelper alert = new Alerthelper();


    public AttendanceDashboard(double full_attendance,double male_attendance,double female_attendance,double absentees,String term,String year,HBox check_layout){
        this.term = term;
        this.year = year;
        layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        HBox chart_layout = new HBox(20);
        check_layout.getChildren().add(generateLayout());
    
        chart_layout.getChildren().addAll(createGradeAttendanceChart(year),createAttendanceLineChart(year),createAttendanceBarChart(year));
        layout.getChildren().addAll(check_layout,createSurmmaryCards(full_attendance,male_attendance,female_attendance,absentees),chart_layout);
    }
    private HBox createSurmmaryCards(double full_attendance,double male_attendance,double female_attendance,double absentees){
        HBox cardsBox = new HBox(20);
        
        cardsBox.getChildren().addAll(
            createCard("Full Attendance",full_attendance,"#2ecc71"),
            createCard("Male Attendance",male_attendance,"#3498db"),
            createCard("Female Attendance",female_attendance,"#f1c40f"),
            createCard("Absentees",absentees,"#e74c3c")
        );
        
        HBox.setHgrow(cardsBox,Priority.ALWAYS);
        
        return cardsBox;

    }
    private VBox createCard(String title,double attendance,String color){
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250,150);
        card.setPadding(new Insets(20));
        card.setBackground(new Background(new BackgroundFill(Color.web(color), new CornerRadii(10),Insets.EMPTY)));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI",FontWeight.BOLD,14));
        titleLabel.setTextFill(Color.WHITE);
        Label valueLabel = new Label(String.format("%,.0f",attendance)+" %");
        valueLabel.setFont(Font.font("Segoe UI",FontWeight.EXTRA_BOLD,18));
        valueLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(titleLabel,valueLabel);

        card.setOnMouseEntered(e -> card.setScaleX(1.05));
        card.setOnMouseExited(e -> card.setScaleX(1));

        return card;
    }

    private PieChart createGradeAttendanceChart(String year){
        DBConnection db = new DBConnection();
        int pp1 = db.getCount("select count(*) from students_studentattendance where grade = 'PP1' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int pp1_no = db.getCount("select count(*) from students_studentinfo where grade = 'PP1'");
        int per_pp1 = (pp1 * 100) /pp1_no;

        int pp2 = db.getCount("select count(*) from students_studentattendance where grade = 'PP2' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int pp2_no = db.getCount("select count(*) from students_studentinfo where grade = 'PP2'");
        int per_pp2 = (pp2 * 100) /pp2_no;

        int grade1 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 1' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade1_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 1'");
        int per_grade1 = (grade1 * 100) /grade1_no;

        int grade2 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 2' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade2_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 2'");
        int per_grade2 = (grade2 * 100) /grade2_no;

        int grade3 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 3' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade3_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 3'");
        int per_grade3 = (grade3 * 100) /grade3_no;

        int grade4 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 4' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade4_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 4'");
        int per_grade4 = (grade4 * 100) /grade4_no;

        int grade5 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 5' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade5_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 5'");
        int per_grade5 = (grade5 * 100) /grade5_no;

        int grade6 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 6' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade6_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 6'");
        int per_grade6 = (grade6 * 100) /grade6_no;

        int grade7 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 7' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade7_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 7'");
        int per_grade7 = (grade7 * 100) /grade7_no;

        int grade8 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 8' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade8_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 8'");
        int per_grade8 = (grade8 * 100) /grade8_no;

        int grade9 = db.getCount("select count(*) from students_studentattendance where grade = 'Grade 9' and status = 'Present' and date_of_attendance = CURDATE() and year = '"+year+"'");
        int grade9_no = db.getCount("select count(*) from students_studentinfo where grade = 'Grade 9'");
        int per_grade9 = (grade9 * 100) /grade9_no;

        int[] grades = {};
        String[] classes = {"PP1","PP2","Grade 1","Grade 2","Grade 3","Grade 4","Grade 5","Grade 6","Grade 7","Grade 8","Grade 9"};
        for(String grade : classes){
            int grade_ = db.getCount("select count(*) from students_studentattendance where grade = '"+grade+"' and status = 'Present'");
        
        }
        PieChart piechart = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("PP1",per_pp1),
            new PieChart.Data("PP2",per_pp2),
            new PieChart.Data("Grade 1",per_grade1),
            new PieChart.Data("Grade 2",per_grade2),
            new PieChart.Data("Grade 3",per_grade3),
            new PieChart.Data("Grade 4",per_grade4),
            new PieChart.Data("Grade 5",per_grade5),
            new PieChart.Data("Grade 6",per_grade6),
            new PieChart.Data("Grade 7",per_grade7),
            new PieChart.Data("Grade 8",per_grade8),
            new PieChart.Data("Grade 9",per_grade9)
            
    
        ));
        piechart.setTitle("Grade Attendance");

        for(PieChart.Data data :piechart.getData()){
            Tooltip tooltip = new Tooltip((int)data.getPieValue() + " %");
            Tooltip.install(data.getNode(),tooltip);
        }
        return piechart;

    }
    
    private double[] monthlyAttendance(String year){
        DBConnection db = new DBConnection();
        int jan = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-1-1' and '"+year+"-1-31'");
        int feb = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-2-1' and '"+year+"-2-28'");
        int mar = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-3-1' and '"+year+"-3-31'");
        int apr = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-4-1' and '"+year+"-4-30'");
        int may = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-5-1' and '"+year+"-5-31'");

        int june = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-6-1' and '"+year+"-6-30'");
        int july = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-7-1' and '"+year+"-7-31'");
        int aug = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-8-1' and '"+year+"-8-31'");
        int sep = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-9-1' and '"+year+"-9-30'");
        int oct = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-10-1' and '"+year+"-10-30'");
        int nov = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-11-1' and '"+year+"-11-30'");
        int dec = db.getCount("select count(*) from students_studentattendance where date_of_attendance between '"+year+"-12-1' and '"+year+"-12-31'");

        double[] attendants = {jan,feb,mar,apr,may,june,july,aug,sep,oct,nov,dec};

        return attendants;

    }

    private LineChart<String,Number> createAttendanceLineChart(String year){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Students");

        LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Monthly Attendance Trends");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName(year);

        String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        double[] values = monthlyAttendance(year);

        for (int i =0; i < months.length; i++){
            XYChart.Data<String,Number> data = new XYChart.Data<>(months[i],values[i]);
            series.getData().add(data);

            FadeTransition ft = new FadeTransition(Duration.millis(800),data.getNode());
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    private BarChart<String,Number> createAttendanceBarChart(String year){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Term");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Students");

        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Attendance Per Term");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName(year);

        DBConnection db = new DBConnection();
        int term_1 = db.getCount("select count(*) from students_studentattendance where term = 'Term 1' and year = '"+year+"' ");
        int term_2 = db.getCount("select count(*) from students_studentattendance where term = 'Term 2' and year = '"+year+"' ");
        int term_3 = db.getCount("select count(*) from students_studentattendance where term = 'Term 3' and year = '"+year+"' ");
        series.getData().add(new XYChart.Data<>("Term 1",term_1));

        series.getData().add(new XYChart.Data<>("Term 2",term_2));
        series.getData().add(new XYChart.Data<>("Term 3",term_3));

        barChart.getData().add(series);
        return barChart;
    }



    private ComboBox<String> grade_combo;
    private ComboBox<String> stream_combo;

    private HBox generateLayout(){
        Alerthelper alert = new Alerthelper();
        HBox check_layout = new HBox(10);
        
        Button generateButton = new Button("Download Report");
        generateButton.setOnAction(e-> generateAttendanceReport());
        generateButton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold");
        grade_combo = new ComboBox<>();
        grade_combo.setPromptText("Select Grade");
        grade_combo.getItems().addAll("All Grades","PP1","PP2");
        for(int i = 1; i<10; i++){
            grade_combo.getItems().add("Grade "+i);
        }
        stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select Stream");
        for(int i = 1; i<=4; i++){
            stream_combo.getItems().add("Stream "+i);
        }
        
        check_layout.getChildren().addAll(grade_combo,stream_combo,generateButton);
        return check_layout;
    }

    public void generateAttendanceReport(){
        String grade = grade_combo.getValue();
        String stream = stream_combo.getValue();
        if(grade.equals("All Grades")){
            List<Map<String,Object>> attendance_data = db.fetchAll("select a.registration_no_id as reg_no,concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,a.date_of_attendance as date_of_attendance,a.time as time,a.status as status from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where a.term = '"+term+"' and a.year = '"+year+"' group by a.registration_no_id, s.grade,s.stream,a.term,a.year,a.date_of_attendance,a.status,a.time");
        
            File base_dir = new File("ShuleBase Files"+File.separator+"Attendance Reports"+File.separator+grade+File.separator+stream);
            if(!base_dir.exists()){
                base_dir.mkdirs();
            }
            DirectoryUtil dir = new DirectoryUtil();
            String path = dir.createFileName(base_dir.toString(),term+" leave report.pdf");
            boolean confirm = alert.showConfirmation("you want to generate leave report for "+term);
            if(confirm){
                try{
                    GeneratePdf pdf = new GeneratePdf();
                    pdf.generateTermAttendanceReport(grade,stream,term,year,path,attendance_data);
                    alert.showSuccess("Fee report generated and saevd as: "+path);
                } catch(Exception e){
                    e.printStackTrace();

                }
            }

        }else {
            if(grade == null || stream == null || term == null || year == null){
                alert.showWarning("Please select term year grade stream to proceed");
            }else{
                List<Map<String,Object>> attendance_data = db.fetchAll("select a.registration_no_id as reg_no,concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,a.date_of_attendance as date_of_attendance,a.time as time,a.status as status from students_studentattendance a join students_studentinfo s on s.registration_no = a.registration_no_id where s.grade = '"+grade+"' and s.stream = '"+stream+"' and a.term = '"+term+"' and a.year = '"+year+"' group by a.registration_no_id, s.grade,s.stream,a.term,a.year,a.date_of_attendance,a.status,a.time");
                for(Map<String,Object> data : attendance_data){
                    System.out.println(data.get("reg_no").toString()+data.get("name"));
                }
                File base_dir = new File("ShuleBase Files"+File.separator+"Attendance Reports"+File.separator+grade+File.separator+stream);
                if(!base_dir.exists()){
                    base_dir.mkdirs();
                }
                DirectoryUtil dir = new DirectoryUtil();
                String path = dir.createFileName(base_dir.toString(),term+" attendance report.pdf");
                try {
                    boolean confirm = alert.showConfirmation("you want to generate leave report for: "+grade+" "+stream+" "+term);
                    if(confirm){
                        GeneratePdf pdf = new GeneratePdf();
                        pdf.generateGradeAttendanceReport(grade,stream,term,path,attendance_data);
                        alert.showSuccess("Attendance report generated and saved as: "+path);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            } 

        }
        
    }


    public VBox getLayout(){
        return layout;
    }
    
}



