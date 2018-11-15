package example.localcloud.localcloud.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import example.localcloud.localcloud.db.Db;

public class FolderModel extends APathName implements Comparable, Serializable {

    private boolean syncEnabled = false;
    private String path;
    private ArrayList<FileModel> files;
    private Db db;

    public FolderModel(Db db, String path, ArrayList<FileModel> files) {
        this.db = db;
        this.path = path;
        this.files = files;
        Cursor cursor = this
                .getDb()
                .getReadableDatabase()
                .query(
                        Db.TBL_FOLDERS,
                        new String[]{"sha_256", "synchronized"},
                        "sha_256=?",
                        new String[]{String.valueOf(this.getPk())}, null, null, null, "1");
        if (cursor != null && cursor.moveToFirst()) {
            this.syncEnabled = cursor.getInt(cursor.getColumnIndex("synchronized")) > 0;
            cursor.close();
        }
    }

    public String getPath() {
        return this.path;
    }

    @Override
    protected Db getDb() {
        return this.db;
    }

    public ArrayList<FileModel> getFiles() {
        return this.files;
    }

    public int countFiles() {
        return this.files.size();
    }

    public boolean isSyncEnabled() {
        return this.syncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("sha_256", this.getPk());
        initialValues.put("synchronized", syncEnabled);

        int id = (int) this.getDb()
                .getWritableDatabase()
                .insertWithOnConflict(Db.TBL_FOLDERS, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            this.getDb()
                    .getWritableDatabase()
                    .update(Db.TBL_FOLDERS, initialValues, "sha_256=?", new String[]{this.getPk()});
        }

        this.syncEnabled = syncEnabled;
    }

    @Override
    public int compareTo(Object o) {
        return ((FolderModel) o).countFiles() > this.countFiles() ? 1 : 0;
    }
}
