-- 更新脚本

-- ----------------------------
-- 2022年11月21日21:01:09
-- ----------------------------
-- 新增代码生成菜单
INSERT INTO `sys_menu` VALUES (85, '代码生成', 'sys:gen', '/sys/gen', 3, NULL, '12', '0', 'admin', '2022-11-21 20:54:36', 'admin', '2022-11-21 20:54:36', 0, 'sys/gen/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (86, '查看代码生成', 'sys:gen:view', NULL, 85, NULL, '1', '1', 'admin', '2022-11-21 20:55:13', 'admin', '2022-11-21 20:55:13', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (87, '编辑代码生成', 'sys:gen:edit', NULL, 85, NULL, '2', '1', 'admin', '2022-11-21 20:55:33', 'admin', '2022-11-21 20:55:33', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (88, '新增代码生成', 'sys:gen:add', NULL, 85, NULL, '3', '1', 'admin', '2022-11-21 20:55:56', 'admin', '2022-11-21 20:55:56', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (89, '删除代码生成', 'sys:gen:del', NULL, 85, NULL, '4', '1', 'admin', '2022-11-21 20:56:12', 'admin', '2022-11-21 20:56:12', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (90, '课程章节', NULL, '/exam/course_chapter/:id', 28, NULL, '9', '0', 'admin', '2022-11-21 22:53:11', 'admin', '2022-11-21 22:53:11', 0, 'exam/courseChapter/index', 0, 0, NULL, 'gitee', 1, '', '/exam/course');

INSERT INTO `sys_role_menu` VALUES (141, 1, 85, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (142, 1, 86, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (143, 1, 87, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (144, 1, 88, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (145, 1, 89, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');

INSERT INTO `sys_role_menu` VALUES (146, 2, 86, '', '2022-11-21 20:58:13', '', '2022-11-21 20:58:13', 0, '');
INSERT INTO `sys_role_menu` VALUES (147, 2, 85, '', '2022-11-21 20:58:13', '', '2022-11-21 20:58:13', 0, '');
INSERT INTO `sys_role_menu` VALUES (406, 1, 90, '', '2022-11-21 23:00:52', '', '2022-11-21 23:00:52', 0, '');
INSERT INTO `sys_role_menu` VALUES (426, 2, 90, '', '2022-11-21 23:01:06', '', '2022-11-21 23:01:06', 0, '');

-- 课程章节表
CREATE TABLE `exam_course_chapter` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
   `sort` int DEFAULT NULL COMMENT '序号',
   `course_id` bigint DEFAULT NULL COMMENT '课程ID',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `chapter_desc` varchar(255) DEFAULT NULL COMMENT '描述',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='课程章表';

CREATE TABLE `exam_course_section` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
   `sort` int DEFAULT NULL COMMENT '序号',
   `chapter_id` bigint DEFAULT NULL COMMENT '章ID',
   `learn_hour` int DEFAULT NULL COMMENT '时长',
   `video_id` bigint DEFAULT NULL COMMENT '视频ID',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `section_desc` varchar(255) DEFAULT NULL COMMENT '描述',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='课程节表';