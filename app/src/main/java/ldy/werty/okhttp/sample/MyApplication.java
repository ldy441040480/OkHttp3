package ldy.werty.okhttp.sample;

import android.app.Application;

import com.ldy.werty.okhttp.OkHttpUtil;

/**
 * Created by lidongyang on 2016/11/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtil.init();
    }
}
