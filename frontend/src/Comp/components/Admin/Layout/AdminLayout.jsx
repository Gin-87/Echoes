import React from 'react';
import { Box, List, ListItem, ListItemIcon, ListItemText, Divider } from '@mui/material';
import { useNavigate, Outlet, useLocation, Navigate } from 'react-router-dom';
import PersonIcon from '@mui/icons-material/Person';
import GroupIcon from '@mui/icons-material/Group';
import { useTranslation } from 'react-i18next';

const drawerWidth = 240;

const AdminLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { t } = useTranslation();

  const menuItems = [
    { 
      text: t('admin.characterManagement'), 
      icon: <GroupIcon />, 
      path: '/admin/characters' 
    },
    { 
      text: t('admin.userManagement'), 
      icon: <PersonIcon />, 
      path: '/admin/users' 
    }
  ];

  return (
    <Box
      sx={{
        display: 'flex',
        height: '100vh',
      }}
    >
      {/* 重定向 */}
      {location.pathname === '/admin' && <Navigate to="/admin/characters" replace />}
      
      {/* 侧边栏 - 固定位置 */}
      <Box
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          borderRight: '1px solid rgba(0, 0, 0, 0.12)',
          backgroundColor: '#f5f5f5',
          height: '100%',
          position: 'fixed',
          left: 0,
        }}
      >
        <List sx={{ p: 0 }}>
          {menuItems.map((item, index) => (
            <React.Fragment key={item.text}>
              <ListItem 
                component="button"
                onClick={() => navigate(item.path)}
                sx={{
                  backgroundColor: location.pathname === item.path ? 'rgba(25, 118, 210, 0.08)' : 'transparent',
                  '&:hover': {
                    backgroundColor: 'rgba(25, 118, 210, 0.12)',
                  },
                  cursor: 'pointer',
                  height: '48px',
                  pl: 2,
                  width: '100%',
                  border: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  textAlign: 'left',
                }}
              >
                <ListItemIcon sx={{
                  color: location.pathname === item.path ? 'primary.main' : 'inherit',
                  minWidth: '40px'
                }}>
                  {item.icon}
                </ListItemIcon>
                <ListItemText 
                  primary={item.text} 
                  sx={{
                    '& .MuiTypography-root': {
                      color: location.pathname === item.path ? 'primary.main' : 'inherit'
                    }
                  }}
                />
              </ListItem>
              {index < menuItems.length - 1 && <Divider />}
            </React.Fragment>
          ))}
        </List>
      </Box>

      {/* 主内容区域 - 可滚动 */}
      <Box
        sx={{
          flex: 1,
          marginLeft: `${drawerWidth}px`,
          overflow: 'auto',
          height: '100%',
        }}
      >
        <Outlet />
      </Box>
    </Box>
  );
};

export default AdminLayout; 