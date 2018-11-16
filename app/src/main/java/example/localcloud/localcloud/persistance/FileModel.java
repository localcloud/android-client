package example.localcloud.localcloud.persistance;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "files")
public class FileModel {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String id;

    @ColumnInfo(name = "sync_status")
    public Integer syncStatus;

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(@NonNull Integer syncStatus) {
        this.syncStatus = syncStatus;
    }
}
