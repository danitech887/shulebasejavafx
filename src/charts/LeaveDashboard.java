package charts;

import utils.*;
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
public class LeaveDashboard {
    private VBox layout;
    private String term;
    private String year;
    private int school_id;

    private Alerthelper alert = new Alerthelper();
    private DBConnection db = new DBConnection();

    public LeaveDashboard(double total,double male,double female,double on_leave,double school_fees, double other_reason,String term,String year,HBox check_layout){
        this.term = term;
        this.year = year;
        layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        HBox chart_layout = new HBox(20);

        check_layout.getChildren().add(generateLayout());
        chart_layout.getChildren().addAll(createLeavePieChart(male,female),createLeaveReasonPieChart(school_fees,other_reason),createLeaveLineChart(year),createLeaveBarChart(year));
        layout.getChildren().addAll(check_layout,createSurmmaryCards(total,male,female,on_leave),chart_layout);
    }
    private HBox createSurmmaryCards(double total,double male,double female,double on_leave){
        HBox cardsBox = new HBox(20);
        
        cardsBox.getChildren().addAll(
            createCard("Total Leave Outs",total,"#2ecc71"),
            createCard("Male",male,"#3498db"),
            createCard("Female",female,"#f1c40f"),
            createCard("On leave",on_leave,"#e74c3c")
        );
        
        HBox.setHgrow(cardsBox,Priority.ALWAYS);
        
        return cardsBox;

    }
    private VBox createCard(String title,double number,String color){
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250,150);
        card.setPadding(new Insets(20));
        card.setBackground(new Background(new BackgroundFill(Color.web(color), new CornerRadii(10),Insets.EMPTY)));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI",FontWeight.BOLD,14));
        titleLabel.setTextFill(Color.WHITE);
        Label valueLabel = new Label(String.format("%,.0f",number));
        valueLabel.setFont(Font.font("Segoe UI",FontWeight.EXTRA_BOLD,18));
        valueLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(titleLabel,valueLabel);

        card.setOnMouseEntered(e -> card.setScaleX(1.05));
        card.setOnMouseExited(e -> card.setScaleX(1));

