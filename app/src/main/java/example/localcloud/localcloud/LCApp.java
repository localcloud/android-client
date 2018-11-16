package example.localcloud.localcloud;

import android.app.Application;

import example.localcloud.localcloud.components.BaseComponent;
import example.localcloud.localcloud.components.Components;
import example.localcloud.localcloud.repositories.BaseRepository;
import example.localcloud.localcloud.repositories.Repositories;

public class LCApp extends Application {

    private static LCApp i;

    public void onCreate() {
        super.onCreate();
        i = this;
    }


    public static LCApp app() {
        return i;
    }


    public Components components() {
        return (Components) BaseComponent.getOrCreate(Components.class, this);
    }

    public Repositories repositories() {
        return (Repositories) BaseRepository.getOrCreate(Repositories.class);
    }
}
