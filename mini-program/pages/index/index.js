// index.js
// 获取应用实例
const app = getApp()

Page({
  data: {},

  onLoad() {},

  toPayment() {
    wx.navigateTo({
      url: '/pages/payment/index',
    })
  }
})
