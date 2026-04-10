import { useToast } from '../Comp/components/General/Business/ToastContext';

// 创建一个全局的 toast 实例
let toastInstance = null;

// 设置 toast 实例的函数
export const setToastInstance = (instance) => {
  toastInstance = instance;
};

// 创建请求拦截器
const request = async (url, options = {}) => {
  // 添加 token 到 headers
  const token = localStorage.getItem('accessToken');
  if (token) {
    options.headers = {
      ...options.headers,
      'Authorization': `Bearer ${token}`
    };
  }

  try {
    const response = await fetch(url, options);
    const data = await response.json();

    // 处理 401 错误
    if (response.status === 401 || data.code === 401) {
      // 清除过期的 token
      localStorage.removeItem('accessToken');
      // 触发登录框
      window.dispatchEvent(new Event('openLoginDialog'));
      if (toastInstance) {
        toastInstance.showToast("登录已过期，请重新登录", "warning");
      }
      return null;
    }

    return data;
  } catch (error) {
    console.error('Request failed:', error);
    if (toastInstance) {
      toastInstance.showToast("请求失败", "error");
    }
    return null;
  }
};

// GET 请求
request.get = (url, options = {}) => {
  return request(url, { ...options, method: 'GET' });
};

// POST 请求
request.post = (url, data, options = {}) => {
  return request(url, {
    ...options,
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    body: JSON.stringify(data),
  });
};

// PUT 请求
request.put = (url, data, options = {}) => {
  return request(url, {
    ...options,
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    body: JSON.stringify(data),
  });
};

// DELETE 请求
request.delete = (url, options = {}) => {
  return request(url, { ...options, method: 'DELETE' });
};

export default request; 