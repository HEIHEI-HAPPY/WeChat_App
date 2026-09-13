// pages/goods/detail/index.js
const request = require('../../../utils/request.js');
const auth = require('../../../utils/auth.js');
const { priceYuan } = require('../../../utils/format.js');

Page({
  data: {
    goodsId: null,
    goods: null,
    specs: [],          // [{ name, options: [{label, extraPrice}] }]
    selected: {},       // { '杯型': '中杯', '温度': '热' }
    quantity: 1,
    totalYuan: '',      // 当前总价的「元」显示
    submitting: false,
    loading: true
  },

  onLoad(query) {
    this.setData({ goodsId: Number(query.id) });
    this.fetchGoods();
  },

  async fetchGoods() {
    try {
      const res = await request.get(`/api/goods/${this.data.goodsId}`);
      if (res.code !== 0) {
        wx.showToast({ title: res.message || '加载失败', icon: 'none' });
        return;
      }
      const goods = res.data;

      // 解析 specs JSON 字符串
      let specs = [];
      if (goods.specs) {
        try { specs = JSON.parse(goods.specs); } catch (e) { specs = []; }
      }

      // 默认选第一项
      const selected = {};
      specs.forEach(spec => {
        if (spec.options && spec.options.length > 0) {
          selected[spec.name] = spec.options[0].label;
        }
      });

      this.setData({
        goods,
        specs,
        selected,
        loading: false
      });
      this.recalcTotal();
    } catch (err) {
      wx.showToast({ title: '网络错误', icon: 'none' });
      this.setData({ loading: false });
    }
  },

  /** 选规格：{ 杯型: '大杯' } */
  onSelectSpec(e) {
    const { specName, value, extraPrice } = e.currentTarget.dataset;
    const selected = { ...this.data.selected, [specName]: value };
    this.setData({ selected });
    this.recalcTotal(extraPrice);
  },

  /** 数量 - */
  onMinus() {
    if (this.data.quantity > 1) {
      this.setData({ quantity: this.data.quantity - 1 });
      this.recalcTotal();
    }
  },

  /** 数量 + */
  onPlus() {
    if (this.data.quantity < 99) {
      this.setData({ quantity: this.data.quantity + 1 });
      this.recalcTotal();
    }
  },

  /** 重算总价：基础价 + 各规格 extraPrice */
  recalcTotal(latestExtra = null) {
    if (!this.data.goods) return;
    let extra = 0;
    // 把每个当前选中对应的 extraPrice 累加
    this.data.specs.forEach(spec => {
      const chosen = this.data.selected[spec.name];
      const opt = (spec.options || []).find(o => o.label === chosen);
      if (opt && opt.extraPrice) extra += opt.extraPrice;
    });
    const totalFen = (this.data.goods.basePrice + extra) * this.data.quantity;
    this.setData({ totalYuan: priceYuan(totalFen) });
  },

  /** [加入购物车] 按钮 */
  async onAddCart() {
    if (!auth.isLogin()) {
      this.promptLogin();
      return;
    }
    if (this.data.submitting) return;
    try {
      this.setData({ submitting: true });
      wx.showLoading({ title: '加入中...' });

      const res = await request.post('/api/cart', {
        goodsId: this.data.goodsId,
        sku: JSON.stringify(this.data.selected),
        quantity: this.data.quantity
      });

      wx.hideLoading();
      if (res.code === 0) {
        wx.showToast({ title: '已加入购物车', icon: 'success' });
      } else {
        wx.showToast({ title: res.message || '失败', icon: 'none' });
      }
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '网络错误', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  },

  /** [立即购买]：加购 → 跳到订单确认 */
  async onBuyNow() {
    if (!auth.isLogin()) {
      this.promptLogin();
      return;
    }
    if (this.data.submitting) return;
    try {
      this.setData({ submitting: true });
      wx.showLoading({ title: '处理中...' });
      // 先加购
      const res = await request.post('/api/cart', {
        goodsId: this.data.goodsId,
        sku: JSON.stringify(this.data.selected),
        quantity: this.data.quantity
      });
      wx.hideLoading();
      if (res.code !== 0) {
        wx.showToast({ title: res.message || '失败', icon: 'none' });
        return;
      }
      // 跳到订单确认页
      wx.navigateTo({ url: '/pages/order/confirm/index' });
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '网络错误', icon: 'none' });
    } finally {
      this.setData({ submitting: false });
    }
  },

  /** 底部购物车图标 */
  goCart() {
    if (!auth.isLogin()) {
      this.promptLogin();
      return;
    }
    wx.navigateTo({ url: '/pages/cart/index' });
  },

  /** 引导登录：wx.login → 成功后再继续原动作 */
  async promptLogin() {
    try {
      wx.showLoading({ title: '登录中...' });
      await getApp().wxLogin();
      wx.hideLoading();
      wx.showToast({ title: '登录成功，请重试', icon: 'success' });
    } catch (err) {
      wx.hideLoading();
      wx.showToast({ title: err.message || '登录失败', icon: 'none' });
    }
  }
});