package example.localcloud.localcloud.components;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class BaseComponent {
    private Context ctx;
    private static HashMap<String, BaseComponent> hm = new HashMap<>();


    protected BaseComponent setContext(Context ctx) {
        this.ctx = ctx;
        return this;
    }

    public Context getContext() {
        return this.ctx;
    }


    public static BaseComponent getOrCreate(Class<?> cls, Context ctx) {
        if (!BaseComponent.hm.containsKey(cls.getName())) {
            Log.d("help", cls.getName());

            try {
                BaseComponent i = (BaseComponent) cls.newInstance();
                i.setContext(ctx);
                BaseComponent.hm.put(
                        cls.getName(), i);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.d("hello", String.format("IllegalAccessException %s", e.getMessage()));
            } catch (InstantiationException e) {
                e.printStackTrace();
                Log.d("hello", String.format("InstantiationException %s", e.getMessage()));
            }

        }
        return BaseComponent.hm.get(cls.getName());
    }
}
