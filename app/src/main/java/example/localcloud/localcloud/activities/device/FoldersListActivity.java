package example.localcloud.localcloud.activities.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import example.localcloud.localcloud.R;
import example.localcloud.localcloud.adapters.FolderAdapter;
import example.localcloud.localcloud.contentProviders.MediaContentProvider;
import example.localcloud.localcloud.intentServices.SyncTaskService;

public class FoldersListActivity extends AppCompatActivity {
    GridView gv_folder;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String TAG = "FoldersListActivity";
    private Map selectedState = new HashMap<String, Boolean>();
    private FileUploadBroadcastReceiver fileUploadBroadcastReceiver;
    private ProgressBar progressView;
    private MediaContentProvider mediaContentProvider = MediaContentProvider.instance(this);

    public class FileUploadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Integer result = intent.getIntExtra(SyncTaskService.EXTRA_KEY_PROGRESS, 0);
            Log.d(TAG, String.format("progress %d", result));
            progressView.setProgress(result, true);
            if (progressView.getProgress() == 100) {
                progressView.setVisibility(ProgressBar.INVISIBLE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_folder_list);
        this.progressView = findViewById(R.id.determinateBar);
        progressView.setVisibility(ProgressBar.INVISIBLE);
        this.fileUploadBroadcastReceiver = new FileUploadBroadcastReceiver();
        IntentFilter updateIntentFilter = new IntentFilter(SyncTaskService.INTENT_ACTION_UPDATE_PROGRESS);
        updateIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(fileUploadBroadcastReceiver, updateIntentFilter);


        final Intent syncTaskService = new Intent(this, SyncTaskService.class);
        FloatingActionButton fab = findViewById(R.id.send_files);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressView.setVisibility(ProgressBar.VISIBLE);
                startService(syncTaskService.putExtra(SyncTaskService.EXTRA_KEY_FILE_LIST, mediaContentProvider.fetchOnlyPath()));
            }
        });


        gv_folder = (GridView) findViewById(R.id.gv_folder);

        final FoldersListActivity self = this;

        gv_folder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(self, FilesListActivity.class);
                intent.putExtra(FilesListActivity.EXTRA_KEY_SELECTED_FOLDER_POSITION, position);
                self.startActivity(intent);

            }
        });

        gv_folder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = view.findViewById(R.id.tv_folder);
                if (textView != null) {
                    String path = mediaContentProvider.fetch().get(position).getPath();
                    ImageView cl = view.findViewById(R.id.iv_image_cloud_enabled);

                    if (selectedState.containsKey(path) && selectedState.get(path).equals(true)) {
                        selectedState.put(path, false);
                        cl.setVisibility(ImageView.INVISIBLE);
                    } else {
                        selectedState.put(path, true);
                        cl.setVisibility(ImageView.VISIBLE);
                    }
                }
                return false;
            }
        });


        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(FoldersListActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(FoldersListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(FoldersListActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            syncImages();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.fileUploadBroadcastReceiver);
    }

    public void syncImages() {
        gv_folder.setAdapter(new FolderAdapter(getApplicationContext(), mediaContentProvider.fetch()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        syncImages();
                    } else {
                        Toast.makeText(FoldersListActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
