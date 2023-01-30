SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for exam_answer
-- ----------------------------
DROP TABLE IF EXISTS `exam_answer`;
CREATE TABLE `exam_answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exam_record_id` bigint(20) NOT NULL COMMENT '考试记录id',
  `subject_id` bigint(20) NOT NULL COMMENT '题目ID',
  `type` tinyint(1) DEFAULT NULL,
  `answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `answer_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '答题类型，0：正确，1：错误',
  `score` tinyint(4) DEFAULT NULL COMMENT '实际得分',
  `mark_status` tinyint(1) DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '',
  `mark_operator` varchar(16) DEFAULT '' COMMENT '批改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_record_sub_id` (`exam_record_id`,`subject_id`) USING BTREE,
  KEY `idx_record_id` (`exam_record_id`),
  KEY `idx_answer_type` (`answer_type`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='答题表';

-- ----------------------------
-- Table structure for exam_course
-- ----------------------------
DROP TABLE IF EXISTS `exam_course`;
CREATE TABLE `exam_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_name` varchar(128) NOT NULL DEFAULT '' COMMENT '课程名称',
  `college` varchar(128) DEFAULT NULL COMMENT '学院',
  `major` varchar(128) DEFAULT NULL COMMENT '专业',
  `teacher` varchar(128) DEFAULT NULL COMMENT '老师',
  `course_description` varchar(255) DEFAULT NULL COMMENT '课程描述',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `image_id` bigint DEFAULT NULL COMMENT '图片ID',
  `cate_name` varchar(255) DEFAULT NULL COMMENT '类别名称',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='课程信息';

-- ----------------------------
-- Table structure for exam_examination
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination`;
CREATE TABLE `exam_examination` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `examination_name` varchar(255) DEFAULT NULL COMMENT '考试名称',
  `type` tinyint(1) DEFAULT NULL COMMENT '考试类型',
  `attention` varchar(3000) DEFAULT NULL COMMENT '考试注意事项',
  `start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '考试结束时间',
  `total_score` int(11) NOT NULL COMMENT '总分',
  `status` tinyint(1) DEFAULT NULL COMMENT '考试状态',
  `image_id` bigint(20) DEFAULT NULL COMMENT '图片ID',
  `course_id` bigint(20) DEFAULT NULL COMMENT '课程',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
  `tags` varchar(255) DEFAULT '' COMMENT '标签，多个逗号分隔',
  `answer_type` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '答题模式，0：展示全部题目，1：上一题、下一题模式',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_exam_id` (`examination_name`) COMMENT '考试名称',
  KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考试信息表';

-- ----------------------------
-- Table structure for exam_examination_favorites
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination_favorites`;
CREATE TABLE `exam_examination_favorites` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `examination_id` bigint(20) NOT NULL COMMENT '考试ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户收藏考试表';

-- ----------------------------
-- Table structure for exam_examination_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination_record`;
CREATE TABLE `exam_examination_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(1) unsigned zerofill NOT NULL COMMENT '类型',
  `examination_id` bigint(20) NOT NULL COMMENT '考试ID',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `score` int(11) DEFAULT NULL COMMENT '成绩',
  `correct_number` int(11) DEFAULT NULL COMMENT '正确题目数量',
  `in_correct_number` int(11) DEFAULT NULL COMMENT '错误题目数量',
  `submit_status` tinyint(1) DEFAULT NULL,
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`),
  KEY `idx_exam_id` (`examination_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考试记录表';

-- ----------------------------
-- Table structure for exam_examination_subject
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination_subject`;
CREATE TABLE `exam_examination_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `examination_id` bigint(20) DEFAULT NULL COMMENT '考试ID',
  `subject_id` bigint(20) NOT NULL COMMENT '题目ID',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `sort` int(2) unsigned zerofill NOT NULL COMMENT '排序',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) unsigned zerofill NOT NULL COMMENT '删除标记 0:正常;1:删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_examination_id_sort` (`examination_id`,`sort`) USING BTREE COMMENT '考试ID+序号',
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考试题目表';

-- ----------------------------
-- Table structure for exam_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `exam_knowledge`;
CREATE TABLE `exam_knowledge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `knowledge_name` varchar(128) NOT NULL COMMENT '知识名称',
  `knowledge_desc` varchar(255) DEFAULT NULL COMMENT '知识描述',
  `attachment_id` bigint(20) DEFAULT NULL COMMENT '附件ID',
  `status` tinyint(1) NOT NULL COMMENT '状态',
  `creator` varchar(16) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识表';

-- ----------------------------
-- Table structure for exam_pictures
-- ----------------------------
DROP TABLE IF EXISTS `exam_pictures`;
CREATE TABLE `exam_pictures` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `picture_address` varchar(255) DEFAULT NULL COMMENT '知识名称',
  `attachment_id` bigint(20) DEFAULT NULL COMMENT '附件ID',
  `creator` varchar(16) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片表';

-- ----------------------------
-- Table structure for exam_subject_category
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_category`;
CREATE TABLE `exam_subject_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_name` varchar(128) DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父分类ID',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型: 0-私共,1-公有',
  `creator` varchar(16) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='题目分类表';

-- ----------------------------
-- Table structure for exam_subject_choices
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_choices`;
CREATE TABLE `exam_subject_choices` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) DEFAULT '0',
  `subject_name` varchar(5000) NOT NULL COMMENT '题目名称',
  `choices_type` tinyint(1) NOT NULL COMMENT '题目类型',
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
  `speech_id` bigint(20) DEFAULT NULL COMMENT '语音ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='选择题表';

-- ----------------------------
-- Table structure for exam_subject_favorites
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_favorites`;
CREATE TABLE `exam_subject_favorites` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `subject_id` bigint(20) NOT NULL COMMENT '题目ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏题目表';

