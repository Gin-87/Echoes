import React, { useState, useEffect } from "react";
import { AppBar, Toolbar, Typography, Button, Box, IconButton, Avatar, Menu, MenuItem, Divider } from "@mui/material";
import MailOutlineIcon from "@mui/icons-material/MailOutline";
import LanguageIcon from "@mui/icons-material/Language";
import HomeIcon from "@mui/icons-material/Home";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ModelTrainingIcon from '@mui/icons-material/ModelTraining';
import AssignmentIcon from '@mui/icons-material/Assignment';
import GroupIcon from '@mui/icons-material/Group';
import PersonIcon from '@mui/icons-material/Person';
import { useToast } from "../Business/ToastContext.jsx";
import { changeLanguage } from "../../../../utils/i18n/i18n.js";
import { useTranslation } from "react-i18next";
import { useNavigate, useLocation } from "react-router-dom";
import LoginDialog from "../../Auth/LoginDialog";
import { logout, getUserMenus, updateUserLanguage } from "../../../../services/authService";
import SettingsIcon from '@mui/icons-material/Settings';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

const iconMap = {
  'HomeIcon': HomeIcon,
  'AccountCircleIcon': AccountCircleIcon,
  'ModelTrainingIcon': ModelTrainingIcon,
  'AssignmentIcon': AssignmentIcon,
  'GroupIcon': GroupIcon,
  'PersonIcon': PersonIcon,
  'SettingsIcon': SettingsIcon
};

