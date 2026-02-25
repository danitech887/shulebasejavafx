package charts;
import utils.*;
import model.DBConnection;
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
import java.util.List;
import java.util.Map;
public class FeeDashboard {
    private VBox layout;
    private DBConnection db = new DBConnection();
    private String term;
    private String year;
    private int school_id;
    private Alerthelper alert = new Alerthelper();
    public FeeDashboard(double total,double mpesa,double cash,double pending,String term,String year,HBox check_layout){
        this.term = term;
        this.year = year;
        layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        HBox chart_layout = new HBox(20);
        
        check_layout.getChildren().add(generateLayout());
        chart_layout.getChildren().addAll(createPaymentPieChart(mpesa,cash),createFeeLineChart(year),createFeeBarChart(year));
        layout.getChildren().addAll(check_layout,createSurmmaryCards(total,mpesa,cash,pending),chart_layout);
    }
    private HBox createSurmmaryCards(double total,double mpesa,double cash,double pending){
        HBox cardsBox = new HBox(20);
        
        cardsBox.getChildren().addAll(
            createCard("Total Paid",total,"#2ecc71"),
            createCard("Mpesa",mpesa,"#3498db"),
            createCard("Cash",cash,"#f1c40f"),
            createCard("Pending",pending,"#e74c3c")
        );
        
        HBox.setHgrow(cardsBox,Priority.ALWAYS);
        
        return cardsBox;

    }
    private VBox createCard(String title,double amount,String color){
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250,150);
        card.setPadding(new Insets(20));
        card.setBackground(new Background(new BackgroundFill(Color.web(color), new CornerRadii(10),Insets.EMPTY)));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI",FontWeight.BOLD,14));
        titleLabel.setTextFill(Color.WHITE);
        Label valueLabel = new Label("KES "+ String.format("%,.0f",amount));
        valueLabel.setFont(Font.font("Segoe UI",FontWeight.EXTRA_BOLD,18));
        valueLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(titleLabel,valueLabel);

        card.setOnMouseEntered(e -> card.setScaleX(1.05));
        card.setOnMouseExited(e -> card.setScaleX(1));

