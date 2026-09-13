// components/goods-card/goods-card.js
const { priceYuan } = require('../../utils/format.js');

Component({
  properties: {
    goods: {
      type: Object,
      value: {}
    }
  },
  data: {
    priceText: '￥0.00'
  },
  observers: {
    'goods.basePrice': function (v) {
      this.setData({ priceText: priceYuan(v || 0) });
    },
    'goods': function (g) {
      // 兜底：如果父组件传了 goods 但 basePrice 没触发 observer
      if (g && g.basePrice != null && !this.data.priceText) {
        this.setData({ priceText: priceYuan(g.basePrice) });
      }
    }
  },
  methods: {
    onTap() {
      const id = (this.data.goods && this.data.goods.id) || 0;
      this.triggerEvent('select', { id });
    }
  }
});