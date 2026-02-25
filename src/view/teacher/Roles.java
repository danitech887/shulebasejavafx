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


public class Roles{
    private TableView<Map<String,Object>> roles_table;
    private ComboBox<String> criteria_combo;
    private TextField search_entry;

    private String reg_no;

    private ComboBox<String> lower_grade_combo;
    private ComboBox<String> lower_stream_combo;

    private ComboBox<String> upper_grade_combo;
    private ComboBox<String> upper_stream_combo;
    private ComboBox<String> upper_type_of_teacher_combo;

    private ComboBox<String> junior_grade_combo;
    private ComboBox<String> junior_stream_combo;
    private ComboBox<String> junior_type_of_teacher_combo;


    private List<CheckBox> lower_checks;
    private List<CheckBox> upper_checks;
    private List<CheckBox> junior_checks;

    private List<String> lower_selected_subjects = new ArrayList<>();
    private List<String> upper_selected_subjects = new ArrayList<>();
    private List<String> junior_selected_subjects = new ArrayList<>();
    private Label name;
    private Label add;




    private VBox roles_layout;

    public void getStreams(ComboBox<String> grade_combo,ComboBox<String> stream_combo,StreamFetch fetch){
        String grade_value = grade_combo.getValue();
        stream_combo.getItems().clear();
        List<String> streams = fetch.populateStreams(grade_value);
        stream_combo.getItems().addAll(streams);
    }
    public Roles(){
        HBox header = new HBox();
        header.setStyle("-fx-background-color: black");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        Label title = new Label("ROLES MANAGEMENT");
        title.setFont(Font.font("Times New Roman",FontWeight.BOLD,15));
        title.setStyle("-fx-text-fill: gold");
        header.getChildren().add(title);

        GridPane details = new GridPane();
        details.setPadding(new Insets(5));
        details.setAlignment(Pos.TOP_CENTER);
        Label name_label = new Label("Name:");
        name = new Label();
        Label add_label = new Label("Addressed as:");
        add = new Label();

        Label[] labels = {name_label,name,add_label,add};

        for(int i = 0; i<labels.length; i++){
            labels[i].setFont(Font.font("Arial Black",FontWeight.BOLD,14));
            details.add(labels[i],i,0);

        }

        GridPane subjects_layout = new GridPane();
        subjects_layout.setHgap(10);

        VBox lower_section = new VBox();
        Label lower_top_label = new Label("Lower Primary");
        lower_top_label.setFont(Font.font("Times New Roman",FontWeight.BOLD,15));
        lower_grade_combo = new ComboBox<>();
        lower_grade_combo.setPromptText("Select Grade");
        lower_grade_combo.getItems().addAll("PP1","PP2","Grade 1","Grade 2","Grade 3");
        lower_grade_combo.setOnAction(e -> getStreams(lower_grade_combo,lower_stream_combo,new StreamFetch(new DBConnection())));
        lower_stream_combo = new ComboBox<>();
        lower_stream_combo.setPromptText("Select Stream");
        
        lower_section.getChildren().addAll(lower_top_label,lower_grade_combo,lower_stream_combo);
        CheckBox math_check1 = new CheckBox("Mathematics");
        CheckBox eng_check1 = new CheckBox("English");
        CheckBox kisw_check1 = new CheckBox("Kiswahili");
        CheckBox int_check = new CheckBox("Integrated Creative");
        CheckBox env_check = new CheckBox("Environmental");

        lower_checks =new ArrayList<>();
        CheckBox[] checks = {math_check1,eng_check1,kisw_check1,int_check,env_check};
        for(CheckBox check : checks){
            lower_checks.add(check);
            check.setFont(Font.font("Times New Roman",FontWeight.BOLD,11));
            check.setPadding(new Insets(4));
            lower_section.getChildren().add(check);
        }
        Button lower_approve_btn = new Button("Approve");
        lower_approve_btn.setOnAction(e ->approve_lower());
        lower_section.getChildren().add(lower_approve_btn);


        VBox upper_section = new VBox();
        Label upper_top_label = new Label("Upper Primary");
        upper_top_label.setFont(Font.font("Times New Roman",FontWeight.BOLD,14));
        upper_grade_combo = new ComboBox<>();
        upper_grade_combo.setPromptText("Select Grade");

        for(int i = 4; i<6; i++){
            upper_grade_combo.getItems().add("Grade "+i);
        }
        upper_grade_combo.setOnAction(e -> getStreams(upper_grade_combo,upper_stream_combo,new StreamFetch(new DBConnection())));
        
        

        upper_stream_combo = new ComboBox<>();
        upper_stream_combo.setPromptText("Select Stream");
        
        upper_type_of_teacher_combo = new ComboBox<>();
        upper_type_of_teacher_combo.setPromptText("Type of Teacher");
        upper_type_of_teacher_combo.getItems().addAll("Class Teacher","Other");


        upper_section.getChildren().addAll(upper_top_label,upper_grade_combo,upper_stream_combo,upper_type_of_teacher_combo);

        CheckBox math_check2 = new CheckBox("Mathematics");
        CheckBox eng_check2 = new CheckBox("English");
        CheckBox kisw_check2 = new CheckBox("Kiswahili");
        CheckBox sci_check = new CheckBox("Science & Technology");
        CheckBox social_check1 = new CheckBox("Social Studies");
        CheckBox cre_check1 = new CheckBox("CRE");
        CheckBox agri_nutri_check1 = new CheckBox("Agriculture & Nutrition");
        CheckBox creative_arts_check1 = new CheckBox("Creative Arts");

        upper_checks = new ArrayList<>();
        CheckBox[] checks2 = {math_check2,eng_check2,kisw_check2,sci_check,social_check1,cre_check1,agri_nutri_check1,creative_arts_check1};

        for(CheckBox check : checks2){
            upper_checks.add(check);
            check.setFont(Font.font("Times New Roman",FontWeight.BOLD,11));
            check.setPadding(new Insets(4));
            upper_section.getChildren().add(check);
        }
        Button upper_approve_btn = new Button("Approve");
        upper_approve_btn.setOnAction(e -> approve_upper());

        upper_section.getChildren().add(upper_approve_btn);

        VBox junior_section = new VBox();
        Label junior_top_label = new Label("Junior Secondary");
        junior_top_label.setFont(Font.font("Times New Roman",FontWeight.BOLD,14));


        junior_grade_combo = new ComboBox<>();
        junior_grade_combo.setPromptText("Select Grade");
        for(int i = 7; i<=9; i++){
            junior_grade_combo.getItems().add("Grade "+i);
        }
        junior_grade_combo.setOnAction(e -> getStreams(junior_grade_combo,junior_stream_combo,new StreamFetch(new DBConnection())));
        junior_stream_combo = new ComboBox<>();
        junior_stream_combo.setPromptText("Select Stream");
        
        junior_type_of_teacher_combo = new ComboBox<>();
        junior_type_of_teacher_combo.setPromptText("Type of Teacher");
        junior_type_of_teacher_combo.getItems().addAll("Class Teacher","Other");
        junior_section.getChildren().addAll(junior_top_label,junior_grade_combo,junior_stream_combo,junior_type_of_teacher_combo);

        CheckBox math_check3 = new CheckBox("Mathematics");
        CheckBox eng_check3 = new CheckBox("English");
        CheckBox kisw_check3 = new CheckBox("Kiswahili");
        CheckBox int_sci_check = new CheckBox("Integrated Science");
        CheckBox social_check2 = new CheckBox("Social Studies");
        CheckBox cre_check2 = new CheckBox("CRE");
        CheckBox agri_nutri_check2 = new CheckBox("Agriculture & Nutrition");
        CheckBox creative_arts_check2 = new CheckBox("Creative Arts");
        CheckBox pre_comp_bs_check = new CheckBox("Pretech/Bs/Comps");

        junior_checks = new ArrayList<>();
        CheckBox[] checks3 = {math_check3,eng_check3,kisw_check3,int_sci_check,social_check2,cre_check2,agri_nutri_check2,creative_arts_check2,pre_comp_bs_check};

        for(CheckBox check : checks3){
            junior_checks.add(check);
            check.setFont(Font.font("Times New Roman",FontWeight.BOLD,11));
            check.setPadding(new Insets(4));
            junior_section.getChildren().add(check);
        }
        Button junior_approve_btn = new Button("Approve");
        junior_approve_btn.setOnAction(e -> approve_junior());
        junior_section.getChildren().add(junior_approve_btn);

        Label[] top_labels = {lower_top_label,upper_top_label,junior_top_label};
        for(Label label : top_labels){
            label.setPadding(new Insets(20));
            label.setStyle("-fx-text-fill: blue");
        }
        ComboBox[] combos = {lower_grade_combo,lower_stream_combo,upper_grade_combo,upper_stream_combo,junior_grade_combo, junior_stream_combo};
        for(ComboBox combo : combos){
            combo.setPrefWidth(170);
            
        }
        DBConnection db = new DBConnection();
        StreamFetch fetch = new StreamFetch(db);
        List <String> grades = fetch.populateGrades();
        ComboBox[] grade_combos = {lower_grade_combo,upper_grade_combo,junior_grade_combo};

        ComboBox[] stream_combos = {lower_stream_combo,upper_stream_combo,junior_stream_combo};
        for (ComboBox<String> grade_combo : grade_combos ){
            if (grades.contains(grade_combo.getValue())){
                String grade_value = grade_combo.getValue();
                List<String> streams = fetch.populateStreams(grade_value);
                for (ComboBox<String> stream_combo : stream_combos){
                    stream_combo.getItems().clear();
                    stream_combo.getItems().addAll(streams);
                }
            }
        }
        

        VBox approved_layout = new VBox();
        Label approved_top_label = new Label("Approved Teachers");
        approved_top_label.setFont(Font.font("Arial Black",FontWeight.EXTRA_BOLD,15));
        GridPane search_panel = new GridPane();
        search_panel.setPadding(new Insets(10));
        search_panel.setHgap(10);

        criteria_combo = new ComboBox<>();
        criteria_combo.setPromptText("--Search By--");
        criteria_combo.getItems().addAll(
            "Teacher No","Type of Teacher","Grade","Stream"
        );
        search_entry = new TextField();
        Button search_button = new Button("Search");
        search_button.setOnAction(e ->searchRolesData());
        search_panel.add(criteria_combo,0,0);
        search_panel.add(search_entry,1,0);
        search_panel.add(search_button,2,0);

        roles_table = new TableView<>();
        HBox button_lay = new HBox(20);
        button_lay.setPadding(new Insets(10));
    
        Button deduct_role_btn = new Button("Deduct Roles");
        deduct_role_btn.setOnAction(e -> deductRoles());
        Button approve_btn = new Button("New Approval");
        approve_btn.setOnAction(e -> approve_teachers());
        button_lay.getChildren().addAll(approve_btn,deduct_role_btn);
        approved_layout.getChildren().addAll(approved_top_label,search_panel,roles_table,button_lay);



        VBox[] sections = {lower_section,upper_section,junior_section,approved_layout};

        for(int i =0; i<sections.length; i++){
            sections[i].setPadding(new Insets(5,15,10,15));
            
            subjects_layout.add(sections[i],i,0);
        }
        GridPane.setHgrow(subjects_layout,Priority.ALWAYS);

        roles_layout = new VBox();
        roles_layout.getChildren().addAll(header,details,subjects_layout);
        displayApprovedTeachers();
        
        lower_grade_combo.setOnAction(e ->{
            String grade = lower_grade_combo.getValue();
            String stream = lower_stream_combo.getValue();
            if(grade != null && stream != null){
                normalizeChecks(lower_checks);
                restrictRoles(lower_checks,grade,stream);
            }
        });
        lower_stream_combo.setOnAction(e -> {
            String grade =  lower_grade_combo.getValue();
            String stream = lower_stream_combo.getValue();
            if(grade != null && stream != null){
                normalizeChecks(lower_checks);
                restrictRoles(lower_checks,grade,stream);
            }
        });
        upper_grade_combo.setOnAction(e -> {
            String grade = upper_grade_combo.getValue();
            String stream = upper_stream_combo.getValue();
            if(grade != null && stream != null){
                normalizeChecks(upper_checks);
                restrictRoles(upper_checks,grade,stream);
            }
        });
        upper_stream_combo.setOnAction(e -> {
            String grade = upper_grade_combo.getValue();
            String stream = upper_stream_combo.getValue();
            if(grade != null && stream != null){
                normalizeChecks(upper_checks);
                restrictRoles(upper_checks,grade,stream);
            }
        });
        junior_grade_combo.setOnAction(e -> {
            String grade = junior_grade_combo.getValue();
            String stream = junior_stream_combo.getValue();
            if (grade != null && stream != null){
                normalizeChecks(junior_checks);
                restrictRoles(junior_checks,grade,stream);
            }
        });
        junior_stream_combo.setOnAction(e -> {
            String grade = junior_grade_combo.getValue();
            String stream = junior_stream_combo.getValue();
            if(grade !=  null && stream != null){
                normalizeChecks(junior_checks);
                restrictRoles(junior_checks,grade,stream);
            }
        });
        
    }
    public VBox getLayout(){
        return roles_layout;
    }

