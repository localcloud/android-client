package example.localcloud.localcloud.activities.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import example.localcloud.localcloud.LCApp;
import example.localcloud.localcloud.persistance.FileModel;

public class FileViewModel extends ViewModel {
    private LiveData<List<FileModel>> allFiles;

    public FileViewModel() {
        allFiles = LCApp.app().repositories().files().getAll();
    }


    public LiveData<List<FileModel>> getAllFiles() {
        return allFiles;
    }

    public void insert(FileModel file) {
        LCApp.app().repositories().files().save(file);
    }



}
