package example.localcloud.localcloud;

import java.util.ArrayList;


public class ModelImages {
    private String folderName;
    private String folderPath;
    private ArrayList<String> allImagesPath;

    public String getFolderName() {
        return folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public ArrayList<String> getAllImagesPath() {
        return allImagesPath;
    }

    public void setAllImagesPath(ArrayList<String> allImagesPath) {
        this.allImagesPath = allImagesPath;
    }
}