    public void displayApprovedTeachers(){
        DBConnection db = new DBConnection();
        List<Map<String,Object>> approved_teachers = db.fetchAll("select registration_no as TeacherNo,type_of_teacher as TypOfTeacher,grade as Grade,stream as Stream,subject as Subjects from students_teachersrole");
        TableViewHelper.populateTable(roles_table,approved_teachers);

    }
    public void search(String column){
        DBConnection db = new DBConnection();
        Alerthelper alert = new Alerthelper();
        String search = search_entry.getText().trim();
        if(search.equals("")){
            alert.showSuccess("Please type something in the search box");
        } else{
            List<Map<String,Object>> roles_data = db.fetchAll("select registration_no as TeacherNo, type_of_teacher as TypeOfTeacher,grade as Grade, stream as Stream,subject as Subjects from students_teachersrole where "+column+" = '"+search+"' ");
            TableViewHelper.populateTable(roles_table,roles_data);
        }
    }
    public void searchRolesData(){
        Alerthelper alert = new Alerthelper();
        String criteria = criteria_combo.getValue().toString();
        if(criteria.equals("")){
            alert.showError("Please select a search criteria");
        } else{
            if(criteria.equals("Teacher No")){
                search("registration_no");
            }else if(criteria.equals("Type of Teacher")){
                search("type_of_teacher");
            }else if (criteria.equals("Grade")){
                search("grade");
            } else if(criteria.equals("Stream")){
                search("stream");
            }
        }
    }

