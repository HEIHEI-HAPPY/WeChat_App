// pages/order/confirm/index.js
const request = require('../../../utils/request.js');
const auth = require('../../../utils/auth.js');
const { priceYuan } = require('../../../utils/format.js');

Page({
  data: {
    items: [],
    itemYuan: {},
    totalYuan: '￥0.00',
    addresses: [],
    selectedAddressId: null,
    selectedAddress: null,
    remark: '',
    pickupType: 1,         // 1=自取 2=外卖
    submitting: false,
    loading: true
  },

  onShow() {
    this.fetchAll();
  },

  async fetchAll() {
    if (!auth.isLogin()) {
      this.setData({ loading: false, items: [] });
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    try {
      // 购物车 + 地址 并行
      const [cartRes, addrRes] = await Promise.all([
        request.get('/api/cart'),
        request.get('/api/addresses')
      ]);

      let items = [];
      if (cartRes.code === 0) items = cartRes.data || [];

      let addresses = [];
      let selectedAddress = null;
      let selectedAddressId = this.data.selectedAddressId;

      if (addrRes.code === 0) {
        addresses = addrRes.data || [];
        // 优先用之前选过的，否则取默认，再否则第一个
        if (selectedAddressId) {
          selectedAddress = addresses.find(a => a.id === selectedAddressId);
        }
        if (!selectedAddress) {
          selectedAddress = addresses.find(a => a.isDefault === 1) || addresses[0];
        }
        if (selectedAddress) selectedAddressId = selectedAddress.id;
      }

      // 算总价
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
        addresses,
        selectedAddress,
        selectedAddressId,
        loading: false
      });
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
      this.setData({ loading: false });
    }
  },

  onPickAddress() {
    wx.navigateTo({ url: '/pages/address/list/index?select=1' });
  },

  onRemarkInput(e) {
    this.setData({ remark: e.detail.value });
  },

  onPickupChange(e) {
    this.setData({ pickupType: e.detail.value });
  },

  onAddAddress() {
    wx.navigateTo({ url: '/pages/address/edit/index' });
  },

  async onSubmit() {
    if (!this.data.selectedAddressId) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' });
      return;
    }
    if (this.data.items.length === 0) {
      wx.showToast({ title: '购物车为空', icon: 'none' });
      return;
    }
    if (this.data.submitting) return;

    try {
      this.setData({ submitting: true });
      wx.showLoading({ title: '提交中...' });
      const res = await request.post('/api/orders', {
        addressId: this.data.selectedAddressId,
        pickupType: this.data.pickupType,
        remark: this.data.remark
      });
      wx.hideLoading();
      if (res.code === 0) {
        // 跳到订单详情页
        wx.redirectTo({ url: `/pages/order/detail/index?id=${res.data.id}` });
      } else {
        wx.showToast({ title: res.message || '提交失败', icon: 'none' });
      }
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '网络错误', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  }
});