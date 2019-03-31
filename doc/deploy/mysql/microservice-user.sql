/*
 Navicat Premium Data Transfer

 Source Server         : 144
 Source Server Type    : MySQL
 Source Server Version : 50710
 Source Host           : 192.168.0.144:3306
 Source Schema         : microservice-user

 Target Server Type    : MySQL
 Target Server Version : 50710
 File Encoding         : 65001

 Date: 31/03/2019 15:16:38
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
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
INSERT INTO `sys_attachment` VALUES ('10dbd80892f744bea618136bc4a8af7b', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQer6Acs4vAAD-GneoEJo368.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:14:42', NULL, '2019-03-19 13:14:42', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('129ac1e3627f4e78aa0e7b7038258270', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/wKgAX1ySR9CAZrPBAAB2ngEqI1k658.jpg', NULL, NULL, '0', NULL, '2019-03-20 21:56:50', NULL, '2019-03-20 21:56:50', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('19a1ae447b2f4f2d92816028b14c296f', '简易.jpg', '30366', 'group1', 'group1/M00/00/00/wKgAX1yXnqmABxpuAAB2ngEqI1k023.jpg', NULL, NULL, '1', 'admin', '2019-03-24 23:08:38', 'admin', '2019-03-24 23:08:38', '0', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('207d0d519efd40c682331a1163e3a6dc', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQe72ARa01AAD-GneoEJo964.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:18:56', NULL, '2019-03-19 13:18:56', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('218048dfd45c44eea637fa15dd2cc744', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQe9SAdTyFAAD-GneoEJo170.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:19:19', NULL, '2019-03-19 13:19:19', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('472442834a334f68a41a9d2f0222e1bf', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQdtqATfkTAAD-GneoEJo916.jpg', NULL, NULL, '1', NULL, '2019-03-19 12:58:06', NULL, '2019-03-19 12:58:06', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('49c75cba7b234631bc173ecc5965c79d', '4.jpg', '11191', 'group1', 'group1/M00/00/00/wKgAX1yQfCSAIwgdAAArtzxOXJ4821.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:20:40', NULL, '2019-03-19 13:20:40', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('6479f07b602848999e7a006962f2265e', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQe0WAfUhbAAD-GneoEJo139.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:16:57', NULL, '2019-03-19 13:16:57', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('68c600923e0b4be394cafa184d14b52b', 'docker笔记.txt', '7755', 'group1', 'group1/M00/00/00/wKgAX1yQdd-AZjs6AAAeS9XckBo526.txt', NULL, NULL, '0', NULL, '2019-03-19 12:53:55', NULL, '2019-03-19 12:53:55', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('88e153571aef4ad5872747d725084f93', '4.jpg', '11191', 'group1', 'group1/M00/00/00/wKgAX1yPO1OAUlcsAAArtzxOXJ4407.jpg', NULL, NULL, '0', NULL, '2019-03-18 14:31:50', NULL, '2019-03-18 14:31:50', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('89ef923d02664d038cc613f8a9a77b4f', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/wKgAX1ySTT-AZC7YAADGq28ys0g424.jpg', NULL, NULL, '0', 'admin', '2019-03-20 22:20:01', 'admin', '2019-03-20 22:20:01', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('8bc09a183bd847fc8203268a994a199e', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQeXOAMOLPAAD-GneoEJo761.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:09:11', NULL, '2019-03-19 13:09:11', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('99e2f3d783e4449da47377a208c6eea5', 'header.jpg', '50859', 'group1', 'group1/M00/00/00/wKgAX1ySS02ACUt3AADGq28ys0g176.jpg', NULL, NULL, '0', NULL, '2019-03-20 22:11:43', NULL, '2019-03-20 22:11:43', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('bf29d5c50bb9486b8aa1ea408a7c2eee', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQdv2ABH5KAAD-GneoEJo357.jpg', NULL, NULL, '1', NULL, '2019-03-19 12:58:41', NULL, '2019-03-19 12:58:41', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('ca40d668997e42f4bf6bd9772bbe729b', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQfAWAe6-BAAD-GneoEJo971.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:20:08', NULL, '2019-03-19 13:20:08', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('e30dd7a33dc3463f818182679b6a8c1b', '4.jpg', '11191', 'group1', 'group1/M00/00/00/wKgAX1yQeiSAcbC6AAArtzxOXJ4854.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:12:08', NULL, '2019-03-19 13:12:08', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('e6d9c0f6371c4832b7fcbb9e816a6b9e', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQegSAEbrCAAD-GneoEJo244.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:11:36', NULL, '2019-03-19 13:11:36', '1', 'EXAM');
INSERT INTO `sys_attachment` VALUES ('ec5a65adfd0b4d239470905977dd506e', 'hdImg_624beb298a54895760501c95a368f2121539400955845.jpg', '65050', 'group1', 'group1/M00/00/00/wKgAX1yQgFCAdTuiAAD-GneoEJo490.jpg', NULL, NULL, '1', NULL, '2019-03-19 13:38:28', NULL, '2019-03-19 13:38:28', '1', 'EXAM');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父部门id',
  `sort` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序号',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 0-启用，1-禁用',
  `dept_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门描述',
  `dept_leader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门负责人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('83f8d25ddc93445fa91e8c9d3db750a0', '测试部门', '-1', '30', 'admin', '2019-02-25 13:40:40', 'admin', '2019-03-28 16:12:46', '0', 'EXAM', '0', '测试部门', '管理员');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `title` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志标题',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户的IP地址',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户代理信息',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的URI',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的方式',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作提交的数据',
  `exception` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  `service_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '耗时',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `create_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `modify_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向url',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('095bb0644ab14d97b31418f87e1cf823', '导出菜单', 'sys:menu:export', NULL, '3', '', '34', '1', 'admin', '2018-11-28 19:07:02', 'admin', '2018-11-28 19:07:02', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0dc80492cf414db984d825fdd842e022', '新增用户', 'sys:user:add', NULL, '4', 'example', '1', '1', '', '2018-10-28 16:38:57', NULL, '2019-03-20 21:54:33', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0ee02b8dc3064fcd972f527c31aad5a7', '修改菜单', 'sys:menu:edit', NULL, '3', 'example', '4', '1', '', '2018-10-28 16:46:38', 'admin', '2018-11-04 10:21:23', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0fe1156ec9e24dd4bc2c663c665a5048', '导出用户', 'sys:user:export', NULL, '4', '', '33', '1', 'admin', '2018-11-27 12:05:03', 'admin', '2018-11-27 12:05:03', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1', '系统管理', 'sys', '/api/user/v1/user/**', '-1', 'component', '1', '0', '1', '1', NULL, '2019-03-18 14:46:40', '0', '1', 'Layout', '/sys', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('13f925e8559c43aa8ef33a8e1e3f9b4d', '知识库', 'exam:knowledge', '/api/exam/v1/knowledge/**', 'b93eba1199b6420a82d285a8919bcd23', '', '5', '0', 'admin', '2019-01-01 14:55:31', NULL, '2019-03-18 14:47:51', '0', 'EXAM', 'views/exam/knowledge', 'knowledge', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('14', '个人管理', 'personal', '/admin/api/v1/personal/**', '-1', 'form', '30', '0', '', '2018-10-28 16:12:34', 'admin', '2019-03-06 14:10:22', '0', '', 'Layout', '/personal', NULL, '个人管理');
INSERT INTO `sys_menu` VALUES ('15', '附件管理', 'attachment', '/admin/api/v1/attachment/**', '-1', 'excel', '10', '0', 'admin', '2018-10-30 19:48:36', 'admin', '2019-03-06 14:10:18', '0', 'EXAM', 'Layout', '/attachment', NULL, '附件管理');
INSERT INTO `sys_menu` VALUES ('1717eabc03174c2e9bdaf27c5a5697dd', '题库管理', 'exam:subject', '/api/exam/v1/subject/**', 'b93eba1199b6420a82d285a8919bcd23', '', '3', '0', 'admin', '2018-12-04 21:33:40', NULL, '2019-03-18 14:47:40', '0', 'EXAM', 'views/exam/subject', 'subject', NULL, '题库管理');
INSERT INTO `sys_menu` VALUES ('1b68d41bfcc3441f839188a9d7b6ead0', '接口文档', 'monitor:document', '/monitor/document/**', '7', '', '34', '0', 'admin', '2018-10-30 15:06:08', 'admin', '2019-03-07 09:27:47', '0', 'EXAM', '', 'http://localhost:8000/swagger-ui.html', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1e6a90e57df541e0973691c17d44564c', '日志监控', 'monitor:log', '/admin/api/v1/log/**', '7', '', '30', '0', 'admin', '2018-10-30 15:00:25', 'admin', '2019-03-06 14:11:03', '0', 'EXAM', 'views/monitor/log', 'log', NULL, '日志监控');
INSERT INTO `sys_menu` VALUES ('23df3c2475504ca781e25c3443d7ad25', '修改课程', 'exam:course:edit', NULL, 'b8969a3731b0405e82d0bb896e13841e', '', '2', '1', 'admin', '2018-11-10 22:44:28', 'admin', '2018-11-10 22:44:28', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2a232ac9f43146a49ab5a19226e76742', '删除部门', 'sys:dept:del', NULL, '6', 'example', '3', '1', '', '2018-10-28 16:43:19', 'admin', '2018-11-04 10:20:22', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2ac8ccadf70140edbed65c8c1bd05e2c', '新增终端', 'sys:client:add', NULL, 'a6ee113d593448e2a63a9d9712d9a81e', '', '1', '1', 'admin', '2019-03-30 22:32:37', 'admin', '2019-03-30 22:32:37', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3', '菜单管理', 'sys:menu', '/api/user/v1/menu/**', '1', '', '10', '0', '1', '1', NULL, '2019-03-18 14:47:10', '0', '1', 'views/sys/menu', 'menu', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('34371d1e990549f0b633389bdf64ce0f', '修改题目分类', 'exam:subject:category:edit', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '5', '1', 'admin', '2018-12-04 21:36:34', 'admin', '2018-12-04 21:36:34', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('347d24c6e1cf42eaa976c91f5607007a', '新增部门', 'sys:dept:add', NULL, '6', 'example', '1', '1', '', '2018-10-28 16:42:40', 'admin', '2018-11-04 10:20:15', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('34ffa24d3c124902893e601fe8e22b08', 'consul监控', 'monitor:service', '/admin/monitor/service/**', '7', '', '31', '0', 'admin', '2018-10-30 15:01:17', NULL, '2019-03-18 14:45:48', '0', 'EXAM', '', 'http://localhost:8500/', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('36cca77232f3487cbee02bb68ae12652', '新增菜单', 'sys:menu:add', NULL, '3', 'example', '1', '1', '', '2018-10-28 16:45:45', 'admin', '2018-11-04 10:21:06', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3a64f8a80dce4f6c8bc4483f0230f49f', '导入用户', 'sys:user:import', NULL, '4', '', '34', '1', 'admin', '2018-11-27 12:05:29', 'admin', '2018-11-27 12:05:29', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3bb2fec1ba094584aa1a984ec1f05dc7', '删除课程', 'exam:course:del', NULL, 'b8969a3731b0405e82d0bb896e13841e', '', '3', '1', 'admin', '2018-11-10 22:44:53', 'admin', '2018-11-10 22:44:53', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('4', '用户管理', 'sys:user', '/admin/api/v1/user/**', '1', '', '2', '0', '1', '1', 'admin', '2019-03-06 14:12:50', '0', '1', 'views/sys/user', 'user', '', '用户管理');
INSERT INTO `sys_menu` VALUES ('42c69128d30a4242b08ef0003da68528', '修改角色', 'sys:role:edit', NULL, '5', 'example', '4', '1', '', '2018-10-28 16:45:11', 'admin', '2018-11-04 10:19:45', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('451605025d9a4715b4ae78f5a5d01fea', '删除题目分类', 'exam:subject:category:del', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '6', '1', 'admin', '2018-12-04 21:36:55', 'admin', '2018-12-04 21:36:55', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('4f3e874dc310463a82e3b650fd851fdb', '修改密码', 'personal:password', '/admin/api/v1/user/updateInfo', '14', '', '30', '0', 'admin', '2018-10-29 21:59:03', 'admin', '2019-03-06 14:10:38', '0', 'EXAM', 'views/personal/password', 'password', NULL, '密码修改');
INSERT INTO `sys_menu` VALUES ('5', '角色管理', 'sys:role', '/api/user/v1/role/**', '1', '', '9', '0', '1', '1', NULL, '2019-03-18 14:46:59', '0', '1', 'views/sys/role', 'role', '', NULL);
INSERT INTO `sys_menu` VALUES ('530f933da3824e1f9bf3182794141e9e', '删除题目', 'exam:subject:bank:del', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '3', '1', 'admin', '2018-12-04 21:35:13', 'admin', '2018-12-09 20:36:05', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('594e7afda95c42e6af2046f1bfe81c53', '删除日志', 'monitor:log:del', NULL, '1e6a90e57df541e0973691c17d44564c', '', '30', '1', 'admin', '2019-01-07 22:15:02', 'admin', '2019-01-07 22:15:02', '0', 'EXAM', NULL, NULL, NULL, '删除日志');
INSERT INTO `sys_menu` VALUES ('5ba624643cd34ec3b78ca622964c0f8a', '修改题目', 'exam:subject:bank:edit', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '2', '1', 'admin', '2018-12-04 21:34:50', 'admin', '2018-12-09 20:35:58', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6', '部门管理', 'sys:dept', '/api/user/v1/dept/**', '1', '', '8', '0', '1', '1', NULL, '2019-03-18 14:46:50', '0', '1', 'views/sys/dept', 'dept', '', NULL);
INSERT INTO `sys_menu` VALUES ('63f039ea5bcf4208978150b59484a429', '考试管理', 'exam:exam', '/api/exam/v1/examination/**', 'b93eba1199b6420a82d285a8919bcd23', '', '2', '0', 'admin', '2018-11-10 22:22:42', NULL, '2019-03-18 14:47:34', '0', 'EXAM', 'views/exam/exam', 'exam', NULL, '考试管理');
INSERT INTO `sys_menu` VALUES ('657026922f494801a41b64f40e63fca6', '删除考试', 'exam:exam:del', NULL, '63f039ea5bcf4208978150b59484a429', '', '3', '1', 'admin', '2018-11-10 22:46:12', 'admin', '2018-11-10 22:46:12', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('69a2a85608064762a3d76fc0c92072cc', '新增题目分类', 'exam:subject:category:add', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '4', '1', 'admin', '2018-12-04 21:35:59', 'admin', '2018-12-04 21:35:59', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6f605148282b4949b5c96e2877dc9052', '题目管理', 'exam:exam:subject', NULL, '63f039ea5bcf4208978150b59484a429', '', '4', '1', 'admin', '2018-11-13 20:50:07', 'admin', '2018-11-13 20:50:41', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7', '系统监控', 'sys', '/admin/monitor/**', '-1', 'chart', '7', '0', '1', '1', 'admin', '2019-01-05 16:19:56', '0', '1', 'Layout', '/monitor', '', NULL);
INSERT INTO `sys_menu` VALUES ('71e5179363bc4e119a87daaa631a2712', '导入题目', 'exam:exam:subject:import', NULL, '63f039ea5bcf4208978150b59484a429', '', '36', '1', 'admin', '2018-11-27 12:06:45', 'admin', '2018-11-27 12:06:45', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('72de30896d3a401eb62edc0aa6fbf190', '导出成绩', 'exam:examRecord:export', NULL, 'c3adad9112de41a6a2d4cc9fe4a4d94b', '', '30', '1', 'admin', '2018-12-30 22:12:20', 'admin', '2019-01-22 19:43:52', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7780e3fd6cea4ba78d780f33c111d95a', '导入菜单', 'sys:menu:import', NULL, '3', '', '35', '1', 'admin', '2018-11-28 19:07:20', 'admin', '2018-11-28 19:07:20', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7f78172c09d3408dab1534d26b608b31', '首页', 'dashboard', '/', '-1', 'dashboard', '0', '0', 'admin', '2018-11-06 23:26:57', 'admin', '2019-03-28 15:59:52', '0', 'EXAM', '', '/dashboard', '', '首页');
INSERT INTO `sys_menu` VALUES ('8aefee22294d47d7a3e4a29ae5ced4b4', '个人资料', 'personal:message', '/admin/api/v1/user/updateInfo', '14', '', '29', '0', 'admin', '2018-10-29 21:58:32', 'admin', '2019-03-06 14:10:33', '0', 'EXAM', 'views/personal/message', 'message', NULL, '个人资料');
INSERT INTO `sys_menu` VALUES ('8b67ccbe89f74b728e58c2e4a4795027', '删除菜单', 'sys:menu:del', NULL, '3', 'example', '3', '1', '', '2018-10-28 16:46:23', 'admin', '2018-11-04 10:21:14', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8bcf03f73377412b981572517b9055e0', '删除题目', 'exam:exam:subject:del', NULL, '63f039ea5bcf4208978150b59484a429', '', '38', '1', 'admin', '2018-11-29 14:11:24', 'admin', '2018-11-29 14:11:24', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('901959fd37df4f7d90adaa4ab6c4b331', '分配权限', 'sys:role:auth', NULL, '5', '', '30', '1', 'admin', '2018-11-04 10:19:32', 'admin', '2018-11-04 10:19:32', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('91861ef795ab4fc4a207567606fa62cc', '修改用户', 'sys:user:edit', NULL, '4', 'example', '4', '1', '', '2018-10-28 16:40:19', 'admin', '2018-11-04 10:20:54', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9c0846685bb24aafae731bdacf879ba2', '删除角色', 'sys:role:del', NULL, '5', 'example', '3', '1', '', '2018-10-28 16:44:56', 'admin', '2018-11-04 10:20:01', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9c2e04eab32c467f87d89ad0a2b4892c', '导出题目', 'exam:subject:bank:export', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '32', '1', 'admin', '2018-12-09 20:38:06', 'admin', '2018-12-09 20:38:06', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a398216ac2f14c16928452483786329e', '新增题目', 'exam:subject:bank:add', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '1', '1', 'admin', '2018-12-04 21:34:29', 'admin', '2018-12-09 20:35:18', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a663e71f7b8441b0b8363ae5eb20bbb3', '修改部门', 'sys:dept:edit', NULL, '6', 'example', '4', '1', '', '2018-10-28 16:43:38', 'admin', '2018-11-04 10:20:30', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a6ee113d593448e2a63a9d9712d9a81e', '终端管理', 'sys:client', '/api/user/v1/client/**', '1', '', '11', '0', 'admin', '2019-03-30 22:31:47', 'admin', '2019-03-30 22:31:47', '0', 'EXAM', 'views/sys/client', 'client', NULL, '终端管理');
INSERT INTO `sys_menu` VALUES ('ac6768a097184c99ada64810a897f727', '新增考试', 'exam:exam:add', NULL, '63f039ea5bcf4208978150b59484a429', '', '1', '1', 'admin', '2018-11-10 22:45:29', 'admin', '2018-11-10 22:45:29', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('b85dda1e623e48e4ae82dc228df3edfe', '新增课程', 'exam:course:add', NULL, 'b8969a3731b0405e82d0bb896e13841e', '', '1', '1', 'admin', '2018-11-10 22:44:05', 'admin', '2018-11-10 22:44:05', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('b8969a3731b0405e82d0bb896e13841e', '课程管理', 'exam:course', '/api/exam/v1/course/**', 'b93eba1199b6420a82d285a8919bcd23', '', '1', '0', 'admin', '2018-11-10 22:21:58', NULL, '2019-03-18 14:47:28', '0', 'EXAM', 'views/exam/course', 'course', NULL, '课程管理');
INSERT INTO `sys_menu` VALUES ('b93eba1199b6420a82d285a8919bcd23', '考务管理', 'exam', '/exam/**', '-1', 'nested', '8', '0', 'admin', '2018-11-10 22:20:10', 'admin', '2018-11-10 22:20:10', '0', 'EXAM', 'Layout', '/exam', NULL, '考务管理');
INSERT INTO `sys_menu` VALUES ('c2bc24819bcc4e8790f0dba586914efe', '服务监控', 'monitor:admin', '/admin/monitor/admin/**', '7', '', '33', '0', 'admin', '2019-01-08 20:38:05', 'admin', '2019-03-30 14:27:39', '0', 'EXAM', NULL, 'http://localhost:8085/admin', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c3adad9112de41a6a2d4cc9fe4a4d94b', '成绩管理', 'exam:examRecord', '/api/exam/v1/examRecord/**', 'b93eba1199b6420a82d285a8919bcd23', '', '4', '0', 'admin', '2018-12-30 22:10:53', NULL, '2019-03-18 14:47:46', '0', 'EXAM', 'views/exam/examRecord', 'score', NULL, '成绩管理');
INSERT INTO `sys_menu` VALUES ('c435ac944cd6430ba9a1039d8adb80a7', '新增角色', 'sys:role:add', NULL, '5', 'example', '1', '1', '', '2018-10-28 16:44:29', 'admin', '2018-11-04 10:19:53', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ca168a2d7c854320b207b7a7ea67eb5d', '修改终端', 'sys:client:edit', NULL, 'a6ee113d593448e2a63a9d9712d9a81e', '', '3', '1', 'admin', '2019-03-30 22:33:27', 'admin', '2019-03-30 22:33:27', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ca7e69aae4994099a6424aa9727b6a28', '删除用户', 'sys:user:del', NULL, '4', 'example', '3', '1', '', '2018-10-28 16:40:01', 'admin', '2018-11-04 10:20:46', '0', '', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('cfc631763d3e4f1ab973141ddbeee449', '修改考试', 'exam:exam:edit', NULL, '63f039ea5bcf4208978150b59484a429', '', '2', '1', 'admin', '2018-11-10 22:45:51', 'admin', '2018-11-10 22:45:51', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('cffa2058b7c746efa2fca7ceb6052bdc', '新增题目', 'exam:exam:subject:add', NULL, '63f039ea5bcf4208978150b59484a429', '', '37', '1', 'admin', '2018-11-29 14:11:06', 'admin', '2018-11-29 14:11:06', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d1967064f3584672af29c184818e38a9', '导入题目', 'exam:subject:bank:import', NULL, '1717eabc03174c2e9bdaf27c5a5697dd', '', '31', '1', 'admin', '2018-12-09 20:37:22', 'admin', '2018-12-09 20:37:22', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ecb3ebcb68b54e06bd29f270a97cbb67', '删除终端', 'sys:client:del', NULL, 'a6ee113d593448e2a63a9d9712d9a81e', '', '2', '1', 'admin', '2019-03-30 22:32:59', 'admin', '2019-03-30 22:32:59', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ee35a2abc0b04f3bb70527a7f79806e8', 'zipkin监控', 'monitor:link', '/admin/monitor/link/**', '7', '', '32', '0', 'admin', '2018-11-07 20:30:43', 'admin', '2019-01-08 20:36:38', '0', 'EXAM', NULL, 'http://localhost:9411/zipkin/', NULL, NULL);
INSERT INTO `sys_menu` VALUES ('fa483765360243d0a631a2b9793aaf41', '导出题目', 'exam:exam:subject:export', NULL, '63f039ea5bcf4208978150b59484a429', '', '35', '1', 'admin', '2018-11-27 12:06:17', 'admin', '2018-11-27 12:06:17', '0', 'EXAM', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('fe73699236be4b148cd35628929cc876', '附件列表', 'attachment:list', '/admin/api/v1/attachment/list', '15', '', '30', '0', 'admin', '2018-10-30 19:49:50', 'admin', '2019-03-06 14:10:45', '0', 'EXAM', 'views/attachment/list', 'list', NULL, '附件列表');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编号',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_default` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 0-启用，1-禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('23ab268d7e0247868dcf484ab63ff595', '普通用户', 'role_user', '普通用户', '1', 'admin', '2019-02-25 13:43:15', 'admin', '2019-02-25 13:43:15', '0', 'EXAM', '0');
INSERT INTO `sys_role` VALUES ('59fcb8500eb24f20bf7263467d757212', '管理员', 'role_admin', '管理员', '0', 'admin', '2019-02-25 13:41:27', 'admin', '2019-03-06 13:47:31', '0', 'EXAM', '0');
INSERT INTO `sys_role` VALUES ('fce0ad963d1f42be9178c4c5c493f55e', '教师', 'role_teacher', '教师', '0', 'admin', '2019-02-25 13:42:29', 'admin', '2019-02-25 13:42:29', '0', 'EXAM', '0');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES ('b9d76015d54e411393465395b8254c6b', 'fce0ad963d1f42be9178c4c5c493f55e', '83f8d25ddc93445fa91e8c9d3db750a0');
INSERT INTO `sys_role_dept` VALUES ('bd6f5a3aaecd4f1b8430c3f11f3e3ee6', '23ab268d7e0247868dcf484ab63ff595', '83f8d25ddc93445fa91e8c9d3db750a0');
INSERT INTO `sys_role_dept` VALUES ('da61e277cf4e46e9b7b9b125f26a22d3', '59fcb8500eb24f20bf7263467d757212', '83f8d25ddc93445fa91e8c9d3db750a0');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `menu_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('00fb7f39f1904de0a8f814e941810628', '59fcb8500eb24f20bf7263467d757212', '0fe1156ec9e24dd4bc2c663c665a5048');
INSERT INTO `sys_role_menu` VALUES ('0174bfbc9a3c49cb8cbc662533891a40', '59fcb8500eb24f20bf7263467d757212', '2a232ac9f43146a49ab5a19226e76742');
INSERT INTO `sys_role_menu` VALUES ('037ea7d36a294986a5896e1f9d8a83fb', '23ab268d7e0247868dcf484ab63ff595', '8aefee22294d47d7a3e4a29ae5ced4b4');
INSERT INTO `sys_role_menu` VALUES ('05aedfd986dc47d49da7a77d99d5eb8e', '59fcb8500eb24f20bf7263467d757212', 'ac6768a097184c99ada64810a897f727');
INSERT INTO `sys_role_menu` VALUES ('06e3edb0e5c94d039e6ddc57c25739c1', '59fcb8500eb24f20bf7263467d757212', '13f925e8559c43aa8ef33a8e1e3f9b4d');
INSERT INTO `sys_role_menu` VALUES ('078bb68faae94ae49c919a33b8ce6ed8', '59fcb8500eb24f20bf7263467d757212', '23df3c2475504ca781e25c3443d7ad25');
INSERT INTO `sys_role_menu` VALUES ('0f965e0dc620461e95e908e904e75034', '59fcb8500eb24f20bf7263467d757212', '1b68d41bfcc3441f839188a9d7b6ead0');
INSERT INTO `sys_role_menu` VALUES ('10a12ee807744e98a7401dea56c87250', 'fce0ad963d1f42be9178c4c5c493f55e', '8aefee22294d47d7a3e4a29ae5ced4b4');
INSERT INTO `sys_role_menu` VALUES ('12f1b54b5b0f41cf839e56db6f491ab9', '23ab268d7e0247868dcf484ab63ff595', '7f78172c09d3408dab1534d26b608b31');
INSERT INTO `sys_role_menu` VALUES ('15de309b6e8247c492ee378d529782ec', '59fcb8500eb24f20bf7263467d757212', '7780e3fd6cea4ba78d780f33c111d95a');
INSERT INTO `sys_role_menu` VALUES ('1634529946a74d1b84a5bcc884e52222', '59fcb8500eb24f20bf7263467d757212', '91861ef795ab4fc4a207567606fa62cc');
INSERT INTO `sys_role_menu` VALUES ('164966101805422ca5bd801b735305bf', 'fce0ad963d1f42be9178c4c5c493f55e', '63f039ea5bcf4208978150b59484a429');
INSERT INTO `sys_role_menu` VALUES ('1705876c9b224bc6acfdff152ffa9506', 'fce0ad963d1f42be9178c4c5c493f55e', '69a2a85608064762a3d76fc0c92072cc');
INSERT INTO `sys_role_menu` VALUES ('1a7209a0c2b24e7faf1b38426cea9dc8', 'fce0ad963d1f42be9178c4c5c493f55e', '34371d1e990549f0b633389bdf64ce0f');
INSERT INTO `sys_role_menu` VALUES ('1c34460b23ad4ad98892dfb510e191d2', '59fcb8500eb24f20bf7263467d757212', '901959fd37df4f7d90adaa4ab6c4b331');
INSERT INTO `sys_role_menu` VALUES ('224b38adb44f4ffb9b792a3780ad4a67', '59fcb8500eb24f20bf7263467d757212', '7');
INSERT INTO `sys_role_menu` VALUES ('25928e403495414ea04d3cb6609f886c', '59fcb8500eb24f20bf7263467d757212', '42c69128d30a4242b08ef0003da68528');
INSERT INTO `sys_role_menu` VALUES ('281d59486c284c359148f3c63af5fd92', '23ab268d7e0247868dcf484ab63ff595', 'b93eba1199b6420a82d285a8919bcd23');
INSERT INTO `sys_role_menu` VALUES ('2b3ddae30b3c4df799c0c036c22cb8d6', 'fce0ad963d1f42be9178c4c5c493f55e', '72de30896d3a401eb62edc0aa6fbf190');
INSERT INTO `sys_role_menu` VALUES ('2b77915c9d8e48a587d32c85af59f2fa', '59fcb8500eb24f20bf7263467d757212', 'b93eba1199b6420a82d285a8919bcd23');
INSERT INTO `sys_role_menu` VALUES ('2c0c6096bfc84605a1c803b8d08f33e9', 'fce0ad963d1f42be9178c4c5c493f55e', 'ac6768a097184c99ada64810a897f727');
INSERT INTO `sys_role_menu` VALUES ('2c87e444edd94d95bb902c3aea4ea1a7', 'fce0ad963d1f42be9178c4c5c493f55e', '13f925e8559c43aa8ef33a8e1e3f9b4d');
INSERT INTO `sys_role_menu` VALUES ('2e1bb1a5fcd94c93b482333894a6b697', '59fcb8500eb24f20bf7263467d757212', '530f933da3824e1f9bf3182794141e9e');
INSERT INTO `sys_role_menu` VALUES ('2ed5e41dff8548e199e689f1a4c590e1', '59fcb8500eb24f20bf7263467d757212', 'b8969a3731b0405e82d0bb896e13841e');
INSERT INTO `sys_role_menu` VALUES ('2fb311360a984fba832211c39c7e92db', '59fcb8500eb24f20bf7263467d757212', 'd1967064f3584672af29c184818e38a9');
INSERT INTO `sys_role_menu` VALUES ('35815a91ebe14eeba186ff2295f4d758', '59fcb8500eb24f20bf7263467d757212', '1717eabc03174c2e9bdaf27c5a5697dd');
INSERT INTO `sys_role_menu` VALUES ('35e38527b4934ddaa80b4c87649d8914', '23ab268d7e0247868dcf484ab63ff595', '14');
INSERT INTO `sys_role_menu` VALUES ('36d7be7b66564edca3e02e0e0748bd7c', '59fcb8500eb24f20bf7263467d757212', '69a2a85608064762a3d76fc0c92072cc');
INSERT INTO `sys_role_menu` VALUES ('393dce49aa9b4174ae68ca16aa804d31', '59fcb8500eb24f20bf7263467d757212', '6f605148282b4949b5c96e2877dc9052');
INSERT INTO `sys_role_menu` VALUES ('39a9488b90e5403fb7d31162e67299d9', '23ab268d7e0247868dcf484ab63ff595', '63f039ea5bcf4208978150b59484a429');
INSERT INTO `sys_role_menu` VALUES ('40e33021e9c54420942744813d079daa', '59fcb8500eb24f20bf7263467d757212', 'a663e71f7b8441b0b8363ae5eb20bbb3');
INSERT INTO `sys_role_menu` VALUES ('41158e7ab6ef4707872304cecb1ac332', '59fcb8500eb24f20bf7263467d757212', 'b85dda1e623e48e4ae82dc228df3edfe');
INSERT INTO `sys_role_menu` VALUES ('4198329687684550aa78db44947cde5c', 'fce0ad963d1f42be9178c4c5c493f55e', 'cfc631763d3e4f1ab973141ddbeee449');
INSERT INTO `sys_role_menu` VALUES ('4258bdc8026f4e86ba5902bb15f10073', '59fcb8500eb24f20bf7263467d757212', '7f78172c09d3408dab1534d26b608b31');
INSERT INTO `sys_role_menu` VALUES ('43b1561644a54a708d9dbc21cb99321a', '59fcb8500eb24f20bf7263467d757212', '14');
INSERT INTO `sys_role_menu` VALUES ('43f2d49511ac4129bc9f22689d85dfd6', 'fce0ad963d1f42be9178c4c5c493f55e', '530f933da3824e1f9bf3182794141e9e');
INSERT INTO `sys_role_menu` VALUES ('4460783765cb4beba3781f8b850e1f65', 'fce0ad963d1f42be9178c4c5c493f55e', 'a398216ac2f14c16928452483786329e');
INSERT INTO `sys_role_menu` VALUES ('4af4db6a479b4f0ba576874c1b047b75', 'fce0ad963d1f42be9178c4c5c493f55e', '1717eabc03174c2e9bdaf27c5a5697dd');
INSERT INTO `sys_role_menu` VALUES ('4c39e95a8b054d4fb69ab842d83f01d2', 'fce0ad963d1f42be9178c4c5c493f55e', '23df3c2475504ca781e25c3443d7ad25');
INSERT INTO `sys_role_menu` VALUES ('4c8b5623dd164ef591cf9fe6811d1ad1', 'fce0ad963d1f42be9178c4c5c493f55e', '8bcf03f73377412b981572517b9055e0');
INSERT INTO `sys_role_menu` VALUES ('4ed7f391d2ea4c189e1634c94f9afe65', '59fcb8500eb24f20bf7263467d757212', '8b67ccbe89f74b728e58c2e4a4795027');
INSERT INTO `sys_role_menu` VALUES ('4f4eb31a74e046d7bad9c6b705350abc', '23ab268d7e0247868dcf484ab63ff595', '13f925e8559c43aa8ef33a8e1e3f9b4d');
INSERT INTO `sys_role_menu` VALUES ('53b61a2e5e95406ab786cf14b550368e', '59fcb8500eb24f20bf7263467d757212', '8aefee22294d47d7a3e4a29ae5ced4b4');
INSERT INTO `sys_role_menu` VALUES ('54cf5fb79114447fbeef10812e49f43e', '59fcb8500eb24f20bf7263467d757212', 'c2bc24819bcc4e8790f0dba586914efe');
INSERT INTO `sys_role_menu` VALUES ('56f2fd8fc31841999f1528db759a6f0b', '59fcb8500eb24f20bf7263467d757212', '0dc80492cf414db984d825fdd842e022');
INSERT INTO `sys_role_menu` VALUES ('59e7f69573044ad1b5f184b11107ec6b', '59fcb8500eb24f20bf7263467d757212', 'ee35a2abc0b04f3bb70527a7f79806e8');
INSERT INTO `sys_role_menu` VALUES ('5e254b4dd683430eb1b07443d3633c09', '23ab268d7e0247868dcf484ab63ff595', 'b8969a3731b0405e82d0bb896e13841e');
INSERT INTO `sys_role_menu` VALUES ('60586550779c4bb4813404e0065e926e', '59fcb8500eb24f20bf7263467d757212', '34371d1e990549f0b633389bdf64ce0f');
INSERT INTO `sys_role_menu` VALUES ('610efea9c2b24b57b55282eaa5ab7207', '59fcb8500eb24f20bf7263467d757212', 'ca7e69aae4994099a6424aa9727b6a28');
INSERT INTO `sys_role_menu` VALUES ('6274cb2b74c3462687f1297d3333ab42', 'fce0ad963d1f42be9178c4c5c493f55e', 'cffa2058b7c746efa2fca7ceb6052bdc');
INSERT INTO `sys_role_menu` VALUES ('6385b189f2454aefbc9ee17fdf6742bc', 'fce0ad963d1f42be9178c4c5c493f55e', '4f3e874dc310463a82e3b650fd851fdb');
INSERT INTO `sys_role_menu` VALUES ('66c95f9bdbff44c9bed689d617c06e9e', '23ab268d7e0247868dcf484ab63ff595', '1717eabc03174c2e9bdaf27c5a5697dd');
INSERT INTO `sys_role_menu` VALUES ('6b0737f1e3204282a389eab633c65cb0', '59fcb8500eb24f20bf7263467d757212', '9c2e04eab32c467f87d89ad0a2b4892c');
INSERT INTO `sys_role_menu` VALUES ('6cc8b627612b4fcd99e220cd99be99d3', '59fcb8500eb24f20bf7263467d757212', '1');
INSERT INTO `sys_role_menu` VALUES ('6f4da45ca1094f1cba704fa2519fc4fb', 'fce0ad963d1f42be9178c4c5c493f55e', 'c3adad9112de41a6a2d4cc9fe4a4d94b');
INSERT INTO `sys_role_menu` VALUES ('6ffdbf889d7a4f5b92318494d8c55488', '59fcb8500eb24f20bf7263467d757212', '4');
INSERT INTO `sys_role_menu` VALUES ('71c5d297bf1e4f72a1a04173ceaaf48b', '59fcb8500eb24f20bf7263467d757212', '594e7afda95c42e6af2046f1bfe81c53');
INSERT INTO `sys_role_menu` VALUES ('745cb4fd48444233bbbd8448f313ef35', '59fcb8500eb24f20bf7263467d757212', 'cfc631763d3e4f1ab973141ddbeee449');
INSERT INTO `sys_role_menu` VALUES ('7e779baa30854519819ed3e2fd9d5bfc', '59fcb8500eb24f20bf7263467d757212', '15');
INSERT INTO `sys_role_menu` VALUES ('85c84c45e9fd4991b3b1f5e47ab024fc', '59fcb8500eb24f20bf7263467d757212', '3bb2fec1ba094584aa1a984ec1f05dc7');
INSERT INTO `sys_role_menu` VALUES ('87f041f70c3845f3a014f977f8be50e0', '59fcb8500eb24f20bf7263467d757212', '657026922f494801a41b64f40e63fca6');
INSERT INTO `sys_role_menu` VALUES ('8805ee782529431e94aa314df4deb959', 'fce0ad963d1f42be9178c4c5c493f55e', '451605025d9a4715b4ae78f5a5d01fea');
INSERT INTO `sys_role_menu` VALUES ('88071d3cbb994a0c8b6eeba198624737', 'fce0ad963d1f42be9178c4c5c493f55e', 'fa483765360243d0a631a2b9793aaf41');
INSERT INTO `sys_role_menu` VALUES ('894cc05c35344c6f9b58f27570f718b6', '59fcb8500eb24f20bf7263467d757212', 'fe73699236be4b148cd35628929cc876');
INSERT INTO `sys_role_menu` VALUES ('8aae120a470641acab06f3f7f98222ea', '59fcb8500eb24f20bf7263467d757212', '8bcf03f73377412b981572517b9055e0');
INSERT INTO `sys_role_menu` VALUES ('90f4318eb5b04bad85ec2eddad195635', 'fce0ad963d1f42be9178c4c5c493f55e', '3bb2fec1ba094584aa1a984ec1f05dc7');
INSERT INTO `sys_role_menu` VALUES ('933c016eaf4c48189ae7fdb1c7b63312', '59fcb8500eb24f20bf7263467d757212', '1e6a90e57df541e0973691c17d44564c');
INSERT INTO `sys_role_menu` VALUES ('9b53f1bab5ab4aab80e818d638b23c30', '59fcb8500eb24f20bf7263467d757212', '2ac8ccadf70140edbed65c8c1bd05e2c');
INSERT INTO `sys_role_menu` VALUES ('9df8f86a41ce46869436e3b87d0d687c', '59fcb8500eb24f20bf7263467d757212', '095bb0644ab14d97b31418f87e1cf823');
INSERT INTO `sys_role_menu` VALUES ('a963e469c99d48f28493aca9ea16b87e', 'fce0ad963d1f42be9178c4c5c493f55e', 'b8969a3731b0405e82d0bb896e13841e');
INSERT INTO `sys_role_menu` VALUES ('a99bbdc5a50a42bd9776b6b1c685bef5', '59fcb8500eb24f20bf7263467d757212', '36cca77232f3487cbee02bb68ae12652');
INSERT INTO `sys_role_menu` VALUES ('ac03e93df05449c58809b201a545d18d', 'fce0ad963d1f42be9178c4c5c493f55e', '9c2e04eab32c467f87d89ad0a2b4892c');
INSERT INTO `sys_role_menu` VALUES ('b5c89c4bb84b4ac593d410c0d993195f', '23ab268d7e0247868dcf484ab63ff595', '4f3e874dc310463a82e3b650fd851fdb');
INSERT INTO `sys_role_menu` VALUES ('b6f2aeb27d244b20abced05b12991f38', '59fcb8500eb24f20bf7263467d757212', '71e5179363bc4e119a87daaa631a2712');
INSERT INTO `sys_role_menu` VALUES ('b707d0b541784438a6666e6f022d7a79', 'fce0ad963d1f42be9178c4c5c493f55e', 'b85dda1e623e48e4ae82dc228df3edfe');
INSERT INTO `sys_role_menu` VALUES ('b7bd80aaa0bb481b8c04cee2b484aff4', '59fcb8500eb24f20bf7263467d757212', 'c435ac944cd6430ba9a1039d8adb80a7');
INSERT INTO `sys_role_menu` VALUES ('ba6dfab783d24a18a08d130fc4ff1470', 'fce0ad963d1f42be9178c4c5c493f55e', 'd1967064f3584672af29c184818e38a9');
INSERT INTO `sys_role_menu` VALUES ('bebbf99836b241e7822a076a12263482', '59fcb8500eb24f20bf7263467d757212', '72de30896d3a401eb62edc0aa6fbf190');
INSERT INTO `sys_role_menu` VALUES ('bec6832fc0a3460aa1f9d8b4b1857031', '59fcb8500eb24f20bf7263467d757212', 'a398216ac2f14c16928452483786329e');
INSERT INTO `sys_role_menu` VALUES ('bf8d6f3bc9ed469299113c74ec414e25', 'fce0ad963d1f42be9178c4c5c493f55e', '5ba624643cd34ec3b78ca622964c0f8a');
INSERT INTO `sys_role_menu` VALUES ('c3bc5eda1c054d1e90db02b7dc7c9ee2', '59fcb8500eb24f20bf7263467d757212', 'cffa2058b7c746efa2fca7ceb6052bdc');
INSERT INTO `sys_role_menu` VALUES ('c57887944ac2403e9f63537a9f63abb8', '59fcb8500eb24f20bf7263467d757212', 'a6ee113d593448e2a63a9d9712d9a81e');
INSERT INTO `sys_role_menu` VALUES ('c716281f370a4cd18e0a2e6a5a385faa', 'fce0ad963d1f42be9178c4c5c493f55e', '657026922f494801a41b64f40e63fca6');
INSERT INTO `sys_role_menu` VALUES ('c74f1f256f8b426a8cb80db6bd592308', '59fcb8500eb24f20bf7263467d757212', 'ca168a2d7c854320b207b7a7ea67eb5d');
INSERT INTO `sys_role_menu` VALUES ('c839e32fdae243ee9b4d0f129e5c89f6', '59fcb8500eb24f20bf7263467d757212', '4f3e874dc310463a82e3b650fd851fdb');
INSERT INTO `sys_role_menu` VALUES ('c9ec768e1d3a41e29bb22355fa2f7323', '59fcb8500eb24f20bf7263467d757212', '0ee02b8dc3064fcd972f527c31aad5a7');
INSERT INTO `sys_role_menu` VALUES ('ca0d753cc21a44c79dc742eb69a3231a', '59fcb8500eb24f20bf7263467d757212', '5ba624643cd34ec3b78ca622964c0f8a');
INSERT INTO `sys_role_menu` VALUES ('ca33cc44d9ea4a2590f109c1ac3808c4', '59fcb8500eb24f20bf7263467d757212', '6');
INSERT INTO `sys_role_menu` VALUES ('cabcefbd441648b1b6ee43cabef9508b', '23ab268d7e0247868dcf484ab63ff595', 'c3adad9112de41a6a2d4cc9fe4a4d94b');
INSERT INTO `sys_role_menu` VALUES ('cad015554cb848829328fb707cd6df90', '59fcb8500eb24f20bf7263467d757212', '3a64f8a80dce4f6c8bc4483f0230f49f');
INSERT INTO `sys_role_menu` VALUES ('cb92f317fb134cd7b571e2594c1aefee', '59fcb8500eb24f20bf7263467d757212', '347d24c6e1cf42eaa976c91f5607007a');
INSERT INTO `sys_role_menu` VALUES ('cec6baac80f44624a725c9c09801f342', '59fcb8500eb24f20bf7263467d757212', 'c3adad9112de41a6a2d4cc9fe4a4d94b');
INSERT INTO `sys_role_menu` VALUES ('d3bda633963c413ca9dbcb0f9fc1193a', '59fcb8500eb24f20bf7263467d757212', '451605025d9a4715b4ae78f5a5d01fea');
INSERT INTO `sys_role_menu` VALUES ('d8454ab03df5400396266a466db716c5', 'fce0ad963d1f42be9178c4c5c493f55e', '6f605148282b4949b5c96e2877dc9052');
INSERT INTO `sys_role_menu` VALUES ('e300bb593d234f06b128ca88062038ac', '59fcb8500eb24f20bf7263467d757212', 'ecb3ebcb68b54e06bd29f270a97cbb67');
INSERT INTO `sys_role_menu` VALUES ('e387e62403a1428d969ed3e2b82f3e83', '59fcb8500eb24f20bf7263467d757212', '63f039ea5bcf4208978150b59484a429');
INSERT INTO `sys_role_menu` VALUES ('e553f51f98524b3e90df2adc02c380cb', 'fce0ad963d1f42be9178c4c5c493f55e', '7f78172c09d3408dab1534d26b608b31');
INSERT INTO `sys_role_menu` VALUES ('e6be0318da604650b3bcdad0824c8281', '59fcb8500eb24f20bf7263467d757212', '34ffa24d3c124902893e601fe8e22b08');
INSERT INTO `sys_role_menu` VALUES ('e86a10855264400d815bddce913669f3', '59fcb8500eb24f20bf7263467d757212', '3');
INSERT INTO `sys_role_menu` VALUES ('ed86f7b076354fa18b2cf3e47fd0de67', 'fce0ad963d1f42be9178c4c5c493f55e', '71e5179363bc4e119a87daaa631a2712');
INSERT INTO `sys_role_menu` VALUES ('f0dafca3efcb4d7cb26716af01f34ed7', 'fce0ad963d1f42be9178c4c5c493f55e', 'b93eba1199b6420a82d285a8919bcd23');
INSERT INTO `sys_role_menu` VALUES ('f210587bfb854f3eab2e83d37ddfd157', '59fcb8500eb24f20bf7263467d757212', '5');
INSERT INTO `sys_role_menu` VALUES ('f62f94733cb54e6ca5151d280fef5d19', '59fcb8500eb24f20bf7263467d757212', 'fa483765360243d0a631a2b9793aaf41');
INSERT INTO `sys_role_menu` VALUES ('fbbbc28ce94448f293cae17279a60462', '59fcb8500eb24f20bf7263467d757212', '9c0846685bb24aafae731bdacf879ba2');
INSERT INTO `sys_role_menu` VALUES ('fc8e2c159e644b5b93921eedec96f506', 'fce0ad963d1f42be9178c4c5c493f55e', '14');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '手机',
  `avatar` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '头像',
  `avatar_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像对应的附件id',
  `email` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '性别',
  `born` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '生日',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启用禁用',
  `creator` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(20) NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '系统编号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('2', '管理员', 'admin', '$2a$10$VguPuhGmeq10EfAuc7rGH.4ibasIXwL4toMU8T5O/iXidah0xgSoe', NULL, '15521089144', 'group1/M00/00/00/wKgAX1yXnqmABxpuAAB2ngEqI1k023.jpg', '19a1ae447b2f4f2d92816028b14c296f', '1633736729@qq.com', '0', '2018-10-04', '0', 'admin', '2018-09-01 14:37:09', 'admin', '2019-03-18 14:12:45', 0, 'EXAM', '管理员', '83f8d25ddc93445fa91e8c9d3db750a0');
INSERT INTO `sys_user` VALUES ('3078afafab8346348cef667b3d56f813', '林老师', 'teacher', '$2a$10$53KTjxEBtn.RIJOMZ/tAcOpIRzwXaHWPeTZFqAYXXBLPMbDBBqbE6', NULL, '12345678901', '', NULL, NULL, '0', '2019-02-18', '0', 'admin', '2019-02-25 13:52:50', 'admin', '2019-03-20 21:43:40', 0, 'EXAM', '张老师', '83f8d25ddc93445fa91e8c9d3db750a0');
INSERT INTO `sys_user` VALUES ('abd4dbe19faf4f7f8ff239b63acc5d34', '梁同学', 'student', '$2a$10$WKc4EjebJbLHLnCgc1Jn/OgeiCVOXSuxw/Flb45Ay9pcogLepr1p6', NULL, '32323232323', '', '', NULL, '0', '2019-02-04', '0', 'admin', '2019-02-25 13:53:47', 'admin', '2019-03-28 16:13:09', 0, 'EXAM', '梁同学', '83f8d25ddc93445fa91e8c9d3db750a0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1bb0ec8a1e3b421f84a1b54eb9f00d33', 'abd4dbe19faf4f7f8ff239b63acc5d34', '23ab268d7e0247868dcf484ab63ff595');
INSERT INTO `sys_user_role` VALUES ('3ebf446ab584424eb67a1acbadb684d2', '3078afafab8346348cef667b3d56f813', 'fce0ad963d1f42be9178c4c5c493f55e');
INSERT INTO `sys_user_role` VALUES ('4380dcd018bd4d8eb6417a28089b7b8d', '2', '59fcb8500eb24f20bf7263467d757212');

SET FOREIGN_KEY_CHECKS = 1;
