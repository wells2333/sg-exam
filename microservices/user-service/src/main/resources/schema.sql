-- ----------------------------
-- Table structure for sys_attachment
-- ----------------------------
DROP TABLE IF EXISTS sys_attachment;
CREATE TABLE sys_attachment  (
  id bigint(20) NOT NULL,
  attach_name varchar(255)  DEFAULT NULL ,
  attach_type varchar(128)  DEFAULT NULL ,
  attach_size varchar(255)  DEFAULT NULL ,
  group_name varchar(255)  DEFAULT NULL ,
  fast_file_id varchar(255)  DEFAULT NULL ,
  busi_id varchar(255)  DEFAULT NULL ,
  busi_module varchar(255)  DEFAULT NULL ,
  busi_type varchar(255)  DEFAULT NULL ,
  preview_url varchar(255)  DEFAULT NULL ,
  upload_type tinyint(4) NULL DEFAULT NULL ,
  creator varchar(128)  DEFAULT NULL ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)  DEFAULT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NULL DEFAULT NULL ,
  application_code varchar(128)  DEFAULT NULL ,
  tenant_code varchar(128)  DEFAULT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept  (
  id bigint(20) NOT NULL ,
  dept_name varchar(128)  DEFAULT NULL ,
  dept_desc varchar(255)  DEFAULT NULL,
  dept_leader varchar(128)  DEFAULT NULL,
  parent_id bigint(20) NULL DEFAULT NULL ,
  sort int(11) NULL DEFAULT NULL ,
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
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log  (
  id bigint(20) NOT NULL ,
  type int(11) NULL DEFAULT NULL ,
  title varchar(255)  DEFAULT NULL ,
  ip varchar(128)  DEFAULT NULL ,
  user_agent varchar(255)  DEFAULT NULL ,
  request_uri varchar(128)  DEFAULT NULL ,
  method varchar(64)  DEFAULT NULL ,
  params varchar(255)  DEFAULT NULL ,
  exception varchar(2000)  DEFAULT NULL ,
  service_id varchar(128)  DEFAULT NULL ,
  time varchar(64)  DEFAULT NULL ,
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
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu  (
  id varchar(64)   NOT NULL,
  name varchar(255)  DEFAULT NULL ,
  permission varchar(255)  DEFAULT NULL ,
  url varchar(255)  DEFAULT NULL ,
  parent_id varchar(64)  DEFAULT NULL ,
  icon varchar(255)  DEFAULT NULL ,
  sort varchar(20)  DEFAULT NULL ,
  type varchar(20)  DEFAULT NULL ,
  data_level int(11) NULL DEFAULT NULL,
  creator varchar(255)  DEFAULT NULL,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(255)  DEFAULT NULL,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag varchar(20)  DEFAULT NULL,
  application_code varchar(64)  DEFAULT NULL,
  component varchar(255)  DEFAULT NULL ,
  path varchar(255)  DEFAULT NULL ,
  redirect varchar(255)  DEFAULT NULL ,
  remark varchar(255)  DEFAULT NULL,
  tenant_code varchar(255)  DEFAULT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role  (
  id bigint(20) NOT NULL ,
  role_name varchar(128)  DEFAULT '' ,
  role_code varchar(128)  DEFAULT '' ,
  role_desc varchar(255)  DEFAULT '' ,
  is_default tinyint(4) NOT NULL DEFAULT 0 ,
  status tinyint(4) NOT NULL DEFAULT 0 ,
  creator varchar(255)  DEFAULT '' ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(255)  DEFAULT '' ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu  (
  id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  menu_id bigint(20) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS sys_student;
CREATE TABLE sys_student  (
  id bigint(20) NOT NULL ,
  student_name varchar(128)  DEFAULT NULL ,
  phone varchar(11)  DEFAULT NULL ,
  born date NULL DEFAULT NULL,
  sex tinyint(4) NULL DEFAULT NULL ,
  address varchar(255)  DEFAULT NULL ,
  grade varchar(32)  DEFAULT NULL ,
  school varchar(32)  DEFAULT NULL ,
  wechat varchar(64)  DEFAULT NULL ,
  city_id bigint(20) NULL DEFAULT NULL ,
  county_id bigint(20) NULL DEFAULT NULL ,
  creator varchar(128)   NOT NULL ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)   NOT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)   NOT NULL ,
  tenant_code varchar(128)   NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS sys_tenant;
CREATE TABLE sys_tenant  (
  id bigint(20) NOT NULL ,
  tenant_code varchar(128)  DEFAULT '' ,
  tenant_name varchar(128)  DEFAULT '' ,
  tenant_desc varchar(255)  DEFAULT NULL ,
  status tinyint(4) NOT NULL DEFAULT 0 ,
  creator varchar(128)  DEFAULT '',
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(128)  DEFAULT '',
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user  (
  id bigint(20) NOT NULL ,
  name varchar(128)  DEFAULT '' ,
  phone varchar(255)  DEFAULT '',
  avatar_id varchar(64)  DEFAULT NULL ,
  email varchar(128)  DEFAULT NULL ,
  born date NULL DEFAULT NULL ,
  status tinyint(4) NOT NULL DEFAULT 0,
  sex tinyint(4) NULL DEFAULT NULL ,
  dept_id bigint(20) NULL DEFAULT NULL ,
  user_desc varchar(128)  DEFAULT NULL ,
  parent_uid bigint(20) NULL DEFAULT NULL ,
  street_id bigint(20) NULL DEFAULT NULL ,
  county_id bigint(20) NULL DEFAULT NULL ,
  city_id bigint(20) NULL DEFAULT NULL ,
  province_id bigint(20) NULL DEFAULT NULL ,
  login_time timestamp(0) NULL DEFAULT NULL ,
  lock_time timestamp(0) NULL DEFAULT NULL ,
  wechat varchar(128)  DEFAULT NULL ,
  signature varchar(255)  DEFAULT NULL ,
  family_role tinyint(4) NULL DEFAULT NULL ,
  creator varchar(255)   NOT NULL ,
  create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  modifier varchar(255)   NOT NULL ,
  modify_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  del_flag tinyint(4) NOT NULL DEFAULT 0 ,
  application_code varchar(128)  DEFAULT '' ,
  tenant_code varchar(128)  DEFAULT '',
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_user_auths
-- ----------------------------
DROP TABLE IF EXISTS sys_user_auths;
CREATE TABLE sys_user_auths  (
  id bigint(20) NOT NULL ,
  user_id bigint(20) NOT NULL ,
  identity_type tinyint(4) NOT NULL ,
  identifier varchar(128)   NOT NULL ,
  credential varchar(255)  DEFAULT NULL ,
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
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role  (
  id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_user_student
-- ----------------------------
DROP TABLE IF EXISTS sys_user_student;
CREATE TABLE sys_user_student  (
  id bigint(20) NOT NULL ,
  user_id bigint(20) NOT NULL ,
  student_id bigint(20) NOT NULL ,
  relationship_type tinyint(4) NOT NULL ,
  PRIMARY KEY (id)
);