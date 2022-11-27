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