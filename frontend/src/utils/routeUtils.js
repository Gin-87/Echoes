import { lazy } from 'react';
import HomePage from '../Pages/HomePage';
import UserManagement from '../Comp/components/Admin/Sections/UserManagement';
import CharacterManagement from '../Comp/components/Admin/Sections/CharacterManagement';
// ... 其他组件导入

// 组件映射
const componentMap = {
  'HomePage': HomePage,
  'Admin/Sections/UserManagement': UserManagement,
  'Admin/Sections/CharacterManagement': CharacterManagement,
  // ... 其他组件映射
};

// 动态导入组件
export const loadComponent = (componentPath) => {
  return lazy(() => {
    // 添加文件扩展名并使用 @vite-ignore 忽略动态导入警告
    return /* @vite-ignore */ import(`../Comp/components/${componentPath}.jsx`);
  });
};

// 根据后端返回的菜单数据动态生成路由
export const generateRoutes = (menuPermissions) => {
  return menuPermissions.map(menu => ({
    path: menu.resource,
    element: loadComponent(menu.component),
    requiresAuth: true,
    resource: menu.resource
  }));
};

// 定义公开路由列表
export const publicRoutes = [
  '/home',
  '/login',
  '/register'
];

// 检查是否是公开路由
export const isPublicRoute = (path) => {
  return publicRoutes.some(route => path.startsWith(route));
}; 