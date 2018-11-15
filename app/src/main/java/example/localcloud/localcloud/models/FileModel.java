package example.localcloud.localcloud.models;

import example.localcloud.localcloud.db.Db;

public class FileModel extends APathName {

    private String path;
    private Db db;

    public FileModel(Db db, String path) {

        this.db = db;
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    protected Db getDb() {
        return this.db;
    }

    public String getFolder() {
        return this.getParent();
    }
}
