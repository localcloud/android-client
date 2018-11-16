package example.localcloud.localcloud.components;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabasesComponent extends BaseComponent {

    private FilesDatabase _files;

    /**
     * Provide instance of file database
     */
    public FilesDatabase files() {
        if (this._files == null) {
            this._files = Room.databaseBuilder(this.getContext(), FilesDatabase.class, "files").build();
        }
        return this._files;
    }
}
