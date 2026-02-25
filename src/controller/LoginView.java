package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.DBConnection;
import javafx.stage.Modality;
import javafx.scene.Scene;
import utils.Alerthelper;
import controller.PasswordEncrypter;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.concurrent.Task;
import utils.ImageLoader;

import java.util.Map;

public class LoginView extends Stage {
    private VBox root;
    private TextField usernamefield;
    private PasswordField passwordfield;
    private Button loginbutton;
    private Button exitbutton;

    // Progress UI shown after successful login
    private VBox progressBox;
    private ProgressBar progressBar;
    private Label progressText;

    private DBConnection connection;
    int school_id;

    public LoginView() {
        initModality(Modality.APPLICATION_MODAL);

        // Outer layout
        root = new VBox();
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #eef2f7, #ffffff);");

        // Card container
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24));
        card.setMaxWidth(360);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: rgba(0,0,0,0.05);" +
                        "-fx-border-width: 1;"
        );
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2);
        shadow.setRadius(10);
        shadow.setColor(Color.rgb(0, 0, 0, 0.12));
        card.setEffect(shadow);

        // Logo and title
        VBox branding = new VBox(8);
        branding.setAlignment(Pos.CENTER);

        Image appLogo = ImageLoader.load("/resources/images/logo.png");


        ImageView logoView = null;
        if (appLogo != null) {
            logoView = new ImageView(appLogo);
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
        }



        Label subtitle = new Label("Sign in to continue");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        if (logoView != null) {
            branding.getChildren().addAll(logoView, subtitle);
        } else {
            branding.getChildren().add(subtitle);
        }

        // Form fields
        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);

        usernamefield = new TextField();
        usernamefield.setPromptText("Username");
        usernamefield.setStyle(
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 10;" +
                        "-fx-border-color: #dfe6e9; -fx-border-width: 1;"
        );
        usernamefield.setMinWidth(260);

        passwordfield = new PasswordField();
        passwordfield.setPromptText("Password");
        passwordfield.setStyle(
                "-fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 10;" +
                        "-fx-border-color: #dfe6e9; -fx-border-width: 1;"
        );
        passwordfield.setMinWidth(260);

        form.getChildren().addAll(usernamefield, passwordfield);

        // Buttons
        HBox buttonlayout = new HBox(12);
        buttonlayout.setAlignment(Pos.CENTER);
        buttonlayout.setPadding(new Insets(8, 0, 0, 0));

        loginbutton = new Button("Login");
        loginbutton.setDefaultButton(true);
        loginbutton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2980b9, #3498db);" +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 8; -fx-padding: 10 16;"
        );
        loginbutton.setMinWidth(120);

        exitbutton = new Button("Exit");
        exitbutton.setCancelButton(true);
        exitbutton.setStyle(
                "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50;" +
                        "-fx-font-size: 14px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 8; -fx-padding: 10 16;"
        );
        exitbutton.setMinWidth(100);

        buttonlayout.getChildren().addAll(loginbutton, exitbutton);

        // Success progress area (hidden by default)
        progressText = new Label("Signing you in...");
        progressText.setTextFill(Color.web("#7f8c8d"));
        progressText.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 12));
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(260);
        progressBar.setStyle("-fx-accent: #2ecc71;");
        progressBox = new VBox(8, progressText, progressBar);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setPadding(new Insets(4, 0, 0, 0));
        progressBox.setVisible(false);
        progressBar.setVisible(false);
        progressText.setVisible(false);

        card.getChildren().addAll(branding, form, buttonlayout, progressBox);
        root.getChildren().add(card);

        // Logic
        PasswordEncrypter decrypt = new PasswordEncrypter();

        loginbutton.setOnAction(e -> doLogin(decrypt));
        passwordfield.setOnAction(e -> doLogin(decrypt)); // Enter key in password triggers login

        exitbutton.setOnAction(e -> {
            this.connection = null;
            close();
        });
    }

    private void setInputsDisabled(boolean disabled) {
        usernamefield.setDisable(disabled);
        passwordfield.setDisable(disabled);
        loginbutton.setDisable(disabled);
        exitbutton.setDisable(disabled);
    }

    private void showProgressAndClose() {
        // Reveal progress UI
        progressBox.setVisible(true);
        progressText.setVisible(true);
        progressBar.setVisible(true);
        progressBar.setProgress(0);

        // Simulate a short transition with a Task to keep UI responsive, then close
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                final int steps = 100;
                for (int i = 1; i <= steps; i++) {
                    Thread.sleep(50);
                    updateProgress(i, steps);
                }
                return null;
            }
        };
        progressBar.progressProperty().bind(t.progressProperty());
        t.setOnSucceeded(ev -> {
            // Close dialog; showAndWait will return the connection set earlier
            close();
        });
        t.setOnFailed(ev -> {
            // In case something unexpected happens, re-enable inputs
            setInputsDisabled(false);
            progressBox.setVisible(false);
        });
        Thread th = new Thread(t);
        th.setDaemon(true);
        th.start();
    }

    private void doLogin(PasswordEncrypter decrypt) {
        String username = usernamefield.getText() != null ? usernamefield.getText().trim() : "";
        String password = passwordfield.getText() != null ? passwordfield.getText() : "";

        Alerthelper alert = new Alerthelper();
        if (username.isEmpty() || password.isEmpty()) {
            alert.showWarning("Please enter your username and password to continue.");
            return;
        }

        setInputsDisabled(true);

        DBConnection db = new DBConnection();
        if (!db.isConnected()) {
            setInputsDisabled(false);
            alert.showError("Unable to connect to database. Please try again later.");
            return;
        }

        try {
            // Prefer parameterized queries in DBConnection; keeping current approach but escaping username
            String safeUsername = username.replace("'", "''");
            Map<String, Object> row = db.fetchOne("select password, school_id from students_users where username = '" + safeUsername + "'");

            if (row == null || row.isEmpty()) {
                setInputsDisabled(false);
                alert.showError("User not found or invalid credentials.");
                return;
            }

            String password_from_user = String.valueOf(row.get("password"));
            String id_select = String.valueOf(row.get("school_id"));
            int id = Integer.parseInt(id_select);

            boolean isvalid = decrypt.verifyPBKDF2SHA256(password_from_user, password);
            if (isvalid) {
                // Store connection and school id, then animate progress and close
                this.connection = db;
                this.school_id = id;
                showProgressAndClose();
            } else {
                setInputsDisabled(false);
                alert.showError("Invalid password.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setInputsDisabled(false);
            alert.showError("Login failed: " + ex.getMessage());
        }
    }

    public DBConnection showAndWaitForConnection() {
        setTitle("Login");
        Scene scene = new Scene(root, 420, 500);
        setScene(scene);
        showAndWait();
        return connection;
    }

    public int idReturn() {
        return school_id;
    }

    public VBox getRoot() {
        return root;
    }
}