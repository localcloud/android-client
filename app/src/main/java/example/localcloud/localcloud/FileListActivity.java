package example.localcloud.localcloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
                    List<String> where = new ArrayList<String>();

                    DocumentFile df = DocumentFile.fromTreeUri(this, uri);
                    for (DocumentFile cdf : df.listFiles()) {
                        where.add(cdf.getName());
                        Log.d("cejixo3", cdf.getUri().getPath());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, where);
                    l.setAdapter(adapter);
                }
                break;
        }
    }
}