        return card;
    }
    private PieChart createLeavePieChart(double male,double female){
        PieChart piechart = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("Male",male),
            new PieChart.Data("Female",female)
        ));
        piechart.setTitle("Leave Gender Analysis");

        for(PieChart.Data data :piechart.getData()){
            Tooltip tooltip = new Tooltip((int)data.getPieValue() + " No of Students");
            Tooltip.install(data.getNode(),tooltip);
        }
        return piechart;

    }
    private PieChart createLeaveReasonPieChart(double school_fees,double other_reason){
        PieChart pieChart = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("School fees",school_fees),
            new PieChart.Data("Other Reason",other_reason)
        ));
        pieChart.setTitle("Leave Reasons");

        for(PieChart.Data data : pieChart.getData()){
            Tooltip tooltip = new Tooltip((int)data.getPieValue() + " No of Students");
            Tooltip.install(data.getNode(),tooltip);
        }

        return pieChart;
    }
    private double[] monthlyLeaveOuts(String year){
        DBConnection db = new DBConnection();
        int jan = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-1-1' and date_of_leave<= '"+year+"-1-31'");
        int feb = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-2-1' and date_of_leave<= '"+year+"-2-28'");
        int mar = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-3-1' and date_of_leave<= '"+year+"-3-31'");
        int apr = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-4-1' and date_of_leave>= '"+year+"-4-30'");
        int may = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-5-1' and date_of_leave>= '"+year+"-5-31'");

        int june = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-6-1' and date_of_leave<= '"+year+"-6-30'");
        int july = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-7-1' and date_of_leave<= '"+year+"-7-31'");
        int aug = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-8-1' and date_of_leave<= '"+year+"-8-31'");
        int sep = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-9-1' and date_of_leave<= '"+year+"-9-30'");
        int oct = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-10-1' and date_of_leave <= '"+year+"-10-30'");
        int nov = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-11-1' and date_of_leave <= '"+year+"-11-30'");
        int dec = db.getCount("select count(*) from students_leavemanagement where date_of_leave >= '"+year+"-12-1' and date_of_leave <= '"+year+"-12-31'");

        double[] leave_outs = {jan,feb,mar,apr,may,june,july,aug,sep,oct,nov,dec};

        return leave_outs;

    }

    private LineChart<String,Number> createLeaveLineChart(String year){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("No of Students");

        LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Monthly Leave Trends");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("2025");

        String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        double[] values = monthlyLeaveOuts(year);

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

    private BarChart<String,Number> createLeaveBarChart(String year){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Term");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("No of Students");

        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Leave Outs Per Term");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName(year);

        DBConnection db = new DBConnection();
        int term_1 = db.getCount("select count(*) from students_leavemanagement where term = 'Term 1' and year = '"+year+"'");
        int term_2 = db.getCount("select count(*) from students_leavemanagement where term = 'Term 2' and year = '"+year+"'");
        int term_3 = db.getCount("select count(*) from students_leavemanagement where term = 'Term 3' and year = '"+year+"'");
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
        generateButton.setOnAction(e-> generateLeaveReport());
        generateButton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold");
        grade_combo = new ComboBox<>();
        grade_combo.setPromptText("Select Grade");
        StreamFetch fetch = new StreamFetch(db);
        List<String> grades = fetch.populateGrades();
        grade_combo.getItems().add("All Grades");
        grade_combo.getItems().addAll(grades);

        
        stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select Stream");
        for (String grade : grades){
            List streams = fetch.populateStreams(grade);
            stream_combo.getItems().clear();
            stream_combo.getItems().addAll(streams);
        }
        
        check_layout.getChildren().addAll(grade_combo,stream_combo,generateButton);
        return check_layout;
    }

    public void generateLeaveReport(){
        String grade = grade_combo.getValue();
        String stream = stream_combo.getValue();
        if(grade.equals("All Grades")){
            List<Map<String,Object>> leave_data = db.fetchAll("select l.registration_no_id as reg_no,concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,l.reason as reason,l.other_reason as other_reason,l.date_of_leave as date_of_leave,l.time as time,l.return_date as return_date from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where l.term = '"+term+"' and l.year = '"+year+"' group by l.registration_no_id, s.grade,s.stream,l.term,l.year,l.date_of_leave,l.return_date,l.time,l.reason,l.other_reason");
            

            File base_dir = new File("ShuleBase Files"+File.separator+"Leave Reports"+File.separator+grade+File.separator+stream);
            if(!base_dir.exists()){
                base_dir.mkdirs();
            }
            DirectoryUtil dir = new DirectoryUtil();
            String path = dir.createFileName(base_dir.toString(),term+" leave report.pdf");
            boolean confirm = alert.showConfirmation("you want to generate leave report for "+term);
            if(confirm){
                try{
                    GeneratePdf pdf = new GeneratePdf();
                    pdf.generateTermLeaveReport(grade,stream,term,year,path,leave_data);
                    alert.showSuccess("Fee report generated and saevd as: "+path);
                } catch(Exception e){
                    e.printStackTrace();

                }
            }

        }else {
            if(grade == null || stream == null || term == null || year == null){
                alert.showWarning("Please select term year grade stream to proceed");
            }else{
                List<Map<String,Object>> leave_data = db.fetchAll("select l.registration_no_id as reg_no,concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,l.reason as reason,l.other_reason as other_reason,l.date_of_leave as date_of_leave,l.time as time,l.return_date as return_date from students_leavemanagement l join students_studentinfo s on s.registration_no = l.registration_no_id where s.grade = '"+grade+"' and s.stream = '"+stream+"' and l.term = '"+term+"' and l.year = '"+year+"' group by l.registration_no_id, s.grade,s.stream,l.term,l.year,l.date_of_leave,l.return_date,l.time,l.reason,l.other_reason");
                for(Map<String,Object> data : leave_data){
                    System.out.println(data.get("reg_no").toString()+data.get("name"));
                }
                File base_dir = new File("ShuleBase Files"+File.separator+"Leave Reports"+File.separator+grade+File.separator+stream);
                if(!base_dir.exists()){
                    base_dir.mkdirs();
                }
                DirectoryUtil dir = new DirectoryUtil();
                String path = dir.createFileName(base_dir.toString(),term+" leave report.pdf");
                try {
                    boolean confirm = alert.showConfirmation("you want to generate leave report for: "+grade+" "+stream+" "+term);
                    if(confirm){
                        GeneratePdf pdf = new GeneratePdf();
                        pdf.generateGradeLeaveReport(grade,stream,term,path,leave_data);
                        alert.showSuccess("Leave report generated and saved as: "+path);
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
