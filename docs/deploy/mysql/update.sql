ALTER TABLE `microservice-user`.`sys_user`
ADD COLUMN `signature` varchar(255) NULL COMMENT '个性签名' AFTER `wechat`;

ALTER TABLE `microservice-user`.`sys_attachment`
ADD COLUMN `attach_type` varchar(128) NULL COMMENT '附件类型' AFTER `attach_name`,
ADD COLUMN `upload_type` tinyint(4) NULL COMMENT '上传类型' AFTER `preview_url`;