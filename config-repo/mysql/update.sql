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
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
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
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `content` text COMMENT '知识点内容',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3 COMMENT='章节知识点';

ALTER TABLE `exam_course`
    MODIFY COLUMN `course_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '课程描述';

ALTER TABLE `exam_course`
    ADD COLUMN `simple_desc` varchar(255) NULL COMMENT '简短的描述';

-- ----------------------------
-- 2022年12月04日21:43:36
-- ----------------------------
ALTER TABLE `exam_course`
    ADD COLUMN `sort` int NOT NULL DEFAULT 100 COMMENT '排序号',
ADD COLUMN `course_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '课程状态：0：上架，1：下架';

ALTER TABLE `exam_course_knowledge_point`
    ADD COLUMN `learn_hour` int NULL DEFAULT NULL COMMENT '学习时长',
    ADD COLUMN `video_id` bigint NULL DEFAULT NULL COMMENT '视频ID',
    ADD COLUMN `video_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频名称',
    ADD COLUMN `content_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '节内容类型，0：视频，1：图文' AFTER `video_name`;

-- ----------------------------
-- 2022年12月17日11:04:34
-- ----------------------------
CREATE TABLE `exam_user_favorites` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `target_id` bigint(20) NOT NULL COMMENT '目标ID',
   `user_id` bigint(20) NOT NULL COMMENT '用户ID',
   `target_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型ID，0：考试，1：题目，2：课程',
   PRIMARY KEY (`id`),
   UNIQUE KEY `idx_unique` (`user_id`,`target_id`,`target_type`),
   KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
   KEY `idx_target_id` (`target_id`) USING BTREE COMMENT '目标ID索引',
   KEY `idx_target_type` (`target_type`) USING BTREE COMMENT '目标ID类型索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户收藏表';

CREATE TABLE `exam_fav_start_count` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
    `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
    `target_id` bigint NOT NULL COMMENT '目标ID',
    `start_count` bigint NOT NULL DEFAULT '0' COMMENT '开始数量',
    `target_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型ID，0：考试，1：题目，2：课程',
    `fav_count` bigint NOT NULL DEFAULT '0' COMMENT '收藏数量',
    PRIMARY KEY (`id`),
    KEY `idx_target_id` (`target_id`) USING BTREE COMMENT '目标ID索引',
    KEY `idx_target_type` (`target_type`) USING BTREE COMMENT '目标ID类型索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='收藏、开始数量表';

ALTER TABLE `exam_course_evaluate`
    ADD COLUMN `operator_name` varchar(16) NULL COMMENT '评论人名称';