package example.localcloud.localcloud.components;

import android.content.Context;

public class Components extends BaseComponent {


    public DatabasesComponent db() {
        return (DatabasesComponent) BaseComponent.getOrCreate(DatabasesComponent.class, this.getContext());
    }
}
