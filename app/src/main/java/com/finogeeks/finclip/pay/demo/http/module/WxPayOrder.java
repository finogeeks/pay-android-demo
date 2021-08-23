package com.finogeeks.finclip.pay.demo.http.module;

public class WxPayOrder {
    public int code;
    public Data data;

    public static class Data {
        public String prepay_id;
    }
}
