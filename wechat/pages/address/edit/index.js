// pages/address/edit/index.js
const request = require('../../../utils/request.js');

Page({
  data: {
    id: null,
    form: {
      name: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detail: '',
      isDefault: 0
    },
    submitting: false
  },

  onLoad(query) {
    if (query.id) {
      wx.setNavigationBarTitle({ title: '编辑地址' });
      this.setData({ id: Number(query.id) });
      this.loadAddress(Number(query.id));
    }
  },

  async loadAddress(id) {
    try {
      const res = await request.get('/api/addresses');
      if (res.code === 0) {
        const found = (res.data || []).find(a => a.id === id);
        if (found) this.setData({ form: found });
      }
    } catch (err) {
      wx.showToast({ title: '加载失败', icon: 'none' });
    }
  },

  onInput(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({ [`form.${field}`]: e.detail.value });
  },

  onSwitchDefault(e) {
    this.setData({ 'form.isDefault': e.detail.value ? 1 : 0 });
  },

  async onSave() {
    const f = this.data.form;
    if (!f.name || !f.phone || !f.detail) {
      wx.showToast({ title: '请填写姓名/手机/详细地址', icon: 'none' });
      return;
    }
    if (!/^1[3-9]\d{9}$/.test(f.phone)) {
      wx.showToast({ title: '手机号格式错误', icon: 'none' });
      return;
    }

    try {
      this.setData({ submitting: true });
      wx.showLoading({ title: '保存中...' });
      let res;
      if (this.data.id) {
        res = await request.patch(`/api/addresses/${this.data.id}`, f);
      } else {
        res = await request.post('/api/addresses', f);
      }
      wx.hideLoading();
      if (res.code === 0) {
        wx.showToast({ title: '已保存', icon: 'success' });
        setTimeout(() => wx.navigateBack(), 600);
      } else {
        wx.showToast({ title: res.message || '失败', icon: 'none' });
      }
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '网络错误', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  }
});