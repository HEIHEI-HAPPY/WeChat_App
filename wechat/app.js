// app.js
const auth = require('./utils/auth.js');

App({
  globalData: {
    baseUrl: 'http://localhost:3000',
    userInfo: null,
    token: ''
  },

  onLaunch() {
    // 启动时拿缓存里的 token，避免每次都要重新登录
    this.globalData.token = wx.getStorageSync('token') || '';
    if (this.globalData.token) {
      console.log('[app] 已存在 token');
    }
  },

  /**
   * 调 wx.login 拿 code，再调后端换 token + openid
   * 整个微信登录链路只在这一个方法里
   */
  async wxLogin() {
    // 1. wx.login 拿一次性 code
    const { code } = await this.wxLoginCode();
    if (!code) throw new Error('wx.login 未返回 code');

    // 2. 把 code 发给后端换 token + 用户信息
    const res = await this.request({
      url: '/api/wx-login',
      method: 'POST',
      data: { code }
    });

    if (res.code !== 0) throw new Error(res.message || '登录失败');

    // 3. 缓存 token + 用户信息
    this.globalData.token = res.data.token;
    this.globalData.userInfo = {
      id: res.data.userId,
      openid: res.data.openid,
      nickname: res.data.nickname,
      avatarUrl: res.data.avatarUrl
    };
    wx.setStorageSync('token', res.data.token);
    wx.setStorageSync('userInfo', this.globalData.userInfo);

    return this.globalData.userInfo;
  },

  wxLoginCode() {
    return new Promise((resolve, reject) => {
      wx.login({
        success: (res) => resolve(res),
        fail: (err) => reject(err)
      });
    });
  },

  /**
   * 统一 wx.request 封装：
   * - 自动拼 baseUrl
   * - 自动带 Authorization
   * - 统一错误处理
   */
  request({ url, method = 'GET', data, header = {} }) {
    const fullUrl = url.startsWith('http') ? url : this.globalData.baseUrl + url;
    // 从 storage 取 token（会自动跳过过期 token）
    let token = wx.getStorageSync('token') || '';
    if (token) {
      try {
        const payload = JSON.parse(
          decodeURIComponent(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')).split('').map(c =>
            '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
          ).join(''))
        );
        if (payload.exp && payload.exp * 1000 < Date.now()) {
          token = '';
          wx.removeStorageSync('token');
          this.globalData.token = '';
        }
      } catch (e) {
        // 无法解析，当无效
        token = '';
        wx.removeStorageSync('token');
      }
    }
    this.globalData.token = token;

    return new Promise((resolve, reject) => {
      wx.request({
        url: fullUrl,
        method,
        data,
        header: {
          'Content-Type': 'application/json',
          ...(token ? { Authorization: 'Bearer ' + token } : {}),
          ...header
        },
        success: (res) => {
          // 后端约定：{ code:0, message, data } / { code:4xxx, message, data:null }
          if (res.data && typeof res.data.code !== 'undefined') {
            // 如果 401 自动清掉（前端没法区分 token 过期 vs 没带 token，但都是去登录）
            if (res.data.code === 401) {
              wx.removeStorageSync('token');
              this.globalData.token = '';
            }
            resolve(res.data);
          } else {
            reject(new Error('后端返回格式异常'));
          }
        },
        fail: (err) => {
          console.error('[request] fail', err);
          reject(err);
        }
      });
    });
  },

  // 退出登录
  logout() {
    this.globalData.token = '';
    this.globalData.userInfo = null;
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
  }
});