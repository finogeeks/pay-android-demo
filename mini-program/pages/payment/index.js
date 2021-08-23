// pages/payment/index.js
Page({
  data: {
    visible: false,
    text: '',
    current: ''
  },
  onLoad() {
    const time = new Date()
    this.setData({
      current: `${time.getFullYear()}-${time.getMonth() + 1}-${time.getDate()}`
    })
  },
  close() {
    this.setData({
      visible: false,
      text: ''
    })
  },
  payment() {
    wx.requestPayment({
      success: (res) => {
        console.log('payment success', res)
        this.setData({
          visible: true,
          text: '支付成功！文档的下载链接将通过短信发送至您的手机，请注意查收。'
        })
      },
      fail: (res) => {
        console.log('payment fail', res)
        this.setData({
          visible: true,
          text: '支付失败！请您重新操作。'
        })
      }
    })
  }
})
