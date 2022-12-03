-- 更新脚本

-- ----------------------------
-- 2022年11月26日17:17:37
-- ----------------------------
ALTER TABLE `operation_banner` ADD COLUMN `image_id` bigint(20) NULL COMMENT '图片ID';

ALTER TABLE `sys_tenant` ADD COLUMN `image_id` bigint NULL COMMENT '图片';

CREATE TABLE `exam_course_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` bigint DEFAULT NULL COMMENT '课程ID',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_course_id` (`course_id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COMMENT='课程学员表';

ALTER TABLE `exam_course`
    ADD COLUMN `charge_type` tinyint(1) NULL COMMENT '收费类型：0：免费，1：收费',
    ADD COLUMN `charge_price` double(10,2) DEFAULT NULL COMMENT '收费价格',
    ADD COLUMN `level` tinyint(1) NULL DEFAULT 3 COMMENT '难度等级，1~5，默认3';

INSERT INTO `sys_menu` VALUES (91, '查看题库', 'exam:subject:bank:view', NULL, 41, NULL, '1', '1', 'admin', '2022-11-30 22:42:14', 'admin', '2022-11-30 22:42:14', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_role_menu` VALUES (445, 2, 91, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');

-- ----------------------------
-- 2022年12月02日20:54:49
-- ----------------------------
ALTER TABLE `exam_course_section`
    ADD COLUMN `content_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '节内容类型，0：视频，1：图文';
ALTER TABLE `exam_course_section`
    ADD COLUMN `content` text NULL COMMENT '节内容';

CREATE TABLE `exam_course_knowledge_point` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '知识点标题',
   `sort` int NOT NULL COMMENT '序号',
   `section_id` bigint NOT NULL COMMENT '节ID',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `content` text COMMENT '知识点内容',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3 COMMENT='章节知识点';

ALTER TABLE `exam_course`
    MODIFY COLUMN `course_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '课程描述';

ALTER TABLE `exam_course`
    ADD COLUMN `simple_desc` varchar(255) NULL COMMENT '简短的描述';