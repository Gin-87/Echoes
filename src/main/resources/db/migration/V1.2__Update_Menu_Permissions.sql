-- 更新菜单权限的 component 和 icon
UPDATE menu_permissions 
SET 
    component = CASE id
        WHEN 1 THEN 'HomePage'
        WHEN 2 THEN 'UserCenter/UserCenterPage'
        WHEN 3 THEN 'MyModel/MyModelPage'
        WHEN 4 THEN 'MyTask/MyTaskPage'
        WHEN 5 THEN 'Admin/Sections/CharacterManagement'
        WHEN 6 THEN 'Admin/Sections/UserManagement'
    END,
    icon = CASE id
        WHEN 1 THEN 'HomeIcon'
        WHEN 2 THEN 'AccountCircleIcon'
        WHEN 3 THEN 'ModelTrainingIcon'
        WHEN 4 THEN 'AssignmentIcon'
        WHEN 5 THEN 'GroupIcon'
        WHEN 6 THEN 'PersonIcon'
    END
WHERE id BETWEEN 1 AND 6; 