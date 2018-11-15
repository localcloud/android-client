package example.localcloud.localcloud.contentProviders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import example.localcloud.localcloud.db.Db;
import example.localcloud.localcloud.models.FileModel;
import example.localcloud.localcloud.models.FolderModel;

public class MediaContentProvider implements ISyncContentProvider {

    private ArrayList<FolderModel> folders = new ArrayList<>();
    private boolean scanned = false;
    private Context ctx;
    private Db db;
    private HashMap<String, ArrayList<FileModel>> filterMap = new HashMap<>();

    private static MediaContentProvider i;

    private MediaContentProvider(Context ctx) {
        this.ctx = ctx;
        this.db = new Db(ctx);
    }


    public static MediaContentProvider instance(Context ctx) {
        if (MediaContentProvider.i == null) {
            MediaContentProvider.i = new MediaContentProvider(ctx);
        }
        return MediaContentProvider.i;
    }

    private void clear() {
        this.scanned = false;
        this.folders.clear();
    }

    private ContentResolver getContentResolver() {
        return this.ctx.getContentResolver();
    }

    private MergeCursor newCursor() {
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        String order = String.format("%s DESC", MediaStore.Images.Media.DATE_TAKEN);
        return new MergeCursor(
                new Cursor[]{
                        this.getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                projection,
                                null,
                                null,
                                order
                        ),
                        this.getContentResolver().query(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                projection,
                                null,
                                null,
                                order
                        ),
                }
        );
    }

    private void addFileToHashMap(FileModel fileModel) {
        if (!this.filterMap.containsKey(fileModel.getFolder())) {
            this.filterMap.put(fileModel.getFolder(), new ArrayList<FileModel>());
        }
        this.filterMap.get(fileModel.getFolder()).add(fileModel);
    }

    public ArrayList<FolderModel> fetch() {
        if (this.scanned) {
            return this.folders;
        }
        MergeCursor cursor = this.newCursor();
        int colData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int colBucket = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            File file = new File(cursor.getString(colData));
            String bucketName = cursor.getString(colBucket);
            if (file.getParent().endsWith(bucketName)) {
                this.addFileToHashMap(new FileModel(this.db, file.getPath()));
            }
        }
        for (String folder : this.filterMap.keySet()) {
            this.folders.add(new FolderModel(this.db, folder, this.filterMap.get(folder)));
        }
        this.scanned = true;
        this.filterMap.clear();
        cursor.close();
        return this.folders;
    }


    public ArrayList<String> fetchOnlyPath() {
        ArrayList<String> res = new ArrayList<>();
        for (FolderModel folderModel : this.fetch()) {
            for (FileModel fileModel : folderModel.getFiles()) {
                res.add(fileModel.getPath());
            }
        }
        return res;
    }

    /**
     * @param renew Does need to fetch all list again?
     * @return provide full list of content
     */
    public ArrayList<FolderModel> fetch(boolean renew) {
        if (renew) {
            this.clear();
        }
        return this.fetch();
    }
}
