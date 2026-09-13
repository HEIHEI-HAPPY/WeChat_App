// pages/order/detail/index.js
const request = require('../../../utils/request.js');
const { priceYuan } = require('../../../utils/format.js');

const STATUS_TEXT = {
  0: '待支付', 1: '已支付', 2: '制作中', 3: '待取餐',
  4: '已完成', 5: '已取消', 6: '已退款'
};
const STATUS_HINT = {
  0: '请在 30 分钟内完成支付',
  1: '商家正在准备中',
  2: '咖啡师正在制作您的咖啡',
  3: '请到店取餐',
  4: '订单已完成，期待您再次光临',
  5: '订单已取消',
  6: '退款已处理'
};

Page({
  data: {
    id: null,
    order: null,
    statusText: '',
    statusHint: '',
    totalYuan: '',
    submitting: false
  },

  onLoad(query) {
    this.setData({ id: Number(query.id) });
    this.fetchDetail();
  },

  async fetchDetail() {
    try {
      const res = await request.get(`/api/orders/${this.data.id}`);
      if (res.code === 0) {
        const o = res.data;
        this.setData({
          order: o,
          statusText: STATUS_TEXT[o.status] || '',
          statusHint: STATUS_HINT[o.status] || '',
          totalYuan: priceYuan(o.total)
        });
      } else {
        wx.showToast({ title: res.message || '加载失败', icon: 'none' });
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
    }
  },

  async onPay() {
    if (this.data.submitting) return;
    try {
      this.setData({ submitting: true });
      wx.showLoading({ title: '支付中...' });
      // 模拟支付：真实环境用 wx.requestPayment 调起微信支付
      const res = await request.post(`/api/orders/${this.data.id}/pay`);
      wx.hideLoading();
      if (res.code === 0) {
        wx.showToast({ title: '支付成功', icon: 'success' });
        this.fetchDetail();
      } else {
        wx.showToast({ title: res.message || '支付失败', icon: 'none' });
      }
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '网络错误', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  },

  async onCancel() {
    const ok = await new Promise(resolve => {
      wx.showModal({
        title: '取消订单',
        content: '确认取消订单？取消后不可恢复',
        success: r => resolve(r.confirm)
      });
    });
    if (!ok) return;
    try {
      wx.showLoading({ title: '处理中...' });
      const res = await request.post(`/api/orders/${this.data.id}/cancel`);
      wx.hideLoading();
      if (res.code === 0) {
        wx.showToast({ title: '已取消', icon: 'success' });
        this.fetchDetail();
      } else {
        wx.showToast({ title: res.message || '失败', icon: 'none' });
      }
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: '网络错误', icon: 'none' });
    }
  }
});