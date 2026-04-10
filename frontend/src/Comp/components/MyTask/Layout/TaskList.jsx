import React from 'react';
import { Box, Grid } from '@mui/material';
import TaskCard from '../Atoms/TaskCard';

/**
 * 任务列表组件
 * 负责:
 * 1. 响应式网格布局展示任务卡片
 * 2. 根据屏幕宽度自动调整列数
 * 3. 管理任务数据和渲染任务卡片
 */
const TaskList = () => {
  // 模拟任务数据
  const tasks = [
    {
      id: 1,
      taskName: '哆啦A梦的家书',
      characterName: '哆啦A梦',
      status: 'active',
      lastUpdated: '2024-12-30 12:34 pm',
      createdAt: '2024-12-30 12:34 pm',
      image: 'https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/82ef6441-6663-4180-8e05-280672e3c715/width=800/height=600/18085386.jpeg'
    },
    {
      id: 2,
      taskName: '机器猫的日记',
      characterName: '哆啦A梦',
      status: 'inactive',
      lastUpdated: '2024-12-29 15:20 pm',
      createdAt: '2024-12-29 15:20 pm',
      image: 'https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/3c2bb151-34d7-454f-9f58-ca736aeb851e/width=800/height=600/35658176.jpeg'
    },
    {
      id: 3,
      taskName: '大雄的故事',
      characterName: '大雄',
      status: 'active',
      lastUpdated: '2024-12-28 09:15 am',
      createdAt: '2024-12-28 09:15 am',
      image: 'https://firebasestorage.googleapis.com/v0/b/xchatsair/o/ugc%2FAgACAgUAAxkBAAEZy0pnZUDp7Ynd-G_t22svBEBRJ6QcRwACTsYxG8rkKFcMWbPfGTj8AgEAAwIAA3kAAzYE.jpg?alt=media&token=f7ba5c7d-cd38-4bfe-b57e-5e3d7da0924e'
    },
  ];

  return (
    <Box
      sx={{
        width: "80%", // 两侧留出10%的间距
        margin: "0 auto",
        p: 2
      }}
    >
      <Grid
        container
        spacing={3}
        sx={{
          justifyContent: { xs: 'center', sm: 'flex-start' } // 小屏幕下居中显示
        }}
      >
        {tasks.map((task) => (
          <Grid
            item
            xs={12}
            sm={6}
            md={4}
            lg={3}  // 大屏幕下改为4个卡片
            xl={2}  // 超大屏幕下显示6个卡片
            key={task.id}
            sx={{
              display: 'flex',
              justifyContent: 'center'
            }}
          >
            <TaskCard task={task} />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default TaskList; 