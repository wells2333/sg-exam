-- 更新脚本

ALTER TABLE `exam_subject_category` ADD COLUMN `status` tinyint(1) ZEROFILL NOT NULL COMMENT '状态：0：草稿，1：已发布';

CREATE TABLE shedlock(
 `NAME` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名',
 `lock_until` timestamp(3) NULL DEFAULT NULL COMMENT '释放时间',
 `locked_at` timestamp(3) NULL DEFAULT NULL COMMENT '锁定时间',
 `locked_by` varchar(255) DEFAULT NULL COMMENT '锁定实例',
 PRIMARY KEY (name)
)
