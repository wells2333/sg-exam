
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

-- 2024 年 03 月 16 日 21:53:20
ALTER TABLE `exam_examination` ADD COLUMN `max_exam_cnt` int(10) NOT NULL DEFAULT 0 COMMENT '最大考试次数，默认 0 不限制';

-- 2024 年 3 月 25 日 21:34:17
CREATE TABLE `exam_subject_material` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='材料表';

-- 2024 年 3 月 26 日 12:43:26
CREATE TABLE `exam_material_subject`  (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
 `material_id` bigint NULL DEFAULT NULL COMMENT '材料 ID',
 `subject_id` bigint NOT NULL COMMENT '题目 ID',
 `tenant_code` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '租户编号',
 `sort` int(2) UNSIGNED ZEROFILL NOT NULL COMMENT '排序',
 `creator` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `operator` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '修改人',
 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 `is_deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '删除标记 0:正常;1:删除',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE INDEX `idx_material_id_sort`(`material_id` ASC, `sort` ASC) USING BTREE COMMENT '材料 ID+序号',
 INDEX `idx_subject_id`(`subject_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '材料题目表' ROW_FORMAT = DYNAMIC;

-- 2024 年 3 月 26 日 16:25:50
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `type`, `creator`, `create_time`,
    `operator`, `update_time`, `is_deleted`, `component`, `is_ext`, `keepalive`, `remark`, `tenant_code`, `hide_menu`, `redirect`, `current_active_menu`)
VALUES (100, '材料题目管理', NULL, '/exam/material_subjects/:id', 28, NULL, '5', '0', 'admin', '2024-03-26 13:23:24',
        'admin', '2024-03-26 08:22:53', 0, 'exam/materialSubjects/index', 0, 0, NULL, 'gitee', 1, '', '/exam/subject');

-- 2024 年 3 月 27 日 09:41:14
ALTER TABLE `exam_material_subject`
    ADD COLUMN examination_id bigint DEFAULT NULL COMMENT '考试 ID';

-- 2024 年 3 月 27 日 09:59:39
update `sys_menu`
    set path = '/exam/material_subjects/:materialId/:examinationId*'
where id = 100

-- 2024 年 3 月 28 日 20:51:29
ALTER TABLE `exam_course_section`
    ADD COLUMN video_url varchar(255) DEFAULT NULL COMMENT '视频 URL';
ALTER TABLE `exam_course_section`
    ADD COLUMN speech_id bigint DEFAULT NULL COMMENT '音频 ID';
ALTER TABLE `exam_course_section`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';

-- 2024 年 3 月 28 日 22:03:01
ALTER TABLE `exam_course_knowledge_point`
    ADD COLUMN video_url varchar(255) DEFAULT NULL COMMENT '视频 URL';
ALTER TABLE `exam_course_knowledge_point`
    ADD COLUMN speech_id bigint DEFAULT NULL COMMENT '音频 ID';
ALTER TABLE `exam_course_knowledge_point`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';

-- 2024 年 3 月 29 日 10:34:54
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_desc`, `creator`, `create_time`,
                                         `operator`, `update_time`, `is_deleted`, `tenant_code`)
VALUES ('sys_tenant_code', 'gitee', '系统默认租户 code', '', '2024-03-29 02:38:51', '', '2024-03-29 02:39:08', 0, '');

-- 2024 年 3 月 29 日 14:26:05
ALTER TABLE `exam_subject_fill_blank`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';
ALTER TABLE `exam_subject_judgement`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';
ALTER TABLE `exam_subject_material`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';
ALTER TABLE `exam_subject_short_answer`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';
ALTER TABLE `exam_subject_choices`
    ADD COLUMN speech_url varchar(255) DEFAULT NULL COMMENT '音频 URL';

-- 2024 年 4 月 5 日 14:00:49
ALTER TABLE `exam_subjects`
    ADD COLUMN parent_id bigint DEFAULT NULL COMMENT '考试题目对应的分类题目 ID';
