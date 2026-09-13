// pages/cart/index.js
const request = require('../../../utils/request.js');
const auth = require('../../../utils/auth.js');
const { priceYuan } = require('../../../utils/format.js');

Page({
  data: {
    items: [],          // CartItemDto[]
    itemYuan: {},       // itemId -> '￥xx.xx'
    totalYuan: '￥0.00',
    isLogin: false,
    loading: true
  },

  onShow() {
    this.setData({ isLogin: auth.isLogin() });
    this.fetchCart();
  },

  async onTapLogin() {
    try {
      wx.showLoading({ title: '登录中...' });
      await getApp().wxLogin();
      wx.hideLoading();
      this.setData({ isLogin: true });
      this.fetchCart();
      wx.showToast({ title: '登录成功', icon: 'success' });
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '登录失败', icon: 'none' });
    }
  },

  onPullDownRefresh() {
    this.fetchCart().then(() => wx.stopPullDownRefresh());
  },

  async fetchCart() {
    if (!auth.isLogin()) {
      this.setData({ items: [], loading: false, totalYuan: '￥0.00' });
      return;
    }
    try {
      const res = await request.get('/api/cart');
      if (res.code === 0) {
        const items = res.data || [];
        const itemYuan = {};
        let totalFen = 0;
        items.forEach(it => {
          const lineFen = it.priceSnapshot * it.quantity;
          itemYuan[it.itemId] = priceYuan(lineFen);
          totalFen += lineFen;
        });
        this.setData({
          items,
          itemYuan,
          totalYuan: priceYuan(totalFen),
          loading: false
        });
      } else {
        this.setData({ loading: false });
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
      this.setData({ loading: false });
    }
  },

  async onMinus(e) {
    const { itemId, qty } = e.currentTarget.dataset;
    if (qty <= 1) return;
    try {
      await request.patch(`/api/cart/${itemId}`, { quantity: qty - 1 });
      this.fetchCart();
    } catch (err) {
      wx.showToast({ title: '更新失败', icon: 'none' });
    }
  },

  async onPlus(e) {
    const { itemId, qty } = e.currentTarget.dataset;
    try {
      await request.patch(`/api/cart/${itemId}`, { quantity: qty + 1 });
      this.fetchCart();
    } catch (err) {
      wx.showToast({ title: '更新失败', icon: 'none' });
    }
  },

  async onDelete(e) {
    const { itemId } = e.currentTarget.dataset;
    try {
      await request.delete(`/api/cart/${itemId}`);
      this.fetchCart();
      wx.showToast({ title: '已删除', icon: 'success' });
    } catch (err) {
      wx.showToast({ title: '删除失败', icon: 'none' });
    }
  },

  async onClear() {
    try {
      await request.delete('/api/cart');
      this.fetchCart();
      wx.showToast({ title: '已清空' });
    } catch (err) {
      wx.showToast({ title: '清空失败', icon: 'none' });
    }
  },

  onCheckout() {
    if (!auth.isLogin()) {
      this.onTapLogin();
      return;
    }
    if (this.data.items.length === 0) {
      wx.showToast({ title: '购物车是空的', icon: 'none' });
      return;
    }
    wx.navigateTo({ url: '/pages/order/confirm/index' });
  },

  goHome() {
    wx.switchTab({ url: '/pages/home/index' });
  }
});