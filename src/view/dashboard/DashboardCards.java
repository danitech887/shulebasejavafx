package view.dashboard;

import model.DBConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.net.URL;

public class DashboardCards {
    private VBox layout;
    private GridPane cardgrid;


    public DashboardCards(){
        layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("MAIN DASHBOARD");
        title.setStyle("-fx-text-fill: darkred;");
        title.setFont(Font.font("Times new Roman", FontWeight.EXTRA_BOLD, 20));

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.getChildren().add(title);

        cardgrid = new GridPane();
        cardgrid.setPadding(new Insets(30));
        cardgrid.setHgap(40);
        cardgrid.setVgap(30);
        cardgrid.setAlignment(Pos.CENTER);

        LocalDate today = LocalDate.now();

        DBConnection db = new DBConnection();
        int no_of_students = db.getCount("select count(*) from students_studentinfo");
        int present_students = db.getCount("select count(*) from students_studentattendance  status = 'Present' and date_of_attendance = '"+ today +"'");
        int students_on_leave = db.getCount("select count(*) from students_leavemanagement  return_date = NULL");
        int no_of_teachers = db.getCount("select count(*) from students_teacherinfo");
        int present_teachers = db.getCount("select count(*) from students_teacherattendance  status = 'Present' and date_of_attendance = '"+ today +"'");
        int fee_paid = db.getCount("select sum(amount) from students_fee");
        int fee_balance = (no_of_students * 10000) - fee_paid;

        String[] titles  = {"No of Students","Present Students","Students on Leave","No of Teachers","Present Teachers","Paid Fee","Fee Balance","No of Classes"};
        int[] values = {no_of_students, present_students, students_on_leave, no_of_teachers, present_teachers, fee_paid, fee_balance, 36};
        String[] colors = {"#2ecc71","#3498db","#9b59b6","#f39c12","#3498db","#9b59b6","#f39c12","#3498db"};
        String[] iconpath = {
                "/resources/images/students.png",
                "/resources/images/attendance.png",
                "/resources/images/attendance.png",
                "/resources/images/teacher.png",
                "/resources/images/teach.png",
                "/resources/images/money.png",
                "/resources/images/money.png",
                "/resources/images/home.png",
        };

        for (int i = 0; i < titles.length; i++){
            VBox card = createCard(titles[i], iconpath[i], colors[i], values[i]);
            cardgrid.add(card, i % 4, i / 4);
        }

       layout.getChildren().addAll(header, cardgrid);
       
    }

    public VBox createCard(String title, String iconpath, String color, int value){
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250, 150);
        card.setPadding(new Insets(15));
        card.setBackground(new Background(new BackgroundFill(Paint.valueOf(color), new CornerRadii(12), Insets.EMPTY)));

        // Load icon from classpath; display above the title
        ImageView iconView = null;
        try {
            URL iconUrl = getClass().getResource(iconpath);
            if (iconUrl != null) {
                Image img = new Image(iconUrl.toExternalForm(), 40, 40, true, true);
                iconView = new ImageView(img);
                iconView.setFitWidth(40);
                iconView.setFitHeight(40);
                iconView.setPreserveRatio(true);
                iconView.setSmooth(true);
            }
        } catch (Exception ignored) {
            // If image fails to load, continue without an icon
        }

        Label titlelabel = new Label(title);
        titlelabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titlelabel.setStyle("-fx-text-fill: white;");

        StringBuilder sb = new StringBuilder();
        sb.append(value);
        Label valuelabel = new Label(sb.toString());
        valuelabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 20));
        valuelabel.setStyle("-fx-text-fill: white;");

        if (iconView != null) {
            card.getChildren().addAll(iconView, titlelabel, valuelabel);
        } else {
            card.getChildren().addAll(titlelabel, valuelabel);
        }

        card.setOnMouseEntered(e -> {
            card.setScaleX(1.05);
            card.setScaleY(1.05);
            card.setCursor(javafx.scene.Cursor.HAND);
        });
        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            card.setCursor(javafx.scene.Cursor.DEFAULT);
        });

        return card;
    }

    public VBox getLayout(){
        return layout;
    }
}