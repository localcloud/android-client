package example.localcloud.localcloud.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import example.localcloud.localcloud.R;
import example.localcloud.localcloud.activities.device.FolderListActivity;
import example.localcloud.localcloud.contentProviders.MediaContentProvider;


public class PhotosActivity extends AppCompatActivity {
    int int_position;
    private GridView gridView;
    GridViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);
        gridView = (GridView) findViewById(R.id.gv_folder);
        int_position = getIntent().getIntExtra("value", 0);
        adapter = new GridViewAdapter(this, FolderListActivity.al_images, int_position);
        gridView.setAdapter(adapter);
    }
}
