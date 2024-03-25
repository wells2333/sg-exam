
-- 2024 年 03 月 07 日 22:49:08
CREATE TABLE `exam_subject_fill_blank` (
   `id` bigint(20) NOT NULL COMMENT '主键',
   `category_id` bigint(20) DEFAULT '0',
   `subject_name` varchar(5000) NOT NULL COMMENT '题目名称',
   `answer` varchar(5000) NOT NULL COMMENT '参考答案',
   `score` int(11) NOT NULL COMMENT '题目分值',
   `analysis` varchar(5000) DEFAULT NULL COMMENT '解析',
   `level` tinyint(1) NOT NULL COMMENT '难度等级',
   `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
   `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
   `speech_id` bigint(20) DEFAULT NULL COMMENT '语音 ID',
   `subject_video_id` bigint(20) DEFAULT NULL COMMENT '题目视频 ID',
   `subject_video_url` varchar(1024) DEFAULT NULL COMMENT '题目视频 URL',
   `speech_play_limit` int(10) DEFAULT NULL COMMENT '语音最大播放次数',
   `auto_play_speech` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '是否自动播放语音，0：否，1：是',
   PRIMARY KEY (`id`) USING BTREE,
   KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='填空题表';

-- 2024年03月16日21:53:20
ALTER TABLE `exam_examination` ADD COLUMN `max_exam_cnt` int(10) NOT NULL DEFAULT 0 COMMENT '最大考试次数，默认 0 不限制';

-- 2024年3月25日21:34:17
CREATE TABLE `exam_subject_material` (
   `id` bigint(20) NOT NULL COMMENT '主键',
   `category_id` bigint(20) DEFAULT '0',
   `subject_name` varchar(5000) NOT NULL COMMENT '材料名称',
   `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
   `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
   `speech_id` bigint(20) DEFAULT NULL COMMENT '语音 ID',
   `subject_video_id` bigint(20) DEFAULT NULL COMMENT '题目视频 ID',
   `subject_video_url` varchar(1024) DEFAULT NULL COMMENT '题目视频 URL',
   `speech_play_limit` int(10) DEFAULT NULL COMMENT '语音最大播放次数',
   `auto_play_speech` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '是否自动播放语音，0：否，1：是',
   PRIMARY KEY (`id`) USING BTREE,
   KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='材料表';
