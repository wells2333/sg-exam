DROP DATABASE IF EXISTS `microservice-auth`;

CREATE DATABASE  `microservice-auth` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `microservice-auth`;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'client_id',
  `resource_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'resource_ids',
  `client_secret_plain_text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥明文',
  `client_secret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'client_secret',
  `scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '授权范围',
  `authorized_grant_types` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorities` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'access_token有效时间',
  `refresh_token_validity` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'refresh_token有效时间',
  `additional_information` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人',
  `modify_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标记',
  `application_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '系统编号',
  `tenant_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES (607150228717572096, 'web_app', NULL, 'sg-exam-secret', '$2a$10$IWLD8r7e0rKMW.ZhGsGCUO./MZUNDKudIswSToa9puXJwty.h59.u', 'read,write', 'password,authorization_code,refresh_token,implicit', NULL, NULL, '86400', '86400', NULL, NULL, 'admin', '2019-03-30 23:43:07', 'admin', '2020-03-28 20:01:31', 0, 'EXAM', 'gitee');

