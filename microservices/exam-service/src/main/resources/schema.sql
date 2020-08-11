-- ----------------------------
-- Table structure for exam_answer
-- ----------------------------
DROP TABLE IF EXISTS exam_answer;
CREATE TABLE exam_answer  (
  id bigint(20) NOT NULL ,
  exam_record_id bigint(20) NOT NULL ,
  subject_id bigint(20) NOT NULL ,
  type tinyint(4) NULL DEFAULT NULL,
  answer varchar(255)  DEFAULT NULL ,
  answer_type tinyint(4) NOT NULL DEFAULT 1 ,
  score tinyint(4) NULL DEFAULT NULL ,
  mark_status tinyint(4) NULL DEFAULT NULL,
  start_time timestamp(0) NULL DEFAULT NULL ,
  end_time timestamp(0) NULL DEFAULT NULL ,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL  AS CURRENT_TIMESTAMP(0) ,
  modifier varchar(128)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_course
-- ----------------------------
DROP TABLE IF EXISTS exam_course;
CREATE TABLE exam_course  (
  id bigint(20) NOT NULL ,
  course_name varchar(128)  DEFAULT '' ,
  college varchar(128)  DEFAULT NULL ,
  major varchar(128)  DEFAULT NULL ,
  teacher varchar(128)  DEFAULT NULL ,
  course_description varchar(255)  DEFAULT NULL ,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL  AS CURRENT_TIMESTAMP(0) ,
  modifier varchar(128)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_examination
-- ----------------------------
DROP TABLE IF EXISTS exam_examination;
CREATE TABLE exam_examination  (
  id bigint(20) NOT NULL ,
  examination_name varchar(255)  DEFAULT NULL ,
  type tinyint(4) NULL DEFAULT NULL ,
  attention varchar(3000)  DEFAULT NULL ,
  start_time timestamp(0) NULL DEFAULT NULL ,
  end_time timestamp(0) NULL DEFAULT NULL ,
  total_score int(11) NOT NULL ,
  status tinyint(4) NULL DEFAULT NULL ,
  avatar_id bigint(20) NULL DEFAULT NULL ,
  course_id bigint(20) NULL DEFAULT NULL ,
  remark varchar(255)  DEFAULT NULL ,
  creator varchar(128)  DEFAULT '' ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(255)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_examination_record
-- ----------------------------
DROP TABLE IF EXISTS exam_examination_record;
CREATE TABLE exam_examination_record  (
  id bigint(20) NOT NULL ,
  user_id bigint(20) NOT NULL ,
  examination_id bigint(20) NOT NULL ,
  start_time timestamp(0) NULL DEFAULT NULL ,
  end_time timestamp(0) NULL DEFAULT NULL ,
  score int(11) NULL DEFAULT NULL ,
  correct_number int(11) NULL DEFAULT NULL ,
  incorrect_number int(11) NULL DEFAULT NULL ,
  submit_status tinyint(4) NULL DEFAULT NULL,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL  AS CURRENT_TIMESTAMP(0) ,
  modifier varchar(128)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_examination_subject
-- ----------------------------
DROP TABLE IF EXISTS exam_examination_subject;
CREATE TABLE exam_examination_subject  (
  id bigint(20) NOT NULL ,
  examination_id bigint(20) NULL DEFAULT NULL ,
  category_id bigint(20) NULL DEFAULT NULL ,
  subject_id bigint(20) NOT NULL ,
  type tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_knowledge
-- ----------------------------
DROP TABLE IF EXISTS exam_knowledge;
CREATE TABLE exam_knowledge  (
  id bigint(20) NOT NULL ,
  knowledge_name varchar(128)   NOT NULL ,
  knowledge_desc varchar(255)  DEFAULT NULL ,
  attachment_id bigint(20) NULL DEFAULT NULL ,
  status tinyint(4) NOT NULL ,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL  AS CURRENT_TIMESTAMP(0) ,
  modifier varchar(128)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_pictures
-- ----------------------------
DROP TABLE IF EXISTS exam_pictures;
CREATE TABLE exam_pictures  (
  id bigint(20) NOT NULL ,
  picture_address varchar(255)  DEFAULT NULL ,
  attachment_id bigint(20) NULL DEFAULT NULL ,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL  AS CURRENT_TIMESTAMP(0) ,
  modifier varchar(128)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_subject_category
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_category;
CREATE TABLE exam_subject_category  (
  id bigint(20) NOT NULL ,
  category_name varchar(128)  DEFAULT NULL ,
  category_desc varchar(255)  DEFAULT NULL ,
  parent_id bigint(20) NULL DEFAULT NULL ,
  sort int(11) NULL DEFAULT NULL ,
  type tinyint(4) NOT NULL DEFAULT 0 ,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)   NOT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_subject_choices
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_choices;
CREATE TABLE exam_subject_choices  (
  id bigint(20) NOT NULL ,
  category_id bigint(20) NULL DEFAULT 0,
  subject_name varchar(5000)   NOT NULL ,
  choices_type tinyint(4) NOT NULL ,
  answer varchar(5000)   NOT NULL ,
  score int(11) NOT NULL ,
  analysis varchar(5000)  DEFAULT NULL ,
  level tinyint(4) NOT NULL ,
  creator varchar(128)  DEFAULT '' ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)  DEFAULT '' ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_subject_judgement
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_judgement;
CREATE TABLE exam_subject_judgement  (
  id bigint(20) NOT NULL ,
  category_id bigint(20) NULL DEFAULT NULL ,
  subject_name varchar(5000)  DEFAULT '' ,
  answer varchar(5000)  DEFAULT '' ,
  score int(11) NOT NULL DEFAULT 0 ,
  analysis varchar(5000)  DEFAULT NULL ,
  level tinyint(4) NOT NULL ,
  creator varchar(128)  DEFAULT '' ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)  DEFAULT '' ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_subject_option
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_option;
CREATE TABLE exam_subject_option  (
  id bigint(20) NOT NULL ,
  subject_choices_id bigint(20) NOT NULL ,
  option_name varchar(64)  DEFAULT '' ,
  option_content varchar(5000)  DEFAULT '' ,
  creator varchar(128)  DEFAULT '' ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)  DEFAULT '' ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for exam_subject_short_answer
-- ----------------------------
DROP TABLE IF EXISTS exam_subject_short_answer;
CREATE TABLE exam_subject_short_answer  (
  id bigint(20) NOT NULL ,
  category_id bigint(20) NULL DEFAULT NULL ,
  subject_name varchar(5000)   NOT NULL ,
  answer varchar(5000)   NOT NULL ,
  score int(11) NOT NULL ,
  analysis varchar(5000)  DEFAULT NULL ,
  level tinyint(4) NOT NULL ,
  creator varchar(128)  DEFAULT '' ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)  DEFAULT '' ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '' ,
  PRIMARY KEY (id)
);