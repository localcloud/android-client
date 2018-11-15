package example.localcloud.localcloud.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import example.localcloud.localcloud.R;
import example.localcloud.localcloud.activities.device.FoldersListActivity;
import example.localcloud.localcloud.activities.settings.SettingsActivity;
import example.localcloud.localcloud.contentProviders.MediaContentProvider;
import example.localcloud.localcloud.intentServices.SyncTaskService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FileUploadBroadcastReceiver fileUploadBroadcastReceiver;
    private ProgressBar progressView;
    private MediaContentProvider mediaContentProvider = MediaContentProvider.instance(this);
    private static final String TAG = "FoldersListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                startService(syncTaskService.putExtra(SyncTaskService.EXTRA_KEY_FILE_LIST, mediaContentProvider.fetchOnlyPathForSync()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, SettingsActivity.class);
//            this.startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_on_device) {
            Intent intent = new Intent(this, FoldersListActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.fileUploadBroadcastReceiver);
    }
}
