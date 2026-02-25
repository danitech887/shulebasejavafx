package view.teacher;

import utils.*;
import model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.time.LocalDate;


public class TeachingTracking {
    private TableView<Map<String, Object>> progress_table;
    private ComboBox<String> criteria_combo;
    private TextField search_entry;
    private ComboBox<String> grade_combo;
    private ComboBox<String> stream_combo;

    private VBox progress_layout;

    private Alerthelper alert = new Alerthelper();
    private DBConnection db = new DBConnection();

    public TeachingTracking() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        Label title = new Label("TEACHING PROGRESS TRACKING");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);
        grade_combo = new ComboBox<>();
        grade_combo.setPromptText("Select Grade");
        grade_combo.getItems().addAll("All Grades", "PP1", "PP2");
        for (int i = 1; i <= 9; i++) {
            grade_combo.getItems().add("Grade " + i);
        }
        stream_combo = new ComboBox<>();
        stream_combo.setPromptText("Select Stream");
        for (int i = 1; i <= 4; i++) {
            stream_combo.getItems().add("Stream " + i);
        }
        Button filter_btn = new Button("Filter");
        filter_btn.setOnAction(e -> filter());

        criteria_combo = new ComboBox<>();
        criteria_combo.setPromptText("--Search By--");
        criteria_combo.getItems().addAll("Teacher No", "Name", "Grade", "Stream", "Type of Teacher", "Subject", "Status");

        search_entry = new TextField();
        search_entry.setPromptText("Type something to search");
        Button search_btn = new Button("Search");
        search_btn.setOnAction(e -> searchProgressData());

        progress_layout = new VBox();
        progress_layout.getChildren().add(header);
        HBox combo_layout = new HBox(20);
        combo_layout.setPadding(new Insets(10));
        combo_layout.getChildren().addAll(grade_combo, stream_combo, filter_btn, criteria_combo, search_entry, search_btn);


        VBox table_layout = new VBox(10);
        progress_table = new TableView<>();
        table_layout.getChildren().addAll(combo_layout, progress_table);
        progress_layout.getChildren().add(table_layout);

        displayApprovedTeachers();

    }

    public VBox getLayout() {
        return progress_layout;
    }

    public void displayApprovedTeachers() {

        List<Map<String, Object>> approved_teachers = db.fetchAll("select p.registration_no as TeacherNo,t.first_name as FirstName,r.type_of_teacher as TypeOfTeacher,r.grade as Grade,r.stream as Stream,p.subject as Subject,p.topic as Topic,p.sub_topic as SubTopic,p.status as Status,p.date_of_teaching as StartedAt,p.date_finished as DateFinished from students_teachingprogress p join students_teacherinfo t on t.registration_no = p.registration_no join students_teachersrole r on r.registration_no = p.registration_no");
        TableViewHelper.populateTable(progress_table, approved_teachers);

    }

    public void filter() {
        String grade = grade_combo.getValue();
        String stream = stream_combo.getValue();

        if (grade.equals("All Grades")) {
            displayApprovedTeachers();
        } else {
            if (grade == null || stream == null) {
                alert.showWarning("Please select grade and stream to continue");

            } else {
                List<Map<String, Object>> progress_data = db.fetchAll("select p.registration_no as TeacherNo,t.first_name as FirstName,r.type_of_teacher as TypeOfTeacher,r.grade as Grade,r.stream as Stream,p.subject as Subject,p.topic as Topic,p.sub_topic as SubTopic,p.status as Status,p.date_of_teaching as StartedAt,p.date_finished as DateFinished from teachingprogress p join students_teacherinfo t on p.registration_no = t.registration_no join students_teachersrole r on r.registration_no = p.registration_no where r.grade = '" + grade + "' and r.stream = '" + stream + "'");
                TableViewHelper.populateTable(progress_table, progress_data);

            }
        }
    }

    public void search(String column) {
        String search = search_entry.getText().trim();
        if (search.equals("")) {
            alert.showSuccess("Please type something in the search box");
        } else {
            List<Map<String, Object>> roles_data = db.fetchAll("select p.registration_no as TeacherNo,t.first_name as FirstName,r.type_of_teacher as TypeOfTeacher,r.grade as Grade,r.stream as Stream,p.subject as Subject,p.topic as Topic,p.sub_topic as SubTopic,p.status as Status,p.date_of_teaching as StartedAt,p.date_finished as DateFinished from teachingprogress p join students_teacherinfo t on p.registration_no = t.registration_no join teachers_role r on r.registration_no = p.registration_no where " + column + " = '" + search + "'");
            TableViewHelper.populateTable(progress_table, roles_data);
        }
    }

    public void searchProgressData() {
        String criteria = criteria_combo.getValue().toString();
        if (criteria.equals("")) {
            alert.showError("Please select a search criteria");
        } else {
            if (criteria.equals("Teacher No")) {
                search("p.registration_no");
            } else if (criteria.equals("Type of Teacher")) {
                search("p.type_of_teacher");
            } else if (criteria.equals("Grade")) {
                search("r.grade");
            } else if (criteria.equals("Stream")) {
                search("r.stream");
            } else if (criteria.equals("Name")) {
                search("t.first_name");
            } else if (criteria.equals("Status")) {
                search("p.status");
            } else if (criteria.equals("Subject")) {
                search("p.subject");
            }
        }
    }
}

