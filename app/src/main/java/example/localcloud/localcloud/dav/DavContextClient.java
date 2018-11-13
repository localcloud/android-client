package example.localcloud.localcloud.dav;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;
import com.thegrizzlylabs.sardineandroid.impl.SardineException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class DavContextClient {

    private static String TAG = "lc_dav_client";
    private Sardine sardine;
    private String serverAddress;

    DavContextClient(Context context) {
        this.sardine = new OkHttpSardine();
        this.sardine.setCredentials("nikolay", "nikolay_password", false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.serverAddress = sharedPref.getString("server_address", "https://local-cloud.com");

    }

    private Sardine getSardine() {
        return this.sardine;
    }

    private String buildUrl(String path) {
        if (path.startsWith("/")) {
            return this.serverAddress + path;
        }
        return this.serverAddress + "/" + path;
    }


    private boolean dirRecursive(String path) {
        StringBuilder currPath = new StringBuilder("");
        String[] parts = path.split(Pattern.quote("/"));
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (!part.equals("")) {
                currPath.append("/").append(part);
                try {
                    if (!getSardine().exists(buildUrl(currPath.toString()))) {
                        try {
                            getSardine().createDirectory(buildUrl(currPath.toString()));
                        } catch (SardineException e) {
                            Log.d(TAG, e.getMessage());
                            return false;
                        } catch (IOException e) {
                            return false;
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    private boolean exist(String path) {
        try {
            return getSardine().exists(buildUrl(path));
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * Sends image on a server
     *
     * @param path
     * @return
     */
    public boolean put(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
        if (exist(file.getParent()) || this.dirRecursive(file.getParent())) {
            try {
                getSardine().put(buildUrl(path), bytes);
            } catch (SardineException e) {
                Log.d(TAG, String.valueOf(e.getStatusCode()));
                return false;
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                return false;
            }
        }
        return true;
    }

}
