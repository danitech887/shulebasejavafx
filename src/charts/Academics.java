package charts;
import utils.Alerthelper;
import model.DBConnection;

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
public class Academics {
    private VBox layout;
    private int school_id;

    public Academics(double total,double mpesa,double cash,double pending){
        layout = new VBox();
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        

        HBox chart_layout = new HBox(20);
        chart_layout.getChildren().addAll(createPaymentPieChart(mpesa,cash),createFeeLineChart(),createFeeBarChart());
        layout.getChildren().addAll(createSurmmaryCards(total,mpesa,cash,pending),chart_layout,createExportButton());
    }
    private HBox createSurmmaryCards(double total,double mpesa,double cash,double pending){
        HBox cardsBox = new HBox(20);
        
        cardsBox.getChildren().addAll(
            createCard("Total Piad",total,"#2ecc71"),
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
    private double[] monthlyPayments(){
        DBConnection db = new DBConnection();
        int jan = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-1-1' and date_of_payment <= '2025-1-31'");
        int feb = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-2-1' and date_of_payment <= '2025-2-28'");
        int mar = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-3-1' and date_of_payment <= '2025-3-31'");
        int apr = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-4-1' and date_of_payment >= '2025-4-30'");
        int may = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-5-1' and date_of_payment >= '2025-5-31'");

        int june = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-6-1' and date_of_payment <= '2025-6-30'");
        int july = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-7-1' and date_of_payment <= '2025-7-31'");
        int aug = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-8-1' and date_of_payment <= '2025-8-31'");
        int sep = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-9-1' and date_of_payment <= '2025-9-30'");
        int oct = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-10-1' and date_of_payment <= '2025-10-30'");
        int nov = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-11-1' and date_of_payment <= '2025-11-30'");
        int dec = db.getCount("select sum(amount) from fee where date_of_payment >= '2025-12-1' and date_of_payment <= '2025-12-31'");

        double[] payments = {jan,feb,mar,apr,may,june,july,aug,sep,oct,nov,dec};

        return payments;

    }

    private LineChart<String,Number> createFeeLineChart(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("KES");

        LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Monthly Fee Trends");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("2025");

        String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        double[] values = monthlyPayments();

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

    private BarChart<String,Number> createFeeBarChart(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Term");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("KES");

        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Fee Collection Per Term");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("2025");

        DBConnection db = new DBConnection();
        int term_1 = db.getCount("select sum(amount) from fee where term = 'Term 1'");
        int term_2 = db.getCount("select sum(amount) from fee where term = 'Term 2'");
        int term_3 = db.getCount("select sum(amount) from fee where term = 'Term 3'");
        series.getData().add(new XYChart.Data<>("Term 1",term_1));

        series.getData().add(new XYChart.Data<>("Term 2",term_2));
        series.getData().add(new XYChart.Data<>("Term 3",term_3));

        barChart.getData().add(series);
        return barChart;
    }

    private Button createExportButton(){
        Alerthelper alert = new Alerthelper();
        Button exportBtn = new Button("Download Report");
        exportBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold");

        exportBtn.setOnAction(e ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
            File file  = fileChooser.showSaveDialog(null);

            if (file != null){
                try(FileWriter writer = new FileWriter(file)){
                    writer.write("Title,Amount\n");
                    writer.write("Total Paod,1020000\n");
                    writer.write("Mpesa,630000\n");
                    writer.write("Cash, 39000\n");
                    writer.write("Pending,250000");
                    alert.showSuccess("Report saved to "+ file.getAbsolutePath());
                } catch (IOException ex){
                    alert.showError("Error saving file: "+ ex.getMessage());
                }
            }

        });
        return exportBtn;
    }
    public VBox getLayout(){
        return layout;
    }
    
}