        return card;
    }
    private PieChart createPaymentPieChart(double mpesa,double cash){
        PieChart piechart = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("Mpesa",mpesa),
            new PieChart.Data("Cash",cash)
        ));
        piechart.setTitle("Payment Modes");

        for(PieChart.Data data :piechart.getData()){
            Tooltip tooltip = new Tooltip((int)data.getPieValue() + " KES");
            Tooltip.install(data.getNode(),tooltip);
        }
        return piechart;

    }
    private double[] monthlyPayments(String year){
        DBConnection db = new DBConnection();
        int jan = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-1-1' and date_of_payment <='"+year+"-1-31'");
        int feb = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-2-1' and date_of_payment <='"+year+"-2-28'");
        int mar = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-3-1' and date_of_payment <='"+year+"-3-31'");
        int apr = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-4-1' and date_of_payment >='"+year+"-4-30'");
        int may = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-5-1' and date_of_payment >='"+year+"-5-31'");

        int june = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-6-1' and date_of_payment <='"+year+"-6-30'");
        int july = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-7-1' and date_of_payment <='"+year+"-7-31'");
        int aug = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-8-1' and date_of_payment <='"+year+"-8-31'");
        int sep = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-9-1' and date_of_payment <='"+year+"-9-30'");
        int oct = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-10-1' and date_of_payment <='"+year+"-10-30'");
        int nov = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-11-1' and date_of_payment <='"+year+"-11-30'");
        int dec = db.getCount("select sum(amount) from students_fee where date_of_payment >='"+year+"-12-1' and date_of_payment <='"+year+"-12-31'");

        double[] payments = {jan,feb,mar,apr,may,june,july,aug,sep,oct,nov,dec};

        return payments;

    }

    private LineChart<String,Number> createFeeLineChart(String year){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("KES");

        LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Monthly Fee Trends");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName(year);

        String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        double[] values = monthlyPayments(year);

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

    private BarChart<String,Number> createFeeBarChart(String year){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Term");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("KES");

        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Fee Collection Per Term");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName(year);

        DBConnection db = new DBConnection();
        int term_1 = db.getCount("select sum(amount) from students_fee where term = 'Term 1' and year = '"+year+"'");
        int term_2 = db.getCount("select sum(amount) from students_fee where term = 'Term 2' and year = '"+year+"'");
        int term_3 = db.getCount("select sum(amount) from students_fee where term = 'Term 3' and year = '"+year+"'");
        series.getData().add(new XYChart.Data<>("Term 1",term_1));

        series.getData().add(new XYChart.Data<>("Term 2",term_2));
        series.getData().add(new XYChart.Data<>("Term 3",term_3));

        barChart.getData().add(series);
        return barChart;
    }
    private  ComboBox<String> filetype_combo;
    private  ComboBox<String> grade_combo;
    private  ComboBox<String> stream_combo;

    private HBox generateLayout(){
        Alerthelper alert = new Alerthelper();
        Button exportBtn = new Button("Generate Fee Report");
        exportBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold");
        exportBtn.setOnAction(e -> generateFeeReport());
        filetype_combo = new ComboBox<>();
        filetype_combo.setPromptText("Select File Type");
        filetype_combo.getItems().addAll("PDF","CSV");
        StreamFetch fetch = new StreamFetch(db);
        List <String> grades = fetch.populateGrades();
        grade_combo = new ComboBox<>();
        grade_combo.setPromptText("Select Grade");
        grade_combo.getItems().add("All Grades");
        stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select stream");
        HBox combo_layout = new HBox(10);
        combo_layout.getChildren().addAll(grade_combo,stream_combo,exportBtn);
    
        grade_combo.getItems().addAll(grades);
    
        for (String grade_ : grades){
            List <String> streams = fetch.populateStreams(grade_);
            stream_combo.getItems().addAll(streams);
        }
        return combo_layout;
    }
    public void generateFeeReport(){
        String grade = grade_combo.getValue();
        String stream = stream_combo.getValue();
        if(grade.equals("All Grades")){
            List<Map<String,Object>> fee_data = db.fetchAll("select f.registration_no_id as reg_no,concat_ws(' ',s.first_name,s.second_name,s.surname) as name,s.grade as grade,s.stream as stream,f.term as term,sum(f.amount) as amount,10000 - sum(f.amount) as balance from students_fee f join students_studentinfo s on s.registration_no = f.registration_no_id where f.term = '"+term+"' and f.year = '"+year+"' group by f.registration_no_id, s.grade,s.stream,f.term,f.year");

            File base_dir = new File("ShuleBase Files"+File.separator+"Fee Reports"+File.separator+grade+File.separator+stream);
            if(!base_dir.exists()){
                base_dir.mkdirs();
            }
            DirectoryUtil dir = new DirectoryUtil();
            String path = dir.createFileName(base_dir.toString(),term+" fee report.pdf");
            boolean confirm = alert.showConfirmation("you want to generate fee report for "+term);
            if(confirm){
                try{
                    GeneratePdf pdf = new GeneratePdf();
                    pdf.generateTermFeeReport(grade,stream,term,year,path,fee_data);
                    alert.showSuccess("Fee report generated and saevd as: "+path);
                } catch(Exception e){
                    e.printStackTrace();

                }
            }

        }else {
            if(grade == null || stream == null || term == null || year == null){
                alert.showWarning("Please select term year grade stream to proceed");
            }else{
                List<Map<String,Object>> fee_data = db.fetchAll("select f.registration_no_id as reg_no,concat_ws(' ',s.first_name,s.second_name,s.surname) as name,f.term as term, sum(f.amount) as amount,10000 - sum(f.amount) as balance from students_fee f join students_studentinfo s on s.registration_no = f.registration_no_id where s.grade = '"+grade+"' and s.stream = '"+stream+"' and f.term = '"+term+"' group by f.registration_no_id,s.grade,s.stream,f.term");
                File base_dir = new File("ShuleBase Files"+File.separator+"Fee Reports"+File.separator+grade+File.separator+stream);
                if(!base_dir.exists()){
                    base_dir.mkdirs();
                }
                DirectoryUtil dir = new DirectoryUtil();
                String path = dir.createFileName(base_dir.toString(),term+" fee report.pdf");
                try {
                    boolean confirm = alert.showConfirmation("you want to generate report for: "+grade+" "+stream+" "+term);
                    if(confirm){
                        GeneratePdf pdf = new GeneratePdf();
                        pdf.generateGradeFeeReport(grade,stream,term,path,fee_data);
                        alert.showSuccess("Fee report generated and saved as: "+path);
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
