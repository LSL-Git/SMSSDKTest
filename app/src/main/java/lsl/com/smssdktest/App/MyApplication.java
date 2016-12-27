package lsl.com.smssdktest.App;

import android.app.Application;

import cn.smssdk.SMSSDK;

/**
 * Created by M1308_000 on 2016/12/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SMSSDK
        SMSSDK.initSDK(this,"1a21720969643","2e644be6438ca5853448cd188238bc3e");
    }
}
