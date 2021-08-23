package com.finogeeks.finclip.pay.demo.http;

import com.finogeeks.finclip.pay.demo.http.module.WxPayOrder;

import retrofit2.Call;
import retrofit2.http.POST;

public interface OrderApi {

    // 微信支付下单
    @POST("/mop/wechat-auth/api/order")
    Call<WxPayOrder> wxPayOrder();
}