-- ----------------------------
-- Table structure for exam_subject_judgement
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_judgement`;
CREATE TABLE `exam_subject_judgement` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `subject_name` varchar(5000) NOT NULL DEFAULT '' COMMENT '题目名称',
  `answer` varchar(5000) NOT NULL DEFAULT '' COMMENT '参考答案',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '分值',
  `analysis` varchar(5000) DEFAULT NULL COMMENT '解析',
  `level` tinyint(1) NOT NULL COMMENT '难度等级',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
  `speech_id` bigint(20) DEFAULT NULL COMMENT '语音ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='简答题表';

-- ----------------------------
-- Table structure for exam_subject_option
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_option`;
CREATE TABLE `exam_subject_option` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject_choices_id` bigint(20) NOT NULL COMMENT '选择题ID',
  `option_name` varchar(64) NOT NULL DEFAULT '' COMMENT '选项名称',
  `option_content` varchar(5000) NOT NULL DEFAULT '' COMMENT '选项内容',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='选择题选项表';

-- ----------------------------
-- Table structure for exam_subject_short_answer
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_short_answer`;
CREATE TABLE `exam_subject_short_answer` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `subject_name` varchar(5000) NOT NULL COMMENT '题目名称',
  `answer` varchar(5000) NOT NULL COMMENT '参考答案',
  `score` int(11) NOT NULL COMMENT '分值',
  `analysis` varchar(5000) DEFAULT NULL COMMENT '解析',
  `level` tinyint(1) NOT NULL COMMENT '难度等级',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
  `speech_id` bigint(20) DEFAULT NULL COMMENT '语音ID',
  `judge_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '判分类型，0：自动判分，1：人工判分',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='简答题表';

-- ----------------------------
-- Table structure for exam_subject_speech
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_speech`;
CREATE TABLE `exam_subject_speech` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `subject_name` varchar(5000) NOT NULL COMMENT '题目名称',
  `answer` varchar(5000) NOT NULL COMMENT '参考答案',
  `score` int(11) NOT NULL COMMENT '分值',
  `analysis` varchar(5000) DEFAULT NULL COMMENT '解析',
  `level` tinyint(1) NOT NULL COMMENT '难度等级',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
  `speech_id` bigint(20) DEFAULT NULL COMMENT '语音ID',
  `judge_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '判分类型，0：自动判分，1：人工判分',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='语音题目表';

-- ----------------------------
-- Table structure for exam_subject_video
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_video`;
CREATE TABLE `exam_subject_video` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `subject_name` varchar(5000) NOT NULL COMMENT '题目名称',
  `answer` varchar(5000) NOT NULL COMMENT '参考答案',
  `score` int(11) NOT NULL COMMENT '分值',
  `analysis` varchar(5000) DEFAULT NULL COMMENT '解析',
  `level` tinyint(1) NOT NULL COMMENT '难度等级',
  `creator` varchar(16) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `sort` int(10) unsigned zerofill NOT NULL COMMENT '排序',
  `speech_id` bigint(20) DEFAULT NULL COMMENT '语音ID',
  `video_id` bigint(20) DEFAULT NULL COMMENT '视频ID',
  `judge_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '判分类型，0：自动判分，1：人工判分',
  `video_name` varchar(255) DEFAULT NULL COMMENT '视频名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='视频题目表';

-- ----------------------------
-- Table structure for exam_subjects
-- ----------------------------
DROP TABLE IF EXISTS `exam_subjects`;
CREATE TABLE `exam_subjects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `creator` varchar(16) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `type` tinyint(1) unsigned zerofill NOT NULL COMMENT '题目类型',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `subject_id` bigint(20) DEFAULT NULL COMMENT '题目ID',
  `sort` int NOT NULL DEFAULT '0' COMMENT '序号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_subject_id` (`subject_id`),
  KEY `idx_category_id` (`category_id`),
  UNIQUE KEY `idx_type_subject_id` (`subject_id`, `type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='课程表';

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `operator` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) DEFAULT NULL,
  `tenant_code` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表';

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `operator` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT NULL,
  `tenant_code` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表字段';

-- ----------------------------
-- Table structure for operation_banner
-- ----------------------------
DROP TABLE IF EXISTS `operation_banner`;
CREATE TABLE `operation_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `operation_name` varchar(255) DEFAULT NULL COMMENT '运营位名称',
  `operation_type` tinyint(1) DEFAULT NULL COMMENT '运营位类型',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `redirect_url` varchar(255) DEFAULT NULL COMMENT '跳转的URL',
  `operation_desc` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='首页运营位';

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attach_name` varchar(255) NOT NULL DEFAULT '' COMMENT '附件名称',
  `attach_type` varchar(128) NOT NULL DEFAULT '' COMMENT '附件类型',
  `attach_size` varchar(255) NOT NULL DEFAULT '' COMMENT '附件大小',
  `url` varchar(255) DEFAULT NULL COMMENT '预览地址',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) unsigned zerofill NOT NULL COMMENT '删除标记',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `group_code` varchar(128) NOT NULL COMMENT '分组',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_attachment_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment_group`;
CREATE TABLE `sys_attachment_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL DEFAULT '' COMMENT '分组名称',
  `group_code` varchar(128) NOT NULL DEFAULT '' COMMENT '分组标识',
  `url_expire` int(20) unsigned zerofill NOT NULL COMMENT 'url过期时间',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) unsigned zerofill NOT NULL COMMENT '删除标记',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_identifier` (`group_code`) USING BTREE,
  KEY `idx_group_code` (`group_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_name` varchar(16) DEFAULT NULL COMMENT '部门名称',
  `dept_desc` varchar(128) DEFAULT NULL,
  `dept_leader` varchar(16) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级部门id',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_dept_name` (`dept_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部门表';

-- ----------------------------
-- Table structure for sys_feedback
-- ----------------------------
DROP TABLE IF EXISTS `sys_feedback`;
CREATE TABLE `sys_feedback` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `submitter_id` bigint(20) NOT NULL COMMENT '提交人',
  `content` varchar(1024) DEFAULT NULL COMMENT '提交内容',
  `status` tinyint(1) DEFAULT NULL COMMENT '反馈状态',
  `creator` varchar(128) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT NULL,
  `tenant_code` varchar(16) DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(1) DEFAULT NULL COMMENT '日志类型',
  `title` varchar(128) DEFAULT NULL COMMENT '日志标题',
  `ip` varchar(16) DEFAULT NULL COMMENT '操作用户的IP地址',
  `user_agent` varchar(256) DEFAULT NULL COMMENT '操作用户代理信息',
  `request_uri` varchar(128) DEFAULT NULL COMMENT '操作的URI',
  `method` varchar(16) DEFAULT NULL COMMENT '操作的方式',
  `params` varchar(255) DEFAULT NULL COMMENT '操作提交的数据',
  `exception` varchar(2000) DEFAULT NULL COMMENT '异常信息',
  `service_id` varchar(16) DEFAULT NULL COMMENT '服务ID',
  `took` varchar(16) DEFAULT NULL COMMENT '耗时',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '菜单名称',
  `permission` varchar(128) DEFAULT NULL COMMENT '权限标识',
  `path` varchar(128) DEFAULT NULL COMMENT '路由地址',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单id',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `sort` varchar(20) DEFAULT NULL COMMENT '排序',
  `type` varchar(20) DEFAULT NULL COMMENT '类型',
  `creator` varchar(128) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT NULL,
  `component` varchar(128) DEFAULT NULL COMMENT '组件路径',
  `is_ext` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否外链',
  `keepalive` tinyint(1) DEFAULT NULL COMMENT '是否缓存',
  `remark` varchar(128) DEFAULT NULL,
  `tenant_code` varchar(16) DEFAULT NULL COMMENT '租户编号',
  `hide_menu` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否隐藏',
  `redirect` varchar(64) NOT NULL DEFAULT '' COMMENT '重定向地址',
  `current_active_menu` varchar(64) NOT NULL DEFAULT '' COMMENT '当前tag',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `startTime` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `type` tinyint(1) DEFAULT NULL COMMENT '消息类型',
  `status` tinyint(1) DEFAULT NULL COMMENT '消息状态，1: 自动，2：显示，3：未显示',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_news
-- ----------------------------
DROP TABLE IF EXISTS `sys_news`;
CREATE TABLE `sys_news` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `title` varchar(1024) DEFAULT NULL COMMENT '标题',
  `location` varchar(255) DEFAULT NULL COMMENT '跳转页面',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `creator` varchar(128) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT NULL,
  `tenant_code` varchar(16) DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(16) NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_code` varchar(64) NOT NULL DEFAULT '' COMMENT '角色code',
  `role_desc` varchar(128) NOT NULL DEFAULT '' COMMENT '角色描述',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认 0-否，1-是',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '启用禁用状态 0-启用，1-禁用',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL DEFAULT '' COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户code',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `creator` varchar(128) DEFAULT '',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT '',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_speech_synthesis
-- ----------------------------
DROP TABLE IF EXISTS `sys_speech_synthesis`;
CREATE TABLE `sys_speech_synthesis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL DEFAULT '' COMMENT '文字',
  `attach_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '附件ID',
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) unsigned zerofill NOT NULL COMMENT '删除标记',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;
CREATE TABLE `sys_student` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `student_name` varchar(128) DEFAULT NULL COMMENT '学生姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `born` date DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL COMMENT '难度等级',
  `address` varchar(255) DEFAULT NULL COMMENT '部门id',
  `grade` varchar(32) DEFAULT NULL COMMENT '详细描述',
  `school` varchar(32) DEFAULT NULL COMMENT '就读学校',
  `wechat` varchar(64) DEFAULT NULL COMMENT '微信',
  `city_id` bigint(20) DEFAULT NULL COMMENT '城市id',
  `county_id` bigint(20) DEFAULT NULL COMMENT '县id',
  `creator` varchar(16) NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(16) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户标识',
  `tenant_name` varchar(128) NOT NULL DEFAULT '' COMMENT '租户名称',
  `tenant_desc` varchar(255) DEFAULT NULL COMMENT '租户描述信息',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `creator` varchar(128) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `init_status` tinyint(1) DEFAULT '0' COMMENT '初始化状态，0-未初始化，1：初始化中，2：初始化完成',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_tenant_code` (`tenant_code`),
  KEY `idx_is_del` (`is_deleted`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='租户信息表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(16) DEFAULT '' COMMENT '用户姓名',
  `phone` varchar(16) DEFAULT '',
  `avatar_id` bigint(20) DEFAULT NULL COMMENT '头像id',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱地址',
  `born` date DEFAULT NULL COMMENT '出生日期',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
  `user_desc` varchar(128) DEFAULT NULL COMMENT '详细描述',
  `parent_uid` bigint(20) DEFAULT NULL COMMENT '父账号id',
  `street_id` bigint(20) DEFAULT NULL COMMENT '街道id',
  `county_id` bigint(20) DEFAULT NULL COMMENT '国家id',
  `city_id` bigint(20) DEFAULT NULL COMMENT '城市id',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省id',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `lock_time` datetime NULL DEFAULT NULL COMMENT '锁定账号时间',
  `wechat` varchar(128) DEFAULT NULL COMMENT '微信号',
  `signature` varchar(512) DEFAULT NULL COMMENT '个性签名',
  `family_role` tinyint(1) DEFAULT NULL COMMENT '家庭角色',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Table structure for sys_user_auths
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_auths`;
CREATE TABLE `sys_user_auths` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `identity_type` tinyint(1) NOT NULL COMMENT '登录类型，手机号、邮箱、用户名或第三方应用名称（微信 微博等）',
  `identifier` varchar(128) NOT NULL COMMENT '标识，手机号、邮箱、用户名或第三方应用的唯一标识',
  `credential` varchar(512) DEFAULT NULL COMMENT '密码凭证，站内的保存密码，站外的不保存或保存token',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL DEFAULT '' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_identifier` (`identifier`,`tenant_code`) USING BTREE,
  KEY `idx_user_id` (`user_id`),
  KEY `idx_identify_type` (`identity_type`),
  KEY `idx_is_del` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户授权表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户code',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_student`;
CREATE TABLE `sys_user_student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '学生姓名',
  `student_id` bigint(20) NOT NULL COMMENT '电话号码',
  `relationship_type` tinyint(1) NOT NULL COMMENT '关系类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`),
  KEY `idx_stu_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户学生关联表';

CREATE TABLE `exam_course_section` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
   `sort` int DEFAULT NULL COMMENT '序号',
   `chapter_id` bigint DEFAULT NULL COMMENT '章ID',
   `learn_hour` int DEFAULT NULL COMMENT '时长',
   `video_id` bigint DEFAULT NULL COMMENT '视频ID',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `section_desc` varchar(255) DEFAULT NULL COMMENT '描述',
   `video_name` varchar(255) DEFAULT NULL COMMENT '视频名称',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='课程节表';

-- 课程章节表
CREATE TABLE `exam_course_chapter` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '标题',
   `sort` int DEFAULT NULL COMMENT '序号',
   `course_id` bigint DEFAULT NULL COMMENT '课程ID',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `chapter_desc` varchar(255) DEFAULT NULL COMMENT '描述',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='课程章表';

CREATE TABLE `exam_course_evaluate` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `evaluate_content` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '评价内容',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `evaluate_level` int(1) unsigned zerofill DEFAULT '0',
    `creator` varchar(128) NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
    `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
    `course_id` bigint NOT NULL COMMENT '课程ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_course_id` (`course_id`) COMMENT '课程ID'
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COMMENT='课程评价表';

CREATE TABLE `exam_user_favorites` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `target_id` bigint(20) NOT NULL COMMENT '目标ID',
   `user_id` bigint(20) NOT NULL COMMENT '用户ID',
   `target_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型ID，0：考试，1：题目，2：课程',
   PRIMARY KEY (`id`),
   UNIQUE KEY `idx_unique` (`user_id`,`target_id`,`target_type`),
   KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
   KEY `idx_target_id` (`target_id`) USING BTREE COMMENT '目标ID索引',
   KEY `idx_target_type` (`target_type`) USING BTREE COMMENT '目标ID类型索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户收藏表';

CREATE TABLE `exam_fav_start_count` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `creator` varchar(128) NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
    `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
    `target_id` bigint NOT NULL COMMENT '目标ID',
    `start_count` bigint NOT NULL DEFAULT '0' COMMENT '开始数量',
    `target_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型ID，0：考试，1：题目，2：课程',
    `fav_count` bigint NOT NULL DEFAULT '0' COMMENT '收藏数量',
    PRIMARY KEY (`id`),
    KEY `idx_target_id` (`target_id`) USING BTREE COMMENT '目标ID索引',
    KEY `idx_target_type` (`target_type`) USING BTREE COMMENT '目标ID类型索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='收藏、开始数量表';

CREATE TABLE `exam_course_knowledge_point` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '知识点标题',
   `sort` int NOT NULL COMMENT '序号',
   `section_id` bigint NOT NULL COMMENT '节ID',
   `creator` varchar(128) NOT NULL COMMENT '创建人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
   `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
   `content` text COMMENT '知识点内容',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3 COMMENT='章节知识点';

CREATE TABLE `exam_course_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` bigint DEFAULT NULL COMMENT '课程ID',
  `creator` varchar(128) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(128) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0:正常;1:删除',
  `tenant_code` varchar(16) NOT NULL COMMENT '租户编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_course_id` (`course_id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COMMENT='课程学员表';

ALTER TABLE `operation_banner` ADD COLUMN `image_id` bigint(20) NULL COMMENT '图片ID';

ALTER TABLE `sys_tenant` ADD COLUMN `image_id` bigint NULL COMMENT '图片';

ALTER TABLE `exam_course`
    ADD COLUMN `charge_type` tinyint(1) NULL COMMENT '收费类型：0：免费，1：收费',
    ADD COLUMN `charge_price` double(10,2) DEFAULT NULL COMMENT '收费价格',
    ADD COLUMN `level` tinyint(1) NULL DEFAULT 3 COMMENT '难度等级，1~5，默认3';

ALTER TABLE `exam_course`
    MODIFY COLUMN `course_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '课程描述';

ALTER TABLE `exam_course`
    ADD COLUMN `simple_desc` varchar(255) NULL COMMENT '简短的描述';

ALTER TABLE `exam_course`
    ADD COLUMN `sort` int NOT NULL DEFAULT 100 COMMENT '排序号',
ADD COLUMN `course_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '课程状态：0：上架，1：下架';

ALTER TABLE `exam_course_knowledge_point`
    ADD COLUMN `learn_hour` int NULL DEFAULT NULL COMMENT '学习时长',
    ADD COLUMN `video_id` bigint NULL DEFAULT NULL COMMENT '视频ID',
    ADD COLUMN `video_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频名称',
    ADD COLUMN `content_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '节内容类型，0：视频，1：图文' AFTER `video_name`;

ALTER TABLE `exam_course_evaluate`
    ADD COLUMN `operator_name` varchar(16) NULL COMMENT '评论人名称';

ALTER TABLE `exam_course_section`
    ADD COLUMN `content_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '节内容类型，0：视频，1：图文';
ALTER TABLE `exam_course_section`
    ADD COLUMN `content` text NULL COMMENT '节内容';

SET FOREIGN_KEY_CHECKS = 1;

-- 初始化数据

-- ----------------------------
-- 单位信息
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1, 'gitee', '码云', '码云', 1, 'admin', '2019-05-23 21:46:36', 'admin', '2022-05-20 13:26:27', 0, NULL, 2, NULL);

-- ----------------------------
-- 账号
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '管理员', '15521089123', 1, '1633736729@qq.com', '1995-07-03', 0, 0, 1, '管理员', NULL, NULL, NULL, 530100, 530000, '2022-06-08 13:27:48', NULL, NULL, '测试', NULL, 'admin', '2020-04-05 19:58:07', 'admin', '2022-06-08 13:27:48', 0, 'gitee');
INSERT INTO `sys_user` VALUES (2, '预览账号', '', 2, NULL, NULL, 0, 0, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2022-11-13 13:27:58', 'admin', '2022-11-13 13:28:20', 0, 'gitee');

INSERT INTO `sys_user_auths` VALUES (1, 1, 1, 'admin', '$2a$10$QdZynI98f6aRANUQw4Fk/O6hXoucba6nIdvuXf8Gdy3Zz/mF78FgS', 'admin', '2020-02-29 16:13:29', 'admin', '2022-05-01 16:17:22', 0, 'gitee');
INSERT INTO `sys_user_auths` VALUES (2, 2, 1, 'preview', '$2a$10$QdZynI98f6aRANUQw4Fk/O6hXoucba6nIdvuXf8Gdy3Zz/mF78FgS', 'admin', '2022-11-13 13:27:58', 'admin', '2022-11-13 13:27:58', 0, 'gitee');

-- ----------------------------
-- 部门信息
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '运营中心', '运营中心', '管理员', -1, 2, 'admin', '2022-03-25 20:46:58', 'admin', '2022-03-25 20:46:58', 0, 'gitee');

-- ----------------------------
-- 角色
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'role_admin', '超级管理员', 0, 0, 'admin', '2019-10-07 15:02:17', 'admin', '2019-10-07 14:53:50', 0, 'gitee');
INSERT INTO `sys_role` VALUES (2, '预览角色', 'role_preview', '只有查看权限', 1, 0, 'admin', '2022-11-13 13:24:39', 'admin', '2022-11-13 13:24:39', 0, 'gitee');

-- ----------------------------
-- 用户角色关系
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, 'admin', '2022-03-25 20:46:37', 'admin', '2022-03-25 20:46:37', 0, 'gitee');
INSERT INTO `sys_user_role` VALUES (2, 2, 2, 'preview', '2022-11-13 13:27:58', 'preview', '2022-11-13 13:27:58', 0, 'gitee');

-- ----------------------------
-- 菜单
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '首页', 'dashboard', '/dashboard', -1, 'ant-design:appstore-twotone', '1', '0', 'admin', '2022-07-02 20:24:57', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, '首页', 'gitee', 0, '/dashboard/analysis', '');
INSERT INTO `sys_menu` VALUES (2, '分析页', 'dashboard:view', '/dashboard/analysis', 1, '', '1', '0', 'admin', '2022-03-25 14:18:55', 'admin', '2022-11-13 11:48:52', 0, 'dashboard/analysis/index', 0, NULL, '查看首页', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (3, '系统管理', 'sys', '/sys', -1, 'ion:settings-outline', '2', '0', 'admin', '2019-04-26 16:10:43', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, NULL, '系统管理', 'gitee', 0, '/sys/tenant', '');
INSERT INTO `sys_menu` VALUES (4, '菜单管理', 'sys:menu', '/sys/menu', 3, '', '10', '0', 'admin', '2019-04-26 16:16:47', 'admin', '2022-11-13 11:48:52', 0, 'sys/menu/index', 0, NULL, '菜单管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (5, '新增菜单', 'sys:menu:add', NULL, 4, '', '1', '1', 'admin', '2019-04-26 16:35:48', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (6, '修改菜单', 'sys:menu:edit', NULL, 4, '', '2', '1', 'admin', '2019-04-26 16:36:08', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (7, '删除菜单', 'sys:menu:del', NULL, 4, '', '3', '1', 'admin', '2019-04-26 16:36:23', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (8, '导入菜单', 'sys:menu:import', NULL, 4, '', '4', '1', 'admin', '2019-04-26 16:36:44', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (9, '导出菜单', 'sys:menu:export', NULL, 4, '', '5', '1', 'admin', '2019-04-26 16:36:59', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (10, '用户管理', 'sys:user', '/sys/user', 3, '', '2', '0', 'admin', '2019-04-26 16:12:19', 'admin', '2022-11-13 11:48:52', 0, 'sys/user/index', 0, NULL, '用户管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (11, '新增用户', 'sys:user:add', NULL, 10, '', '1', '1', 'admin', '2019-04-26 16:25:51', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (12, '删除用户', 'sys:user:del', NULL, 10, '', '2', '1', 'admin', '2019-04-26 16:26:15', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (13, '修改用户', 'sys:user:edit', NULL, 10, '', '3', '1', 'admin', '2019-04-26 16:26:46', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (14, '导出用户', 'sys:user:export', NULL, 10, '', '4', '1', 'admin', '2019-04-26 16:27:08', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (15, '导入用户', 'sys:user:import', NULL, 10, '', '5', '1', 'admin', '2019-04-26 16:27:26', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (16, '部门管理', 'sys:dept', '/sys/dept', 3, '', '8', '0', 'admin', '2019-04-26 16:13:09', 'admin', '2022-11-13 11:48:52', 0, 'sys/dept/index', 0, NULL, '部门管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (17, '新增部门', 'sys:dept:add', NULL, 16, '', '1', '1', 'admin', '2019-04-26 16:30:01', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (18, '修改部门', 'sys:dept:edit', NULL, 16, '', '2', '1', 'admin', '2019-04-26 16:30:33', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (19, '删除部门', 'sys:dept:del', NULL, 16, '', '3', '1', 'admin', '2019-04-26 16:31:08', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (20, '角色管理', 'sys:role', '/sys/role', 3, '', '9', '0', 'admin', '2019-04-26 16:14:56', 'admin', '2022-11-13 11:48:52', 0, 'sys/role/index', 0, NULL, '角色管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (21, '新增角色', 'sys:role:add', NULL, 20, '', '1', '1', 'admin', '2019-04-26 16:33:11', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (22, '修改角色', 'sys:role:edit', NULL, 20, '', '2', '1', 'admin', '2019-04-26 16:33:28', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (23, '删除角色', 'sys:role:del', NULL, 20, '', '3', '1', 'admin', '2019-04-26 16:33:45', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (24, '分配权限', 'sys:role:auth', NULL, 20, '', '4', '1', 'admin', '2019-04-26 16:34:12', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (25, '单位管理', 'tenant:tenant', '/sys/tenant', 3, '', '1', '0', 'admin', '2019-05-23 21:53:41', 'admin', '2022-11-13 11:48:52', 0, 'sys/tenant/index', 0, NULL, '单位管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (26, '修改单位', 'tenant:tenant:edit', '', 25, '', '2', '1', 'admin', '2019-05-23 21:55:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (27, '删除单位', 'tenant:tenant:del', NULL, 25, '', '3', '1', 'admin', '2019-05-23 21:55:48', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (28, '考务管理', 'exam', '/exam', -1, 'ant-design:bank-twotone', '3', '0', 'admin', '2019-04-26 15:12:02', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, NULL, '考务管理', 'gitee', 0, '/exam/course', '');
INSERT INTO `sys_menu` VALUES (29, '课程管理', 'exam:course', '/exam/course', 28, '', '1', '0', 'admin', '2019-04-26 15:13:45', 'admin', '2022-11-13 11:48:52', 0, 'exam/course/index', 0, NULL, '课程管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (30, '新增课程', 'exam:course:add', NULL, 29, '', '1', '1', 'admin', '2019-04-26 15:21:44', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (31, '修改课程', 'exam:course:edit', NULL, 29, '', '2', '1', 'admin', '2019-04-26 15:22:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (32, '删除课程', 'exam:course:del', NULL, 29, '', '3', '1', 'admin', '2019-04-26 15:22:43', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (33, '考试管理', 'exam:exam', '/exam/examination', 28, '', '2', '0', 'admin', '2019-04-26 15:14:55', 'admin', '2022-11-13 11:48:52', 0, 'exam/examination/index', 0, NULL, '考试管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (34, '新增考试', 'exam:exam:add', NULL, 33, '', '1', '1', 'admin', '2019-04-26 15:23:30', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (35, '删除考试', 'exam:exam:del', NULL, 33, '', '3', '1', 'admin', '2019-04-26 15:24:26', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (36, '题目管理', NULL, '/exam/examination_subjects/:id', 28, NULL, '7', '0', 'admin', '2022-04-11 22:43:26', 'admin', '2022-11-13 11:48:52', 0, 'exam/examSubjects/index', 0, 0, NULL, 'gitee', 1, '', '/exam/examination');
INSERT INTO `sys_menu` VALUES (37, '导出题目', 'exam:exam:subject:export', NULL, 33, '', '5', '1', 'admin', '2019-04-26 15:26:53', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (38, '导入题目', 'exam:exam:subject:import', NULL, 33, '', '6', '1', 'admin', '2019-04-26 15:28:14', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (39, '新增题目', 'exam:exam:subject:add', NULL, 33, '', '7', '1', 'admin', '2019-04-26 15:29:01', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (40, '删除题目', 'exam:exam:subject:del', NULL, 33, '', '8', '1', 'admin', '2019-04-26 15:29:40', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (41, '题库管理', 'exam:subject', '/exam/subject', 28, '', '3', '0', 'admin', '2019-04-26 15:16:47', 'admin', '2022-11-13 11:48:52', 0, 'exam/subject/index', 0, NULL, '题库管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (42, '新增题目', 'exam:subject:bank:add', NULL, 41, '', '1', '1', 'admin', '2019-04-26 15:30:45', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (43, '修改题目', 'exam:subject:bank:edit', NULL, 41, '', '2', '1', 'admin', '2019-04-26 15:32:00', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (44, '删除题目', 'exam:subject:bank:del', NULL, 41, '', '3', '1', 'admin', '2019-04-26 15:32:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (45, '新增题目分类', 'exam:subject:category:add', NULL, 41, '', '4', '1', 'admin', '2019-04-26 15:33:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (46, '修改题目分类', 'exam:subject:category:edit', NULL, 41, '', '5', '1', 'admin', '2019-04-26 15:33:56', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (47, '删除题目分类', 'exam:subject:category:del', NULL, 41, '', '6', '1', 'admin', '2019-04-26 15:34:19', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (48, '导入题目', 'exam:subject:bank:import', NULL, 41, '', '7', '1', 'admin', '2019-04-26 15:35:09', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (49, '导出题目', 'exam:subject:bank:export', NULL, 41, '', '8', '1', 'admin', '2019-04-26 15:36:23', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (50, '成绩管理', 'exam:examRecord', '/exam/score', 28, '', '4', '0', 'admin', '2019-04-26 15:18:30', 'admin', '2022-11-13 11:48:52', 0, 'exam/score/index', 0, NULL, '成绩管理', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (51, '导出成绩', 'exam:examRecord:export', NULL, 50, '', '30', '1', 'admin', '2019-04-26 15:37:19', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (53, '附件管理', 'attachment', '/attachment', -1, 'ant-design:profile-outlined', '98', '0', 'admin', '2022-11-12 12:00:57', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (54, '附件列表', NULL, '/attachment/list', 53, NULL, '2', '0', 'admin', '2022-04-16 15:47:20', 'admin', '2022-11-13 11:48:52', 0, 'attachment/list/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (55, '附件分组', NULL, '/attachment/group', 53, NULL, '1', '0', 'admin', '2022-04-16 15:47:08', 'admin', '2022-11-13 11:48:52', 0, 'attachment/group/index', 0, 0, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (56, '语音合成', 'speech', '/speech', -1, 'ant-design:codepen-outlined', '97', '0', 'admin', '2022-11-12 12:01:12', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (57, '语音管理', 'speech:synthesis', '/speech/synthesis', 56, NULL, '1', '0', 'admin', '2022-06-16 22:28:53', 'admin', '2022-11-13 11:48:52', 0, 'speech/synthesis/index', 0, 0, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (58, '运营管理', 'operation', '/operation', -1, 'ant-design:bold-outlined', '4', '0', 'admin', '2022-11-12 12:02:00', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, NULL, 'gitee', 0, '/operation', '');
INSERT INTO `sys_menu` VALUES (59, 'Banner管理', 'operation:banner', '/operation/banner', 58, NULL, '1', '0', 'admin', '2022-11-12 12:00:10', 'admin', '2022-11-13 11:48:52', 0, 'operation/banner/index', 0, 0, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (60, '个人中心', 'personal', '/personal', -1, 'ant-design:user-outlined', '99', '0', 'admin', '2022-04-15 22:35:22', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, NULL, '个人管理', 'gitee', 0, '/personal/details', '');
INSERT INTO `sys_menu` VALUES (61, '个人资料', 'personal:message', '/personal/details', 60, '', '29', '0', 'admin', '2019-04-26 15:00:11', 'admin', '2022-11-13 11:48:52', 0, 'personal/details/index', 0, NULL, '个人资料', 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (62, '修改密码', 'personal:password', '/personal/password', 60, '', '30', '0', 'admin', '2019-04-26 15:01:18', 'admin', '2022-11-13 11:48:52', 0, 'personal/password/index', 0, NULL, '修改密码', 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (63, '工作台', NULL, '/dashboard/workbench', 1, NULL, '2', '0', 'admin', '2022-03-21 21:26:05', 'admin', '2022-11-13 11:48:52', 0, 'dashboard/workbench/index', 0, NULL, NULL, 'gitee', 0, '', '');

INSERT INTO `sys_menu` VALUES (64, '成绩详情', NULL, '/exam/score_detail/:id', 28, NULL, '8', '0', 'admin', '2022-04-12 13:35:12', 'admin', '2022-11-13 11:48:52', 0, 'exam/scoreDetail/index', 0, 0, NULL, 'gitee', 1, '', '/exam/score');
INSERT INTO `sys_menu` VALUES (65, '题目详情', NULL, '/exam/subject_detail/:id', 28, NULL, '6', '0', 'admin', '2022-04-05 09:54:38', 'admin', '2022-11-13 11:48:52', 0, 'exam/subject/SubjectDetail', 0, 0, NULL, 'gitee', 1, '', '/exam/subject');

INSERT INTO `sys_menu` VALUES (67, '操作日志', 'sys:log', '/sys/log', 3, NULL, '11', '0', 'admin', '2022-11-20 14:16:34', 'admin', '2022-11-20 14:16:34', 0, 'sys/log/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (68, '查看操作日志', 'sys:log:view', NULL, 67, NULL, '1', '1', 'admin', '2022-11-20 14:20:58', 'admin', '2022-11-20 14:20:58', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (73, '查看考试', 'exam:exam:view', NULL, 33, NULL, '1', '1', 'admin', '2022-11-20 18:44:51', 'admin', '2022-11-20 18:44:51', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (74, '编辑考试', 'exam:exam:edit', NULL, 33, NULL, '2', '1', 'admin', '2022-11-20 18:45:11', 'admin', '2022-11-20 18:45:11', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (75, '查看成绩', 'exam:examRecord:view', NULL, 50, NULL, '1', '1', 'admin', '2022-11-20 18:47:23', 'admin', '2022-11-20 18:47:23', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (76, '删除操作日志', 'sys:log:del', NULL, 67, NULL, '2', '1', 'admin', '2022-11-20 18:50:33', 'admin', '2022-11-20 18:50:33', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (77, '查看单位', 'tenant:tenant:view', NULL, 25, NULL, '1', '1', 'admin', '2022-11-20 18:55:12', 'admin', '2022-11-20 18:55:12', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (78, '新增单位', 'tenant:tenant:add', NULL, 25, NULL, '1', '1', 'admin', '2022-11-20 18:57:18', 'admin', '2022-11-20 18:57:18', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (79, '查看用户', 'sys:user:view', NULL, 10, NULL, '1', '1', 'admin', '2022-11-20 19:00:04', 'admin', '2022-11-20 19:00:04', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (81, '查看角色', 'sys:role:view', NULL, 20, NULL, '1', '1', 'admin', '2022-11-20 19:00:55', 'admin', '2022-11-20 19:00:55', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (82, '查看菜单', 'sys:menu:view', NULL, 4, NULL, '1', '1', 'admin', '2022-11-20 19:01:21', 'admin', '2022-11-20 19:01:21', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (83, '查看部门', 'sys:dept:view', NULL, 16, NULL, '1', '1', 'admin', '2022-11-20 19:12:41', 'admin', '2022-11-20 19:12:41', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (84, '查看课程', 'exam:course:view', NULL, 29, NULL, '1', '1', 'admin', '2022-11-20 19:13:01', 'admin', '2022-11-20 19:13:01', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (85, '代码生成', 'sys:gen', '/sys/gen', 3, NULL, '12', '0', 'admin', '2022-11-21 20:54:36', 'admin', '2022-11-21 20:54:36', 0, 'sys/gen/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO `sys_menu` VALUES (86, '查看代码生成', 'sys:gen:view', NULL, 85, NULL, '1', '1', 'admin', '2022-11-21 20:55:13', 'admin', '2022-11-21 20:55:13', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (87, '编辑代码生成', 'sys:gen:edit', NULL, 85, NULL, '2', '1', 'admin', '2022-11-21 20:55:33', 'admin', '2022-11-21 20:55:33', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (88, '新增代码生成', 'sys:gen:add', NULL, 85, NULL, '3', '1', 'admin', '2022-11-21 20:55:56', 'admin', '2022-11-21 20:55:56', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (89, '删除代码生成', 'sys:gen:del', NULL, 85, NULL, '4', '1', 'admin', '2022-11-21 20:56:12', 'admin', '2022-11-21 20:56:12', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO `sys_menu` VALUES (90, '课程章节', NULL, '/exam/course_chapter/:id', 28, NULL, '9', '0', 'admin', '2022-11-21 22:53:11', 'admin', '2022-11-21 22:53:11', 0, 'exam/courseChapter/index', 0, 0, NULL, 'gitee', 1, '', '/exam/course');
INSERT INTO `sys_menu` VALUES (91, '查看题库', 'exam:subject:bank:view', NULL, 41, NULL, '1', '1', 'admin', '2022-11-30 22:42:14', 'admin', '2022-11-30 22:42:14', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');

-- ----------------------------
-- 角色菜单关系
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (4, 1, 4, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (5, 1, 5, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (6, 1, 6, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (7, 1, 7, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (8, 1, 8, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (9, 1, 9, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (10, 1, 10, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (11, 1, 11, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (12, 1, 12, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (13, 1, 13, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (14, 1, 14, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (15, 1, 15, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (16, 1, 16, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (17, 1, 17, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (18, 1, 18, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (19, 1, 19, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (20, 1, 20, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (21, 1, 21, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (22, 1, 22, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (23, 1, 23, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (24, 1, 24, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (25, 1, 25, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (26, 1, 26, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (27, 1, 27, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (28, 1, 28, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (29, 1, 29, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (30, 1, 30, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (31, 1, 31, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (32, 1, 32, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (33, 1, 33, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (34, 1, 34, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (35, 1, 35, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (36, 1, 36, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (37, 1, 37, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (38, 1, 38, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (39, 1, 39, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (40, 1, 40, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (41, 1, 41, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (42, 1, 42, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (43, 1, 43, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (44, 1, 44, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (45, 1, 45, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (46, 1, 46, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (47, 1, 47, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (48, 1, 48, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (49, 1, 49, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (50, 1, 50, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (51, 1, 51, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (53, 1, 53, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (54, 1, 54, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (55, 1, 55, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (56, 1, 56, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (57, 1, 57, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (58, 1, 58, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (59, 1, 59, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (60, 1, 60, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (61, 1, 61, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (62, 1, 62, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (63, 1, 63, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (64, 1, 64, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (65, 1, 65, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');

INSERT INTO `sys_role_menu` VALUES (67, 2, 1, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO `sys_role_menu` VALUES (68, 2, 2, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO `sys_role_menu` VALUES (69, 2, 63, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO `sys_role_menu` VALUES (70, 2, 61, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO `sys_role_menu` VALUES (72, 2, 60, '', '2022-11-20 14:46:39', '', '2022-11-20 14:46:39', 0, '');

INSERT INTO `sys_role_menu` VALUES (73, 1, 67, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO `sys_role_menu` VALUES (74, 1, 68, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO `sys_role_menu` VALUES (79, 1, 73, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (80, 1, 74, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (81, 1, 75, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (82, 1, 76, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (83, 1, 77, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (84, 1, 78, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (85, 1, 79, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (86, 1, 80, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (87, 1, 81, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (88, 1, 82, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (89, 1, 83, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO `sys_role_menu` VALUES (90, 1, 84, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');

INSERT INTO `sys_role_menu` VALUES (119, 2, 77, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (120, 2, 79, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (121, 2, 81, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (122, 2, 82, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (123, 2, 68, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (124, 2, 69, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (125, 2, 73, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (126, 2, 75, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (127, 2, 83, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (128, 2, 84, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (129, 2, 25, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (130, 2, 3, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (131, 2, 10, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (132, 2, 20, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (133, 2, 4, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (134, 2, 67, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (136, 2, 28, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (137, 2, 33, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (138, 2, 50, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (139, 2, 16, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO `sys_role_menu` VALUES (140, 2, 29, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');

INSERT INTO `sys_role_menu` VALUES (141, 1, 85, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (142, 1, 86, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (143, 1, 87, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (144, 1, 88, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO `sys_role_menu` VALUES (145, 1, 89, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');

INSERT INTO `sys_role_menu` VALUES (146, 2, 86, '', '2022-11-21 20:58:13', '', '2022-11-21 20:58:13', 0, '');
INSERT INTO `sys_role_menu` VALUES (147, 2, 85, '', '2022-11-21 20:58:13', '', '2022-11-21 20:58:13', 0, '');
INSERT INTO `sys_role_menu` VALUES (406, 1, 90, '', '2022-11-21 23:00:52', '', '2022-11-21 23:00:52', 0, '');
INSERT INTO `sys_role_menu` VALUES (426, 2, 90, '', '2022-11-21 23:01:06', '', '2022-11-21 23:01:06', 0, '');
INSERT INTO `sys_role_menu` VALUES (445, 2, 91, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');

-- ----------------------------
-- 默认头像
-- ----------------------------
INSERT INTO `sys_attachment` VALUES (1, '24.jpeg', 'jpeg', '168574', '', 'admin', '2022-07-02 21:37:46', 'admin', '2022-07-02 21:37:46', 0, 'gitee', 'user_avatar');
INSERT INTO `sys_attachment` VALUES (2, '24.jpeg', 'jpeg', '168574', '', 'admin', '2022-07-02 21:37:46', 'admin', '2022-07-02 21:37:46', 0, 'gitee', 'user_avatar');
INSERT INTO `sys_attachment` VALUES (3, '23.jpeg', 'jpeg', '168574', '', 'admin', '2022-07-02 21:37:46', 'admin', '2022-07-02 21:37:46', 0, 'gitee', 'default_image');

-- ----------------------------
-- 附件分组
-- ----------------------------
INSERT INTO `sys_attachment_group` VALUES (1, '用户头像', 'user_avatar', 00000000002147483647, 'admin', '2022-04-16 16:17:55', 'admin', '2022-07-02 21:01:20', 0, 'gitee', '用户头像');
INSERT INTO `sys_attachment_group` VALUES (2, '课程图片', 'course', 00000000002147483647, 'admin', '2022-04-16 16:46:43', 'admin', '2022-07-02 21:01:05', 0, 'gitee', '课程图片');
INSERT INTO `sys_attachment_group` VALUES (3, '考试图片', 'exam', 00000000002147483647, 'admin', '2022-04-16 16:46:31', 'admin', '2022-07-02 21:01:30', 0, 'gitee', '考试图片');
INSERT INTO `sys_attachment_group` VALUES (4, '其它', 'other', 00000000002147483647, 'admin', '2022-04-16 16:49:32', 'admin', '2022-07-02 21:23:37', 0, 'gitee', '其它');
INSERT INTO `sys_attachment_group` VALUES (5, '语音合成', 'speech', 00000000002147483647, 'admin', '2022-06-18 13:33:29', 'admin', '2022-07-02 21:00:45', 0, 'gitee', '语音合成');
INSERT INTO `sys_attachment_group` VALUES (6, '默认分组', 'default', 00000000002147483647, 'admin', '2022-07-02 20:26:48', 'admin', '2022-07-02 21:00:51', 0, 'gitee', '默认分组');
INSERT INTO `sys_attachment_group` VALUES (7, '默认图片', 'default_image', 00000000002147483647, 'admin', '2022-07-02 22:05:36', 'admin', '2022-07-02 22:05:36', 0, 'gitee', '默认图片');
INSERT INTO `sys_attachment_group` VALUES (8, '考务视频', 'exam/video', 00000000002147483647, 'admin', '2022-12-04 22:38:13', 'admin', '2022-12-04 22:38:13', 0, 'gitee', '考务视频');
INSERT INTO `sys_attachment_group` VALUES (9, '考务图片', 'exam/image', 00000000002147483647, 'admin', '2022-12-04 22:38:30', 'admin', '2022-12-04 22:38:30', 0, 'gitee', '考务图片');

-- ----------------------------
-- 小程序首页运营位
-- ----------------------------
INSERT INTO `operation_banner` VALUES (1, 'admin', '2022-11-12 12:42:26', 'admin', '2022-11-12 12:52:00', 0, 'gitee', '运营位Banner1', 0, 'https://cdn.yunmianshi.com/default_image/10.jpeg?e=1668228237&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:ZW1CihRYlSwQRBlybYWbX8lrz48=', 'https://cdn.yunmianshi.com/default_image/10.jpeg?e=1668228237&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:ZW1CihRYlSwQRBlybYWbX8lrz48=', '2232323', NULL);
INSERT INTO `operation_banner` VALUES (2, 'admin', '2022-11-12 12:43:24', 'admin', '2022-11-12 12:51:56', 0, 'gitee', '运营位Banner2', 0, 'https://cdn.yunmianshi.com/default_image/13.jpeg?e=1668228494&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:REKRNJMtCekDjmrMkSvbLOuWAvM=', 'https://cdn.yunmianshi.com/default_image/13.jpeg?e=1668228494&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:REKRNJMtCekDjmrMkSvbLOuWAvM=', '3', NULL);

-- ----------------------------
-- 考试
-- ----------------------------
INSERT INTO `exam_examination` VALUES (1, '语文考试', 0, '语文考试', '2022-11-13 14:22:02', '2023-11-20 14:22:02', 25, 0, 3, 1, '语文考试 https://www.zgshici.com/etagid224722b0/', 'admin', '2022-11-13 14:22:55', 'admin', '2022-11-13 14:22:55', 0, 'gitee', 0000000001, '语文', 0);

-- ----------------------------
-- 题目
-- ----------------------------
INSERT INTO `exam_examination_subject` VALUES (1, 1, 1, 'gitee', 01, 'admin', '2022-11-13 14:41:41', 'admin', '2022-11-13 14:41:41', 0);
INSERT INTO `exam_examination_subject` VALUES (2, 1, 2, 'gitee', 02, 'admin', '2022-11-13 14:42:34', 'admin', '2022-11-13 14:42:34', 0);
INSERT INTO `exam_examination_subject` VALUES (3, 1, 3, 'gitee', 03, 'admin', '2022-11-13 14:43:14', 'admin', '2022-11-13 14:43:14', 0);
INSERT INTO `exam_examination_subject` VALUES (4, 1, 4, 'gitee', 04, 'admin', '2022-11-13 14:43:47', 'admin', '2022-11-13 14:43:47', 0);
INSERT INTO `exam_examination_subject` VALUES (5, 1, 5, 'gitee', 05, 'admin', '2022-11-13 14:44:29', 'admin', '2022-11-13 14:44:29', 0);

INSERT INTO `exam_subject_choices` VALUES (1, 0, '<p>《山行》是描绘了___的景色.</p>', 0, 'C', 5, NULL, 1, 'admin', '2022-11-13 14:41:40', 'admin', '2022-11-13 14:41:40', 0, 'gitee', 0000000001, NULL);
INSERT INTO `exam_subject_choices` VALUES (2, 0, '<p>&ldquo;劝君更尽一杯酒，西出阳关无故人.&rdquo;出自___的名句</p>', 0, 'B', 5, NULL, 1, 'admin', '2022-11-13 14:42:33', 'admin', '2022-11-13 14:42:33', 0, 'gitee', 0000000002, NULL);
INSERT INTO `exam_subject_choices` VALUES (3, 0, '<p>把&ldquo;春风&rdquo;比作&ldquo;剪刀&rdquo;的是哪首诗？</p>', 0, 'C', 5, NULL, 1, 'admin', '2022-11-13 14:43:13', 'admin', '2022-11-13 14:43:13', 0, 'gitee', 0000000003, NULL);
INSERT INTO `exam_subject_choices` VALUES (4, 0, '<p>&ldquo;横看成岭侧成峰，远近高低各不同.&rdquo;诗中写的名胜是</p>', 0, 'D', 5, NULL, 1, 'admin', '2022-11-13 14:43:46', 'admin', '2022-11-13 14:43:46', 0, 'gitee', 0000000004, NULL);
INSERT INTO `exam_subject_choices` VALUES (5, 0, '<p>&ldquo;解落三秋叶，能开二月花.过江千尺浪，入竹万竿斜.&rdquo;这首诗写的是</p>', 0, 'B', 5, NULL, 1, 'admin', '2022-11-13 14:44:29', 'admin', '2022-11-13 14:44:29', 0, 'gitee', 0000000005, NULL);

INSERT INTO `exam_subjects` VALUES (1, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 1, 1);
INSERT INTO `exam_subjects` VALUES (2, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 2, 2);
INSERT INTO `exam_subjects` VALUES (3, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 3, 3);
INSERT INTO `exam_subjects` VALUES (4, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 4, 4);
INSERT INTO `exam_subjects` VALUES (5, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 5, 5);

INSERT INTO `exam_subject_option` VALUES (1, 1, 'A', '<p>春天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO `exam_subject_option` VALUES (2, 1, 'B', '<p>夏天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO `exam_subject_option` VALUES (3, 1, 'C', '<p>秋天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO `exam_subject_option` VALUES (4, 1, 'D', '<p>冬天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);

INSERT INTO `exam_subject_option` VALUES (5, 2, 'A', '<p>李白</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO `exam_subject_option` VALUES (6, 2, 'B', '<p>王维</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO `exam_subject_option` VALUES (7, 2, 'C', '<p>王昌龄</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO `exam_subject_option` VALUES (8, 2, 'D', '<p>杜牧</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);

INSERT INTO `exam_subject_option` VALUES (9, 3, 'A', '<p>《忆江南》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO `exam_subject_option` VALUES (10, 3, 'B', '<p>《滁州西涧》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO `exam_subject_option` VALUES (11, 3, 'C', '<p>《咏柳》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO `exam_subject_option` VALUES (12, 3, 'D', '<p>《游园不值》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);

INSERT INTO `exam_subject_option` VALUES (13, 4, 'A', '<p>泰山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO `exam_subject_option` VALUES (14, 4, 'B', '<p>华山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO `exam_subject_option` VALUES (15, 4, 'C', '<p>黄山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO `exam_subject_option` VALUES (16, 4, 'D', '<p>庐山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);

INSERT INTO `exam_subject_option` VALUES (17, 5, 'A', '<p>花</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO `exam_subject_option` VALUES (18, 5, 'B', '<p>风</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO `exam_subject_option` VALUES (19, 5, 'C', '<p>竹</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO `exam_subject_option` VALUES (20, 5, 'D', '<p>水</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);

-- ----------------------------
-- 题目分类
-- ----------------------------
INSERT INTO `exam_subject_category` VALUES (1, '语文', NULL, -1, 1, 0, 'admin', '2022-11-13 15:33:49', 'admin', '2022-11-13 15:33:49', 0, 'gitee');

INSERT INTO `exam_course` VALUES (1, '义务教育语文学科课程', NULL, NULL, NULL, '义务教育语文学科课程', 'admin', '2022-11-26 11:00:04', 'admin', '2022-11-26 11:00:04', 0, 'gitee', 4, NULL, NULL, 0, 0.00, 3, '义务教育语文学科课程', 100, 0);

INSERT INTO `exam_course_chapter` VALUES (1, '单元一 四季美景', 1, 1, 'admin', '2022-11-26 11:00:23', 'admin', '2022-11-26 11:02:24', 0, 'gitee', '单元一 四季美景');
INSERT INTO `exam_course_chapter` VALUES (2, '单元二 至情至爱', 2, 1, 'admin', '2022-11-26 11:02:14', 'admin', '2022-11-26 11:02:14', 0, 'gitee', '单元二 至情至爱');
INSERT INTO `exam_course_chapter` VALUES (3, '单元三 学习生活', 3, 1, 'admin', '2022-11-26 11:02:37', 'admin', '2022-11-26 11:02:37', 0, 'gitee', '单元三 学习生活');
INSERT INTO `exam_course_chapter` VALUES (4, '单元四 人生之舟', 4, 1, 'admin', '2022-11-26 11:02:52', 'admin', '2022-11-26 11:02:52', 0, 'gitee', '单元四 人生之舟');
INSERT INTO `exam_course_chapter` VALUES (5, '单元五 动物与人', 5, 1, 'admin', '2022-11-26 11:03:03', 'admin', '2022-11-26 11:03:03', 0, 'gitee', '单元五 动物与人');
INSERT INTO `exam_course_chapter` VALUES (6, '单元六 想象之翼', 6, 1, 'admin', '2022-11-26 11:03:17', 'admin', '2022-11-26 11:03:17', 0, 'gitee', '单元六 想象之翼');

INSERT INTO `exam_course_section` VALUES (2, '1．春/朱自清', 1, 1, 1, NULL, 'admin', '2022-11-26 11:03:37', 'admin', '2022-11-26 11:04:28', 0, 'gitee', '1．春/朱自清', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (3, '2．济南的冬天/老舍', 2, 1, 1, NULL, 'admin', '2022-11-26 11:03:50', 'admin', '2022-11-26 11:04:37', 0, 'gitee', '2．济南的冬天/老舍', NULL, 1, '');
INSERT INTO `exam_course_section` VALUES (4, '3．雨的四季/刘湛秋', 3, 1, 1, NULL, 'admin', '2022-11-26 11:04:17', 'admin', '2022-11-26 11:04:17', 0, 'gitee', '3．雨的四季/刘湛秋', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (5, '4．古代诗歌 观沧海/曹操', 4, 1, 1, NULL, 'admin', '2022-11-26 11:06:01', 'admin', '2022-11-26 11:06:01', 0, 'gitee', '4．古代诗歌 观沧海/曹操', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (6, '5．秋天的怀念/史铁生', 1, 2, 1, NULL, 'admin', '2022-11-26 11:06:29', 'admin', '2022-11-26 11:06:29', 0, 'gitee', '5．秋天的怀念/史铁生', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (7, '6．散步/莫怀戚', 2, 2, 1, NULL, 'admin', '2022-11-26 11:06:41', 'admin', '2022-11-26 11:06:41', 0, 'gitee', '6．散步/莫怀戚', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (8, '7．散文诗两首金色花/泰戈尔', 100, 2, 1, NULL, 'admin', '2022-11-26 11:06:54', 'admin', '2022-11-26 11:06:54', 0, 'gitee', '7．散文诗两首金色花/泰戈尔', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (9, '8.《世说新语》两则咏雪', 100, 2, 1, NULL, 'admin', '2022-11-26 11:07:05', 'admin', '2022-11-26 11:07:05', 0, 'gitee', '8.《世说新语》两则咏雪', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (10, '9．从百草园到三味书屋/鲁迅', 1, 3, 1, NULL, 'admin', '2022-11-26 11:07:24', 'admin', '2022-11-26 11:07:24', 0, 'gitee', '9．从百草园到三味书屋/鲁迅', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (11, '10．再塑生命的人/海伦•凯勒', 2, 3, 1, NULL, 'admin', '2022-11-26 11:07:40', 'admin', '2022-11-26 11:07:40', 0, 'gitee', '10．再塑生命的人/海伦•凯勒', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (12, '11．窃读记/林海音', 3, 3, 1, NULL, 'admin', '2022-11-26 11:07:58', 'admin', '2022-11-26 11:07:58', 0, 'gitee', '11．窃读记/林海音', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (13, '12．《论语》十二章', 4, 3, 1, NULL, 'admin', '2022-11-26 11:08:12', 'admin', '2022-11-26 11:08:12', 0, 'gitee', '12．《论语》十二章', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (14, '13．纪念白求恩/毛泽东', 1, 4, 1, NULL, 'admin', '2022-11-26 11:08:42', 'admin', '2022-11-26 11:08:42', 0, 'gitee', '13．纪念白求恩/毛泽东', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (15, '17．猫/郑振铎', 1, 5, 1, NULL, 'admin', '2022-11-26 11:09:05', 'admin', '2022-11-26 11:09:05', 0, 'gitee', '17．猫/郑振铎', NULL, 0, '');
INSERT INTO `exam_course_section` VALUES (16, '21．皇帝的新装/安徒生', 1, 6, 1, NULL, 'admin', '2022-11-26 11:09:21', 'admin', '2022-11-26 11:09:21', 0, 'gitee', '21．皇帝的新装/安徒生', NULL, 0, '');
