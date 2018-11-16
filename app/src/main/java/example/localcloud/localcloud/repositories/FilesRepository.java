package example.localcloud.localcloud.repositories;


import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import example.localcloud.localcloud.LCApp;
import example.localcloud.localcloud.persistance.FileDao;
import example.localcloud.localcloud.persistance.FileModel;

public class FilesRepository extends BaseRepository {
    private final FileDao fileDao;
    private LiveData<List<FileModel>> allFiles;

    public FilesRepository() {
        this.fileDao = LCApp.app().components().db().files().fileDao();
        this.allFiles = this.fileDao.getAll();
    }


    public void save(final FileModel fileModel) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                fileDao.save(fileModel);
            }
        });
    }

    public LiveData<List<FileModel>> getAll() {
        return this.allFiles;
    }

}
