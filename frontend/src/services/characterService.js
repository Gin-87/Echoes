// 创建角色
export const createCharacter = async (characterData) => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      throw new Error('No access token found');
    }

    // 将性别映射为后端枚举值
    const genderMap = {
      'male': 'Male',
      'female': 'Female',
      'special': 'Unknown'
    };

    const response = await fetch('/api/characters/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        name: characterData.name,
        code: characterData.code,
        description: characterData.description,
        avatar_url: characterData.avatar_url,
        status: characterData.status,
        backgroundStory: characterData.backgroundStory,
        personalityTraits: characterData.personalityTraits,
        languageStyle: characterData.languageStyle,
        firstLetter: characterData.firstLetter,
        language: characterData.language,
        userAppellation: characterData.userAppellation,
        gender: genderMap[characterData.gender],
        token: token
      })
    });

    const data = await response.json();
    if (data.code === 200) {
      return data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Failed to create character:', error);
    throw error;
  }
};

// 获取角色列表
export const getCharacters = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    const headers = {
      'Content-Type': 'application/json'
    };
    
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch('/api/characters/getAll', {
      headers: headers
    });

    const data = await response.json();
    if (data.code === 200) {
      return data.data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Failed to get characters:', error);
    throw error;
  }
};

// 获取单个角色详情
export const getCharacterById = async (id) => {
  try {
    const response = await fetch(`/api/characters/${id}`);
    const data = await response.json();
    if (data.code === 200) {
      return data.data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Failed to get character:', error);
    throw error;
  }
};

// 获取管理员级别的角色列表
export const getAdminCharacters = async () => {
  try {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      throw new Error('No access token found');
    }

    const response = await fetch('/api/characters/admin/getAll', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    const data = await response.json();
    if (data.code === 200) {
      return data.data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Failed to get admin characters:', error);
    throw error;
  }
}; 