package example.localcloud.localcloud.syncDrivers;

import android.content.Context;


public class DavClientFactory {

    public static DavContextClient client(Context context) {
        return new DavContextClient(context);
    }
}
