import React from 'react';
import { Box, Paper, List, ListItem, ListItemText, Typography, ListItemButton } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import UserProfile from '../Sections/UserProfile';
import PointsInfo from '../Sections/PointsInfo';

function UserCenterLayout() {
  const { t } = useTranslation();
  const location = useLocation();
  const navigate = useNavigate();
  const [currentTab, setCurrentTab] = React.useState(0);

  const menuItems = [
    { label: t('userCenter.profile'), value: 0, path: '/user-center' },
    { label: t('userCenter.points'), value: 1, path: '/user-center/points' }
  ];

  // 根据当前路径设置选中的菜单
  React.useEffect(() => {
    // 使用更灵活的匹配方式
    const currentItem = menuItems.find(item => {
      // 去除末尾斜杠后进行比较
      const normalizedPath = location.pathname.replace(/\/$/, '');
      const normalizedItemPath = item.path.replace(/\/$/, '');
      
      // 如果是个人资料页面（根路径），需要精确匹配
      if (item.value === 0) {
        return normalizedPath === normalizedItemPath;
      }
      // 其他页面使用包含匹配
      return normalizedPath.startsWith(normalizedItemPath);
    });

    if (currentItem) {
      setCurrentTab(currentItem.value);
    }
  }, [location.pathname]);

  const handleTabChange = (newValue, path) => {
    setCurrentTab(newValue);
    navigate(path);
  };

  return (
    <Box sx={{ 
      p: 3, 
      width: '80%',
      margin: '0 auto',
      display: 'flex', 
      gap: 3 
    }}>
      {/* 左侧导航菜单 */}
      <Paper 
        elevation={2}
        sx={{ 
          width: 220,
          minWidth: 220,
          minHeight: 600,
          flexShrink: 0,
          borderRadius: 2,
          overflow: 'hidden',
          backgroundColor: '#f8f9fa',
          position: 'sticky',
          top: 24,
          alignSelf: 'flex-start',
          display: 'flex',
          flexDirection: 'column'
        }}
      >
        <Box
          sx={{ 
            p: 3,  // 增加内边距
            pl: 3.5,
            borderBottom: '1px solid rgba(0, 0, 0, 0.08)',
            backgroundColor: 'primary.main',
            color: 'white',
          }}
        >
          <Typography 
            variant="h6" 
            sx={{ 
              fontSize: '1.1rem',
              fontWeight: 500,
              letterSpacing: 0.5
            }}
          >
            {t('userCenter.title')}
          </Typography>
        </Box>

        <List 
          component="nav" 
          sx={{ 
            py: 2,  // 增加上下内边距
            flexGrow: 1,  // 占满剩余空间
            '& .MuiListItem-root + .MuiListItem-root': {
              borderTop: '1px solid rgba(0, 0, 0, 0.06)'
            }
          }}
        >
          {menuItems.map((item, index) => (
            <ListItem key={index} disablePadding>
              <ListItemButton
                selected={currentTab === item.value}
                onClick={() => handleTabChange(item.value, item.path)}
                sx={{
                  py: 2,  // 增加菜单项的高度
                  position: 'relative',
                  '&.Mui-selected': {
                    backgroundColor: 'rgba(25, 118, 210, 0.08)',
                    color: 'primary.main',
                    fontWeight: 500,
                    '&::before': {
                      content: '""',
                      position: 'absolute',
                      left: 0,
                      top: 8,  // 调整指示条的位置
                      bottom: 8,
                      width: 4,
                      backgroundColor: 'primary.main',
                      borderRadius: '0 4px 4px 0'
                    }
                  },
                  '&:hover': {
                    backgroundColor: 'rgba(25, 118, 210, 0.04)',
                    color: 'primary.main',
                    '&::before': {
                      content: '""',
                      position: 'absolute',
                      left: 0,
                      top: 8,  // 调整指示条的位置
                      bottom: 8,
                      width: 4,
                      backgroundColor: 'primary.main',
                      opacity: 0.5,
                      borderRadius: '0 4px 4px 0'
                    }
                  },
                  transition: 'all 0.2s'
                }}
              >
                <ListItemText 
                  primary={item.label}
                  primaryTypographyProps={{
                    fontSize: '0.95rem',
                    fontWeight: currentTab === item.value ? 500 : 400,
                    sx: {
                      pl: 1.5
                    }
                  }}
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Paper>

      {/* 右侧内容区域 */}
      <Paper 
        elevation={2}
        sx={{ 
          flex: 1,
          minWidth: 0,
          p: 4,
          borderRadius: 2
        }}
      >
        {currentTab === 0 && <UserProfile />}
        {currentTab === 1 && <PointsInfo />}
      </Paper>
    </Box>
  );
}

export default UserCenterLayout; 