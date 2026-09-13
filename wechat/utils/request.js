// utils/request.js
// 二次封装：直接拿到业务层关心的 data（已处理 code/message）
// 用法：
//   const list = await request.get('/api/goods')
//   await request.post('/api/wx-login', { code })

const app = getApp();

function request({ url, method = 'GET', data, header = {} }) {
  return app.request({ url, method, data, header });
}

module.exports = {
  get: (url, params) => request({ url, method: 'GET', data: params }),
  post: (url, data) => request({ url, method: 'POST', data }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  patch: (url, data) => request({ url, method: 'PATCH', data }),
  delete: (url) => request({ url, method: 'DELETE' }),
};