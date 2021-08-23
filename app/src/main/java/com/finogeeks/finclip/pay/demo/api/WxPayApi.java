package com.finogeeks.finclip.pay.demo.api;

import android.content.Context;
import android.util.Base64;

import com.finogeeks.finclip.pay.demo.Constants;
import com.finogeeks.finclip.pay.demo.http.OrderApi;
import com.finogeeks.finclip.pay.demo.http.module.WxPayOrder;
import com.finogeeks.finclip.pay.demo.util.MessageDigestUtils;
import com.finogeeks.lib.applet.api.AbsApi;
import com.finogeeks.lib.applet.interfaces.ICallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WxPayApi extends AbsApi {

    private final Context context;

    public WxPayApi(Context context) {
        this.context = context;
    }

    private static final String API_NAME_REQUEST_PAYMENT = "requestPayment";

    private static ICallback callback;

    public static void notifyPayResult(int errCode) {
        if (callback != null) {
            if (errCode == 0) {
                callback.onSuccess(null);
            } else {
                JSONObject result = new JSONObject();
                try {
                    result.put("errCode", errCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onFail(result);
            }
            WxPayApi.callback = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    @Override
    public String[] apis() {
        return new String[]{API_NAME_REQUEST_PAYMENT};
    }

    @Override
    public void invoke(String event, JSONObject param, ICallback callback) {
        if (API_NAME_REQUEST_PAYMENT.equals(event)) {
            WxPayApi.callback = null;
            requestPayment(callback);
        }
    }

    private void requestPayment(ICallback callback) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        if (!wxApi.isWXAppInstalled()) {
            JSONObject result = new JSONObject();
            try {
                result.put("errMsg", "WeChat not installed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callback.onFail(result);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("your order url") // 后台下单接口
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(OrderApi.class)
                .wxPayOrder()
                .enqueue(new Callback<WxPayOrder>() {
                    @Override
                    public void onResponse(@NotNull Call<WxPayOrder> call, @NotNull Response<WxPayOrder> response) {
                        WxPayOrder order = response.body();
                        if (order != null && order.code == 1) {
                            WxPayApi.callback = callback;
                            pay(wxApi, order.data.prepay_id);
                        } else {
                            JSONObject result = new JSONObject();
                            try {
                                result.put("errMsg", "order failed");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            callback.onFail(result);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<WxPayOrder> call, @NotNull Throwable t) {
                        JSONObject result = new JSONObject();
                        try {
                            result.put("errMsg", t.getMessage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onFail(result);
                    }
                });
    }

    private void pay(IWXAPI wxApi, String prepayId) {
        // 一般情况，调起微信支付所需的参数来自后台接口返回，此处用本地创建的参数用于演示，
        // 建议开发者从后台接口获取所需参数。
        String timeStamp = String.valueOf((int) (System.currentTimeMillis() / 1000)); // 秒
        Random random = new Random();
        String num = String.valueOf(random.nextInt());
        String nonceStr = MessageDigestUtils.md5(num);
        String sign = Constants.WX_APP_ID + "\n" + timeStamp + "\n" + nonceStr + "\n" + prepayId + "\n";
        sign = MessageDigestUtils.sha256(sign);
        sign = Base64.encodeToString(sign.getBytes(), Base64.DEFAULT);

        PayReq request = new PayReq();
        request.appId = Constants.WX_APP_ID;
        request.partnerId = Constants.WX_WX_MCH_ID;
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay"; // 微信订单详情扩展字符串，暂填写固定值Sign=WXPay
        request.nonceStr = nonceStr; // 随机字符串，不长于32位
        request.timeStamp = timeStamp;
        request.sign = sign;
        wxApi.sendReq(request); // 调起微信支付
    }
}
