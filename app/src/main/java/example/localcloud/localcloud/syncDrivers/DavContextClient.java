package example.localcloud.localcloud.syncDrivers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;
import com.thegrizzlylabs.sardineandroid.impl.SardineException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DavContextClient {

    private static String TAG = "lc_dav_client";
    private Sardine sardine;
    private String serverAddress;
    private String id;
    private HashMap<String, Boolean> cache = new HashMap<>();

    DavContextClient(Context context) {
        this.id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
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
            return String.format("%s%s", this.serverAddress, path);
        }
        return String.format("%s/%s", this.serverAddress, path);
    }

    private boolean dirRecursive(String path) {
        StringBuilder currPath = new StringBuilder();
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
        this.cache.put(buildUrl(path), true);
        return true;
    }

    private boolean exist(String path) {
        String addr = buildUrl(path);
        if (this.cache.containsKey(addr)) {
            return this.cache.get(addr);
        }
        try {
            boolean res = getSardine().exists(addr);
            this.cache.put(buildUrl(path), res);
            return res;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return false;
    }

    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * Sends image on a server
     *
     * @param path
     * @return
     */
    public boolean put(String path) {
        File file = new File(path);
        if (exist(file.getParent()) || this.dirRecursive(file.getParent())) {
            try {
                getSardine().put(buildUrl(path), file, this.getMimeType(path), true);
            } catch (SardineException e) {
                Log.d(TAG, String.valueOf(e.getStatusCode()));
                return false;
            } catch (IOException e) {
                Log.d(TAG, String.format("image %s error: %s", buildUrl(path), e.getMessage()));
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}
