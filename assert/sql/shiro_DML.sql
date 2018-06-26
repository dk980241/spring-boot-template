INSERT INTO web_user (id, username, password, salt, mobile, real_name, is_locked, gmt_create, gmt_modified)
VALUES (1, 'yuyanjia', '364bea5949fb9b7cb6f2873cd4a5bfc5', '846DD041290C47DD83480AA02027076B', '123123', '123123', 1,
        '2018-06-21 09:36:34', '2018-06-21 09:36:34');

INSERT INTO web_role (id, role_name, role_description, is_available, gmt_create, gmt_modified)
VALUES (1, 'test', NULL, 1, '2018-06-26 01:09:02', '2018-06-26 01:09:03');

INSERT INTO web_permission (id, permission_name, permission_value, is_available, gmt_create, gmt_modified)
VALUES (1, 'test', '/website/user/user-password-update', 1, '2018-06-26 01:08:41', '2018-06-26 01:08:44');

INSERT INTO web_role_permission (id, role_id, permission_id, gmt_create, gmt_modified)
VALUES (1, 1, 1, '2018-06-26 01:09:10', '2018-06-26 01:09:12');

INSERT INTO web_user_role (id, user_id, role_id, gmt_create, gmt_modified)
VALUES (1, 1, 1, '2018-06-26 01:09:26', '2018-06-26 01:09:28');