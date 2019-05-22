/*
 Navicat Premium Data Transfer

 Source Server         : mysql_localhost
 Source Server Type    : MySQL
 Source Server Version : 50617
 Source Host           : localhost:3306
 Source Schema         : microservice-exam

 Target Server Type    : MySQL
 Target Server Version : 50617
 File Encoding         : 65001

 Date: 22/05/2019 22:21:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `examination_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试ID',
  `exam_record_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试记录id',
  `course_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程ID',
  `subject_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目ID',
  `answer` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '答案',
  `option_answer` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选择题答案',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_answer`(`user_id`, `examination_id`, `exam_record_id`, `subject_id`) USING BTREE COMMENT '答题索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `course_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `college` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业',
  `teacher` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老师',
  `course_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程描述',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('571449673634680832', '大学语文', '信息学院', '软件工程', '陈老师', '大学语文', 'admin', '2019-04-26 21:36:59', 'admin', '2019-04-26 21:36:59', '0', 'EXAM', NULL);
INSERT INTO `course` VALUES ('573843243792470016', '计算机基础', '信息学院', '软件工程', '', '', 'admin', '2019-05-03 12:08:10', 'admin', '2019-05-22 22:17:41', '0', 'EXAM', NULL);

-- ----------------------------
-- Table structure for exam_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `examination_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试id',
  `examination_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试名称',
  `course_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程id',
  `start_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `score` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成绩',
  `correct_number` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正确题目数量',
  `incorrect_number` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误题目数量',
  `submit_status` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交状态 1-已提交 0-未提交',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for examination
-- ----------------------------
DROP TABLE IF EXISTS `examination`;
CREATE TABLE `examination`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `examination_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试名称',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试类型',
  `attention` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试注意事项',
  `start_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试开始时间',
  `end_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试结束时间',
  `duration` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试持续时间',
  `total_score` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '总分',
  `total_subject` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目数',
  `status` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试状态',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面',
  `avatar_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `college_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业',
  `course_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of examination
-- ----------------------------
INSERT INTO `examination` VALUES ('571450640874737664', '四川省2016年普通高考文科综合能力测试-语文部分', '0', '注意事项:\n1.本试卷分第工卷(选择题)和第II卷(非选择题)两部分。答卷前，考生务必将白己的姓名、准考证号填写在答题卡上。\n2.回答第Ⅰ卷时，选出每小题答案后，用铅笔把答题卡上对应题目的答案标号涂黑。如需改动，用橡皮擦干净后，再选涂其它答案标号。写在本试卷上无效。\n3.回答第Ⅱ卷时，将答案写在答题卡上。写在本试卷上无效。\n4.考试结束后，将本试卷和答题卡一并交回。', '2019-05-19 07:56', '2019-05-19 23:56', '', '48', '12', '0', '', NULL, '信息学院', '软件工程', '571449673634680832', '四川省2016年普通高考-文科综合能力测试', 'admin', '2019-04-26 21:40:49', 'admin', '2019-05-19 21:38:37', '0', 'EXAM', NULL);
INSERT INTO `examination` VALUES ('571460951203778560', '四川省2016年普通高考文科综合能力测试-地理部分', '0', '本卷共35个小题，每小题4分，共140分。在每小题给出的四个选项中，只有一项是符合题目要求的。', '2019-05-07 15:40', '2019-05-07 23:40', '', '200', '0', '0', '', NULL, '信息学院', '软件工程', '571449673634680832', '四川省2016年普通高考文科综合能力测试-地理部分', 'admin', '2019-04-26 22:21:47', 'admin', '2019-04-26 22:21:47', '1', 'EXAM', NULL);
INSERT INTO `examination` VALUES ('573841891515305984', '全国计算机统考练习题10道', '2', '练习', '2019-05-19 15:40', '2019-05-19 23:40', '', '40', '10', '0', '', NULL, '测试学院', '软件工程', '573843243792470016', '全国计算机统考练习题10道', 'admin', '2019-05-03 12:02:48', 'admin', '2019-05-19 21:38:12', '0', 'EXAM', NULL);
INSERT INTO `examination` VALUES ('576869700143812608', '测试1', '0', '323', '2019-05-16 20:37', '2019-05-16 23:34', '', '20', '3', '0', 'group1/M00/00/00/rBAAClzXxROAC5s0AADGqyYhFOI619.jpg', '577147187818008576', '32323', '23232', '571449673634680832', '323', 'admin', '2019-05-11 20:34:14', 'admin', '2019-05-16 22:19:45', '0', 'EXAM', NULL);
INSERT INTO `examination` VALUES ('577225388753817600', '测试2', '0', '22', '2019-05-14 20:07', '2019-05-14 23:00', '', '2', '0', '0', '', NULL, '22', '2', '571449673634680832', '2222', 'admin', '2019-05-12 20:07:37', 'admin', '2019-05-14 21:02:19', '0', 'EXAM', NULL);
INSERT INTO `examination` VALUES ('577225454889603072', '测试3', '0', '33', '2019-05-12 20:07', '2019-05-13 00:00', '', '3', '0', '0', 'group1/M00/00/00/rBAAClzYEqeAQRfVAAB2nqimI80762.jpg', '577230488272506880', '3', '3', '573843243792470016', '', 'admin', '2019-05-12 20:07:52', 'admin', '2019-05-12 20:27:53', '0', 'EXAM', NULL);
INSERT INTO `examination` VALUES ('577235497684963328', '测试4', '0', '33', '2019-05-12 20:47', '2019-05-13 20:47', '', '3', '0', '0', '', NULL, '3333', '3', '', '333', 'admin', '2019-05-12 20:47:47', 'admin', '2019-05-22 22:17:49', '0', 'EXAM', NULL);

-- ----------------------------
-- Table structure for incorrect_answer
-- ----------------------------
DROP TABLE IF EXISTS `incorrect_answer`;
CREATE TABLE `incorrect_answer`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考生ID',
  `examination_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试ID',
  `exam_record_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试记录ID',
  `subject_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目ID',
  `serial_number` int(64) NULL DEFAULT NULL COMMENT '题目序号',
  `incorrect_answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误答案',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for knowledge
-- ----------------------------
DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `knowledge_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识名称',
  `knowledge_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识描述',
  `attachment_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件ID',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `examination_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试ID',
  `serial_number` int(64) NULL DEFAULT NULL COMMENT '序号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目名称',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目类型',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目内容',
  `option_a` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项a',
  `option_b` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项b',
  `option_c` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项c',
  `option_d` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项d',
  `option_e` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项e',
  `option_f` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项f',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `score` int(64) NULL DEFAULT NULL COMMENT '分值',
  `analysis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解析',
  `level` int(64) NULL DEFAULT NULL COMMENT '难度等级',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES ('571452549010427904', '571450640874737664', 1, '《小雅·鹿鸣》本是西周贵族宣扬宴飨之仪的乐歌，后扩散到民间，在乡人宴会上也可传唱。这表明西周时期', '0', '', '周人生活较为富足', '礼乐文明得到广泛认同', '乡人社会地位提高', '贵族奢靡之风波及民间', '', '', 'B', 4, '', 2, 'admin', '2019-04-26 21:48:24', 'admin', '2019-04-26 21:48:24', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571453252617506816', '571450640874737664', 2, '公元前 212 年，秦始皇坑杀“术士”，长子扶苏劝谏说：“远方黔首未集，诸生皆诵法孔子，今上皆重法绳之，臣恐天下不安，唯上察之”。这反映当时', '0', '', '统一进程曲折', '地方治理不畅', '始皇灭儒崇法', '儒学影响较大', '', '', 'D', 4, '', 2, 'admin', '2019-04-26 21:51:12', 'admin', '2019-04-26 21:51:12', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571453393525149696', '571450640874737664', 3, '唐前期的政治人物多为北方人，北宋时政治人物多出生于江西、福建、苏南等地。\n这一变化主要反映了', '0', '', '官僚集团重视本地域人才', '南北方士人志向差异', '科举制改变人才地域分布', '政治中心转移到南方', '', '', 'C', 4, '', 2, 'admin', '2019-04-26 21:51:46', 'admin', '2019-04-26 21:51:46', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571453523254972416', '571450640874737664', 5, '《荆楚岁时记》云：“社日，四邻并结宗会社，宰牲牢，为屋于树下，先祭神，然后食其胙。”据此可知，社日的民俗功能主要是', '0', '', '联谊乡邻', '颂扬盛世', '缅怀先祖', '助危济困', '', '', 'A', 4, '', 2, 'admin', '2019-04-26 21:52:17', 'admin', '2019-04-26 22:08:03', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571453689655595008', '571450640874737664', 4, '李鸿章凭淮军实力日渐强盛。一次，他在游孔林时说道：“孔子不会打洋枪，今不足贵也。”李鸿章这样评价孔子，其背景最可能是', '0', '', '“师夷长技”思想萌发', '“中体西用”思潮兴起', '“中体西用”思潮兴起', '“尊孔复古”思潮泛滥', '', '', 'B', 4, '', 2, 'admin', '2019-04-26 21:52:56', 'admin', '2019-04-26 21:52:56', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571457619294818304', '571450640874737664', 6, '李鸿章凭淮军实力日渐强盛。一次，他在游孔林时说道：“孔子不会打洋枪，今不足贵也。”李鸿章这样评价孔子，其背景最可能是', '0', '', '“师夷长技”思想萌发 ', '“中体西用”思潮兴起', '“托古改制”思想产生', '“尊孔复古”思潮泛滥', '', '', 'B', 4, '', 2, 'admin', '2019-04-26 22:08:33', 'admin', '2019-04-26 22:08:33', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571457724336967680', '571450640874737664', 7, '1930 年，中共中央在回应共产国际指示时说：党的任务决不是准备夺取部分的政权，如果认为现在还是准备夺取部分的政权,无疑是对革命形势估量不足的右倾观念。这一回应表明', '0', '', '中共夺取全国政权时机成熟', '中共找到符合国情的道路', '中共找到符合国情的道路', '中共出现自主革命的倾向', '', '', 'D', 4, '', 2, 'admin', '2019-04-26 22:08:58', 'admin', '2019-04-26 22:08:58', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571457810760601600', '571450640874737664', 8, '1938 年初，国民政府颁布多部法规，要求将每一工厂、商号、银行、钱庄都纳入到同业工会内，又将每一同业工会纳入当地商会内。这些法规', '0', '', '抑制了官僚资本膨胀', '挫败了日军经济掠夺', '防止了国民经济崩溃 ', '积聚了抗战经济力量', '', '', 'D', 4, '', 2, 'admin', '2019-04-26 22:09:19', 'admin', '2019-04-26 22:09:19', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571457947507494912', '571450640874737664', 9, '古代雅典法律规定：每个公民从出生起，城邦就是他的最高监护人，要按城邦的需要来抚养和教育。这反映出雅典', '0', '', '公民培养受强制性约束', '法律有违人文精神', '父母失去教育子女资格', '公民教育制度完备', '', '', 'A', 4, '', 2, 'admin', '2019-04-26 22:09:51', 'admin', '2019-04-26 22:09:51', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571458049324224512', '571450640874737664', 10, '19 世纪末 20 世纪初，一向傲慢的英国人惊奇地发现：“身上的衣服是德国缝制的，少女们周末穿的漂亮披风与上衣来自德国。更让人吃惊的是生活中有许多东西都产自德国，……连周末歌剧院里上演的歌剧也是德国人创作的，演员无一例外是德国人。”可见，当时傲慢的英国人', '0', '', '推崇德国文化', '注意到了德国的威胁', '喜爱德国产品', '意识到了德国的崛起', '', '', 'D', 4, '', 2, 'admin', '2019-04-26 22:10:16', 'admin', '2019-04-26 22:10:16', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571458149744250880', '571450640874737664', 11, '1920–1921 年，苏俄许多工人流往农村，还有一些则自谋生路成了小手工业者。据此推知当时苏俄', '0', '', '战时共产主义政策有所突破', '新经济政策成效显著', '国营农庄吸引了大量劳动力', '政府重视日用品生产', '', '', 'A', 4, '', 2, 'admin', '2019-04-26 22:10:40', 'admin', '2019-04-26 22:10:40', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571458244669739008', '571450640874737664', 12, '1964 年 6 月，美国《时代》杂志发表社论指出：“从北约到联合国，从拉丁美洲到红色中国，几乎在世界政治中的每一个问题或地区上，法国都采取和美国政策不一致的态度。”这一社论', '0', '', '揭示了法国倒向社会主义阵营', '反映了法国推行独立外交', '体现了两大阵营对抗趋于缓和', '体现了两大阵营对抗趋于缓和', '', '', 'B', 4, '', 2, 'admin', '2019-04-26 22:11:02', 'admin', '2019-04-26 22:11:02', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('571461232968732672', '571460951203778560', 1, '若下列城市都安装了“风光互补路灯”，北半球夏至日这一天太阳能板左右摆动幅度最大的地点是', '0', '', '哈尔滨', '北京', '广州', '海口', '', '', 'A', 4, '', 2, 'admin', '2019-04-26 22:22:55', 'admin', '2019-04-26 22:22:55', '1', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843792789114880', '573841891515305984', 1, '自计算机问世至今已经经历了四个时代，划分时代的主要依据是计算机的_', '0', '', '规模', '功能', '性能', '构成元件', '', '', 'D', 4, '', 2, 'admin', '2019-05-03 12:10:21', 'admin', '2019-05-03 12:10:21', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843833855545344', '573841891515305984', 2, '第一台计算机是在1946年在美国诞生，该机的英文缩写是_', '0', '', 'ENIAC', 'EDVAC', 'EDVAE', 'MARK', '', '', 'A', 4, '', 2, 'admin', '2019-05-03 12:10:31', 'admin', '2019-05-03 12:10:31', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843857750495232', '573841891515305984', 3, '当前的计算机一般被认为是第四代计算机，它所采用的逻辑元件是_', '0', '', '集成电路', '晶体管', '大规模集成电路', '电子管', '', '', 'C', 4, '', 2, 'admin', '2019-05-03 12:10:37', 'admin', '2019-05-03 12:10:37', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843879313412096', '573841891515305984', 4, '当前计算机的应用最广泛的领域是_', '0', '', '辅助设计', '科学计算', '数据处理', '过程控制', '', '', 'C', 4, '', 2, 'admin', '2019-05-03 12:10:42', 'admin', '2019-05-03 12:10:42', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843899269910528', '573841891515305984', 5, '早期的计算机体积大、耗电多、速度慢，其主要原因是制约于_', '0', '', '元材料', '工艺水平', '设计水平', '元器件', '', '', 'D', 4, '', 2, 'admin', '2019-05-03 12:10:47', 'admin', '2019-05-03 12:10:47', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843926826487808', '573841891515305984', 6, '现代计算机之所以能够自动、连续地进行数据处理，主要是因为_', '0', '', '采用了开关电路', '采用了半导体器件', '采用了二进制', '具有存储程序的功能', '', '', 'D', 4, '', 2, 'admin', '2019-05-03 12:10:53', 'admin', '2019-05-03 12:10:53', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843944526450688', '573841891515305984', 7, '电子计算机按规模和处理能力划分，可以分为_', '0', '', '数字电子计算机和模拟电子计算机', '通用计算机和专用计算机', '巨型计算机、中小型计算机和微型计算机', '科学与过程计算计算机、工业控制计算机和数据计算机', '', '', 'C', 4, '', 2, 'admin', '2019-05-03 12:10:57', 'admin', '2019-05-03 12:10:57', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843961102340096', '573841891515305984', 8, '将计算机分为通用计算机、专用计算机两类的分类标准是_', '0', '', '计算机处理数据的方式', '计算机使用范围', '机器的规模', '计算机的功能和处理能力', '', '', 'B', 4, '', 2, 'admin', '2019-05-03 12:11:01', 'admin', '2019-05-03 12:11:01', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843978617753600', '573841891515305984', 9, '既可以接收、处理和输出模拟量，也可以接收、处理和输出数字量的计算机是_', '0', '', '电子数字计算机', '电子模拟计算机', '数模混合计算机', '专用计算机', '', '', 'C', 4, '', 2, 'admin', '2019-05-03 12:11:06', 'admin', '2019-05-03 12:11:06', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('573843994983927808', '573841891515305984', 10, '至今日，计算机仍采用存储程序原理，原理的提出者是_', '0', '', '莫尔', '比尔·盖次', '冯·诺依曼', '科得', '', '', 'C', 4, '', 2, 'admin', '2019-05-03 12:11:09', 'admin', '2019-05-03 12:11:09', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('576870238407233536', '576869700143812608', 1, '<p>测试题目1,<strong>现在</strong>是多少点_，如图所示：<img src=\"http://shp.qlogo.cn/pghead/PiajxSqBRaEILS6yspfhu0gxtcJAuphAfQI79V6Mooz0/140\" alt=\"32323\" width=\"140\" height=\"140\" /></p>', '0', '', '<p>测试测试测试测试测试<strong>测试测试测试测试测试</strong></p>', '<p style=\"text-align: center;\">测试测试</p>', '<p>测试测试<span style=\"color: #ff6600;\">00:07:59</span></p>', '<blockquote>\n<p>测试测试<a href=\"http://www.baidu.com\" target=\"_blank\" rel=\"noopener\">测试测试 问<img src=\"http://shp.qlogo.cn/pghead/PiajxSqBRaEILS6yspfhu0gxtcJAuphAfQI79V6Mooz0/140\" alt=\"测试\" width=\"140\" height=\"140\" /></a></p>\n</blockquote>', '', '', 'A', 5, '<ol>\n<li>测试测试</li>\n<li>fsdfa</li>\n<li>f2w232323</li>\n</ol>', 2, 'admin', '2019-05-11 20:36:22', 'admin', '2019-05-12 20:55:10', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('576870647137964032', '576869700143812608', 2, '<p>等下是多少点?X<sup>2</sup>等于几</p>', '0', '', '<p>本项目国际<span style=\"color: #008000;\">化基于 vue-i18n</span></p>', '232323', '323', '323', '', '', 'C', 4, '3232323', 2, 'admin', '2019-05-11 20:38:00', 'admin', '2019-05-11 20:38:00', '0', 'EXAM', NULL);
INSERT INTO `subject` VALUES ('577632765948858368', '576869700143812608', 3, '测试简答题', '3', '请问唐宋八大家是谁？', '', '', '', '', '', '', '测试测色', 4, '测试测色', 2, 'admin', '2019-05-13 23:06:23', 'admin', '2019-05-13 23:06:23', '0', 'EXAM', NULL);

-- ----------------------------
-- Table structure for subject_bank
-- ----------------------------
DROP TABLE IF EXISTS `subject_bank`;
CREATE TABLE `subject_bank`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `category_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `serial_number` int(62) NULL DEFAULT NULL COMMENT '题目序号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目名称',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目类型',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目内容',
  `option_a` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项a',
  `option_b` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项b',
  `option_c` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项c',
  `option_d` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项d',
  `option_e` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项e',
  `option_f` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项f',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `score` int(64) NULL DEFAULT NULL COMMENT '分值',
  `analysis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解析',
  `level` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '难度等级',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题库表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of subject_bank
-- ----------------------------
INSERT INTO `subject_bank` VALUES ('571463421858549760', '571450070935932928', 1, '若下列城市都安装了“风光互补路灯”，北半球夏至日这一天太阳能板左右摆动幅度最大的地点是', '0', '', '哈尔滨', '北京', '广州', '广州', '', '', 'A', 4, '', '2', 'admin', '2019-04-26 22:31:37', 'admin', '2019-05-22 22:17:59', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('571467576304078848', '571450070935932928', 2, '干热岩主要属于', '0', '', '岩浆', '喷出岩', '沉积岩', '侵入岩', '', '', 'B', 4, '', '2', 'admin', '2019-04-26 22:48:07', 'admin', '2019-04-26 22:48:07', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('571467771968360448', '571450070935932928', 3, '我国干热岩最主要分布在', '0', '', '东南沿海', '华北地区', '西北内陆', '青藏高原', '', '', 'D', 4, '', '2', 'admin', '2019-04-26 22:48:54', 'admin', '2019-04-26 22:52:01', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('571467945222475776', '571450070935932928', 4, '与传统地热资源相比，干热岩', '0', '', '开发难度小、成本低', '埋藏较浅、分布广', '无季节变化、污染少', '产业链短、效率低', '', '', 'C', 4, '', '2', 'admin', '2019-04-26 22:49:35', 'admin', '2019-04-26 22:52:15', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('571468124784824320', '571450070935932928', 5, '草场恢复促使光伏发电量大增的最主要原因是', '0', '', '蒸发增强，湿度增加', '气温升高，风力减小', '沙尘减少，晴天增多', '降水增加，气温降低', '', '', 'C', 4, '', '2', 'admin', '2019-04-26 22:50:18', 'admin', '2019-04-26 22:52:37', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573839142304223232', '573838899969921024', 1, '自计算机问世至今已经经历了四个时代，划分时代的主要依据是计算机的_', '0', '', '规模', '功能', '性能', '构成元件', '', '', 'D', 4, '', '2', 'admin', '2019-05-03 11:51:52', 'admin', '2019-05-03 12:01:13', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573839858523574272', '573838899969921024', 2, '第一台计算机是在1946年在美国诞生，该机的英文缩写是_', '0', '', 'ENIAC', 'EDVAC', 'EDVAE', 'MARK', '', '', 'A', 4, '', '2', 'admin', '2019-05-03 11:54:43', 'admin', '2019-05-03 11:54:43', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573840018745987072', '573838899969921024', 3, '当前的计算机一般被认为是第四代计算机，它所采用的逻辑元件是_', '0', '', '集成电路', '晶体管', '大规模集成电路', '电子管', '', '', 'C', 4, '', '2', 'admin', '2019-05-03 11:55:21', 'admin', '2019-05-03 11:55:21', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573840173176066048', '573838899969921024', 4, '当前计算机的应用最广泛的领域是_', '0', '', '辅助设计', '科学计算', '数据处理', '过程控制', '', '', 'C', 4, '', '2', 'admin', '2019-05-03 11:55:58', 'admin', '2019-05-03 11:55:58', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573840411693551616', '573838899969921024', 5, '早期的计算机体积大、耗电多、速度慢，其主要原因是制约于_', '0', '', '元材料', '工艺水平', '设计水平', '元器件', '', '', 'D', 4, '', '2', 'admin', '2019-05-03 11:56:55', 'admin', '2019-05-03 11:56:55', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573840623543652352', '573838899969921024', 6, '现代计算机之所以能够自动、连续地进行数据处理，主要是因为_', '0', '', '采用了开关电路', '采用了半导体器件', '采用了二进制', '具有存储程序的功能', '', '', 'D', 4, '', '2', 'admin', '2019-05-03 11:57:46', 'admin', '2019-05-03 11:57:46', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573840833552453632', '573838899969921024', 7, '电子计算机按规模和处理能力划分，可以分为_', '0', '', '数字电子计算机和模拟电子计算机', '通用计算机和专用计算机', '巨型计算机、中小型计算机和微型计算机', '科学与过程计算计算机、工业控制计算机和数据计算机', '', '', 'C', 4, '', '2', 'admin', '2019-05-03 11:58:36', 'admin', '2019-05-03 11:58:36', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573841001802764288', '573838899969921024', 8, '将计算机分为通用计算机、专用计算机两类的分类标准是_', '0', '', '计算机处理数据的方式', '计算机使用范围', '机器的规模', '计算机的功能和处理能力', '', '', 'B', 4, '', '2', 'admin', '2019-05-03 11:59:16', 'admin', '2019-05-03 11:59:16', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573841215032791040', '573838899969921024', 9, '既可以接收、处理和输出模拟量，也可以接收、处理和输出数字量的计算机是_', '0', '', '电子数字计算机', '电子模拟计算机', '数模混合计算机', '专用计算机', '', '', 'C', 4, '', '2', 'admin', '2019-05-03 12:00:07', 'admin', '2019-05-03 12:00:07', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('573841445803397120', '573838899969921024', 10, '至今日，计算机仍采用存储程序原理，原理的提出者是_', '0', '', '莫尔', '比尔·盖次', '冯·诺依曼', '科得', '', '', 'C', 4, '', '2', 'admin', '2019-05-03 12:01:02', 'admin', '2019-05-03 12:01:02', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('577624003204747264', '571450172626833408', 1, '<p>测试1</p>', '0', '', '<p>2323</p>', '<p>32323</p>', '<p>4444</p>', '<p>4444</p>', '', '', 'B', 4, '<p>444434入得厨房啊打发</p>', '2', 'admin', '2019-05-13 22:31:34', 'admin', '2019-05-13 22:31:34', '0', 'EXAM', NULL);
INSERT INTO `subject_bank` VALUES ('577624117902184448', '571450172626833408', 2, '<p>2222222222222</p>', '0', '', '<p>32332顶顶顶顶</p>', '<p>未访问的发的</p>', '<p>232323</p>', '<p>而父亲2父亲为对方</p>', '', '', 'D', 4, '<p>少时诵诗书所所所所</p>', '2', 'admin', '2019-05-13 22:32:01', 'admin', '2019-05-13 22:32:01', '0', 'EXAM', NULL);

-- ----------------------------
-- Table structure for subject_category
-- ----------------------------
DROP TABLE IF EXISTS `subject_category`;
CREATE TABLE `subject_category`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类描述',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父分类ID',
  `sort` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序号',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标记',
  `application_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of subject_category
-- ----------------------------
INSERT INTO `subject_category` VALUES ('571449735345475584', '计算机', '计算机', '-1', '30', 'admin', '2019-04-26 21:37:13', 'admin', '2019-04-26 21:37:13', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571449792375427072', '英语', NULL, '-1', '31', 'admin', '2019-04-26 21:37:27', 'admin', '2019-04-26 21:37:27', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571449862449664000', '数据库基础', '数据库基础', '571449735345475584', '1', 'admin', '2019-04-26 21:37:44', 'admin', '2019-04-26 21:37:44', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571449921748733952', 'Java程序设计', 'Java程序设计', '571449735345475584', '2', 'admin', '2019-04-26 21:37:58', 'admin', '2019-04-26 21:38:03', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571449994326970368', '数据结构', '数据结构', '571449735345475584', '3', 'admin', '2019-04-26 21:38:15', 'admin', '2019-04-26 21:38:15', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571450070935932928', '地理', '地理', '-1', '33', 'admin', '2019-04-26 21:38:33', 'admin', '2019-04-26 22:24:16', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571450122261630976', '大学四级', NULL, '571449792375427072', '1', 'admin', '2019-04-26 21:38:46', 'admin', '2019-04-26 21:38:46', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('571450172626833408', '大学六级', '大学六级', '571449792375427072', '2', 'admin', '2019-04-26 21:38:58', 'admin', '2019-04-26 21:38:58', '0', 'EXAM', NULL);
INSERT INTO `subject_category` VALUES ('573838899969921024', '计算机基础', NULL, '571449735345475584', '4', 'admin', '2019-05-03 11:50:55', 'admin', '2019-05-03 11:50:55', '0', 'EXAM', NULL);

SET FOREIGN_KEY_CHECKS = 1;
