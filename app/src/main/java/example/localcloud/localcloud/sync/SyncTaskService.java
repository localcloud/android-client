package example.localcloud.localcloud.sync;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import example.localcloud.localcloud.dav.DavClientFactory;

public class SyncTaskService extends IntentService {

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

        Integer percent = intent.getIntExtra("percent", 0);
        Log.d(TAG, "percent: " + percent);
        String imgPath = intent.getStringExtra("img_path");
        if (DavClientFactory.client(this).put(imgPath)) {
            Log.d(TAG, String.format("image %s was sent successfully to the server", imgPath));
        } else {
            Log.d(TAG, String.format("image %s didn't sent to the server, something went wrong", imgPath));
        }
        Intent updateIntent = new Intent();
        updateIntent.setAction("cejixo3.update");
        updateIntent.addCategory(Intent.CATEGORY_DEFAULT);
        updateIntent.putExtra("progress", percent);
        sendBroadcast(updateIntent);
    }
}
