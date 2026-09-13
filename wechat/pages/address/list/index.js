// pages/address/list/index.js
const request = require('../../../utils/request.js');
const auth = require('../../../utils/auth.js');

Page({
  data: {
    addresses: [],
    selectMode: false,   // 选择模式（订单确认页进来用）
    selectedId: null,
    loading: true
  },

  onLoad(query) {
    // selectMode=1 表示从订单确认页跳转过来要选地址
    if (query.select === '1') {
      this.setData({ selectMode: true });
      wx.setNavigationBarTitle({ title: '选择收货地址' });
    }
  },

  onShow() {
    this.fetchAddresses();
  },

  onPullDownRefresh() {
    this.fetchAddresses().then(() => wx.stopPullDownRefresh());
  },

  async fetchAddresses() {
    if (!auth.isLogin()) {
      this.setData({ loading: false, addresses: [] });
      return;
    }
    try {
      const res = await request.get('/api/addresses');
      if (res.code === 0) {
        this.setData({ addresses: res.data || [], loading: false });
      } else {
        this.setData({ loading: false });
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
      this.setData({ loading: false });
    }
  },

  onAdd() {
    wx.navigateTo({ url: '/pages/address/edit/index' });
  },

  onEdit(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({ url: `/pages/address/edit/index?id=${id}` });
  },

  async onDelete(e) {
    const { id } = e.currentTarget.dataset;
    const ok = await new Promise(resolve => {
      wx.showModal({
        title: '删除地址',
        content: '确认删除这个地址吗？',
        success: r => resolve(r.confirm)
      });
    });
    if (!ok) return;
    try {
      const res = await request.delete(`/api/addresses/${id}`);
      if (res.code === 0) {
        wx.showToast({ title: '已删除', icon: 'success' });
        this.fetchAddresses();
      } else {
        wx.showToast({ title: res.message || '失败', icon: 'none' });
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
    }
  },

  /** 选择模式：选中并返回 */
  onSelect(e) {
    const { id } = e.currentTarget.dataset;
    if (!this.data.selectMode) return;
    // 把选中的 id 存到上一页，全的页页 onShow 时读
    const pages = getCurrentPages();
    const prev = pages[pages.length - 2];
    if (prev) {
      prev.setData({ selectedAddressId: Number(id) });
    }
    wx.navigateBack();
  },

  async onSetDefault(e) {
    const { id } = e.currentTarget.dataset;
    try {
      const res = await request.patch(`/api/addresses/${id}`, { isDefault: 1 });
      if (res.code === 0) {
        wx.showToast({ title: '已设为默认', icon: 'success' });
        this.fetchAddresses();
      }
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
    }
  }
});