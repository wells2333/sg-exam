-- 更新脚本

ALTER TABLE `exam_subject_category` ADD COLUMN `status` tinyint(1) ZEROFILL NOT NULL COMMENT '状态：0：草稿，1：已发布';

CREATE TABLE shedlock(
 `NAME` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名',
 `lock_until` timestamp(3) NULL DEFAULT NULL COMMENT '释放时间',
 `locked_at` timestamp(3) NULL DEFAULT NULL COMMENT '锁定时间',
 `locked_by` varchar(255) DEFAULT NULL COMMENT '锁定实例',
 PRIMARY KEY (name)
)

CREATE TABLE `sys_sms` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `receiver` varchar(255) DEFAULT NULL COMMENT '接收方',
   `content` varchar(512) DEFAULT NULL COMMENT '发送内容',
   `response` varchar(512) DEFAULT NULL COMMENT '消息响应',
   `creator` varchar(128) DEFAULT NULL,
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL,
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) DEFAULT NULL,
   `tenant_code` varchar(16) DEFAULT NULL COMMENT '租户编号',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息发送表';

