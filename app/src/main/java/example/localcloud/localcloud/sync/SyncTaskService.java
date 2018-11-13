package example.localcloud.localcloud.sync;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import example.localcloud.localcloud.dav.DavClientFactory;

public class SyncTaskService extends IntentService {

    public static final String EXTRA_KEY_FILE_LIST = "file_list";
    public static final String EXTRA_KEY_PROGRESS = "progress";
    public static final String INTENT_ACTION_UPDATE_PROGRESS = "example.localcloud.localcloud.sync.update_progress";


    private String TAG = "zzzz_sync_task_service";
    private NotificationManager mNotificationManager;

    public SyncTaskService() {
        super("Upload files to server");
    }

    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<String> fileList = intent.getStringArrayListExtra(SyncTaskService.EXTRA_KEY_FILE_LIST);
        int executeCount = 0;
        for (String path : fileList) {
            executeCount++;
            if (DavClientFactory.client(this).put(path)) {
                Log.d(TAG, String.format("image %s was sent successfully to the server", path));
            } else {
                Log.d(TAG, String.format("image %s didn't sent to the server, something went wrong", path));
            }
            this.sendProgress((executeCount / fileList.size() * 100));
        }
    }

    private void sendProgress(int progress) {
        Intent updateIntent = new Intent();
        updateIntent.setAction(SyncTaskService.INTENT_ACTION_UPDATE_PROGRESS);
        updateIntent.addCategory(Intent.CATEGORY_DEFAULT);
        updateIntent.putExtra(SyncTaskService.EXTRA_KEY_PROGRESS, progress);
        sendBroadcast(updateIntent);
    }
}
