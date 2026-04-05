
-- ----------------------------
-- Table structure for exam_answer
-- ----------------------------
DROP TABLE IF EXISTS exam_answer;
CREATE TABLE exam_answer (
  id bigint NOT NULL AUTO_INCREMENT,
  exam_record_id bigint NOT NULL,
  subject_id bigint NOT NULL,
  type tinyint DEFAULT NULL,
  answer varchar(255) DEFAULT NULL,
  answer_type tinyint NOT NULL DEFAULT '1',
  score tinyint DEFAULT NULL,
  mark_status tinyint DEFAULT NULL,
  start_time timestamp DEFAULT NULL,
  end_time timestamp DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  mark_operator varchar(16) DEFAULT '',
  speech_play_cnt bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (exam_record_id,subject_id)
);

-- ----------------------------
-- Records of exam_answer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_course
-- ----------------------------
DROP TABLE IF EXISTS exam_course;
CREATE TABLE exam_course (
  id bigint NOT NULL AUTO_INCREMENT,
  course_name varchar(128) NOT NULL DEFAULT '',
  college varchar(128) DEFAULT NULL,
  major varchar(128) DEFAULT NULL,
  teacher varchar(128) DEFAULT NULL,
  course_description text,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  image_id bigint DEFAULT NULL,
  cate_name varchar(255) DEFAULT NULL,
  tags varchar(255) DEFAULT NULL,
  charge_type tinyint DEFAULT NULL,
  charge_price decimal(10,2) DEFAULT NULL,
  level tinyint DEFAULT '3',
  simple_desc varchar(255) DEFAULT NULL,
  sort int NOT NULL DEFAULT '100',
  course_status tinyint NOT NULL DEFAULT '0',
  attach_id bigint DEFAULT NULL,
  access_type tinyint NOT NULL,
  image_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_course
-- ----------------------------
BEGIN;
INSERT INTO exam_course (id, course_name, college, major, teacher, course_description, creator, create_time, operator, update_time, is_deleted, tenant_code, image_id, cate_name, tags, charge_type, charge_price, level, simple_desc, sort, course_status, attach_id, access_type, image_url) VALUES (1, '示例课程', NULL, NULL, NULL, '示例课程', 'admin', '2022-11-26 11:00:04', 'admin', '2024-05-10 13:13:03', 0, 'gitee', 4, NULL, NULL, 0, 0.00, 3, '示例课程', 100, 1, NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for exam_course_chapter
-- ----------------------------
DROP TABLE IF EXISTS exam_course_chapter;
CREATE TABLE exam_course_chapter (
  id bigint NOT NULL AUTO_INCREMENT,
  title varchar(128) NOT NULL DEFAULT '',
  sort int DEFAULT NULL,
  course_id bigint DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  chapter_desc varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_course_chapter
-- ----------------------------
BEGIN;
INSERT INTO exam_course_chapter (id, title, sort, course_id, creator, create_time, operator, update_time, is_deleted, tenant_code, chapter_desc) VALUES (1, '单元一 四季美景', 1, 1, 'admin', '2022-11-26 11:00:23', 'admin', '2022-11-26 11:02:24', 0, 'gitee', '单元一 四季美景');
INSERT INTO exam_course_chapter (id, title, sort, course_id, creator, create_time, operator, update_time, is_deleted, tenant_code, chapter_desc) VALUES (2, '单元二 至情至爱', 2, 1, 'admin', '2022-11-26 11:02:14', 'admin', '2022-11-26 11:02:14', 0, 'gitee', '单元二 至情至爱');
INSERT INTO exam_course_chapter (id, title, sort, course_id, creator, create_time, operator, update_time, is_deleted, tenant_code, chapter_desc) VALUES (3, '单元三 学习生活', 3, 1, 'admin', '2022-11-26 11:02:37', 'admin', '2022-11-26 11:02:37', 0, 'gitee', '单元三 学习生活');
INSERT INTO exam_course_chapter (id, title, sort, course_id, creator, create_time, operator, update_time, is_deleted, tenant_code, chapter_desc) VALUES (4, '单元四 人生之舟', 4, 1, 'admin', '2022-11-26 11:02:52', 'admin', '2022-11-26 11:02:52', 0, 'gitee', '单元四 人生之舟');
INSERT INTO exam_course_chapter (id, title, sort, course_id, creator, create_time, operator, update_time, is_deleted, tenant_code, chapter_desc) VALUES (5, '单元五 动物与人', 5, 1, 'admin', '2022-11-26 11:03:03', 'admin', '2022-11-26 11:03:03', 0, 'gitee', '单元五 动物与人');
INSERT INTO exam_course_chapter (id, title, sort, course_id, creator, create_time, operator, update_time, is_deleted, tenant_code, chapter_desc) VALUES (6, '单元六 想象之翼', 6, 1, 'admin', '2022-11-26 11:03:17', 'admin', '2022-11-26 11:03:17', 0, 'gitee', '单元六 想象之翼');
COMMIT;

-- ----------------------------
-- Table structure for exam_course_evaluate
-- ----------------------------
DROP TABLE IF EXISTS exam_course_evaluate;
CREATE TABLE exam_course_evaluate (
  id bigint NOT NULL AUTO_INCREMENT,
  evaluate_content varchar(512) NOT NULL DEFAULT '',
  user_id bigint NOT NULL,
  evaluate_level int   DEFAULT '0',
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  course_id bigint NOT NULL,
  operator_name varchar(16) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_course_evaluate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_course_knowledge_point
-- ----------------------------
DROP TABLE IF EXISTS exam_course_knowledge_point;
CREATE TABLE exam_course_knowledge_point (
  id bigint NOT NULL AUTO_INCREMENT,
  title varchar(128) NOT NULL DEFAULT '',
  sort int NOT NULL,
  section_id bigint NOT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  content text,
  learn_hour int DEFAULT NULL,
  video_id bigint DEFAULT NULL,
  video_name varchar(255) DEFAULT NULL,
  content_type tinyint NOT NULL DEFAULT '0',
  video_url varchar(255) DEFAULT NULL,
  speech_id bigint DEFAULT NULL,
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_course_knowledge_point
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_course_member
-- ----------------------------
DROP TABLE IF EXISTS exam_course_member;
CREATE TABLE exam_course_member (
  id bigint NOT NULL AUTO_INCREMENT,
  course_id bigint DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  user_id bigint NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_course_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_course_section
-- ----------------------------
DROP TABLE IF EXISTS exam_course_section;
CREATE TABLE exam_course_section (
  id bigint NOT NULL AUTO_INCREMENT,
  title varchar(128) NOT NULL DEFAULT '',
  sort int DEFAULT NULL,
  chapter_id bigint DEFAULT NULL,
  learn_hour int DEFAULT NULL,
  video_id bigint DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  section_desc varchar(255) DEFAULT NULL,
  video_name varchar(255) DEFAULT NULL,
  content_type tinyint NOT NULL DEFAULT '0',
  content text,
  video_url varchar(255) DEFAULT NULL,
  speech_id bigint DEFAULT NULL,
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_course_section
-- ----------------------------
BEGIN;
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (2, '1．春/朱自清', 1, 1, 1, NULL, 'admin', '2022-11-26 11:03:37', 'admin', '2022-11-26 11:04:28', 0, 'gitee', '1．春/朱自清', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (3, '2．济南的冬天/老舍', 2, 1, 1, NULL, 'admin', '2022-11-26 11:03:50', 'admin', '2022-11-26 11:04:37', 0, 'gitee', '2．济南的冬天/老舍', NULL, 1, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (4, '3．雨的四季/刘湛秋', 3, 1, 1, NULL, 'admin', '2022-11-26 11:04:17', 'admin', '2022-11-26 11:04:17', 0, 'gitee', '3．雨的四季/刘湛秋', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (5, '4．古代诗歌 观沧海/曹操', 4, 1, 1, NULL, 'admin', '2022-11-26 11:06:01', 'admin', '2022-11-26 11:06:01', 0, 'gitee', '4．古代诗歌 观沧海/曹操', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (6, '5．秋天的怀念/史铁生', 1, 2, 1, NULL, 'admin', '2022-11-26 11:06:29', 'admin', '2022-11-26 11:06:29', 0, 'gitee', '5．秋天的怀念/史铁生', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (7, '6．散步/莫怀戚', 2, 2, 1, NULL, 'admin', '2022-11-26 11:06:41', 'admin', '2022-11-26 11:06:41', 0, 'gitee', '6．散步/莫怀戚', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (8, '7．散文诗两首金色花/泰戈尔', 100, 2, 1, NULL, 'admin', '2022-11-26 11:06:54', 'admin', '2022-11-26 11:06:54', 0, 'gitee', '7．散文诗两首金色花/泰戈尔', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (9, '8.《世说新语》两则咏雪', 100, 2, 1, NULL, 'admin', '2022-11-26 11:07:05', 'admin', '2022-11-26 11:07:05', 0, 'gitee', '8.《世说新语》两则咏雪', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (10, '9．从百草园到三味书屋/鲁迅', 1, 3, 1, NULL, 'admin', '2022-11-26 11:07:24', 'admin', '2022-11-26 11:07:24', 0, 'gitee', '9．从百草园到三味书屋/鲁迅', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (11, '10．再塑生命的人/海伦•凯勒', 2, 3, 1, NULL, 'admin', '2022-11-26 11:07:40', 'admin', '2022-11-26 11:07:40', 0, 'gitee', '10．再塑生命的人/海伦•凯勒', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (12, '11．窃读记/林海音', 3, 3, 1, NULL, 'admin', '2022-11-26 11:07:58', 'admin', '2022-11-26 11:07:58', 0, 'gitee', '11．窃读记/林海音', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (13, '12．《论语》十二章', 4, 3, 1, NULL, 'admin', '2022-11-26 11:08:12', 'admin', '2022-11-26 11:08:12', 0, 'gitee', '12．《论语》十二章', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (14, '13．纪念白求恩/毛泽东', 1, 4, 1, NULL, 'admin', '2022-11-26 11:08:42', 'admin', '2022-11-26 11:08:42', 0, 'gitee', '13．纪念白求恩/毛泽东', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (15, '17．猫/郑振铎', 1, 5, 1, NULL, 'admin', '2022-11-26 11:09:05', 'admin', '2022-11-26 11:09:05', 0, 'gitee', '17．猫/郑振铎', NULL, 0, '', NULL, NULL, NULL);
INSERT INTO exam_course_section (id, title, sort, chapter_id, learn_hour, video_id, creator, create_time, operator, update_time, is_deleted, tenant_code, section_desc, video_name, content_type, content, video_url, speech_id, speech_url) VALUES (16, '21．皇帝的新装/安徒生', 1, 6, 1, NULL, 'admin', '2022-11-26 11:09:21', 'admin', '2022-11-26 11:09:21', 0, 'gitee', '21．皇帝的新装/安徒生', NULL, 0, '', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for exam_exam_evaluate
-- ----------------------------
DROP TABLE IF EXISTS exam_exam_evaluate;
CREATE TABLE exam_exam_evaluate (
  id bigint NOT NULL AUTO_INCREMENT,
  evaluate_content varchar(512) NOT NULL DEFAULT '',
  user_id bigint NOT NULL,
  evaluate_level int   DEFAULT '0',
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  exam_id bigint NOT NULL,
  operator_name varchar(16) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_exam_evaluate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_examination
-- ----------------------------
DROP TABLE IF EXISTS exam_examination;
CREATE TABLE exam_examination (
  id bigint NOT NULL AUTO_INCREMENT,
  examination_name varchar(255) DEFAULT NULL,
  type tinyint DEFAULT NULL,
  attention varchar(3000) DEFAULT NULL,
  start_time timestamp DEFAULT NULL,
  end_time timestamp DEFAULT NULL,
  total_score int NOT NULL,
  status tinyint DEFAULT NULL,
  image_id bigint DEFAULT NULL,
  course_id bigint DEFAULT NULL,
  remark varchar(255) DEFAULT NULL,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  sort int   NOT NULL,
  tags varchar(255) DEFAULT '',
  answer_type tinyint   NOT NULL DEFAULT '0',
  access_type tinyint NOT NULL,
  max_exam_cnt int NOT NULL DEFAULT '0',
  image_url varchar(255) DEFAULT NULL,
  show_subject_type tinyint NOT NULL DEFAULT '0',
  exam_duration_minute int NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_examination
-- ----------------------------
BEGIN;
INSERT INTO exam_examination (id, examination_name, type, attention, start_time, end_time, total_score, status, image_id, course_id, remark, creator, create_time, operator, update_time, is_deleted, tenant_code, sort, tags, answer_type, access_type, max_exam_cnt, image_url, show_subject_type, exam_duration_minute) VALUES (1, '示例考试', 0, '示例考试', '2023-11-13 14:22:02', '2025-11-20 14:22:02', 25, 1, 3, 1, '示例考试', 'admin', '2022-11-13 14:22:55', 'admin', '2024-05-10 13:12:44', 0, 'gitee', 0000000001, '语文', 0, 0, 0, NULL, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for exam_examination_favorites
-- ----------------------------
DROP TABLE IF EXISTS exam_examination_favorites;
CREATE TABLE exam_examination_favorites (
  id bigint NOT NULL AUTO_INCREMENT,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  examination_id bigint NOT NULL,
  user_id bigint NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_examination_favorites
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_examination_member
-- ----------------------------
DROP TABLE IF EXISTS exam_examination_member;
CREATE TABLE exam_examination_member (
  id bigint NOT NULL AUTO_INCREMENT,
  exam_type tinyint   NOT NULL DEFAULT '0',
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  member_type tinyint NOT NULL,
  member_id bigint NOT NULL,
  exam_id bigint NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_examination_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_examination_record
-- ----------------------------
DROP TABLE IF EXISTS exam_examination_record;
CREATE TABLE exam_examination_record (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  type tinyint   NOT NULL,
  examination_id bigint NOT NULL,
  start_time timestamp DEFAULT NULL,
  end_time timestamp DEFAULT NULL,
  score int DEFAULT NULL,
  correct_number int DEFAULT NULL,
  in_correct_number int DEFAULT NULL,
  submit_status tinyint DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_examination_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_examination_subject
-- ----------------------------
DROP TABLE IF EXISTS exam_examination_subject;
CREATE TABLE exam_examination_subject (
  id bigint NOT NULL AUTO_INCREMENT,
  examination_id bigint DEFAULT NULL,
  subject_id bigint NOT NULL,
  tenant_code varchar(16) NOT NULL,
  sort int   NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint   NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (examination_id,sort)
);

-- ----------------------------
-- Records of exam_examination_subject
-- ----------------------------
BEGIN;
INSERT INTO exam_examination_subject (id, examination_id, subject_id, tenant_code, sort, creator, create_time, operator, update_time, is_deleted) VALUES (1, 1, 1, 'gitee', 01, 'admin', '2022-11-13 14:41:41', 'admin', '2022-11-13 14:41:41', 0);
INSERT INTO exam_examination_subject (id, examination_id, subject_id, tenant_code, sort, creator, create_time, operator, update_time, is_deleted) VALUES (2, 1, 2, 'gitee', 02, 'admin', '2022-11-13 14:42:34', 'admin', '2022-11-13 14:42:34', 0);
INSERT INTO exam_examination_subject (id, examination_id, subject_id, tenant_code, sort, creator, create_time, operator, update_time, is_deleted) VALUES (3, 1, 3, 'gitee', 03, 'admin', '2022-11-13 14:43:14', 'admin', '2022-11-13 14:43:14', 0);
INSERT INTO exam_examination_subject (id, examination_id, subject_id, tenant_code, sort, creator, create_time, operator, update_time, is_deleted) VALUES (4, 1, 4, 'gitee', 04, 'admin', '2022-11-13 14:43:47', 'admin', '2022-11-13 14:43:47', 0);
INSERT INTO exam_examination_subject (id, examination_id, subject_id, tenant_code, sort, creator, create_time, operator, update_time, is_deleted) VALUES (5, 1, 5, 'gitee', 05, 'admin', '2022-11-13 14:44:29', 'admin', '2022-11-13 14:44:29', 0);
COMMIT;

-- ----------------------------
-- Table structure for exam_fav_start_count
-- ----------------------------
DROP TABLE IF EXISTS exam_fav_start_count;
CREATE TABLE exam_fav_start_count (
  id bigint NOT NULL AUTO_INCREMENT,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  target_id bigint NOT NULL,
  start_count bigint NOT NULL DEFAULT '0',
  target_type tinyint NOT NULL DEFAULT '0',
  fav_count bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_fav_start_count
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_knowledge
-- ----------------------------
DROP TABLE IF EXISTS exam_knowledge;
CREATE TABLE exam_knowledge (
  id bigint NOT NULL AUTO_INCREMENT,
  knowledge_name varchar(128) NOT NULL,
  knowledge_desc varchar(255) DEFAULT NULL,
  attachment_id bigint DEFAULT NULL,
  status tinyint NOT NULL,
  creator varchar(16) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_knowledge
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_material_subject
-- ----------------------------
DROP TABLE IF EXISTS exam_material_subject;
CREATE TABLE exam_material_subject (
  id bigint NOT NULL AUTO_INCREMENT,
  material_id bigint DEFAULT NULL,
  subject_id bigint NOT NULL,
  tenant_code varchar(16) NOT NULL,
  sort int   NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint   NOT NULL,
  examination_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (material_id,sort)
);

-- ----------------------------
-- Records of exam_material_subject
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_pictures
-- ----------------------------
DROP TABLE IF EXISTS exam_pictures;
CREATE TABLE exam_pictures (
  id bigint NOT NULL,
  picture_address varchar(255) DEFAULT NULL,
  attachment_id bigint DEFAULT NULL,
  creator varchar(16) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_pictures
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_category
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_category;
CREATE TABLE exam_subject_category (
  id bigint NOT NULL AUTO_INCREMENT,
  category_name varchar(128) DEFAULT NULL,
  category_desc varchar(255) DEFAULT NULL,
  parent_id bigint DEFAULT NULL,
  sort int DEFAULT NULL,
  type tinyint NOT NULL DEFAULT '0',
  creator varchar(16) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  status tinyint   NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_category
-- ----------------------------
BEGIN;
INSERT INTO exam_subject_category (id, category_name, category_desc, parent_id, sort, type, creator, create_time, operator, update_time, is_deleted, tenant_code, status) VALUES (1, '语文', NULL, -1, 1, 0, 'admin', '2022-11-13 15:33:49', 'admin', '2022-11-13 15:33:49', 0, 'gitee', 0);
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_choices
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_choices;
CREATE TABLE exam_subject_choices (
  id bigint NOT NULL,
  category_id bigint DEFAULT '0',
  subject_name varchar(10000) NOT NULL,
  choices_type tinyint NOT NULL,
  answer varchar(5000) NOT NULL,
  score int NOT NULL,
  analysis varchar(5000) DEFAULT NULL,
  level tinyint NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  sort int   NOT NULL,
  speech_id bigint DEFAULT NULL,
  subject_video_id bigint DEFAULT NULL,
  subject_video_url varchar(1024) DEFAULT NULL,
  speech_play_limit int DEFAULT NULL,
  auto_play_speech tinyint   NOT NULL DEFAULT '0',
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_choices
-- ----------------------------
BEGIN;
INSERT INTO exam_subject_choices (id, category_id, subject_name, choices_type, answer, score, analysis, level, creator, create_time, operator, update_time, is_deleted, tenant_code, sort, speech_id, subject_video_id, subject_video_url, speech_play_limit, auto_play_speech, speech_url) VALUES (1, 0, '<p>《山行》是描绘了___的景色.</p>', 0, 'C', 5, NULL, 1, 'admin', '2022-11-13 14:41:40', 'admin', '2022-11-13 14:41:40', 0, 'gitee', 0000000001, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO exam_subject_choices (id, category_id, subject_name, choices_type, answer, score, analysis, level, creator, create_time, operator, update_time, is_deleted, tenant_code, sort, speech_id, subject_video_id, subject_video_url, speech_play_limit, auto_play_speech, speech_url) VALUES (2, 0, '<p>&ldquo;劝君更尽一杯酒，西出阳关无故人.&rdquo;出自___的名句</p>', 0, 'B', 5, NULL, 1, 'admin', '2022-11-13 14:42:33', 'admin', '2022-11-13 14:42:33', 0, 'gitee', 0000000002, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO exam_subject_choices (id, category_id, subject_name, choices_type, answer, score, analysis, level, creator, create_time, operator, update_time, is_deleted, tenant_code, sort, speech_id, subject_video_id, subject_video_url, speech_play_limit, auto_play_speech, speech_url) VALUES (3, 0, '<p>把&ldquo;春风&rdquo;比作&ldquo;剪刀&rdquo;的是哪首诗？</p>', 0, 'C', 5, NULL, 1, 'admin', '2022-11-13 14:43:13', 'admin', '2022-11-13 14:43:13', 0, 'gitee', 0000000003, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO exam_subject_choices (id, category_id, subject_name, choices_type, answer, score, analysis, level, creator, create_time, operator, update_time, is_deleted, tenant_code, sort, speech_id, subject_video_id, subject_video_url, speech_play_limit, auto_play_speech, speech_url) VALUES (4, 0, '<p>&ldquo;横看成岭侧成峰，远近高低各不同.&rdquo;诗中写的名胜是</p>', 0, 'D', 5, NULL, 1, 'admin', '2022-11-13 14:43:46', 'admin', '2022-11-13 14:43:46', 0, 'gitee', 0000000004, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO exam_subject_choices (id, category_id, subject_name, choices_type, answer, score, analysis, level, creator, create_time, operator, update_time, is_deleted, tenant_code, sort, speech_id, subject_video_id, subject_video_url, speech_play_limit, auto_play_speech, speech_url) VALUES (5, 0, '<p>&ldquo;解落三秋叶，能开二月花。过江千尺浪，入竹万竿斜.&rdquo;这首诗写的是</p>', 0, 'B', 5, NULL, 1, 'admin', '2022-11-13 14:44:29', 'admin', '2022-11-13 14:44:29', 0, 'gitee', 0000000005, NULL, NULL, NULL, NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_favorites
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_favorites;
CREATE TABLE exam_subject_favorites (
  id bigint NOT NULL AUTO_INCREMENT,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  subject_id bigint NOT NULL,
  user_id bigint NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_favorites
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_fill_blank
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_fill_blank;
CREATE TABLE exam_subject_fill_blank (
  id bigint NOT NULL,
  category_id bigint DEFAULT '0',
  subject_name varchar(5000) NOT NULL,
  answer varchar(5000) NOT NULL,
  score int NOT NULL,
  analysis varchar(5000) DEFAULT NULL,
  level tinyint NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  sort int   NOT NULL,
  speech_id bigint DEFAULT NULL,
  subject_video_id bigint DEFAULT NULL,
  subject_video_url varchar(1024) DEFAULT NULL,
  speech_play_limit int DEFAULT NULL,
  auto_play_speech tinyint   NOT NULL DEFAULT '0',
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_fill_blank
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_judgement
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_judgement;
CREATE TABLE exam_subject_judgement (
  id bigint NOT NULL,
  category_id bigint DEFAULT NULL,
  subject_name varchar(10000) NOT NULL DEFAULT '',
  answer varchar(5000) NOT NULL DEFAULT '',
  score int NOT NULL DEFAULT '0',
  analysis varchar(5000) DEFAULT NULL,
  level tinyint NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  sort int   NOT NULL,
  speech_id bigint DEFAULT NULL,
  subject_video_id bigint DEFAULT NULL,
  subject_video_url varchar(1024) DEFAULT NULL,
  speech_play_limit int DEFAULT NULL,
  auto_play_speech tinyint   NOT NULL DEFAULT '0',
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_judgement
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_material
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_material;
CREATE TABLE exam_subject_material (
  id bigint NOT NULL,
  category_id bigint DEFAULT '0',
  subject_name varchar(5000) NOT NULL,
  answer varchar(5000) NOT NULL,
  score int NOT NULL,
  analysis varchar(5000) DEFAULT NULL,
  level tinyint NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  sort int   NOT NULL,
  speech_id bigint DEFAULT NULL,
  subject_video_id bigint DEFAULT NULL,
  subject_video_url varchar(1024) DEFAULT NULL,
  speech_play_limit int DEFAULT NULL,
  auto_play_speech tinyint   NOT NULL DEFAULT '0',
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_material
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_option
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_option;
CREATE TABLE exam_subject_option (
  id bigint NOT NULL AUTO_INCREMENT,
  subject_choices_id bigint NOT NULL,
  option_name varchar(64) NOT NULL DEFAULT '',
  option_content varchar(5000) NOT NULL DEFAULT '',
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  sort int   NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_option
-- ----------------------------
BEGIN;
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (1, 1, 'A', '<p>春天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (2, 1, 'B', '<p>夏天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (3, 1, 'C', '<p>秋天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (4, 1, 'D', '<p>冬天</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (5, 2, 'A', '<p>李白</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (6, 2, 'B', '<p>王维</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (7, 2, 'C', '<p>王昌龄</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (8, 2, 'D', '<p>杜牧</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (9, 3, 'A', '<p>《忆江南》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (10, 3, 'B', '<p>《滁州西涧》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (11, 3, 'C', '<p>《咏柳》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (12, 3, 'D', '<p>《游园不值》</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (13, 4, 'A', '<p>泰山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (14, 4, 'B', '<p>华山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (15, 4, 'C', '<p>黄山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (16, 4, 'D', '<p>庐山</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (17, 5, 'A', '<p>花</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000001);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (18, 5, 'B', '<p>风</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000002);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (19, 5, 'C', '<p>竹</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000003);
INSERT INTO exam_subject_option (id, subject_choices_id, option_name, option_content, creator, create_time, operator, update_time, is_deleted, tenant_code, sort) VALUES (20, 5, 'D', '<p>水</p>', 'admin', '2022-05-14 20:39:05', 'admin', '2022-05-14 20:39:05', 0, 'gitee', 0000000004);
COMMIT;

-- ----------------------------
-- Table structure for exam_subject_short_answer
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_short_answer;
CREATE TABLE exam_subject_short_answer (
  id bigint NOT NULL,
  category_id bigint DEFAULT NULL,
  subject_name varchar(10000) NOT NULL,
  answer varchar(5000) NOT NULL,
  score int NOT NULL,
  analysis varchar(5000) DEFAULT NULL,
  level tinyint NOT NULL,
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  sort int   NOT NULL,
  speech_id bigint DEFAULT NULL,
  subject_video_id bigint DEFAULT NULL,
  judge_type tinyint NOT NULL DEFAULT '0',
  subject_video_url varchar(1024) DEFAULT NULL,
  speech_play_limit int DEFAULT NULL,
  auto_play_speech tinyint   NOT NULL DEFAULT '0',
  speech_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of exam_subject_short_answer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for exam_subjects
-- ----------------------------
DROP TABLE IF EXISTS exam_subjects;
CREATE TABLE exam_subjects (
  id bigint NOT NULL AUTO_INCREMENT,
  creator varchar(16) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  type tinyint   NOT NULL,
  category_id bigint DEFAULT NULL,
  subject_id bigint DEFAULT NULL,
  sort int NOT NULL DEFAULT '0',
  parent_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (subject_id,type)
);

-- ----------------------------
-- Records of exam_subjects
-- ----------------------------
BEGIN;
INSERT INTO exam_subjects (id, creator, create_time, operator, update_time, is_deleted, tenant_code, type, category_id, subject_id, sort, parent_id) VALUES (1, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 1, 1, NULL);
INSERT INTO exam_subjects (id, creator, create_time, operator, update_time, is_deleted, tenant_code, type, category_id, subject_id, sort, parent_id) VALUES (2, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 2, 2, NULL);
INSERT INTO exam_subjects (id, creator, create_time, operator, update_time, is_deleted, tenant_code, type, category_id, subject_id, sort, parent_id) VALUES (3, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 3, 3, NULL);
INSERT INTO exam_subjects (id, creator, create_time, operator, update_time, is_deleted, tenant_code, type, category_id, subject_id, sort, parent_id) VALUES (4, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 4, 4, NULL);
INSERT INTO exam_subjects (id, creator, create_time, operator, update_time, is_deleted, tenant_code, type, category_id, subject_id, sort, parent_id) VALUES (5, 'admin', '2022-04-14 22:25:06', 'admin', '2022-04-14 22:25:06', 0, 'gitee', 0, NULL, 5, 5, NULL);
COMMIT;

-- ----------------------------
-- Table structure for exam_user_favorites
-- ----------------------------
DROP TABLE IF EXISTS exam_user_favorites;
CREATE TABLE exam_user_favorites (
  id bigint NOT NULL AUTO_INCREMENT,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  target_id bigint NOT NULL,
  user_id bigint NOT NULL,
  target_type tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  UNIQUE (user_id,target_id,target_type)
);

-- ----------------------------
-- Records of exam_user_favorites
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS gen_table;
CREATE TABLE gen_table (
  table_id bigint NOT NULL AUTO_INCREMENT,
  table_name varchar(200) DEFAULT '',
  table_comment varchar(500) DEFAULT '',
  sub_table_name varchar(64) DEFAULT NULL,
  sub_table_fk_name varchar(64) DEFAULT NULL,
  class_name varchar(100) DEFAULT '',
  tpl_category varchar(200) DEFAULT 'crud',
  package_name varchar(100) DEFAULT NULL,
  module_name varchar(30) DEFAULT NULL,
  business_name varchar(30) DEFAULT NULL,
  function_name varchar(50) DEFAULT NULL,
  function_author varchar(50) DEFAULT NULL,
  gen_type char(1) DEFAULT '0',
  gen_path varchar(200) DEFAULT '/',
  options varchar(1000) DEFAULT NULL,
  creator varchar(64) DEFAULT '',
  create_time timestamp DEFAULT NULL,
  operator varchar(64) DEFAULT '',
  update_time timestamp DEFAULT NULL,
  remark varchar(500) DEFAULT NULL,
  is_deleted tinyint DEFAULT NULL,
  tenant_code varchar(16) DEFAULT NULL,
  PRIMARY KEY (table_id)
);

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS gen_table_column;
CREATE TABLE gen_table_column (
  column_id bigint NOT NULL AUTO_INCREMENT,
  table_id varchar(64) DEFAULT NULL,
  column_name varchar(200) DEFAULT NULL,
  column_comment varchar(500) DEFAULT NULL,
  column_type varchar(100) DEFAULT NULL,
  java_type varchar(500) DEFAULT NULL,
  java_field varchar(200) DEFAULT NULL,
  is_pk char(1) DEFAULT NULL,
  is_increment char(1) DEFAULT NULL,
  is_required char(1) DEFAULT NULL,
  is_insert char(1) DEFAULT NULL,
  is_edit char(1) DEFAULT NULL,
  is_list char(1) DEFAULT NULL,
  is_query char(1) DEFAULT NULL,
  query_type varchar(200) DEFAULT 'EQ',
  html_type varchar(200) DEFAULT NULL,
  dict_type varchar(200) DEFAULT '',
  sort int DEFAULT NULL,
  creator varchar(64) DEFAULT '',
  create_time timestamp DEFAULT NULL,
  operator varchar(64) DEFAULT '',
  update_time timestamp DEFAULT NULL,
  is_deleted tinyint DEFAULT NULL,
  tenant_code varchar(16) DEFAULT NULL,
  PRIMARY KEY (column_id)
);

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for operation_banner
-- ----------------------------
DROP TABLE IF EXISTS operation_banner;
CREATE TABLE operation_banner (
  id bigint NOT NULL AUTO_INCREMENT,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  operation_name varchar(255) DEFAULT NULL,
  operation_type tinyint DEFAULT NULL,
  image_url varchar(255) DEFAULT NULL,
  redirect_url varchar(255) DEFAULT NULL,
  operation_desc varchar(255) DEFAULT NULL,
  image_id bigint DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of operation_banner
-- ----------------------------
BEGIN;
INSERT INTO operation_banner (id, creator, create_time, operator, update_time, is_deleted, tenant_code, operation_name, operation_type, image_url, redirect_url, operation_desc, image_id) VALUES (1, 'admin', '2022-11-12 12:42:26', 'admin', '2022-11-12 12:52:00', 0, 'gitee', '运营位 Banner1', 0, 'https://cdn.yunmianshi.com/default_image/10.jpeg?e=1668228237&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:ZW1CihRYlSwQRBlybYWbX8lrz48=', 'https://cdn.yunmianshi.com/default_image/10.jpeg?e=1668228237&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:ZW1CihRYlSwQRBlybYWbX8lrz48=', '2232323', NULL);
INSERT INTO operation_banner (id, creator, create_time, operator, update_time, is_deleted, tenant_code, operation_name, operation_type, image_url, redirect_url, operation_desc, image_id) VALUES (2, 'admin', '2022-11-12 12:43:24', 'admin', '2022-11-12 12:51:56', 0, 'gitee', '运营位 Banner2', 0, 'https://cdn.yunmianshi.com/default_image/13.jpeg?e=1668228494&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:REKRNJMtCekDjmrMkSvbLOuWAvM=', 'https://cdn.yunmianshi.com/default_image/13.jpeg?e=1668228494&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:REKRNJMtCekDjmrMkSvbLOuWAvM=', '3', NULL);
COMMIT;

-- ----------------------------
-- Table structure for shedlock
-- ----------------------------
DROP TABLE IF EXISTS shedlock;
CREATE TABLE shedlock (
  name varchar(64) NOT NULL DEFAULT '',
  lock_until timestamp(3) NULL DEFAULT NULL,
  locked_at timestamp(3) NULL DEFAULT NULL,
  locked_by varchar(255) DEFAULT NULL,
  PRIMARY KEY (name)
);

-- ----------------------------
-- Records of shedlock
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS sys_attachment;
CREATE TABLE sys_attachment (
  id bigint NOT NULL AUTO_INCREMENT,
  attach_name varchar(255) NOT NULL DEFAULT '',
  attach_type varchar(128) NOT NULL DEFAULT '',
  attach_size varchar(255) NOT NULL DEFAULT '',
  url varchar(1024) DEFAULT NULL,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint   NOT NULL,
  tenant_code varchar(16) NOT NULL DEFAULT '',
  group_code varchar(128) NOT NULL,
  hash varchar(255) NOT NULL DEFAULT '',
  upload_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_attachment_chunk
-- ----------------------------
DROP TABLE IF EXISTS sys_attachment_chunk;
CREATE TABLE sys_attachment_chunk (
  id bigint NOT NULL AUTO_INCREMENT,
  chunk_name varchar(255) NOT NULL DEFAULT '',
  chunk_number int NOT NULL DEFAULT '0',
  chunk_data_size int NOT NULL DEFAULT '0',
  chunk_status tinyint NOT NULL DEFAULT '0',
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint   NOT NULL,
  tenant_code varchar(16) NOT NULL DEFAULT '',
  chunk_upload_res varchar(1024) NOT NULL,
  hash varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_attachment_chunk
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_attachment_group
-- ----------------------------
DROP TABLE IF EXISTS sys_attachment_group;
CREATE TABLE sys_attachment_group (
  id bigint NOT NULL AUTO_INCREMENT,
  group_name varchar(255) NOT NULL DEFAULT '',
  group_code varchar(128) NOT NULL DEFAULT '',
  url_expire bigint   NOT NULL,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint   NOT NULL,
  tenant_code varchar(16) NOT NULL DEFAULT '',
  remark varchar(255) DEFAULT NULL,
  storage_type tinyint NOT NULL DEFAULT '2',
  PRIMARY KEY (id),
  UNIQUE (group_code)
);

-- ----------------------------
-- Records of sys_attachment_group
-- ----------------------------
BEGIN;
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (1, '用户头像', 'user_avatar', 00000000002147483647, 'admin', '2022-04-16 16:17:55', 'admin', '2022-07-02 21:01:20', 0, 'gitee', '用户头像', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (2, '课程图片', 'course', 00000000002147483647, 'admin', '2022-04-16 16:46:43', 'admin', '2022-07-02 21:01:05', 0, 'gitee', '课程图片', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (3, '考试图片', 'exam', 00000000002147483647, 'admin', '2022-04-16 16:46:31', 'admin', '2022-07-02 21:01:30', 0, 'gitee', '考试图片', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (4, '其它', 'other', 00000000002147483647, 'admin', '2022-04-16 16:49:32', 'admin', '2022-07-02 21:23:37', 0, 'gitee', '其它', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (5, '语音合成', 'speech', 00000000002147483647, 'admin', '2022-06-18 13:33:29', 'admin', '2022-07-02 21:00:45', 0, 'gitee', '语音合成', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (6, '默认分组', 'default', 00000000002147483647, 'admin', '2022-07-02 20:26:48', 'admin', '2022-07-02 21:00:51', 0, 'gitee', '默认分组', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (7, '默认图片', 'default_image', 00000000002147483647, 'admin', '2022-07-02 22:05:36', 'admin', '2022-07-02 22:05:36', 0, 'gitee', '默认图片', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (8, '考务视频', 'exam/video', 00000000002147483647, 'admin', '2022-12-04 22:38:13', 'admin', '2022-12-04 22:38:13', 0, 'gitee', '考务视频', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (9, '考务图片', 'exam/image', 00000000002147483647, 'admin', '2022-12-04 22:38:30', 'admin', '2022-12-04 22:38:30', 0, 'gitee', '考务图片', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (11, '考务语音', 'exam/speech', 00000000002147483647, 'admin', '2023-06-01 22:36:32', 'admin', '2023-06-01 22:36:39', 0, 'gitee', '考务语音', 2);
INSERT INTO sys_attachment_group (id, group_name, group_code, url_expire, creator, create_time, operator, update_time, is_deleted, tenant_code, remark, storage_type) VALUES (12, '课程课件', 'course/attach', 00000000033233472000, 'admin', '2023-07-20 22:27:40', 'admin', '2023-07-20 22:27:40', 0, 'gitee', NULL, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config (
  id bigint NOT NULL AUTO_INCREMENT,
  config_key varchar(255) NOT NULL,
  config_value varchar(255) NOT NULL DEFAULT '',
  config_desc varchar(255) NOT NULL DEFAULT '',
  creator varchar(16) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE (config_key,tenant_code)
);

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (3, 'sys_web_name', 'SG-EXAM', 'web 首页网站名称', 'admin', '2023-02-21 22:01:24', 'admin', '2023-02-21 23:00:34', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (4, 'sys_avatar', 'SG-EXAM', '系统 LOGO', 'admin', '2023-02-21 22:01:53', 'admin', '2023-02-21 22:05:24', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (5, 'sys_admin_main_title', 'sg-exam 后台管理系统', 'admin 后台主标题', 'admin', '2023-02-21 22:02:16', 'admin', '2023-02-21 22:04:31', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (6, 'sys_admin_sub_title', '方便好用的考试管理系统！', 'admin 后台副标题', 'admin', '2023-02-21 22:02:29', 'admin', '2023-02-21 22:05:04', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (7, 'sys_wxapp_avatar', 'https://cdn.yunmianshi.com/app/wx/svg/study_v3.svg?e=1677074888&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:-zqjhckC8BfXnNC51KmilksTx5o=', '小程序首页封面图片', 'admin', '2023-02-21 22:02:47', 'admin', '2023-02-21 22:08:25', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (8, 'sys_wxapp_main_title', 'HI，欢迎使用 sg-exam', '小程序首页主标题', 'admin', '2023-02-21 22:02:57', 'admin', '2023-02-21 22:03:38', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (9, 'sys_wxapp_sub_title', '演示考试、练习、刷题、在线学习等功能', '小程序首页副标题', 'admin', '2023-02-21 22:03:08', 'admin', '2023-02-21 22:04:01', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (10, 'sys_web_main_title', 'sg-exam', 'web 网站首页主标题', 'admin', '2023-02-21 23:02:27', 'admin', '2023-02-21 23:02:27', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (11, 'sys_web_sub_title_one', '开源项目 sg-exam 功能演示网站', 'web 网站首页副标题', 'admin', '2023-02-21 23:02:51', 'admin', '2023-02-21 23:03:01', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (12, 'sys_web_sub_title_two', '演示考试、练习、刷题、在线学习等功能', 'web 网站首页副标题', 'admin', '2023-02-21 23:03:19', 'admin', '2023-02-21 23:03:19', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (13, 'sys_web_copyright', '粤 ICP 备 2024192218 号', '网站 Copyright', 'admin', '2023-02-22 20:54:38', 'admin', '2023-02-22 20:54:38', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (14, 'sys_login_show_tenant_code', 'true', '登录页面是否展示单位标识输入框', 'admin', '2023-05-11 22:47:47', 'admin', '2023-05-11 22:48:35', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (15, 'sys_file_preview_url', 'http://127.0.0.1:8012/onlinePreview?url=', '附件在线预览地址', 'admin', '2023-06-27 22:36:38', 'admin', '2023-06-27 22:36:38', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (16, 'sys_web_show_banner', 'false', '', 'admin', '2023-02-22 20:54:38', 'admin', '2023-02-22 20:54:38', 0, 'gitee');
INSERT INTO sys_config (id, config_key, config_value, config_desc, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (17, 'sys_tenant_code', 'gitee', '系统默认租户 code', '', '2024-03-29 02:38:51', '', '2024-03-29 02:39:08', 0, '');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
  id bigint NOT NULL AUTO_INCREMENT,
  dept_name varchar(16) DEFAULT NULL,
  dept_desc varchar(128) DEFAULT NULL,
  dept_leader varchar(16) DEFAULT NULL,
  parent_id bigint DEFAULT NULL,
  sort int DEFAULT NULL,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE (dept_name)
);

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO sys_dept (id, dept_name, dept_desc, dept_leader, parent_id, sort, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (1, '示例部门', '示例部门', '管理员', -1, 2, 'admin', '2022-03-25 20:46:58', 'admin', '2024-05-10 13:16:11', 0, 'gitee');
COMMIT;

-- ----------------------------
-- Table structure for sys_feedback
-- ----------------------------
DROP TABLE IF EXISTS sys_feedback;
CREATE TABLE sys_feedback (
  id bigint NOT NULL,
  submitter_id bigint NOT NULL,
  content varchar(1024) DEFAULT NULL,
  status tinyint DEFAULT NULL,
  creator varchar(128) DEFAULT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT NULL,
  tenant_code varchar(16) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_feedback
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log (
  id bigint NOT NULL AUTO_INCREMENT,
  type tinyint DEFAULT NULL,
  title varchar(128) DEFAULT NULL,
  ip varchar(16) DEFAULT NULL,
  user_agent varchar(256) DEFAULT NULL,
  request_uri varchar(128) DEFAULT NULL,
  method varchar(16) DEFAULT NULL,
  params varchar(255) DEFAULT NULL,
  exception varchar(2000) DEFAULT NULL,
  service_id varchar(16) DEFAULT NULL,
  took varchar(16) DEFAULT NULL,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(128) DEFAULT NULL,
  permission varchar(128) DEFAULT NULL,
  path varchar(128) DEFAULT NULL,
  parent_id bigint DEFAULT NULL,
  icon varchar(64) DEFAULT NULL,
  sort varchar(20) DEFAULT NULL,
  type varchar(20) DEFAULT NULL,
  creator varchar(128) DEFAULT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT NULL,
  component varchar(128) DEFAULT NULL,
  is_ext tinyint NOT NULL DEFAULT '0',
  keepalive tinyint DEFAULT NULL,
  remark varchar(128) DEFAULT NULL,
  tenant_code varchar(16) DEFAULT NULL,
  hide_menu tinyint NOT NULL DEFAULT '1',
  redirect varchar(64) NOT NULL DEFAULT '',
  current_active_menu varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (1, '首页', 'dashboard', '/dashboard', -1, 'ant-design:appstore-twotone', '1', '0', 'admin', '2022-07-02 20:24:57', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, '首页', 'gitee', 0, '/dashboard/analysis', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (2, '分析页', 'dashboard:view', '/dashboard/analysis', 1, '', '1', '0', 'admin', '2022-03-25 14:18:55', 'admin', '2022-11-13 11:48:52', 0, 'dashboard/analysis/index', 0, NULL, '查看首页', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (3, '系统管理', 'sys', '/sys', -1, 'ion:settings-outline', '2', '0', 'admin', '2019-04-26 16:10:43', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, NULL, '系统管理', 'gitee', 0, '/sys/tenant', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (4, '菜单管理', 'sys:menu', '/sys/menu', 3, '', '10', '0', 'admin', '2019-04-26 16:16:47', 'admin', '2022-11-13 11:48:52', 0, 'sys/menu/index', 0, NULL, '菜单管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (5, '新增菜单', 'sys:menu:add', NULL, 4, '', '1', '1', 'admin', '2019-04-26 16:35:48', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (6, '修改菜单', 'sys:menu:edit', NULL, 4, '', '2', '1', 'admin', '2019-04-26 16:36:08', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (7, '删除菜单', 'sys:menu:del', NULL, 4, '', '3', '1', 'admin', '2019-04-26 16:36:23', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (8, '导入菜单', 'sys:menu:import', NULL, 4, '', '4', '1', 'admin', '2019-04-26 16:36:44', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (9, '导出菜单', 'sys:menu:export', NULL, 4, '', '5', '1', 'admin', '2019-04-26 16:36:59', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (10, '用户管理', 'sys:user', '/sys/user', 3, '', '2', '0', 'admin', '2019-04-26 16:12:19', 'admin', '2022-11-13 11:48:52', 0, 'sys/user/index', 0, NULL, '用户管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (11, '新增用户', 'sys:user:add', NULL, 10, '', '1', '1', 'admin', '2019-04-26 16:25:51', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (12, '删除用户', 'sys:user:del', NULL, 10, '', '2', '1', 'admin', '2019-04-26 16:26:15', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (13, '修改用户', 'sys:user:edit', NULL, 10, '', '3', '1', 'admin', '2019-04-26 16:26:46', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (14, '导出用户', 'sys:user:export', NULL, 10, '', '4', '1', 'admin', '2019-04-26 16:27:08', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (15, '导入用户', 'sys:user:import', NULL, 10, '', '5', '1', 'admin', '2019-04-26 16:27:26', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (17, '新增部门', 'sys:dept:add', NULL, 10, '', '1', '1', 'admin', '2019-04-26 16:30:01', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (18, '修改部门', 'sys:dept:edit', NULL, 10, '', '2', '1', 'admin', '2019-04-26 16:30:33', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (19, '删除部门', 'sys:dept:del', NULL, 10, '', '3', '1', 'admin', '2019-04-26 16:31:08', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (20, '角色管理', 'sys:role', '/sys/role', 3, '', '9', '0', 'admin', '2019-04-26 16:14:56', 'admin', '2022-11-13 11:48:52', 0, 'sys/role/index', 0, NULL, '角色管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (21, '新增角色', 'sys:role:add', NULL, 20, '', '1', '1', 'admin', '2019-04-26 16:33:11', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (22, '修改角色', 'sys:role:edit', NULL, 20, '', '2', '1', 'admin', '2019-04-26 16:33:28', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (23, '删除角色', 'sys:role:del', NULL, 20, '', '3', '1', 'admin', '2019-04-26 16:33:45', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (24, '分配权限', 'sys:role:auth', NULL, 20, '', '4', '1', 'admin', '2019-04-26 16:34:12', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (25, '单位管理', 'tenant:tenant', '/sys/tenant', 3, '', '1', '0', 'admin', '2019-05-23 21:53:41', 'admin', '2022-11-13 11:48:52', 0, 'sys/tenant/index', 0, NULL, '单位管理', 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (26, '修改单位', 'tenant:tenant:edit', '', 25, '', '2', '1', 'admin', '2019-05-23 21:55:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (27, '删除单位', 'tenant:tenant:del', NULL, 25, '', '3', '1', 'admin', '2019-05-23 21:55:48', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (28, '考务管理', 'exam', '/exam', -1, 'ant-design:bank-twotone', '3', '0', 'admin', '2019-04-26 15:12:02', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, NULL, '考务管理', 'gitee', 0, '/exam/course', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (29, '课程管理', 'exam:course', '/exam/course', 28, '', '1', '0', 'admin', '2019-04-26 15:13:45', 'admin', '2022-11-13 11:48:52', 0, 'exam/course/index', 0, NULL, '课程管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (30, '新增课程', 'exam:course:add', NULL, 29, '', '1', '1', 'admin', '2019-04-26 15:21:44', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (31, '修改课程', 'exam:course:edit', NULL, 29, '', '2', '1', 'admin', '2019-04-26 15:22:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (32, '删除课程', 'exam:course:del', NULL, 29, '', '3', '1', 'admin', '2019-04-26 15:22:43', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (33, '考试管理', 'exam:exam', '/exam/examination', 28, '', '2', '0', 'admin', '2019-04-26 15:14:55', 'admin', '2022-11-13 11:48:52', 0, 'exam/examination/index', 0, NULL, '考试管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (34, '新增考试', 'exam:exam:add', NULL, 33, '', '1', '1', 'admin', '2019-04-26 15:23:30', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (35, '删除考试', 'exam:exam:del', NULL, 33, '', '3', '1', 'admin', '2019-04-26 15:24:26', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (36, '题目管理', NULL, '/exam/examination_subjects/:id', 28, NULL, '7', '0', 'admin', '2022-04-11 22:43:26', 'admin', '2022-11-13 11:48:52', 0, 'exam/examSubjects/index', 0, 0, NULL, 'gitee', 1, '', '/exam/examination');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (37, '导出题目', 'exam:exam:subject:export', NULL, 33, '', '5', '1', 'admin', '2019-04-26 15:26:53', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (38, '导入题目', 'exam:exam:subject:import', NULL, 33, '', '6', '1', 'admin', '2019-04-26 15:28:14', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (39, '新增题目', 'exam:exam:subject:add', NULL, 33, '', '7', '1', 'admin', '2019-04-26 15:29:01', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (40, '删除题目', 'exam:exam:subject:del', NULL, 33, '', '8', '1', 'admin', '2019-04-26 15:29:40', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (41, '题库管理', 'exam:subject', '/exam/subject', 28, '', '3', '0', 'admin', '2019-04-26 15:16:47', 'admin', '2022-11-13 11:48:52', 0, 'exam/subject/index', 0, NULL, '题库管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (42, '新增题目', 'exam:subject:bank:add', NULL, 41, '', '1', '1', 'admin', '2019-04-26 15:30:45', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (43, '修改题目', 'exam:subject:bank:edit', NULL, 41, '', '2', '1', 'admin', '2019-04-26 15:32:00', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (44, '删除题目', 'exam:subject:bank:del', NULL, 41, '', '3', '1', 'admin', '2019-04-26 15:32:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (45, '新增题目分类', 'exam:subject:category:add', NULL, 41, '', '4', '1', 'admin', '2019-04-26 15:33:27', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (46, '修改题目分类', 'exam:subject:category:edit', NULL, 41, '', '5', '1', 'admin', '2019-04-26 15:33:56', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (47, '删除题目分类', 'exam:subject:category:del', NULL, 41, '', '6', '1', 'admin', '2019-04-26 15:34:19', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (48, '导入题目', 'exam:subject:bank:import', NULL, 41, '', '7', '1', 'admin', '2019-04-26 15:35:09', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (49, '导出题目', 'exam:subject:bank:export', NULL, 41, '', '8', '1', 'admin', '2019-04-26 15:36:23', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (50, '成绩管理', 'exam:examRecord', '/exam/score', 28, '', '4', '0', 'admin', '2019-04-26 15:18:30', 'admin', '2022-11-13 11:48:52', 0, 'exam/score/index', 0, NULL, '成绩管理', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (51, '导出成绩', 'exam:examRecord:export', NULL, 50, '', '30', '1', 'admin', '2019-04-26 15:37:19', 'admin', '2022-11-13 11:48:52', 0, NULL, 0, NULL, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (53, '附件管理', 'attachment', '/attachment', -1, 'ant-design:profile-outlined', '98', '0', 'admin', '2022-11-12 12:00:57', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (54, '附件列表', NULL, '/attachment/list', 53, NULL, '2', '0', 'admin', '2022-04-16 15:47:20', 'admin', '2022-11-13 11:48:52', 0, 'attachment/list/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (55, '附件分组', NULL, '/attachment/group', 53, NULL, '1', '0', 'admin', '2022-04-16 15:47:08', 'admin', '2022-11-13 11:48:52', 0, 'attachment/group/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (56, '语音合成', 'speech', '/speech', -1, 'ant-design:codepen-outlined', '97', '0', 'admin', '2022-11-12 12:01:12', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (57, '语音管理', 'speech:synthesis', '/speech/synthesis', 56, NULL, '1', '0', 'admin', '2022-06-16 22:28:53', 'admin', '2022-11-13 11:48:52', 0, 'speech/synthesis/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (58, '运营管理', 'operation', '/operation', -1, 'ant-design:bold-outlined', '4', '0', 'admin', '2022-11-12 12:02:00', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, 0, NULL, 'gitee', 0, '/operation', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (59, 'Banner 管理', 'operation:banner', '/operation/banner', 58, NULL, '1', '0', 'admin', '2022-11-12 12:00:10', 'admin', '2022-11-13 11:48:52', 0, 'operation/banner/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (60, '个人中心', 'personal', '/personal', -1, 'ant-design:user-outlined', '99', '0', 'admin', '2022-04-15 22:35:22', 'admin', '2022-11-13 11:48:52', 0, 'LAYOUT', 0, NULL, '个人管理', 'gitee', 0, '/personal/details', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (61, '个人资料', 'personal:message', '/personal/details', 60, '', '29', '0', 'admin', '2019-04-26 15:00:11', 'admin', '2022-11-13 11:48:52', 0, 'personal/details/index', 0, NULL, '个人资料', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (62, '修改密码', 'personal:password', '/personal/password', 60, '', '30', '0', 'admin', '2019-04-26 15:01:18', 'admin', '2022-11-13 11:48:52', 0, 'personal/password/index', 0, NULL, '修改密码', 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (64, '成绩详情', NULL, '/exam/score_detail/:id', 28, NULL, '8', '0', 'admin', '2022-04-12 13:35:12', 'admin', '2022-11-13 11:48:52', 0, 'exam/scoreDetail/index', 0, 0, NULL, 'gitee', 1, '', '/exam/score');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (65, '题目详情', NULL, '/exam/subject_detail/:id', 28, NULL, '6', '0', 'admin', '2022-04-05 09:54:38', 'admin', '2022-11-13 11:48:52', 0, 'exam/subject/SubjectDetail', 0, 0, NULL, 'gitee', 1, '', '/exam/subject');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (67, '操作日志', 'sys:log', '/sys/log', 3, NULL, '11', '0', 'admin', '2022-11-20 14:16:34', 'admin', '2022-11-20 14:16:34', 0, 'sys/log/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (68, '查看操作日志', 'sys:log:view', NULL, 67, NULL, '1', '1', 'admin', '2022-11-20 14:20:58', 'admin', '2022-11-20 14:20:58', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (73, '查看考试', 'exam:exam:view', NULL, 33, NULL, '1', '1', 'admin', '2022-11-20 18:44:51', 'admin', '2022-11-20 18:44:51', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (74, '编辑考试', 'exam:exam:edit', NULL, 33, NULL, '2', '1', 'admin', '2022-11-20 18:45:11', 'admin', '2022-11-20 18:45:11', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (75, '查看成绩', 'exam:examRecord:view', NULL, 50, NULL, '1', '1', 'admin', '2022-11-20 18:47:23', 'admin', '2022-11-20 18:47:23', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (76, '删除操作日志', 'sys:log:del', NULL, 67, NULL, '2', '1', 'admin', '2022-11-20 18:50:33', 'admin', '2022-11-20 18:50:33', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (77, '查看单位', 'tenant:tenant:view', NULL, 25, NULL, '1', '1', 'admin', '2022-11-20 18:55:12', 'admin', '2022-11-20 18:55:12', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (78, '新增单位', 'tenant:tenant:add', NULL, 25, NULL, '1', '1', 'admin', '2022-11-20 18:57:18', 'admin', '2022-11-20 18:57:18', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (79, '查看用户', 'sys:user:view', NULL, 10, NULL, '1', '1', 'admin', '2022-11-20 19:00:04', 'admin', '2022-11-20 19:00:04', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (81, '查看角色', 'sys:role:view', NULL, 20, NULL, '1', '1', 'admin', '2022-11-20 19:00:55', 'admin', '2022-11-20 19:00:55', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (82, '查看菜单', 'sys:menu:view', NULL, 4, NULL, '1', '1', 'admin', '2022-11-20 19:01:21', 'admin', '2022-11-20 19:01:21', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (83, '查看部门', 'sys:dept:view', NULL, 16, NULL, '1', '1', 'admin', '2022-11-20 19:12:41', 'admin', '2022-11-20 19:12:41', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (84, '查看课程', 'exam:course:view', NULL, 29, NULL, '1', '1', 'admin', '2022-11-20 19:13:01', 'admin', '2022-11-20 19:13:01', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (85, '代码生成', 'sys:gen', '/sys/gen', 3, NULL, '12', '0', 'admin', '2022-11-21 20:54:36', 'admin', '2022-11-21 20:54:36', 0, 'sys/gen/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (86, '查看代码生成', 'sys:gen:view', NULL, 85, NULL, '1', '1', 'admin', '2022-11-21 20:55:13', 'admin', '2022-11-21 20:55:13', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (87, '编辑代码生成', 'sys:gen:edit', NULL, 85, NULL, '2', '1', 'admin', '2022-11-21 20:55:33', 'admin', '2022-11-21 20:55:33', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (88, '新增代码生成', 'sys:gen:add', NULL, 85, NULL, '3', '1', 'admin', '2022-11-21 20:55:56', 'admin', '2022-11-21 20:55:56', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (89, '删除代码生成', 'sys:gen:del', NULL, 85, NULL, '4', '1', 'admin', '2022-11-21 20:56:12', 'admin', '2022-11-21 20:56:12', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (90, '课程章节', NULL, '/exam/course_chapter/:id', 28, NULL, '9', '0', 'admin', '2022-11-21 22:53:11', 'admin', '2022-11-21 22:53:11', 0, 'exam/courseChapter/index', 0, 0, NULL, 'gitee', 1, '', '/exam/course');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (91, '查看题库', 'exam:subject:bank:view', NULL, 41, NULL, '1', '1', 'admin', '2022-11-30 22:42:14', 'admin', '2022-11-30 22:42:14', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (92, '消息管理', 'sys:message', '/sys/message', 3, NULL, '11', '0', 'admin', '2023-02-18 10:17:38', 'admin', '2023-02-18 10:17:38', 0, 'sys/message/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (93, '新增消息', 'sys:message:add', NULL, 92, NULL, '1', '1', 'admin', '2023-02-18 10:29:11', 'admin', '2023-02-18 10:29:11', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (94, '编辑消息', 'sys:message:edit', NULL, 92, NULL, '2', '1', 'admin', '2023-02-18 10:29:29', 'admin', '2023-02-18 10:29:29', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (95, '删除消息', 'sys:message:del', NULL, 92, NULL, '3', '1', 'admin', '2023-02-18 10:29:46', 'admin', '2023-02-18 10:29:46', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (96, '系统配置', 'sys:config', '/sys/config', 3, NULL, '13', '0', 'admin', '2023-02-21 13:33:57', 'admin', '2023-02-21 13:33:57', 0, 'sys/config/index', 0, 0, NULL, 'gitee', 0, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (97, '新增配置', 'sys:config:add', NULL, 96, NULL, '1', '1', 'admin', '2023-02-21 13:34:34', 'admin', '2023-02-21 13:34:34', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (98, '修改配置', 'sys:config:edit', NULL, 96, NULL, '2', '1', 'admin', '2023-02-21 13:34:53', 'admin', '2023-02-21 13:34:53', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (99, '删除配置', 'sys:config:del', NULL, 96, NULL, '3', '1', 'admin', '2023-02-21 13:35:10', 'admin', '2023-02-21 13:35:10', 0, NULL, 0, NULL, NULL, 'gitee', 1, '', '');
INSERT INTO sys_menu (id, name, permission, path, parent_id, icon, sort, type, creator, create_time, operator, update_time, is_deleted, component, is_ext, keepalive, remark, tenant_code, hide_menu, redirect, current_active_menu) VALUES (100, '材料题目管理', NULL, '/exam/material_subjects/:materialId/:examinationId*', 28, NULL, '5', '0', 'admin', '2024-03-26 13:23:24', 'admin', '2024-05-10 13:09:15', 0, 'exam/materialSubjects/index', 0, 0, NULL, 'gitee', 1, '', '/exam/subject');
COMMIT;

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS sys_message;
CREATE TABLE sys_message (
  id bigint NOT NULL AUTO_INCREMENT,
  title varchar(255) DEFAULT NULL,
  content varchar(255) DEFAULT NULL,
  type tinyint   DEFAULT '0',
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  sender varchar(255) NOT NULL,
  receiver_type tinyint NOT NULL,
  status tinyint NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_message_read
-- ----------------------------
DROP TABLE IF EXISTS sys_message_read;
CREATE TABLE sys_message_read (
  id bigint NOT NULL AUTO_INCREMENT,
  message_id bigint NOT NULL,
  receiver_id bigint NOT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_message_read
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_message_receiver
-- ----------------------------
DROP TABLE IF EXISTS sys_message_receiver;
CREATE TABLE sys_message_receiver (
  id bigint NOT NULL AUTO_INCREMENT,
  message_id bigint NOT NULL,
  receiver_id bigint NOT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  type tinyint   NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_message_receiver
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_news
-- ----------------------------
DROP TABLE IF EXISTS sys_news;
CREATE TABLE sys_news (
  id bigint NOT NULL,
  title varchar(1024) DEFAULT NULL,
  location varchar(255) DEFAULT NULL,
  status tinyint DEFAULT NULL,
  creator varchar(128) DEFAULT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT NULL,
  tenant_code varchar(16) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_news
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
  id bigint NOT NULL AUTO_INCREMENT,
  role_name varchar(16) NOT NULL DEFAULT '',
  role_code varchar(64) NOT NULL DEFAULT '',
  role_desc varchar(128) NOT NULL DEFAULT '',
  is_default tinyint NOT NULL DEFAULT '0',
  status tinyint NOT NULL DEFAULT '0',
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE (role_code)
);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO sys_role (id, role_name, role_code, role_desc, is_default, status, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (1, '超级管理员', 'role_admin', '超级管理员', 0, 0, 'admin', '2019-10-07 15:02:17', 'admin', '2019-10-07 14:53:50', 0, 'gitee');
INSERT INTO sys_role (id, role_name, role_code, role_desc, is_default, status, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (2, '预览角色', 'role_preview', '只有查看权限', 1, 0, 'admin', '2022-11-13 13:24:39', 'admin', '2022-11-13 13:24:39', 0, 'gitee');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
  id bigint NOT NULL AUTO_INCREMENT,
  role_id bigint NOT NULL,
  menu_id bigint NOT NULL,
  creator varchar(128) DEFAULT '',
  create_time timestamp DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT '',
  update_time timestamp DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT '0',
  tenant_code varchar(16) DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (1, 1, 1, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (2, 1, 2, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (3, 1, 3, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (4, 1, 4, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (5, 1, 5, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (6, 1, 6, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (7, 1, 7, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (8, 1, 8, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (9, 1, 9, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (10, 1, 10, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (11, 1, 11, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (12, 1, 12, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (13, 1, 13, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (14, 1, 14, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (15, 1, 15, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (17, 1, 17, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (18, 1, 18, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (19, 1, 19, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (20, 1, 20, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (21, 1, 21, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (22, 1, 22, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (23, 1, 23, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (24, 1, 24, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (25, 1, 25, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (26, 1, 26, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (27, 1, 27, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (28, 1, 28, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (29, 1, 29, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (30, 1, 30, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (31, 1, 31, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (32, 1, 32, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (33, 1, 33, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (34, 1, 34, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (35, 1, 35, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (36, 1, 36, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (37, 1, 37, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (38, 1, 38, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (39, 1, 39, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (40, 1, 40, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (41, 1, 41, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (42, 1, 42, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (43, 1, 43, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (44, 1, 44, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (45, 1, 45, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (46, 1, 46, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (47, 1, 47, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (48, 1, 48, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (49, 1, 49, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (50, 1, 50, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (51, 1, 51, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (53, 1, 53, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (54, 1, 54, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (55, 1, 55, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (56, 1, 56, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (57, 1, 57, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (58, 1, 58, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (59, 1, 59, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (60, 1, 60, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (61, 1, 61, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (62, 1, 62, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (63, 1, 63, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (64, 1, 64, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (65, 1, 65, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (67, 2, 1, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (68, 2, 2, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (69, 2, 63, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (70, 2, 61, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (72, 2, 60, '', '2022-11-20 14:46:39', '', '2022-11-20 14:46:39', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (73, 1, 67, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (74, 1, 68, '', '2022-11-13 13:26:11', '', '2022-11-13 13:26:11', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (79, 1, 73, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (80, 1, 74, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (81, 1, 75, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (82, 1, 76, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (83, 1, 77, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (84, 1, 78, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (85, 1, 79, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (86, 1, 80, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (87, 1, 81, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (88, 1, 82, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (89, 1, 83, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (90, 1, 84, NULL, '2022-05-18 22:38:51', '', NULL, NULL, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (119, 2, 77, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (120, 2, 79, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (121, 2, 81, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (122, 2, 82, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (123, 2, 68, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (124, 2, 69, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (125, 2, 73, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (126, 2, 75, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (127, 2, 83, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (128, 2, 84, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (129, 2, 25, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (130, 2, 3, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (131, 2, 10, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (132, 2, 20, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (133, 2, 4, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (134, 2, 67, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (136, 2, 28, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (137, 2, 33, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (138, 2, 50, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (139, 2, 16, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (140, 2, 29, '', '2022-11-20 19:13:28', '', '2022-11-20 19:13:28', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (141, 1, 85, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (142, 1, 86, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (143, 1, 87, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (144, 1, 88, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (145, 1, 89, '', '2022-11-21 20:57:59', '', '2022-11-21 20:57:59', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (146, 2, 86, '', '2022-11-21 20:58:13', '', '2022-11-21 20:58:13', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (147, 2, 85, '', '2022-11-21 20:58:13', '', '2022-11-21 20:58:13', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (406, 1, 90, '', '2022-11-21 23:00:52', '', '2022-11-21 23:00:52', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (426, 2, 90, '', '2022-11-21 23:01:06', '', '2022-11-21 23:01:06', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (445, 2, 91, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (446, 2, 92, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (447, 2, 93, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (448, 2, 94, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (449, 2, 95, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (450, 2, 96, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (451, 2, 97, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (452, 2, 98, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
INSERT INTO sys_role_menu (id, role_id, menu_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (453, 2, 99, '', '2022-11-30 22:42:31', '', '2022-11-30 22:42:31', 0, '');
COMMIT;

-- ----------------------------
-- Table structure for sys_sms
-- ----------------------------
DROP TABLE IF EXISTS sys_sms;
CREATE TABLE sys_sms (
  id bigint NOT NULL AUTO_INCREMENT,
  receiver varchar(255) DEFAULT NULL,
  content varchar(512) DEFAULT NULL,
  response varchar(512) DEFAULT NULL,
  creator varchar(128) DEFAULT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT NULL,
  tenant_code varchar(16) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_sms
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_speech_synthesis
-- ----------------------------
DROP TABLE IF EXISTS sys_speech_synthesis;
CREATE TABLE sys_speech_synthesis (
  id bigint NOT NULL AUTO_INCREMENT,
  text varchar(255) NOT NULL DEFAULT '',
  attach_id bigint NOT NULL DEFAULT '0',
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint   NOT NULL,
  tenant_code varchar(16) NOT NULL DEFAULT '',
  remark varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_speech_synthesis
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS sys_student;
CREATE TABLE sys_student (
  id bigint NOT NULL,
  student_name varchar(128) DEFAULT NULL,
  phone varchar(11) DEFAULT NULL,
  born date DEFAULT NULL,
  sex tinyint DEFAULT NULL,
  address varchar(255) DEFAULT NULL,
  grade varchar(32) DEFAULT NULL,
  school varchar(32) DEFAULT NULL,
  wechat varchar(64) DEFAULT NULL,
  city_id bigint DEFAULT NULL,
  county_id bigint DEFAULT NULL,
  creator varchar(16) NOT NULL,
  create_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(16) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  del_flag tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_student
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS sys_tenant;
CREATE TABLE sys_tenant (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_code varchar(16) NOT NULL DEFAULT '',
  tenant_name varchar(128) NOT NULL DEFAULT '',
  tenant_desc varchar(255) DEFAULT NULL,
  status tinyint NOT NULL DEFAULT '0',
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) DEFAULT '',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  role_id bigint DEFAULT NULL,
  init_status tinyint DEFAULT '0',
  image_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (tenant_code)
);

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO sys_tenant (id, tenant_code, tenant_name, tenant_desc, status, creator, create_time, operator, update_time, is_deleted, role_id, init_status, image_id) VALUES (1, 'gitee', '码云', '码云', 1, 'admin', '2019-05-23 21:46:36', 'admin', '2022-05-20 13:26:27', 0, NULL, 2, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(16) DEFAULT '',
  phone varchar(16) DEFAULT NULL,
  avatar_id bigint DEFAULT NULL,
  email varchar(128) DEFAULT NULL,
  born date DEFAULT NULL,
  status tinyint NOT NULL DEFAULT '0',
  gender tinyint DEFAULT NULL,
  dept_id bigint DEFAULT NULL,
  user_desc varchar(128) DEFAULT NULL,
  parent_uid bigint DEFAULT NULL,
  street_id bigint DEFAULT NULL,
  county_id bigint DEFAULT NULL,
  city_id bigint DEFAULT NULL,
  province_id bigint DEFAULT NULL,
  login_time timestamp DEFAULT NULL,
  lock_time timestamp DEFAULT NULL,
  wechat varchar(128) DEFAULT NULL,
  signature varchar(512) DEFAULT NULL,
  family_role tinyint DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE (phone,tenant_code)
);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO sys_user (id, name, phone, avatar_id, email, born, status, gender, dept_id, user_desc, parent_uid, street_id, county_id, city_id, province_id, login_time, lock_time, wechat, signature, family_role, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (1, '管理员', '15521089123', 1, '1633736729@qq.com', '1988-07-03', 0, 0, 1, '管理员', NULL, NULL, NULL, 530100, 530000, '2022-06-08 13:27:48', NULL, NULL, '测试', NULL, 'admin', '2020-04-05 19:58:07', 'admin', '2024-05-10 13:11:55', 0, 'gitee');
INSERT INTO sys_user (id, name, phone, avatar_id, email, born, status, gender, dept_id, user_desc, parent_uid, street_id, county_id, city_id, province_id, login_time, lock_time, wechat, signature, family_role, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (2, '预览账号', '', 2, NULL, NULL, 0, 0, 1, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2022-11-13 13:27:58', 'admin', '2022-11-13 13:28:20', 0, 'gitee');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_auths
-- ----------------------------
DROP TABLE IF EXISTS sys_user_auths;
CREATE TABLE sys_user_auths (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  identity_type tinyint NOT NULL,
  identifier varchar(128) NOT NULL,
  credential varchar(512) DEFAULT NULL,
  creator varchar(128) NOT NULL,
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint NOT NULL DEFAULT '0',
  tenant_code varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE (identifier,tenant_code)
);

-- ----------------------------
-- Records of sys_user_auths
-- ----------------------------
BEGIN;
INSERT INTO sys_user_auths (id, user_id, identity_type, identifier, credential, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (1, 1, 1, 'admin', '$2a$10$QdZynI98f6aRANUQw4Fk/O6hXoucba6nIdvuXf8Gdy3Zz/mF78FgS', 'admin', '2020-02-29 16:13:29', 'admin', '2022-05-01 16:17:22', 0, 'gitee');
INSERT INTO sys_user_auths (id, user_id, identity_type, identifier, credential, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (2, 2, 1, 'preview', '$2a$10$QdZynI98f6aRANUQw4Fk/O6hXoucba6nIdvuXf8Gdy3Zz/mF78FgS', 'admin', '2022-11-13 13:27:58', 'admin', '2022-11-13 13:27:58', 0, 'gitee');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  creator varchar(128) NOT NULL DEFAULT '',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  operator varchar(128) NOT NULL,
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted tinyint DEFAULT NULL,
  tenant_code varchar(16) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO sys_user_role (id, user_id, role_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (1, 1, 1, 'admin', '2022-03-25 20:46:37', 'admin', '2022-03-25 20:46:37', 0, 'gitee');
INSERT INTO sys_user_role (id, user_id, role_id, creator, create_time, operator, update_time, is_deleted, tenant_code) VALUES (2, 2, 2, 'preview', '2022-11-13 13:27:58', 'preview', '2022-11-13 13:27:58', 0, 'gitee');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_student
-- ----------------------------
DROP TABLE IF EXISTS sys_user_student;
CREATE TABLE sys_user_student (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  student_id bigint NOT NULL,
  relationship_type tinyint NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Records of sys_user_student
-- ----------------------------
BEGIN;
COMMIT;