    public void deductRoles(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Requred");
        dialog.setHeaderText("Fill the Entry");
        dialog.setContentText("Teacher No");
        Optional<String> dialog_result = dialog.showAndWait();
        dialog_result.ifPresent(value ->{
            reg_no = value;
            Alerthelper alert = new Alerthelper();
            DBConnection db = new DBConnection();
            boolean confirm = alert.showConfirmation("You want to deduct role for this teacher");
            if(confirm){
                if(db.delete("delete from students_teachersrole where registration_no = '"+reg_no+"'")){
                    alert.showSuccess("Role deduction for teacher with registration_no "+reg_no+" has been initiated");
                    displayApprovedTeachers();
                }

            }else {
                alert.showSuccess("Please confirm before continuing");
            }
        });
    }

    public void approve_teachers(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText("Fill the Entry");
        dialog.setContentText("Teacher No");
        Optional<String> dialog_result = dialog.showAndWait();
        dialog_result.ifPresent(value ->{
            if(value != null){
                reg_no = value;
                DBConnection db = new DBConnection();
                Map<String,Object> teacher_data = db.fetchOne("select first_name, gender from students_teacherinfo where registration_no = '"+reg_no+"'");
                if(teacher_data.get("gender").equals("Male")){
                    add.setText("Mr. "+teacher_data.get("first_name"));
                }else{
                    add.setText("Mrs/Miss. "+teacher_data.get("first_name"));
                }
            }
        });
    }
    public void selectedSubjects(){     
        for(CheckBox checkbox : lower_checks){
            if(checkbox.isSelected()){
                lower_selected_subjects.add(checkbox.getText());
            }
        }
        
        for(CheckBox checkbox : upper_checks){
            if(checkbox.isSelected()){
                upper_selected_subjects.add(checkbox.getText());
            }
        }

        for(CheckBox checkbox : junior_checks){
            if(checkbox.isSelected()){
                junior_selected_subjects.add(checkbox.getText());
            }
        }
    }
    public void normalizeChecks(List<CheckBox> checks){
        for(CheckBox checkbox : checks){
            checkbox.setDisable(false);
        }
    }
    public void  restrictRoles(List<CheckBox> checks, String grade,String stream){
        DBConnection db = new DBConnection();
        if(checks == lower_checks){
            
            Map<String,Object> lower_subjects = db.fetchOne("select subject from students_teachersrole where grade = '"+grade+"' and stream = '"+stream+"'");
            
            List<String> lower_list = new ArrayList<>();
            String lower_raw = lower_subjects.get("subject").toString();
            lower_raw = lower_raw.replace("[","")
                                    .replace("]","")
                                    .replace("'","");
            String[] lowerArray = lower_raw.split(",");
            for(String s : lowerArray){
                lower_list.add(s.trim());
            }
            for(CheckBox check : checks){
                    if(lower_list.contains(check.getText())){
                        check.setDisable(true);
                    }else {
                        check.setDisable(false);
                    }
            }
    
        } else if(checks == upper_checks){
            Map<String,Object> upper_subjects = db.fetchOne("select subject from students_teachersrole where grade = '"+grade+"' and stream = '"+stream+"'");

            List<String> upper_list = new ArrayList<>();
            String upper_raw = upper_subjects.get("subject").toString();
            upper_raw = upper_raw.replace("[","")
                                    .replace("]","")
                                    .replace("'","");
            String[] upperArray = upper_raw.split(",");
            for (String s : upperArray){
                upper_list.add(s.trim());
            }
            for(CheckBox check : checks){
                if(upper_list.contains(check.getText())){
                    check.setDisable(true);
                } else {
                    check.setDisable(false);
                }
            }
        } else if (checks == junior_checks){
            Map<String,Object> junior_subjects = db.fetchOne("select subject from students_teachersrole where grade = '"+grade+"' and stream = '"+stream+"'");
            List<String> junior_list = new ArrayList<>();
            String junior_raw = junior_subjects.get("subject").toString();
            junior_raw = junior_raw.replace("[","")
                                    .replace("]","")
                                    .replace("'","");
            String[] juniorArray = junior_raw.split(",");

            for(String s : juniorArray){
                junior_list.add(s.trim());
            }

            for(CheckBox check : checks){
                if(junior_list.contains(check.getText())){
                    check.setDisable(true);
                } else {
                    check.setDisable(false);
                }
            }
        }
    }
    

