package example.localcloud.localcloud;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import example.localcloud.localcloud.sync.SyncTaskService;


public class FileListActivity extends AppCompatActivity {

    public static ArrayList<ModelImages> al_images = new ArrayList<>();
    boolean boolean_folder;
    AdapterPhotosFolder obj_adapter;
    GridView gv_folder;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String TAG = "zzzz_preview_log";
    private Map selectedState = new HashMap<String, Boolean>();
    private FileUploadBroadcastReceiver fileUploadBroadcastReceiver;
    private ProgressBar progressView;

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
        setContentView(R.layout.activity_file_list);
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
                ArrayList<String> filesList = new ArrayList<String>();
                for (int i = 0; i < al_images.size(); i++) {
                    String path = al_images.get(i).getFolderPath();
                    if (selectedState.containsKey(path) && selectedState.get(path).equals(true)) {
                        filesList.addAll(al_images.get(i).getAllImagesPath());
                    }
                }
                startService(syncTaskService.putExtra(SyncTaskService.EXTRA_KEY_FILE_LIST, filesList));
            }
        });


        gv_folder = (GridView) findViewById(R.id.gv_folder);
        gv_folder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = view.findViewById(R.id.tv_folder);
                if (textView != null) {
                    String path = al_images.get(position).getFolderPath();
                    ImageView cl = view.findViewById(R.id.iv_image_cloud_enabled);
                    Log.d(TAG, "long click " + position + "image folder: " + path);
                    Resources.Theme t = view.getContext().getTheme();
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
            if ((ActivityCompat.shouldShowRequestPermissionRationale(FileListActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(FileListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(FileListActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            Log.e("Else", "Else");
            fn_imagespath();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.fileUploadBroadcastReceiver);
    }

    public ArrayList<ModelImages> fn_imagespath() {
        al_images.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = new MergeCursor(
                new Cursor[]{
                        getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC"),
                        getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, orderBy + " DESC"),
                }
        );

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));
            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getFolderName().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAllImagesPath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setAllImagesPath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                ModelImages obj_model = new ModelImages();
                obj_model.setFolderName(cursor.getString(column_index_folder_name));
                if (al_path.size() > 0) {
                    File f = new File(al_path.get(0));
                    obj_model.setFolderPath(f.getParent());
                }
                obj_model.setAllImagesPath(al_path);

                al_images.add(obj_model);


            }


        }


        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getFolderName());
            for (int j = 0; j < al_images.get(i).getAllImagesPath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAllImagesPath().get(j));
            }
        }
        obj_adapter = new AdapterPhotosFolder(getApplicationContext(), al_images);
        gv_folder.setAdapter(obj_adapter);
        return al_images;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fn_imagespath();
                    } else {
                        Toast.makeText(FileListActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
