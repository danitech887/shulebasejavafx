// package view;
// import view.DashboardCards;

// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Label;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.FontWeight;
// import javafx.scene.text.Font;
// import javafx.stage.Stage;

// public class Dashboard {
//     public void start(Stage stage){
//         BorderPane root = new BorderPane();
//         // To header

//         HBox header = new HBox();
//         header.setPadding(new Insets(20));
//         header.setSpacing(20);
//         header.setStyle("-fx-background-color: #2c3e50");
//         header.setAlignment(Pos.CENTER);
//         Label title = new Label("MAIN DASHBAORD");
//         title.setFont(Font.font("Arial Black",FontWeight.BOLD,15));
//         title.setTextFill(Color.WHITE);
//         DashboardCards cards = new DashboardCards();
//         root.setTop(header);
//         root.setCenter(cards.getLayout());
//         Scene scene = new Scene(root,400,500);
//         stage.setScene(scene);
//         stage.show();
//     }
// }