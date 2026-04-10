import React, { useEffect, useState } from "react";
import { useRoutes } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { getUserPreference } from "../services/authService";
import Header from "../Comp/components/General/Layout/Header";
import { ToastProvider } from "../Comp/components/General/Business/ToastContext";
import LoginDialog from '../Comp/components/Auth/LoginDialog';
import routes from "../router";
import { Box } from "@mui/material";

/**
 * 应用程序的根组件
 * 负责:
 * 1. 全局路由配置
 * 2. 提供全局 Toast 通知功能
 * 3. 应用程序的整体布局结构
 */
function App() {
  const routing = useRoutes(routes);
  const { i18n } = useTranslation();
  const [loginDialogOpen, setLoginDialogOpen] = useState(false);

  // 语言初始化
  useEffect(() => {
    const initLanguage = async () => {
      // 首先获取本地缓存的语言设置
      const cachedLanguage = localStorage.getItem('languagePrefer');
      
      try {
        const preference = await getUserPreference();
        // 如果接口返回了有效的语言设置
        if (preference?.languagePrefer) {
          const languageMap = {
            'CHINESE': 'zh',
            'ENGLISH': 'en'
          };
          const language = languageMap[preference.languagePrefer] || cachedLanguage || 'zh';
          i18n.changeLanguage(language);
        } else if (cachedLanguage) {
          // 如果有缓存语言，使用缓存
          i18n.changeLanguage(cachedLanguage);
        } else {
          // 如果既没有接口返回也没有缓存，使用默认中文
          i18n.changeLanguage('zh');
        }
      } catch (error) {
        console.error('Failed to load user preferences:', error);
        // 如果接口调用失败，优先使用缓存语言
        if (cachedLanguage) {
          i18n.changeLanguage(cachedLanguage);
        } else {
          i18n.changeLanguage('zh');
        }
      }
    };

    initLanguage();
  }, [i18n]);

  // 登录对话框事件监听
  useEffect(() => {
    const handleOpenLoginDialog = () => {
      setLoginDialogOpen(true);
    };
    window.addEventListener('openLoginDialog', handleOpenLoginDialog);
    return () => {
      window.removeEventListener('openLoginDialog', handleOpenLoginDialog);
    };
  }, []);

  return (
    <ToastProvider>
      <Box 
        sx={{ 
          display: "flex", 
          flexDirection: "column",
          minHeight: '100vh',
          paddingTop: '64px', // 为 header 预留空间
        }}
      >
        <Header />
        <Box 
          component="main" 
          sx={{ 
            flex: 1,
            width: '100%',
            backgroundColor: '#fff'
          }}
        >
          {routing}
        </Box>
        <LoginDialog 
          open={loginDialogOpen} 
          onClose={() => setLoginDialogOpen(false)}
          onSuccess={() => setLoginDialogOpen(false)}
        />
      </Box>
    </ToastProvider>
  );
}

export default App;