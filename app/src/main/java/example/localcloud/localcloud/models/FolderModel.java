package example.localcloud.localcloud.models;

import java.util.ArrayList;

public class FolderModel extends APathName {

    private String path;
    private ArrayList<FileModel> files;

    public FolderModel(String path, ArrayList<FileModel> files) {
        this.path = path;
        this.files = files;
    }

    public String getPath() {
        return this.path;
    }

    public ArrayList<FileModel> getFiles() {
        return this.files;
    }

    public int countFiles() {
        return this.files.size();
    }
}
