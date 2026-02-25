package utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryUtil{
    private static final String BASE_FOLDER = "ShuleBase Files";

    public static void createBaseFolder(){
        createDirectory(BASE_FOLDER);
    }

    public static String createSubFolder(String folderName){
        String full_path = BASE_FOLDER + File.separator + folderName;
        createDirectory(full_path);
        return full_path;
    }
    public static void createDirectory(String path){
        File dir = new File(path);
        if(!dir.exists()){
            boolean created = dir.mkdirs();
            if(created){
                System.out.println("👍Directory created: "+dir.getAbsolutePath());
            } else {
                System.out.println("😒Failed to create directory");
            }
        }
    }
    public static void createAllFolders(){
        createBaseFolder();
        createSubFolder("Fee Receipts");
        createSubFolder("Report Forms");
        createSubFolder("Leave Receipts");
        createSubFolder("Students Attendance Files");
        createSubFolder("Teachers Attendance Files");
        createSubFolder("Fee Reports");

    }
    public static String createFileName(String... params){
        return Paths.get(params[0],java.util.Arrays.copyOfRange(params,1,params.length)).toString();
        

    }

    
}