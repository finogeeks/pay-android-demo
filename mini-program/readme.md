

### 3、小程序接口调用说明

小程序如需支持 login 和 getUserProfile api，需要在 APP  以注入 API 的方式实现，注入 API 的实现步骤可参阅第二步。

需要注意的是，login 和 getUserProfile 为小程序默认注入接口，无需在小程序根目录配置 FinChatConf 即可调通该两个接口。

APP 未注入相关接口时，调用会报错并执行 fail 回调。

1、调起登录接口

```js
wx.login({
  param1: '', // 按需添加参数，会透传给 APP 处理
  success: (res) => {
    console.log(res)
  },
  fail: (res) => {
    console.log(res.errMsg)
  }
})
```


2、调起获取用户信息接口

```js
wx.getUserProfile({
  success: (res) => {
    console.log('getUserProfile success', res)
  },
  fail: (res) => {
    console.log('getUserProfile fail', res.errMsg)
  }
})
```
