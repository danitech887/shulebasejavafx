package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;
import java.util.List;
import java.util.Map;

public class TableViewHelper {
    public static void populateTable(TableView<Map<String,Object>> tableview, List<Map<String,Object>> data){
        tableview.setStyle("-fx-background-color:rgb(252, 219, 219)");
        tableview.getColumns().clear();

        if (data == null || data.isEmpty()) return;
        Map<String,Object> firstRow = data.get(0);
        for (String key : firstRow.keySet()){
            TableColumn<Map<String,Object>,Object> column = new TableColumn<>(key);
            column.setCellValueFactory((MapValueFactory)new MapValueFactory<>(key));
            // column.setStyle("-fx-background-color:rgb(230, 230, 230); -fx-text-fill: white");
            column.setMinWidth(100);
            tableview.getColumns().add(column);
        }
        ObservableList<Map<String,Object>> observabledata = FXCollections.observableArrayList(data);
        tableview.setItems(observabledata);
    }
}