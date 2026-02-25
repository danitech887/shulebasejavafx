package application;
import view.student.*;
import view.teacher.*;
import view.dashboard.*;
import model.DBConnection;
import utils.Settings;
import utils.ImageLoader;
import utils.DirectoryUtil;
import controller.LoginView;

import javafx.scene.Scene;

import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color;

class DashboardPage {

    private final StackPane mainContent;
    public DashboardPage(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void load(){
        DashboardCards cards = new DashboardCards();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(cards.getLayout());
    }
}

class Manage{
    private final StackPane mainContent;
    public Manage(StackPane mainContent){
        this.mainContent = mainContent;
    }

    public void load(){
        ManageDetails details = new ManageDetails();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(details.getLayout());
    }
}

class Fee {
    private final StackPane mainContent;
    public Fee(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void load(){
        ManageFee managefee = new ManageFee();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(managefee.getLayout());
    }
}

class Leave {
    private final StackPane mainContent;
    public Leave(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void load(){
        ManageLeave manageleave = new ManageLeave();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(manageleave.getLayout());
    }
}

class Attendance {
    private final StackPane mainContent;
    public Attendance(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void load(){
        PupilAttendance students_studentattendance = new PupilAttendance();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(students_studentattendance.getLayout());
    }
}

// class Academics {
//     private final StackPane mainContent;
//     public Academics(StackPane mainContent){
//         this.mainContent = mainContent;
//     }
//     public void load(){
//         AcademicView academic = new AcademicView();
//         mainContent.getChildren().clear();
//         mainContent.getChildren().add(academic.getLayout());
//     }
// }

class Teachers{
    private final StackPane mainContent;
    public Teachers(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void load(){
        Teacher teacher = new Teacher();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(teacher.getLayout());
    }
}

public class Main extends Application {
    private VBox sidebar;
    private StackPane mainContent;
    private DBConnection db;

    private void exit(Stage primaryStage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("You're about to exit");
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            primaryStage.close();
        }
    }

    @Override
    public void start (Stage primaryStage){
        // Show login dialog and connect to DB first
        LoginView loginView = new LoginView();
        DBConnection conn = loginView.showAndWaitForConnection();
        if (conn == null || !conn.isConnected()) {
            primaryStage.close();
            return;
        }

        this.db = conn;





        // Load logo via ImageLoader (classpath resource)
        Image appLogo = ImageLoader.load("/resources/images/logo.png");
        if (appLogo != null) {
            primaryStage.getIcons().add(appLogo);
        }

        // HEADER
        HBox header = buildHeader(appLogo);

        mainContent = new StackPane();
        mainContent.setPadding(new Insets(20));

        Label welcomelabel = new Label("WELCOME TO: \n STUDENT MANAGEMENT SYSTEM..");
        welcomelabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 50));
        welcomelabel.setTextFill(Color.BLUE);
        welcomelabel.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.seconds(5), welcomelabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        mainContent.getChildren().addAll(welcomelabel);
        

        // navigation..
        sidebar = buildSidebar(primaryStage);


        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(mainContent);
        root.setLeft(sidebar);

        Scene scene = new Scene(root, 1000, 650);

        primaryStage.setTitle("ShuleBase");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox buildHeader(Image appLogo) {
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: rgb(154, 139, 148);");

        // Left: logo
        ImageView logoView = null;
        if (appLogo != null) {
            logoView = new ImageView(appLogo);
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
        }

        HBox leftbox = new HBox();
        leftbox.setAlignment(Pos.CENTER_LEFT);
        leftbox.setPadding(new Insets(10));

        if (logoView != null) {
            leftbox.getChildren().add(logoView);
        } else {
            // Fallback placeholder if image not found
            Label placeholder = new Label("LOGO");
            placeholder.setTextFill(Color.WHITE);
            placeholder.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            leftbox.getChildren().add(placeholder);
        }

        // Center: title
        Label title = new Label("STUDENTS MANAGEMENT SYSTEM");
        title.setStyle("-fx-text-fill: rgb(243, 245, 247);");
        title.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 40));

        HBox centerbox = new HBox(title);
        centerbox.setAlignment(Pos.CENTER);
        centerbox.setPadding(new Insets(10));
        HBox.setHgrow(centerbox, Priority.ALWAYS);

        header.getChildren().addAll(leftbox, centerbox);
        return header;
    }

    private VBox buildSidebar(Stage primaryStage) {
        VBox bar = new VBox(14);
        bar.setPadding(new Insets(20));
        bar.setStyle("-fx-background-color: #2c3e50");
        bar.setPrefWidth(180);

        String[] buttonnames = {"Dashboard","Manage Details","Fee","Leave","Attendance","Teacher","Settings","Log Out"};
        Button[] navButtons = new Button[buttonnames.length];

        for (int i = 0; i < buttonnames.length; i++){
            Button btn = new Button(buttonnames[i]);
            btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10px;");
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setMinWidth(160);

            btn.setOnMouseEntered(e ->{
                btn.setScaleX(1.05);
                btn.setScaleY(1.05);
                btn.setCursor(javafx.scene.Cursor.HAND);
            });
            btn.setOnMouseExited(e ->{
                btn.setScaleX(1.0);
                btn.setScaleY(1.0);
                btn.setCursor(javafx.scene.Cursor.DEFAULT);
            });

            final int index = i;
            btn.setOnAction(e -> handleNavigation(index, primaryStage));

            navButtons[i] = btn;
            bar.getChildren().add(btn);
        }

        return bar;
    }

    private void handleNavigation(int index, Stage primaryStage) {
        switch (index) {
            case 0: {
                DashboardPage dashboardPage = new DashboardPage(mainContent);
                dashboardPage.load();
                break;
            }
            case 1: {
                Manage details = new Manage(mainContent);
                details.load();
                break;
            }
            case 2: {
                Fee fee = new Fee(mainContent);
                fee.load();
                break;
            }
            case 3: {
                Leave leave = new Leave(mainContent);
                leave.load();
                break;
            }
            case 4: {
                Attendance attendance = new Attendance(mainContent);
                attendance.load();
                break;
            }
            
            case 5: {
                Teachers teachers = new Teachers(mainContent);
                teachers.load();
                break;
            }
            case 6: {
                // Ensure app directories exist before showing settings if needed
                DirectoryUtil create_dirs = new DirectoryUtil();
                create_dirs.createAllFolders();

                Scene currentScene = primaryStage.getScene();
                Settings settings = new Settings(primaryStage, currentScene);
                settings.showSettings();
                break;
            }
            case 7: {
                exit(primaryStage);
                break;
            }
            default:
                break;
        }

    }

    public static void main(String[] args){
        launch(args);
    }
}