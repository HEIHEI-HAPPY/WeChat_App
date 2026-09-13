// pages/home/index.js
const request = require('../../utils/request.js');
const auth = require('../../utils/auth.js');

Page({
  data: {
    goods: [],
    cartCount: 0,
    isLoginText: '微信登录',
    loading: true
  },

  onLoad() {
    this.fetchGoods();
  },

  onShow() {
    // 每次回到首页刷新：登录态文字 + 购物车角标
    this.refreshLoginText();
    this.refreshCartCount();
  },

  refreshLoginText() {
    this.setData({
      isLoginText: auth.isLogin() ? '已登录' : '微信登录'
    });
  },

  onPullDownRefresh() {
    this.fetchGoods().then(() => {
      this.refreshCartCount();
      wx.stopPullDownRefresh();
    });
  },

  async fetchGoods() {
    try {
      this.setData({ loading: true });
      const res = await request.get('/api/goods');
      if (res.code === 0) {
        this.setData({ goods: res.data });
      } else {
        wx.showToast({ title: res.message || '加载失败', icon: 'none' });
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  },

  async refreshCartCount() {
    if (!auth.isLogin()) {
      this.setData({ cartCount: 0 });
      return;
    }
    try {
      const res = await request.get('/api/cart/count');
      if (res.code === 0) {
        this.setData({ cartCount: res.data });
        // tabBar 角标
        if (res.data > 0) {
          wx.setTabBarBadge({ index: 0, text: String(res.data) });
        } else {
          wx.removeTabBarBadge({ index: 0 });
        }
      }
    } catch (err) {
      // 静默失败，不打扰用户
    }
  },

  onSelectGoods(e) {
    const { id } = e.detail;
    wx.navigateTo({ url: '/pages/goods/detail/index?id=' + id });
  },

  // 一键登录入口（首页顶部按钮）
  async onTapLogin() {
    if (auth.isLogin()) {
      // 已登录：刷新一下用户信息
      this.refreshCartCount();
      wx.showToast({ title: '已登录', icon: 'success' });
      return;
    }
    try {
      wx.showLoading({ title: '登录中...' });
      const user = await getApp().wxLogin();
      wx.hideLoading();
      this.refreshLoginText();
      this.refreshCartCount();
      wx.showToast({ title: '登录成功', icon: 'success' });
      console.log('[home] wxLogin ok', user);
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '登录失败', icon: 'none' });
    }
  }
});