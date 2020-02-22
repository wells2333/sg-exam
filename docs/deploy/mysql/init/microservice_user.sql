
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
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

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
INSERT INTO `sys_dept` VALUES (596290673729212416, '测试部门', '测试部门', '测试部门', -1, 30, 'admin', '2019-12-15 20:09:48', 'admin', '2019-12-15 20:00:10', 0, 'EXAM', 'gitee');
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
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (654101868447928320, 0, '更新用户基本信息', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/user/updateInfo', 'PUT', '', NULL, 'web_app', '631', 'admin', '2019-12-10 23:27:18', 'admin', '2019-12-10 23:27:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654101892493873152, 0, '重置密码', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/user/resetPassword', 'PUT', '', NULL, 'web_app', '447', 'admin', '2019-12-10 23:27:24', 'admin', '2019-12-10 23:27:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654101897770307584, 0, '更新用户基本信息', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/user/updateInfo', 'PUT', '', NULL, 'web_app', '238', 'admin', '2019-12-10 23:27:25', 'admin', '2019-12-10 23:27:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654435085185060864, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '170', 'admin', '2019-12-11 21:31:23', 'admin', '2019-12-11 21:31:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654436308160221184, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '317', 'admin', '2019-12-11 21:36:15', 'admin', '2019-12-11 21:36:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654436996416147456, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '152', 'admin', '2019-12-11 21:38:59', 'admin', '2019-12-11 21:38:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654438516587106304, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '250', 'admin', '2019-12-11 21:45:01', 'admin', '2019-12-11 21:45:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654440035709489152, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '178', 'admin', '2019-12-11 21:51:03', 'admin', '2019-12-11 21:51:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654440979562106880, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '185', 'admin', '2019-12-11 21:54:48', 'admin', '2019-12-11 21:54:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654441195489071104, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '98', 'admin', '2019-12-11 21:55:40', 'admin', '2019-12-11 21:55:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654441462087421952, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '103', 'admin', '2019-12-11 21:56:43', 'admin', '2019-12-11 21:56:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654441531247300608, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '104', 'admin', '2019-12-11 21:57:00', 'admin', '2019-12-11 21:57:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654441875683545088, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2019-12-11 21:58:22', 'admin', '2019-12-11 21:58:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654442439716769792, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '98', 'admin', '2019-12-11 22:00:37', 'admin', '2019-12-11 22:00:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654442518804566016, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '338', 'admin', '2019-12-11 22:00:55', 'admin', '2019-12-11 22:00:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654443202551615488, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '152', 'admin', '2019-12-11 22:03:38', 'admin', '2019-12-11 22:03:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654443451793936384, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '97', 'admin', '2019-12-11 22:04:38', 'admin', '2019-12-11 22:04:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654444311571730432, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '99', 'admin', '2019-12-11 22:08:03', 'admin', '2019-12-11 22:08:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654445055737729024, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '107', 'admin', '2019-12-11 22:11:00', 'admin', '2019-12-11 22:11:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654445538565033984, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '109', 'admin', '2019-12-11 22:12:55', 'admin', '2019-12-11 22:12:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654445711252918272, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '94', 'admin', '2019-12-11 22:13:37', 'admin', '2019-12-11 22:13:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654445863279661056, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '98', 'admin', '2019-12-11 22:14:13', 'admin', '2019-12-11 22:14:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654445911094726656, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '364', 'admin', '2019-12-11 22:14:24', 'admin', '2019-12-11 22:14:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654446642514235392, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '490', 'admin', '2019-12-11 22:17:19', 'admin', '2019-12-11 22:17:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447041300271104, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '410', 'admin', '2019-12-11 22:18:54', 'admin', '2019-12-11 22:18:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447057603530752, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '343', 'admin', '2019-12-11 22:18:58', 'admin', '2019-12-11 22:18:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447071100801024, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '350', 'admin', '2019-12-11 22:19:01', 'admin', '2019-12-11 22:19:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447089819979776, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '254', 'admin', '2019-12-11 22:19:05', 'admin', '2019-12-11 22:19:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447147906895872, 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/user/596332387600830464', 'PUT', '', NULL, 'web_app', '493', 'admin', '2019-12-11 22:19:19', 'admin', '2019-12-11 22:19:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447178315599872, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '716', 'admin', '2019-12-11 22:19:26', 'admin', '2019-12-11 22:19:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447190797848576, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '240', 'admin', '2019-12-11 22:19:29', 'admin', '2019-12-11 22:19:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447232564727808, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '312', 'admin', '2019-12-11 22:19:39', 'admin', '2019-12-11 22:19:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447245541904384, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '242', 'admin', '2019-12-11 22:19:42', 'admin', '2019-12-11 22:19:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447289082974208, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '355', 'admin', '2019-12-11 22:19:53', 'admin', '2019-12-11 22:19:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447291213680640, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '305', 'admin', '2019-12-11 22:19:53', 'admin', '2019-12-11 22:19:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447297572245504, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '230', 'admin', '2019-12-11 22:19:55', 'admin', '2019-12-11 22:19:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447310947880960, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '248', 'admin', '2019-12-11 22:19:58', 'admin', '2019-12-11 22:19:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447316681494528, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '280', 'admin', '2019-12-11 22:19:59', 'admin', '2019-12-11 22:19:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447323480461312, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '243', 'admin', '2019-12-11 22:20:01', 'admin', '2019-12-11 22:20:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447353385848832, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '358', 'admin', '2019-12-11 22:20:08', 'admin', '2019-12-11 22:20:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447360331616256, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '315', 'admin', '2019-12-11 22:20:10', 'admin', '2019-12-11 22:20:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447366253973504, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '246', 'admin', '2019-12-11 22:20:11', 'admin', '2019-12-11 22:20:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447370758656000, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '254', 'admin', '2019-12-11 22:20:12', 'admin', '2019-12-11 22:20:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447376077033472, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '268', 'admin', '2019-12-11 22:20:14', 'admin', '2019-12-11 22:20:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447381558988800, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '274', 'admin', '2019-12-11 22:20:15', 'admin', '2019-12-11 22:20:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447406527680512, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '350', 'admin', '2019-12-11 22:20:21', 'admin', '2019-12-11 22:20:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447412068356096, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '252', 'admin', '2019-12-11 22:20:22', 'admin', '2019-12-11 22:20:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447603106320384, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '424', 'admin', '2019-12-11 22:21:08', 'admin', '2019-12-11 22:21:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447713030639616, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '236', 'admin', '2019-12-11 22:21:34', 'admin', '2019-12-11 22:21:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654447875153072128, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '255', 'admin', '2019-12-11 22:22:12', 'admin', '2019-12-11 22:22:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654449103203340288, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '108', 'admin', '2019-12-11 22:27:05', 'admin', '2019-12-11 22:27:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654451487128293376, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '107', 'admin', '2019-12-11 22:36:34', 'admin', '2019-12-11 22:36:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654451544871276544, 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/user/596332387600830464', 'PUT', '', NULL, 'web_app', '414', 'admin', '2019-12-11 22:36:47', 'admin', '2019-12-11 22:36:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654459308817387520, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '123', 'admin', '2019-12-11 23:07:38', 'admin', '2019-12-11 23:07:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654460313143480320, 0, '修改角色', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/role/', 'PUT', '', NULL, 'web_app', '306', 'admin', '2019-12-11 23:11:38', 'admin', '2019-12-11 23:11:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654460734440345600, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '100', 'admin', '2019-12-11 23:13:18', 'admin', '2019-12-11 23:13:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654461519786020864, 0, '更新部门', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '263', 'admin', '2019-12-11 23:16:26', 'admin', '2019-12-11 23:16:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654461613285445632, 0, '上传文件', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/attachment/upload', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '1574', 'admin', '2019-12-11 23:16:48', 'admin', '2019-12-11 23:16:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654461636438003712, 0, '更新用户头像', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/user/updateAvatar', 'PUT', '', NULL, 'web_app', '5447', 'admin', '2019-12-11 23:16:53', 'admin', '2019-12-11 23:16:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (654461708999462912, 0, '用户登录', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '283', 'admin', '2019-12-11 23:17:11', 'admin', '2019-12-11 23:17:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655533650036789248, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '402', 'admin', '2019-12-14 22:16:41', 'admin', '2019-12-14 22:16:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655534330285789184, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '90', 'admin', '2019-12-14 22:19:24', 'admin', '2019-12-14 22:19:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655534478478938112, 0, '更新考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '298', 'admin', '2019-12-14 22:19:59', 'admin', '2019-12-14 22:19:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655534539766108160, 0, '更新考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '221', 'admin', '2019-12-14 22:20:14', 'admin', '2019-12-14 22:20:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655534670661947392, 0, '更新考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '227', 'admin', '2019-12-14 22:20:45', 'admin', '2019-12-14 22:20:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655534708230328320, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '507', 'admin', '2019-12-14 22:20:54', 'admin', '2019-12-14 22:20:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655534979853455360, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '86', 'admin', '2019-12-14 22:21:58', 'admin', '2019-12-14 22:21:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655535082072838144, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '85', 'admin', '2019-12-14 22:22:23', 'admin', '2019-12-14 22:22:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655535144383418368, 0, '修改客户端', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '324', 'admin', '2019-12-14 22:22:38', 'admin', '2019-12-14 22:22:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655535483262210048, 0, '更新考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '231', 'admin', '2019-12-14 22:23:58', 'admin', '2019-12-14 22:23:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655535542318010368, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '422', 'admin', '2019-12-14 22:24:13', 'admin', '2019-12-14 22:24:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655536592357822464, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '137', 'admin', '2019-12-14 22:28:23', 'admin', '2019-12-14 22:28:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655536624905621504, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '371', 'admin', '2019-12-14 22:28:31', 'admin', '2019-12-14 22:28:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655540939019915264, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '88', 'admin', '2019-12-14 22:45:39', 'admin', '2019-12-14 22:45:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655541010566352896, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '399', 'admin', '2019-12-14 22:45:56', 'admin', '2019-12-14 22:45:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655541090908246016, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '86', 'admin', '2019-12-14 22:46:15', 'admin', '2019-12-14 22:46:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655542808035332096, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '221', 'admin', '2019-12-14 22:53:05', 'admin', '2019-12-14 22:53:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655542864419360768, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '467', 'admin', '2019-12-14 22:53:18', 'admin', '2019-12-14 22:53:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655542967020425216, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '224', 'admin', '2019-12-14 22:53:43', 'admin', '2019-12-14 22:53:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655543876903374848, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '225', 'admin', '2019-12-14 22:57:20', 'admin', '2019-12-14 22:57:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655544005861445632, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '209', 'admin', '2019-12-14 22:57:50', 'admin', '2019-12-14 22:57:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655544120055566336, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '446', 'admin', '2019-12-14 22:58:18', 'admin', '2019-12-14 22:58:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655544288855330816, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '232', 'admin', '2019-12-14 22:58:58', 'admin', '2019-12-14 22:58:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655547192085450752, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '452', 'admin', '2019-12-14 23:10:30', 'admin', '2019-12-14 23:10:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655547266274299904, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '207', 'admin', '2019-12-14 23:10:48', 'admin', '2019-12-14 23:10:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655856957697167360, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '166', 'admin', '2019-12-15 19:41:24', 'admin', '2019-12-15 19:41:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655857400791830528, 0, '更新部门', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '208', 'admin', '2019-12-15 19:43:10', 'admin', '2019-12-15 19:43:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655857706250407936, 0, '更新部门', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '206', 'admin', '2019-12-15 19:44:22', 'admin', '2019-12-15 19:44:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655861678432456704, 0, '更新部门', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '229', 'admin', '2019-12-15 20:00:09', 'admin', '2019-12-15 20:00:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655861681972449280, 0, '更新部门', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/dept/', 'PUT', '', NULL, 'web_app', '180', 'admin', '2019-12-15 20:00:10', 'admin', '2019-12-15 20:00:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655862961721708544, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '368', 'admin', '2019-12-15 20:05:15', 'admin', '2019-12-15 20:05:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655863104084774912, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '799', 'admin', '2019-12-15 20:05:49', 'admin', '2019-12-15 20:05:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655863329474088960, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '449', 'admin', '2019-12-15 20:06:43', 'admin', '2019-12-15 20:06:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655863477742735360, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '218', 'admin', '2019-12-15 20:07:18', 'admin', '2019-12-15 20:07:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655866431673602048, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '447', 'admin', '2019-12-15 20:19:03', 'admin', '2019-12-15 20:19:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655871513236475904, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '425', 'admin', '2019-12-15 20:39:14', 'admin', '2019-12-15 20:39:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655873890794475520, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '445', 'admin', '2019-12-15 20:48:41', 'admin', '2019-12-15 20:48:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655873928899727360, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '426', 'admin', '2019-12-15 20:48:50', 'admin', '2019-12-15 20:48:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655874176376246272, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '431', 'admin', '2019-12-15 20:49:49', 'admin', '2019-12-15 20:49:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655874349244485632, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '434', 'admin', '2019-12-15 20:50:30', 'admin', '2019-12-15 20:50:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655885391873839104, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2019-12-15 21:34:23', 'admin', '2019-12-15 21:34:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655885416347602944, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '424', 'admin', '2019-12-15 21:34:29', 'admin', '2019-12-15 21:34:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655886795258597376, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '223', 'admin', '2019-12-15 21:39:58', 'admin', '2019-12-15 21:39:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655889901446893568, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '406', 'admin', '2019-12-15 21:52:18', 'admin', '2019-12-15 21:52:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655889982883500032, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '213', 'admin', '2019-12-15 21:52:38', 'admin', '2019-12-15 21:52:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (655901196468490240, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '91', 'admin', '2019-12-15 22:37:11', 'admin', '2019-12-15 22:37:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656247717101703168, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '232', 'admin', '2019-12-16 21:34:08', 'admin', '2019-12-16 21:34:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656248225703006208, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '87', 'admin', '2019-12-16 21:36:10', 'admin', '2019-12-16 21:36:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656250472537133056, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '88', 'admin', '2019-12-16 21:45:05', 'admin', '2019-12-16 21:45:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656251606504968192, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '87', 'admin', '2019-12-16 21:49:36', 'admin', '2019-12-16 21:49:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656251729779757056, 0, '开始考试', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '404', 'admin', '2019-12-16 21:50:05', 'admin', '2019-12-16 21:50:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656252056243408896, 0, '提交答题', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '215', 'admin', '2019-12-16 21:51:23', 'admin', '2019-12-16 21:51:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (656605467568640000, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '188', 'admin', '2019-12-17 21:15:43', 'admin', '2019-12-17 21:15:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659150292155240448, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '552', 'admin', '2019-12-24 21:47:56', 'admin', '2019-12-24 21:47:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659150655885283328, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '292', 'admin', '2019-12-24 21:49:23', 'admin', '2019-12-24 21:49:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659150819567996928, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '258', 'admin', '2019-12-24 21:50:02', 'admin', '2019-12-24 21:50:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659151066398593024, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '129', 'admin', '2019-12-24 21:51:01', 'admin', '2019-12-24 21:51:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659152330834448384, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '87', 'admin', '2019-12-24 21:56:02', 'admin', '2019-12-24 21:56:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659152402938728448, 0, '修改客户端', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/client/', 'PUT', '', NULL, 'web_app', '268', 'admin', '2019-12-24 21:56:19', 'admin', '2019-12-24 21:56:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659152616399441920, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '223', 'admin', '2019-12-24 21:57:10', 'admin', '2019-12-24 21:57:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659869870711771136, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '607', 'admin', '2019-12-26 21:27:17', 'admin', '2019-12-26 21:27:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659877292134240256, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '91', 'admin', '2019-12-26 21:56:46', 'admin', '2019-12-26 21:56:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (659889886974840832, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '87', 'admin', '2019-12-26 22:46:49', 'admin', '2019-12-26 22:46:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660247552205131776, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '223', 'admin', '2019-12-27 22:28:03', 'admin', '2019-12-27 22:28:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660248787842895872, 0, '新增题目', '192.168.75.106', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '490', 'admin', '2019-12-27 22:32:58', 'admin', '2019-12-27 22:32:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660455620994011136, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '671', 'admin', '2019-12-28 12:14:51', 'admin', '2019-12-28 12:14:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660461290686386176, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '399', 'admin', '2019-12-28 12:37:22', 'admin', '2019-12-28 12:37:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660461453656068096, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '393', 'admin', '2019-12-28 12:38:01', 'admin', '2019-12-28 12:38:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660461906397630464, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '10363', 'admin', '2019-12-28 12:39:49', 'admin', '2019-12-28 12:39:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660477467529711616, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '96', 'admin', '2019-12-28 13:41:39', 'admin', '2019-12-28 13:41:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660492758758330368, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2019-12-28 14:42:25', 'admin', '2019-12-28 14:42:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660506289251684352, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '324', 'admin', '2019-12-28 15:36:11', 'admin', '2019-12-28 15:36:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660506376115720192, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '311', 'admin', '2019-12-28 15:36:32', 'admin', '2019-12-28 15:36:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660506819462041600, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '329', 'admin', '2019-12-28 15:38:17', 'admin', '2019-12-28 15:38:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660509361122185216, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '92', 'admin', '2019-12-28 15:48:23', 'admin', '2019-12-28 15:48:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660509503632052224, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '322', 'admin', '2019-12-28 15:48:57', 'admin', '2019-12-28 15:48:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660509637119971328, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '323', 'admin', '2019-12-28 15:49:29', 'admin', '2019-12-28 15:49:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660559196328169472, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '89', 'admin', '2019-12-28 19:06:25', 'admin', '2019-12-28 19:06:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660560592289009664, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '366', 'admin', '2019-12-28 19:11:58', 'admin', '2019-12-28 19:11:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660560970388738048, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '366', 'admin', '2019-12-28 19:13:28', 'admin', '2019-12-28 19:13:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660565434583617536, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '328', 'admin', '2019-12-28 19:31:12', 'admin', '2019-12-28 19:31:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660565831775817728, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '326', 'admin', '2019-12-28 19:32:47', 'admin', '2019-12-28 19:32:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660565967054704640, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '329', 'admin', '2019-12-28 19:33:19', 'admin', '2019-12-28 19:33:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660566017122111488, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '325', 'admin', '2019-12-28 19:33:31', 'admin', '2019-12-28 19:33:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660566043974045696, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '333', 'admin', '2019-12-28 19:33:38', 'admin', '2019-12-28 19:33:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660571782092296192, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '322', 'admin', '2019-12-28 19:56:26', 'admin', '2019-12-28 19:56:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660572177267036160, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '320', 'admin', '2019-12-28 19:58:00', 'admin', '2019-12-28 19:58:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660573027221770240, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '363', 'admin', '2019-12-28 20:01:23', 'admin', '2019-12-28 20:01:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660573273104453632, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '365', 'admin', '2019-12-28 20:02:21', 'admin', '2019-12-28 20:02:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660573622498365440, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '363', 'admin', '2019-12-28 20:03:44', 'admin', '2019-12-28 20:03:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660573713636397056, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '367', 'admin', '2019-12-28 20:04:06', 'admin', '2019-12-28 20:04:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660574754041892864, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '97', 'admin', '2019-12-28 20:08:14', 'admin', '2019-12-28 20:08:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660575331027128320, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '364', 'admin', '2019-12-28 20:10:32', 'admin', '2019-12-28 20:10:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660577160976797696, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '402', 'admin', '2019-12-28 20:17:48', 'admin', '2019-12-28 20:17:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660577222675009536, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '349', 'admin', '2019-12-28 20:18:03', 'admin', '2019-12-28 20:18:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660577396923174912, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '379', 'admin', '2019-12-28 20:18:44', 'admin', '2019-12-28 20:18:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660578184831569920, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '369', 'admin', '2019-12-28 20:21:52', 'admin', '2019-12-28 20:21:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660578214917312512, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '343', 'admin', '2019-12-28 20:21:59', 'admin', '2019-12-28 20:21:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660579586907705344, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '370', 'admin', '2019-12-28 20:27:26', 'admin', '2019-12-28 20:27:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660579691270377472, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '377', 'admin', '2019-12-28 20:27:51', 'admin', '2019-12-28 20:27:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660579774430842880, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '337', 'admin', '2019-12-28 20:28:11', 'admin', '2019-12-28 20:28:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660580589916786688, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '325', 'admin', '2019-12-28 20:31:26', 'admin', '2019-12-28 20:31:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660580604043202560, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '325', 'admin', '2019-12-28 20:31:29', 'admin', '2019-12-28 20:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660581138317840384, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '322', 'admin', '2019-12-28 20:33:36', 'admin', '2019-12-28 20:33:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660581951043932160, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '333', 'admin', '2019-12-28 20:36:50', 'admin', '2019-12-28 20:36:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660593098090483712, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '91', 'admin', '2019-12-28 21:21:08', 'admin', '2019-12-28 21:21:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660933938541367296, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '239', 'admin', '2019-12-29 19:55:31', 'admin', '2019-12-29 19:55:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660934131760369664, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '357', 'admin', '2019-12-29 19:56:17', 'admin', '2019-12-29 19:56:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660937118692020224, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '448', 'admin', '2019-12-29 20:08:09', 'admin', '2019-12-29 20:08:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660941852110884864, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '365', 'admin', '2019-12-29 20:26:57', 'admin', '2019-12-29 20:26:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660942636571561984, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '504', 'admin', '2019-12-29 20:30:04', 'admin', '2019-12-29 20:30:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (660944354545897472, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '866', 'admin', '2019-12-29 20:36:54', 'admin', '2019-12-29 20:36:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661319970420035584, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '492', 'admin', '2019-12-30 21:29:28', 'admin', '2019-12-30 21:29:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661321739879452672, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '401', 'admin', '2019-12-30 21:36:30', 'admin', '2019-12-30 21:36:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661321773568102400, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '355', 'admin', '2019-12-30 21:36:38', 'admin', '2019-12-30 21:36:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661321951138156544, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '351', 'admin', '2019-12-30 21:37:20', 'admin', '2019-12-30 21:37:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661322976674844672, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '39198', 'admin', '2019-12-30 21:41:24', 'admin', '2019-12-30 21:41:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661323475549556736, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '12799', 'admin', '2019-12-30 21:43:23', 'admin', '2019-12-30 21:43:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661324565586251776, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '363', 'admin', '2019-12-30 21:47:43', 'admin', '2019-12-30 21:47:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661324764954103808, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '559', 'admin', '2019-12-30 21:48:31', 'admin', '2019-12-30 21:48:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661325071645806592, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '381', 'admin', '2019-12-30 21:49:44', 'admin', '2019-12-30 21:49:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661325370578046976, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '14460', 'admin', '2019-12-30 21:50:55', 'admin', '2019-12-30 21:50:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661325878122385408, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '17548', 'admin', '2019-12-30 21:52:56', 'admin', '2019-12-30 21:52:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661327944987316224, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '465', 'admin', '2019-12-30 22:01:09', 'admin', '2019-12-30 22:01:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661328614012358656, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '319', 'admin', '2019-12-30 22:03:48', 'admin', '2019-12-30 22:03:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661329095900139520, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '313', 'admin', '2019-12-30 22:05:43', 'admin', '2019-12-30 22:05:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661329107107319808, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '329', 'admin', '2019-12-30 22:05:46', 'admin', '2019-12-30 22:05:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661338978804436992, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '96', 'admin', '2019-12-30 22:45:00', 'admin', '2019-12-30 22:45:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661339693199265792, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '328', 'admin', '2019-12-30 22:47:50', 'admin', '2019-12-30 22:47:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661339723993845760, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '361', 'admin', '2019-12-30 22:47:57', 'admin', '2019-12-30 22:47:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661339743920984064, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '331', 'admin', '2019-12-30 22:48:02', 'admin', '2019-12-30 22:48:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340381656518656, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '346', 'admin', '2019-12-30 22:50:34', 'admin', '2019-12-30 22:50:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340682820128768, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/661327943049547776', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '393', 'admin', '2019-12-30 22:51:46', 'admin', '2019-12-30 22:51:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340735722885120, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/661325804524933120', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '402', 'admin', '2019-12-30 22:51:59', 'admin', '2019-12-30 22:51:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340746347057152, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/661325309932605440', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '387', 'admin', '2019-12-30 22:52:01', 'admin', '2019-12-30 22:52:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340757289996288, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/661324762609487872', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '399', 'admin', '2019-12-30 22:52:04', 'admin', '2019-12-30 22:52:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340767805116416, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/661323421891825664', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '393', 'admin', '2019-12-30 22:52:06', 'admin', '2019-12-30 22:52:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340778186018816, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/636313717977518080', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '386', 'admin', '2019-12-30 22:52:09', 'admin', '2019-12-30 22:52:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340788554338304, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/636313694325837824', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '395', 'admin', '2019-12-30 22:52:11', 'admin', '2019-12-30 22:52:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340798931046400, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/636313668962881536', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '392', 'admin', '2019-12-30 22:52:14', 'admin', '2019-12-30 22:52:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340813900517376, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/636313644250042368', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '396', 'admin', '2019-12-30 22:52:17', 'admin', '2019-12-30 22:52:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340828702216192, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/636313644250042368', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '58', 'admin', '2019-12-30 22:52:21', 'admin', '2019-12-30 22:52:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661340868875259904, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/636313644250042368', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '68', 'admin', '2019-12-30 22:52:30', 'admin', '2019-12-30 22:52:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661917602926432256, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '700', 'admin', '2020-01-01 13:04:14', 'admin', '2020-01-01 13:04:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661917754051399680, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '601', 'admin', '2020-01-01 13:04:50', 'admin', '2020-01-01 13:04:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661918376720994304, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '334', 'admin', '2020-01-01 13:07:19', 'admin', '2020-01-01 13:07:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661918398753673216, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '340', 'admin', '2020-01-01 13:07:24', 'admin', '2020-01-01 13:07:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (661924788540936192, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '91', 'admin', '2020-01-01 13:32:48', 'admin', '2020-01-01 13:32:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662038160926183424, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '94', 'admin', '2020-01-01 21:03:18', 'admin', '2020-01-01 21:03:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662039900421492736, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '450', 'admin', '2020-01-01 21:10:12', 'admin', '2020-01-01 21:10:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662041453924585472, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '456', 'admin', '2020-01-01 21:16:23', 'admin', '2020-01-01 21:16:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662042921880653824, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '437', 'admin', '2020-01-01 21:22:13', 'admin', '2020-01-01 21:22:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662043700171509760, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '442', 'admin', '2020-01-01 21:25:18', 'admin', '2020-01-01 21:25:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662043857046867968, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '263', 'admin', '2020-01-01 21:25:56', 'admin', '2020-01-01 21:25:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662057081750294528, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '91', 'admin', '2020-01-01 22:18:29', 'admin', '2020-01-01 22:18:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662057241821712384, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '462', 'admin', '2020-01-01 22:19:07', 'admin', '2020-01-01 22:19:07', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662057809009053696, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '482', 'admin', '2020-01-01 22:21:22', 'admin', '2020-01-01 22:21:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662057947056181248, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '468', 'admin', '2020-01-01 22:21:55', 'admin', '2020-01-01 22:21:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662058042560483328, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '323', 'admin', '2020-01-01 22:22:18', 'admin', '2020-01-01 22:22:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662058218373124096, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '350', 'admin', '2020-01-01 22:23:00', 'admin', '2020-01-01 22:23:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662402966577352704, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '531', 'admin', '2020-01-02 21:12:54', 'admin', '2020-01-02 21:12:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662418008781754368, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '437', 'admin', '2020-01-02 22:12:40', 'admin', '2020-01-02 22:12:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662418044785659904, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '352', 'admin', '2020-01-02 22:12:49', 'admin', '2020-01-02 22:12:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662425008487927808, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '95', 'admin', '2020-01-02 22:40:29', 'admin', '2020-01-02 22:40:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662425086753640448, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '309', 'admin', '2020-01-02 22:40:48', 'admin', '2020-01-02 22:40:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662425308321943552, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '312', 'admin', '2020-01-02 22:41:41', 'admin', '2020-01-02 22:41:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (662775807533518848, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '252', 'admin', '2020-01-03 21:54:26', 'admin', '2020-01-03 21:54:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663862714644434944, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '475', 'admin', '2020-01-06 21:53:25', 'admin', '2020-01-06 21:53:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663862816331141120, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '410', 'admin', '2020-01-06 21:53:49', 'admin', '2020-01-06 21:53:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663862830965067776, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '339', 'admin', '2020-01-06 21:53:53', 'admin', '2020-01-06 21:53:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663863936013176832, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '344', 'admin', '2020-01-06 21:58:16', 'admin', '2020-01-06 21:58:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663863982960021504, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '277', 'admin', '2020-01-06 21:58:28', 'admin', '2020-01-06 21:58:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663864031513284608, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662057945093246976', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '398', 'admin', '2020-01-06 21:58:39', 'admin', '2020-01-06 21:58:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663864042686910464, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662057239883943936', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '404', 'admin', '2020-01-06 21:58:42', 'admin', '2020-01-06 21:58:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663864056821714944, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662043855947960320', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '398', 'admin', '2020-01-06 21:58:45', 'admin', '2020-01-06 21:58:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663864077973590016, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/661917751564177408', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '401', 'admin', '2020-01-06 21:58:50', 'admin', '2020-01-06 21:58:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663864090120294400, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662039898534055936', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '400', 'admin', '2020-01-06 21:58:53', 'admin', '2020-01-06 21:58:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663864110110347264, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662041452016177152', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '401', 'admin', '2020-01-06 21:58:58', 'admin', '2020-01-06 21:58:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663871426113179648, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '274', 'admin', '2020-01-06 22:28:02', 'admin', '2020-01-06 22:28:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663871636621103104, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '272', 'admin', '2020-01-06 22:28:52', 'admin', '2020-01-06 22:28:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663873137234022400, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662042920043548672', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '390', 'admin', '2020-01-06 22:34:50', 'admin', '2020-01-06 22:34:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663873152446763008, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662043698313433088', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '396', 'admin', '2020-01-06 22:34:54', 'admin', '2020-01-06 22:34:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663873163012214784, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/662057806991593472', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '399', 'admin', '2020-01-06 22:34:56', 'admin', '2020-01-06 22:34:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663873173619609600, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/663863981798199296', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '394', 'admin', '2020-01-06 22:34:59', 'admin', '2020-01-06 22:34:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663873196239491072, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/663863981798199296', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '36', 'admin', '2020-01-06 22:35:04', 'admin', '2020-01-06 22:35:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (663873640240123904, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '5294', 'admin', '2020-01-06 22:36:50', 'admin', '2020-01-06 22:36:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667856412134739968, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '550', 'admin', '2020-01-17 22:22:57', 'admin', '2020-01-17 22:22:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667856724849463296, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '352', 'admin', '2020-01-17 22:24:11', 'admin', '2020-01-17 22:24:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667857068232937472, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '271', 'admin', '2020-01-17 22:25:33', 'admin', '2020-01-17 22:25:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859234200883200, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '454', 'admin', '2020-01-17 22:34:10', 'admin', '2020-01-17 22:34:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859334197284864, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '332', 'admin', '2020-01-17 22:34:33', 'admin', '2020-01-17 22:34:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859369790148608, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '334', 'admin', '2020-01-17 22:34:42', 'admin', '2020-01-17 22:34:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859433178664960, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '338', 'admin', '2020-01-17 22:34:57', 'admin', '2020-01-17 22:34:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859462316494848, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '326', 'admin', '2020-01-17 22:35:04', 'admin', '2020-01-17 22:35:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859553286754304, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '336', 'admin', '2020-01-17 22:35:26', 'admin', '2020-01-17 22:35:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667859877267378176, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '381', 'admin', '2020-01-17 22:36:43', 'admin', '2020-01-17 22:36:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667862556626522112, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '55390', 'admin', '2020-01-17 22:47:22', 'admin', '2020-01-17 22:47:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667866795268509696, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '396', 'admin', '2020-01-17 23:04:12', 'admin', '2020-01-17 23:04:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667866832195162112, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '352', 'admin', '2020-01-17 23:04:21', 'admin', '2020-01-17 23:04:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667866874809290752, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '348', 'admin', '2020-01-17 23:04:31', 'admin', '2020-01-17 23:04:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667866908925759488, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '349', 'admin', '2020-01-17 23:04:39', 'admin', '2020-01-17 23:04:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667871567493074944, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '104', 'admin', '2020-01-17 23:23:10', 'admin', '2020-01-17 23:23:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667872532870860800, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '374', 'admin', '2020-01-17 23:27:00', 'admin', '2020-01-17 23:27:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667872748206428160, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '4102', 'admin', '2020-01-17 23:27:52', 'admin', '2020-01-17 23:27:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667873162863710208, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '236', 'admin', '2020-01-17 23:29:31', 'admin', '2020-01-17 23:29:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (667873174154776576, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '203', 'admin', '2020-01-17 23:29:33', 'admin', '2020-01-17 23:29:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668086557776941056, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '162', 'admin', '2020-01-18 13:37:28', 'admin', '2020-01-18 13:37:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668092840198410240, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '3639', 'admin', '2020-01-18 14:02:26', 'admin', '2020-01-18 14:02:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668093043513102336, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '414', 'admin', '2020-01-18 14:03:14', 'admin', '2020-01-18 14:03:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668094076389822464, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '315', 'admin', '2020-01-18 14:07:20', 'admin', '2020-01-18 14:07:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668096975199145984, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', 'examinationId=%5B636313595680002048%5D', NULL, 'web_app', '4542', 'admin', '2020-01-18 14:18:52', 'admin', '2020-01-18 14:18:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668098680007561216, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '232', 'admin', '2020-01-18 14:25:38', 'admin', '2020-01-18 14:25:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668098713608130560, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '173', 'admin', '2020-01-18 14:25:46', 'admin', '2020-01-18 14:25:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668099927557148672, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '1902', 'admin', '2020-01-18 14:30:35', 'admin', '2020-01-18 14:30:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668101835499900928, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '90', 'admin', '2020-01-18 14:38:10', 'admin', '2020-01-18 14:38:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668101950780346368, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '204', 'admin', '2020-01-18 14:38:38', 'admin', '2020-01-18 14:38:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668102603997057024, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '211', 'admin', '2020-01-18 14:41:14', 'admin', '2020-01-18 14:41:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668109234659004416, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '168', 'admin', '2020-01-18 15:07:34', 'admin', '2020-01-18 15:07:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668110306391756800, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '215055', 'admin', '2020-01-18 15:11:50', 'admin', '2020-01-18 15:11:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668111931781025792, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '1158', 'admin', '2020-01-18 15:18:17', 'admin', '2020-01-18 15:18:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668112990104588288, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '13195', 'admin', '2020-01-18 15:22:30', 'admin', '2020-01-18 15:22:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668113629119385600, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '1960', 'admin', '2020-01-18 15:25:02', 'admin', '2020-01-18 15:25:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668113869671108608, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '738', 'admin', '2020-01-18 15:25:59', 'admin', '2020-01-18 15:25:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668114221296390144, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '388', 'admin', '2020-01-18 15:27:23', 'admin', '2020-01-18 15:27:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668114330184716288, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '11421', 'admin', '2020-01-18 15:27:49', 'admin', '2020-01-18 15:27:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668114912261836800, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '714', 'admin', '2020-01-18 15:30:08', 'admin', '2020-01-18 15:30:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668115343193018368, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '1024', 'admin', '2020-01-18 15:31:51', 'admin', '2020-01-18 15:31:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668115836816461824, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '794', 'admin', '2020-01-18 15:33:48', 'admin', '2020-01-18 15:33:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668115906077003776, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '791', 'admin', '2020-01-18 15:34:05', 'admin', '2020-01-18 15:34:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (668581719686909952, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '553', 'admin', '2020-01-19 22:25:04', 'admin', '2020-01-19 22:25:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672478503073091584, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '886', 'admin', '2020-01-30 16:29:29', 'admin', '2020-01-30 16:29:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672481875666800640, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '790', 'admin', '2020-01-30 16:42:53', 'admin', '2020-01-30 16:42:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672482817954942976, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '713', 'admin', '2020-01-30 16:46:38', 'admin', '2020-01-30 16:46:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672484289899794432, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '530', 'admin', '2020-01-30 16:52:29', 'admin', '2020-01-30 16:52:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672484580577644544, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '663', 'admin', '2020-01-30 16:53:38', 'admin', '2020-01-30 16:53:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672484737222316032, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '595', 'admin', '2020-01-30 16:54:16', 'admin', '2020-01-30 16:54:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672485100503568384, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '564', 'admin', '2020-01-30 16:55:42', 'admin', '2020-01-30 16:55:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672485162050785280, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '584', 'admin', '2020-01-30 16:55:57', 'admin', '2020-01-30 16:55:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672485271064940544, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '476', 'admin', '2020-01-30 16:56:23', 'admin', '2020-01-30 16:56:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672485365642301440, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '489', 'admin', '2020-01-30 16:56:45', 'admin', '2020-01-30 16:56:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672493850308251648, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '125', 'admin', '2020-01-30 17:30:28', 'admin', '2020-01-30 17:30:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672546958841352192, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '115', 'admin', '2020-01-30 21:01:30', 'admin', '2020-01-30 21:01:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672739378522624000, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '457', 'admin', '2020-01-31 09:46:07', 'admin', '2020-01-31 09:46:07', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672739507900125184, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '547', 'admin', '2020-01-31 09:46:38', 'admin', '2020-01-31 09:46:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672739570961485824, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '574', 'admin', '2020-01-31 09:46:53', 'admin', '2020-01-31 09:46:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672739850104999936, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '507', 'admin', '2020-01-31 09:47:59', 'admin', '2020-01-31 09:47:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672739891670552576, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '561', 'admin', '2020-01-31 09:48:09', 'admin', '2020-01-31 09:48:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740013582192640, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '1047', 'admin', '2020-01-31 09:48:38', 'admin', '2020-01-31 09:48:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740081181790208, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '575', 'admin', '2020-01-31 09:48:54', 'admin', '2020-01-31 09:48:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740115587665920, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '457', 'admin', '2020-01-31 09:49:02', 'admin', '2020-01-31 09:49:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740127851810816, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '457', 'admin', '2020-01-31 09:49:05', 'admin', '2020-01-31 09:49:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740191231938560, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '552', 'admin', '2020-01-31 09:49:20', 'admin', '2020-01-31 09:49:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740232994623488, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '487', 'admin', '2020-01-31 09:49:30', 'admin', '2020-01-31 09:49:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740255337680896, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '495', 'admin', '2020-01-31 09:49:36', 'admin', '2020-01-31 09:49:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740378868322304, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '460', 'admin', '2020-01-31 09:50:05', 'admin', '2020-01-31 09:50:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672740449651396608, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '510', 'admin', '2020-01-31 09:50:22', 'admin', '2020-01-31 09:50:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672741764427288576, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '568', 'admin', '2020-01-31 09:55:36', 'admin', '2020-01-31 09:55:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672741898280112128, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '476', 'admin', '2020-01-31 09:56:07', 'admin', '2020-01-31 09:56:07', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672742766501040128, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '533', 'admin', '2020-01-31 09:59:34', 'admin', '2020-01-31 09:59:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672743002749407232, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '457', 'admin', '2020-01-31 10:00:31', 'admin', '2020-01-31 10:00:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672743136275075072, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '522', 'admin', '2020-01-31 10:01:03', 'admin', '2020-01-31 10:01:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672744139930079232, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '697', 'admin', '2020-01-31 10:05:02', 'admin', '2020-01-31 10:05:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672744210071425024, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '464', 'admin', '2020-01-31 10:05:19', 'admin', '2020-01-31 10:05:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672745631076454400, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '314', 'admin', '2020-01-31 10:10:57', 'admin', '2020-01-31 10:10:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672745703960875008, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '729', 'admin', '2020-01-31 10:11:15', 'admin', '2020-01-31 10:11:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672746417181298688, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '487', 'admin', '2020-01-31 10:14:05', 'admin', '2020-01-31 10:14:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672746491122683904, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '395', 'admin', '2020-01-31 10:14:23', 'admin', '2020-01-31 10:14:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672746680675864576, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '496', 'admin', '2020-01-31 10:15:08', 'admin', '2020-01-31 10:15:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672746751823843328, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '395', 'admin', '2020-01-31 10:15:25', 'admin', '2020-01-31 10:15:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748290512654336, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '489', 'admin', '2020-01-31 10:21:32', 'admin', '2020-01-31 10:21:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748345596448768, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '566', 'admin', '2020-01-31 10:21:45', 'admin', '2020-01-31 10:21:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748388499984384, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '486', 'admin', '2020-01-31 10:21:55', 'admin', '2020-01-31 10:21:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748425112064000, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '401', 'admin', '2020-01-31 10:22:04', 'admin', '2020-01-31 10:22:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748445529935872, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '316', 'admin', '2020-01-31 10:22:08', 'admin', '2020-01-31 10:22:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748494762676224, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '405', 'admin', '2020-01-31 10:22:20', 'admin', '2020-01-31 10:22:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672748697062346752, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '326', 'admin', '2020-01-31 10:23:08', 'admin', '2020-01-31 10:23:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672749274848694272, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '574', 'admin', '2020-01-31 10:25:26', 'admin', '2020-01-31 10:25:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672749316019982336, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '454', 'admin', '2020-01-31 10:25:36', 'admin', '2020-01-31 10:25:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672749348307734528, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '402', 'admin', '2020-01-31 10:25:44', 'admin', '2020-01-31 10:25:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672749420982439936, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '582', 'admin', '2020-01-31 10:26:01', 'admin', '2020-01-31 10:26:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672749484056383488, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '666', 'admin', '2020-01-31 10:26:16', 'admin', '2020-01-31 10:26:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672752378134532096, 0, '删除考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/636313595680002048', 'DELETE', '', NULL, 'web_app', '11245', 'admin', '2020-01-31 10:37:46', 'admin', '2020-01-31 10:37:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672753152231084032, 0, '新增考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'POST', '', NULL, 'web_app', '331', 'admin', '2020-01-31 10:40:51', 'admin', '2020-01-31 10:40:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672753203464507392, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '325', 'admin', '2020-01-31 10:41:03', 'admin', '2020-01-31 10:41:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672753541571547136, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '269', 'admin', '2020-01-31 10:42:23', 'admin', '2020-01-31 10:42:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672753548248879104, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '311', 'admin', '2020-01-31 10:42:25', 'admin', '2020-01-31 10:42:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672754217475248128, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '321', 'admin', '2020-01-31 10:45:05', 'admin', '2020-01-31 10:45:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672754229919748096, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '316', 'admin', '2020-01-31 10:45:08', 'admin', '2020-01-31 10:45:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672754595335901184, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '131', 'admin', '2020-01-31 10:46:35', 'admin', '2020-01-31 10:46:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672754748113424384, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '616', 'admin', '2020-01-31 10:47:11', 'admin', '2020-01-31 10:47:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672754935842082816, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '816', 'admin', '2020-01-31 10:47:56', 'admin', '2020-01-31 10:47:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672755128897507328, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '591', 'admin', '2020-01-31 10:48:42', 'admin', '2020-01-31 10:48:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672755247072022528, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '475', 'admin', '2020-01-31 10:49:10', 'admin', '2020-01-31 10:49:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672755356161675264, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '501', 'admin', '2020-01-31 10:49:36', 'admin', '2020-01-31 10:49:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672755542107754496, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '578', 'admin', '2020-01-31 10:50:20', 'admin', '2020-01-31 10:50:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672759388284522496, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '464', 'admin', '2020-01-31 11:05:37', 'admin', '2020-01-31 11:05:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672759398879334400, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '310', 'admin', '2020-01-31 11:05:40', 'admin', '2020-01-31 11:05:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672762876926234624, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', 'examinationId=%5B672753150888906752%5D', NULL, 'web_app', '8534', 'admin', '2020-01-31 11:19:29', 'admin', '2020-01-31 11:19:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672764406123335680, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', 'examinationId=%5B672753150888906752%5D', NULL, 'web_app', '503', 'admin', '2020-01-31 11:25:34', 'admin', '2020-01-31 11:25:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672764683865952256, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', 'examinationId=%5B672753150888906752%5D', NULL, 'web_app', '30241', 'admin', '2020-01-31 11:26:40', 'admin', '2020-01-31 11:26:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672765113987633152, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', 'examinationId=%5B672753150888906752%5D', NULL, 'web_app', '585', 'admin', '2020-01-31 11:28:23', 'admin', '2020-01-31 11:28:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672765508168323072, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '254', 'admin', '2020-01-31 11:29:57', 'admin', '2020-01-31 11:29:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672765659033243648, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '1904', 'admin', '2020-01-31 11:30:32', 'admin', '2020-01-31 11:30:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672768296533233664, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '2170', 'admin', '2020-01-31 11:41:01', 'admin', '2020-01-31 11:41:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672768434521640960, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/672768293026795520', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '1563', 'admin', '2020-01-31 11:41:34', 'admin', '2020-01-31 11:41:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672768451160444928, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/672765654029438976', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '650', 'admin', '2020-01-31 11:41:38', 'admin', '2020-01-31 11:41:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672768829302116352, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/import', 'POST', 'busiType=%5B1%5D', NULL, 'web_app', '3357', 'admin', '2020-01-31 11:43:08', 'admin', '2020-01-31 11:43:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672769161683931136, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/672754745533927424', 'DELETE', 'type=%5B0%5D', NULL, 'web_app', '589', 'admin', '2020-01-31 11:44:28', 'admin', '2020-01-31 11:44:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672808371124047872, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '177', 'admin', '2020-01-31 14:20:16', 'admin', '2020-01-31 14:20:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672815801455218688, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '652', 'admin', '2020-01-31 14:49:47', 'admin', '2020-01-31 14:49:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672817388319805440, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '612', 'admin', '2020-01-31 14:56:06', 'admin', '2020-01-31 14:56:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672818582375239680, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '671', 'admin', '2020-01-31 15:00:50', 'admin', '2020-01-31 15:00:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672819274540257280, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '454', 'admin', '2020-01-31 15:03:35', 'admin', '2020-01-31 15:03:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672819287752314880, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '461', 'admin', '2020-01-31 15:03:39', 'admin', '2020-01-31 15:03:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672819335739346944, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '625', 'admin', '2020-01-31 15:03:50', 'admin', '2020-01-31 15:03:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672822704411381760, 0, '批量删除题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/deleteAll', 'POST', '', NULL, 'web_app', '610', 'admin', '2020-01-31 15:17:13', 'admin', '2020-01-31 15:17:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672822793045413888, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '7308', 'admin', '2020-01-31 15:17:34', 'admin', '2020-01-31 15:17:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672824372133761024, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '122', 'admin', '2020-01-31 15:23:51', 'admin', '2020-01-31 15:23:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672824431567048704, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', '', NULL, 'web_app', '261', 'admin', '2020-01-31 15:24:05', 'admin', '2020-01-31 15:24:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672824504350806016, 0, '导入题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/import', 'POST', 'categoryId=%5B602231546270846976%5D', NULL, 'web_app', '1182', 'admin', '2020-01-31 15:24:22', 'admin', '2020-01-31 15:24:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672827102436921344, 0, '导出题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/export', 'POST', 'categoryId=%5B602231546270846976%5D', NULL, 'web_app', '582', 'admin', '2020-01-31 15:34:42', 'admin', '2020-01-31 15:34:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672839815108104192, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '136', 'admin', '2020-01-31 16:25:13', 'admin', '2020-01-31 16:25:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672847433008549888, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '305', 'admin', '2020-01-31 16:55:29', 'admin', '2020-01-31 16:55:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672867549750366208, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '294', 'admin', '2020-01-31 18:15:25', 'admin', '2020-01-31 18:15:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672867920665251840, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '124', 'admin', '2020-01-31 18:16:54', 'admin', '2020-01-31 18:16:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (672913621969408000, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '202', 'admin', '2020-01-31 21:18:30', 'admin', '2020-01-31 21:18:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673488120884367360, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '620', 'admin', '2020-02-02 11:21:21', 'admin', '2020-02-02 11:21:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673509353743060992, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '144', 'admin', '2020-02-02 12:45:43', 'admin', '2020-02-02 12:45:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673510875512049664, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '108', 'admin', '2020-02-02 12:51:46', 'admin', '2020-02-02 12:51:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673541963944955904, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '165', 'admin', '2020-02-02 14:55:18', 'admin', '2020-02-02 14:55:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673557934327861248, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '197', 'admin', '2020-02-02 15:58:46', 'admin', '2020-02-02 15:58:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673638603758374912, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '111', 'admin', '2020-02-02 21:19:19', 'admin', '2020-02-02 21:19:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673839325611429888, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '255', 'admin', '2020-02-03 10:36:55', 'admin', '2020-02-03 10:36:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673854773333725184, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '723', 'admin', '2020-02-03 11:38:18', 'admin', '2020-02-03 11:38:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673902488172236800, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '225', 'admin', '2020-02-03 14:47:54', 'admin', '2020-02-03 14:47:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673905890054770688, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '127', 'admin', '2020-02-03 15:01:25', 'admin', '2020-02-03 15:01:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673922750301212672, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '99', 'admin', '2020-02-03 16:08:25', 'admin', '2020-02-03 16:08:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673935539287756800, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '122', 'admin', '2020-02-03 16:59:14', 'admin', '2020-02-03 16:59:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673937946876973056, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '128', 'admin', '2020-02-03 17:08:48', 'admin', '2020-02-03 17:08:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (673977664217944064, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '243', 'admin', '2020-02-03 19:46:37', 'admin', '2020-02-03 19:46:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674002981187883008, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '122', 'admin', '2020-02-03 21:27:13', 'admin', '2020-02-03 21:27:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674009480161267712, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '696', 'admin', '2020-02-03 21:53:03', 'admin', '2020-02-03 21:53:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674012195721449472, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '514', 'admin', '2020-02-03 22:03:50', 'admin', '2020-02-03 22:03:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674012221902295040, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '450', 'admin', '2020-02-03 22:03:56', 'admin', '2020-02-03 22:03:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674018152975306752, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '113', 'admin', '2020-02-03 22:27:30', 'admin', '2020-02-03 22:27:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674188659615993856, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '250', 'admin', '2020-02-04 09:45:02', 'admin', '2020-02-04 09:45:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674194296110452736, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '375', 'admin', '2020-02-04 10:07:26', 'admin', '2020-02-04 10:07:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674194304662638592, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '287', 'admin', '2020-02-04 10:07:28', 'admin', '2020-02-04 10:07:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674202322703880192, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '465', 'admin', '2020-02-04 10:39:20', 'admin', '2020-02-04 10:39:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674202632365150208, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '411', 'admin', '2020-02-04 10:40:34', 'admin', '2020-02-04 10:40:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674204141073076224, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '109', 'admin', '2020-02-04 10:46:33', 'admin', '2020-02-04 10:46:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674209687842983936, 0, '更新用户基本信息', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/updateInfo', 'PUT', '', NULL, 'web_app', '304', 'admin', '2020-02-04 11:08:36', 'admin', '2020-02-04 11:08:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674215529275330560, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '304', 'admin', '2020-02-04 11:31:48', 'admin', '2020-02-04 11:31:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674217950823190528, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '286', 'admin', '2020-02-04 11:41:26', 'admin', '2020-02-04 11:41:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674217978576900096, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '299', 'admin', '2020-02-04 11:41:32', 'admin', '2020-02-04 11:41:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674227046334992384, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '654', 'admin', '2020-02-04 12:17:34', 'admin', '2020-02-04 12:17:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674228379561627648, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '515', 'admin', '2020-02-04 12:22:52', 'admin', '2020-02-04 12:22:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674228773209640960, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '270', 'admin', '2020-02-04 12:24:26', 'admin', '2020-02-04 12:24:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674228787042455552, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '308', 'admin', '2020-02-04 12:24:29', 'admin', '2020-02-04 12:24:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674309912905519104, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '361', 'admin', '2020-02-04 17:46:51', 'admin', '2020-02-04 17:46:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674364405932232704, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '173', 'admin', '2020-02-04 21:23:23', 'admin', '2020-02-04 21:23:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674549086346678272, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '311', 'admin', '2020-02-05 09:37:15', 'admin', '2020-02-05 09:37:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674552405848887296, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '2610', 'admin', '2020-02-05 09:50:26', 'admin', '2020-02-05 09:50:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674554340786180096, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '360', 'admin', '2020-02-05 09:58:07', 'admin', '2020-02-05 09:58:07', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674554582290010112, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '110', 'admin', '2020-02-05 09:59:05', 'admin', '2020-02-05 09:59:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674555280427716608, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '281', 'admin', '2020-02-05 10:01:51', 'admin', '2020-02-05 10:01:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674555885938413568, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '266', 'admin', '2020-02-05 10:04:16', 'admin', '2020-02-05 10:04:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674556249832034304, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '358', 'admin', '2020-02-05 10:05:43', 'admin', '2020-02-05 10:05:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674557827582398464, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '358', 'admin', '2020-02-05 10:11:59', 'admin', '2020-02-05 10:11:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674558257364340736, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '336', 'admin', '2020-02-05 10:13:41', 'admin', '2020-02-05 10:13:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (674559464828964864, 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/647192514083819520', 'PUT', '', NULL, 'web_app', '477', 'admin', '2020-02-05 10:18:29', 'admin', '2020-02-05 10:18:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675288081817014272, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '344', 'admin', '2020-02-07 10:33:45', 'admin', '2020-02-07 10:33:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675288090306285568, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '280', 'admin', '2020-02-07 10:33:47', 'admin', '2020-02-07 10:33:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675295384683286528, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '245', 'admin', '2020-02-07 11:02:46', 'admin', '2020-02-07 11:02:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675295715433517056, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2020-02-07 11:04:05', 'admin', '2020-02-07 11:04:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675298190462947328, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '297', 'admin', '2020-02-07 11:13:55', 'admin', '2020-02-07 11:13:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675304280688824320, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '139', 'admin', '2020-02-07 11:38:07', 'admin', '2020-02-07 11:38:07', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675304308341870592, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '336', 'admin', '2020-02-07 11:38:14', 'admin', '2020-02-07 11:38:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675320973175689216, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '478', 'admin', '2020-02-07 12:44:27', 'admin', '2020-02-07 12:44:27', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675322084334899200, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '106', 'admin', '2020-02-07 12:48:52', 'admin', '2020-02-07 12:48:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675322908448526336, 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/647192514083819520', 'PUT', '', NULL, 'web_app', '736', 'admin', '2020-02-07 12:52:08', 'admin', '2020-02-07 12:52:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675322931164876800, 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/596332387600830464', 'PUT', '', NULL, 'web_app', '452', 'admin', '2020-02-07 12:52:14', 'admin', '2020-02-07 12:52:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675322968116695040, 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/596307222997372928', 'PUT', '', NULL, 'web_app', '443', 'admin', '2020-02-07 12:52:22', 'admin', '2020-02-07 12:52:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675336717905039360, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '101', 'admin', '2020-02-07 13:47:01', 'admin', '2020-02-07 13:47:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675700178694574080, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '437', 'admin', '2020-02-08 13:51:16', 'admin', '2020-02-08 13:51:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675700516436709376, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '115', 'admin', '2020-02-08 13:52:37', 'admin', '2020-02-08 13:52:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675701325685723136, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '2786', 'admin', '2020-02-08 13:55:50', 'admin', '2020-02-08 13:55:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675701326046433280, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '522', 'admin', '2020-02-08 13:55:50', 'admin', '2020-02-08 13:55:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675701609522663424, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '16594', 'admin', '2020-02-08 13:56:58', 'admin', '2020-02-08 13:56:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675739488286281728, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '134', 'admin', '2020-02-08 16:27:29', 'admin', '2020-02-08 16:27:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675739541172260864, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '653', 'admin', '2020-02-08 16:27:41', 'admin', '2020-02-08 16:27:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675756093313519616, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '317', 'admin', '2020-02-08 17:33:28', 'admin', '2020-02-08 17:33:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675757640093143040, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '761', 'admin', '2020-02-08 17:39:36', 'admin', '2020-02-08 17:39:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675757898109947904, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '570', 'admin', '2020-02-08 17:40:38', 'admin', '2020-02-08 17:40:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675760831136403456, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '605', 'admin', '2020-02-08 17:52:17', 'admin', '2020-02-08 17:52:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (675761047646375936, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '570', 'admin', '2020-02-08 17:53:09', 'admin', '2020-02-08 17:53:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (676009373667037184, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '214', 'admin', '2020-02-09 10:19:54', 'admin', '2020-02-09 10:19:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (676009429459668992, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '841', 'admin', '2020-02-09 10:20:08', 'admin', '2020-02-09 10:20:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (676025604956622848, 0, '用户登录', '192.168.1.11', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '168', 'admin', '2020-02-09 11:24:24', 'admin', '2020-02-09 11:24:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (676025640511737856, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '660', 'admin', '2020-02-09 11:24:33', 'admin', '2020-02-09 11:24:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679042235462979584, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '569', 'admin', '2020-02-17 19:11:25', 'admin', '2020-02-17 19:11:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679042361711529984, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '268', 'admin', '2020-02-17 19:11:55', 'admin', '2020-02-17 19:11:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679042638351044608, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '168', 'admin', '2020-02-17 19:13:01', 'admin', '2020-02-17 19:13:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679042708563693568, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '727', 'admin', '2020-02-17 19:13:18', 'admin', '2020-02-17 19:13:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679059241952219136, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '127', 'admin', '2020-02-17 20:19:00', 'admin', '2020-02-17 20:19:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679059278551715840, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '497', 'admin', '2020-02-17 20:19:08', 'admin', '2020-02-17 20:19:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679059627551363072, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '398', 'admin', '2020-02-17 20:20:32', 'admin', '2020-02-17 20:20:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679061846082326528, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '201', 'admin', '2020-02-17 20:29:20', 'admin', '2020-02-17 20:29:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679077774165676032, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '133', 'admin', '2020-02-17 21:32:38', 'admin', '2020-02-17 21:32:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679077808416362496, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '556', 'admin', '2020-02-17 21:32:46', 'admin', '2020-02-17 21:32:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679094741329121280, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '259', 'admin', '2020-02-17 22:40:03', 'admin', '2020-02-17 22:40:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679094810187010048, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '556', 'admin', '2020-02-17 22:40:20', 'admin', '2020-02-17 22:40:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679309861774823424, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '199', 'admin', '2020-02-18 12:54:52', 'admin', '2020-02-18 12:54:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679309943744106496, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '1017', 'admin', '2020-02-18 12:55:12', 'admin', '2020-02-18 12:55:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679325035437101056, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '129', 'admin', '2020-02-18 13:55:10', 'admin', '2020-02-18 13:55:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679325089044500480, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '785', 'admin', '2020-02-18 13:55:22', 'admin', '2020-02-18 13:55:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679439682379059200, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '419', 'admin', '2020-02-18 21:30:44', 'admin', '2020-02-18 21:30:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679439746472218624, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '1244', 'admin', '2020-02-18 21:30:59', 'admin', '2020-02-18 21:30:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679442928942321664, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '1035', 'admin', '2020-02-18 21:43:38', 'admin', '2020-02-18 21:43:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679443890381656064, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '381', 'admin', '2020-02-18 21:47:27', 'admin', '2020-02-18 21:47:27', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679444074893283328, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '141', 'admin', '2020-02-18 21:48:11', 'admin', '2020-02-18 21:48:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679444163745419264, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/672755245079728128', 'DELETE', 'type=%5B1%5D', NULL, 'web_app', '328', 'admin', '2020-02-18 21:48:32', 'admin', '2020-02-18 21:48:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679444185283170304, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/672755126418673664', 'DELETE', 'type=%5B3%5D', NULL, 'web_app', '538', 'admin', '2020-02-18 21:48:37', 'admin', '2020-02-18 21:48:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679446261979549696, 0, '删除考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/672753150888906752', 'DELETE', '', NULL, 'web_app', '1337', 'admin', '2020-02-18 21:56:52', 'admin', '2020-02-18 21:56:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679446627840299008, 0, '新增考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'POST', '', NULL, 'web_app', '307', 'admin', '2020-02-18 21:58:20', 'admin', '2020-02-18 21:58:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679446649457741824, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '239', 'admin', '2020-02-18 21:58:25', 'admin', '2020-02-18 21:58:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679454973460877312, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '128', 'admin', '2020-02-18 22:31:29', 'admin', '2020-02-18 22:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455144315850752, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '511', 'admin', '2020-02-18 22:32:10', 'admin', '2020-02-18 22:32:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455225177837568, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '484', 'admin', '2020-02-18 22:32:29', 'admin', '2020-02-18 22:32:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455355146735616, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '481', 'admin', '2020-02-18 22:33:00', 'admin', '2020-02-18 22:33:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455413007159296, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2020-02-18 22:33:14', 'admin', '2020-02-18 22:33:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455445185859584, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '491', 'admin', '2020-02-18 22:33:22', 'admin', '2020-02-18 22:33:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455648647352320, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '439', 'admin', '2020-02-18 22:34:10', 'admin', '2020-02-18 22:34:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679455800271441920, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '224', 'admin', '2020-02-18 22:34:46', 'admin', '2020-02-18 22:34:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679457943766306816, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '211', 'admin', '2020-02-18 22:43:18', 'admin', '2020-02-18 22:43:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679458054487543808, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '766', 'admin', '2020-02-18 22:43:44', 'admin', '2020-02-18 22:43:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679461555410178048, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '384', 'admin', '2020-02-18 22:57:39', 'admin', '2020-02-18 22:57:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679462555558744064, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '283', 'admin', '2020-02-18 23:01:37', 'admin', '2020-02-18 23:01:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679675606903951360, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '204', 'admin', '2020-02-19 13:08:12', 'admin', '2020-02-19 13:08:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679675726479364096, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '765', 'admin', '2020-02-19 13:08:41', 'admin', '2020-02-19 13:08:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679676009292894208, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '271', 'admin', '2020-02-19 13:09:48', 'admin', '2020-02-19 13:09:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679676317184167936, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '509', 'admin', '2020-02-19 13:11:02', 'admin', '2020-02-19 13:11:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679679543912566784, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '175', 'admin', '2020-02-19 13:23:51', 'admin', '2020-02-19 13:23:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679680051926667264, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '485', 'admin', '2020-02-19 13:25:52', 'admin', '2020-02-19 13:25:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679680689666396160, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '519', 'admin', '2020-02-19 13:28:24', 'admin', '2020-02-19 13:28:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679754701046157312, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '158', 'admin', '2020-02-19 18:22:30', 'admin', '2020-02-19 18:22:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679755204278751232, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '906', 'admin', '2020-02-19 18:24:30', 'admin', '2020-02-19 18:24:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679777691884457984, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '120', 'admin', '2020-02-19 19:53:51', 'admin', '2020-02-19 19:53:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679777739103932416, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '488', 'admin', '2020-02-19 19:54:03', 'admin', '2020-02-19 19:54:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679777886600826880, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '316', 'admin', '2020-02-19 19:54:38', 'admin', '2020-02-19 19:54:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679780391581782016, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '294', 'admin', '2020-02-19 20:04:35', 'admin', '2020-02-19 20:04:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679794337344786432, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '106', 'admin', '2020-02-19 21:00:00', 'admin', '2020-02-19 21:00:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679809490681466880, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '279', 'admin', '2020-02-19 22:00:13', 'admin', '2020-02-19 22:00:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679810728709984256, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '567', 'admin', '2020-02-19 22:05:08', 'admin', '2020-02-19 22:05:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679811376839004160, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '120', 'admin', '2020-02-19 22:07:43', 'admin', '2020-02-19 22:07:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679811461056434176, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '439', 'admin', '2020-02-19 22:08:03', 'admin', '2020-02-19 22:08:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679811656016072704, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '256', 'admin', '2020-02-19 22:08:49', 'admin', '2020-02-19 22:08:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (679819271290884096, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '285', 'admin', '2020-02-19 22:39:05', 'admin', '2020-02-19 22:39:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680021812834340864, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '222', 'admin', '2020-02-20 12:03:54', 'admin', '2020-02-20 12:03:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680022028278960128, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '110', 'admin', '2020-02-20 12:04:46', 'admin', '2020-02-20 12:04:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680022196067897344, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '416', 'admin', '2020-02-20 12:05:26', 'admin', '2020-02-20 12:05:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680022234257035264, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '965', 'admin', '2020-02-20 12:05:35', 'admin', '2020-02-20 12:05:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680022321955737600, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '406', 'admin', '2020-02-20 12:05:56', 'admin', '2020-02-20 12:05:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680036947246321664, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '128', 'admin', '2020-02-20 13:04:03', 'admin', '2020-02-20 13:04:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680037356098686976, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '100', 'admin', '2020-02-20 13:05:40', 'admin', '2020-02-20 13:05:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680037910229159936, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '849', 'admin', '2020-02-20 13:07:52', 'admin', '2020-02-20 13:07:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680038307110981632, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '844', 'admin', '2020-02-20 13:09:27', 'admin', '2020-02-20 13:09:27', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680041825792299008, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '532', 'admin', '2020-02-20 13:23:26', 'admin', '2020-02-20 13:23:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680042887018319872, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '140', 'admin', '2020-02-20 13:27:39', 'admin', '2020-02-20 13:27:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680049575075123200, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '107', 'admin', '2020-02-20 13:54:13', 'admin', '2020-02-20 13:54:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680050634199797760, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '108', 'admin', '2020-02-20 13:58:26', 'admin', '2020-02-20 13:58:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680050759659819008, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '633', 'admin', '2020-02-20 13:58:56', 'admin', '2020-02-20 13:58:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680050788583739392, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '268', 'admin', '2020-02-20 13:59:03', 'admin', '2020-02-20 13:59:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680051615511744512, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '866', 'admin', '2020-02-20 14:02:20', 'admin', '2020-02-20 14:02:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680051883699736576, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '449', 'admin', '2020-02-20 14:03:24', 'admin', '2020-02-20 14:03:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680098564352905216, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '357', 'admin', '2020-02-20 17:08:53', 'admin', '2020-02-20 17:08:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680101058952957952, 0, '重置密码', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/resetPassword', 'PUT', '', NULL, 'web_app', '940', 'admin', '2020-02-20 17:18:48', 'admin', '2020-02-20 17:18:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680101132126785536, 0, '重置密码', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/user/resetPassword', 'PUT', '', NULL, 'web_app', '869', 'admin', '2020-02-20 17:19:06', 'admin', '2020-02-20 17:19:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680113931582312448, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '189', 'admin', '2020-02-20 18:09:57', 'admin', '2020-02-20 18:09:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680121864235913216, 0, '用户登录', '10.100.145.42', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '129', 'admin', '2020-02-20 18:41:28', 'admin', '2020-02-20 18:41:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680162228061736960, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '245', 'admin', '2020-02-20 21:21:52', 'admin', '2020-02-20 21:21:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680189954776764416, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '1393', 'admin', '2020-02-20 23:12:03', 'admin', '2020-02-20 23:12:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680196143371653120, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '169', 'admin', '2020-02-20 23:36:38', 'admin', '2020-02-20 23:36:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680343084399202304, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '187', 'admin', '2020-02-21 09:20:31', 'admin', '2020-02-21 09:20:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680366921044070400, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '466', 'admin', '2020-02-21 10:55:15', 'admin', '2020-02-21 10:55:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680399546014109696, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '611', 'admin', '2020-02-21 13:04:53', 'admin', '2020-02-21 13:04:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680412767857020928, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '642', 'admin', '2020-02-21 13:57:25', 'admin', '2020-02-21 13:57:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680412953882791936, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '15874', 'admin', '2020-02-21 13:58:10', 'admin', '2020-02-21 13:58:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680413522156457984, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '1037', 'admin', '2020-02-21 14:00:25', 'admin', '2020-02-21 14:00:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680414142552739840, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '972', 'admin', '2020-02-21 14:02:53', 'admin', '2020-02-21 14:02:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680414183614976000, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '265', 'admin', '2020-02-21 14:03:03', 'admin', '2020-02-21 14:03:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680414939013320704, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '450', 'admin', '2020-02-21 14:06:03', 'admin', '2020-02-21 14:06:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680417309390671872, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '550', 'admin', '2020-02-21 14:15:28', 'admin', '2020-02-21 14:15:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680417359822983168, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '1489', 'admin', '2020-02-21 14:15:40', 'admin', '2020-02-21 14:15:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680417510478188544, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '226', 'admin', '2020-02-21 14:16:16', 'admin', '2020-02-21 14:16:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680424846160695296, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '433', 'admin', '2020-02-21 14:45:25', 'admin', '2020-02-21 14:45:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680425098762653696, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '228', 'admin', '2020-02-21 14:46:25', 'admin', '2020-02-21 14:46:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680425107029626880, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '183', 'admin', '2020-02-21 14:46:27', 'admin', '2020-02-21 14:46:27', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680425692411858944, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2020-02-21 14:48:47', 'admin', '2020-02-21 14:48:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680437765820780544, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '215', 'admin', '2020-02-21 15:36:45', 'admin', '2020-02-21 15:36:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680437774393937920, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '203', 'admin', '2020-02-21 15:36:47', 'admin', '2020-02-21 15:36:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680437825233096704, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '211', 'admin', '2020-02-21 15:36:59', 'admin', '2020-02-21 15:36:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680437864131072000, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '174', 'admin', '2020-02-21 15:37:09', 'admin', '2020-02-21 15:37:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680441971638145024, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '97', 'admin', '2020-02-21 15:53:28', 'admin', '2020-02-21 15:53:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680444722292068352, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '225', 'admin', '2020-02-21 16:04:24', 'admin', '2020-02-21 16:04:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680444758736375808, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '223', 'admin', '2020-02-21 16:04:33', 'admin', '2020-02-21 16:04:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449238836056064, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '227', 'admin', '2020-02-21 16:22:21', 'admin', '2020-02-21 16:22:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449251972616192, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '173', 'admin', '2020-02-21 16:22:24', 'admin', '2020-02-21 16:22:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449359854309376, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '205', 'admin', '2020-02-21 16:22:50', 'admin', '2020-02-21 16:22:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449366414200832, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '228', 'admin', '2020-02-21 16:22:51', 'admin', '2020-02-21 16:22:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449757335916544, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '204', 'admin', '2020-02-21 16:24:24', 'admin', '2020-02-21 16:24:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449786335334400, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '201', 'admin', '2020-02-21 16:24:31', 'admin', '2020-02-21 16:24:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449798909857792, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '204', 'admin', '2020-02-21 16:24:34', 'admin', '2020-02-21 16:24:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680449810129620992, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '208', 'admin', '2020-02-21 16:24:37', 'admin', '2020-02-21 16:24:37', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450022965383168, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '210', 'admin', '2020-02-21 16:25:28', 'admin', '2020-02-21 16:25:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450278604017664, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-21 16:26:29', 'admin', '2020-02-21 16:26:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450362347491328, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-21 16:26:49', 'admin', '2020-02-21 16:26:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450621488369664, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '212', 'admin', '2020-02-21 16:27:50', 'admin', '2020-02-21 16:27:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450953798881280, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-21 16:29:10', 'admin', '2020-02-21 16:29:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450962837606400, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '200', 'admin', '2020-02-21 16:29:12', 'admin', '2020-02-21 16:29:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680450972291567616, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '206', 'admin', '2020-02-21 16:29:14', 'admin', '2020-02-21 16:29:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680451631288029184, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-21 16:31:51', 'admin', '2020-02-21 16:31:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680451641144643584, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '201', 'admin', '2020-02-21 16:31:53', 'admin', '2020-02-21 16:31:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680451656726482944, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-21 16:31:57', 'admin', '2020-02-21 16:31:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680451676137721856, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '197', 'admin', '2020-02-21 16:32:02', 'admin', '2020-02-21 16:32:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680451685977559040, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '210', 'admin', '2020-02-21 16:32:04', 'admin', '2020-02-21 16:32:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680452267257761792, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '10226', 'admin', '2020-02-21 16:34:23', 'admin', '2020-02-21 16:34:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680452276472647680, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '208', 'admin', '2020-02-21 16:34:25', 'admin', '2020-02-21 16:34:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680454120569704448, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '222', 'admin', '2020-02-21 16:41:45', 'admin', '2020-02-21 16:41:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680454165360676864, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '226', 'admin', '2020-02-21 16:41:55', 'admin', '2020-02-21 16:41:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680454220167647232, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '224', 'admin', '2020-02-21 16:42:08', 'admin', '2020-02-21 16:42:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680457469759197184, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '95', 'admin', '2020-02-21 16:55:03', 'admin', '2020-02-21 16:55:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680458338105954304, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '88', 'admin', '2020-02-21 16:58:30', 'admin', '2020-02-21 16:58:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680458523448053760, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '5231', 'admin', '2020-02-21 16:59:14', 'admin', '2020-02-21 16:59:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680458695968165888, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '227', 'admin', '2020-02-21 16:59:55', 'admin', '2020-02-21 16:59:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680459207715196928, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '216', 'admin', '2020-02-21 17:01:57', 'admin', '2020-02-21 17:01:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680459213499142144, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '216', 'admin', '2020-02-21 17:01:59', 'admin', '2020-02-21 17:01:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680459220151308288, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '218', 'admin', '2020-02-21 17:02:00', 'admin', '2020-02-21 17:02:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680459252258705408, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '216', 'admin', '2020-02-21 17:02:08', 'admin', '2020-02-21 17:02:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680459895211954176, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '195', 'admin', '2020-02-21 17:04:41', 'admin', '2020-02-21 17:04:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680460210879467520, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '195', 'admin', '2020-02-21 17:05:57', 'admin', '2020-02-21 17:05:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680460241816653824, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '194', 'admin', '2020-02-21 17:06:04', 'admin', '2020-02-21 17:06:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680460265556414464, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-21 17:06:10', 'admin', '2020-02-21 17:06:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680460273362014208, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '192', 'admin', '2020-02-21 17:06:12', 'admin', '2020-02-21 17:06:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680461264761262080, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '198', 'admin', '2020-02-21 17:10:08', 'admin', '2020-02-21 17:10:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680461271946104832, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '199', 'admin', '2020-02-21 17:10:10', 'admin', '2020-02-21 17:10:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680461325574475776, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '199', 'admin', '2020-02-21 17:10:22', 'admin', '2020-02-21 17:10:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680465831687229440, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '208', 'admin', '2020-02-21 17:28:17', 'admin', '2020-02-21 17:28:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680465846354710528, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '199', 'admin', '2020-02-21 17:28:20', 'admin', '2020-02-21 17:28:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466356205916160, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '177', 'admin', '2020-02-21 17:30:22', 'admin', '2020-02-21 17:30:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466369107595264, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '184', 'admin', '2020-02-21 17:30:25', 'admin', '2020-02-21 17:30:25', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466389932314624, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '178', 'admin', '2020-02-21 17:30:30', 'admin', '2020-02-21 17:30:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466713577394176, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '185', 'admin', '2020-02-21 17:31:47', 'admin', '2020-02-21 17:31:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466728068714496, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '186', 'admin', '2020-02-21 17:31:50', 'admin', '2020-02-21 17:31:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466741540818944, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '185', 'admin', '2020-02-21 17:31:54', 'admin', '2020-02-21 17:31:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466945287524352, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '194', 'admin', '2020-02-21 17:32:42', 'admin', '2020-02-21 17:32:42', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680466951683837952, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '194', 'admin', '2020-02-21 17:32:44', 'admin', '2020-02-21 17:32:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680467027810455552, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '174', 'admin', '2020-02-21 17:33:02', 'admin', '2020-02-21 17:33:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680467345210216448, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '190', 'admin', '2020-02-21 17:34:18', 'admin', '2020-02-21 17:34:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680467410414866432, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '187', 'admin', '2020-02-21 17:34:33', 'admin', '2020-02-21 17:34:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680470854143971328, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '177', 'admin', '2020-02-21 17:48:14', 'admin', '2020-02-21 17:48:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680473497910251520, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '98', 'admin', '2020-02-21 17:58:44', 'admin', '2020-02-21 17:58:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680473604693037056, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '5190', 'admin', '2020-02-21 17:59:10', 'admin', '2020-02-21 17:59:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680475497456603136, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '208', 'admin', '2020-02-21 18:06:41', 'admin', '2020-02-21 18:06:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680475505002156032, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '188', 'admin', '2020-02-21 18:06:43', 'admin', '2020-02-21 18:06:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680475514825216000, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '187', 'admin', '2020-02-21 18:06:45', 'admin', '2020-02-21 18:06:45', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680475661038653440, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '197', 'admin', '2020-02-21 18:07:20', 'admin', '2020-02-21 18:07:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680475954132422656, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '189', 'admin', '2020-02-21 18:08:30', 'admin', '2020-02-21 18:08:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680475999317659648, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '189', 'admin', '2020-02-21 18:08:41', 'admin', '2020-02-21 18:08:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680476069438033920, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '185', 'admin', '2020-02-21 18:08:58', 'admin', '2020-02-21 18:08:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680476074810937344, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '187', 'admin', '2020-02-21 18:08:59', 'admin', '2020-02-21 18:08:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680476086093615104, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '194', 'admin', '2020-02-21 18:09:02', 'admin', '2020-02-21 18:09:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680476123968180224, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '186', 'admin', '2020-02-21 18:09:11', 'admin', '2020-02-21 18:09:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680476170965356544, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '180', 'admin', '2020-02-21 18:09:22', 'admin', '2020-02-21 18:09:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680476198165417984, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '195', 'admin', '2020-02-21 18:09:28', 'admin', '2020-02-21 18:09:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680478574377046016, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '180', 'admin', '2020-02-21 18:18:55', 'admin', '2020-02-21 18:18:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680478696414515200, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '187', 'admin', '2020-02-21 18:19:24', 'admin', '2020-02-21 18:19:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480148503531520, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '188', 'admin', '2020-02-21 18:25:10', 'admin', '2020-02-21 18:25:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480154908233728, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '186', 'admin', '2020-02-21 18:25:12', 'admin', '2020-02-21 18:25:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480163800158208, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '186', 'admin', '2020-02-21 18:25:14', 'admin', '2020-02-21 18:25:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480167512117248, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '188', 'admin', '2020-02-21 18:25:15', 'admin', '2020-02-21 18:25:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480173627412480, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '190', 'admin', '2020-02-21 18:25:16', 'admin', '2020-02-21 18:25:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480181839859712, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '168', 'admin', '2020-02-21 18:25:18', 'admin', '2020-02-21 18:25:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480189964226560, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '183', 'admin', '2020-02-21 18:25:20', 'admin', '2020-02-21 18:25:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680480203146924032, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '170', 'admin', '2020-02-21 18:25:23', 'admin', '2020-02-21 18:25:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680494705913499648, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '99', 'admin', '2020-02-21 19:23:01', 'admin', '2020-02-21 19:23:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680494832287879168, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '210', 'admin', '2020-02-21 19:23:31', 'admin', '2020-02-21 19:23:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680494838164099072, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '5222', 'admin', '2020-02-21 19:23:32', 'admin', '2020-02-21 19:23:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680494843683803136, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '210', 'admin', '2020-02-21 19:23:34', 'admin', '2020-02-21 19:23:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680494871940829184, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '209', 'admin', '2020-02-21 19:23:40', 'admin', '2020-02-21 19:23:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680495807237066752, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '247', 'admin', '2020-02-21 19:27:23', 'admin', '2020-02-21 19:27:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680495836517502976, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '215', 'admin', '2020-02-21 19:27:30', 'admin', '2020-02-21 19:27:30', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680495853508628480, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '213', 'admin', '2020-02-21 19:27:34', 'admin', '2020-02-21 19:27:34', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680495859481317376, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '212', 'admin', '2020-02-21 19:27:36', 'admin', '2020-02-21 19:27:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680495871053402112, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '5217', 'admin', '2020-02-21 19:27:39', 'admin', '2020-02-21 19:27:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680495887499268096, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '206', 'admin', '2020-02-21 19:27:43', 'admin', '2020-02-21 19:27:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496529651404800, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '91', 'admin', '2020-02-21 19:30:16', 'admin', '2020-02-21 19:30:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496563210031104, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '447', 'admin', '2020-02-21 19:30:24', 'admin', '2020-02-21 19:30:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496630859960320, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '216', 'admin', '2020-02-21 19:30:40', 'admin', '2020-02-21 19:30:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496862523953152, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '215', 'admin', '2020-02-21 19:31:35', 'admin', '2020-02-21 19:31:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496874209284096, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '211', 'admin', '2020-02-21 19:31:38', 'admin', '2020-02-21 19:31:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496880232304640, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '215', 'admin', '2020-02-21 19:31:39', 'admin', '2020-02-21 19:31:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496898402029568, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '212', 'admin', '2020-02-21 19:31:44', 'admin', '2020-02-21 19:31:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496911375011840, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '215', 'admin', '2020-02-21 19:31:47', 'admin', '2020-02-21 19:31:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496920942219264, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '218', 'admin', '2020-02-21 19:31:49', 'admin', '2020-02-21 19:31:49', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496927304978432, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '216', 'admin', '2020-02-21 19:31:50', 'admin', '2020-02-21 19:31:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496936821854208, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '212', 'admin', '2020-02-21 19:31:53', 'admin', '2020-02-21 19:31:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496941825658880, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '240', 'admin', '2020-02-21 19:31:54', 'admin', '2020-02-21 19:31:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680496976042790912, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '213', 'admin', '2020-02-21 19:32:02', 'admin', '2020-02-21 19:32:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680497000009043968, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '209', 'admin', '2020-02-21 19:32:08', 'admin', '2020-02-21 19:32:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680497032212910080, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '212', 'admin', '2020-02-21 19:32:16', 'admin', '2020-02-21 19:32:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680497039624245248, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '217', 'admin', '2020-02-21 19:32:17', 'admin', '2020-02-21 19:32:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680497045936672768, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '211', 'admin', '2020-02-21 19:32:19', 'admin', '2020-02-21 19:32:19', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680515339200630784, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '97', 'admin', '2020-02-21 20:45:00', 'admin', '2020-02-21 20:45:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680542714642698240, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '93', 'admin', '2020-02-21 22:33:47', 'admin', '2020-02-21 22:33:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680557977551376384, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '98', 'admin', '2020-02-21 23:34:26', 'admin', '2020-02-21 23:34:26', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680558212512092160, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '232', 'admin', '2020-02-21 23:35:22', 'admin', '2020-02-21 23:35:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680749801729560576, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '237', 'admin', '2020-02-22 12:16:40', 'admin', '2020-02-22 12:16:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680750526836641792, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '2254', 'admin', '2020-02-22 12:19:33', 'admin', '2020-02-22 12:19:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680759112778256384, 0, '更新菜单', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/menu/', 'PUT', '', NULL, 'web_app', '334', 'admin', '2020-02-22 12:53:40', 'admin', '2020-02-22 12:53:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680759179337666560, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '300', 'admin', '2020-02-22 12:53:56', 'admin', '2020-02-22 12:53:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680759713863962624, 0, '更新菜单', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/menu/', 'PUT', '', NULL, 'web_app', '236', 'admin', '2020-02-22 12:56:04', 'admin', '2020-02-22 12:56:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680760317147484160, 0, '更新菜单', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/menu/', 'PUT', '', NULL, 'web_app', '269', 'admin', '2020-02-22 12:58:28', 'admin', '2020-02-22 12:58:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680760383094525952, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '352', 'admin', '2020-02-22 12:58:43', 'admin', '2020-02-22 12:58:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680760990635266048, 0, '更新菜单', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/menu/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-22 13:01:08', 'admin', '2020-02-22 13:01:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680761301760348160, 0, '更新菜单', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/menu/', 'PUT', '', NULL, 'web_app', '238', 'admin', '2020-02-22 13:02:22', 'admin', '2020-02-22 13:02:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680761497353326592, 0, '更新菜单', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/menu/', 'PUT', '', NULL, 'web_app', '203', 'admin', '2020-02-22 13:03:09', 'admin', '2020-02-22 13:03:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680761548456726528, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '246', 'admin', '2020-02-22 13:03:21', 'admin', '2020-02-22 13:03:21', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680764363707125760, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '361', 'admin', '2020-02-22 13:14:32', 'admin', '2020-02-22 13:14:32', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680764377128898560, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '200', 'admin', '2020-02-22 13:14:35', 'admin', '2020-02-22 13:14:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680764516153298944, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '209', 'admin', '2020-02-22 13:15:09', 'admin', '2020-02-22 13:15:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680764552639549440, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '209', 'admin', '2020-02-22 13:15:17', 'admin', '2020-02-22 13:15:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680764681203355648, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '201', 'admin', '2020-02-22 13:15:48', 'admin', '2020-02-22 13:15:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680764799356899328, 0, '更新知识', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/knowledge/', 'PUT', '', NULL, 'web_app', '217', 'admin', '2020-02-22 13:16:16', 'admin', '2020-02-22 13:16:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767056945221632, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '254', 'admin', '2020-02-22 13:25:14', 'admin', '2020-02-22 13:25:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767063987458048, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '214', 'admin', '2020-02-22 13:25:16', 'admin', '2020-02-22 13:25:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767119234830336, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '206', 'admin', '2020-02-22 13:25:29', 'admin', '2020-02-22 13:25:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767136678940672, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '203', 'admin', '2020-02-22 13:25:33', 'admin', '2020-02-22 13:25:33', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767160112517120, 0, '更新题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'PUT', '', NULL, 'web_app', '202', 'admin', '2020-02-22 13:25:39', 'admin', '2020-02-22 13:25:39', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767321853267968, 0, '更新考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examination/', 'PUT', '', NULL, 'web_app', '209', 'admin', '2020-02-22 13:26:18', 'admin', '2020-02-22 13:26:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767823626244096, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '90', 'admin', '2020-02-22 13:28:17', 'admin', '2020-02-22 13:28:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680767848926285824, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '448', 'admin', '2020-02-22 13:28:23', 'admin', '2020-02-22 13:28:23', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680772497485991936, 0, '删除题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/680750517378486272', 'DELETE', 'type=%5B2%5D', NULL, 'web_app', '274', 'admin', '2020-02-22 13:46:52', 'admin', '2020-02-22 13:46:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680772678331797504, 0, '新增题目', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/subject/', 'POST', '', NULL, 'web_app', '233', 'admin', '2020-02-22 13:47:35', 'admin', '2020-02-22 13:47:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680774921420410880, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '189', 'admin', '2020-02-22 13:56:29', 'admin', '2020-02-22 13:56:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775008125063168, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '174', 'admin', '2020-02-22 13:56:50', 'admin', '2020-02-22 13:56:50', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775016299761664, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '173', 'admin', '2020-02-22 13:56:52', 'admin', '2020-02-22 13:56:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775021576196096, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '197', 'admin', '2020-02-22 13:56:53', 'admin', '2020-02-22 13:56:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775032544301056, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '192', 'admin', '2020-02-22 13:56:56', 'admin', '2020-02-22 13:56:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775038663790592, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '172', 'admin', '2020-02-22 13:56:57', 'admin', '2020-02-22 13:56:57', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775048096780288, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '148', 'admin', '2020-02-22 13:57:00', 'admin', '2020-02-22 13:57:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775056414085120, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '188', 'admin', '2020-02-22 13:57:02', 'admin', '2020-02-22 13:57:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775310144311296, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '188', 'admin', '2020-02-22 13:58:02', 'admin', '2020-02-22 13:58:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775327655530496, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '181', 'admin', '2020-02-22 13:58:06', 'admin', '2020-02-22 13:58:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775734817591296, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '386', 'admin', '2020-02-22 13:59:43', 'admin', '2020-02-22 13:59:43', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680775998161162240, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '151', 'admin', '2020-02-22 14:00:46', 'admin', '2020-02-22 14:00:46', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680776786635788288, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '101', 'admin', '2020-02-22 14:03:54', 'admin', '2020-02-22 14:03:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680776828050345984, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '386', 'admin', '2020-02-22 14:04:04', 'admin', '2020-02-22 14:04:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778192935587840, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '169', 'admin', '2020-02-22 14:09:29', 'admin', '2020-02-22 14:09:29', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778230231339008, 0, '用户登录', '172.31.141.33', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '90', 'admin', '2020-02-22 14:09:38', 'admin', '2020-02-22 14:09:38', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778438625333248, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '199', 'admin', '2020-02-22 14:10:28', 'admin', '2020-02-22 14:10:28', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778450503602176, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '180', 'admin', '2020-02-22 14:10:31', 'admin', '2020-02-22 14:10:31', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778486998241280, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '201', 'admin', '2020-02-22 14:10:40', 'admin', '2020-02-22 14:10:40', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778517922844672, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '185', 'admin', '2020-02-22 14:10:47', 'admin', '2020-02-22 14:10:47', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778564588670976, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '184', 'admin', '2020-02-22 14:10:58', 'admin', '2020-02-22 14:10:58', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778785838206976, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '207', 'admin', '2020-02-22 14:11:51', 'admin', '2020-02-22 14:11:51', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778869464240128, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '202', 'admin', '2020-02-22 14:12:11', 'admin', '2020-02-22 14:12:11', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680778907301056512, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '212', 'admin', '2020-02-22 14:12:20', 'admin', '2020-02-22 14:12:20', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780554899165184, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '182', 'admin', '2020-02-22 14:18:53', 'admin', '2020-02-22 14:18:53', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780563451351040, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '179', 'admin', '2020-02-22 14:18:55', 'admin', '2020-02-22 14:18:55', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780584422871040, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '182', 'admin', '2020-02-22 14:19:00', 'admin', '2020-02-22 14:19:00', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780600914874368, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '201', 'admin', '2020-02-22 14:19:04', 'admin', '2020-02-22 14:19:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780615418777600, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '189', 'admin', '2020-02-22 14:19:07', 'admin', '2020-02-22 14:19:07', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780622888833024, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '179', 'admin', '2020-02-22 14:19:09', 'admin', '2020-02-22 14:19:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780661715505152, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '189', 'admin', '2020-02-22 14:19:18', 'admin', '2020-02-22 14:19:18', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780684826120192, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '209', 'admin', '2020-02-22 14:19:24', 'admin', '2020-02-22 14:19:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680780735191322624, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '182', 'admin', '2020-02-22 14:19:36', 'admin', '2020-02-22 14:19:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781093221306368, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '186', 'admin', '2020-02-22 14:21:01', 'admin', '2020-02-22 14:21:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781107964284928, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '202', 'admin', '2020-02-22 14:21:04', 'admin', '2020-02-22 14:21:04', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781114192826368, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '201', 'admin', '2020-02-22 14:21:06', 'admin', '2020-02-22 14:21:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781122820509696, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '181', 'admin', '2020-02-22 14:21:08', 'admin', '2020-02-22 14:21:08', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781141166395392, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '205', 'admin', '2020-02-22 14:21:12', 'admin', '2020-02-22 14:21:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781154269401088, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '181', 'admin', '2020-02-22 14:21:15', 'admin', '2020-02-22 14:21:15', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680781190889869312, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '206', 'admin', '2020-02-22 14:21:24', 'admin', '2020-02-22 14:21:24', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680785639368691712, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '3326', 'admin', '2020-02-22 14:39:05', 'admin', '2020-02-22 14:39:05', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680786558869180416, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '178', 'admin', '2020-02-22 14:42:44', 'admin', '2020-02-22 14:42:44', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680787301835608064, 0, '修改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/', 'PUT', '', NULL, 'web_app', '217', 'admin', '2020-02-22 14:45:41', 'admin', '2020-02-22 14:45:41', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788104214351872, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '235', 'admin', '2020-02-22 14:48:52', 'admin', '2020-02-22 14:48:52', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788196421931008, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '343', 'admin', '2020-02-22 14:49:14', 'admin', '2020-02-22 14:49:14', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788200893059072, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '5302', 'admin', '2020-02-22 14:49:16', 'admin', '2020-02-22 14:49:16', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788336423604224, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '229', 'admin', '2020-02-22 14:49:48', 'admin', '2020-02-22 14:49:48', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788362336014336, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '333', 'admin', '2020-02-22 14:49:54', 'admin', '2020-02-22 14:49:54', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788370816897024, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '233', 'admin', '2020-02-22 14:49:56', 'admin', '2020-02-22 14:49:56', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680788430803832832, 0, '批改答题', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/answer/mark', 'PUT', '', NULL, 'web_app', '341', 'admin', '2020-02-22 14:50:10', 'admin', '2020-02-22 14:50:10', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680798701706416128, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '572', 'admin', '2020-02-22 15:30:59', 'admin', '2020-02-22 15:30:59', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680802625960153088, 0, '用户登录', '192.168.75.105', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/oauth/token', 'POST', NULL, NULL, 'auth-service', '121', 'admin', '2020-02-22 15:46:35', 'admin', '2020-02-22 15:46:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680802781493334016, 0, '开始考试', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/examRecord/start', 'POST', '', NULL, 'web_app', '671', 'admin', '2020-02-22 15:47:12', 'admin', '2020-02-22 15:47:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES (680802931120934912, 0, '提交答题', '127.0.0.1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36', '/v1/answer/submit', 'POST', '', NULL, 'web_app', '283', 'admin', '2020-02-22 15:47:47', 'admin', '2020-02-22 15:47:47', 0, 'EXAM', 'gitee');

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
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
INSERT INTO `sys_menu` VALUES ('571348650370928640', '个人管理', 'personal', '/api/user/v1/personal/**', '-1', 'user', '30', '0', 1, 'admin', '2019-04-26 14:55:33', 'admin', '2020-02-22 13:03:09', '0', 'EXAM', 'Layout', '/personal', NULL, '个人管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571349816924311552', '个人资料', 'personal:message', '/api/user/v1/user/updateInfo', '571348650370928640', '', '29', '0', 1, 'admin', '2019-04-26 15:00:11', 'admin', '2019-04-26 15:00:11', '0', 'EXAM', 'views/personal/message', 'message', NULL, '个人资料', 'gitee');
INSERT INTO `sys_menu` VALUES ('571350099653955584', '修改密码', 'personal:password', '/api/user/v1/user/updateInfo', '571348650370928640', '', '30', '0', 1, 'admin', '2019-04-26 15:01:18', 'admin', '2019-04-26 15:01:18', '0', 'EXAM', 'views/personal/password', 'password', NULL, '修改密码', 'gitee');
INSERT INTO `sys_menu` VALUES ('571351763521769472', '附件管理', 'attachment', '/api/user/v1/attachment/**', '-1', 'documentation', '10', '0', 1, 'admin', '2019-04-26 15:07:55', 'admin', '2020-02-22 13:02:22', '0', 'EXAM', 'Layout', '/attachment', NULL, '附件管理', 'gitee');
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
INSERT INTO `sys_menu` VALUES ('571361163502292992', '系统监控', 'sys', '/api/monitor/**', '-1', 'monitoring', '7', '0', 1, 'admin', '2019-04-26 15:45:16', 'admin', '2020-02-22 13:01:08', '0', 'EXAM', 'Layout', '/monitor', NULL, '系统监控', 'gitee');
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
INSERT INTO `sys_menu` VALUES ('571369709904203776', '终端管理', 'sys:client', '/api/user/v1/client/**', '581237996276289536', '', '11', '0', 1, 'admin', '2019-04-26 16:19:14', 'admin', '2019-04-26 16:19:14', '0', 'EXAM', 'views/sys/client', 'client', NULL, '终端管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369965811273728', '路由管理', 'sys:route', '/api/user/route/**', '581237996276289536', '', '12', '0', 1, 'admin', '2019-04-26 16:20:15', 'admin', '2019-04-26 16:20:15', '0', 'EXAM', 'views/sys/route', 'route', NULL, '路由管理', 'gitee');
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
INSERT INTO `sys_menu` VALUES ('581237996276289536', '租户中心', 'tenant', '/api/user/v1/tenent/**', '-1', 'example', '3', '0', 1, 'admin', '2019-05-23 21:52:17', 'admin', '2020-02-22 12:58:27', '0', 'EXAM', 'Layout', '/tenant', NULL, '租户管理', 'gitee');
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
INSERT INTO `sys_role_menu` VALUES (640208290051133440, 624964171867492352, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (640208290051133441, 624964171867492352, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (640208290051133442, 624964171867492352, 571367565360762880);
INSERT INTO `sys_role_menu` VALUES (640208290051133443, 624964171867492352, 571367969767165952);
INSERT INTO `sys_role_menu` VALUES (640208290051133444, 624964171867492352, 571371375550402560);
INSERT INTO `sys_role_menu` VALUES (640208290051133445, 624964171867492352, 571371477828505600);
INSERT INTO `sys_role_menu` VALUES (640208290051133446, 624964171867492352, 571371606652358656);
INSERT INTO `sys_role_menu` VALUES (640208290051133447, 624964171867492352, 571371699010932736);
INSERT INTO `sys_role_menu` VALUES (640208290051133448, 624964171867492352, 571371773073952768);
INSERT INTO `sys_role_menu` VALUES (640208290051133449, 624964171867492352, 571368181252362240);
INSERT INTO `sys_role_menu` VALUES (640208290051133450, 624964171867492352, 571372425787346944);
INSERT INTO `sys_role_menu` VALUES (640208290051133451, 624964171867492352, 571372559308820480);
INSERT INTO `sys_role_menu` VALUES (640208290051133452, 624964171867492352, 571372707153842176);
INSERT INTO `sys_role_menu` VALUES (640208290051133453, 624964171867492352, 571368627413061632);
INSERT INTO `sys_role_menu` VALUES (640208290051133454, 624964171867492352, 571373219269971968);
INSERT INTO `sys_role_menu` VALUES (640208290051133455, 624964171867492352, 571373292582211584);
INSERT INTO `sys_role_menu` VALUES (640208290051133456, 624964171867492352, 571373363046518784);
INSERT INTO `sys_role_menu` VALUES (640208290051133457, 624964171867492352, 571373478440210432);
INSERT INTO `sys_role_menu` VALUES (640208290051133458, 624964171867492352, 571369094226513920);
INSERT INTO `sys_role_menu` VALUES (640208290051133459, 624964171867492352, 571373881496047616);
INSERT INTO `sys_role_menu` VALUES (640208290051133460, 624964171867492352, 571373962609692672);
INSERT INTO `sys_role_menu` VALUES (640208290051133461, 624964171867492352, 571374025859796992);
INSERT INTO `sys_role_menu` VALUES (640208290051133462, 624964171867492352, 571374113881460736);
INSERT INTO `sys_role_menu` VALUES (640208290051133463, 624964171867492352, 571374178956087296);
INSERT INTO `sys_role_menu` VALUES (640208290051133464, 624964171867492352, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (640208290051133465, 624964171867492352, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (640208290051133466, 624964171867492352, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (640208290051133467, 624964171867492352, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (640208290051133468, 624964171867492352, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (640208290051133469, 624964171867492352, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (640208290051133470, 624964171867492352, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (640208290051133471, 624964171867492352, 625058053556932608);
INSERT INTO `sys_role_menu` VALUES (640208290051133472, 624964171867492352, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (640208290051133473, 624964171867492352, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (640208290051133474, 624964171867492352, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (640208290051133475, 624964171867492352, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (640208290051133476, 624964171867492352, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (640208290051133477, 624964171867492352, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (640208290051133478, 624964171867492352, 625058859773464576);
INSERT INTO `sys_role_menu` VALUES (640208290051133479, 624964171867492352, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (640208290051133480, 624964171867492352, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (640208290051133481, 624964171867492352, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (640208290051133482, 624964171867492352, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (640208290051133483, 624964171867492352, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (640208290051133484, 624964171867492352, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (640208290051133485, 624964171867492352, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (640208290051133486, 624964171867492352, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (640208290051133487, 624964171867492352, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (640208290051133488, 624964171867492352, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (640208290051133489, 624964171867492352, 571350099653955584);
INSERT INTO `sys_role_menu` VALUES (640214746716573696, 596330074307956736, 571365178965364736);
INSERT INTO `sys_role_menu` VALUES (640214746716573697, 596330074307956736, 624972495417643008);
INSERT INTO `sys_role_menu` VALUES (640214746716573698, 596330074307956736, 571352797233156096);
INSERT INTO `sys_role_menu` VALUES (640214746716573699, 596330074307956736, 571353230286655488);
INSERT INTO `sys_role_menu` VALUES (640214746716573700, 596330074307956736, 571355240792723456);
INSERT INTO `sys_role_menu` VALUES (640214746716573701, 596330074307956736, 571355418715099136);
INSERT INTO `sys_role_menu` VALUES (640214746716573702, 596330074307956736, 571355486121758720);
INSERT INTO `sys_role_menu` VALUES (640214746716573703, 596330074307956736, 571353525381107712);
INSERT INTO `sys_role_menu` VALUES (640214746716573704, 596330074307956736, 571355686403969024);
INSERT INTO `sys_role_menu` VALUES (640214746716573705, 596330074307956736, 625058053556932608);
INSERT INTO `sys_role_menu` VALUES (640214746716573706, 596330074307956736, 571355921259827200);
INSERT INTO `sys_role_menu` VALUES (640214746716573707, 596330074307956736, 571356206782877696);
INSERT INTO `sys_role_menu` VALUES (640214746716573708, 596330074307956736, 571356537642160128);
INSERT INTO `sys_role_menu` VALUES (640214746716573709, 596330074307956736, 571356877741494272);
INSERT INTO `sys_role_menu` VALUES (640214746716573710, 596330074307956736, 571357072436891648);
INSERT INTO `sys_role_menu` VALUES (640214746716573711, 596330074307956736, 571357235276550144);
INSERT INTO `sys_role_menu` VALUES (640214746716573712, 596330074307956736, 625058859773464576);
INSERT INTO `sys_role_menu` VALUES (640214746716573713, 596330074307956736, 571353992756596736);
INSERT INTO `sys_role_menu` VALUES (640214746716573714, 596330074307956736, 571357821778661376);
INSERT INTO `sys_role_menu` VALUES (640214746716573715, 596330074307956736, 571358188264361984);
INSERT INTO `sys_role_menu` VALUES (640214746716573716, 596330074307956736, 571358308477308928);
INSERT INTO `sys_role_menu` VALUES (640214746716573717, 596330074307956736, 571358407353831424);
INSERT INTO `sys_role_menu` VALUES (640214746716573718, 596330074307956736, 571354428217626624);
INSERT INTO `sys_role_menu` VALUES (640214746716573719, 596330074307956736, 571359163205160960);
INSERT INTO `sys_role_menu` VALUES (640214746716573720, 596330074307956736, 571354823258148864);
INSERT INTO `sys_role_menu` VALUES (640214746716573721, 596330074307956736, 571348650370928640);
INSERT INTO `sys_role_menu` VALUES (640214746716573722, 596330074307956736, 571349816924311552);
INSERT INTO `sys_role_menu` VALUES (640214746716573723, 596330074307956736, 571350099653955584);

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
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '租户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (581236567985754112, 'gitee', '码云', '码云', 1, 'admin', '2019-05-23 21:46:36', 'admin', '2019-05-23 21:46:36', 0);
INSERT INTO `sys_tenant` VALUES (624983209976926208, 'test', 'test', 'test', 1, 'admin', '2019-09-21 19:34:42', 'admin', '2019-09-21 19:26:31', 1);
INSERT INTO `sys_tenant` VALUES (640219139809611776, 'test', 'test', 'test', 1, 'admin', '2019-11-02 16:10:39', 'admin', '2019-11-02 16:02:18', 0);

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
INSERT INTO `sys_user` VALUES (596078038307966976, '管理员', '15521089185', '0', '1633736729@qq.com', '2019-07-01', 0, 0, 571347099191480320, '管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-02-05 10:18:36', 'admin', '2020-02-05 10:18:36', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (596307222997372928, '梁同学', '15521089185', NULL, '1633736729@qq.com', '2019-07-01', 0, 1, NULL, '梁同学', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-02-07 12:52:22', 'admin', '2020-02-07 12:52:22', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (596332387600830464, '林老师', '15521089185', NULL, '1633736729@qq.com', '2019-07-03', 0, 1, NULL, '林老师', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-02-07 12:52:13', 'admin', '2020-02-07 12:52:13', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES (640219156066734080, 'test', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-11-02 16:02:22', 'admin', '2019-11-02 16:02:22', 0, 'EXAM', 'test');
INSERT INTO `sys_user` VALUES (647192514083819520, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-11-21 21:51:59', NULL, NULL, NULL, '15521089185', '2020-02-07 12:52:07', 'admin', '2020-02-07 12:52:08', 0, 'EXAM', 'gitee');

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
INSERT INTO `sys_user_auths` VALUES (596332387693105152, 596332387600830464, 1, 'teacher', '$2a$10$4p0VfFRF969ltVakk6Qz9uQyuZXZSRZy4kA2Ur8pgazKQMqpvNAEy', 'admin', '2019-07-04 13:32:01', 'admin', '2019-07-04 13:32:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES (640219156343558144, 640219156066734080, 1, 'test', '$2a$10$bpguhy/ha53j49hloMmn4ebOEfQ5I9vjFyUVWauHUbDb8eoq2nm8a', 'admin', '2019-11-02 16:02:22', 'admin', '2019-11-02 16:02:22', 0, 'EXAM', 'test');
INSERT INTO `sys_user_auths` VALUES (654009476407496704, 647192514083819520, 2, '15521089185', '$2a$10$5M076SrwnP/tcNgk4.KYV.FEkDlHSrkQh.C.rxP67dJ7xiPRIyyu2', 'admin', '2019-12-10 17:20:10', 'admin', '2019-12-10 17:20:10', 0, 'EXAM', 'gitee');

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
INSERT INTO `sys_user_role` VALUES (624959784956858368, 624959783077810176, 596117256346406912);
INSERT INTO `sys_user_role` VALUES (624983494547869696, 624983493071474688, 624964171867492352);
INSERT INTO `sys_user_role` VALUES (625079311711473664, 596332387600830500, 596330074307956700);
INSERT INTO `sys_user_role` VALUES (625079323929481216, 596078038307967000, 596117256346406900);
INSERT INTO `sys_user_role` VALUES (627482325994835968, 627482273154994200, 596116511031169000);
INSERT INTO `sys_user_role` VALUES (640219157350191104, 640219156066734080, 624964171867492352);
INSERT INTO `sys_user_role` VALUES (674559494004543488, 596078038307966976, 596117256346406912);
INSERT INTO `sys_user_role` VALUES (675322907609665536, 647192514083819520, 596116511031169024);
INSERT INTO `sys_user_role` VALUES (675322930409902080, 596332387600830464, 596330074307956736);
INSERT INTO `sys_user_role` VALUES (675322967361720320, 596307222997372928, 596116511031169024);

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
