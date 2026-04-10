import React from "react";
import Search from "../Comp/components/HomePage/Layout/Search.jsx";
import HomePageDisplay from "../Comp/components/HomePage/Layout/HomePageDisplay.jsx";
import { Box } from "@mui/material";
import { useToast } from '../Comp/components/General/Business/ToastContext';

/**
 * 首页组件
 * 包含:
 * 1. 搜索区域
 * 2. 主要内容展示区域
 * 
 * 布局采用 flex 纵向布局，确保页面占满整个视口高度
 */
function HomePage() {
  const { showToast } = useToast();

  // 搜索角色
  const handleSearch = async (keyword) => {
    try {
      const token = localStorage.getItem('accessToken');
      const headers = token ? { 'Authorization': `Bearer ${token}` } : {};

      if (!keyword) {
        // 如果关键词为空，加载所有角色
        window.dispatchEvent(new Event('reloadCharacters'));
        return;
      }

      const response = await fetch(`/api/characters/search/ByKeyWord?keyword=${encodeURIComponent(keyword)}`, {
        headers: headers
      });

      const data = await response.json();

      if (data.code === 200) {
        // 触发一个自定义事件来更新角色列表
        const searchEvent = new CustomEvent('searchResults', { detail: data.data });
        window.dispatchEvent(searchEvent);
      } else {
        showToast(data.message || "搜索失败", "error");
      }
    } catch (error) {
      console.error('Search failed:', error);
      showToast("搜索失败", "error");
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#fff' }}>
      <Search onSearch={handleSearch} />
      <HomePageDisplay />
    </Box>
  );
}

export default HomePage;
