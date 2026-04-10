-- 先删除角色-菜单关联表的数据
DELETE FROM role_to_menu;

-- 再删除菜单权限表的数据
DELETE FROM menu_permissions;

-- 然后插入新的菜单权限数据
INSERT INTO menu_permissions 
(menu_name, resource, parent_id, order_num, icon, component, created_at, updated_at) 
VALUES 
-- 主页
('主页', '/home', NULL, 1, 'HomeOutlined', 'Home/HomePage', NOW(), NOW()),

-- 用户菜单
('个人中心', '/user-center', NULL, 2, 'UserOutlined', 'UserCenter/UserCenterPage', NOW(), NOW()),
('我的角色', '/my-models', NULL, 3, 'TeamOutlined', 'MyModel/MyModelPage', NOW(), NOW()),
('我的任务', '/my-tasks', NULL, 4, 'TaskOutlined', 'MyTask/MyTaskPage', NOW(), NOW()),

-- 管理后台
('系统管理', '/admin', NULL, 5, 'SettingOutlined', NULL, NOW(), NOW()),
('角色管理', '/admin/characters', 5, 1, 'UsergroupAddOutlined', 'Admin/Sections/CharacterManagement', NOW(), NOW()),
('用户管理', '/admin/users', 5, 2, 'UserSwitchOutlined', 'Admin/Sections/UserManagement', NOW(), NOW());

-- 插入角色-菜单关联
-- guest(role_id=3) 只能看首页
INSERT INTO role_to_menu (role_id, menu_permission_id) VALUES (3, 1);

-- user(role_id=1) 可以看首页、个人中心、我的角色、我的任务
INSERT INTO role_to_menu (role_id, menu_permission_id) VALUES 
(1, 1),
(1, 2),
(1, 3),
(1, 4);

-- admin(role_id=2) 可以看所有菜单
INSERT INTO role_to_menu (role_id, menu_permission_id) VALUES 
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7); 