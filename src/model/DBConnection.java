package model;
import utils.Alerthelper;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class DBConnection {
    private static DBConnection instance;

    String url;
    String username;
    String password;


    private  Connection connection;
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // No-arg constructor reuses existing shared instance/connection

    public DBConnection(){
        this.username = "root";
        this.password  = "daenicel";
        this.url = "jdbc:mysql://localhost/shulebase";
//        this.password = "XyyYQWLsZ5GMDGmBIVKFa0IQXDsJlwmI";
//        this.url = "jdbc:postgresql://dpg-d3d2qp56ubrc73f8k360-a.oregon-postgres.render.com/shulebase_hbsp";
        try {
            connection = DriverManager.getConnection(this.url,this.username,this.password);
            System.out.println("Connected successfully");
            instance = this; // set shared instance on successful connection
        }catch (SQLException e){
            Alerthelper alert = new Alerthelper();
            alert.showError("Database connection failed"+ e.getMessage());
        }
    }

    public List<Map<String,Object>> fetchAll(String query){
        List<Map<String,Object>> data = new ArrayList<>();
        try (PreparedStatement stmt =  connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query)){
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                while (rs.next()){
                    Map<String,Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++){
                        row.put(meta.getColumnLabel(i),rs.getObject(i));
                    }
                    data.add(row);
                }
            }catch (SQLException e){
                Alerthelper alert = new Alerthelper();
                alert.showError("Error: "+e.getMessage());
            }
            return data;
    }

    public Map<String,Object> fetchOne(String query, Object...params){

        try (PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query)
        
        ){
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            if (rs.next()){
                Map<String,Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++){
                    row.put(meta.getColumnLabel(i),rs.getObject(i));
                }
                return row;
            }
        }catch (SQLException e){
            Alerthelper alert = new Alerthelper();
            alert.showError("Error: "+e.getMessage());
        }
        return null;
    }
    public int getCount(String query){
        try(PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query)
        ){
            if (rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            System.err.println("Get count failed "+e.getMessage());
        }
        return 0;
    }

    public double getAmount(String query){
        try(PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query)
        ){
            if (rs.next()){
                return rs.getDouble(1);
            }
        }catch(SQLException e){
            System.err.println("Failed "+ e.getMessage());
        }
        return 0.0;
    }
    public boolean storeData(String query){
        try(PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e){
            Alerthelper alert = new Alerthelper();
            alert.showError("Error: "+e.getMessage());
        }
        return false;
    }
    public boolean delete(String query){
        try(PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.executeUpdate(query);
            return true;
        }catch(SQLException e){
            Alerthelper alert = new Alerthelper();
            alert.showError("Failed: " +e.getMessage());
        }
        return false;
    }
    public void close(){
        try {
            if(connection != null && ! connection.isClosed())
                connection.close();
        }catch (SQLException e){
            System.err.println("Close error: " + e.getMessage());
        }
    }
}
