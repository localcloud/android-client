package example.localcloud.localcloud.dav;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;
import com.thegrizzlylabs.sardineandroid.impl.SardineException;
import com.thegrizzlylabs.sardineandroid.util.SardineUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class Dav {
    private static String TAG = "lc_dav_client";
    private static Dav instance;
    private Sardine sardine;
    private String address = "http://192.168.31.156:5566";

    private void Dav() {
        Log.d(TAG, "New Instance DAV CONSTRUCT!");
    }

    public static Dav client() {
        if (Dav.instance == null) {
            Log.d(TAG, "New Instance DAV");
            Dav.instance = new Dav();
        }
        return instance;
    }


    private String buildUrl(String path) {
        if (path.startsWith("/")) {
            return this.address + path;
        }
        return this.address + "/" + path;
    }

    private Sardine getSardine() {
        if (this.sardine == null) {
            this.sardine = new OkHttpSardine();
            this.sardine.setCredentials("nikolay", "nikolay_password", false);
        }
        return this.sardine;
    }


    @NonNull
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
