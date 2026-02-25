package charts;
import utils.Alerthelper;
import model.DBConnection;
import model.GenerateMarks;
import pdf.GeneratePdf;
import utils.DirectoryUtil;

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
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;

public class AcademicDashboard {
    private VBox layout;
    private VBox lay;
    private String term;
    private String year;
    private List<Map<String,Object>> exam_data;
    private String exam_type;
    private HBox check_layout;
    private DBConnection db = new DBConnection();


    private Alerthelper alert = new Alerthelper();
    public AcademicDashboard(List<Map<String,Object>> exam_data,String exam_type,String term,String year,HBox check_layout){
        this.term = term;
        this.year = year;
        this.exam_data = exam_data;
        this.check_layout = check_layout;

        this.exam_type = exam_type;
        layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        check_layout.getChildren().add(generateLayout());
        layout.getChildren().clear();
        layout.getChildren().addAll(check_layout,createCard(exam_data));
        HBox chart_layout = new HBox(20);
        // chart_layout.getChildren().addAll(assessmentChart(mpesa,cash),createFeeLineChart(),createFeeBarChart());
        
    }

    private VBox createCard(List<Map<String,Object>> exam_data){ 
        VBox card_layout = new VBox(5);
        card_layout.setAlignment(Pos.TOP_CENTER);
        Label top_label = new Label("Top Students");
        top_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,16)); 
        GridPane cardsBox = new GridPane();
        cardsBox.setHgap(20);
        cardsBox.setVgap(10);
        int col = 0;
        int row = 2;  
        Map<String,List<Map<String,Object>>> gradeMap = new LinkedHashMap<>();
        for(Map<String,Object> row_data : exam_data){
            String grade = row_data.get("grade").toString();
            gradeMap.computeIfAbsent(grade,k -> new ArrayList<>()).add(row_data);
        }
        int colorindex = 0;
        for(Map.Entry<String,List<Map<String,Object>>> entry : gradeMap.entrySet()){
        Color[] colors = {
            Color.CORNFLOWERBLUE,
            Color.LIGHTSEAGREEN,
            Color.ORANGE,
            Color.LIGHTGREEN,
            Color.SALMON,
            Color.MEDIUMPURPLE,
            Color.KHAKI,
            Color.GOLD,
            Color.INDIANRED
        };
            String grade = entry.getKey();
            List<Map<String,Object>> students = entry.getValue();
            lay = new VBox(10);
  
            
            lay.setAlignment(Pos.TOP_LEFT);
            lay.setPrefSize(300,150);
            lay.setPadding(new Insets(20));
            BackgroundFill backgroundfill = new BackgroundFill(
                colors[colorindex % colors.length],new CornerRadii(10),Insets.EMPTY
            );
            lay.setBackground(new Background(backgroundfill));
            colorindex++;
            Label titleLabel = new Label(grade);
            titleLabel.setFont(Font.font("Segoe UI",FontWeight.EXTRA_BOLD,15));
            titleLabel.setAlignment(Pos.TOP_CENTER);
            lay.getChildren().add(titleLabel);
            try {
                for (Map<String, Object> data : students) {

                    String name = data.get("name").toString();
                    String stream = data.get("stream").toString();
                    String mark = data.get("marks").toString();
                    HBox cards = new HBox(5);

                    Label stream_label = new Label(stream);
                    stream_label.setMinWidth(70);

                    stream_label.setTextFill(Color.WHITE);
                    Label namelabel = new Label(name);
                    namelabel.setMinWidth(150);

                    namelabel.setTextFill(Color.WHITE);
                    Label mark_label = new Label(mark);
                    mark_label.setMinWidth(50);

                    Label[] labels = {namelabel, stream_label, mark_label};
                    for (Label label : labels) {
                        label.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 13));

                    }
                    cards.getChildren().addAll(namelabel, stream_label, new Label(mark));
                    lay.getChildren().add(cards);


                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            col ++;
            cardsBox.add(lay,col,row);
            if(col == 3){
                col = 0;
                row++;
            }
        }
        
        card_layout.getChildren().addAll(top_label,cardsBox);
        return card_layout;
    }

    // private PieChart assessmentChart(double ee,double me,double ae,double be){
    //     PieChart piechart = new PieChart(FXCollections.observableArrayList(
    //         new PieChart.Data("Exceeding Expectations",ee),
    //         new PieChart.Data("Meeting",me),
    //         new PieChart.Data("Aproaching Expectation",ae),
    //         new PieCHart.Data("Below Expectation",be)
    //     ));
    //     piechart.setTitle("Payment Modes");

    //     for(PieChart.Data data :piechart.getData()){
    //         Tooltip tooltip = new Tooltip((int)data.getPieValue() + " KES");
    //         Tooltip.install(data.getNode(),tooltip);
    //     }
    //     return piechart;

    // }
    // private double[] monthlyPayments(){
    //     DBConnection db = new DBConnection();
    //     int jan = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-1-1' and date_of_payment <= '2025-1-31'");
    //     int feb = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-2-1' and date_of_payment <= '2025-2-28'");
    //     int mar = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-3-1' and date_of_payment <= '2025-3-31'");
    //     int apr = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-4-1' and date_of_payment >= '2025-4-30'");
    //     int may = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-5-1' and date_of_payment >= '2025-5-31'");

    //     int june = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-6-1' and date_of_payment <= '2025-6-30'");
    //     int july = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-7-1' and date_of_payment <= '2025-7-31'");
    //     int aug = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-8-1' and date_of_payment <= '2025-8-31'");
    //     int sep = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-9-1' and date_of_payment <= '2025-9-30'");
    //     int oct = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-10-1' and date_of_payment <= '2025-10-30'");
    //     int nov = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-11-1' and date_of_payment <= '2025-11-30'");
    //     int dec = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-12-1' and date_of_payment <= '2025-12-31'");

    //     double[] payments = {jan,feb,mar,apr,may,june,july,aug,sep,oct,nov,dec};

    //     return payments;

    // }

    // private LineChart<String,Number> createFeeLineChart(){
    //     CategoryAxis xAxis = new CategoryAxis();
    //     xAxis.setLabel("Month");
    //     NumberAxis yAxis = new NumberAxis();
    //     yAxis.setLabel("KES");

    //     LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

    //     lineChart.setTitle("Monthly Fee Trends");

    //     XYChart.Series<String,Number> series = new XYChart.Series<>();
    //     series.setName("2025");

    //     String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
    //     double[] values = monthlyPayments();

    //     for (int i =0; i < months.length; i++){
    //         XYChart.Data<String,Number> data = new XYChart.Data<>(months[i],values[i]);
    //         series.getData().add(data);

    //         FadeTransition ft = new FadeTransition(Duration.millis(800),data.getNode());
    //         ft.setFromValue(0.0);
    //         ft.setToValue(1.0);
    //         ft.play();
    //     }

    //     lineChart.getData().add(series);
    //     return lineChart;
    // }

    // private BarChart<String,Number> createFeeBarChart(){
    //     CategoryAxis xAxis = new CategoryAxis();
    //     xAxis.setLabel("Term");
    //     NumberAxis yAxis = new NumberAxis();
    //     yAxis.setLabel("KES");

    //     BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
    //     barChart.setTitle("Fee Collection Per Term");

    //     XYChart.Series<String,Number> series = new XYChart.Series<>();
    //     series.setName("2025");

    //     DBConnection db = new DBConnection();
    //     int term_1 = db.getCount("select sum(amount) from fee where term = 'Term 1'");
    //     int term_2 = db.getCount("select sum(amount) from fee where term = 'Term 2'");
    //     int term_3 = db.getCount("select sum(amount) from fee where term = 'Term 3'");
    //     series.getData().add(new XYChart.Data<>("Term 1",term_1));

    //     series.getData().add(new XYChart.Data<>("Term 2",term_2));
    //     series.getData().add(new XYChart.Data<>("Term 3",term_3));

    //     barChart.getData().add(series);
    //     return barChart;
    // }
  
    private HBox generateLayout(){
        HBox layout1 = new HBox(10);
        // layout1.setPadding(new Insets(10));
        
        Button reportforms_btn = new Button("Generate Report Forms");
        reportforms_btn.setOnAction(e -> generateReportForms());
        Button result_btn = new Button("Generate Results Pappers");
        reportforms_btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold");
        result_btn.setStyle(
            "-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold"
        );
        Button view_btn = new Button("Top Students");
        view_btn.setStyle(
            "-fx-background-color: darkred; -fx-text-fill: white; -fx-font-weight: bold"
        );
        // view_btn.setOnAction(e ->{
        //     if(term == null || exam_type == null || year == null){
        //         alert.showWarning("Please select term exam type and year to view top students");
        //     }else {
        //         layout.getChildren().clear();
        //         layout.getChildren().addAll(check_layout,createCard(exam_data));
        //     }
        // });
        layout1.getChildren().addAll(reportforms_btn,result_btn);

        return layout1;
    }
    String[] lower_grades  = {"PP1","PP2","Grade 1","Grade 2","Grade 3"};
    String[] lower_subjects = {"Mathematics","English","Kiswahili","Environmental_Activities","Integrated_Creative"};
    String[] upper_grades = {"Grade 4","Grade 5","Grade 6"};
    String[] upper_subjects = {
        "Mathematics",
        "English",
        "Kiswahili",
        "Science_Technology",
        "SST",
        "CRE",
        "SST_CRE",
        "Agri_Nutrition",
        "Creative_Arts"
    };


    String[] junior_grades = {"Grade 7","Grade 8","Grade 9"};
    String[] junior_subjects = {
        "Mathematics",
        "English",
        "Kiswahili",
        "SST",
        "CRE",
        "SST_CRE",
        "Agri_Nutrition",
        "Creative_Arts",
        "Pretech_Bs_Comps",
        "Integrated_Science"
    };
    GeneratePdf pdf = new GeneratePdf();
    DirectoryUtil dir = new DirectoryUtil();
    public void generateReportForms(){
        try{
            boolean confirm = alert.showConfirmation("you want to generate report forms for "+term+" "+year);
            if(confirm){
                for(String grade : lower_grades){
                    List<Map<String,Object>> regnos = db.fetchAll("select registration_no,stream from marks where grade = '"+grade+"' group by registration_no,grade,stream");
                    for(Map<String,Object> regn : regnos){
                        String regno = regn.get("registration_no").toString();
                        System.out.println(regno);
                        GenerateMarks lower_marks = new GenerateMarks(grade,term,year);
                        Map<String,Object> lower_average =  lower_marks.getLowerAverageExam(regno);
                        String name = lower_average.get("full_name").toString();
                        String stream = lower_average.get("stream").toString();
                        String total_marks = lower_average.get("total_marks").toString();
                        String mean_marks = lower_average.get("mean_marks").toString();
                        String stream_position = lower_average.get("position").toString();
                        String grade_position = lower_average.get("overall_position").toString();
                    
                        List<Map<String,Object>> lower_opener = lower_marks.getLowerOpenerExam(regno);
                        List<Map<String,Object>> lower_mid = lower_marks.getLowerMidExam(regno);
                        List<Map<String,Object>> lower_end = lower_marks.getLowerEndExam(regno);

                        File base_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade+File.separator+stream);
                        if(!base_dir.exists()){
                            base_dir.mkdirs();
                        }
                        String path = dir.createFileName(base_dir.toString(),regno+" report form .pdf");
                        pdf.generateReportForm(regno,name,grade,stream,term,year,lower_subjects,lower_average,lower_opener,lower_mid,lower_end,total_marks,mean_marks,stream_position,grade_position,path);
                        
                    }
                    Map<String,Object> stream_map = !regnos.isEmpty() ? regnos.get(0) : new HashMap<>();
                    String stream = stream_map.getOrDefault("stream","-").toString();
                    File base_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade+File.separator+stream);

                    File report_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade);
                    String full_path = dir.createFileName(report_dir.toString(),grade+" "+stream+" all report forms.pdf");
                    pdf.mergePdf(base_dir.toString(),full_path);
                }
                for(String grade : upper_grades){
                    GenerateMarks upper_marks = new GenerateMarks(grade,term,year);
                    
                    List<Map<String,Object>> regnos = db.fetchAll("select registration_no,stream from marks where grade = '"+grade+"' group by registration_no,grade,stream");
                    for(Map<String,Object> regn : regnos){
                        String regno = regn.get("registration_no").toString();
                        Map<String,Object> upper_average =  upper_marks.getUpperAverageExam(regno);
                        String name = upper_average.get("full_name").toString();
                        String stream = upper_average.get("stream").toString();
                        String total_marks = upper_average.get("total_marks").toString();
                        String mean_marks = upper_average.get("mean_marks").toString();
                        String stream_position = upper_average.get("position").toString();
                        String grade_position = upper_average.get("overall_position").toString();
                        List<Map<String,Object>> upper_opener = upper_marks.getUpperOpenerExam(regno);
                        List<Map<String,Object>> upper_mid = upper_marks.getUpperMidExam(regno);
                        List<Map<String,Object>> upper_end = upper_marks.getUpperEndExam(regno);

                        File base_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade+File.separator+stream);
                        if(!base_dir.exists()){
                            base_dir.mkdirs();
                        }
                        String path = dir.createFileName(base_dir.toString(),regno+" report form .pdf");
                        
                        pdf.generateReportForm(regno,name,grade,stream,term,year,upper_subjects,upper_average,upper_opener,upper_mid,upper_end,total_marks,mean_marks,stream_position,grade_position,path);
                        
                        
                    }
                    Map<String,Object> stream_map = !regnos.isEmpty() ? regnos.get(0) : new HashMap<>();
                    String strm = stream_map.getOrDefault("stream","-").toString();
                    System.out.println("STREAM "+strm);
                    File folder = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade+File.separator+strm);

                    File report_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade);
                    String full_path = dir.createFileName(report_dir.toString(),grade+" "+strm+" all report forms.pdf");
                    pdf.mergePdf(folder.toString(),full_path);
                    
                }
                for(String grade : junior_grades){
                    
                    GenerateMarks Junior_marks = new GenerateMarks(grade,term,year);
                    List<Map<String,Object>> regnos = db.fetchAll("select registration_no,stream from marks where grade = '"+grade+"' group by registration_no,grade,stream");
                    for(Map<String,Object> regn : regnos){
                        String regno = regn.get("registration_no").toString();
                        Map<String,Object> Junior_average =  Junior_marks.getJuniorAverageExam(regno);
                        String name = Junior_average.get("full_name").toString();
                        String stream = Junior_average.get("stream").toString();
                        String total_marks = Junior_average.get("total_marks").toString();
                        String mean_marks = Junior_average.get("mean_marks").toString();
                        String stream_position = Junior_average.get("position").toString();
                        String grade_position = Junior_average.get("overall_position").toString();
                        List<Map<String,Object>> Junior_opener = Junior_marks.getJuniorOpenerExam(regno);
                        List<Map<String,Object>> Junior_mid = Junior_marks.getJuniorMidExam(regno);
                        List<Map<String,Object>> Junior_end = Junior_marks.getJuniorEndExam(regno);

                        File base_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade+File.separator+stream);
                        if(!base_dir.exists()){
                            base_dir.mkdirs();
                        }
                        String path = dir.createFileName(base_dir.toString(),regno+" report form .pdf");
                        
                        pdf.generateReportForm(regno,name,grade,stream,term,year,junior_subjects,Junior_average,Junior_opener,Junior_mid,Junior_end,total_marks,mean_marks,stream_position,grade_position,path);
                        
                    }
                    Map<String,Object> stream_map = !regnos.isEmpty() ? regnos.get(0) : new HashMap<>();
                    String strm = stream_map.getOrDefault("stream","-").toString();
                    File base_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade+File.separator+strm);

                    File report_dir = new File("ShuleBase Files"+File.separator+"Report Forms"+File.separator+grade);
                    String full_path = dir.createFileName(report_dir.toString(),grade+" "+strm+" all report forms.pdf");
                    pdf.mergePdf(base_dir.toString(),full_path);
                }
            }else {
                alert.showWarning("Please confirm before generationg");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public VBox getLayout(){
        return layout;
    }
}
