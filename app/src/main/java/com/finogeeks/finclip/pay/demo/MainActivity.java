package com.finogeeks.finclip.pay.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.finogeeks.lib.applet.client.FinAppClient;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regToWx();

        findViewById(R.id.startAppletBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开小程序
                FinAppClient.INSTANCE.getAppletApiManager()
                        .startApplet(MainActivity.this, "your appId");
            }
        });
    }

    private void regToWx() {
        IWXAPI api = WXAPIFactory.createWXAPI(this, null);

        // 将应用的appId注册到微信
        api.registerApp(Constants.WX_APP_ID);

        // 建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(Constants.WX_APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }
}
