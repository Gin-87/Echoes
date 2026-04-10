-- 更新现有菜单的顺序和图标
UPDATE menu_permissions 
SET order_num = 1,
    component = 'HomePage',
    icon = 'HomeIcon'
WHERE menu_name = '主页';

UPDATE menu_permissions 
SET order_num = 2,
    component = 'MyModel/MyModelPage',
    icon = 'ModelTrainingIcon'
WHERE menu_name = '我的模型';

UPDATE menu_permissions 
SET order_num = 3,
    component = 'MyTask/MyTaskPage',
    icon = 'AssignmentIcon'
WHERE menu_name = '我的任务';

-- 插入后台管理主菜单
INSERT INTO menu_permissions 
(menu_name, resource, component, icon, parent_id, order_num, created_at, updated_at)
VALUES 
('后台管理', '/admin', 'Admin/AdminLayout', 'SettingsIcon', NULL, 4, NOW(), NOW());

-- 更新角色管理和用户管理为后台管理的子菜单
UPDATE menu_permissions 
SET parent_id = (SELECT id FROM menu_permissions WHERE menu_name = '后台管理'),
    order_num = 1,
    component = 'Admin/Sections/CharacterManagement',
    icon = 'GroupIcon'
WHERE menu_name = '角色管理';

UPDATE menu_permissions 
SET parent_id = (SELECT id FROM menu_permissions WHERE menu_name = '后台管理'),
    order_num = 2,
    component = 'Admin/Sections/UserManagement',
    icon = 'PersonIcon'
WHERE menu_name = '用户管理'; 