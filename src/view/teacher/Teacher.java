package view.teacher;
import view.teacher.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Manage{
    private StackPane mainContent;

    public Manage(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void loadView(){
        Details details_view = new Details();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(details_view.getLayout());
    }
}
class ManageRoles{
    private StackPane mainContent;
    private int school_id;
    public ManageRoles(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void loadView(){
        Roles roles = new Roles();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(roles.getLayout());
    }
}
class ManageAttendance{
    private StackPane mainContent;

    public ManageAttendance(StackPane mainContent){
        this.mainContent = mainContent;
    }
    public void loadView(){
        Attendance attendance = new Attendance();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(attendance.getLayout());
    }
}
class Progress{
    private StackPane mainContent;
    private int school_id;
    public Progress(StackPane mainContent){
        this.mainContent = mainContent;

    }
    public void loadView(){
        TeachingTracking progress = new TeachingTracking();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(progress.getLayout());
    }
}

public class Teacher{
    private VBox teacher_layout;
    private StackPane mainlayout;
    private int school_id;



    public Teacher(){
        teacher_layout = new VBox(10);

        mainlayout = new StackPane();
        mainlayout.setAlignment(Pos.CENTER);
//        Manage dets = new Manage(mainlayout);
//        dets.loadView();

        HBox sidebar = new HBox(5);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color:rgb(33, 17, 0)");
        // sidebar.setPrefWidth(150);

        String[] buttonnames = {"Details","Attendance","Roles","Teaching Tracking"};
        Button[] navButtons = new Button[buttonnames.length];


        for (int i = 0; i < buttonnames.length; i++){
            Button btn = new Button(buttonnames[i]);
            btn.setFont(Font.font("Times New Roman",FontWeight.BOLD,13));
            btn.setStyle(
                "-fx-background-color: transparent;-fx-text-fill: white;-fx-padding: 8px;"
            );
        
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setMinWidth(100);

            btn.setOnMouseEntered(e ->{
                btn.setScaleX(1.1);
                btn.setScaleY(1.1);
                btn.setCursor(javafx.scene.Cursor.HAND);
            });
            btn.setOnMouseExited(e ->{
                btn.setScaleX(1.0);
                btn.setScaleY(1.0);
                btn.setCursor(javafx.scene.Cursor.DEFAULT);
            });
            
            final int index = i;
            btn.setOnAction(e -> {
                switch (index) {
                    case 0: Manage manage = new Manage(mainlayout);
                        manage.loadView();
                        break;
                    case 1: ManageAttendance manage_attendance = new ManageAttendance(mainlayout);
                        manage_attendance.loadView();
                        break;
                    case 2: ManageRoles roles = new ManageRoles(mainlayout);
                        roles.loadView();
                        break;
                    case 3: Progress progress = new Progress(mainlayout);
                    progress.loadView();
                    break;
                }
            });

            navButtons[i] = btn;
            sidebar.getChildren().add(btn);
        }


        teacher_layout.getChildren().addAll(sidebar,mainlayout);
    }
    public VBox getLayout(){
        return teacher_layout;
    }

}


