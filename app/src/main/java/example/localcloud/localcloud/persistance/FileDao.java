package example.localcloud.localcloud.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FileDao {

    @Insert(onConflict = REPLACE)
    void save(FileModel fileModel);

    @Query("SELECT * FROM files WHERE id = :fileId")
    LiveData<FileModel> loadByPk(String fileId);

    @Query("SELECT * FROM files")
    LiveData<List<FileModel>> getAll();

}
