
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment`  (
  `id` bigint(20) NOT NULL,
  `attach_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `attach_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件大小',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
  `fast_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件ID',
  `busi_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务ID',
  `busi_module` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务模块',
  `busi_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型 0-普通，1-头像',
  `preview_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览地址',
  `creator` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
INSERT INTO `sys_attachment` VALUES (587326094462554112, '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBAAClz8zQeAcxdFAAB2nqimI80188.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-09 17:04:12', 'admin', '2019-06-09 17:04:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (590961604166815744, '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0KBuqASK_rAAB2nqimI80768.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 17:50:25', 'admin', '2019-06-19 17:50:25', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (590966504460259328, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0KC3qAWcIjAADGqyYhFOI172.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 18:09:54', 'admin', '2019-06-19 18:09:54', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (590967223477211136, '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0KDCWAeZqEAAB2nqimI80765.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 18:12:45', 'admin', '2019-06-19 18:12:45', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (590968058558943232, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0KDO2AI6HzAADGqyYhFOI297.jpg', NULL, NULL, '0', NULL, 'admin', '2019-06-19 18:16:04', 'admin', '2019-06-19 18:16:04', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (590978944174526464, '四川省2016年普通高考适应性测试.docx', '1310242', 'group1', 'group1/M00/00/00/rBEADV0KFxCANq2WABP-In3Uu2Y18.docx', NULL, NULL, '2', NULL, 'admin', '2019-06-19 18:59:20', 'admin', '2019-06-19 18:59:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (590992646021976064, '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0KI9KAGsTzAAB2nqimI80536.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 19:53:46', 'admin', '2019-06-19 19:53:46', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591693567886495744, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MsJ2Af0dbAADGqyYhFOI739.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:18:59', 'admin', '2019-06-21 18:18:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591694952287834112, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MseeAPR34AADGqyYhFOI455.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:24:29', 'admin', '2019-06-21 18:24:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591695271881216000, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MsjOANVdtAADGqyYhFOI235.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:25:45', 'admin', '2019-06-21 18:25:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591695964004290560, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MstiALOW1AADGqyYhFOI015.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:28:30', 'admin', '2019-06-21 18:28:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591696571138183168, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0Ms2mAE1w9AADGqyYhFOI173.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:30:55', 'admin', '2019-06-21 18:30:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591697443129790464, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MtDmAYZByAADGqyYhFOI194.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:34:23', 'admin', '2019-06-21 18:34:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591706139419348992, 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MvFKASQkYAADGqyYhFOI455.jpg', NULL, NULL, '1', NULL, 'student', '2019-06-21 19:08:57', 'student', '2019-06-21 19:08:57', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (591706239474470912, '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0MvGqABeulAAB2nqimI80233.jpg', NULL, NULL, '1', NULL, 'student', '2019-06-21 19:09:20', 'student', '2019-06-21 19:09:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (596089139502387200, '20161004_152739.png', '1151189', 'group1', 'group1/M00/00/00/rBEADV0crl6APS7jABGQ1Qn9F3w723.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:25:26', 'admin', '2019-07-03 13:25:26', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (596091468104470528, '20161004_152739.png', '1151189', 'group1', 'group1/M00/00/00/rBEADV0csImAAEqgABGQ1Qn9F3w067.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:34:41', 'admin', '2019-07-03 13:34:41', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (596091563143204864, '20160706_145011.png', '848567', 'group1', 'group1/M00/00/00/rBEADV0csKCACMJ0AAzyt1rmFJA424.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:35:04', 'admin', '2019-07-03 13:35:04', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (596091774175416320, '20160706_144936.png', '426226', 'group1', 'group1/M00/00/00/rBEADV0csNKAHjgIAAaA8md5_jE199.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:35:54', 'admin', '2019-07-03 13:35:54', 1, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (596094244884713472, '20160706_145011.png', '848567', 'group1', 'group1/M00/00/00/rBEADV0csx-AG-ohAAzyt1rmFJA952.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:45:43', 'admin', '2019-07-03 13:45:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (597108212914851840, NULL, NULL, NULL, NULL, NULL, NULL, '1', 'https://wx.qlogo.cn/mmopen/vi_32/cNgLBdVaia1yhPsxIXVIzI69YMrK2dEiaiafo9PKvickKlsNMoA0BrfdqT9t5c8goO6m2JMrOpSuR4diaWlicKaiaeN3A/132', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES (597110984758398977, NULL, NULL, NULL, NULL, NULL, NULL, '1', 'https://wx.qlogo.cn/mmopen/vi_32/cNgLBdVaia1yhPsxIXVIzI69YMrK2dEiaiafo9PKvickKlsNMoA0BrfdqT9t5c8goO6m2JMrOpSuR4diaWlicKaiaeN3A/132', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `dept_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `dept_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dept_leader` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级部门id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `creator` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (596290673729212416, '测试部门', '测试部门', '测试部门', -1, 30, 'admin', '2019-09-21 22:38:00', 'admin', '2019-09-21 22:29:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_dept` VALUES (624983967778607104, '1', NULL, NULL, -1, 30, 'test', '2019-09-21 15:11:14', 'test', '2019-09-21 15:03:15', 1, 'EXAM', 'test');
INSERT INTO `sys_dept` VALUES (630779718874042368, '测试部门2', NULL, NULL, 596290673729212416, 30, 'admin', '2019-10-07 15:01:56', 'admin', '2019-10-07 14:53:29', 1, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` int(11) NULL DEFAULT NULL COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志标题',
  `ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户的IP地址',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户代理信息',
  `request_uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的URI',
  `method` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的方式',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作提交的数据',
  `exception` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  `service_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '耗时',
  `creator` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记',
  `application_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父菜单id',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排序',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `data_level` int(11) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `modify_date` timestamp(0) NULL DEFAULT NULL,
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向url',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('571348650370928640', '个人管理', 'personal', '/api/user/v1/personal/**', '-1', 'form', '30', '0', 1, 'admin', '2019-04-26 14:55:33', 'admin', '2019-04-26 15:04:16', '0', 'EXAM', 'Layout', '/personal', NULL, '个人管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571349816924311552', '个人资料', 'personal:message', '/api/user/v1/user/updateInfo', '571348650370928640', '', '29', '0', 1, 'admin', '2019-04-26 15:00:11', 'admin', '2019-04-26 15:00:11', '0', 'EXAM', 'views/personal/message', 'message', NULL, '个人资料', 'gitee');
INSERT INTO `sys_menu` VALUES ('571350099653955584', '修改密码', 'personal:password', '/api/user/v1/user/updateInfo', '571348650370928640', '', '30', '0', 1, 'admin', '2019-04-26 15:01:18', 'admin', '2019-04-26 15:01:18', '0', 'EXAM', 'views/personal/password', 'password', NULL, '修改密码', 'gitee');
INSERT INTO `sys_menu` VALUES ('571351763521769472', '附件管理', 'attachment', '/api/user/v1/attachment/**', '-1', 'excel', '10', '0', 1, 'admin', '2019-04-26 15:07:55', 'admin', '2019-04-26 15:09:59', '0', 'EXAM', 'Layout', '/attachment', NULL, '附件管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571352087896657920', '附件列表', 'attachment:list', '/api/user/v1/attachment/list', '571351763521769472', '', '30', '0', 1, 'admin', '2019-04-26 15:09:12', 'admin', '2019-04-26 15:09:12', '0', 'EXAM', 'views/attachment/list', 'list', NULL, '附件列表', 'gitee');
INSERT INTO `sys_menu` VALUES ('571352797233156096', '考务管理', 'exam', '/api/exam/**', '-1', 'nested', '8', '0', 1, 'admin', '2019-04-26 15:12:02', 'admin', '2019-05-23 21:28:32', '0', 'EXAM', 'Layout', '/exam', NULL, '考务管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571353230286655488', '课程管理', 'exam:course', '/api/exam/v1/course/**', '571352797233156096', '', '1', '0', 1, 'admin', '2019-04-26 15:13:45', 'admin', '2019-04-26 15:13:45', '0', 'EXAM', 'views/exam/course', 'course', NULL, '课程管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571353525381107712', '考试管理', 'exam:exam', '/api/exam/v1/examination/**', '571352797233156096', '', '2', '0', 1, 'admin', '2019-04-26 15:14:55', 'admin', '2019-04-26 15:14:55', '0', 'EXAM', 'views/exam/exam', 'exam', NULL, '考试管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571353992756596736', '题库管理', 'exam:subject', '/api/exam/v1/subject/**', '571352797233156096', '', '3', '0', 1, 'admin', '2019-04-26 15:16:47', 'admin', '2019-04-26 15:16:47', '0', 'EXAM', 'views/exam/subject', 'subject', NULL, '题库管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571354428217626624', '成绩管理', 'exam:examRecord', '/api/exam/v1/examRecord/**', '571352797233156096', '', '4', '0', 1, 'admin', '2019-04-26 15:18:30', 'admin', '2019-04-26 15:18:30', '0', 'EXAM', 'views/exam/examRecord', 'score', NULL, '成绩管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571354823258148864', '知识库', 'exam:knowledge', '/api/exam/v1/knowledge/**', '571352797233156096', '', '5', '0', 1, 'admin', '2019-04-26 15:20:05', 'admin', '2019-04-26 15:20:05', '0', 'EXAM', 'views/exam/knowledge', 'knowledge', NULL, '知识库', 'gitee');
INSERT INTO `sys_menu` VALUES ('571355240792723456', '新增课程', 'exam:course:add', NULL, '571353230286655488', '', '1', '1', 1, 'admin', '2019-04-26 15:21:44', 'admin', '2019-04-26 15:21:44', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355418715099136', '修改课程', 'exam:course:edit', NULL, '571353230286655488', '', '2', '1', 1, 'admin', '2019-04-26 15:22:27', 'admin', '2019-04-26 15:22:27', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355486121758720', '删除课程', 'exam:course:del', NULL, '571353230286655488', '', '3', '1', 1, 'admin', '2019-04-26 15:22:43', 'admin', '2019-04-26 15:22:43', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355686403969024', '新增考试', 'exam:exam:add', NULL, '571353525381107712', '', '1', '1', 1, 'admin', '2019-04-26 15:23:30', 'admin', '2019-04-26 15:23:30', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355830226653184', '修改课程', 'exam:exam:edit', NULL, '571353525381107712', '', '2', '1', 1, 'admin', '2019-04-26 15:24:05', 'admin', '2019-04-26 15:24:05', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355921259827200', '删除考试', 'exam:exam:del', NULL, '571353525381107712', '', '3', '1', 1, 'admin', '2019-04-26 15:24:26', 'admin', '2019-04-26 15:24:26', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571356206782877696', '题目管理', 'exam:exam:subject', NULL, '571353525381107712', '', '4', '1', 1, 'admin', '2019-04-26 15:25:34', 'admin', '2019-04-26 15:25:34', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571356537642160128', '导出题目', 'exam:exam:subject:export', NULL, '571353525381107712', '', '5', '1', 1, 'admin', '2019-04-26 15:26:53', 'admin', '2019-04-26 15:27:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571356877741494272', '导入题目', 'exam:exam:subject:import', NULL, '571353525381107712', '', '6', '1', 1, 'admin', '2019-04-26 15:28:14', 'admin', '2019-04-26 15:28:14', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357072436891648', '新增题目', 'exam:exam:subject:add', NULL, '571353525381107712', '', '7', '1', 1, 'admin', '2019-04-26 15:29:01', 'admin', '2019-04-26 15:29:01', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357235276550144', '删除题目', 'exam:exam:subject:del', NULL, '571353525381107712', '', '8', '1', 1, 'admin', '2019-04-26 15:29:40', 'admin', '2019-04-26 15:29:40', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357509638557696', '新增题目', 'exam:subject:bank:add', NULL, '571353992756596736', '', '1', '1', 1, 'admin', '2019-04-26 15:30:45', 'admin', '2019-04-26 15:30:45', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357821778661376', '修改题目', 'exam:subject:bank:edit', NULL, '571353992756596736', '', '2', '1', 1, 'admin', '2019-04-26 15:32:00', 'admin', '2019-04-26 15:32:00', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357937931522048', '删除题目', 'exam:subject:bank:del', NULL, '571353992756596736', '', '3', '1', 1, 'admin', '2019-04-26 15:32:27', 'admin', '2019-04-26 17:41:21', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358188264361984', '新增题目分类', 'exam:subject:category:add', NULL, '571353992756596736', '', '4', '1', 1, 'admin', '2019-04-26 15:33:27', 'admin', '2019-04-26 15:33:27', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358308477308928', '修改题目分类', 'exam:subject:category:edit', NULL, '571353992756596736', '', '5', '1', 1, 'admin', '2019-04-26 15:33:56', 'admin', '2019-04-26 15:33:56', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358407353831424', '删除题目分类', 'exam:subject:category:del', NULL, '571353992756596736', '', '6', '1', 1, 'admin', '2019-04-26 15:34:19', 'admin', '2019-04-26 15:34:19', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358617991778304', '导入题目', 'exam:subject:bank:import', NULL, '571353992756596736', '', '7', '1', 1, 'admin', '2019-04-26 15:35:09', 'admin', '2019-04-26 15:35:09', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358928483520512', '导出题目', 'exam:subject:bank:export', NULL, '571353992756596736', '', '8', '1', 1, 'admin', '2019-04-26 15:36:23', 'admin', '2019-04-26 15:36:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571359163205160960', '导出成绩', 'exam:examRecord:export', NULL, '571354428217626624', '', '30', '1', 1, 'admin', '2019-04-26 15:37:19', 'admin', '2019-04-26 15:37:19', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571361163502292992', '系统监控', 'sys', '/api/monitor/**', '-1', 'chart', '7', '0', 1, 'admin', '2019-04-26 15:45:16', 'admin', '2019-04-26 15:59:22', '0', 'EXAM', 'Layout', '/monitor', NULL, '系统监控', 'gitee');
INSERT INTO `sys_menu` VALUES ('571361526066319360', '日志监控', 'monitor:log', '/api/user/v1/log/**', '571361163502292992', '', '30', '0', 1, 'admin', '2019-04-26 15:46:43', 'admin', '2019-04-26 15:46:43', '0', 'EXAM', 'views/monitor/log', 'log', NULL, '日志监控', 'gitee');
INSERT INTO `sys_menu` VALUES ('571361746552492032', 'consul监控', 'monitor:service', '/api/monitor/service/**', '571361163502292992', '', '31', '0', 1, 'admin', '2019-04-26 15:47:35', 'admin', '2019-04-26 15:47:35', '0', 'EXAM', NULL, 'http://localhost:8500', NULL, 'consul监控', 'gitee');
INSERT INTO `sys_menu` VALUES ('571362994005610496', 'zipkin监控', 'monitor:link', '/api/monitor/**', '571361163502292992', '', '32', '0', 1, 'admin', '2019-04-26 15:52:33', 'admin', '2019-04-26 15:52:33', '0', 'EXAM', NULL, 'http://localhost:9411/zipkin', NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571363268497641472', '服务监控', 'monitor:admin', '/api/monitor/**', '571361163502292992', '', '33', '0', 1, 'admin', '2019-04-26 15:53:38', 'admin', '2019-04-26 15:53:38', '0', 'EXAM', NULL, 'http://localhost:8085/admin', NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571363537549660160', '接口文档', 'monitor:document', '/api/monitor/**', '571361163502292992', '', '34', '0', 1, 'admin', '2019-04-26 15:54:42', 'admin', '2019-04-26 15:54:42', '0', 'EXAM', NULL, 'http://localhost:8000/swagger-ui.html', NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571364115214372864', '删除日志', 'monitor:log:del', NULL, '571361526066319360', '', '30', '1', 1, 'admin', '2019-04-26 15:57:00', 'admin', '2019-04-26 15:57:00', '0', 'EXAM', NULL, NULL, NULL, '删除日志', 'gitee');
INSERT INTO `sys_menu` VALUES ('571365178965364736', '首页', 'dashboard', '/', '-1', 'dashboard', '0', '0', 1, 'admin', '2019-04-26 16:01:14', 'test', '2019-09-21 15:06:37', '0', 'EXAM', 'Layout', '/dashboard', NULL, '首页', 'gitee');
INSERT INTO `sys_menu` VALUES ('571367565360762880', '系统管理', 'sys', '/api/user/v1/**', '-1', 'component', '1', '0', 1, 'admin', '2019-04-26 16:10:43', 'admin', '2019-05-23 21:52:26', '0', 'EXAM', 'Layout', '/sys', NULL, '系统管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571367969767165952', '用户管理', 'sys:user', '/api/user/v1/user/**', '571367565360762880', '', '2', '0', 1, 'admin', '2019-04-26 16:12:19', 'admin', '2019-04-26 16:12:19', '0', 'EXAM', 'views/sys/user', 'user', NULL, '用户管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571368181252362240', '部门管理', 'sys:dept', '/api/user/v1/dept/**', '571367565360762880', '', '8', '0', 1, 'admin', '2019-04-26 16:13:09', 'admin', '2019-04-26 16:13:09', '0', 'EXAM', 'views/sys/dept', 'dept', NULL, '部门管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571368627413061632', '角色管理', 'sys:role', '/api/user/v1/role/**', '571367565360762880', '', '9', '0', 1, 'admin', '2019-04-26 16:14:56', 'admin', '2019-04-26 16:14:56', '0', 'EXAM', 'views/sys/role', 'role', NULL, '角色管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369094226513920', '菜单管理', 'sys:menu', '/api/user/v1/menu/**', '571367565360762880', '', '10', '0', 1, 'admin', '2019-04-26 16:16:47', 'admin', '2019-04-26 16:16:47', '0', 'EXAM', 'views/sys/menu', 'menu', NULL, '菜单管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369709904203776', '终端管理', 'sys:client', '/api/user/v1/client/**', '571367565360762880', '', '11', '0', 1, 'admin', '2019-04-26 16:19:14', 'admin', '2019-04-26 16:19:14', '0', 'EXAM', 'views/sys/client', 'client', NULL, '终端管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369965811273728', '路由管理', 'sys:route', '/api/user/route/**', '571367565360762880', '', '12', '0', 1, 'admin', '2019-04-26 16:20:15', 'admin', '2019-04-26 16:20:15', '0', 'EXAM', 'views/sys/route', 'route', NULL, '路由管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571371375550402560', '新增用户', 'sys:user:add', NULL, '571367969767165952', '', '1', '1', 1, 'admin', '2019-04-26 16:25:51', 'admin', '2019-07-04 10:50:33', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371477828505600', '删除用户', 'sys:user:del', NULL, '571367969767165952', '', '2', '1', 1, 'admin', '2019-04-26 16:26:15', 'admin', '2019-04-26 16:26:15', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371606652358656', '修改用户', 'sys:user:edit', NULL, '571367969767165952', '', '3', '1', 1, 'admin', '2019-04-26 16:26:46', 'admin', '2019-04-26 16:26:46', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371699010932736', '导出用户', 'sys:user:export', NULL, '571367969767165952', '', '4', '1', 1, 'admin', '2019-04-26 16:27:08', 'admin', '2019-04-26 16:27:08', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371773073952768', '导入用户', 'sys:user:import', NULL, '571367969767165952', '', '5', '1', 1, 'admin', '2019-04-26 16:27:26', 'admin', '2019-04-26 16:27:26', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571372425787346944', '新增部门', 'sys:dept:add', NULL, '571368181252362240', '', '1', '1', 1, 'admin', '2019-04-26 16:30:01', 'admin', '2019-04-26 16:30:01', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571372559308820480', '修改部门', 'sys:dept:edit', NULL, '571368181252362240', '', '2', '1', 1, 'admin', '2019-04-26 16:30:33', 'admin', '2019-04-26 16:30:33', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571372707153842176', '删除部门', 'sys:dept:del', NULL, '571368181252362240', '', '3', '1', 1, 'admin', '2019-04-26 16:31:08', 'admin', '2019-04-26 17:41:02', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373219269971968', '新增角色', 'sys:role:add', NULL, '571368627413061632', '', '1', '1', 1, 'admin', '2019-04-26 16:33:11', 'admin', '2019-04-26 16:33:11', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373292582211584', '修改角色', 'sys:role:edit', NULL, '571368627413061632', '', '2', '1', 1, 'admin', '2019-04-26 16:33:28', 'admin', '2019-04-26 16:33:28', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373363046518784', '删除角色', 'sys:role:del', NULL, '571368627413061632', '', '3', '1', 1, 'admin', '2019-04-26 16:33:45', 'admin', '2019-04-26 16:33:45', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373478440210432', '分配权限', 'sys:role:auth', NULL, '571368627413061632', '', '4', '1', 1, 'admin', '2019-04-26 16:34:12', 'admin', '2019-04-26 16:34:12', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373881496047616', '新增菜单', 'sys:menu:add', NULL, '571369094226513920', '', '1', '1', 1, 'admin', '2019-04-26 16:35:48', 'admin', '2019-04-26 16:35:48', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373962609692672', '修改菜单', 'sys:menu:edit', NULL, '571369094226513920', '', '2', '1', 1, 'admin', '2019-04-26 16:36:08', 'admin', '2019-04-26 16:36:08', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374025859796992', '删除菜单', 'sys:menu:del', NULL, '571369094226513920', '', '3', '1', 1, 'admin', '2019-04-26 16:36:23', 'admin', '2019-04-26 16:36:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374113881460736', '导入菜单', 'sys:menu:import', NULL, '571369094226513920', '', '4', '1', 1, 'admin', '2019-04-26 16:36:44', 'admin', '2019-04-26 16:36:44', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374178956087296', '导出菜单', 'sys:menu:export', NULL, '571369094226513920', '', '5', '1', 1, 'admin', '2019-04-26 16:36:59', 'admin', '2019-04-26 16:36:59', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374593844056064', '新增终端', 'sys:client:add', NULL, '571369709904203776', '', '1', '1', 1, 'admin', '2019-04-26 16:38:38', 'admin', '2019-04-26 16:38:38', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374671245742080', '修改终端', 'sys:client:edit', NULL, '571369709904203776', '', '2', '1', 1, 'admin', '2019-04-26 16:38:57', 'admin', '2019-04-26 16:38:57', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374747460440064', '删除终端', 'sys:client:del', NULL, '571369709904203776', '', '3', '1', 1, 'admin', '2019-04-26 16:39:15', 'admin', '2019-04-26 16:39:15', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374884270247936', '新增路由', 'sys:route:add', NULL, '571369965811273728', '', '1', '1', 1, 'admin', '2019-04-26 16:39:48', 'admin', '2019-04-26 16:39:48', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374951823708160', '修改路由', 'sys:route:edit', NULL, '571369965811273728', '', '2', '1', 1, 'admin', '2019-04-26 16:40:04', 'admin', '2019-04-26 16:40:04', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571375033570693120', '删除路由', 'sys:route:del', NULL, '571369965811273728', '', '3', '1', 1, 'admin', '2019-04-26 16:40:23', 'admin', '2019-04-26 16:40:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571375135655858176', '刷新路由', 'sys:route:refresh', NULL, '571369965811273728', '', '4', '1', 1, 'admin', '2019-04-26 16:40:47', 'admin', '2019-04-26 16:40:47', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571454722205159424', '修改题目', 'exam:exam:subject:edit', NULL, '571353525381107712', '', '9', '1', 1, 'admin', '2019-04-26 21:57:02', 'admin', '2019-04-26 21:57:02', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('581237996276289536', '租户中心', 'tenant', '/api/user/v1/tenent/**', '-1', 'component', '3', '0', 1, 'admin', '2019-05-23 21:52:17', 'admin', '2019-05-23 22:00:51', '0', 'EXAM', 'Layout', '/tenant', NULL, '租户管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('581238351663861760', '单位管理', 'tenant:tenant', '/api/user/tenant/**', '581237996276289536', '', '1', '0', 1, 'admin', '2019-05-23 21:53:41', 'admin', '2019-05-23 21:55:30', '0', 'EXAM', 'views/tenant/tenant', 'tenant', NULL, '单位管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('581238560250793984', '新增单位', 'tenant:tenant:add', NULL, '581238351663861760', '', '1', '1', 1, 'admin', '2019-05-23 21:54:31', 'admin', '2019-05-23 21:55:05', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('581238795467362304', '修改单位', 'tenant:tenant:edit', '', '581238351663861760', '', '2', '1', 1, 'admin', '2019-05-23 21:55:27', 'admin', '2019-05-23 21:55:27', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('581238883182841856', '删除单位', 'tenant:tenant:del', NULL, '581238351663861760', '', '3', '1', 1, 'admin', '2019-05-23 21:55:48', 'admin', '2019-05-23 21:55:48', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('624972495417643008', '查看首页', 'dashboard:view', NULL, '571365178965364736', '', '30', '1', NULL, 'admin', '2019-09-21 14:17:34', 'admin', '2019-09-21 14:17:34', '0', 'EXAM', NULL, NULL, NULL, '查看首页', 'gitee');
INSERT INTO `sys_menu` VALUES ('625058053556932608', '编辑考试', 'exam:exam:edit', '/api/exam/v1/examination/**', '571353525381107712', '', '2', '1', NULL, 'admin', '2019-09-21 19:57:33', 'admin', '2019-09-21 19:57:33', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('625058859773464576', '编辑题目', 'exam:exam:subject:edit', NULL, '571353525381107712', '', '9', '1', NULL, 'admin', '2019-09-21 20:00:45', 'admin', '2019-09-21 20:00:45', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色code',
  `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `is_default` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否默认 0-否，1-是',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '启用禁用状态 0-启用，1-禁用',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '租户code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (596116511031169024, '普通用户', 'role_user', '普通用户', 1, 0, 'admin', '2019-09-08 20:57:57', 'admin', '2019-09-08 20:57:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_role` VALUES (596117256346406912, '超级管理员', 'role_admin', '超级管理员', 0, 0, 'admin', '2019-10-07 15:02:17', 'admin', '2019-10-07 14:53:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_role` VALUES (596330074307956736, '教师', 'role_teacher', '教师', 0, 0, 'admin', '2019-09-08 20:57:57', 'admin', '2019-09-08 20:57:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_role` VALUES (624964171867492352, '租户管理员', 'role_tenant_admin', '租户管理员', 0, 0, 'admin', '2019-10-08 21:55:13', 'admin', '2019-10-08 21:46:48', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (581239021590679552, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (581239021590679553, 571347202849509376, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (581239021590679554, 571347202849509376, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (581239021590679555, 571347202849509376, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (581239021590679556, 571347202849509376, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (581239021590679557, 571347202849509376, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (581239021590679558, 571347202849509376, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (581239021590679559, 571347202849509376, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (581239021590679560, 571347202849509376, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (581239021590679561, 571347202849509376, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (581239021590679562, 571347202849509376, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (581239021590679563, 571347202849509376, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (581239021590679564, 571347202849509376, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (581239021590679565, 571347202849509376, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (581239021590679566, 571347202849509376, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (581239021590679567, 571347202849509376, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (581239021590679568, 571347202849509376, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (581239021590679569, 571347202849509376, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (581239021590679570, 571347202849509376, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (581239021590679571, 571347202849509376, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (581239021590679572, 571347202849509376, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (581239021590679573, 571347202849509376, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (581239021590679574, 571347202849509376, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (581239021590679575, 571347202849509376, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (581239021590679576, 571347202849509376, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (581239021590679577, 571347202849509376, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (581239021590679578, 571347202849509376, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (581239021590679579, 571347202849509376, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (581239021590679580, 571347202849509376, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (581239021590679581, 571347202849509376, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (581239021590679582, 571347202849509376, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (581239021590679583, 571347202849509376, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (581239021590679584, 571347202849509376, 581237996276289536);
INSERT INTO `sys_role_menu` VALUES (581239021590679585, 571347202849509376, 581238351663861760);
INSERT INTO `sys_role_menu` VALUES (581239021590679586, 571347202849509376, 581238560250793984);
INSERT INTO `sys_role_menu` VALUES (581239021590679587, 571347202849509376, 581238795467362304);
INSERT INTO `sys_role_menu` VALUES (581239021590679588, 571347202849509376, 581238883182841856);
INSERT INTO `sys_role_menu` VALUES (581239021590679589, 571347202849509376, 571361163502292992);
INSERT INTO `sys_role_menu` VALUES (581239021590679590, 571347202849509376, 571361526066319360);
INSERT INTO `sys_role_menu` VALUES (581239021590679591, 571347202849509376, 571364115214372864);
INSERT INTO `sys_role_menu` VALUES (581239021590679592, 571347202849509376, 571361746552492032);
INSERT INTO `sys_role_menu` VALUES (581239021590679593, 571347202849509376, 571362994005610496);
INSERT INTO `sys_role_menu` VALUES (581239021590679594, 571347202849509376, 571363268497641472);
INSERT INTO `sys_role_menu` VALUES (581239021590679595, 571347202849509376, 571363537549660160);
INSERT INTO `sys_role_menu` VALUES (581239021590679596, 571347202849509376, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (581239021590679597, 571347202849509376, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (581239021590679598, 571347202849509376, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (581239021590679599, 571347202849509376, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (581239021590679600, 571347202849509376, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (581239021590679601, 571347202849509376, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (581239021590679602, 571347202849509376, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (581239021590679603, 571347202849509376, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (581239021590679604, 571347202849509376, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (581239021590679605, 571347202849509376, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (581239021590679606, 571347202849509376, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (581239021590679607, 571347202849509376, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (581239021590679608, 571347202849509376, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (581239021590679609, 571347202849509376, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (581239021590679610, 571347202849509376, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (581239021590679611, 571347202849509376, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (581239021590679612, 571347202849509376, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (581239021590679613, 571347202849509376, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (581239021590679614, 571347202849509376, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (581239021590679615, 571347202849509376, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (581239021590679616, 571347202849509376, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (581239021590679617, 571347202849509376, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (581239021590679618, 571347202849509376, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (581239021590679619, 571347202849509376, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (581239021590679620, 571347202849509376, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (581239021590679621, 571347202849509376, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (581239021590679622, 571347202849509376, 571351763521769472);
INSERT INTO `sys_role_menu` VALUES (581239021590679623, 571347202849509376, 571352087896657920);
INSERT INTO `sys_role_menu` VALUES (581239021590679624, 571347202849509376, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (581239021590679625, 571347202849509376, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (581239021590679626, 571347202849509376, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (586547078973493248, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (586547078973493249, 571347272906969088, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (586547078973493250, 571347272906969088, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (586547078973493251, 571347272906969088, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (586547078973493252, 571347272906969088, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (586547078973493253, 571347272906969088, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (586547078973493254, 571347272906969088, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (586547078973493255, 571347272906969088, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (586547078973493256, 571347272906969088, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (586547078973493257, 571347272906969088, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (586547078973493258, 571347272906969088, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (586547078973493259, 571347272906969088, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (586547078973493260, 571347272906969088, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (586547078973493261, 571347272906969088, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (586547078973493262, 571347272906969088, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (586547078973493263, 571347272906969088, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (586547078973493264, 571347272906969088, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (586547078973493265, 571347272906969088, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (586547078973493266, 571347272906969088, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (586547078973493267, 571347272906969088, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (586547078973493268, 571347272906969088, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (586547078973493269, 571347272906969088, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (586547078973493270, 571347272906969088, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (586547078973493271, 571347272906969088, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (586547078973493272, 571347272906969088, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (586547078973493273, 571347272906969088, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (586547078973493274, 571347272906969088, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (586547078973493275, 571347272906969088, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (586547078973493276, 571347272906969088, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (586547078973493277, 571347272906969088, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (586547078973493278, 571347272906969088, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (586547078973493279, 571347272906969088, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (586547078973493280, 571347272906969088, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (586547078973493281, 571347272906969088, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (586547078973493282, 571347272906969088, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (586547078973493283, 571347272906969088, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (586547078973493284, 571347272906969088, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (586547078973493285, 571347272906969088, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (586547078973493286, 571347272906969088, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (586547078973493287, 571347272906969088, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (586547078973493288, 571347272906969088, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (586547078973493289, 571347272906969088, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (586547078973493290, 571347272906969088, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (586547078973493291, 571347272906969088, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (586547078973493292, 571347272906969088, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (586547078973493293, 571347272906969088, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (586547078973493294, 571347272906969088, 571454722205159424);
INSERT INTO `sys_role_menu` VALUES (586547078973493295, 571347272906969088, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (586547078973493296, 571347272906969088, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (586547078973493297, 571347272906969088, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (586547078973493298, 571347272906969088, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (586547078973493299, 571347272906969088, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (586547078973493300, 571347272906969088, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (586547078973493301, 571347272906969088, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (586547078973493302, 571347272906969088, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (586547078973493303, 571347272906969088, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (586547078973493304, 571347272906969088, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (586547078973493305, 571347272906969088, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (586547078973493306, 571347272906969088, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (586547078973493307, 571347272906969088, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (586547078973493308, 571347272906969088, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (586547078973493309, 571347272906969088, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (586915493013753856, 586914735614726144, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (590994415867269120, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (590994415867269121, 571347357346697216, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (590994415867269122, 571347357346697216, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (590994415867269123, 571347357346697216, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (590994415867269124, 571347357346697216, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (590994415867269125, 571347357346697216, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (590994415867269126, 571347357346697216, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (590994415867269127, 571347357346697216, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (590994415867269128, 571347357346697216, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (590994415867269129, 571347357346697216, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (590994415867269130, 571347357346697216, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (590994415867269131, 571347357346697216, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (590994415867269132, 571347357346697216, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (590994415867269133, 571347357346697216, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (590994415867269134, 571347357346697216, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (590994415867269135, 571347357346697216, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (590994415867269136, 571347357346697216, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (590994415867269137, 571347357346697216, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (590994415867269138, 571347357346697216, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (590994415867269139, 571347357346697216, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (590994415867269140, 571347357346697216, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (590994415867269141, 571347357346697216, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (590994415867269142, 571347357346697216, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (590994415867269143, 571347357346697216, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (590994415867269144, 571347357346697216, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (590994415867269145, 571347357346697216, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (590994415867269146, 571347357346697216, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (590994415867269147, 571347357346697216, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (590994415867269148, 571347357346697216, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (590994415867269149, 571347357346697216, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (590994415867269150, 571347357346697216, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (590994415867269151, 571347357346697216, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (590994415867269152, 571347357346697216, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (590994415867269153, 571347357346697216, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (590994415867269154, 571347357346697216, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624972978748264448, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624972978748264449, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624972978748264450, 596116511031169024, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624972978748264451, 596116511031169024, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624972978748264452, 596116511031169024, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624985379413561344, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624985379413561345, 571347202849509376, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624985379413561346, 571347202849509376, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624985379413561347, 571347202849509376, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624985379413561348, 571347202849509376, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624985379413561349, 571347202849509376, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624985379413561350, 571347202849509376, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624985379413561351, 571347202849509376, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624985379413561352, 571347202849509376, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624985379413561353, 571347202849509376, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624985379417755648, 571347202849509376, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624985379417755649, 571347202849509376, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624985379417755650, 571347202849509376, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624985379417755651, 571347202849509376, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624985379417755652, 571347202849509376, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624985379417755653, 571347202849509376, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624985379417755654, 571347202849509376, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624985379417755655, 571347202849509376, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624985379417755656, 571347202849509376, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624985379417755657, 571347202849509376, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624985379417755658, 571347202849509376, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624985379417755659, 571347202849509376, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624985379417755660, 571347202849509376, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624985379417755661, 571347202849509376, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624985379417755662, 571347202849509376, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624985379417755663, 571347202849509376, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624985379417755664, 571347202849509376, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624985379417755665, 571347202849509376, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624985379417755666, 571347202849509376, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624985379417755667, 571347202849509376, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624985379417755668, 571347202849509376, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624985379417755669, 571347202849509376, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624985379417755670, 571347202849509376, 581237996276289536);
INSERT INTO `sys_role_menu` VALUES (624985379417755671, 571347202849509376, 581238351663861760);
INSERT INTO `sys_role_menu` VALUES (624985379417755672, 571347202849509376, 581238560250793984);
INSERT INTO `sys_role_menu` VALUES (624985379417755673, 571347202849509376, 581238795467362304);
INSERT INTO `sys_role_menu` VALUES (624985379417755674, 571347202849509376, 581238883182841856);
INSERT INTO `sys_role_menu` VALUES (624985379417755675, 571347202849509376, 571361163502292992);
INSERT INTO `sys_role_menu` VALUES (624985379417755676, 571347202849509376, 571361526066319360);
INSERT INTO `sys_role_menu` VALUES (624985379417755677, 571347202849509376, 571364115214372864);
INSERT INTO `sys_role_menu` VALUES (624985379417755678, 571347202849509376, 571361746552492032);
INSERT INTO `sys_role_menu` VALUES (624985379417755679, 571347202849509376, 571362994005610496);
INSERT INTO `sys_role_menu` VALUES (624985379417755680, 571347202849509376, 571363268497641472);
INSERT INTO `sys_role_menu` VALUES (624985379417755681, 571347202849509376, 571363537549660160);
INSERT INTO `sys_role_menu` VALUES (624985379417755682, 571347202849509376, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (624985379417755683, 571347202849509376, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (624985379417755684, 571347202849509376, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (624985379417755685, 571347202849509376, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (624985379417755686, 571347202849509376, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (624985379417755687, 571347202849509376, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (624985379417755688, 571347202849509376, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (624985379417755689, 571347202849509376, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (624985379417755690, 571347202849509376, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (624985379417755691, 571347202849509376, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (624985379417755692, 571347202849509376, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (624985379417755693, 571347202849509376, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (624985379417755694, 571347202849509376, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (624985379417755695, 571347202849509376, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (624985379417755696, 571347202849509376, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (624985379417755697, 571347202849509376, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (624985379417755698, 571347202849509376, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (624985379421949952, 571347202849509376, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (624985379421949953, 571347202849509376, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (624985379421949954, 571347202849509376, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (624985379421949955, 571347202849509376, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (624985379421949956, 571347202849509376, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (624985379421949957, 571347202849509376, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (624985379421949958, 571347202849509376, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (624985379421949959, 571347202849509376, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (624985379421949960, 571347202849509376, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (624985379421949961, 571347202849509376, 571351763521769472);
INSERT INTO `sys_role_menu` VALUES (624985379421949962, 571347202849509376, 571352087896657920);
INSERT INTO `sys_role_menu` VALUES (624985379421949963, 571347202849509376, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624985379421949964, 571347202849509376, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624985379421949965, 571347202849509376, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624985379421949966, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624985379421949967, 571347272906969088, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624985379421949968, 571347272906969088, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624985379421949969, 571347272906969088, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624985379421949970, 571347272906969088, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624985379421949971, 571347272906969088, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624985379421949972, 571347272906969088, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624985379421949973, 571347272906969088, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624985379421949974, 571347272906969088, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624985379421949975, 571347272906969088, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624985379421949976, 571347272906969088, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624985379421949977, 571347272906969088, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624985379421949978, 571347272906969088, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624985379421949979, 571347272906969088, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624985379421949980, 571347272906969088, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624985379421949981, 571347272906969088, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624985379421949982, 571347272906969088, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624985379421949983, 571347272906969088, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624985379421949984, 571347272906969088, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624985379421949985, 571347272906969088, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624985379421949986, 571347272906969088, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624985379421949987, 571347272906969088, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624985379421949988, 571347272906969088, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624985379421949989, 571347272906969088, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624985379421949990, 571347272906969088, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624985379421949991, 571347272906969088, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624985379421949992, 571347272906969088, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624985379421949993, 571347272906969088, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624985379421949994, 571347272906969088, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624985379421949995, 571347272906969088, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624985379421949996, 571347272906969088, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624985379421949997, 571347272906969088, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624985379421949998, 571347272906969088, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (624985379421949999, 571347272906969088, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (624985379421950000, 571347272906969088, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (624985379421950001, 571347272906969088, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (624985379421950002, 571347272906969088, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (624985379421950003, 571347272906969088, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (624985379421950004, 571347272906969088, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (624985379426144256, 571347272906969088, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (624985379426144257, 571347272906969088, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (624985379426144258, 571347272906969088, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (624985379426144259, 571347272906969088, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (624985379426144260, 571347272906969088, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (624985379426144261, 571347272906969088, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (624985379426144262, 571347272906969088, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (624985379426144263, 571347272906969088, 571454722205159424);
INSERT INTO `sys_role_menu` VALUES (624985379426144264, 571347272906969088, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (624985379426144265, 571347272906969088, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (624985379426144266, 571347272906969088, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (624985379426144267, 571347272906969088, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (624985379426144268, 571347272906969088, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (624985379426144269, 571347272906969088, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (624985379426144270, 571347272906969088, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (624985379426144271, 571347272906969088, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (624985379426144272, 571347272906969088, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (624985379426144273, 571347272906969088, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (624985379426144274, 571347272906969088, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (624985379426144275, 571347272906969088, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (624985379426144276, 571347272906969088, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624985379426144277, 571347272906969088, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624985379426144278, 571347272906969088, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624985379426144279, 586914735614726144, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624985379426144280, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624985379426144281, 571347357346697216, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624985379426144282, 571347357346697216, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624985379426144283, 571347357346697216, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624985379426144284, 571347357346697216, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624985379426144285, 571347357346697216, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624985379426144286, 571347357346697216, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624985379426144287, 571347357346697216, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624985379426144288, 571347357346697216, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624985379426144289, 571347357346697216, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624985379426144290, 571347357346697216, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624985379426144291, 571347357346697216, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624985379426144292, 571347357346697216, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624985379426144293, 571347357346697216, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624985379426144294, 571347357346697216, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624985379426144295, 571347357346697216, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624985379426144296, 571347357346697216, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624985379426144297, 571347357346697216, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624985379426144298, 571347357346697216, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624985379426144299, 571347357346697216, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624985379426144300, 571347357346697216, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624985379426144301, 571347357346697216, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624985379426144302, 571347357346697216, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624985379426144303, 571347357346697216, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624985379426144304, 571347357346697216, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624985379426144305, 571347357346697216, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624985379426144306, 571347357346697216, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624985379426144307, 571347357346697216, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624985379426144308, 571347357346697216, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624985379426144309, 571347357346697216, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624985379426144310, 571347357346697216, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624985379426144311, 571347357346697216, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624985379426144312, 571347357346697216, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624985379426144313, 571347357346697216, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624985379426144314, 571347357346697216, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624985379434532967, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624985379434532968, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624985379434532969, 596116511031169024, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624985379434532970, 596116511031169024, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624985379434532971, 596116511031169024, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615829475328, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615829475329, 571347202849509376, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624992615829475330, 571347202849509376, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624992615829475331, 571347202849509376, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615829475332, 571347202849509376, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624992615829475333, 571347202849509376, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624992615829475334, 571347202849509376, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624992615829475335, 571347202849509376, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624992615829475336, 571347202849509376, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624992615829475337, 571347202849509376, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624992615829475338, 571347202849509376, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624992615829475339, 571347202849509376, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624992615829475340, 571347202849509376, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624992615829475341, 571347202849509376, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624992615829475342, 571347202849509376, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624992615829475343, 571347202849509376, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624992615829475344, 571347202849509376, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624992615829475345, 571347202849509376, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624992615829475346, 571347202849509376, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624992615829475347, 571347202849509376, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624992615829475348, 571347202849509376, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624992615829475349, 571347202849509376, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624992615829475350, 571347202849509376, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624992615829475351, 571347202849509376, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624992615829475352, 571347202849509376, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624992615829475353, 571347202849509376, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624992615829475354, 571347202849509376, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624992615829475355, 571347202849509376, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624992615829475356, 571347202849509376, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624992615829475357, 571347202849509376, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624992615829475358, 571347202849509376, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624992615829475359, 571347202849509376, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624992615829475360, 571347202849509376, 581237996276289536);
INSERT INTO `sys_role_menu` VALUES (624992615829475361, 571347202849509376, 581238351663861760);
INSERT INTO `sys_role_menu` VALUES (624992615829475362, 571347202849509376, 581238560250793984);
INSERT INTO `sys_role_menu` VALUES (624992615829475363, 571347202849509376, 581238795467362304);
INSERT INTO `sys_role_menu` VALUES (624992615829475364, 571347202849509376, 581238883182841856);
INSERT INTO `sys_role_menu` VALUES (624992615829475365, 571347202849509376, 571361163502292992);
INSERT INTO `sys_role_menu` VALUES (624992615829475366, 571347202849509376, 571361526066319360);
INSERT INTO `sys_role_menu` VALUES (624992615829475367, 571347202849509376, 571364115214372864);
INSERT INTO `sys_role_menu` VALUES (624992615829475368, 571347202849509376, 571361746552492032);
INSERT INTO `sys_role_menu` VALUES (624992615829475369, 571347202849509376, 571362994005610496);
INSERT INTO `sys_role_menu` VALUES (624992615829475370, 571347202849509376, 571363268497641472);
INSERT INTO `sys_role_menu` VALUES (624992615829475371, 571347202849509376, 571363537549660160);
INSERT INTO `sys_role_menu` VALUES (624992615829475372, 571347202849509376, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (624992615829475373, 571347202849509376, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (624992615829475374, 571347202849509376, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (624992615829475375, 571347202849509376, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (624992615829475376, 571347202849509376, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (624992615829475377, 571347202849509376, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (624992615829475378, 571347202849509376, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (624992615829475379, 571347202849509376, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (624992615829475380, 571347202849509376, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (624992615829475381, 571347202849509376, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (624992615829475382, 571347202849509376, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (624992615829475383, 571347202849509376, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (624992615829475384, 571347202849509376, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (624992615829475385, 571347202849509376, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (624992615829475386, 571347202849509376, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (624992615829475387, 571347202849509376, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (624992615829475388, 571347202849509376, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (624992615829475389, 571347202849509376, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (624992615829475390, 571347202849509376, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (624992615829475391, 571347202849509376, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (624992615829475392, 571347202849509376, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (624992615829475393, 571347202849509376, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (624992615829475394, 571347202849509376, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (624992615829475395, 571347202849509376, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (624992615829475396, 571347202849509376, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (624992615829475397, 571347202849509376, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (624992615833669632, 571347202849509376, 571351763521769472);
INSERT INTO `sys_role_menu` VALUES (624992615833669633, 571347202849509376, 571352087896657920);
INSERT INTO `sys_role_menu` VALUES (624992615833669634, 571347202849509376, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615833669635, 571347202849509376, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615833669636, 571347202849509376, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615833669637, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615833669638, 571347272906969088, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624992615833669639, 571347272906969088, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624992615833669640, 571347272906969088, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615833669641, 571347272906969088, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624992615833669642, 571347272906969088, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624992615833669643, 571347272906969088, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624992615833669644, 571347272906969088, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624992615833669645, 571347272906969088, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624992615833669646, 571347272906969088, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624992615833669647, 571347272906969088, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624992615833669648, 571347272906969088, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624992615833669649, 571347272906969088, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624992615833669650, 571347272906969088, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624992615833669651, 571347272906969088, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624992615833669652, 571347272906969088, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624992615833669653, 571347272906969088, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624992615833669654, 571347272906969088, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624992615833669655, 571347272906969088, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624992615833669656, 571347272906969088, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624992615833669657, 571347272906969088, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624992615833669658, 571347272906969088, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624992615833669659, 571347272906969088, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624992615833669660, 571347272906969088, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624992615833669661, 571347272906969088, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624992615833669662, 571347272906969088, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624992615833669663, 571347272906969088, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624992615833669664, 571347272906969088, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624992615833669665, 571347272906969088, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624992615833669666, 571347272906969088, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624992615833669667, 571347272906969088, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624992615833669668, 571347272906969088, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624992615833669669, 571347272906969088, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (624992615833669670, 571347272906969088, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (624992615833669671, 571347272906969088, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (624992615833669672, 571347272906969088, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (624992615833669673, 571347272906969088, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (624992615833669674, 571347272906969088, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (624992615833669675, 571347272906969088, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (624992615833669676, 571347272906969088, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (624992615833669677, 571347272906969088, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (624992615833669678, 571347272906969088, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (624992615833669679, 571347272906969088, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (624992615833669680, 571347272906969088, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (624992615833669681, 571347272906969088, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (624992615833669682, 571347272906969088, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (624992615833669683, 571347272906969088, 571454722205159424);
INSERT INTO `sys_role_menu` VALUES (624992615833669684, 571347272906969088, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (624992615833669685, 571347272906969088, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (624992615833669686, 571347272906969088, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (624992615833669687, 571347272906969088, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (624992615833669688, 571347272906969088, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (624992615833669689, 571347272906969088, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (624992615833669690, 571347272906969088, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (624992615833669691, 571347272906969088, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (624992615833669692, 571347272906969088, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (624992615833669693, 571347272906969088, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (624992615833669694, 571347272906969088, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (624992615833669695, 571347272906969088, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (624992615833669696, 571347272906969088, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615833669697, 571347272906969088, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615833669698, 571347272906969088, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615833669699, 586914735614726144, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615833669700, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615833669701, 571347357346697216, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624992615833669702, 571347357346697216, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624992615833669703, 571347357346697216, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615833669704, 571347357346697216, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624992615833669705, 571347357346697216, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624992615833669706, 571347357346697216, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624992615833669707, 571347357346697216, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624992615833669708, 571347357346697216, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624992615833669709, 571347357346697216, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624992615833669710, 571347357346697216, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624992615833669711, 571347357346697216, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624992615833669712, 571347357346697216, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624992615833669713, 571347357346697216, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624992615833669714, 571347357346697216, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624992615833669715, 571347357346697216, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624992615833669716, 571347357346697216, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624992615833669717, 571347357346697216, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624992615833669718, 571347357346697216, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624992615833669719, 571347357346697216, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624992615833669720, 571347357346697216, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624992615833669721, 571347357346697216, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624992615833669722, 571347357346697216, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624992615833669723, 571347357346697216, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624992615833669724, 571347357346697216, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624992615833669725, 571347357346697216, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624992615833669726, 571347357346697216, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624992615833669727, 571347357346697216, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624992615833669728, 571347357346697216, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624992615833669729, 571347357346697216, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624992615833669730, 571347357346697216, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624992615833669731, 571347357346697216, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624992615833669732, 571347357346697216, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615833669733, 571347357346697216, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615833669734, 571347357346697216, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615837864087, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615837864088, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624992615837864089, 596116511031169024, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615837864090, 596116511031169024, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615837864091, 596116511031169024, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615837864092, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615837864093, 571347202849509376, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624992615837864094, 571347202849509376, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624992615837864095, 571347202849509376, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615837864096, 571347202849509376, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624992615837864097, 571347202849509376, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624992615837864098, 571347202849509376, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624992615837864099, 571347202849509376, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624992615837864100, 571347202849509376, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624992615837864101, 571347202849509376, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624992615837864102, 571347202849509376, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624992615837864103, 571347202849509376, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624992615837864104, 571347202849509376, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624992615837864105, 571347202849509376, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624992615837864106, 571347202849509376, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624992615837864107, 571347202849509376, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624992615837864108, 571347202849509376, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624992615837864109, 571347202849509376, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624992615837864110, 571347202849509376, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624992615837864111, 571347202849509376, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624992615837864112, 571347202849509376, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624992615837864113, 571347202849509376, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624992615837864114, 571347202849509376, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624992615837864115, 571347202849509376, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624992615837864116, 571347202849509376, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624992615837864117, 571347202849509376, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624992615837864118, 571347202849509376, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624992615837864119, 571347202849509376, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624992615837864120, 571347202849509376, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624992615837864121, 571347202849509376, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624992615837864122, 571347202849509376, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624992615837864123, 571347202849509376, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624992615837864124, 571347202849509376, 581237996276289536);
INSERT INTO `sys_role_menu` VALUES (624992615837864125, 571347202849509376, 581238351663861760);
INSERT INTO `sys_role_menu` VALUES (624992615837864126, 571347202849509376, 581238560250793984);
INSERT INTO `sys_role_menu` VALUES (624992615837864127, 571347202849509376, 581238795467362304);
INSERT INTO `sys_role_menu` VALUES (624992615837864128, 571347202849509376, 581238883182841856);
INSERT INTO `sys_role_menu` VALUES (624992615837864129, 571347202849509376, 571361163502292992);
INSERT INTO `sys_role_menu` VALUES (624992615837864130, 571347202849509376, 571361526066319360);
INSERT INTO `sys_role_menu` VALUES (624992615837864131, 571347202849509376, 571364115214372864);
INSERT INTO `sys_role_menu` VALUES (624992615837864132, 571347202849509376, 571361746552492032);
INSERT INTO `sys_role_menu` VALUES (624992615837864133, 571347202849509376, 571362994005610496);
INSERT INTO `sys_role_menu` VALUES (624992615837864134, 571347202849509376, 571363268497641472);
INSERT INTO `sys_role_menu` VALUES (624992615837864135, 571347202849509376, 571363537549660160);
INSERT INTO `sys_role_menu` VALUES (624992615837864136, 571347202849509376, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (624992615837864137, 571347202849509376, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (624992615837864138, 571347202849509376, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (624992615837864139, 571347202849509376, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (624992615837864140, 571347202849509376, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (624992615837864141, 571347202849509376, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (624992615837864142, 571347202849509376, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (624992615837864143, 571347202849509376, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (624992615837864144, 571347202849509376, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (624992615837864145, 571347202849509376, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (624992615837864146, 571347202849509376, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (624992615837864147, 571347202849509376, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (624992615837864148, 571347202849509376, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (624992615837864149, 571347202849509376, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (624992615837864150, 571347202849509376, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (624992615837864151, 571347202849509376, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (624992615837864152, 571347202849509376, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (624992615837864153, 571347202849509376, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (624992615837864154, 571347202849509376, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (624992615837864155, 571347202849509376, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (624992615837864156, 571347202849509376, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (624992615837864157, 571347202849509376, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (624992615837864158, 571347202849509376, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (624992615837864159, 571347202849509376, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (624992615837864160, 571347202849509376, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (624992615837864161, 571347202849509376, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (624992615837864162, 571347202849509376, 571351763521769472);
INSERT INTO `sys_role_menu` VALUES (624992615837864163, 571347202849509376, 571352087896657920);
INSERT INTO `sys_role_menu` VALUES (624992615837864164, 571347202849509376, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615837864165, 571347202849509376, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615837864166, 571347202849509376, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615837864167, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615837864168, 571347272906969088, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624992615837864169, 571347272906969088, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624992615837864170, 571347272906969088, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615837864171, 571347272906969088, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624992615837864172, 571347272906969088, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624992615837864173, 571347272906969088, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624992615837864174, 571347272906969088, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624992615837864175, 571347272906969088, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624992615837864176, 571347272906969088, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624992615837864177, 571347272906969088, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624992615837864178, 571347272906969088, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624992615837864179, 571347272906969088, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624992615837864180, 571347272906969088, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624992615837864181, 571347272906969088, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624992615837864182, 571347272906969088, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624992615837864183, 571347272906969088, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624992615837864184, 571347272906969088, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624992615837864185, 571347272906969088, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624992615837864186, 571347272906969088, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624992615837864187, 571347272906969088, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624992615837864188, 571347272906969088, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624992615837864189, 571347272906969088, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624992615837864190, 571347272906969088, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624992615837864191, 571347272906969088, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624992615837864192, 571347272906969088, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624992615837864193, 571347272906969088, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624992615837864194, 571347272906969088, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624992615837864195, 571347272906969088, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624992615837864196, 571347272906969088, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624992615837864197, 571347272906969088, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624992615837864198, 571347272906969088, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624992615846252544, 571347272906969088, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (624992615846252545, 571347272906969088, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (624992615846252546, 571347272906969088, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (624992615846252547, 571347272906969088, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (624992615846252548, 571347272906969088, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (624992615846252549, 571347272906969088, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (624992615846252550, 571347272906969088, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (624992615846252551, 571347272906969088, 571355830226653184);
INSERT INTO `sys_role_menu` VALUES (624992615846252552, 571347272906969088, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (624992615846252553, 571347272906969088, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (624992615846252554, 571347272906969088, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (624992615846252555, 571347272906969088, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (624992615846252556, 571347272906969088, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (624992615846252557, 571347272906969088, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (624992615846252558, 571347272906969088, 571454722205159424);
INSERT INTO `sys_role_menu` VALUES (624992615846252559, 571347272906969088, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (624992615846252560, 571347272906969088, 571357509638557696);
INSERT INTO `sys_role_menu` VALUES (624992615846252561, 571347272906969088, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (624992615846252562, 571347272906969088, 571357937931522048);
INSERT INTO `sys_role_menu` VALUES (624992615846252563, 571347272906969088, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (624992615846252564, 571347272906969088, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (624992615846252565, 571347272906969088, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (624992615846252566, 571347272906969088, 571358617991778304);
INSERT INTO `sys_role_menu` VALUES (624992615846252567, 571347272906969088, 571358928483520512);
INSERT INTO `sys_role_menu` VALUES (624992615846252568, 571347272906969088, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (624992615846252569, 571347272906969088, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (624992615846252570, 571347272906969088, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (624992615846252571, 571347272906969088, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615846252572, 571347272906969088, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615846252573, 571347272906969088, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615846252574, 586914735614726144, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615846252575, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615846252576, 571347357346697216, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (624992615846252577, 571347357346697216, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (624992615846252578, 571347357346697216, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (624992615846252579, 571347357346697216, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (624992615846252580, 571347357346697216, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (624992615846252581, 571347357346697216, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (624992615846252582, 571347357346697216, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (624992615846252583, 571347357346697216, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (624992615846252584, 571347357346697216, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (624992615846252585, 571347357346697216, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (624992615846252586, 571347357346697216, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (624992615846252587, 571347357346697216, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (624992615846252588, 571347357346697216, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (624992615846252589, 571347357346697216, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (624992615846252590, 571347357346697216, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (624992615846252591, 571347357346697216, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (624992615846252592, 571347357346697216, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (624992615846252593, 571347357346697216, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (624992615846252594, 571347357346697216, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (624992615846252595, 571347357346697216, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (624992615846252596, 571347357346697216, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (624992615846252597, 571347357346697216, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (624992615846252598, 571347357346697216, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (624992615846252599, 571347357346697216, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (624992615846252600, 571347357346697216, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (624992615846252601, 571347357346697216, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (624992615846252602, 571347357346697216, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (624992615846252603, 571347357346697216, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (624992615846252604, 571347357346697216, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (624992615846252605, 571347357346697216, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (624992615846252606, 571347357346697216, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (624992615846252607, 571347357346697216, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615846252608, 571347357346697216, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615846252609, 571347357346697216, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624992615846252775, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624992615846252776, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624992615846252777, 596116511031169024, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (624992615846252778, 596116511031169024, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (624992615846252779, 596116511031169024, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (624993535845863424, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863425, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863426, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863433, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863434, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624993535845863435, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863436, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863437, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863444, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863445, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624993535845863446, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535845863447, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057728, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057735, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057736, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (624993535850057737, 571347202849509376, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057738, 571347272906969088, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057739, 571347357346697216, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057746, 596116511031169024, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (624993535850057747, 596116511031169024, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (625059215752433664, 596117256346406912, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (625059215752433665, 596117256346406912, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (625059215752433666, 596117256346406912, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (625059215752433667, 596117256346406912, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (625059215752433668, 596117256346406912, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (625059215752433669, 596117256346406912, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (625059215752433670, 596117256346406912, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (625059215752433671, 596117256346406912, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (625059215752433672, 596117256346406912, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (625059215752433673, 596117256346406912, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (625059215752433674, 596117256346406912, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (625059215752433675, 596117256346406912, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (625059215752433676, 596117256346406912, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (625059215752433677, 596117256346406912, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (625059215752433678, 596117256346406912, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (625059215752433679, 596117256346406912, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (625059215752433680, 596117256346406912, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (625059215752433681, 596117256346406912, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (625059215752433682, 596117256346406912, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (625059215752433683, 596117256346406912, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (625059215752433684, 596117256346406912, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (625059215752433685, 596117256346406912, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (625059215752433686, 596117256346406912, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (625059215752433687, 596117256346406912, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (625059215752433688, 596117256346406912, 571369709904203776);
INSERT INTO `sys_role_menu` VALUES (625059215752433689, 596117256346406912, 571374593844056064);
INSERT INTO `sys_role_menu` VALUES (625059215752433690, 596117256346406912, 571374671245742080);
INSERT INTO `sys_role_menu` VALUES (625059215752433691, 596117256346406912, 571374747460440064);
INSERT INTO `sys_role_menu` VALUES (625059215752433692, 596117256346406912, 571369965811273728);
INSERT INTO `sys_role_menu` VALUES (625059215752433693, 596117256346406912, 571374884270247936);
INSERT INTO `sys_role_menu` VALUES (625059215752433694, 596117256346406912, 571374951823708160);
INSERT INTO `sys_role_menu` VALUES (625059215752433695, 596117256346406912, 571375033570693120);
INSERT INTO `sys_role_menu` VALUES (625059215752433696, 596117256346406912, 571375135655858176);
INSERT INTO `sys_role_menu` VALUES (625059215752433697, 596117256346406912, 581237996276289536);
INSERT INTO `sys_role_menu` VALUES (625059215752433698, 596117256346406912, 581238351663861760);
INSERT INTO `sys_role_menu` VALUES (625059215752433699, 596117256346406912, 581238560250793984);
INSERT INTO `sys_role_menu` VALUES (625059215752433700, 596117256346406912, 581238795467362304);
INSERT INTO `sys_role_menu` VALUES (625059215752433701, 596117256346406912, 581238883182841856);
INSERT INTO `sys_role_menu` VALUES (625059215752433702, 596117256346406912, 571361163502292992);
INSERT INTO `sys_role_menu` VALUES (625059215752433703, 596117256346406912, 571361526066319360);
INSERT INTO `sys_role_menu` VALUES (625059215752433704, 596117256346406912, 571364115214372864);
INSERT INTO `sys_role_menu` VALUES (625059215752433705, 596117256346406912, 571361746552492032);
INSERT INTO `sys_role_menu` VALUES (625059215752433706, 596117256346406912, 571362994005610496);
INSERT INTO `sys_role_menu` VALUES (625059215752433707, 596117256346406912, 571363268497641472);
INSERT INTO `sys_role_menu` VALUES (625059215752433708, 596117256346406912, 571363537549660160);
INSERT INTO `sys_role_menu` VALUES (625059215752433709, 596117256346406912, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (625059215752433710, 596117256346406912, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (625059215752433711, 596117256346406912, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (625059215752433712, 596117256346406912, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (625059215752433713, 596117256346406912, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (625059215752433714, 596117256346406912, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (625059215752433715, 596117256346406912, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (625059215752433716, 596117256346406912, 625058053556932608);
INSERT INTO `sys_role_menu` VALUES (625059215752433717, 596117256346406912, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (625059215752433718, 596117256346406912, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (625059215752433719, 596117256346406912, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (625059215752433720, 596117256346406912, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (625059215752433721, 596117256346406912, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (625059215752433722, 596117256346406912, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (625059215752433723, 596117256346406912, 625058859773464576);
INSERT INTO `sys_role_menu` VALUES (625059215752433724, 596117256346406912, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (625059215752433725, 596117256346406912, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (625059215752433726, 596117256346406912, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (625059215752433727, 596117256346406912, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (625059215752433728, 596117256346406912, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (625059215752433729, 596117256346406912, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (625059215752433730, 596117256346406912, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (625059215752433731, 596117256346406912, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (625059215752433732, 596117256346406912, 571351763521769472);
INSERT INTO `sys_role_menu` VALUES (625059215752433733, 596117256346406912, 571352087896657920);
INSERT INTO `sys_role_menu` VALUES (625059215752433734, 596117256346406912, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (625059215752433735, 596117256346406912, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (625059215752433736, 596117256346406912, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (625059307532193792, 596330074307956736, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (625059307532193793, 596330074307956736, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (625059307532193794, 596330074307956736, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (625059307532193795, 596330074307956736, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (625059307532193796, 596330074307956736, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (625059307532193797, 596330074307956736, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (625059307532193798, 596330074307956736, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (625059307532193799, 596330074307956736, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (625059307532193800, 596330074307956736, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (625059307532193801, 596330074307956736, 625058053556932608);
INSERT INTO `sys_role_menu` VALUES (625059307532193802, 596330074307956736, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (625059307532193803, 596330074307956736, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (625059307532193804, 596330074307956736, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (625059307532193805, 596330074307956736, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (625059307532193806, 596330074307956736, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (625059307532193807, 596330074307956736, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (625059307532193808, 596330074307956736, 625058859773464576);
INSERT INTO `sys_role_menu` VALUES (625059307532193809, 596330074307956736, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (625059307532193810, 596330074307956736, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (625059307532193811, 596330074307956736, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (625059307532193812, 596330074307956736, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (625059307532193813, 596330074307956736, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (625059307532193814, 596330074307956736, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (625059307532193815, 596330074307956736, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (625059307532193816, 596330074307956736, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (625059307532193817, 596330074307956736, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (625059307532193818, 596330074307956736, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (625059307532193819, 596330074307956736, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (630780408950296576, 624964171867492352, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (630780408950296577, 624964171867492352, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (630780408950296578, 624964171867492352, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (630780408950296579, 624964171867492352, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (630780408950296580, 624964171867492352, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (630780408950296581, 624964171867492352, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (630780408950296582, 624964171867492352, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (630780408950296583, 624964171867492352, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (630780408950296584, 624964171867492352, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (630780408950296585, 624964171867492352, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (630780408950296586, 624964171867492352, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (630780408950296587, 624964171867492352, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (630780408950296588, 624964171867492352, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (630780408950296589, 624964171867492352, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (630780408950296590, 624964171867492352, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (630780408950296591, 624964171867492352, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (630780408950296592, 624964171867492352, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (630780408950296593, 624964171867492352, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (630780408950296594, 624964171867492352, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (630780408950296595, 624964171867492352, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (630780408950296596, 624964171867492352, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (630780408950296597, 624964171867492352, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (630780408950296598, 624964171867492352, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (630780408950296599, 624964171867492352, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (630780408950296600, 624964171867492352, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (630780408950296601, 624964171867492352, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (630780408950296602, 624964171867492352, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (630780408950296603, 624964171867492352, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (630780408950296604, 624964171867492352, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (630780408950296605, 624964171867492352, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (630780408950296606, 624964171867492352, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (630780408950296607, 624964171867492352, 625058053556932608);
INSERT INTO `sys_role_menu` VALUES (630780408950296608, 624964171867492352, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (630780408950296609, 624964171867492352, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (630780408950296610, 624964171867492352, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (630780408950296611, 624964171867492352, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (630780408950296612, 624964171867492352, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (630780408950296613, 624964171867492352, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (630780408950296614, 624964171867492352, 625058859773464576);
INSERT INTO `sys_role_menu` VALUES (630780408950296615, 624964171867492352, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (630780408950296616, 624964171867492352, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (630780408950296617, 624964171867492352, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (630780408950296618, 624964171867492352, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (630780408950296619, 624964171867492352, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (630780408950296620, 624964171867492352, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (630780408950296621, 624964171867492352, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (630780408950296622, 624964171867492352, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (630780408950296623, 624964171867492352, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (630780408950296624, 624964171867492352, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (630780408950296625, 624964171867492352, 571350099653955584);

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;
CREATE TABLE `sys_student`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `student_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `born` date NULL DEFAULT NULL,
  `sex` tinyint(4) NULL DEFAULT NULL COMMENT '难度等级',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `grade` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细描述',
  `school` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就读学校',
  `wechat` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信',
  `city_id` bigint(20) NULL DEFAULT NULL COMMENT '城市id',
  `county_id` bigint(20) NULL DEFAULT NULL COMMENT '县id',
  `creator` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学生表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '租户标识',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '租户名称',
  `tenant_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户描述信息',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态',
  `creator` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `modifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `modify_date` timestamp(0) NULL DEFAULT NULL,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '租户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (581236567985754112, 'gitee', '码云', '码云', 1, 'admin', '2019-05-23 21:46:36', 'admin', '2019-05-23 21:46:36', 0);
INSERT INTO `sys_tenant` VALUES (624983209976926208, 'test', 'test', 'test', 1, 'admin', '2019-09-21 19:34:42', 'admin', '2019-09-21 19:26:31', 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `avatar_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像id',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `born` date NULL DEFAULT NULL COMMENT '出生日期',
  `status` tinyint(4) NOT NULL DEFAULT 0,
  `sex` tinyint(4) NULL DEFAULT NULL COMMENT '性别',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `user_desc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细描述',
  `parent_uid` bigint(20) NULL DEFAULT NULL COMMENT '父账号id',
  `street_id` bigint(20) NULL DEFAULT NULL COMMENT '街道id',
  `county_id` bigint(20) NULL DEFAULT NULL COMMENT '国家id',
  `city_id` bigint(20) NULL DEFAULT NULL COMMENT '城市id',
  `province_id` bigint(20) NULL DEFAULT NULL COMMENT '省id',
  `login_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `lock_time` timestamp(0) NULL DEFAULT NULL COMMENT '锁定账号时间',
  `wechat` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `family_role` tinyint(4) NULL DEFAULT NULL COMMENT '家庭角色',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (596078038307966976, '管理员', '15521089185', '596094244884713472', '1633736729@qq.com', '2019-07-01', 0, 0, 571347099191480320, '管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-09-08 20:57:57', 'admin', '2019-07-04 13:51:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (596307222997372928, '梁同学', '15521089185', NULL, '1633736729@qq.com', '2019-07-01', 0, 1, NULL, '梁同学', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-09-08 20:57:57', 'admin', '2019-07-05 21:53:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (596332387600830464, '林老师', '15521089185', NULL, '1633736729@qq.com', '2019-07-03', 0, 1, NULL, '林老师', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-09-21 22:33:16', 'admin', '2019-09-21 22:25:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (627482273154994176, 'test1', '15521089185', NULL, '2232@11.com', '2019-10-01', 0, 0, 596290673729212416, 'test', 596329627606192128, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-10-07 14:59:17', 'admin', '2019-10-07 14:50:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (630779163304923136, 'test2', '15521089185', NULL, NULL, NULL, 0, 0, NULL, '', 596329627606192128, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-10-08 21:55:30', 'admin', '2019-10-08 21:47:04', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_user_auths
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_auths`;
CREATE TABLE `sys_user_auths`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `identity_type` tinyint(4) NOT NULL COMMENT '登录类型，手机号、邮箱、用户名或第三方应用名称（微信 微博等）',
  `identifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识，手机号、邮箱、用户名或第三方应用的唯一标识',
  `credential` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码凭证，站内的保存密码，站外的不保存或保存token',
  `creator` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_auths
-- ----------------------------
INSERT INTO `sys_user_auths` VALUES (596329627606192128, 596078038307966976, 1, 'admin', '$2a$10$Lp3nTbBcPPcVCRNiRuqRteAzQfyjPzFWshT4ZyeTNeKMtPjzmPDHa', 'admin', '2019-07-04 13:21:02', 'admin', '2019-07-04 13:21:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES (596329627648135168, 596307222997372928, 1, 'student', '$2a$10$czmVw4WF7Qt7RpwDJ4V4W.jkDKheEev63HlIsP31QnWHVOpSJz3au', 'admin', '2019-07-04 13:21:03', 'admin', '2019-07-04 13:21:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES (596332387693105152, 596332387600830464, 1, 'teacher', '$2a$10$rRuYK1s8xKeGdRPefO1nMuE9rMH6yjmszznXySqcfuuXM/QSssZXW', 'admin', '2019-07-04 13:32:01', 'admin', '2019-07-04 13:32:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES (627482274002243584, 627482273154994176, 1, 'test', '$2a$10$ekScY5JQjDByL./CUcHrjuM3HLagshRqZ.nBh6GYvYAIPzNNF0Uh2', 'admin', '2019-09-28 12:30:32', 'admin', '2019-09-28 12:30:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES (630779164261224448, 630779163304923136, 1, 'test2', '$2a$10$9knBg6uoDSZiZi4DYGWuMuIfojpVYH1LGEmfBArDrTGzDYouVSteG', 'admin', '2019-10-07 14:51:12', 'admin', '2019-10-07 14:51:12', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (596307898116739072, 596078038307966976, 596117256346406912);
INSERT INTO `sys_user_role` VALUES (596820924016234496, 596307222997372928, 596116511031169024);
INSERT INTO `sys_user_role` VALUES (624959784956858368, 624959783077810176, 596117256346406912);
INSERT INTO `sys_user_role` VALUES (624983494547869696, 624983493071474688, 624964171867492352);
INSERT INTO `sys_user_role` VALUES (625079311711473664, 596332387600830500, 596330074307956700);
INSERT INTO `sys_user_role` VALUES (625079323929481216, 596078038307967000, 596117256346406900);
INSERT INTO `sys_user_role` VALUES (627482325994835968, 627482273154994200, 596116511031169000);
INSERT INTO `sys_user_role` VALUES (630779074050134016, 627482273154994176, 596116511031169024);
INSERT INTO `sys_user_role` VALUES (631246209843073024, 630779163304923136, 596116511031169024);

-- ----------------------------
-- Table structure for sys_user_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_student`;
CREATE TABLE `sys_user_student`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '学生姓名',
  `student_id` bigint(20) NOT NULL COMMENT '电话号码',
  `relationship_type` tinyint(4) NOT NULL COMMENT '关系类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户学生关联表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
