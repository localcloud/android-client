package example.localcloud.localcloud.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {

    public static String TBL_FOLDERS = "folders";
    public static String TBL_FILES = "files";
    private static String CREATE_TABLE_FOLDERS = String.format("CREATE TABLE IF NOT EXISTS %s (sha_256 CHARACTER(64) PRIMARY KEY, synchronized BOOLEAN);", TBL_FOLDERS);
    private static String CREATE_TABLE_FILES = String.format("CREATE TABLE IF NOT EXISTS %s (sha_256 CHARACTER(64) PRIMARY KEY, status TINYINT);", TBL_FILES);
    private static int VERSION = 1;

    public Db(Context context) {
        super(context, "local_cloud", null, Db.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Db.CREATE_TABLE_FOLDERS);
        db.execSQL(Db.CREATE_TABLE_FILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
