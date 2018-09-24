package example.localcloud.localcloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);


        Button button = findViewById(R.id.button);
        this.l = (ListView) findViewById(R.id.lvMain);

        Thread thread = new Thread(new Runnable() {
            private Sardine s;

            @Override
            public void run() {

                this.s = new OkHttpSardine();
                s.setCredentials("username", "password");

                try {
                    List<DavResource> resources = s.list("http://192.168.31.156:5555");

                    for (DavResource dr : resources) {
                        Log.d("cejixo3", dr.getPath());
                    }
                } catch (IOException e) {
                    Log.d("cejixo3", "Error" + e.toString());
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cejixo3", "clicked");
                chooseDir();
            }
        });

//
//        Log.d("Files", "Path: " + Environment.getExternalStorageDirectory().toString());
//        String path = Environment.getExternalStorageDirectory().toString()+"/DCIM";
//        Log.d("Files", "Path: " + path);
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//        }
    }


    public void chooseDir() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 9999:

                Uri uri = data.getData();
                if (uri != null) {
                    final List<String> where = new ArrayList<String>();
                    DocumentFile df = DocumentFile.fromTreeUri(this, uri);

                    assert df != null;
                    for (DocumentFile cdf : df.listFiles()) {
                        where.add(cdf.getName());
                        Log.d("cejixo3", cdf.getUri().getPath());

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, where);
                    l.setAdapter(adapter);

                    l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("cejixo3", where.get(i));
                        }
                    });
                }
                break;
        }
    }
}
