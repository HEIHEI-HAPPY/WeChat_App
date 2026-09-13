// utils/auth.js
// 封装 token + 用户信息的存储

/**
 * 解码 JWT payload（不验签，只读 exp）
 * JWT 格式：header.payload.signature（base64url）
 */
function decodeJwtPayload(token) {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    const payload = parts[1];
    // base64url -> base64
    const padded = payload.replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
      atob(padded).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join('')
    );
    return JSON.parse(json);
  } catch (e) {
    return null;
  }
}

/** 检查 token 是否过期（提前 60 秒判定为过期）*/
function isTokenValid(token) {
  if (!token) return false;
  const payload = decodeJwtPayload(token);
  if (!payload || !payload.exp) return false;
  const nowSec = Math.floor(Date.now() / 1000);
  return payload.exp > nowSec + 60;
}

module.exports = {
  /** 是否登录（验证 token 是否存在 + 未过期）*/
  isLogin() {
    const t = wx.getStorageSync('token');
    if (!t) return false;
    if (!isTokenValid(t)) {
      // token 过期，顺手清掉
      wx.removeStorageSync('token');
      return false;
    }
    return true;
  },

  /** 仅看 token 是否存在（不验过期，给 splash 页用）*/
  hasToken() {
    return !!wx.getStorageSync('token');
  },

  getToken() {
    const t = wx.getStorageSync('token') || '';
    if (!isTokenValid(t)) return '';
    return t;
  },

  getUserInfo() {
    if (!this.isLogin()) {
      // 清掉失效数据
      wx.removeStorageSync('userInfo');
      return null;
    }
    return wx.getStorageSync('userInfo') || null;
  },

  setAuth(token, userInfo) {
    wx.setStorageSync('token', token);
    wx.setStorageSync('userInfo', userInfo);
    const app = getApp();
    app.globalData.token = token;
    app.globalData.userInfo = userInfo;
  },

  clear() {
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    const app = getApp();
    app.globalData.token = '';
    app.globalData.userInfo = null;
  },

  // 导出供外部使用
  _isTokenValid: isTokenValid
};