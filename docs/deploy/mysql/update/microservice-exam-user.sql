-- ----------------------------
-- 2019年6月21日14:32:59
-- ----------------------------
ALTER TABLE `microservice-user`.`sys_attachment`
ADD COLUMN `previewUrl` varchar(255) NULL COMMENT '预览地址' AFTER `busi_type`;