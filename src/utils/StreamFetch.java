package utils;

import javafx.scene.control.ComboBox;
import model.DBConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamFetch {
    List<String> grades = new ArrayList<>();
    DBConnection connection;
    List<String> streams = new ArrayList<>();

    public StreamFetch(DBConnection connection) {
        this.connection = connection;

    }

    public List<String> populateStreams(String grade) {
        List<String> streams = new ArrayList<>();
        List<Map<String, Object>> stream_select = connection.fetchAll("select stream from students_gradestream where grade = '" + grade + "'");
        for (Map<String, Object> stream : stream_select) {
            streams.add(stream.get("stream").toString());

        }
        return streams;
    }


    public List<String> populateGrades() {
        List<Map<String, Object>> grade_select = connection.fetchAll("select grade from students_gradestream group by grade");
        for (Map<String, Object> grade : grade_select) {
            grades.add(grade.get("grade").toString());

        }
        return grades;

    }

}
