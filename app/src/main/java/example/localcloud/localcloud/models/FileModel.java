package example.localcloud.localcloud.models;

public class FileModel extends APathName {

    private String path;

    public FileModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public String getFolder() {
        return this.getParent();
    }
}
