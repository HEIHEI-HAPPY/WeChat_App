// pages/order/list/index.js
const request = require('../../../utils/request.js');
const auth = require('../../../utils/auth.js');
const { priceYuan } = require('../../../utils/format.js');

const STATUS_TEXT = {
  0: '待支付', 1: '已支付', 2: '制作中', 3: '待取餐',
  4: '已完成', 5: '已取消', 6: '已退款'
};

Page({
  data: {
    orders: [],
    isLogin: false,
    loading: true
  },

  onShow() {
    this.fetchOrders();
  },

  onPullDownRefresh() {
    this.fetchOrders().then(() => wx.stopPullDownRefresh());
  },

  async fetchOrders() {
    this.setData({ isLogin: auth.isLogin() });
    if (!auth.isLogin()) {
      this.setData({ loading: false, orders: [] });
      return;
    }
    try {
      const res = await request.get('/api/orders');
      if (res.code === 0) {
        const orders = (res.data || []).map(o => ({
          ...o,
          statusText: STATUS_TEXT[o.status] || '未知',
          totalYuan: priceYuan(o.total)
        }));
        this.setData({ orders, loading: false });
      } else {
        this.setData({ loading: false });
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
      this.setData({ loading: false });
    }
  },

  onTapOrder(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/order/detail/index?id=${id}` });
  }
});