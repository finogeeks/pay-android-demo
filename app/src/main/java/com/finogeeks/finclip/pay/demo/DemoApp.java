package com.finogeeks.finclip.pay.demo;

import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.finogeeks.finclip.pay.demo.api.WxPayApi;
import com.finogeeks.lib.applet.client.FinAppClient;
import com.finogeeks.lib.applet.client.FinAppConfig;
import com.finogeeks.lib.applet.client.FinStoreConfig;
import com.finogeeks.lib.applet.interfaces.FinCallback;

import java.util.ArrayList;
import java.util.List;

public class DemoApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initFinClipSdk();
    }

    /**
     * 初始化凡泰SDK，更多配置细节请参考凡泰小程序Android文档：
     * https://www.finclip.com/mop/document/runtime-sdk/sdk-integrate/android.html
     */
    private void initFinClipSdk() {
        if (FinAppClient.INSTANCE.isFinAppProcess(this)) {
            return;
        }

        // 服务器信息集合
        List<FinStoreConfig> storeConfigs = new ArrayList<>();

        // 服务器1的信息
        FinStoreConfig storeConfig1 = new FinStoreConfig(
                "your sdk key", // SDK Key
                "your sdk secret", // SDK Secret
                "https://api.finclip.com", // 服务器地址
                "https://api.finclip.com", // 数据上报服务器地址
                "/api/v1/mop/", // 服务器接口请求路由前缀
                "",
                FinAppConfig.ENCRYPTION_TYPE_SM, // 加密方式，国密:SM，md5: MD5
                false
        );
        storeConfigs.add(storeConfig1);

        FinAppConfig config = new FinAppConfig.Builder()
                .setDebugMode(true)
                .setFinStoreConfigs(storeConfigs) // 服务器信息集合
                .build();

        // SDK初始化结果回调，用于接收SDK初始化状态
        FinCallback<Object> callback = new FinCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                // SDK初始化成功，注册自定义小程序接口
                FinAppClient.INSTANCE.getExtensionApiManager().registerApi(new WxPayApi(DemoApp.this));
            }

            @Override
            public void onError(int code, String error) {
                // SDK初始化失败
                Toast.makeText(DemoApp.this, "SDK初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int status, String error) {
            }
        };

        // 初始化凡泰SDK
        FinAppClient.INSTANCE.init(this, config, callback);
    }
}