function Header() {
  const [anchorEl, setAnchorEl] = useState(null);
  const [userMenuAnchorEl, setUserMenuAnchorEl] = useState(null);
  const { showToast } = useToast();
  const { t, i18n } = useTranslation();
  const navigate = useNavigate();
  const location = useLocation();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loginDialogOpen, setLoginDialogOpen] = useState(false);
  const [menus, setMenus] = useState([]);
  const [adminMenuAnchor, setAdminMenuAnchor] = useState(null);
  const [languageAnchorEl, setLanguageAnchorEl] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    setIsLoggedIn(!!token);
  }, []);

  useEffect(() => {
    const loadMenus = async () => {
      try {
        const userMenus = await getUserMenus();
        if (Array.isArray(userMenus)) {
          setMenus(processMenus(userMenus));
        } else {
          console.error('Invalid menu data structure');
          setMenus([]);
        }
      } catch (error) {
        console.error('Failed to load menus:', error);
        setMenus([]);
      }
    };
    
    if (isLoggedIn) {
      loadMenus();
    } else {
      setMenus([]);
    }
  }, [isLoggedIn]);

  const processMenus = (menus) => {
    const parentMenus = menus.filter(menu => !menu.parentId);
    return parentMenus.map(menu => ({
      ...menu,
      children: menus.filter(child => child.parentId === menu.id)
    }));
  };

  const handleLogoClick = () => {
    // showToast("Logo 被点击了！", "success");
    navigate('/');
  };

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLanguageMenuOpen = (event) => {
    setLanguageAnchorEl(event.currentTarget);
  };

  const handleLanguageMenuClose = () => {
    setLanguageAnchorEl(null);
  };

  const handleLanguageChange = async (language) => {
    const token = localStorage.getItem('accessToken');
    
    if (token) {
      // 已登录用户：调用接口更新语言偏好
      try {
        await updateUserLanguage(language);
        i18n.changeLanguage(language);
      } catch (error) {
        console.error('Failed to update language preference:', error);
        showToast(t('common.updateFailed'), 'error');
      }
    } else {
      // 未登录用户：只更新前端显示语言
      i18n.changeLanguage(language);
    }
    localStorage.setItem('languagePrefer', language);
    handleLanguageMenuClose();
  };

  const handleUserMenuClick = (event) => {
    setUserMenuAnchorEl(event.currentTarget);
  };

  const handleUserMenuClose = () => {
    setUserMenuAnchorEl(null);
  };

  const handleUserCenter = () => {
    navigate('/user-center');
    handleUserMenuClose();
    showToast(t("userCenter.title"), "info");
  };

  const handleLogout = () => {
    logout();
    handleUserMenuClose();
    setIsLoggedIn(false);
    setMenus([]);
    showToast(t("logout"), "info");
  };

  const handleLoginClick = () => {
    setLoginDialogOpen(true);
  };

  const handleLoginSuccess = () => {
    setIsLoggedIn(true);
    setLoginDialogOpen(false);
  };

  const handleNotifClick = () => {
    showToast(t('notifications.comingSoon'), 'info');
  };

  const isPathActive = (path) => {
    if (path === '/') {
      return location.pathname === path;
    }
    if (path === '/admin') {
      return location.pathname.startsWith('/admin');
    }
    return location.pathname.startsWith(path);
  };

  const handleMenuItemClick = (menu, event) => {
    if (menu.children && menu.children.length > 0) {
      setAdminMenuAnchor(event.currentTarget);
    } else {
      navigate(menu.resource);
      setAdminMenuAnchor(null);
    }
  };

  const handleSubMenuClick = (resource) => {
    navigate(resource);
    setAdminMenuAnchor(null);
  };

  const renderMenuButton = (menu) => {
    const Icon = iconMap[menu.icon];
    const isAdmin = menu.resource === '/admin';
    const isActive = isPathActive(menu.resource);

    return (
      <Button
        onClick={(event) => handleMenuItemClick(menu, event)}
        endIcon={menu.children?.length > 0 && <ExpandMoreIcon />}
        startIcon={Icon && <Icon />}
        sx={{ 
          color: isActive ? "#fff" : "#000",
          backgroundColor: isActive ? "primary.main" : "transparent",
          '&:hover': {
            backgroundColor: isActive 
              ? "primary.dark"
              : "rgba(0, 0, 0, 0.04)",
          },
        }}
      >
        {menu.menuName}
      </Button>
    );
  };

  return (
    <>
      <AppBar 
        position="fixed" 
        sx={{ 
          backgroundColor: "#fff", 
          boxShadow: "0px 2px 4px rgba(0, 0, 0, 0.1)",
          zIndex: (theme) => theme.zIndex.drawer + 1
        }}
      >
        <Toolbar sx={{ justifyContent: "space-between" }}>
          <Box sx={{ display: "flex", alignItems: "center", gap: "20px" }}>
            <Typography variant="h6" color="primary" sx={{ display: "flex", alignItems: "center", gap: "5px" }}>
              <img 
                src="/src/assets/images/logo.png"
                alt="Logo"
                style={{ height: "50px", width: "110px", cursor: "pointer" }}
                onClick={handleLogoClick}
              />
            </Typography>

            {menus.map(menu => (
              <React.Fragment key={menu.id}>
                {renderMenuButton(menu)}
                {menu.children && menu.children.length > 0 && (
                  <Menu
                    anchorEl={adminMenuAnchor}
                    open={Boolean(adminMenuAnchor)}
                    onClose={() => setAdminMenuAnchor(null)}
                    anchorOrigin={{
                      vertical: 'bottom',
                      horizontal: 'left',
                    }}
                    transformOrigin={{
                      vertical: 'top',
                      horizontal: 'left',
                    }}
                    sx={{
                      '& .MuiPaper-root': {
                        minWidth: '120px',
                        boxShadow: '0px 2px 8px rgba(0,0,0,0.15)',
                        mt: 1,
                      },
                    }}
                  >
                    {menu.children.map(childMenu => (
                      <MenuItem 
                        key={childMenu.id}
                        onClick={() => handleSubMenuClick(childMenu.resource)}
                        sx={{
                          py: 1,
                          color: isPathActive(childMenu.resource) ? 'primary.main' : 'inherit',
                          '&:hover': { backgroundColor: 'rgba(25, 118, 210, 0.04)' },
                          '& .MuiSvgIcon-root': {
                            mr: 1,
                          },
                        }}
                      >
                        {iconMap[childMenu.icon] && React.createElement(iconMap[childMenu.icon])}
                        {childMenu.menuName}
                      </MenuItem>
                    ))}
                  </Menu>
                )}
              </React.Fragment>
            ))}
          </Box>

          <Box sx={{ display: "flex", alignItems: "center", gap: "20px" }}>
            {isLoggedIn && (
              <IconButton onClick={handleNotifClick} color="primary">
                <MailOutlineIcon />
              </IconButton>
            )}

            <Box sx={{ position: 'relative' }}>
              <Button
                onClick={handleLanguageMenuOpen}
                startIcon={<LanguageIcon />}
                sx={{
                  color: 'primary.main',
                  textTransform: 'none',
                  minWidth: 'auto',
                }}
              />
              <Menu
                anchorEl={languageAnchorEl}
                open={Boolean(languageAnchorEl)}
                onClose={handleLanguageMenuClose}
              >
                <MenuItem onClick={() => handleLanguageChange('zh')}>
                  中文
                </MenuItem>
                <MenuItem onClick={() => handleLanguageChange('en')}>
                  English
                </MenuItem>
              </Menu>
            </Box>

            {isLoggedIn ? (
              <>
                <IconButton onClick={handleUserMenuClick}>
                  <Avatar sx={{ bgcolor: "primary.main" }}>U</Avatar>
                </IconButton>
                <Menu
                  anchorEl={userMenuAnchorEl}
                  open={Boolean(userMenuAnchorEl)}
                  onClose={handleUserMenuClose}
                >
                  <MenuItem onClick={handleUserCenter}>{t('userCenter.title')}</MenuItem>
                  <Divider />
                  <MenuItem onClick={handleLogout}>{t("logout")}</MenuItem>
                </Menu>
              </>
            ) : (
              <Button
                variant="contained"
                color="primary"
                onClick={handleLoginClick}
              >
                {t('auth.login')}
              </Button>
            )}
          </Box>
        </Toolbar>
      </AppBar>

      <LoginDialog
        open={loginDialogOpen}
        onClose={() => setLoginDialogOpen(false)}
        onSuccess={handleLoginSuccess}
      />
    </>
  );
}

export default Header;
