package utils;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;




public class Settings extends Stage {
    private final Scene mainScene;

    public Settings(Stage ownerStage,Scene mainScene){
        this.mainScene = mainScene;

        initOwner(ownerStage);
        initModality(Modality.WINDOW_MODAL);

        if(!ownerStage.getIcons().isEmpty()){
            getIcons().addAll(ownerStage.getIcons());
        }else{
            getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/logo.png")));
        }
    }

    public void showSettings(){
        Stage settingStage = new Stage();
        settingStage.setTitle("Settings");

        settingStage.initModality(Modality.APPLICATION_MODAL);

        VBox navBar = new VBox(10);
        navBar.setPadding(new Insets(20));
        navBar.setStyle("-fx-background-color: #2c3e50");
        navBar.setPrefWidth(120);

        Button themeButton = new Button("Theme");
        themeButton.setMaxWidth(Double.MAX_VALUE);
        themeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        navBar.getChildren().add(themeButton);

        StackPane contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: #f0f0f0");
        contentArea.setPrefSize(350, 200);

        VBox themeBox = new VBox(15);
        themeBox.setAlignment(Pos.CENTER_LEFT);

        Label themeLabel = new Label("Enable Dark Mode");
        CheckBox darkmodeToggle = new CheckBox("Dark Mode");

        darkmodeToggle.setOnAction(e ->{
            boolean isDark = darkmodeToggle.isSelected();
            applyTheme(isDark);
        });

        themeBox.getChildren().addAll(themeLabel,darkmodeToggle);

        themeButton.setOnAction(e ->{
            contentArea.getChildren().clear();
            contentArea.getChildren().add(themeBox);
        });


        HBox layout = new HBox();
        layout.getChildren().addAll(navBar,contentArea);

        Scene dialogScene = new Scene(layout);
        settingStage.setScene(dialogScene);
        settingStage.setResizable(false);
        settingStage.showAndWait();
    }

    private void applyTheme(boolean isDark){
        mainScene.getStylesheets().clear();
        if(isDark){
            mainScene.getStylesheets().add(getClass().getResource("/resources/css/dark-theme.css").toExternalForm());
        } else {
            mainScene.getStylesheets().add(getClass().getResource("/resources/css/light-theme.css").toExternalForm());
        }
    }
    
}
