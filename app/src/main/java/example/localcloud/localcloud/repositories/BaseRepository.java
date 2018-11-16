package example.localcloud.localcloud.repositories;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import example.localcloud.localcloud.components.BaseComponent;

public abstract class BaseRepository {
    private static HashMap<String, BaseRepository> hm = new HashMap<>();

    public static BaseRepository getOrCreate(Class<?> cls) {
        if (!BaseRepository.hm.containsKey(cls.getName())) {
            Log.d("help", cls.getName());

            try {
                BaseRepository i = (BaseRepository) cls.newInstance();
                BaseRepository.hm.put(
                        cls.getName(), i);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.d("hello", String.format("IllegalAccessException %s", e.getMessage()));
            } catch (InstantiationException e) {
                e.printStackTrace();
                Log.d("hello", String.format("InstantiationException %s", e.getMessage()));
            }

        }
        return BaseRepository.hm.get(cls.getName());
    }
}
