
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_route`;
CREATE TABLE `sys_route`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `route_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '路由ID',
  `route_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '路由名称',
  `predicates` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '断言',
  `filters` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '过滤器',
  `uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'URI',
  `sort` int(11) NOT NULL COMMENT '排序',
  `status` tinyint(4) NOT NULL COMMENT '启用禁用',
  `creator` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '系统编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_route
-- ----------------------------
INSERT INTO `sys_route` VALUES (607150228717572096, 'msc-service', '消息中心', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/msc/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://msc-service', 4, 0, '', '2019-10-07 15:04:11', '', '2019-10-07 14:55:44', 0, 'EXAM');
INSERT INTO `sys_route` VALUES (607150228717572097, 'user-service', '用户服务', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/user/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://user-service', 2, 0, 'admin', '2019-04-07 11:22:05', '', '2019-08-03 09:58:35', 0, 'EXAM');
INSERT INTO `sys_route` VALUES (607150228717572098, 'exam-service', '考试服务', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/exam/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://exam-service', 3, 0, 'admin', '2019-04-02 21:39:30', '', '2019-08-03 09:58:54', 0, 'EXAM');
INSERT INTO `sys_route` VALUES (607150228717572099, 'auth-service', '认证授权服务', '[\n  {\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/api/auth/**\"\n    }\n  }\n]', '[\n  {\n    \"name\": \"StripPrefix\",\n    \"args\": {\n      \"_genkey_0\": \"2\"\n    }\n  },\n  {\n    \"name\": \"RemoveRequestHeader\",\n    \"args\": {\n      \"_genkey_0\": \"Cookie\",\n      \"_genkey_1\": \"Set-Cookie\"\n    }\n  }\n]', 'lb://auth-service', 1, 0, 'admin', '2019-04-07 11:20:55', '', '2019-08-03 09:58:48', 0, 'EXAM');

SET FOREIGN_KEY_CHECKS = 1;
