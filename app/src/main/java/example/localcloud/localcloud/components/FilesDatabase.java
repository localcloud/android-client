package example.localcloud.localcloud.components;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import example.localcloud.localcloud.persistance.FileDao;
import example.localcloud.localcloud.persistance.FileModel;

@Database(entities = {FileModel.class}, version = 1, exportSchema = false)
public abstract class FilesDatabase extends RoomDatabase {
    public abstract FileDao fileDao();

}
