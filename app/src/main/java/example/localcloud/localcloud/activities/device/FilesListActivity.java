package example.localcloud.localcloud.activities.device;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Switch;

import example.localcloud.localcloud.R;
import example.localcloud.localcloud.activities.device.listeners.OnSyncToggleChanged;
import example.localcloud.localcloud.adapters.FilesAdapter;
import example.localcloud.localcloud.contentProviders.MediaContentProvider;


public class FilesListActivity extends AppCompatActivity {
    public static String EXTRA_KEY_SELECTED_FOLDER_POSITION = "EXTRA_KEY_SELECTED_FOLDER_POSITION";

    FilesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_file_list);
        GridView gridView = findViewById(R.id.gv_files);
        int position = getIntent().getIntExtra(EXTRA_KEY_SELECTED_FOLDER_POSITION, 0);
        setTitle(MediaContentProvider.instance(this).fetch().get(position).getName());
        adapter = new FilesAdapter(this, MediaContentProvider.instance(this).fetch(), position);
        gridView.setAdapter(adapter);


        Switch toggleSync = findViewById(R.id.toggleSync);
        toggleSync.setChecked(MediaContentProvider.instance(this).fetch().get(position).isSyncEnabled());
        toggleSync.setOnCheckedChangeListener(new OnSyncToggleChanged(MediaContentProvider.instance(this).fetch().get(position)));
    }
}