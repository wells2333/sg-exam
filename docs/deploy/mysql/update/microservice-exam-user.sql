-- ----------------------------
-- 2019年6月21日14:32:59
-- ----------------------------
ALTER TABLE `microservice-user`.`sys_attachment`
ADD COLUMN `previewUrl` varchar(255) NULL COMMENT '预览地址' AFTER `busi_type`;

-- ----------------------------
-- 2019年10月17日21:52:01
-- ----------------------------
ALTER TABLE `dev_microservice_exam`.`exam_examination_subject`
MODIFY COLUMN `examination_id` bigint(20) NULL COMMENT '考试ID' AFTER `id`,
ADD COLUMN `category_id` bigint(20) NULL COMMENT '分类' AFTER `examination_id`;