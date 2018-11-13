package example.localcloud.localcloud.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import example.localcloud.localcloud.dav.DavClientFactory;

public class SyncTaskService extends IntentService {

    private String TAG = "sync_task_service";

    public SyncTaskService() {
        super("Upload files to server");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String imgPath = intent.getStringExtra("img_path");
        if (DavClientFactory.client(this).put(imgPath)) {
            Log.d(TAG, String.format("image %s was sent successfully to the server", imgPath));
        } else {
            Log.d(TAG, String.format("image %s didn't sent to the server, something went wrong", imgPath));
        }
    }
}
