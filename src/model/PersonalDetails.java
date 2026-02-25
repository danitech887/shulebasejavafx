package model;
import javafx.beans.property.SimpleStringProperty;


public class PersonalDetails {
    private final SimpleStringProperty regno;
    private final SimpleStringProperty name;
    private final SimpleStringProperty grade;
    private final SimpleStringProperty stream;
    // private final SimpleStringProperty gender;

    public PersonalDetails(String regno,String name,String grade,String stream){
        this.regno = new SimpleStringProperty(regno);
        this.name = new SimpleStringProperty(name);
        this.grade = new SimpleStringProperty(grade);
        this.stream = new SimpleStringProperty(stream);
    }
    public String getregno(){
        return regno.get();
    }
    public String getname(){
        return name.get();
    }
    public String getgrade(){
        return grade.get();
    }
    public String getstream(){
        return stream.get();
    }
    
}