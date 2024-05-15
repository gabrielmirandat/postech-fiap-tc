-- Insert roles
INSERT INTO role (name, description)
VALUES ('POSTECH_OWNER', 'Owner with full access'),
       ('POSTECH_GROUP_ADMIN', 'Admin with access to user management'),
       ('POSTECH_ORDER_ADMIN', 'Admin with access to order management'),
       ('POSTECH_ORDER_SQUAD', 'Squad with access to orders updates'),
       ('POSTECH_MENU_ADMIN', 'Admin with access to menu management'),
       ('POSTECH_MENU_SQUAD', 'Squad with access to menu updates'),
       ('POSTECH_USER', 'Usual customer');

-- Insert authorities
INSERT INTO authority (name, description)
VALUES ('groups:list', 'List all groups'),
       ('groups:manage', 'Create or update group roles'),
       ('groups:remove', 'Remove group roles'),
       ('groups:admins:list', 'List all group admins'),
       ('groups:admins:add', 'Add new group admins'),
       ('groups:admins:remove', 'Remove group admins'),
       ('scopes:list', 'List all scopes'),
       ('scopes:manage', 'Manage or update scopes'),
       ('scopes:remove', 'Remove scopes'),
       ('groups:users:list', 'List all group users'),
       ('groups:users:add', 'Add new group users'),
       ('groups:users:remove', 'Remove group users'),
       ('groups:scopes:list', 'List all group scopes'),
       ('groups:scopes:add', 'Add new scopes to groups'),
       ('groups:scopes:remove', 'Remove scopes from groups'),
       ('orders:add', 'Create an order'),
       ('orders:view', 'View an order'),
       ('orders:list', 'List all orders'),
       ('orders:cancel', 'Cancel orders'),
       ('orders:update', 'Update orders'),
       ('menu:add', 'Add new menu items'),
       ('menu:view', 'View a menu item'),
       ('menu:list', 'List all menu items'),
       ('menu:remove', 'Remove menu items'),
       ('menu:update', 'Update menu items');

-- Insert role-authority mappings
INSERT INTO role_authority (role_id, authority_id, user_id)
VALUES ((SELECT id FROM role WHERE name = 'POSTECH_OWNER'), (SELECT id FROM authority WHERE name = 'groups:manage'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_OWNER'), (SELECT id FROM authority WHERE name = 'groups:remove'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_OWNER'), (SELECT id FROM authority WHERE name = 'groups:admins:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_OWNER'), (SELECT id FROM authority WHERE name = 'groups:admins:add'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_OWNER'), (SELECT id FROM authority WHERE name = 'groups:admins:remove'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'scopes:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'scopes:manage'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'scopes:remove'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:users:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:users:add'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:users:remove'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:scopes:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:scopes:add'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_GROUP_ADMIN'), (SELECT id FROM authority WHERE name = 'groups:scopes:remove'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_ORDER_ADMIN'), (SELECT id FROM authority WHERE name = 'orders:cancel'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_ORDER_SQUAD'), (SELECT id FROM authority WHERE name = 'orders:update'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_ORDER_SQUAD'), (SELECT id FROM authority WHERE name = 'orders:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_USER'), (SELECT id FROM authority WHERE name = 'orders:add'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_USER'), (SELECT id FROM authority WHERE name = 'orders:view'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_MENU_ADMIN'), (SELECT id FROM authority WHERE name = 'menu:add'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_MENU_ADMIN'), (SELECT id FROM authority WHERE name = 'menu:remove'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_MENU_SQUAD'), (SELECT id FROM authority WHERE name = 'menu:update'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_USER'), (SELECT id FROM authority WHERE name = 'menu:list'), 'system'),
       ((SELECT id FROM role WHERE name = 'POSTECH_USER'), (SELECT id FROM authority WHERE name = 'menu:view'), 'system');
