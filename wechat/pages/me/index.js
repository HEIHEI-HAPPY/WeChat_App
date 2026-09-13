// pages/me/index.js
const auth = require('../../utils/auth.js');
const request = require('../../utils/request.js');

Page({
  data: {
    user: null,
    loading: true
  },

  onShow() {
    this.fetchMe();
  },

  async fetchMe() {
    if (!auth.isLogin()) {
      this.setData({ user: null, loading: false });
      return;
    }
    try {
      const res = await request.get('/api/me');
      if (res.code === 0) {
        this.setData({ user: res.data });
      } else if (res.code === 401) {
        // token 失效，清掉回登录态
        auth.clear();
        this.setData({ user: null });
      }
    } catch (err) {
      console.error('[me] fetchMe fail', err);
    } finally {
      this.setData({ loading: false });
    }
  },

  async onTapLogin() {
    try {
      wx.showLoading({ title: '登录中...' });
      await getApp().wxLogin();
      wx.hideLoading();
      wx.showToast({ title: '登录成功', icon: 'success' });
      this.fetchMe();
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '登录失败', icon: 'none' });
    }
  },

  onTapLogout() {
    auth.clear();
    // 强制 reload 全应用，确保所有页面 globalData 都重置
    wx.reLaunch({ url: '/pages/home/index' });
  },

  // 点击设置/意见反馈 等次要功能的占位
  onTapRow(e) {
    const { type } = e.currentTarget.dataset;
    if (type === 'orders') {
      wx.navigateTo({ url: '/pages/order/list/index' });
    } else if (type === 'addresses') {
      wx.navigateTo({ url: '/pages/address/list/index' });
    } else {
      wx.showToast({ title: '暂未开放', icon: 'none' });
    }
  }
});