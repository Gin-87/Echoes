import React from "react";
import Search from "../Comp/components/HomePage/Layout/Search.jsx";  // 复用首页的搜索组件
import MyModelDisplay from "../Comp/components/MyModel/Layout/MyModelDisplay.jsx";
import { Box } from "@mui/material";

function MyModelPage() {
  // 添加排序函数
  const sortCharacters = (chars) => {
    return [...chars].sort((a, b) => {
      // 首先按照收藏数排序
      if (b.num_of_favorites !== a.num_of_favorites) {
        return b.num_of_favorites - a.num_of_favorites;
      }
      // 如果收藏数相同，按照创建时间排序
      return new Date(a.created_at) - new Date(b.created_at);
    });
  };

  // 修改加载数据的函数
  const loadCharacters = async () => {
    setIsLoading(true);
    try {
      const token = localStorage.getItem('accessToken');
      if (!token) {
        navigate('/');
        return;
      }

      const response = await fetch('/api/characters/getByOwner', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      const data = await response.json();

      if (data.code === 200) {
        // 在设置数据前进行排序
        setCharacters(sortCharacters(data.data));
      } else {
        showToast(data.message || "加载失败", "error");
      }
    } catch (error) {
      console.error('Failed to load characters:', error);
      showToast("加载失败", "error");
    } finally {
      setIsLoading(false);
    }
  };

  // 修改删除后的处理函数
  const handleDeleteSuccess = () => {
    loadCharacters();  // 重新加载数据时会自动排序
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        backgroundColor: "#f5f5f5",
      }}
    >
      {/* 搜索区域 - 包含搜索栏和筛选器 */}
      <Box
        sx={{
          padding: "10px 20px",
          backgroundColor: "#fff",
        }}
      >
        <Search />
      </Box>

      {/* 主体内容区域 - 展示角色卡片列表 */}
      <Box
        sx={{
          flex: 1,
          backgroundColor: "#fff",
        }}
      >
        <MyModelDisplay />
      </Box>
    </Box>
  );
}

export default MyModelPage; 