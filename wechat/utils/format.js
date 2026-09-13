// utils/format.js
// 价格、时间格式化

/**
 * 「分」→「元」显示：1899 / 100 = 18.99 → '￥18.99'
 * 全局规则要求：价格统一存「分」
 */
function priceYuan(fen) {
  if (fen == null) return '￥0.00';
  const yuan = (fen / 100).toFixed(2);
  return '￥' + yuan;
}

module.exports = {
  priceYuan
};