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

 Date: 18/08/2019 20:10:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for exam_answer
-- ----------------------------
DROP TABLE IF EXISTS `exam_answer`;
CREATE TABLE `exam_answer`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `exam_record_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试记录id',
  `subject_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目ID',
  `type` int(11) NULL DEFAULT NULL,
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '答案',
  `answer_type` int(11) NULL DEFAULT NULL COMMENT '答题类型，0：正确，1：错误',
  `score` int(11) NULL DEFAULT NULL COMMENT '实际得分',
  `mark_status` int(255) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '答题表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for exam_course
-- ----------------------------
DROP TABLE IF EXISTS `exam_course`;
CREATE TABLE `exam_course`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `course_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `college` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '专业',
  `teacher` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '老师',
  `course_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程描述',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '课程表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_course
-- ----------------------------
INSERT INTO `exam_course` VALUES ('590968789617741824', '计算机基础', '信息学院', '软件工程', '', '计算机基础', 'admin', '2019-06-19 18:18:59', 'admin', '2019-06-23 13:55:51', 0, 'EXAM', 'gitee');
INSERT INTO `exam_course` VALUES ('590968881187786752', '大学语文', '信息学院', '软件工程', '陈老师', '大学语文', 'admin', '2019-06-19 18:19:20', 'admin', '2019-07-19 22:09:42', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for exam_examination
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination`;
CREATE TABLE `exam_examination`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `examination_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试类型',
  `attention` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试注意事项',
  `start_time` timestamp(0) NULL DEFAULT NULL COMMENT '考试开始时间',
  `end_time` timestamp(0) NULL DEFAULT NULL COMMENT '考试结束时间',
  `total_score` int(11) NULL DEFAULT NULL COMMENT '总分',
  `status` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试状态',
  `avatar_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片ID',
  `course_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考试信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_examination
-- ----------------------------
INSERT INTO `exam_examination` VALUES ('590969316204220416', '全国计算机统考练习题10道', '2', '练习', '2019-06-19 18:20:00', '2019-06-19 20:20:00', 50, '0', NULL, '590968789617741824', '全国计算机统考练习题10道', 'admin', '2019-06-19 18:21:04', 'admin', '2019-06-19 18:45:33', 0, 'EXAM', 'gitee');
INSERT INTO `exam_examination` VALUES ('590969514372501504', '四川省2016年普通高考文科综合能力测试-语文部分', '0', '注意事项:\n1.本试卷分第工卷(选择题)和第II卷(非选择题)两部分。答卷前，考生务必将白己的姓名、准考证号填写在答题卡上。\n2.回答第Ⅰ卷时，选出每小题答案后，用铅笔把答题卡上对应题目的答案标号涂黑。如需改动，用橡皮擦干净后，再选涂其它答案标号。写在本试卷上无效。\n3.回答第Ⅱ卷时，将答案写在答题卡上。写在本试卷上无效。\n4.考试结束后，将本试卷和答题卡一并交回。', '2019-07-02 19:21:00', '2019-07-04 16:02:00', 60, '0', NULL, '590968881187786752', '四川省2016年普通高考-文科综合能力测试', 'admin', '2019-06-19 18:21:51', 'admin', '2019-07-04 14:20:36', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for exam_examination_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination_record`;
CREATE TABLE `exam_examination_record`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `examination_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试ID',
  `start_time` timestamp(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp(0) NULL DEFAULT NULL COMMENT '结束时间',
  `score` int(11) NULL DEFAULT NULL COMMENT '成绩',
  `correct_number` int(11) NULL DEFAULT NULL COMMENT '正确题目数量',
  `incorrect_number` int(11) NULL DEFAULT NULL COMMENT '错误题目数量',
  `submit_status` int(11) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考试记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for exam_examination_subject
-- ----------------------------
DROP TABLE IF EXISTS `exam_examination_subject`;
CREATE TABLE `exam_examination_subject`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `examination_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试ID',
  `subject_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目ID',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 0-选择题，1-简答题',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `serial_number` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考试题目表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_examination_subject
-- ----------------------------
INSERT INTO `exam_examination_subject` VALUES ('590970743886581760', '590969316204220416', '590970743878193152', 0, 'EXAM', 'gitee', 1);
INSERT INTO `exam_examination_subject` VALUES ('590970887440830464', '590969316204220416', '590970887432441856', 0, 'EXAM', 'gitee', 2);
INSERT INTO `exam_examination_subject` VALUES ('590971026888855552', '590969316204220416', '590971026880466944', 0, 'EXAM', 'gitee', 3);
INSERT INTO `exam_examination_subject` VALUES ('590971151145111552', '590969316204220416', '590971151128334336', 0, 'EXAM', 'gitee', 4);
INSERT INTO `exam_examination_subject` VALUES ('590971273547485184', '590969316204220416', '590971273539096576', 0, 'EXAM', 'gitee', 5);
INSERT INTO `exam_examination_subject` VALUES ('590971402648162304', '590969316204220416', '590971402639773696', 0, 'EXAM', 'gitee', 6);
INSERT INTO `exam_examination_subject` VALUES ('590971508113936384', '590969316204220416', '590971508109742080', 0, 'EXAM', 'gitee', 7);
INSERT INTO `exam_examination_subject` VALUES ('590971613944614913', '590969316204220416', '590971613944614912', 0, 'EXAM', 'gitee', 8);
INSERT INTO `exam_examination_subject` VALUES ('590971750154637312', '590969316204220416', '590971750146248704', 0, 'EXAM', 'gitee', 9);
INSERT INTO `exam_examination_subject` VALUES ('590971860049596416', '590969316204220416', '590971860041207808', 0, 'EXAM', 'gitee', 10);
INSERT INTO `exam_examination_subject` VALUES ('590972141151850496', '590969514372501504', '590972141118296064', 0, 'EXAM', 'gitee', 1);
INSERT INTO `exam_examination_subject` VALUES ('590972250216337408', '590969514372501504', '590972250212143104', 0, 'EXAM', 'gitee', 2);
INSERT INTO `exam_examination_subject` VALUES ('590972363114418176', '590969514372501504', '590972363110223872', 0, 'EXAM', 'gitee', 3);
INSERT INTO `exam_examination_subject` VALUES ('590972473890181120', '590969514372501504', '590972473877598208', 0, 'EXAM', 'gitee', 4);
INSERT INTO `exam_examination_subject` VALUES ('590972590185648128', '590969514372501504', '590972590177259520', 0, 'EXAM', 'gitee', 5);
INSERT INTO `exam_examination_subject` VALUES ('590972723384160257', '590969514372501504', '590972723384160256', 0, 'EXAM', 'gitee', 6);
INSERT INTO `exam_examination_subject` VALUES ('590972834742931456', '590969514372501504', '590972834734542848', 0, 'EXAM', 'gitee', 7);
INSERT INTO `exam_examination_subject` VALUES ('590972929701974016', '590969514372501504', '590972929693585408', 0, 'EXAM', 'gitee', 8);
INSERT INTO `exam_examination_subject` VALUES ('590973027999682560', '590969514372501504', '590973027995488256', 0, 'EXAM', 'gitee', 9);
INSERT INTO `exam_examination_subject` VALUES ('590973115165708288', '590969514372501504', '590973115157319680', 0, 'EXAM', 'gitee', 10);
INSERT INTO `exam_examination_subject` VALUES ('590973253548380160', '590969514372501504', '590973253544185856', 0, 'EXAM', 'gitee', 11);
INSERT INTO `exam_examination_subject` VALUES ('590973360670904320', '590969514372501504', '590973360662515712', 0, 'EXAM', 'gitee', 12);

-- ----------------------------
-- Table structure for exam_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `exam_knowledge`;
CREATE TABLE `exam_knowledge`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `knowledge_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识名称',
  `knowledge_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识描述',
  `attachment_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件ID',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '知识表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_knowledge
-- ----------------------------
INSERT INTO `exam_knowledge` VALUES ('590978901526843392', '四川省2016年普通高考适应性测试', '四川省2016年普通高考适应性测试', '590978944174526464', 0, 'admin', '2019-06-19 18:59:09', 'admin', '2019-07-04 13:51:27', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for exam_pictures
-- ----------------------------
DROP TABLE IF EXISTS `exam_pictures`;
CREATE TABLE `exam_pictures`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `picture_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '知识名称',
  `attachment_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件ID',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图片表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for exam_subject_category
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_category`;
CREATE TABLE `exam_subject_category`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类描述',
  `parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父分类ID',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型: 0-私共,1-公有',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目分类表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_subject_category
-- ----------------------------
INSERT INTO `exam_subject_category` VALUES ('590975991732637696', '计算机', NULL, '-1', 30, NULL, 'admin', '2019-06-19 18:47:36', 'admin', '2019-06-19 18:47:36', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_category` VALUES ('590976037467328512', '数据库基础', NULL, '590975991732637696', 30, NULL, 'admin', '2019-06-19 18:47:47', 'admin', '2019-07-04 13:51:03', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_category` VALUES ('590976094983819264', 'Java程序设计', NULL, '590975991732637696', 31, NULL, 'admin', '2019-06-19 18:48:00', 'admin', '2019-06-19 18:48:00', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_category` VALUES ('590976142211682304', '数据结构', NULL, '590975991732637696', 33, NULL, 'admin', '2019-06-19 18:48:12', 'admin', '2019-06-19 18:48:12', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_category` VALUES ('590976191398285312', '地理', NULL, '-1', 31, NULL, 'admin', '2019-06-19 18:48:23', 'admin', '2019-06-19 18:48:23', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_category` VALUES ('590976335996915712', '计算机基础', NULL, '590975991732637696', 34, NULL, 'admin', '2019-06-19 18:48:58', 'admin', '2019-06-19 18:48:58', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_category` VALUES ('602231546270846976', '多选测试', NULL, '-1', 30, NULL, 'admin', '2019-07-20 20:13:10', 'admin', '2019-07-20 20:13:10', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for exam_subject_choices
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_choices`;
CREATE TABLE `exam_subject_choices`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `category_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `serial_number` int(11) NULL DEFAULT NULL COMMENT '题目序号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目名称',
  `choices_type` int(11) NULL DEFAULT NULL COMMENT '题目类型',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `score` int(11) NULL DEFAULT NULL COMMENT '题目分值',
  `analysis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解析',
  `level` int(11) NULL DEFAULT NULL COMMENT '难度等级',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '选择题表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_subject_choices
-- ----------------------------
INSERT INTO `exam_subject_choices` VALUES ('590970743878193152', '0', 1, '自计算机问世至今已经经历了四个时代，划分时代的主要依据是计算机的_', 0, 'D', 5, '<p>无</p>', 2, 'admin', '2019-06-19 18:26:45', 'admin', '2019-06-19 18:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590970887432441856', '0', 2, '第一台计算机是在1946年在美国诞生，该机的英文缩写是_', 0, 'A', 5, '<p>无</p>', 2, 'admin', '2019-06-19 18:27:19', 'admin', '2019-06-19 18:27:19', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971026880466944', '0', 3, '当前的计算机一般被认为是第四代计算机，它所采用的逻辑元件是_', 0, 'C', 5, '无', 2, 'admin', '2019-06-19 18:27:52', 'admin', '2019-06-19 18:27:52', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971151128334336', '0', 4, '当前计算机的应用最广泛的领域是_', 0, 'C', 5, '无', 2, 'admin', '2019-06-19 18:28:22', 'admin', '2019-06-19 18:28:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971273539096576', '0', 5, '早期的计算机体积大、耗电多、速度慢，其主要原因是制约于_', 0, 'D', 5, '无', 2, 'admin', '2019-06-19 18:28:51', 'admin', '2019-06-19 18:28:51', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971402639773696', '0', 6, '现代计算机之所以能够自动、连续地进行数据处理，主要是因为_', 0, 'D', 5, '无', 2, 'admin', '2019-06-19 18:29:22', 'admin', '2019-06-19 18:29:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971508109742080', '0', 7, '电子计算机按规模和处理能力划分，可以分为_', 0, 'C', 5, '无', 2, 'admin', '2019-06-19 18:29:47', 'admin', '2019-06-19 18:29:47', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971613944614912', '0', 8, '将计算机分为通用计算机、专用计算机两类的分类标准是_', 0, 'B', 5, '无', 2, 'admin', '2019-06-19 18:30:12', 'admin', '2019-06-19 18:30:12', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971750146248704', '0', 9, '既可以接收、处理和输出模拟量，也可以接收、处理和输出数字量的计算机是_', 0, 'C', 5, '无', 2, 'admin', '2019-06-19 18:30:44', 'admin', '2019-06-19 18:30:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590971860041207808', '0', 10, '至今日，计算机仍采用存储程序原理，原理的提出者是_', 0, 'C', 5, '无', 2, 'admin', '2019-06-19 18:31:11', 'admin', '2019-06-19 18:31:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972141118296064', '0', 1, '《小雅·鹿鸣》本是西周贵族宣扬宴飨之仪的乐歌，后扩散到民间，在乡人宴会上也可传唱。这表明西周时期', 0, 'B', 5, '无', 2, 'admin', '2019-06-19 18:32:18', 'admin', '2019-06-19 18:32:18', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972250212143104', '0', 2, '公元前 212 年，秦始皇坑杀“术士”，长子扶苏劝谏说：“远方黔首未集，诸生皆诵法孔子，今上皆重法绳之，臣恐天下不安，唯上察之”。这反映当时', 0, 'D', 5, '', 2, 'admin', '2019-06-19 18:32:44', 'admin', '2019-06-19 18:32:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972363110223872', '0', 3, '唐前期的政治人物多为北方人，北宋时政治人物多出生于江西、福建、苏南等地。这一变化主要反映了', 0, 'C', 5, '', 2, 'admin', '2019-06-19 18:33:11', 'admin', '2019-06-19 18:33:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972473877598208', '0', 4, '李鸿章凭淮军实力日渐强盛。一次，他在游孔林时说道：“孔子不会打洋枪，今不足贵也。”李鸿章这样评价孔子，其背景最可能是', 0, 'B', 5, '', 2, 'admin', '2019-06-19 18:33:37', 'admin', '2019-06-19 18:33:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972590177259520', '0', 5, '《荆楚岁时记》云：“社日，四邻并结宗会社，宰牲牢，为屋于树下，先祭神，然后食其胙。”据此可知，社日的民俗功能主要是', 0, 'A', 5, '', 2, 'admin', '2019-06-19 18:34:05', 'admin', '2019-06-19 18:34:05', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972723384160256', '0', 6, '李鸿章凭淮军实力日渐强盛。一次，他在游孔林时说道：“孔子不会打洋枪，今不足贵也。”李鸿章这样评价孔子，其背景最可能是', 0, 'B', 5, '', 2, 'admin', '2019-06-19 18:34:36', 'admin', '2019-06-19 18:34:36', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972834734542848', '0', 7, '1930 年，中共中央在回应共产国际指示时说：党的任务决不是准备夺取部分的政权，如果认为现在还是准备夺取部分的政权,无疑是对革命形势估量不足的右倾观念。这一回应表明', 0, 'D', 5, '', 2, 'admin', '2019-06-19 18:35:03', 'admin', '2019-06-19 18:35:03', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590972929693585408', '0', 8, '1938 年初，国民政府颁布多部法规，要求将每一工厂、商号、银行、钱庄都纳入到同业工会内，又将每一同业工会纳入当地商会内。这些法规', 0, 'D', 5, '', 2, 'admin', '2019-06-19 18:35:26', 'admin', '2019-06-19 18:35:26', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590973027995488256', '0', 9, '古代雅典法律规定：每个公民从出生起，城邦就是他的最高监护人，要按城邦的需要来抚养和教育。这反映出雅典', 0, 'A', 5, '', 2, 'admin', '2019-06-19 18:35:49', 'admin', '2019-06-19 18:35:49', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590973115157319680', '0', 10, '19 世纪末 20 世纪初，一向傲慢的英国人惊奇地发现：“身上的衣服是德国缝制的，少女们周末穿的漂亮披风与上衣来自德国。更让人吃惊的是生活中有许多东西都产自德国，……连周末歌剧院里上演的歌剧也是德国人创作的，演员无一例外是德国人。”可见，当时傲慢的英国人', 0, 'D', 5, '', 2, 'admin', '2019-06-19 18:36:10', 'admin', '2019-06-19 18:36:10', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590973253544185856', '0', 11, '1920–1921 年，苏俄许多工人流往农村，还有一些则自谋生路成了小手工业者。据此推知当时苏俄', 0, 'A', 5, '', 2, 'admin', '2019-06-19 18:36:43', 'admin', '2019-06-19 18:36:43', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590973360662515712', '0', 12, '1964 年 6 月，美国《时代》杂志发表社论指出：“从北约到联合国，从拉丁美洲到红色中国，几乎在世界政治中的每一个问题或地区上，法国都采取和美国政策不一致的态度。”这一社论', 0, 'B', 5, '', 2, 'admin', '2019-06-19 18:37:08', 'admin', '2019-06-19 18:37:08', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('590976455786237952', '590976335996915712', 1, '<p>自计算机问世至今已经经历了四个时代，划分时代的主要依据是计算机的_</p>', 0, 'D', 5, '', 2, 'admin', '2019-06-19 18:49:26', 'admin', '2019-07-20 20:29:15', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_choices` VALUES ('602231589715447808', '602231546270846976', 1, '<p>32</p>', 3, 'A', 5, '233', 2, 'admin', '2019-07-20 20:13:20', 'admin', '2019-07-20 20:30:22', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for exam_subject_judgement
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_judgement`;
CREATE TABLE `exam_subject_judgement`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `category_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `serial_number` int(11) NULL DEFAULT NULL COMMENT '序号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目名称',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `score` int(11) NULL DEFAULT NULL COMMENT '分值',
  `analysis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解析',
  `level` int(11) NULL DEFAULT NULL COMMENT '难度等级',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '简答题表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for exam_subject_option
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_option`;
CREATE TABLE `exam_subject_option`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `subject_choices_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选择题ID',
  `option_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项名称',
  `option_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选项内容',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '选择题选项表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of exam_subject_option
-- ----------------------------
INSERT INTO `exam_subject_option` VALUES ('590970743911747584', '590970743878193152', 'A', '<p>规模</p>', 'admin', '2019-06-19 18:26:45', 'admin', '2019-06-19 18:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970743911747585', '590970743878193152', 'B', '<p>功能</p>', 'admin', '2019-06-19 18:26:45', 'admin', '2019-06-19 18:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970743911747586', '590970743878193152', 'C', '<p>性能</p>', 'admin', '2019-06-19 18:26:45', 'admin', '2019-06-19 18:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970743911747587', '590970743878193152', 'D', '构成元件', 'admin', '2019-06-19 18:26:45', 'admin', '2019-06-19 18:31:29', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970887457607680', '590970887432441856', 'A', '<p>ENIAC</p>', 'admin', '2019-06-19 18:27:19', 'admin', '2019-06-19 18:27:19', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970887457607681', '590970887432441856', 'B', '<p>EDVAC</p>', 'admin', '2019-06-19 18:27:19', 'admin', '2019-06-19 18:27:19', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970887457607682', '590970887432441856', 'C', '<p>EDVAE</p>', 'admin', '2019-06-19 18:27:19', 'admin', '2019-06-19 18:27:19', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590970887457607683', '590970887432441856', 'D', 'MARK', 'admin', '2019-06-19 18:27:19', 'admin', '2019-06-19 18:27:19', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971026893049856', '590971026880466944', 'A', '<p>集成电路</p>', 'admin', '2019-06-19 18:27:52', 'admin', '2019-06-19 18:27:52', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971026901438464', '590971026880466944', 'B', '<p>晶体管</p>', 'admin', '2019-06-19 18:27:52', 'admin', '2019-06-19 18:27:52', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971026901438465', '590971026880466944', 'C', '<p>大规模集成电路</p>', 'admin', '2019-06-19 18:27:52', 'admin', '2019-06-19 18:27:52', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971026901438466', '590971026880466944', 'D', '电子管', 'admin', '2019-06-19 18:27:52', 'admin', '2019-06-19 18:27:52', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971151170277376', '590971151128334336', 'A', '<p>辅助设计</p>', 'admin', '2019-06-19 18:28:22', 'admin', '2019-06-19 18:28:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971151170277377', '590971151128334336', 'B', '<p>科学计算</p>', 'admin', '2019-06-19 18:28:22', 'admin', '2019-06-19 18:28:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971151170277378', '590971151128334336', 'C', '<p>数据处理</p>', 'admin', '2019-06-19 18:28:22', 'admin', '2019-06-19 18:28:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971151170277379', '590971151128334336', 'D', '<p>过程控制</p>', 'admin', '2019-06-19 18:28:22', 'admin', '2019-06-19 18:28:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971273560068096', '590971273539096576', 'A', '<p>元材料</p>', 'admin', '2019-06-19 18:28:51', 'admin', '2019-06-19 18:28:51', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971273560068097', '590971273539096576', 'B', '<p>工艺水平</p>', 'admin', '2019-06-19 18:28:51', 'admin', '2019-06-19 18:28:51', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971273560068098', '590971273539096576', 'C', '<p>设计水平</p>', 'admin', '2019-06-19 18:28:51', 'admin', '2019-06-19 18:28:51', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971273560068099', '590971273539096576', 'D', '元器件', 'admin', '2019-06-19 18:28:51', 'admin', '2019-06-19 18:28:51', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971402681716736', '590971402639773696', 'A', '<p>采用了开关电路</p>', 'admin', '2019-06-19 18:29:22', 'admin', '2019-06-19 18:29:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971402681716737', '590971402639773696', 'B', '<p>采用了半导体器件</p>', 'admin', '2019-06-19 18:29:22', 'admin', '2019-06-19 18:29:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971402681716738', '590971402639773696', 'C', '<p>采用了二进制</p>', 'admin', '2019-06-19 18:29:22', 'admin', '2019-06-19 18:29:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971402681716739', '590971402639773696', 'D', '<p>具有存储程序的功能</p>', 'admin', '2019-06-19 18:29:22', 'admin', '2019-06-19 18:29:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971508126519296', '590971508109742080', 'A', '<p>数字电子计算机和模拟电子计算机</p>', 'admin', '2019-06-19 18:29:47', 'admin', '2019-06-19 18:29:47', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971508126519297', '590971508109742080', 'B', '<p>通用计算机和专用计算机</p>', 'admin', '2019-06-19 18:29:47', 'admin', '2019-06-19 18:29:47', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971508126519298', '590971508109742080', 'C', '<p>巨型计算机、中小型计算机和微型计算机</p>', 'admin', '2019-06-19 18:29:47', 'admin', '2019-06-19 18:29:47', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971508126519299', '590971508109742080', 'D', '科学与过程计算计算机、工业控制计算机和数据计算机', 'admin', '2019-06-19 18:29:47', 'admin', '2019-06-19 18:29:47', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971613961392128', '590971613944614912', 'A', '<p>计算机处理数据的方式</p>', 'admin', '2019-06-19 18:30:12', 'admin', '2019-06-19 18:30:12', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971613961392129', '590971613944614912', 'B', '<p>计算机使用范围</p>', 'admin', '2019-06-19 18:30:12', 'admin', '2019-06-19 18:30:12', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971613961392130', '590971613944614912', 'C', '<p>机器的规模</p>', 'admin', '2019-06-19 18:30:12', 'admin', '2019-06-19 18:30:12', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971613961392131', '590971613944614912', 'D', '计算机的功能和处理能力', 'admin', '2019-06-19 18:30:12', 'admin', '2019-06-19 18:30:12', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971750163025920', '590971750146248704', 'A', '<p>电子数字计算机</p>', 'admin', '2019-06-19 18:30:44', 'admin', '2019-06-19 18:30:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971750167220224', '590971750146248704', 'B', '<p>电子模拟计算机</p>', 'admin', '2019-06-19 18:30:44', 'admin', '2019-06-19 18:30:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971750167220225', '590971750146248704', 'C', '<p>数模混合计算机</p>', 'admin', '2019-06-19 18:30:44', 'admin', '2019-06-19 18:30:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971750167220226', '590971750146248704', 'D', '<p>专用计算机</p>', 'admin', '2019-06-19 18:30:44', 'admin', '2019-06-19 18:30:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971860062179328', '590971860041207808', 'A', '<p>莫尔</p>', 'admin', '2019-06-19 18:31:11', 'admin', '2019-06-19 18:31:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971860062179329', '590971860041207808', 'B', '<p>比尔&middot;盖次</p>', 'admin', '2019-06-19 18:31:11', 'admin', '2019-06-19 18:31:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971860062179330', '590971860041207808', 'C', '<p>冯&middot;诺依曼</p>', 'admin', '2019-06-19 18:31:11', 'admin', '2019-06-19 18:31:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590971860062179331', '590971860041207808', 'D', '科得', 'admin', '2019-06-19 18:31:11', 'admin', '2019-06-19 18:31:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972141156044800', '590972141118296064', 'A', '<p>周人生活较为富足</p>', 'admin', '2019-06-19 18:32:18', 'admin', '2019-06-19 18:32:18', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972141156044801', '590972141118296064', 'B', '<p>礼乐文明得到广泛认同</p>', 'admin', '2019-06-19 18:32:18', 'admin', '2019-06-19 18:32:18', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972141156044802', '590972141118296064', 'C', '<p>乡人社会地位提高</p>', 'admin', '2019-06-19 18:32:18', 'admin', '2019-06-19 18:32:18', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972141156044803', '590972141118296064', 'D', '<p>贵族奢靡之风波及民间</p>', 'admin', '2019-06-19 18:32:18', 'admin', '2019-06-19 18:32:18', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972250224726016', '590972250212143104', 'A', '<p>统一进程曲折</p>', 'admin', '2019-06-19 18:32:44', 'admin', '2019-06-19 18:32:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972250224726017', '590972250212143104', 'B', '<p>地方治理不畅</p>', 'admin', '2019-06-19 18:32:44', 'admin', '2019-06-19 18:32:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972250224726018', '590972250212143104', 'C', '<p>始皇灭儒崇法</p>', 'admin', '2019-06-19 18:32:44', 'admin', '2019-06-19 18:32:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972250224726019', '590972250212143104', 'D', '儒学影响较大', 'admin', '2019-06-19 18:32:44', 'admin', '2019-06-19 18:32:44', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972363127001088', '590972363110223872', 'A', '<p>官僚集团重视本地域人才</p>', 'admin', '2019-06-19 18:33:11', 'admin', '2019-06-19 18:33:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972363127001089', '590972363110223872', 'B', '<p>南北方士人志向差异</p>', 'admin', '2019-06-19 18:33:11', 'admin', '2019-06-19 18:33:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972363127001090', '590972363110223872', 'C', '<p>科举制改变人才地域分布</p>', 'admin', '2019-06-19 18:33:11', 'admin', '2019-06-19 18:33:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972363127001091', '590972363110223872', 'D', '<p>政治中心转移到南方</p>', 'admin', '2019-06-19 18:33:11', 'admin', '2019-06-19 18:33:11', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972473902764032', '590972473877598208', 'A', '<p>&ldquo;师夷长技&rdquo;思想萌发</p>', 'admin', '2019-06-19 18:33:37', 'admin', '2019-06-19 18:33:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972473902764033', '590972473877598208', 'B', '<p>&ldquo;中体西用&rdquo;思潮兴起</p>', 'admin', '2019-06-19 18:33:37', 'admin', '2019-06-19 18:33:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972473902764034', '590972473877598208', 'C', '<p>&ldquo;中体西用&rdquo;思潮兴起</p>', 'admin', '2019-06-19 18:33:37', 'admin', '2019-06-19 18:33:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972473902764035', '590972473877598208', 'D', '<p>&ldquo;尊孔复古&rdquo;思潮泛滥</p>', 'admin', '2019-06-19 18:33:37', 'admin', '2019-06-19 18:33:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972590189842432', '590972590177259520', 'A', '<p>联谊乡邻</p>', 'admin', '2019-06-19 18:34:05', 'admin', '2019-06-19 18:34:05', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972590189842433', '590972590177259520', 'B', '<p>颂扬盛世</p>', 'admin', '2019-06-19 18:34:05', 'admin', '2019-06-19 18:34:05', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972590189842434', '590972590177259520', 'C', '<p>缅怀先祖</p>', 'admin', '2019-06-19 18:34:05', 'admin', '2019-06-19 18:34:05', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972590189842435', '590972590177259520', 'D', '助危济困', 'admin', '2019-06-19 18:34:05', 'admin', '2019-06-19 18:34:05', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972723392548864', '590972723384160256', 'A', '<p>&ldquo;师夷长技&rdquo;思想萌发</p>', 'admin', '2019-06-19 18:34:37', 'admin', '2019-06-19 18:34:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972723392548865', '590972723384160256', 'B', '<p>&ldquo;中体西用&rdquo;思潮兴起</p>', 'admin', '2019-06-19 18:34:37', 'admin', '2019-06-19 18:34:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972723392548866', '590972723384160256', 'C', '<p>&ldquo;托古改制&rdquo;思想产生</p>', 'admin', '2019-06-19 18:34:37', 'admin', '2019-06-19 18:34:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972723392548867', '590972723384160256', 'D', '<p>&ldquo;尊孔复古&rdquo;思潮泛滥</p>', 'admin', '2019-06-19 18:34:37', 'admin', '2019-06-19 18:34:37', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972834759708672', '590972834734542848', 'A', '<p>中共夺取全国政权时机成熟</p>', 'admin', '2019-06-19 18:35:03', 'admin', '2019-06-19 18:35:03', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972834759708673', '590972834734542848', 'B', '<p>中共找到符合国情的道路</p>', 'admin', '2019-06-19 18:35:03', 'admin', '2019-06-19 18:35:03', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972834759708674', '590972834734542848', 'C', '<p>中共找到符合国情的道路</p>', 'admin', '2019-06-19 18:35:03', 'admin', '2019-06-19 18:35:03', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972834759708675', '590972834734542848', 'D', '<p>中共出现自主革命的倾向</p>', 'admin', '2019-06-19 18:35:03', 'admin', '2019-06-19 18:35:03', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972929714556928', '590972929693585408', 'A', '<p>抑制了官僚资本膨胀</p>', 'admin', '2019-06-19 18:35:26', 'admin', '2019-06-19 18:35:26', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972929714556929', '590972929693585408', 'B', '<p>挫败了日军经济掠夺</p>', 'admin', '2019-06-19 18:35:26', 'admin', '2019-06-19 18:35:26', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972929714556930', '590972929693585408', 'C', '<p>防止了国民经济崩溃</p>', 'admin', '2019-06-19 18:35:26', 'admin', '2019-06-19 18:35:26', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590972929714556931', '590972929693585408', 'D', '积聚了抗战经济力量', 'admin', '2019-06-19 18:35:26', 'admin', '2019-06-19 18:35:26', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973028020654080', '590973027995488256', 'A', '<p>公民培养受强制性约束</p>', 'admin', '2019-06-19 18:35:49', 'admin', '2019-06-19 18:35:49', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973028020654081', '590973027995488256', 'B', '<p>法律有违人文精神</p>', 'admin', '2019-06-19 18:35:49', 'admin', '2019-06-19 18:35:49', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973028020654082', '590973027995488256', 'C', '<p>父母失去教育子女资格</p>', 'admin', '2019-06-19 18:35:49', 'admin', '2019-06-19 18:35:49', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973028020654083', '590973027995488256', 'D', '公民教育制度完备', 'admin', '2019-06-19 18:35:49', 'admin', '2019-06-19 18:35:49', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973115178291200', '590973115157319680', 'A', '<p>推崇德国文化</p>', 'admin', '2019-06-19 18:36:10', 'admin', '2019-06-19 18:36:10', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973115178291201', '590973115157319680', 'B', '<p>注意到了德国的威胁</p>', 'admin', '2019-06-19 18:36:10', 'admin', '2019-06-19 18:36:10', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973115178291202', '590973115157319680', 'C', '<p>喜爱德国产品</p>', 'admin', '2019-06-19 18:36:10', 'admin', '2019-06-19 18:36:10', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973115178291203', '590973115157319680', 'D', '意识到了德国的崛起', 'admin', '2019-06-19 18:36:10', 'admin', '2019-06-19 18:36:10', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973253556768768', '590973253544185856', 'A', '<p>战时共产主义政策有所突破</p>', 'admin', '2019-06-19 18:36:43', 'admin', '2019-06-19 18:36:43', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973253556768769', '590973253544185856', 'B', '<p>新经济政策成效显著</p>', 'admin', '2019-06-19 18:36:43', 'admin', '2019-06-19 18:36:43', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973253556768770', '590973253544185856', 'C', '<p>国营农庄吸引了大量劳动力</p>', 'admin', '2019-06-19 18:36:43', 'admin', '2019-06-19 18:36:43', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973253556768771', '590973253544185856', 'D', '政府重视日用品生产', 'admin', '2019-06-19 18:36:43', 'admin', '2019-06-19 18:36:43', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973360691875840', '590973360662515712', 'A', '<p>揭示了法国倒向社会主义阵营</p>', 'admin', '2019-06-19 18:37:08', 'admin', '2019-06-19 18:37:08', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973360691875841', '590973360662515712', 'B', '<p>反映了法国推行独立外交</p>', 'admin', '2019-06-19 18:37:08', 'admin', '2019-06-19 18:37:08', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973360691875842', '590973360662515712', 'C', '<p>体现了两大阵营对抗趋于缓和</p>', 'admin', '2019-06-19 18:37:08', 'admin', '2019-06-19 18:37:08', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590973360691875843', '590973360662515712', 'D', '体现了两大阵营对抗趋于缓和', 'admin', '2019-06-19 18:37:08', 'admin', '2019-06-19 18:37:08', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976455823986688', '590976455786237952', 'A', '<p>规模</p>', 'admin', '2019-06-19 18:49:26', 'admin', '2019-07-20 20:29:15', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976455823986689', '590976455786237952', 'B', '<p>功能</p>', 'admin', '2019-06-19 18:49:26', 'admin', '2019-07-20 20:29:15', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976455823986690', '590976455786237952', 'C', '<p>性能</p>', 'admin', '2019-06-19 18:49:26', 'admin', '2019-07-20 20:29:15', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976455823986691', '590976455786237952', 'D', '构成元件', 'admin', '2019-06-19 18:49:26', 'admin', '2019-07-20 20:29:15', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976456683819008', '590976456675430400', 'A', '<p>规模</p>', 'admin', '2019-06-19 18:49:27', 'admin', '2019-06-19 18:49:27', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976456683819009', '590976456675430400', 'B', '<p>功能</p>', 'admin', '2019-06-19 18:49:27', 'admin', '2019-06-19 18:49:27', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976456683819010', '590976456675430400', 'C', '<p>性能</p>', 'admin', '2019-06-19 18:49:27', 'admin', '2019-06-19 18:49:27', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('590976456683819011', '590976456675430400', 'D', '构成元件', 'admin', '2019-06-19 18:49:27', 'admin', '2019-06-19 18:49:27', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('602231589723836416', '602231589715447808', 'A', '32', 'admin', '2019-07-20 20:13:20', 'admin', '2019-07-20 20:30:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('602231589723836417', '602231589715447808', 'B', '323', 'admin', '2019-07-20 20:13:20', 'admin', '2019-07-20 20:30:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('602231589723836418', '602231589715447808', 'C', '323', 'admin', '2019-07-20 20:13:20', 'admin', '2019-07-20 20:30:22', 0, 'EXAM', 'gitee');
INSERT INTO `exam_subject_option` VALUES ('602231589723836419', '602231589715447808', 'D', '323', 'admin', '2019-07-20 20:13:20', 'admin', '2019-07-20 20:30:22', 0, 'EXAM', 'gitee');

-- ----------------------------
-- Table structure for exam_subject_short_answer
-- ----------------------------
DROP TABLE IF EXISTS `exam_subject_short_answer`;
CREATE TABLE `exam_subject_short_answer`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `category_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `serial_number` int(11) NULL DEFAULT NULL COMMENT '序号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题目名称',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考答案',
  `score` int(11) NULL DEFAULT NULL COMMENT '分值',
  `analysis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解析',
  `level` int(11) NULL DEFAULT NULL COMMENT '难度等级',
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_date` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT '删除标记 0:正常;1:删除',
  `application_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统编号',
  `tenant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '简答题表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
