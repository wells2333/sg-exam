/*
 Navicat Premium Data Transfer

 Source Server         : mysql_localhost
 Source Server Type    : MySQL
 Source Server Version : 50617
 Source Host           : localhost:3306
 Source Schema         : microservice-user

 Target Server Type    : MySQL
 Target Server Version : 50617
 File Encoding         : 65001

 Date: 13/07/2019 17:25:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `attach_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `attach_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件大小',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
  `fast_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件ID',
  `busi_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务ID',
  `busi_module` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务模块',
  `busi_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型 0-普通，1-头像',
  `preview_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览地址',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
INSERT INTO `sys_attachment` VALUES ('587326094462554112', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBAAClz8zQeAcxdFAAB2nqimI80188.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-09 17:04:12', 'admin', '2019-06-09 17:04:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('590961604166815744', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0KBuqASK_rAAB2nqimI80768.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 17:50:25', 'admin', '2019-06-19 17:50:25', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('590966504460259328', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0KC3qAWcIjAADGqyYhFOI172.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 18:09:54', 'admin', '2019-06-19 18:09:54', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('590967223477211136', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0KDCWAeZqEAAB2nqimI80765.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 18:12:45', 'admin', '2019-06-19 18:12:45', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('590968058558943232', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0KDO2AI6HzAADGqyYhFOI297.jpg', NULL, NULL, '0', NULL, 'admin', '2019-06-19 18:16:04', 'admin', '2019-06-19 18:16:04', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('590978944174526464', '四川省2016年普通高考适应性测试.docx', '1310242', 'group1', 'group1/M00/00/00/rBEADV0KFxCANq2WABP-In3Uu2Y18.docx', NULL, NULL, '2', NULL, 'admin', '2019-06-19 18:59:20', 'admin', '2019-06-19 18:59:20', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('590992646021976064', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0KI9KAGsTzAAB2nqimI80536.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-19 19:53:46', 'admin', '2019-06-19 19:53:46', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591693567886495744', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MsJ2Af0dbAADGqyYhFOI739.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:18:59', 'admin', '2019-06-21 18:18:59', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591694952287834112', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MseeAPR34AADGqyYhFOI455.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:24:29', 'admin', '2019-06-21 18:24:29', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591695271881216000', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MsjOANVdtAADGqyYhFOI235.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:25:45', 'admin', '2019-06-21 18:25:45', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591695964004290560', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MstiALOW1AADGqyYhFOI015.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:28:30', 'admin', '2019-06-21 18:28:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591696571138183168', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0Ms2mAE1w9AADGqyYhFOI173.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:30:55', 'admin', '2019-06-21 18:30:55', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591697443129790464', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MtDmAYZByAADGqyYhFOI194.jpg', NULL, NULL, '1', NULL, 'admin', '2019-06-21 18:34:23', 'admin', '2019-06-21 18:34:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591706139419348992', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/rBEADV0MvFKASQkYAADGqyYhFOI455.jpg', NULL, NULL, '1', NULL, 'student', '2019-06-21 19:08:57', 'student', '2019-06-21 19:08:57', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('591706239474470912', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/rBEADV0MvGqABeulAAB2nqimI80233.jpg', NULL, NULL, '1', NULL, 'student', '2019-06-21 19:09:20', 'student', '2019-06-21 19:09:20', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('596089139502387200', '20161004_152739.png', '1151189', 'group1', 'group1/M00/00/00/rBEADV0crl6APS7jABGQ1Qn9F3w723.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:25:25.658', 'admin', '2019-07-03 13:25:25.658', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('596091468104470528', '20161004_152739.png', '1151189', 'group1', 'group1/M00/00/00/rBEADV0csImAAEqgABGQ1Qn9F3w067.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:34:40.833', 'admin', '2019-07-03 13:34:40.833', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('596091563143204864', '20160706_145011.png', '848567', 'group1', 'group1/M00/00/00/rBEADV0csKCACMJ0AAzyt1rmFJA424.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:35:03.5', 'admin', '2019-07-03 13:35:03.5', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('596091774175416320', '20160706_144936.png', '426226', 'group1', 'group1/M00/00/00/rBEADV0csNKAHjgIAAaA8md5_jE199.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:35:53.808', 'admin', '2019-07-03 13:35:53.808', '1', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('596094244884713472', '20160706_145011.png', '848567', 'group1', 'group1/M00/00/00/rBEADV0csx-AG-ohAAzyt1rmFJA952.png', NULL, NULL, '1', NULL, 'admin', '2019-07-03 13:45:42.876', 'admin', '2019-07-03 13:45:42.876', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('597108212914851840', NULL, NULL, NULL, NULL, NULL, NULL, '1', 'https://wx.qlogo.cn/mmopen/vi_32/cNgLBdVaia1yhPsxIXVIzI69YMrK2dEiaiafo9PKvickKlsNMoA0BrfdqT9t5c8goO6m2JMrOpSuR4diaWlicKaiaeN3A/132', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:51.693', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:51.693', '0', 'EXAM', 'gitee');
INSERT INTO `sys_attachment` VALUES ('597110984758398977', NULL, NULL, NULL, NULL, NULL, NULL, '1', 'https://wx.qlogo.cn/mmopen/vi_32/cNgLBdVaia1yhPsxIXVIzI69YMrK2dEiaiafo9PKvickKlsNMoA0BrfdqT9t5c8goO6m2JMrOpSuR4diaWlicKaiaeN3A/132', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:52.552', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:52.552', '0', 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `dept_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `dept_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dept_leader` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目序号',
  `sort` int(11) NULL DEFAULT NULL COMMENT '题目类型',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('596290673729212416', '测试部门', '测试部门', '测试部门', '-1', 30, 'admin', '2019-07-04 10:47:41', 'admin', '2019-07-04 10:47:41', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `type` int(11) NULL DEFAULT NULL COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志标题',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户的IP地址',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户代理信息',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的URI',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的方式',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作提交的数据',
  `exception` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  `service_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '耗时',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('596361862757617664', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-04 15:29:08', 'admin', '2019-07-04 15:29:08', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596362548098502656', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-04 15:31:51', 'admin', '2019-07-04 15:31:51', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596656155825147904', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 10:58:33', 'admin', '2019-07-05 10:58:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596695287880355840', 0, '注册用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'code=%5B2986%5D&grant_type=%5Bmobile%5D&mobile=%5B15521089185%5D', NULL, NULL, '74192', 'anonymousUser', '2019-07-05 13:34:03', 'anonymousUser', '2019-07-05 13:34:03', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596695707675660288', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:35:43', 'admin', '2019-07-05 13:35:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596695876479619072', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:36:23', 'admin', '2019-07-05 13:36:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596695945450754048', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:36:39', 'admin', '2019-07-05 13:36:39', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596695976517963776', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '56', 'admin', '2019-07-05 13:36:47', 'admin', '2019-07-05 13:36:47', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596696085876051968', 0, '注册用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'code=%5B5201%5D&grant_type=%5Bmobile%5D&mobile=%5B15521089185%5D', NULL, NULL, '115', 'anonymousUser', '2019-07-05 13:37:13', 'anonymousUser', '2019-07-05 13:37:13', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596696847972700160', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '42', 'admin', '2019-07-05 13:40:15', 'admin', '2019-07-05 13:40:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596696862694707200', 0, '注册用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'code=%5B9754%5D&grant_type=%5Bmobile%5D&mobile=%5B15521089185%5D', NULL, NULL, '113', 'anonymousUser', '2019-07-05 13:40:18', 'anonymousUser', '2019-07-05 13:40:18', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596697972302024704', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '43', 'admin', '2019-07-05 13:44:43', 'admin', '2019-07-05 13:44:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596698160416559104', 0, '注册用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'code=%5B4033%5D&grant_type=%5Bmobile%5D&mobile=%5B15521089185%5D', NULL, NULL, '29251', 'anonymousUser', '2019-07-05 13:45:28', 'anonymousUser', '2019-07-05 13:45:28', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596698818263781376', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:48:04', 'admin', '2019-07-05 13:48:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596698845577089024', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:48:11', 'admin', '2019-07-05 13:48:11', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596699247445938176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:49:47', 'admin', '2019-07-05 13:49:47', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596699276470521856', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '39', 'admin', '2019-07-05 13:49:54', 'admin', '2019-07-05 13:49:54', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596699368283836416', 0, '注册用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'code=%5B5024%5D&grant_type=%5Bmobile%5D&mobile=%5B15521089185%5D', NULL, NULL, '118', 'anonymousUser', '2019-07-05 13:50:16', 'anonymousUser', '2019-07-05 13:50:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596700381791260672', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 13:54:17', 'admin', '2019-07-05 13:54:17', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596700426892611584', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '41', 'admin', '2019-07-05 13:54:28', 'admin', '2019-07-05 13:54:28', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596701118248128512', 0, '注册用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'code=%5B0122%5D&grant_type=%5Bmobile%5D&mobile=%5B15521089185%5D', NULL, NULL, '113', 'anonymousUser', '2019-07-05 13:57:13', 'anonymousUser', '2019-07-05 13:57:13', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596702905592057856', 0, '注册用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'identifier=%5Bgit1%5D&email=%5B1633736729%40qq.com%5D&randomStr=%5B20991562306638731%5D&code=%5Bw7dg%5D&password=%5B123456%5D&credential=%5B123456%5D', NULL, NULL, '103', 'anonymousUser', '2019-07-05 14:04:19', 'anonymousUser', '2019-07-05 14:04:19', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596705551606484992', 0, '注册用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'identifier=%5Bgit3%5D&email=%5B16337367289%40qq.ocm%5D&randomStr=%5B12141562307222680%5D&code=%5Baybm%5D&password=%5B123456%5D&credential=%5B123456%5D', NULL, NULL, '56643', 'anonymousUser', '2019-07-05 14:14:50', 'anonymousUser', '2019-07-05 14:14:50', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596708048018477056', 0, '注册用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'identifier=%5Bgit5%5D&email=%5B16337367289%40qq.ocm%5D&randomStr=%5B96291562307800540%5D&code=%5Bcc23%5D&password=%5B123456%5D&credential=%5B123456%5D', NULL, NULL, '77292', 'anonymousUser', '2019-07-05 14:24:45', 'anonymousUser', '2019-07-05 14:24:45', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713296971829248', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 14:45:36', 'admin', '2019-07-05 14:45:36', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713356258316288', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '113', 'admin', '2019-07-05 14:45:51', 'admin', '2019-07-05 14:45:51', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713369906581504', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '31', 'admin', '2019-07-05 14:45:54', 'admin', '2019-07-05 14:45:54', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713463074656256', 0, '注册用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/register', 'POST', 'identifier=%5Bgit%5D&email=%5B1633736729%40qq.com%5D&randomStr=%5B87311562309157864%5D&code=%5B4a5d%5D&password=%5B123456%5D&credential=%5B123456%5D', NULL, NULL, '124', 'anonymousUser', '2019-07-05 14:46:16', 'anonymousUser', '2019-07-05 14:46:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713533937422336', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:46:33', 'git', '2019-07-05 14:46:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713697938903040', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:47:12', 'git', '2019-07-05 14:47:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596713726997041152', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:47:19', 'git', '2019-07-05 14:47:19', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596714497012535296', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:50:23', 'git', '2019-07-05 14:50:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596714523482787840', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:50:29', 'git', '2019-07-05 14:50:29', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596714981949575168', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:52:18', 'git', '2019-07-05 14:52:18', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596715008675680256', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:52:24', 'git', '2019-07-05 14:52:24', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596715702845575168', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:55:10', 'git', '2019-07-05 14:55:10', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596715731215847424', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 14:55:17', 'git', '2019-07-05 14:55:17', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596717257812480000', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 15:01:21', 'git', '2019-07-05 15:01:21', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596717309591162880', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 15:01:33', 'git', '2019-07-05 15:01:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596719279789969408', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 15:09:23', 'git', '2019-07-05 15:09:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596719302984470528', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'git', '2019-07-05 15:09:28', 'git', '2019-07-05 15:09:28', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596719344612937728', 0, '更新用户基本信息', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/updateInfo', 'PUT', '', NULL, 'web_app', '208', 'git', '2019-07-05 15:09:38', 'git', '2019-07-05 15:09:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596719832167223296', 0, '删除用户', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/596713462604894208', 'DELETE', '', NULL, 'web_app', '46', 'admin', '2019-07-05 15:11:35', 'admin', '2019-07-05 15:11:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596720819258920960', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 15:15:30', 'admin', '2019-07-05 15:15:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596720858232393728', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 15:15:39', 'admin', '2019-07-05 15:15:39', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596720915270733824', 0, '上传文件', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/attachment/upload', 'POST', 'busiType=%5B0%5D', NULL, 'web_app', '689', 'admin', '2019-07-05 15:15:53', 'admin', '2019-07-05 15:15:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596820839073189888', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-05 21:52:56', 'admin', '2019-07-05 21:52:56', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('596820924108509184', 0, '修改用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/', 'PUT', '', NULL, 'web_app', '91', 'admin', '2019-07-05 21:53:17', 'admin', '2019-07-05 21:53:17', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597065486085263360', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '331', 'anonymousUser', '2019-07-06 14:05:05', 'anonymousUser', '2019-07-06 14:05:05', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597069580317364224', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 14:21:21', 'admin', '2019-07-06 14:21:21', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597069621840973824', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '50', 'admin', '2019-07-06 14:21:31', 'admin', '2019-07-06 14:21:31', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597069760865374208', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '126', 'anonymousUser', '2019-07-06 14:22:04', 'anonymousUser', '2019-07-06 14:22:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597070150759485440', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '29', 'admin', '2019-07-06 14:23:37', 'admin', '2019-07-06 14:23:37', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597070646983397376', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '21', 'admin', '2019-07-06 14:25:35', 'admin', '2019-07-06 14:25:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597071888711290880', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '45', 'admin', '2019-07-06 14:30:31', 'admin', '2019-07-06 14:30:31', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597072314269569024', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '115', 'anonymousUser', '2019-07-06 14:32:13', 'anonymousUser', '2019-07-06 14:32:13', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597072571237797888', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '32', 'admin', '2019-07-06 14:33:14', 'admin', '2019-07-06 14:33:14', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597073154157973504', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '102', 'anonymousUser', '2019-07-06 14:35:33', 'anonymousUser', '2019-07-06 14:35:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597073299444469760', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '29', 'admin', '2019-07-06 14:36:08', 'admin', '2019-07-06 14:36:08', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597073648909684736', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '116', 'anonymousUser', '2019-07-06 14:37:31', 'anonymousUser', '2019-07-06 14:37:31', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597075526271766528', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '23', 'admin', '2019-07-06 14:44:59', 'admin', '2019-07-06 14:44:59', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597075550787473408', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '104', 'anonymousUser', '2019-07-06 14:45:04', 'anonymousUser', '2019-07-06 14:45:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597076021174472704', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '21', 'admin', '2019-07-06 14:46:57', 'admin', '2019-07-06 14:46:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597076037175742464', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '105', 'anonymousUser', '2019-07-06 14:47:00', 'anonymousUser', '2019-07-06 14:47:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077083121913856', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '64', 'admin', '2019-07-06 14:51:10', 'admin', '2019-07-06 14:51:10', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077122984579072', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '137', 'anonymousUser', '2019-07-06 14:51:19', 'anonymousUser', '2019-07-06 14:51:19', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077123865382912', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:51:19', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:51:19', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077123995406336', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:51:20', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:51:20', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077346889109504', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:52:13', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:52:13', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077347023327232', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:52:13', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:52:13', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077693439283200', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:53:35', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:53:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597077693573500928', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:53:35', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:53:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597078045064564736', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:54:59', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:54:59', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597078045186199552', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:54:59', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 14:54:59', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079498768388096', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:00:46', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:00:46', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079498910994432', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:00:46', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:00:46', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079663856193536', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:25', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:25', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079663956856832', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:25', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:25', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079728733687808', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:41', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:41', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079728830156800', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:41', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:41', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079787462332416', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:55', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:55', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079787546218496', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:55', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:55', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079792730378240', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:56', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:56', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597079792818458624', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:56', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:01:56', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597080615543771136', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597080615631851520', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597080697638883328', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:32', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597080697752129536', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:32', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597080793508089856', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:54', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:54', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597080793591975936', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:54', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:05:54', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081343695917056', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:06', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081343792386048', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:06', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081443369357312', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:29', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:29', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081443436466176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:29', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:08:29', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081800241713152', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:09:54', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:09:54', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081800338182144', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:09:54', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:09:54', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081929447247872', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:10:25', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:10:25', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597081929564688384', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:10:25', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:10:25', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082314811510784', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:11:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:11:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082314907979776', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:11:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:11:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082525998911488', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:12:47', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:12:47', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082526095380480', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:12:47', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:12:47', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082725194797056', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:35', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082725274488832', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:35', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082758032003072', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:43', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597082758124277760', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:43', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:13:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597083179278536704', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:23', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597083179345645568', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:23', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597083267950317568', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:44', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:44', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597083268021620736', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:44', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:15:44', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597083649065750528', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:17:15', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:17:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597083649183191040', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:17:15', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:17:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597084248767336448', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:19:38', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:19:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597084248834445312', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:19:38', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:19:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597084675000897536', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:21:20', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:21:20', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597084675109949440', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:21:20', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:21:20', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597084867771109376', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:22:06', 'admin', '2019-07-06 15:22:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597085205832011776', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:23:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:23:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597085205899120640', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:23:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:23:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597085559319564288', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:24:51', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:24:51', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597085559428616192', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:24:51', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:24:51', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597087619804958720', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:33:02', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:33:02', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597087620480241664', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:33:02', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:33:02', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597087889402236928', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:06', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597087889481928704', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:06', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597087997975990272', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:32', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597087998085042176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:32', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:34:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597088149772046336', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:35:08', 'admin', '2019-07-06 15:35:08', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597088230009081856', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:35:27', 'admin', '2019-07-06 15:35:27', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597088251165151232', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:35:32', 'admin', '2019-07-06 15:35:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597088357100687360', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:35:58', 'admin', '2019-07-06 15:35:58', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597088766770941952', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:37:35', 'admin', '2019-07-06 15:37:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597089967168163840', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:42:22', 'admin', '2019-07-06 15:42:22', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597089992266878976', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:42:28', 'admin', '2019-07-06 15:42:28', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597090351592902656', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:43:53', 'admin', '2019-07-06 15:43:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597090401328959488', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:44:05', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:44:05', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597090401408651264', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:44:05', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 15:44:05', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597090503091163136', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 15:44:29', 'admin', '2019-07-06 15:44:29', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597090534712020992', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '44', 'admin', '2019-07-06 15:44:37', 'admin', '2019-07-06 15:44:37', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597100995826290688', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '229', 'anonymousUser', '2019-07-06 16:26:11', 'anonymousUser', '2019-07-06 16:26:11', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597100997990551552', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:26:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:26:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597100999081070592', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:26:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:26:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101213238038528', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '36', 'admin', '2019-07-06 16:27:03', 'admin', '2019-07-06 16:27:03', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101309035941888', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '106', 'anonymousUser', '2019-07-06 16:27:26', 'anonymousUser', '2019-07-06 16:27:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101309400846336', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:27:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:27:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101309493121024', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:27:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:27:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101519153795072', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:28:16', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:28:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101519472562176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:28:16', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:28:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101829083500544', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:29:30', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:29:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597101829200941056', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:29:30', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:29:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597108141506826240', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 16:54:35', 'admin', '2019-07-06 16:54:35', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597108178542530560', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '79', 'admin', '2019-07-06 16:54:43', 'admin', '2019-07-06 16:54:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597108213573357568', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '247', 'anonymousUser', '2019-07-06 16:54:52', 'anonymousUser', '2019-07-06 16:54:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597108215158804480', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597108215326576640', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:54:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109163780345856', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:58:38', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:58:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109163868426240', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:58:38', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:58:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109208156082176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 16:58:49', 'admin', '2019-07-06 16:58:49', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109272643506176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:04', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109272710615040', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:04', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109363018174464', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597109363097866240', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 16:59:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110112049565696', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:02:24', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:02:24', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110112129257472', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:02:25', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:02:25', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110890311061504', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 17:05:30', 'admin', '2019-07-06 17:05:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110937832525824', 0, '批量删除用户', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36', '/v1/user/deleteAll', 'POST', '', NULL, 'web_app', '28', 'admin', '2019-07-06 17:05:41', 'admin', '2019-07-06 17:05:41', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110985291075584', 0, '注册用户', '172.31.141.33', 'okhttp/3.8.1', '/v1/user/register', 'POST', '', NULL, NULL, '160', 'anonymousUser', '2019-07-06 17:05:53', 'anonymousUser', '2019-07-06 17:05:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110985572093952', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597110985643397120', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597111179097280512', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:06:39', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:06:39', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597111179499933696', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:06:39', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:06:39', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597111739598901248', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:08:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:08:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597111740488093696', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:08:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:08:53', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597112157963948032', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:10:32', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:10:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597112158047834112', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:10:32', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:10:32', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597113848155213824', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:17:15', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:17:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597113848704667648', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:17:15', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:17:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597114602605645824', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:20:15', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:20:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597114602714697728', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:20:15', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:20:15', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597114797162631168', NULL, '用户登录', '172.31.141.33', NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 17:21:02', 'admin', '2019-07-06 17:21:02', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597114831119716352', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'admin', '2019-07-06 17:21:10', 'admin', '2019-07-06 17:21:10', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597115751660392448', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:24:49', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:24:49', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597115751769444352', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:24:49', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:24:49', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597115912998490112', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:25:28', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:25:28', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597115913703133184', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:25:28', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:25:28', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597116737141477376', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:28:44', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:28:44', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597116737250529280', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:28:44', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:28:44', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597117136426635264', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:30:19', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:30:19', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597117136523104256', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:30:19', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:30:19', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597117463381020672', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:31:37', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:31:37', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597117463485878272', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:31:37', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:31:37', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597118169601150976', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:34:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:34:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597118169747951616', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:34:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:34:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597118380574642176', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:35:16', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:35:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597118380687888384', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:35:16', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:35:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597119119480983552', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:38:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:38:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597119119573258240', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:38:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:38:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597119641785077760', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:40:17', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:40:17', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597119641919295488', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:40:17', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:40:17', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597119960719953920', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:33', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597119960841588736', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:33', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120053292437504', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:55', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:55', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120053393100800', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:55', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:41:55', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120102030249984', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:06', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120102143496192', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:06', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:06', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120287384932352', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:50', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:50', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120287473012736', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:50', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:42:50', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120466758537216', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:43:33', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:43:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120466863394816', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:43:33', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:43:33', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120688922431488', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120689044066304', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120759667757056', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:43', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120759764226048', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:43', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:43', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120776142983168', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:47', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:47', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120776243646464', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:47', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:47', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120784552562688', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:49', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:49', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120784640643072', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:49', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:49', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120798439903232', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597120798532177920', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:44:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597121076274794496', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:45:59', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:45:59', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597121076341903360', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:45:59', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:45:59', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597121536712904704', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:48', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:48', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597121536771624960', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:48', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:48', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597121553670475776', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597121553729196032', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:47:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597122020290990080', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:49:44', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:49:44', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597122020345516032', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:49:44', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:49:44', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597124521463517184', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:59:40', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:59:40', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597124521539014656', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:59:40', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:59:40', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597125374169714688', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:03', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:03', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597125374228434944', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:03', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:03', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597125530399150080', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:41', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:41', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597125530457870336', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:41', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:03:41', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597126916591456256', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:11', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:11', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597126916645982208', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:11', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:11', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597126977589219328', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597126977660522496', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:26', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:09:26', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597127471904722944', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:23', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597127471959248896', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:23', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597127547947454464', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:42', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:42', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597127548001980416', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:42', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:11:42', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597127963527483392', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:13:21', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:13:21', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597127963590397952', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:13:21', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 18:13:21', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597145793962577920', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:24:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:24:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597145794201653248', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:24:12', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:24:12', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597146089900085248', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:25:22', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:25:22', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597146089996554240', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:25:22', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:25:22', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597147026005823488', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:05', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:05', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597147026089709568', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:05', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:05', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597147071983783936', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:16', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597147072071864320', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:16', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:29:16', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597147256092758016', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:30:00', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:30:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597147256172449792', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:30:00', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:30:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597148356095119360', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:34:23', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:34:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597148356187394048', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:34:23', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:34:23', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597151247589576704', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:45:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:45:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597151249011445760', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:45:52', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:45:52', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597151299271790592', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:46:04', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:46:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597151299376648192', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:46:04', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 19:46:04', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597535300494954496', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:11:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:11:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597535301287677952', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:11:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:11:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597535566401245184', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:13:01', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:13:01', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597535566627737600', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:13:01', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:13:01', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597535975555600384', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:14:38', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:14:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597535975626903552', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:14:38', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:14:38', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597536304661663744', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:15:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:15:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597536304720384000', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:15:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:15:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597537317628022784', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:19:58', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:19:58', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597537317695131648', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:19:58', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:19:58', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597537675976773632', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:21:24', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:21:24', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597537676043882496', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:21:24', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:21:24', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597537954084294656', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:22:30', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:22:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597537954151403520', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:22:30', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:22:30', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597538911287382016', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:26:18', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:26:18', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597538911471931392', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:26:18', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:26:18', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597541351734775808', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:36:00', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:36:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('597541351814467584', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:36:00', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-07 21:36:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('598570380692688896', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:45:00', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:45:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('598570382072614912', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:45:00', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:45:00', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('598571878625447936', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:50:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:50:57', '0', 'EXAM', 'gitee');
INSERT INTO `sys_log` VALUES ('598571878923243520', NULL, '用户登录', NULL, NULL, NULL, NULL, NULL, NULL, 'auth-service', NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:50:57', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-10 17:50:57', '0', 'EXAM', 'gitee');

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
INSERT INTO `sys_menu` VALUES ('571348650370928640', '个人管理', 'personal', '/api/user/v1/personal/**', '-1', 'form', '30', '0', 'admin', '2019-04-26 14:55:33', 'admin', '2019-04-26 15:04:16', '0', 'EXAM', 'Layout', '/personal', NULL, '个人管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571349816924311552', '个人资料', 'personal:message', '/api/user/v1/user/updateInfo', '571348650370928640', '', '29', '0', 'admin', '2019-04-26 15:00:11', 'admin', '2019-04-26 15:00:11', '0', 'EXAM', 'views/personal/message', 'message', NULL, '个人资料', 'gitee');
INSERT INTO `sys_menu` VALUES ('571350099653955584', '修改密码', 'personal:password', '/api/user/v1/user/updateInfo', '571348650370928640', '', '30', '0', 'admin', '2019-04-26 15:01:18', 'admin', '2019-04-26 15:01:18', '0', 'EXAM', 'views/personal/password', 'password', NULL, '修改密码', 'gitee');
INSERT INTO `sys_menu` VALUES ('571351763521769472', '附件管理', 'attachment', '/api/user/v1/attachment/**', '-1', 'excel', '10', '0', 'admin', '2019-04-26 15:07:55', 'admin', '2019-04-26 15:09:59', '0', 'EXAM', 'Layout', '/attachment', NULL, '附件管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571352087896657920', '附件列表', 'attachment:list', '/api/user/v1/attachment/list', '571351763521769472', '', '30', '0', 'admin', '2019-04-26 15:09:12', 'admin', '2019-04-26 15:09:12', '0', 'EXAM', 'views/attachment/list', 'list', NULL, '附件列表', 'gitee');
INSERT INTO `sys_menu` VALUES ('571352797233156096', '考务管理', 'exam', '/api/exam/**', '-1', 'nested', '8', '0', 'admin', '2019-04-26 15:12:02', 'admin', '2019-05-23 21:28:32', '0', 'EXAM', 'Layout', '/exam', NULL, '考务管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571353230286655488', '课程管理', 'exam:course', '/api/exam/v1/course/**', '571352797233156096', '', '1', '0', 'admin', '2019-04-26 15:13:45', 'admin', '2019-04-26 15:13:45', '0', 'EXAM', 'views/exam/course', 'course', NULL, '课程管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571353525381107712', '考试管理', 'exam:exam', '/api/exam/v1/examination/**', '571352797233156096', '', '2', '0', 'admin', '2019-04-26 15:14:55', 'admin', '2019-04-26 15:14:55', '0', 'EXAM', 'views/exam/exam', 'exam', NULL, '考试管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571353992756596736', '题库管理', 'exam:subject', '/api/exam/v1/subject/**', '571352797233156096', '', '3', '0', 'admin', '2019-04-26 15:16:47', 'admin', '2019-04-26 15:16:47', '0', 'EXAM', 'views/exam/subject', 'subject', NULL, '题库管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571354428217626624', '成绩管理', 'exam:examRecord', '/api/exam/v1/examRecord/**', '571352797233156096', '', '4', '0', 'admin', '2019-04-26 15:18:30', 'admin', '2019-04-26 15:18:30', '0', 'EXAM', 'views/exam/examRecord', 'score', NULL, '成绩管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571354823258148864', '知识库', 'exam:knowledge', '/api/exam/v1/knowledge/**', '571352797233156096', '', '5', '0', 'admin', '2019-04-26 15:20:05', 'admin', '2019-04-26 15:20:05', '0', 'EXAM', 'views/exam/knowledge', 'knowledge', NULL, '知识库', 'gitee');
INSERT INTO `sys_menu` VALUES ('571355240792723456', '新增课程', 'exam:course:add', NULL, '571353230286655488', '', '1', '1', 'admin', '2019-04-26 15:21:44', 'admin', '2019-04-26 15:21:44', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355418715099136', '修改课程', 'exam:course:edit', NULL, '571353230286655488', '', '2', '1', 'admin', '2019-04-26 15:22:27', 'admin', '2019-04-26 15:22:27', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355486121758720', '删除课程', 'exam:course:del', NULL, '571353230286655488', '', '3', '1', 'admin', '2019-04-26 15:22:43', 'admin', '2019-04-26 15:22:43', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355686403969024', '新增考试', 'exam:exam:add', NULL, '571353525381107712', '', '1', '1', 'admin', '2019-04-26 15:23:30', 'admin', '2019-04-26 15:23:30', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355830226653184', '修改课程', 'exam:exam:edit', NULL, '571353525381107712', '', '2', '1', 'admin', '2019-04-26 15:24:05', 'admin', '2019-04-26 15:24:05', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571355921259827200', '删除考试', 'exam:exam:del', NULL, '571353525381107712', '', '3', '1', 'admin', '2019-04-26 15:24:26', 'admin', '2019-04-26 15:24:26', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571356206782877696', '题目管理', 'exam:exam:subject', NULL, '571353525381107712', '', '4', '1', 'admin', '2019-04-26 15:25:34', 'admin', '2019-04-26 15:25:34', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571356537642160128', '导出题目', 'exam:exam:subject:export', NULL, '571353525381107712', '', '5', '1', 'admin', '2019-04-26 15:26:53', 'admin', '2019-04-26 15:27:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571356877741494272', '导入题目', 'exam:exam:subject:import', NULL, '571353525381107712', '', '6', '1', 'admin', '2019-04-26 15:28:14', 'admin', '2019-04-26 15:28:14', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357072436891648', '新增题目', 'exam:exam:subject:add', NULL, '571353525381107712', '', '7', '1', 'admin', '2019-04-26 15:29:01', 'admin', '2019-04-26 15:29:01', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357235276550144', '删除题目', 'exam:exam:subject:del', NULL, '571353525381107712', '', '8', '1', 'admin', '2019-04-26 15:29:40', 'admin', '2019-04-26 15:29:40', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357509638557696', '新增题目', 'exam:subject:bank:add', NULL, '571353992756596736', '', '1', '1', 'admin', '2019-04-26 15:30:45', 'admin', '2019-04-26 15:30:45', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357821778661376', '修改题目', 'exam:subject:bank:edit', NULL, '571353992756596736', '', '2', '1', 'admin', '2019-04-26 15:32:00', 'admin', '2019-04-26 15:32:00', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571357937931522048', '删除题目', 'exam:subject:bank:del', NULL, '571353992756596736', '', '3', '1', 'admin', '2019-04-26 15:32:27', 'admin', '2019-04-26 17:41:21', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358188264361984', '新增题目分类', 'exam:subject:category:add', NULL, '571353992756596736', '', '4', '1', 'admin', '2019-04-26 15:33:27', 'admin', '2019-04-26 15:33:27', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358308477308928', '修改题目分类', 'exam:subject:category:edit', NULL, '571353992756596736', '', '5', '1', 'admin', '2019-04-26 15:33:56', 'admin', '2019-04-26 15:33:56', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358407353831424', '删除题目分类', 'exam:subject:category:del', NULL, '571353992756596736', '', '6', '1', 'admin', '2019-04-26 15:34:19', 'admin', '2019-04-26 15:34:19', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358617991778304', '导入题目', 'exam:subject:bank:import', NULL, '571353992756596736', '', '7', '1', 'admin', '2019-04-26 15:35:09', 'admin', '2019-04-26 15:35:09', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571358928483520512', '导出题目', 'exam:subject:bank:export', NULL, '571353992756596736', '', '8', '1', 'admin', '2019-04-26 15:36:23', 'admin', '2019-04-26 15:36:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571359163205160960', '导出成绩', 'exam:examRecord:export', NULL, '571354428217626624', '', '30', '1', 'admin', '2019-04-26 15:37:19', 'admin', '2019-04-26 15:37:19', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571361163502292992', '系统监控', 'sys', '/api/monitor/**', '-1', 'chart', '7', '0', 'admin', '2019-04-26 15:45:16', 'admin', '2019-04-26 15:59:22', '0', 'EXAM', 'Layout', '/monitor', NULL, '系统监控', 'gitee');
INSERT INTO `sys_menu` VALUES ('571361526066319360', '日志监控', 'monitor:log', '/api/user/v1/log/**', '571361163502292992', '', '30', '0', 'admin', '2019-04-26 15:46:43', 'admin', '2019-04-26 15:46:43', '0', 'EXAM', 'views/monitor/log', 'log', NULL, '日志监控', 'gitee');
INSERT INTO `sys_menu` VALUES ('571361746552492032', 'consul监控', 'monitor:service', '/api/monitor/service/**', '571361163502292992', '', '31', '0', 'admin', '2019-04-26 15:47:35', 'admin', '2019-04-26 15:47:35', '0', 'EXAM', NULL, 'http://localhost:8500', NULL, 'consul监控', 'gitee');
INSERT INTO `sys_menu` VALUES ('571362994005610496', 'zipkin监控', 'monitor:link', '/api/monitor/**', '571361163502292992', '', '32', '0', 'admin', '2019-04-26 15:52:33', 'admin', '2019-04-26 15:52:33', '0', 'EXAM', NULL, 'http://localhost:9411/zipkin', NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571363268497641472', '服务监控', 'monitor:admin', '/api/monitor/**', '571361163502292992', '', '33', '0', 'admin', '2019-04-26 15:53:38', 'admin', '2019-04-26 15:53:38', '0', 'EXAM', NULL, 'http://localhost:8085/admin', NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571363537549660160', '接口文档', 'monitor:document', '/api/monitor/**', '571361163502292992', '', '34', '0', 'admin', '2019-04-26 15:54:42', 'admin', '2019-04-26 15:54:42', '0', 'EXAM', NULL, 'http://localhost:8000/swagger-ui.html', NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571364115214372864', '删除日志', 'monitor:log:del', NULL, '571361526066319360', '', '30', '1', 'admin', '2019-04-26 15:57:00', 'admin', '2019-04-26 15:57:00', '0', 'EXAM', NULL, NULL, NULL, '删除日志', 'gitee');
INSERT INTO `sys_menu` VALUES ('571365178965364736', '首页', 'dashboard', '/', '-1', 'dashboard', '0', '0', 'admin', '2019-04-26 16:01:14', 'admin', '2019-07-04 10:49:34', '0', 'EXAM', 'Layout', '/dashboard', NULL, '首页', 'gitee');
INSERT INTO `sys_menu` VALUES ('571367565360762880', '系统管理', 'sys', '/api/user/v1/**', '-1', 'component', '1', '0', 'admin', '2019-04-26 16:10:43', 'admin', '2019-05-23 21:52:26', '0', 'EXAM', 'Layout', '/sys', NULL, '系统管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571367969767165952', '用户管理', 'sys:user', '/api/user/v1/user/**', '571367565360762880', '', '2', '0', 'admin', '2019-04-26 16:12:19', 'admin', '2019-04-26 16:12:19', '0', 'EXAM', 'views/sys/user', 'user', NULL, '用户管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571368181252362240', '部门管理', 'sys:dept', '/api/user/v1/dept/**', '571367565360762880', '', '8', '0', 'admin', '2019-04-26 16:13:09', 'admin', '2019-04-26 16:13:09', '0', 'EXAM', 'views/sys/dept', 'dept', NULL, '部门管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571368627413061632', '角色管理', 'sys:role', '/api/user/v1/role/**', '571367565360762880', '', '9', '0', 'admin', '2019-04-26 16:14:56', 'admin', '2019-04-26 16:14:56', '0', 'EXAM', 'views/sys/role', 'role', NULL, '角色管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369094226513920', '菜单管理', 'sys:menu', '/api/user/v1/menu/**', '571367565360762880', '', '10', '0', 'admin', '2019-04-26 16:16:47', 'admin', '2019-04-26 16:16:47', '0', 'EXAM', 'views/sys/menu', 'menu', NULL, '菜单管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369709904203776', '终端管理', 'sys:client', '/api/user/v1/client/**', '571367565360762880', '', '11', '0', 'admin', '2019-04-26 16:19:14', 'admin', '2019-04-26 16:19:14', '0', 'EXAM', 'views/sys/client', 'client', NULL, '终端管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571369965811273728', '路由管理', 'sys:route', '/api/user/route/**', '571367565360762880', '', '12', '0', 'admin', '2019-04-26 16:20:15', 'admin', '2019-04-26 16:20:15', '0', 'EXAM', 'views/sys/route', 'route', NULL, '路由管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('571371375550402560', '新增用户', 'sys:user:add', NULL, '571367969767165952', '', '1', '1', 'admin', '2019-04-26 16:25:51', 'admin', '2019-07-04 10:50:33', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371477828505600', '删除用户', 'sys:user:del', NULL, '571367969767165952', '', '2', '1', 'admin', '2019-04-26 16:26:15', 'admin', '2019-04-26 16:26:15', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371606652358656', '修改用户', 'sys:user:edit', NULL, '571367969767165952', '', '3', '1', 'admin', '2019-04-26 16:26:46', 'admin', '2019-04-26 16:26:46', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371699010932736', '导出用户', 'sys:user:export', NULL, '571367969767165952', '', '4', '1', 'admin', '2019-04-26 16:27:08', 'admin', '2019-04-26 16:27:08', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571371773073952768', '导入用户', 'sys:user:import', NULL, '571367969767165952', '', '5', '1', 'admin', '2019-04-26 16:27:26', 'admin', '2019-04-26 16:27:26', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571372425787346944', '新增部门', 'sys:dept:add', NULL, '571368181252362240', '', '1', '1', 'admin', '2019-04-26 16:30:01', 'admin', '2019-04-26 16:30:01', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571372559308820480', '修改部门', 'sys:dept:edit', NULL, '571368181252362240', '', '2', '1', 'admin', '2019-04-26 16:30:33', 'admin', '2019-04-26 16:30:33', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571372707153842176', '删除部门', 'sys:dept:del', NULL, '571368181252362240', '', '3', '1', 'admin', '2019-04-26 16:31:08', 'admin', '2019-04-26 17:41:02', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373219269971968', '新增角色', 'sys:role:add', NULL, '571368627413061632', '', '1', '1', 'admin', '2019-04-26 16:33:11', 'admin', '2019-04-26 16:33:11', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373292582211584', '修改角色', 'sys:role:edit', NULL, '571368627413061632', '', '2', '1', 'admin', '2019-04-26 16:33:28', 'admin', '2019-04-26 16:33:28', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373363046518784', '删除角色', 'sys:role:del', NULL, '571368627413061632', '', '3', '1', 'admin', '2019-04-26 16:33:45', 'admin', '2019-04-26 16:33:45', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373478440210432', '分配权限', 'sys:role:auth', NULL, '571368627413061632', '', '4', '1', 'admin', '2019-04-26 16:34:12', 'admin', '2019-04-26 16:34:12', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373881496047616', '新增菜单', 'sys:menu:add', NULL, '571369094226513920', '', '1', '1', 'admin', '2019-04-26 16:35:48', 'admin', '2019-04-26 16:35:48', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571373962609692672', '修改菜单', 'sys:menu:edit', NULL, '571369094226513920', '', '2', '1', 'admin', '2019-04-26 16:36:08', 'admin', '2019-04-26 16:36:08', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374025859796992', '删除菜单', 'sys:menu:del', NULL, '571369094226513920', '', '3', '1', 'admin', '2019-04-26 16:36:23', 'admin', '2019-04-26 16:36:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374113881460736', '导入菜单', 'sys:menu:import', NULL, '571369094226513920', '', '4', '1', 'admin', '2019-04-26 16:36:44', 'admin', '2019-04-26 16:36:44', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374178956087296', '导出菜单', 'sys:menu:export', NULL, '571369094226513920', '', '5', '1', 'admin', '2019-04-26 16:36:59', 'admin', '2019-04-26 16:36:59', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374593844056064', '新增终端', 'sys:client:add', NULL, '571369709904203776', '', '1', '1', 'admin', '2019-04-26 16:38:38', 'admin', '2019-04-26 16:38:38', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374671245742080', '修改终端', 'sys:client:edit', NULL, '571369709904203776', '', '2', '1', 'admin', '2019-04-26 16:38:57', 'admin', '2019-04-26 16:38:57', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374747460440064', '删除终端', 'sys:client:del', NULL, '571369709904203776', '', '3', '1', 'admin', '2019-04-26 16:39:15', 'admin', '2019-04-26 16:39:15', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374884270247936', '新增路由', 'sys:route:add', NULL, '571369965811273728', '', '1', '1', 'admin', '2019-04-26 16:39:48', 'admin', '2019-04-26 16:39:48', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571374951823708160', '修改路由', 'sys:route:edit', NULL, '571369965811273728', '', '2', '1', 'admin', '2019-04-26 16:40:04', 'admin', '2019-04-26 16:40:04', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571375033570693120', '删除路由', 'sys:route:del', NULL, '571369965811273728', '', '3', '1', 'admin', '2019-04-26 16:40:23', 'admin', '2019-04-26 16:40:23', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571375135655858176', '刷新路由', 'sys:route:refresh', NULL, '571369965811273728', '', '4', '1', 'admin', '2019-04-26 16:40:47', 'admin', '2019-04-26 16:40:47', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('571454722205159424', '修改题目', 'exam:exam:subject:edit', NULL, '571353525381107712', '', '9', '1', 'admin', '2019-04-26 21:57:02', 'admin', '2019-04-26 21:57:02', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('581237996276289536', '租户中心', 'tenant', '/api/user/v1/tenent/**', '-1', 'component', '3', '0', 'admin', '2019-05-23 21:52:17', 'admin', '2019-05-23 22:00:51', '0', 'EXAM', 'Layout', '/tenant', NULL, '租户管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('581238351663861760', '单位管理', 'tenant:tenant', '/api/user/tenant/**', '581237996276289536', '', '1', '0', 'admin', '2019-05-23 21:53:41', 'admin', '2019-05-23 21:55:30', '0', 'EXAM', 'views/tenant/tenant', 'tenant', NULL, '单位管理', 'gitee');
INSERT INTO `sys_menu` VALUES ('581238560250793984', '新增单位', 'tenant:tenant:add', NULL, '581238351663861760', '', '1', '1', 'admin', '2019-05-23 21:54:31', 'admin', '2019-05-23 21:55:05', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('581238795467362304', '修改单位', 'tenant:tenant:edit', '', '581238351663861760', '', '2', '1', 'admin', '2019-05-23 21:55:27', 'admin', '2019-05-23 21:55:27', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');
INSERT INTO `sys_menu` VALUES ('581238883182841856', '删除单位', 'tenant:tenant:del', NULL, '581238351663861760', '', '3', '1', 'admin', '2019-05-23 21:55:48', 'admin', '2019-05-23 21:55:48', '0', 'EXAM', NULL, NULL, NULL, NULL, 'gitee');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目类型',
  `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `is_default` int(11) NULL DEFAULT NULL COMMENT '题目分值',
  `status` int(11) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('596116511031169024', '普通用户', 'role_user', '普通用户', 1, 0, 'admin', '2019-07-04 01:03:11', 'admin', '2019-07-04 01:03:12', 0, 'EXAM', 'gitee');
INSERT INTO `sys_role` VALUES ('596117256346406912', '管理员', 'role_admin', '管理员', 0, 0, 'admin', '2019-07-03 15:17:09', 'admin', '2019-07-03 15:17:09', 0, 'EXAM', 'gitee');
INSERT INTO `sys_role` VALUES ('596330074307956736', '教师', 'role_teacher', '教师', 0, 0, 'admin', '2019-07-04 13:22:49', 'admin', '2019-07-04 13:22:49', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `menu_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('581239021590679552', '571347202849509376', '571365178965364736');
INSERT INTO `sys_role_menu` VALUES ('581239021590679553', '571347202849509376', '571367565360762880');
INSERT INTO `sys_role_menu` VALUES ('581239021590679554', '571347202849509376', '571367969767165952');
INSERT INTO `sys_role_menu` VALUES ('581239021590679555', '571347202849509376', '571371375550402560');
INSERT INTO `sys_role_menu` VALUES ('581239021590679556', '571347202849509376', '571371477828505600');
INSERT INTO `sys_role_menu` VALUES ('581239021590679557', '571347202849509376', '571371606652358656');
INSERT INTO `sys_role_menu` VALUES ('581239021590679558', '571347202849509376', '571371699010932736');
INSERT INTO `sys_role_menu` VALUES ('581239021590679559', '571347202849509376', '571371773073952768');
INSERT INTO `sys_role_menu` VALUES ('581239021590679560', '571347202849509376', '571368181252362240');
INSERT INTO `sys_role_menu` VALUES ('581239021590679561', '571347202849509376', '571372425787346944');
INSERT INTO `sys_role_menu` VALUES ('581239021590679562', '571347202849509376', '571372559308820480');
INSERT INTO `sys_role_menu` VALUES ('581239021590679563', '571347202849509376', '571372707153842176');
INSERT INTO `sys_role_menu` VALUES ('581239021590679564', '571347202849509376', '571368627413061632');
INSERT INTO `sys_role_menu` VALUES ('581239021590679565', '571347202849509376', '571373219269971968');
INSERT INTO `sys_role_menu` VALUES ('581239021590679566', '571347202849509376', '571373292582211584');
INSERT INTO `sys_role_menu` VALUES ('581239021590679567', '571347202849509376', '571373363046518784');
INSERT INTO `sys_role_menu` VALUES ('581239021590679568', '571347202849509376', '571373478440210432');
INSERT INTO `sys_role_menu` VALUES ('581239021590679569', '571347202849509376', '571369094226513920');
INSERT INTO `sys_role_menu` VALUES ('581239021590679570', '571347202849509376', '571373881496047616');
INSERT INTO `sys_role_menu` VALUES ('581239021590679571', '571347202849509376', '571373962609692672');
INSERT INTO `sys_role_menu` VALUES ('581239021590679572', '571347202849509376', '571374025859796992');
INSERT INTO `sys_role_menu` VALUES ('581239021590679573', '571347202849509376', '571374113881460736');
INSERT INTO `sys_role_menu` VALUES ('581239021590679574', '571347202849509376', '571374178956087296');
INSERT INTO `sys_role_menu` VALUES ('581239021590679575', '571347202849509376', '571369709904203776');
INSERT INTO `sys_role_menu` VALUES ('581239021590679576', '571347202849509376', '571374593844056064');
INSERT INTO `sys_role_menu` VALUES ('581239021590679577', '571347202849509376', '571374671245742080');
INSERT INTO `sys_role_menu` VALUES ('581239021590679578', '571347202849509376', '571374747460440064');
INSERT INTO `sys_role_menu` VALUES ('581239021590679579', '571347202849509376', '571369965811273728');
INSERT INTO `sys_role_menu` VALUES ('581239021590679580', '571347202849509376', '571374884270247936');
INSERT INTO `sys_role_menu` VALUES ('581239021590679581', '571347202849509376', '571374951823708160');
INSERT INTO `sys_role_menu` VALUES ('581239021590679582', '571347202849509376', '571375033570693120');
INSERT INTO `sys_role_menu` VALUES ('581239021590679583', '571347202849509376', '571375135655858176');
INSERT INTO `sys_role_menu` VALUES ('581239021590679584', '571347202849509376', '581237996276289536');
INSERT INTO `sys_role_menu` VALUES ('581239021590679585', '571347202849509376', '581238351663861760');
INSERT INTO `sys_role_menu` VALUES ('581239021590679586', '571347202849509376', '581238560250793984');
INSERT INTO `sys_role_menu` VALUES ('581239021590679587', '571347202849509376', '581238795467362304');
INSERT INTO `sys_role_menu` VALUES ('581239021590679588', '571347202849509376', '581238883182841856');
INSERT INTO `sys_role_menu` VALUES ('581239021590679589', '571347202849509376', '571361163502292992');
INSERT INTO `sys_role_menu` VALUES ('581239021590679590', '571347202849509376', '571361526066319360');
INSERT INTO `sys_role_menu` VALUES ('581239021590679591', '571347202849509376', '571364115214372864');
INSERT INTO `sys_role_menu` VALUES ('581239021590679592', '571347202849509376', '571361746552492032');
INSERT INTO `sys_role_menu` VALUES ('581239021590679593', '571347202849509376', '571362994005610496');
INSERT INTO `sys_role_menu` VALUES ('581239021590679594', '571347202849509376', '571363268497641472');
INSERT INTO `sys_role_menu` VALUES ('581239021590679595', '571347202849509376', '571363537549660160');
INSERT INTO `sys_role_menu` VALUES ('581239021590679596', '571347202849509376', '571352797233156096');
INSERT INTO `sys_role_menu` VALUES ('581239021590679597', '571347202849509376', '571353230286655488');
INSERT INTO `sys_role_menu` VALUES ('581239021590679598', '571347202849509376', '571355240792723456');
INSERT INTO `sys_role_menu` VALUES ('581239021590679599', '571347202849509376', '571355418715099136');
INSERT INTO `sys_role_menu` VALUES ('581239021590679600', '571347202849509376', '571355486121758720');
INSERT INTO `sys_role_menu` VALUES ('581239021590679601', '571347202849509376', '571353525381107712');
INSERT INTO `sys_role_menu` VALUES ('581239021590679602', '571347202849509376', '571355686403969024');
INSERT INTO `sys_role_menu` VALUES ('581239021590679603', '571347202849509376', '571355830226653184');
INSERT INTO `sys_role_menu` VALUES ('581239021590679604', '571347202849509376', '571355921259827200');
INSERT INTO `sys_role_menu` VALUES ('581239021590679605', '571347202849509376', '571356206782877696');
INSERT INTO `sys_role_menu` VALUES ('581239021590679606', '571347202849509376', '571356537642160128');
INSERT INTO `sys_role_menu` VALUES ('581239021590679607', '571347202849509376', '571356877741494272');
INSERT INTO `sys_role_menu` VALUES ('581239021590679608', '571347202849509376', '571357072436891648');
INSERT INTO `sys_role_menu` VALUES ('581239021590679609', '571347202849509376', '571357235276550144');
INSERT INTO `sys_role_menu` VALUES ('581239021590679610', '571347202849509376', '571353992756596736');
INSERT INTO `sys_role_menu` VALUES ('581239021590679611', '571347202849509376', '571357509638557696');
INSERT INTO `sys_role_menu` VALUES ('581239021590679612', '571347202849509376', '571357821778661376');
INSERT INTO `sys_role_menu` VALUES ('581239021590679613', '571347202849509376', '571357937931522048');
INSERT INTO `sys_role_menu` VALUES ('581239021590679614', '571347202849509376', '571358188264361984');
INSERT INTO `sys_role_menu` VALUES ('581239021590679615', '571347202849509376', '571358308477308928');
INSERT INTO `sys_role_menu` VALUES ('581239021590679616', '571347202849509376', '571358407353831424');
INSERT INTO `sys_role_menu` VALUES ('581239021590679617', '571347202849509376', '571358617991778304');
INSERT INTO `sys_role_menu` VALUES ('581239021590679618', '571347202849509376', '571358928483520512');
INSERT INTO `sys_role_menu` VALUES ('581239021590679619', '571347202849509376', '571354428217626624');
INSERT INTO `sys_role_menu` VALUES ('581239021590679620', '571347202849509376', '571359163205160960');
INSERT INTO `sys_role_menu` VALUES ('581239021590679621', '571347202849509376', '571354823258148864');
INSERT INTO `sys_role_menu` VALUES ('581239021590679622', '571347202849509376', '571351763521769472');
INSERT INTO `sys_role_menu` VALUES ('581239021590679623', '571347202849509376', '571352087896657920');
INSERT INTO `sys_role_menu` VALUES ('581239021590679624', '571347202849509376', '571348650370928640');
INSERT INTO `sys_role_menu` VALUES ('581239021590679625', '571347202849509376', '571349816924311552');
INSERT INTO `sys_role_menu` VALUES ('581239021590679626', '571347202849509376', '571350099653955584');
INSERT INTO `sys_role_menu` VALUES ('586547078973493248', '571347272906969088', '571365178965364736');
INSERT INTO `sys_role_menu` VALUES ('586547078973493249', '571347272906969088', '571367565360762880');
INSERT INTO `sys_role_menu` VALUES ('586547078973493250', '571347272906969088', '571367969767165952');
INSERT INTO `sys_role_menu` VALUES ('586547078973493251', '571347272906969088', '571371375550402560');
INSERT INTO `sys_role_menu` VALUES ('586547078973493252', '571347272906969088', '571371477828505600');
INSERT INTO `sys_role_menu` VALUES ('586547078973493253', '571347272906969088', '571371606652358656');
INSERT INTO `sys_role_menu` VALUES ('586547078973493254', '571347272906969088', '571371699010932736');
INSERT INTO `sys_role_menu` VALUES ('586547078973493255', '571347272906969088', '571371773073952768');
INSERT INTO `sys_role_menu` VALUES ('586547078973493256', '571347272906969088', '571368181252362240');
INSERT INTO `sys_role_menu` VALUES ('586547078973493257', '571347272906969088', '571372425787346944');
INSERT INTO `sys_role_menu` VALUES ('586547078973493258', '571347272906969088', '571372559308820480');
INSERT INTO `sys_role_menu` VALUES ('586547078973493259', '571347272906969088', '571372707153842176');
INSERT INTO `sys_role_menu` VALUES ('586547078973493260', '571347272906969088', '571368627413061632');
INSERT INTO `sys_role_menu` VALUES ('586547078973493261', '571347272906969088', '571373219269971968');
INSERT INTO `sys_role_menu` VALUES ('586547078973493262', '571347272906969088', '571373292582211584');
INSERT INTO `sys_role_menu` VALUES ('586547078973493263', '571347272906969088', '571373363046518784');
INSERT INTO `sys_role_menu` VALUES ('586547078973493264', '571347272906969088', '571373478440210432');
INSERT INTO `sys_role_menu` VALUES ('586547078973493265', '571347272906969088', '571369094226513920');
INSERT INTO `sys_role_menu` VALUES ('586547078973493266', '571347272906969088', '571373881496047616');
INSERT INTO `sys_role_menu` VALUES ('586547078973493267', '571347272906969088', '571373962609692672');
INSERT INTO `sys_role_menu` VALUES ('586547078973493268', '571347272906969088', '571374025859796992');
INSERT INTO `sys_role_menu` VALUES ('586547078973493269', '571347272906969088', '571374113881460736');
INSERT INTO `sys_role_menu` VALUES ('586547078973493270', '571347272906969088', '571374178956087296');
INSERT INTO `sys_role_menu` VALUES ('586547078973493271', '571347272906969088', '571369709904203776');
INSERT INTO `sys_role_menu` VALUES ('586547078973493272', '571347272906969088', '571374593844056064');
INSERT INTO `sys_role_menu` VALUES ('586547078973493273', '571347272906969088', '571374671245742080');
INSERT INTO `sys_role_menu` VALUES ('586547078973493274', '571347272906969088', '571374747460440064');
INSERT INTO `sys_role_menu` VALUES ('586547078973493275', '571347272906969088', '571369965811273728');
INSERT INTO `sys_role_menu` VALUES ('586547078973493276', '571347272906969088', '571374884270247936');
INSERT INTO `sys_role_menu` VALUES ('586547078973493277', '571347272906969088', '571374951823708160');
INSERT INTO `sys_role_menu` VALUES ('586547078973493278', '571347272906969088', '571375033570693120');
INSERT INTO `sys_role_menu` VALUES ('586547078973493279', '571347272906969088', '571375135655858176');
INSERT INTO `sys_role_menu` VALUES ('586547078973493280', '571347272906969088', '571352797233156096');
INSERT INTO `sys_role_menu` VALUES ('586547078973493281', '571347272906969088', '571353230286655488');
INSERT INTO `sys_role_menu` VALUES ('586547078973493282', '571347272906969088', '571355240792723456');
INSERT INTO `sys_role_menu` VALUES ('586547078973493283', '571347272906969088', '571355418715099136');
INSERT INTO `sys_role_menu` VALUES ('586547078973493284', '571347272906969088', '571355486121758720');
INSERT INTO `sys_role_menu` VALUES ('586547078973493285', '571347272906969088', '571353525381107712');
INSERT INTO `sys_role_menu` VALUES ('586547078973493286', '571347272906969088', '571355686403969024');
INSERT INTO `sys_role_menu` VALUES ('586547078973493287', '571347272906969088', '571355830226653184');
INSERT INTO `sys_role_menu` VALUES ('586547078973493288', '571347272906969088', '571355921259827200');
INSERT INTO `sys_role_menu` VALUES ('586547078973493289', '571347272906969088', '571356206782877696');
INSERT INTO `sys_role_menu` VALUES ('586547078973493290', '571347272906969088', '571356537642160128');
INSERT INTO `sys_role_menu` VALUES ('586547078973493291', '571347272906969088', '571356877741494272');
INSERT INTO `sys_role_menu` VALUES ('586547078973493292', '571347272906969088', '571357072436891648');
INSERT INTO `sys_role_menu` VALUES ('586547078973493293', '571347272906969088', '571357235276550144');
INSERT INTO `sys_role_menu` VALUES ('586547078973493294', '571347272906969088', '571454722205159424');
INSERT INTO `sys_role_menu` VALUES ('586547078973493295', '571347272906969088', '571353992756596736');
INSERT INTO `sys_role_menu` VALUES ('586547078973493296', '571347272906969088', '571357509638557696');
INSERT INTO `sys_role_menu` VALUES ('586547078973493297', '571347272906969088', '571357821778661376');
INSERT INTO `sys_role_menu` VALUES ('586547078973493298', '571347272906969088', '571357937931522048');
INSERT INTO `sys_role_menu` VALUES ('586547078973493299', '571347272906969088', '571358188264361984');
INSERT INTO `sys_role_menu` VALUES ('586547078973493300', '571347272906969088', '571358308477308928');
INSERT INTO `sys_role_menu` VALUES ('586547078973493301', '571347272906969088', '571358407353831424');
INSERT INTO `sys_role_menu` VALUES ('586547078973493302', '571347272906969088', '571358617991778304');
INSERT INTO `sys_role_menu` VALUES ('586547078973493303', '571347272906969088', '571358928483520512');
INSERT INTO `sys_role_menu` VALUES ('586547078973493304', '571347272906969088', '571354428217626624');
INSERT INTO `sys_role_menu` VALUES ('586547078973493305', '571347272906969088', '571359163205160960');
INSERT INTO `sys_role_menu` VALUES ('586547078973493306', '571347272906969088', '571354823258148864');
INSERT INTO `sys_role_menu` VALUES ('586547078973493307', '571347272906969088', '571348650370928640');
INSERT INTO `sys_role_menu` VALUES ('586547078973493308', '571347272906969088', '571349816924311552');
INSERT INTO `sys_role_menu` VALUES ('586547078973493309', '571347272906969088', '571350099653955584');
INSERT INTO `sys_role_menu` VALUES ('586915493013753856', '586914735614726144', '571371375550402560');
INSERT INTO `sys_role_menu` VALUES ('590994415867269120', '571347357346697216', '571365178965364736');
INSERT INTO `sys_role_menu` VALUES ('590994415867269121', '571347357346697216', '571367565360762880');
INSERT INTO `sys_role_menu` VALUES ('590994415867269122', '571347357346697216', '571367969767165952');
INSERT INTO `sys_role_menu` VALUES ('590994415867269123', '571347357346697216', '571371375550402560');
INSERT INTO `sys_role_menu` VALUES ('590994415867269124', '571347357346697216', '571371477828505600');
INSERT INTO `sys_role_menu` VALUES ('590994415867269125', '571347357346697216', '571371606652358656');
INSERT INTO `sys_role_menu` VALUES ('590994415867269126', '571347357346697216', '571371699010932736');
INSERT INTO `sys_role_menu` VALUES ('590994415867269127', '571347357346697216', '571371773073952768');
INSERT INTO `sys_role_menu` VALUES ('590994415867269128', '571347357346697216', '571368181252362240');
INSERT INTO `sys_role_menu` VALUES ('590994415867269129', '571347357346697216', '571372425787346944');
INSERT INTO `sys_role_menu` VALUES ('590994415867269130', '571347357346697216', '571372559308820480');
INSERT INTO `sys_role_menu` VALUES ('590994415867269131', '571347357346697216', '571372707153842176');
INSERT INTO `sys_role_menu` VALUES ('590994415867269132', '571347357346697216', '571368627413061632');
INSERT INTO `sys_role_menu` VALUES ('590994415867269133', '571347357346697216', '571373219269971968');
INSERT INTO `sys_role_menu` VALUES ('590994415867269134', '571347357346697216', '571373292582211584');
INSERT INTO `sys_role_menu` VALUES ('590994415867269135', '571347357346697216', '571373363046518784');
INSERT INTO `sys_role_menu` VALUES ('590994415867269136', '571347357346697216', '571373478440210432');
INSERT INTO `sys_role_menu` VALUES ('590994415867269137', '571347357346697216', '571369094226513920');
INSERT INTO `sys_role_menu` VALUES ('590994415867269138', '571347357346697216', '571373881496047616');
INSERT INTO `sys_role_menu` VALUES ('590994415867269139', '571347357346697216', '571373962609692672');
INSERT INTO `sys_role_menu` VALUES ('590994415867269140', '571347357346697216', '571374025859796992');
INSERT INTO `sys_role_menu` VALUES ('590994415867269141', '571347357346697216', '571374113881460736');
INSERT INTO `sys_role_menu` VALUES ('590994415867269142', '571347357346697216', '571374178956087296');
INSERT INTO `sys_role_menu` VALUES ('590994415867269143', '571347357346697216', '571369709904203776');
INSERT INTO `sys_role_menu` VALUES ('590994415867269144', '571347357346697216', '571374593844056064');
INSERT INTO `sys_role_menu` VALUES ('590994415867269145', '571347357346697216', '571374671245742080');
INSERT INTO `sys_role_menu` VALUES ('590994415867269146', '571347357346697216', '571374747460440064');
INSERT INTO `sys_role_menu` VALUES ('590994415867269147', '571347357346697216', '571369965811273728');
INSERT INTO `sys_role_menu` VALUES ('590994415867269148', '571347357346697216', '571374884270247936');
INSERT INTO `sys_role_menu` VALUES ('590994415867269149', '571347357346697216', '571374951823708160');
INSERT INTO `sys_role_menu` VALUES ('590994415867269150', '571347357346697216', '571375033570693120');
INSERT INTO `sys_role_menu` VALUES ('590994415867269151', '571347357346697216', '571375135655858176');
INSERT INTO `sys_role_menu` VALUES ('590994415867269152', '571347357346697216', '571348650370928640');
INSERT INTO `sys_role_menu` VALUES ('590994415867269153', '571347357346697216', '571349816924311552');
INSERT INTO `sys_role_menu` VALUES ('590994415867269154', '571347357346697216', '571350099653955584');
INSERT INTO `sys_role_menu` VALUES ('596332559361773568', '596116511031169024', '571365178965364736');
INSERT INTO `sys_role_menu` VALUES ('596332559361773569', '596116511031169024', '571348650370928640');
INSERT INTO `sys_role_menu` VALUES ('596332559361773570', '596116511031169024', '571349816924311552');
INSERT INTO `sys_role_menu` VALUES ('596332559361773571', '596116511031169024', '571350099653955584');
INSERT INTO `sys_role_menu` VALUES ('596332580949856256', '596117256346406912', '571365178965364736');
INSERT INTO `sys_role_menu` VALUES ('596332580949856257', '596117256346406912', '571367565360762880');
INSERT INTO `sys_role_menu` VALUES ('596332580949856258', '596117256346406912', '571367969767165952');
INSERT INTO `sys_role_menu` VALUES ('596332580949856259', '596117256346406912', '571371375550402560');
INSERT INTO `sys_role_menu` VALUES ('596332580954050560', '596117256346406912', '571371477828505600');
INSERT INTO `sys_role_menu` VALUES ('596332580954050561', '596117256346406912', '571371606652358656');
INSERT INTO `sys_role_menu` VALUES ('596332580954050562', '596117256346406912', '571371699010932736');
INSERT INTO `sys_role_menu` VALUES ('596332580954050563', '596117256346406912', '571371773073952768');
INSERT INTO `sys_role_menu` VALUES ('596332580954050564', '596117256346406912', '571368181252362240');
INSERT INTO `sys_role_menu` VALUES ('596332580954050565', '596117256346406912', '571372425787346944');
INSERT INTO `sys_role_menu` VALUES ('596332580954050566', '596117256346406912', '571372559308820480');
INSERT INTO `sys_role_menu` VALUES ('596332580954050567', '596117256346406912', '571372707153842176');
INSERT INTO `sys_role_menu` VALUES ('596332580954050568', '596117256346406912', '571368627413061632');
INSERT INTO `sys_role_menu` VALUES ('596332580954050569', '596117256346406912', '571373219269971968');
INSERT INTO `sys_role_menu` VALUES ('596332580954050570', '596117256346406912', '571373292582211584');
INSERT INTO `sys_role_menu` VALUES ('596332580954050571', '596117256346406912', '571373363046518784');
INSERT INTO `sys_role_menu` VALUES ('596332580954050572', '596117256346406912', '571373478440210432');
INSERT INTO `sys_role_menu` VALUES ('596332580954050573', '596117256346406912', '571369094226513920');
INSERT INTO `sys_role_menu` VALUES ('596332580954050574', '596117256346406912', '571373881496047616');
INSERT INTO `sys_role_menu` VALUES ('596332580954050575', '596117256346406912', '571373962609692672');
INSERT INTO `sys_role_menu` VALUES ('596332580954050576', '596117256346406912', '571374025859796992');
INSERT INTO `sys_role_menu` VALUES ('596332580954050577', '596117256346406912', '571374113881460736');
INSERT INTO `sys_role_menu` VALUES ('596332580954050578', '596117256346406912', '571374178956087296');
INSERT INTO `sys_role_menu` VALUES ('596332580954050579', '596117256346406912', '571369709904203776');
INSERT INTO `sys_role_menu` VALUES ('596332580954050580', '596117256346406912', '571374593844056064');
INSERT INTO `sys_role_menu` VALUES ('596332580954050581', '596117256346406912', '571374671245742080');
INSERT INTO `sys_role_menu` VALUES ('596332580954050582', '596117256346406912', '571374747460440064');
INSERT INTO `sys_role_menu` VALUES ('596332580954050583', '596117256346406912', '571369965811273728');
INSERT INTO `sys_role_menu` VALUES ('596332580954050584', '596117256346406912', '571374884270247936');
INSERT INTO `sys_role_menu` VALUES ('596332580954050585', '596117256346406912', '571374951823708160');
INSERT INTO `sys_role_menu` VALUES ('596332580954050586', '596117256346406912', '571375033570693120');
INSERT INTO `sys_role_menu` VALUES ('596332580954050587', '596117256346406912', '571375135655858176');
INSERT INTO `sys_role_menu` VALUES ('596332580954050588', '596117256346406912', '581237996276289536');
INSERT INTO `sys_role_menu` VALUES ('596332580954050589', '596117256346406912', '581238351663861760');
INSERT INTO `sys_role_menu` VALUES ('596332580954050590', '596117256346406912', '581238560250793984');
INSERT INTO `sys_role_menu` VALUES ('596332580954050591', '596117256346406912', '581238795467362304');
INSERT INTO `sys_role_menu` VALUES ('596332580954050592', '596117256346406912', '581238883182841856');
INSERT INTO `sys_role_menu` VALUES ('596332580954050593', '596117256346406912', '571361163502292992');
INSERT INTO `sys_role_menu` VALUES ('596332580954050594', '596117256346406912', '571361526066319360');
INSERT INTO `sys_role_menu` VALUES ('596332580954050595', '596117256346406912', '571364115214372864');
INSERT INTO `sys_role_menu` VALUES ('596332580954050596', '596117256346406912', '571361746552492032');
INSERT INTO `sys_role_menu` VALUES ('596332580954050597', '596117256346406912', '571362994005610496');
INSERT INTO `sys_role_menu` VALUES ('596332580954050598', '596117256346406912', '571363268497641472');
INSERT INTO `sys_role_menu` VALUES ('596332580954050599', '596117256346406912', '571363537549660160');
INSERT INTO `sys_role_menu` VALUES ('596332580954050600', '596117256346406912', '571352797233156096');
INSERT INTO `sys_role_menu` VALUES ('596332580954050601', '596117256346406912', '571353230286655488');
INSERT INTO `sys_role_menu` VALUES ('596332580954050602', '596117256346406912', '571355240792723456');
INSERT INTO `sys_role_menu` VALUES ('596332580954050603', '596117256346406912', '571355418715099136');
INSERT INTO `sys_role_menu` VALUES ('596332580954050604', '596117256346406912', '571355486121758720');
INSERT INTO `sys_role_menu` VALUES ('596332580954050605', '596117256346406912', '571353525381107712');
INSERT INTO `sys_role_menu` VALUES ('596332580954050606', '596117256346406912', '571355686403969024');
INSERT INTO `sys_role_menu` VALUES ('596332580954050607', '596117256346406912', '571355830226653184');
INSERT INTO `sys_role_menu` VALUES ('596332580954050608', '596117256346406912', '571355921259827200');
INSERT INTO `sys_role_menu` VALUES ('596332580954050609', '596117256346406912', '571356206782877696');
INSERT INTO `sys_role_menu` VALUES ('596332580954050610', '596117256346406912', '571356537642160128');
INSERT INTO `sys_role_menu` VALUES ('596332580954050611', '596117256346406912', '571356877741494272');
INSERT INTO `sys_role_menu` VALUES ('596332580954050612', '596117256346406912', '571357072436891648');
INSERT INTO `sys_role_menu` VALUES ('596332580954050613', '596117256346406912', '571357235276550144');
INSERT INTO `sys_role_menu` VALUES ('596332580954050614', '596117256346406912', '571454722205159424');
INSERT INTO `sys_role_menu` VALUES ('596332580954050615', '596117256346406912', '571353992756596736');
INSERT INTO `sys_role_menu` VALUES ('596332580954050616', '596117256346406912', '571357509638557696');
INSERT INTO `sys_role_menu` VALUES ('596332580954050617', '596117256346406912', '571357821778661376');
INSERT INTO `sys_role_menu` VALUES ('596332580954050618', '596117256346406912', '571357937931522048');
INSERT INTO `sys_role_menu` VALUES ('596332580954050619', '596117256346406912', '571358188264361984');
INSERT INTO `sys_role_menu` VALUES ('596332580954050620', '596117256346406912', '571358308477308928');
INSERT INTO `sys_role_menu` VALUES ('596332580954050621', '596117256346406912', '571358407353831424');
INSERT INTO `sys_role_menu` VALUES ('596332580954050622', '596117256346406912', '571358617991778304');
INSERT INTO `sys_role_menu` VALUES ('596332580954050623', '596117256346406912', '571358928483520512');
INSERT INTO `sys_role_menu` VALUES ('596332580954050624', '596117256346406912', '571354428217626624');
INSERT INTO `sys_role_menu` VALUES ('596332580954050625', '596117256346406912', '571359163205160960');
INSERT INTO `sys_role_menu` VALUES ('596332580954050626', '596117256346406912', '571354823258148864');
INSERT INTO `sys_role_menu` VALUES ('596332580954050627', '596117256346406912', '571351763521769472');
INSERT INTO `sys_role_menu` VALUES ('596332580954050628', '596117256346406912', '571352087896657920');
INSERT INTO `sys_role_menu` VALUES ('596332580954050629', '596117256346406912', '571348650370928640');
INSERT INTO `sys_role_menu` VALUES ('596332580954050630', '596117256346406912', '571349816924311552');
INSERT INTO `sys_role_menu` VALUES ('596332580954050631', '596117256346406912', '571350099653955584');
INSERT INTO `sys_role_menu` VALUES ('596333782710226944', '596330074307956736', '571365178965364736');
INSERT INTO `sys_role_menu` VALUES ('596333782710226945', '596330074307956736', '571352797233156096');
INSERT INTO `sys_role_menu` VALUES ('596333782710226946', '596330074307956736', '571353230286655488');
INSERT INTO `sys_role_menu` VALUES ('596333782710226947', '596330074307956736', '571355240792723456');
INSERT INTO `sys_role_menu` VALUES ('596333782710226948', '596330074307956736', '571355418715099136');
INSERT INTO `sys_role_menu` VALUES ('596333782710226949', '596330074307956736', '571355486121758720');
INSERT INTO `sys_role_menu` VALUES ('596333782710226950', '596330074307956736', '571353525381107712');
INSERT INTO `sys_role_menu` VALUES ('596333782710226951', '596330074307956736', '571355686403969024');
INSERT INTO `sys_role_menu` VALUES ('596333782710226952', '596330074307956736', '571355830226653184');
INSERT INTO `sys_role_menu` VALUES ('596333782710226953', '596330074307956736', '571355921259827200');
INSERT INTO `sys_role_menu` VALUES ('596333782710226954', '596330074307956736', '571356206782877696');
INSERT INTO `sys_role_menu` VALUES ('596333782710226955', '596330074307956736', '571356537642160128');
INSERT INTO `sys_role_menu` VALUES ('596333782710226956', '596330074307956736', '571356877741494272');
INSERT INTO `sys_role_menu` VALUES ('596333782710226957', '596330074307956736', '571357072436891648');
INSERT INTO `sys_role_menu` VALUES ('596333782710226958', '596330074307956736', '571357235276550144');
INSERT INTO `sys_role_menu` VALUES ('596333782710226959', '596330074307956736', '571454722205159424');
INSERT INTO `sys_role_menu` VALUES ('596333782710226960', '596330074307956736', '571353992756596736');
INSERT INTO `sys_role_menu` VALUES ('596333782710226961', '596330074307956736', '571357509638557696');
INSERT INTO `sys_role_menu` VALUES ('596333782710226962', '596330074307956736', '571357821778661376');
INSERT INTO `sys_role_menu` VALUES ('596333782710226963', '596330074307956736', '571357937931522048');
INSERT INTO `sys_role_menu` VALUES ('596333782710226964', '596330074307956736', '571358188264361984');
INSERT INTO `sys_role_menu` VALUES ('596333782710226965', '596330074307956736', '571358308477308928');
INSERT INTO `sys_role_menu` VALUES ('596333782710226966', '596330074307956736', '571358407353831424');
INSERT INTO `sys_role_menu` VALUES ('596333782710226967', '596330074307956736', '571358617991778304');
INSERT INTO `sys_role_menu` VALUES ('596333782710226968', '596330074307956736', '571358928483520512');
INSERT INTO `sys_role_menu` VALUES ('596333782710226969', '596330074307956736', '571354428217626624');
INSERT INTO `sys_role_menu` VALUES ('596333782710226970', '596330074307956736', '571359163205160960');
INSERT INTO `sys_role_menu` VALUES ('596333782710226971', '596330074307956736', '571354823258148864');
INSERT INTO `sys_role_menu` VALUES ('596333782710226972', '596330074307956736', '571348650370928640');
INSERT INTO `sys_role_menu` VALUES ('596333782710226973', '596330074307956736', '571349816924311552');
INSERT INTO `sys_role_menu` VALUES ('596333782710226974', '596330074307956736', '571350099653955584');

-- ----------------------------
-- Table structure for sys_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_route`;
CREATE TABLE `sys_route`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `route_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由ID',
  `route_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `predicates` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '断言',
  `filters` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '过滤器',
  `uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'URI',
  `sort` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '排序',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '启用禁用',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_route
-- ----------------------------
INSERT INTO `sys_route` VALUES ('0f1aec35a545433c8cc0c8e78995f039', 'auth-service', '认证授权服务', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/auth/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://auth-service', '0', '0', 'admin', '2019-04-07 11:20:55', 'admin', '2019-04-26 22:45:28', 0, 'EXAM');
INSERT INTO `sys_route` VALUES ('5d9dd5f2cb1147aaad6f8b82a58586e8', 'exam-service', '考试服务', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/exam/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://exam-service', '0', '0', 'admin', '2019-04-02 21:39:30', 'admin', '2019-04-26 22:45:30', 0, 'EXAM');
INSERT INTO `sys_route` VALUES ('e9199257e8dc4f2d8fbb2a113c407eca', 'user-service', '用户服务', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/user/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://user-service', '0', '0', 'admin', '2019-04-07 11:22:05', 'admin', '2019-06-07 21:48:45', 0, 'EXAM');

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;
CREATE TABLE `sys_student`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `student_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `born` date NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL COMMENT '难度等级',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `grade` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细描述',
  `school` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就读学校',
  `wechat` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(11) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学生表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户标识',
  `tenant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户名称',
  `tenant_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户描述信息',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '租户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES ('581236567985754112', 'gitee', '码云', '码云', '1', 'admin', '2019-05-23 21:46:36', 'admin', '2019-05-23 21:46:36', '0');
INSERT INTO `sys_tenant` VALUES ('586561187559378944', 'github', 'github', 'github', '1', 'admin', '2019-06-07 14:24:44', NULL, NULL, '0');
INSERT INTO `sys_tenant` VALUES ('590248880176762880', '1', '1', '1', '0', 'admin', '2019-06-17 18:38:19', 'admin', '2019-06-17 18:38:19', '1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `avatar_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目分值',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解析',
  `born` date NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL COMMENT '难度等级',
  `dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `user_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细描述',
  `parent_uid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `street_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `county_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `province_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_time` timestamp(0) NULL DEFAULT NULL,
  `lock_time` timestamp(0) NULL DEFAULT NULL,
  `wechat` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(11) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('596078038307966976', '管理员', '15521089185', '596094244884713472', '1633736729@qq.com', '2019-07-01', 0, 0, '571347099191480320', '管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-07-04 13:51:34', 'admin', '2019-07-04 13:51:35', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES ('596307222997372928', '梁同学', '15521089185', NULL, '1633736729@qq.com', '2019-07-01', 0, 1, NULL, '梁同学', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-07-05 21:53:16', 'admin', '2019-07-05 21:53:17', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES ('596332387600830464', '林老师', '15521089185', NULL, '1633736729@qq.com', '2019-07-03', 0, 1, NULL, '林老师', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-07-04 13:34:28', 'admin', '2019-07-04 13:38:06', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user` VALUES ('597110984758398976', '白炽灯', NULL, '597110984758398977', NULL, NULL, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2019-07-06 17:05:52', NULL, NULL, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_user_auths
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_auths`;
CREATE TABLE `sys_user_auths`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `identity_type` int(11) NOT NULL COMMENT '登录类型，手机号、邮箱、用户名或第三方应用名称（微信 微博等）',
  `identifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识，手机号、邮箱、用户名或第三方应用的唯一标识',
  `credential` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码凭证，站内的保存密码，站外的不保存或保存token',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(11) NOT NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_identifier`(`identifier`) USING BTREE COMMENT '账号唯一标识'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_auths
-- ----------------------------
INSERT INTO `sys_user_auths` VALUES ('596329627606192128', '596078038307966976', 1, 'admin', '$2a$10$Lp3nTbBcPPcVCRNiRuqRteAzQfyjPzFWshT4ZyeTNeKMtPjzmPDHa', 'admin', '2019-07-04 13:21:02', 'admin', '2019-07-04 13:21:02', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES ('596329627648135168', '596307222997372928', 1, 'student', '$2a$10$czmVw4WF7Qt7RpwDJ4V4W.jkDKheEev63HlIsP31QnWHVOpSJz3au', 'admin', '2019-07-04 13:21:03', 'admin', '2019-07-04 13:21:03', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES ('596332387693105152', '596332387600830464', 1, 'teacher', '$2a$10$c9xRyRp3DXACdLgb/.yFR.5qSGIkGdP8o6WIcg4J9Hc6DY4FoUh0y', 'admin', '2019-07-04 13:32:01', 'admin', '2019-07-04 13:32:01', 0, 'EXAM', 'gitee');
INSERT INTO `sys_user_auths` VALUES ('597110984905199616', '597110984758398976', 4, 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '$2a$10$H8/Cm4fYNdcWjtfZkD58OOlUZHx81OTcLDnPDaLheJ.fGr.Xtj7OK', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 'ovLl45InRn4Hz_jrpEk-gF-kEFf8', '2019-07-06 17:05:53', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('596307898116739072', '596078038307966976', '596117256346406912');
INSERT INTO `sys_user_role` VALUES ('596333922548322304', '596332387600830464', '596330074307956736');
INSERT INTO `sys_user_role` VALUES ('596820924016234496', '596307222997372928', '596116511031169024');
INSERT INTO `sys_user_role` VALUES ('597110985244938240', '597110984758398976', '596116511031169024');

-- ----------------------------
-- Table structure for sys_user_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_student`;
CREATE TABLE `sys_user_student`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `student_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `relationship_type` int(11) NULL DEFAULT NULL COMMENT '关系类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户学生关联表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
