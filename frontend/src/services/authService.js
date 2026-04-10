import JSEncrypt from 'jsencrypt';

// 获取 RSA 公钥
export const getPublicKey = async () => {
  const response = await fetch('/rsa-service/rsa/public-key', {
    headers: {
      'Accept': 'text/plain',
    }
  });
  if (!response.ok) {
    throw new Error('Failed to get public key');
  }
  const publicKey = await response.text();
  console.log('Received public key:', publicKey);
  
  // 添加 PEM 格式的头尾
  return `-----BEGIN PUBLIC KEY-----\n${publicKey}\n-----END PUBLIC KEY-----`;
};

// 加密密码
export const encryptPassword = async (password) => {
  const publicKey = await getPublicKey();
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(publicKey);
  
  // 使用与后端相同的加密配置
  const encryptedPassword = encrypt.encrypt(password);
  if (!encryptedPassword) {
    throw new Error('Encryption failed');
  }
  return encryptedPassword;
};



// 注册用户
export const register = async (userData) => {
  try {
    const response = await fetch('/api/users/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
      // 添加防止重复提交
      cache: 'no-store'
    });
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Registration error:', error);
    throw error;
  }
};

// 登录
export const login = async (loginData) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(loginData)
  });
  
  const data = await response.json();
  if (data.code === 200) {
    // 存储 token 时不要包含 Bearer 前缀
    localStorage.setItem('accessToken', data.data.accessToken);
    localStorage.setItem('refreshToken', data.data.refreshToken);
  }
  return data;
};

// 退出登录
export const logout = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
};

// 添加一个测试函数
window.testEncryption = async () => {
  const password = '111';
  try {
    const encryptedPassword = await encryptPassword(password);
    console.log('Encrypted password:', encryptedPassword);
  } catch (error) {
    console.error('Encryption failed:', error);
  }
};

// 获取用户菜单权限
export const getUserMenus = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      console.warn('No access token found');
      return [];
    }

    const response = await fetch('/api/permissions/menu', {
      headers: {
        'Authorization': `Bearer ${token}`,  // 确保 token 格式正确
        'Content-Type': 'application/json'
      }
    });

    const data = await response.json();
    
    if (data.code === 200 && Array.isArray(data.data)) {
      // 对菜单数据进行排序
      return data.data.sort((a, b) => a.orderNum - b.orderNum);
    } else {
      console.error('Invalid menu data received:', data);
      return [];
    }
  } catch (error) {
    console.error('Failed to fetch user menus:', error);
    return [];
  }
};

// 获取用户偏好设置
export const getUserPreference = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    const headers = {
      'Content-Type': 'application/json'
    };
    
    // 如果有 token，添加到请求头
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch('/api/user-prefer/get', {
      headers: headers
    });

    const data = await response.json();
    if (data.code === 200) {
      return data.data;
    } else {
      console.error('Failed to get user preference:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Failed to fetch user preference:', error);
    return null;
  }
};

// 更新用户语言偏好
export const updateUserLanguage = async (language) => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      throw new Error('No access token found');
    }

    // 将 zh/en 映射为 CHINESE/ENGLISH
    const languageMap = {
      'zh': 'CHINESE',
      'en': 'ENGLISH'
    };

    const response = await fetch('/api/user-prefer/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        selectedLanguage: languageMap[language]
      })
    });

    const data = await response.json();
    if (data.code === 200) {
      return data.data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Failed to update user language:', error);
    throw error;
  }
};