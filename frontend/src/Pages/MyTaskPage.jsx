import React from 'react';
import { Box } from '@mui/material';
import TaskSearch from '../Comp/components/MyTask/Atoms/TaskSearch';
import TaskList from '../Comp/components/MyTask/Layout/TaskList';

/**
 * 任务管理页面
 * 包含:
 * 1. 任务搜索和筛选区域
 * 2. 任务列表展示区域
 * 
 * 布局采用与首页相同的结构，确保视觉统一
 */
function MyTaskPage() {
  return (
    
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        backgroundColor: "#f5f5f5",
      }}
    >
      {/* 搜索和筛选区域 */}
      <Box
        sx={{
          padding: "10px 20px",
          backgroundColor: "#fff",
        }}
      >
        <TaskSearch />
      </Box>

      {/* 任务列表区域 */}
      <Box
        sx={{
          flex: 1,
          backgroundColor: "#fff",
        }}
      >
        <TaskList />
      </Box>
    </Box>
  );
}

export default MyTaskPage; 