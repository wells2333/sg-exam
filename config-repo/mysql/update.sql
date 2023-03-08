ALTER TABLE `exam_subject_category` ADD COLUMN `status` tinyint(1) ZEROFILL NOT NULL COMMENT '状态：0：草稿，1：已发布';

CREATE TABLE `shedlock`(
 `name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名',
 `lock_until` timestamp(3) NULL DEFAULT NULL COMMENT '释放时间',
 `locked_at` timestamp(3) NULL DEFAULT NULL COMMENT '锁定时间',
 `locked_by` varchar(255) DEFAULT NULL COMMENT '锁定实例',
 PRIMARY KEY (name)
);

CREATE TABLE `sys_sms` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `receiver` varchar(255) DEFAULT NULL COMMENT '接收方',
   `content` varchar(512) DEFAULT NULL COMMENT '发送内容',
   `response` varchar(512) DEFAULT NULL COMMENT '消息响应',
   `creator` varchar(128) DEFAULT NULL,
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL,
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) DEFAULT NULL,
   `tenant_code` varchar(16) DEFAULT NULL COMMENT '租户编号',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息发送表';

CREATE TABLE `sys_message_read` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `message_id` bigint(20) NOT NULL COMMENT '消息ID',
    `receiver_id` bigint(20) NOT NULL COMMENT '接收人ID',
    `creator` varchar(128) NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operator` varchar(128) NOT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
    `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_message_id` (`message_id`),
    KEY `idx_receiver_id` (`receiver_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='已读消息表';

CREATE TABLE `sys_message_receiver` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `message_id` bigint(20) NOT NULL COMMENT '消息ID',
    `receiver_id` bigint(20) NOT NULL COMMENT '接收人ID',
    `creator` varchar(128) NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operator` varchar(128) NOT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
    `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
    `type` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '消息类型，0：站内信',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_message_id` (`message_id`),
    KEY `idx_receiver_id` (`receiver_id`) USING BTREE,
    KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='消息接收表';

CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_key` varchar(255) NOT NULL COMMENT '配置key',
  `config_value` varchar(255) NOT NULL DEFAULT '' COMMENT '配置的值',
  `config_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '配置描述',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sys_config_config_key_tenant_code_uindex` (`config_key`,`tenant_code`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='系统配置表';

INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (3, 'sys_web_name', '云职评', 'web 首页网站名称', 'admin', '2023-02-21 22:01:24', 'admin', '2023-02-21 23:00:34', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (4, 'sys_avatar', '云面试', '系统 LOGO', 'admin', '2023-02-21 22:01:53', 'admin', '2023-02-21 22:05:24', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (5, 'sys_admin_main_title', 'SG-EXAM 后台管理系统', 'admin 后台主标题', 'admin', '2023-02-21 22:02:16', 'admin', '2023-02-21 22:04:31', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (6, 'sys_admin_sub_title', '方便好用的考试管理系统！', 'admin 后台副标题', 'admin', '2023-02-21 22:02:29', 'admin', '2023-02-21 22:05:04', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (7, 'sys_wxapp_avatar', 'https://cdn.yunmianshi.com/app/wx/svg/study_v3.svg?e=1677074888&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:-zqjhckC8BfXnNC51KmilksTx5o=', '小程序首页封面图片', 'admin', '2023-02-21 22:02:47', 'admin', '2023-02-21 22:08:25', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (8, 'sys_wxapp_main_title', 'HI，欢迎使用云面试', '小程序首页主标题', 'admin', '2023-02-21 22:02:57', 'admin', '2023-02-21 22:03:38', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (9, 'sys_wxapp_sub_title', '提供考试、练习、刷题、在线学习等功能', '小程序首页副标题', 'admin', '2023-02-21 22:03:08', 'admin', '2023-02-21 22:04:01', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (10, 'sys_web_main_title', '云职评AI面试', 'web 网站首页主标题', 'admin', '2023-02-21 23:02:27', 'admin', '2023-02-21 23:02:27', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (11, 'sys_web_sub_title_one', '采用AI技术，新一代面试管理平台', 'web 网站首页副标题', 'admin', '2023-02-21 23:02:51', 'admin', '2023-02-21 23:03:01', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (12, 'sys_web_sub_title_two', '提供私有化、AI面试、肢体测评、笔试、性格测试等功能', 'web 网站首页副标题', 'admin', '2023-02-21 23:03:19', 'admin', '2023-02-21 23:03:19', 0, 'gitee');
INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `config_desc`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `tenant_code`) VALUES (13, 'sys_web_copyright', '陕ICP备20002980号-2', '网站 Copyright', 'admin', '2023-02-22 20:54:38', 'admin', '2023-02-22 20:54:38', 0, 'gitee');

INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (92, '消息管理', 'sys:message', '/sys/message', 3, NULL, '11', '0', 'admin', '2023-02-18 10:17:38', 'admin', '2023-02-18 10:17:38', 0, 'sys/message/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (93, '新增消息', 'sys:message:add', NULL, 92, NULL, '1', '1', 'admin', '2023-02-18 10:29:11', 'admin', '2023-02-18 10:29:11', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (94, '编辑消息', 'sys:message:edit', NULL, 92, NULL, '2', '1', 'admin', '2023-02-18 10:29:29', 'admin', '2023-02-18 10:29:29', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (95, '删除消息', 'sys:message:del', NULL, 92, NULL, '3', '1', 'admin', '2023-02-18 10:29:46', 'admin', '2023-02-18 10:29:46', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (96, '系统配置', 'sys:config', '/sys/config', 3, NULL, '13', '0', 'admin', '2023-02-21 13:33:57', 'admin', '2023-02-21 13:33:57', 0, 'sys/config/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (97, '新增配置', 'sys:config:add', NULL, 96, NULL, '1', '1', 'admin', '2023-02-21 13:34:34', 'admin', '2023-02-21 13:34:34', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (98, '修改配置', 'sys:config:edit', NULL, 96, NULL, '2', '1', 'admin', '2023-02-21 13:34:53', 'admin', '2023-02-21 13:34:53', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`, `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`) VALUES (99, '删除配置', 'sys:config:del', NULL, 96, NULL, '3', '1', 'admin', '2023-02-21 13:35:10', 'admin', '2023-02-21 13:35:10', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');

INSERT INTO `sys_role_menu` VALUES (446, 2, 92, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (447, 2, 93, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (448, 2, 94, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (449, 2, 95, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (450, 2, 96, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (451, 2, 97, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (452, 2, 98, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO `sys_role_menu` VALUES (453, 2, 99, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');

ALTER TABLE `sys_message`
    ADD COLUMN `receiver_type` tinyint(1) NOT NULL COMMENT '接收人类型，0：全部用户，1：部分用户，2：部门';
ALTER TABLE `sys_message`
    ADD COLUMN `status` tinyint(1) NOT NULL COMMENT '状态，0：草稿，1：已发布';

ALTER TABLE `exam_examination`
    ADD COLUMN `access_type` tinyint(1) NOT NULL COMMENT '权限控制，0：全部用户，1：指定用户，2：指定部门' AFTER `answer_type`;

CREATE TABLE `exam_examination_member` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `exam_type` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '类型，0：课程，1：考试',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) NOT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `member_type` tinyint(1) NOT NULL COMMENT '成员类型，0：全部用户，1：用户ID，2：部门',
   `member_id` bigint(20) NOT NULL COMMENT '成员ID',
   `exam_id` bigint(20) NOT NULL COMMENT '考试/课程ID',
   PRIMARY KEY (`id`) USING BTREE,
   KEY `idx_type` (`exam_type`),
   KEY `idx_member_id` (`member_id`),
   KEY `idx_member_type` (`member_type`),
   KEY `idx_exam_type` (`exam_type`),
   KEY `idx_exam_id` (`exam_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='考试成员表';