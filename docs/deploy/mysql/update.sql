ALTER TABLE `microservice-user`.`sys_user`
ADD COLUMN `signature` varchar(255) NULL COMMENT '个性签名' AFTER `wechat`;