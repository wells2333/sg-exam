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
   `video_name` varchar(255) DEFAULT NULL COMMENT '视频名称',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='课程节表';

INSERT INTO `exam_course` VALUES (1, '义务教育语文学科课程', NULL, NULL, NULL, '义务教育语文学科课程', 'admin', '2022-11-26 11:00:04', 'admin', '2022-11-26 11:00:04', 0, 'gitee', 4, NULL, NULL);

INSERT INTO `exam_course_chapter` VALUES (1, '单元一 四季美景', 1, 1, 'admin', '2022-11-26 11:00:23', 'admin', '2022-11-26 11:02:24', 0, 'gitee', '单元一 四季美景');
INSERT INTO `exam_course_chapter` VALUES (2, '单元二 至情至爱', 2, 1, 'admin', '2022-11-26 11:02:14', 'admin', '2022-11-26 11:02:14', 0, 'gitee', '单元二 至情至爱');
INSERT INTO `exam_course_chapter` VALUES (3, '单元三 学习生活', 3, 1, 'admin', '2022-11-26 11:02:37', 'admin', '2022-11-26 11:02:37', 0, 'gitee', '单元三 学习生活');
INSERT INTO `exam_course_chapter` VALUES (4, '单元四 人生之舟', 4, 1, 'admin', '2022-11-26 11:02:52', 'admin', '2022-11-26 11:02:52', 0, 'gitee', '单元四 人生之舟');
INSERT INTO `exam_course_chapter` VALUES (5, '单元五 动物与人', 5, 1, 'admin', '2022-11-26 11:03:03', 'admin', '2022-11-26 11:03:03', 0, 'gitee', '单元五 动物与人');
INSERT INTO `exam_course_chapter` VALUES (6, '单元六 想象之翼', 6, 1, 'admin', '2022-11-26 11:03:17', 'admin', '2022-11-26 11:03:17', 0, 'gitee', '单元六 想象之翼');

INSERT INTO `exam_course_section` VALUES (2, '1．春/朱自清', 1, 1, 1, NULL, 'admin', '2022-11-26 11:03:37', 'admin', '2022-11-26 11:04:28', 0, 'gitee', '1．春/朱自清', NULL);
INSERT INTO `exam_course_section` VALUES (3, '2．济南的冬天/老舍', 2, 1, 1, NULL, 'admin', '2022-11-26 11:03:50', 'admin', '2022-11-26 11:04:37', 0, 'gitee', '2．济南的冬天/老舍', NULL);
INSERT INTO `exam_course_section` VALUES (4, '3．雨的四季/刘湛秋', 3, 1, 1, NULL, 'admin', '2022-11-26 11:04:17', 'admin', '2022-11-26 11:04:17', 0, 'gitee', '3．雨的四季/刘湛秋', NULL);
INSERT INTO `exam_course_section` VALUES (5, '4．古代诗歌 观沧海/曹操', 4, 1, 1, NULL, 'admin', '2022-11-26 11:06:01', 'admin', '2022-11-26 11:06:01', 0, 'gitee', '4．古代诗歌 观沧海/曹操', NULL);
INSERT INTO `exam_course_section` VALUES (6, '5．秋天的怀念/史铁生', 1, 2, 1, NULL, 'admin', '2022-11-26 11:06:29', 'admin', '2022-11-26 11:06:29', 0, 'gitee', '5．秋天的怀念/史铁生', NULL);
INSERT INTO `exam_course_section` VALUES (7, '6．散步/莫怀戚', 2, 2, 1, NULL, 'admin', '2022-11-26 11:06:41', 'admin', '2022-11-26 11:06:41', 0, 'gitee', '6．散步/莫怀戚', NULL);
INSERT INTO `exam_course_section` VALUES (8, '7．散文诗两首金色花/泰戈尔', 100, 2, 1, NULL, 'admin', '2022-11-26 11:06:54', 'admin', '2022-11-26 11:06:54', 0, 'gitee', '7．散文诗两首金色花/泰戈尔', NULL);
INSERT INTO `exam_course_section` VALUES (9, '8.《世说新语》两则咏雪', 100, 2, 1, NULL, 'admin', '2022-11-26 11:07:05', 'admin', '2022-11-26 11:07:05', 0, 'gitee', '8.《世说新语》两则咏雪', NULL);
INSERT INTO `exam_course_section` VALUES (10, '9．从百草园到三味书屋/鲁迅', 1, 3, 1, NULL, 'admin', '2022-11-26 11:07:24', 'admin', '2022-11-26 11:07:24', 0, 'gitee', '9．从百草园到三味书屋/鲁迅', NULL);
INSERT INTO `exam_course_section` VALUES (11, '10．再塑生命的人/海伦•凯勒', 2, 3, 1, NULL, 'admin', '2022-11-26 11:07:40', 'admin', '2022-11-26 11:07:40', 0, 'gitee', '10．再塑生命的人/海伦•凯勒', NULL);
INSERT INTO `exam_course_section` VALUES (12, '11．窃读记/林海音', 3, 3, 1, NULL, 'admin', '2022-11-26 11:07:58', 'admin', '2022-11-26 11:07:58', 0, 'gitee', '11．窃读记/林海音', NULL);
INSERT INTO `exam_course_section` VALUES (13, '12．《论语》十二章', 4, 3, 1, NULL, 'admin', '2022-11-26 11:08:12', 'admin', '2022-11-26 11:08:12', 0, 'gitee', '12．《论语》十二章', NULL);
INSERT INTO `exam_course_section` VALUES (14, '13．纪念白求恩/毛泽东', 1, 4, 1, NULL, 'admin', '2022-11-26 11:08:42', 'admin', '2022-11-26 11:08:42', 0, 'gitee', '13．纪念白求恩/毛泽东', NULL);
INSERT INTO `exam_course_section` VALUES (15, '17．猫/郑振铎', 1, 5, 1, NULL, 'admin', '2022-11-26 11:09:05', 'admin', '2022-11-26 11:09:05', 0, 'gitee', '17．猫/郑振铎', NULL);
INSERT INTO `exam_course_section` VALUES (16, '21．皇帝的新装/安徒生', 1, 6, 1, NULL, 'admin', '2022-11-26 11:09:21', 'admin', '2022-11-26 11:09:21', 0, 'gitee', '21．皇帝的新装/安徒生', NULL);