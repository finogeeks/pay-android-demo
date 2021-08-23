
let showRes = false // 是否展示 res
let showConfirm = true // 是否需要弹框确认

Page({
  data: {
    showRes,
    visible: false,
    isLogin: false,
    login: '',
    profile: '',
    profileData: {}
  },

  onLoad() {

  },

  showConfirm() {
    if (showConfirm) {
      this.setData({
        visible: true
      })
    } else {
      this.handleLogin()
    }
  },

  hideConfirm() {
    this.setData({
      visible: false
    })
  },

  handleLogin() {
    this.hideConfirm()
    wx.login({
      success: (res) => {
        console.log('login success', res)
        this.setData({
          isLogin: true,
          login: JSON.stringify(res)
        })

        this.getUserProfile()
      },
      fail: (res) => {
        console.log('login fail', res)
        wx.showToast({
          title: res.errMsg || '调用失败',
          icon: 'none'
        })
        this.setData({
          login: JSON.stringify(res)
        })
      }
    })
  },

  getUserProfile() {
    wx.getUserProfile({
      success: (res) => {
        console.log('getUserProfile success', res)
        this.setData({
          profileData: res.userInfo || {},
          profile: JSON.stringify(res)
        })
      },
      fail: (res) => {
        console.log('getUserProfile fail', res)
        wx.showToast({
          title: res.errMsg || '调用失败',
          icon: 'none'
        })
        this.setData({
          profile: JSON.stringify(res)
        })
      }
    })
  },

  logout() {
    this.setData({
      isLogin: false,
      login: '',
      profile: '',
      profileData: {}
    })
  }
})