    public void approve_lower(){
        selectedSubjects();
        
        Alerthelper alert = new Alerthelper();
        DBConnection db = new DBConnection();
        String grade = lower_grade_combo.getValue();
        String stream = lower_stream_combo.getValue();
        boolean confirm = alert.showConfirmation("Are you sure of these selected subjects: \n"+lower_selected_subjects);
        if(reg_no == null){
            alert.showSuccess("Please enter teacher no by clicking the New Approval button");
        }else{
            if(confirm){
                alert.showSuccess(reg_no);
                if(db.storeData("insert into students_teachersrole(registration_no,type_of_teacher,grade,stream,subject)values('"+reg_no+"','Class Teacher','"+grade+"','"+stream+"','"+lower_selected_subjects+"')")){
                    alert.showSuccess("Teacher with registration_no "+reg_no+" has been given a role in "+grade+" "+stream);
                    displayApprovedTeachers();
                }else{
                    alert.showError("Failed to approve roles");
                }
            }
        }
    }
    public void approve_upper(){
        selectedSubjects();
        Alerthelper alert = new Alerthelper();
        DBConnection db = new DBConnection();
        String grade = upper_grade_combo.getValue();
        String stream = upper_stream_combo.getValue();
        String type_of_teacher = upper_type_of_teacher_combo.getValue();
        boolean confirm = alert.showConfirmation("Are you sure of these selected subjects: \n"+upper_selected_subjects);
        if(reg_no == null){
            alert.showSuccess("Pleasee enter teacher no by clicking New Approval Button");
        }else{
            if(confirm){
                if(db.storeData("insert into students_teachersrole(registration_no,type_of_teacher,grade,stream)values('"+reg_no+"','"+type_of_teacher+"','"+grade+"','"+stream+"','"+upper_selected_subjects+"')")){
                    alert.showSuccess("Teacher with registration_no "+reg_no+" has been given a role in "+grade+" "+stream);
                    displayApprovedTeachers();
                }else{
                    alert.showError("Failed to approve roles");
                }
            }
        }
    }

    public void approve_junior(){
        selectedSubjects();
        Alerthelper alert = new Alerthelper();
        DBConnection db = new DBConnection();
        String grade = junior_grade_combo.getValue();
        String stream = junior_stream_combo.getValue();
        String type_of_teacher = junior_type_of_teacher_combo.getValue();
        boolean confirm = alert.showConfirmation("Are you sure of these selected subjects: \n"+junior_selected_subjects);
        if(reg_no == null){
            alert.showSuccess("Please enter teacher no by clicking New Approval button");
        }else{
            if (confirm){
                if(db.storeData("insert into students_teachersrole(registration_no,type_of_teacher,grade,stream,subject)values('"+reg_no+"','"+type_of_teacher+"','"+grade+"','"+stream+"','"+junior_selected_subjects+"')")){
                    alert.showSuccess("Teacher with registration_no "+reg_no+" has been given roles in "+grade+" "+stream);
                    displayApprovedTeachers();
                }else{
                    alert.showError("Falied to approve roles");
                }
            }
        }
    }
}

