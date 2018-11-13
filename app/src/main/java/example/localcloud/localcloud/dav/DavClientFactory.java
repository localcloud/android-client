package example.localcloud.localcloud.dav;

import android.content.Context;


public class DavClientFactory {

    public static DavContextClient client(Context context) {
        return new DavContextClient(context);
    }
}
