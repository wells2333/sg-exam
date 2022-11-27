-- 更新脚本

-- ----------------------------
-- 2022年11月26日17:17:37
-- ----------------------------
ALTER TABLE `operation_banner` ADD COLUMN `image_id` bigint(20) NULL COMMENT '图片ID';

ALTER TABLE `sys_tenant` ADD COLUMN `image_id` bigint NULL COMMENT '图片';