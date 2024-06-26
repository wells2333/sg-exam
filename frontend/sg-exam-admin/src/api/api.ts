import {AuthService, ExamService, UserService} from "/@/api/services";

export const AttachmentApi = {
  Base: UserService + '/v1/attachment',
  AttachmentList: UserService + '/v1/attachment/attachmentList',
  Upload: UserService + '/v1/attachment/upload',
  UploadChunk: UserService + '/v1/attachment/uploadChunk'
}

export const AttachmentGroupApi = {
  Base: UserService + '/v1/attachGroup',
  GroupList: UserService + '/v1/attachGroup/groupList',
}

export const AnswerApi = {
  AnswerList: ExamService + '/v1/answer/answerList',
  Base: ExamService + '/v1/answer',
  Analysis: ExamService + '/v1/answer/analysis',
}

export const ChapterApi = {
  Base: ExamService + '/v1/chapter',
  CourseList: ExamService + '/v1/chapter/list'
}

export const CourseApi = {
  Base: ExamService + '/v1/course',
  CourseList: ExamService + '/v1/course/courseList',
  AllCourses: ExamService + '/v1/course/allCourses'
}

export const ExaminationApi = {
  Base: ExamService + '/v1/examination',
  ExaminationList: ExamService + '/v1/examination/examinationList',
  SubjectList: ExamService + '/v1/examination/subjectList'
}
export const MaterialApi = {
  Base: ExamService + '/v1/material',
  ExaminationList: ExamService + '/v1/material/examinationList',
  SubjectList: ExamService + '/v1/material/subjectList'
}

export const ExamMediaApi = {
  UploadSpeech: UserService + '/v1/examMedia/uploadSpeech',
  UploadVideo: UserService + '/v1/examMedia/uploadVideo',
  UploadImage: UserService + '/v1/examMedia/uploadImage'
}

export const ExamOptionApi = {
  Base: ExamService + '/v1/option',
  OptionList: ExamService + '/v1/option/defaultOptions',
}

export const ExamRecordApi = {
  ScoreList: ExamService + '/v1/examRecord/examRecordList',
  Base: ExamService + '/v1/examRecord',
  Export: ExamService + '/v1/examRecord/export',
}

export const SectionApi = {
  Base: ExamService + '/v1/section',
  CourseList: ExamService + '/v1/section/list'
}

export const SubjectsApi = {
  Base: ExamService + '/v1/subjects',
  SubjectList: ExamService + '/v1/subjects/subjectList',
  JsonTemplate: ExamService + '/v1/subjects/template/json',
  ExcelTemplate: ExamService + '/v1/subjects/template/excel',
  Export: ExamService + '/v1/subjects/export',
}

export const SubjectCategoryApi = {
  Base: ExamService + '/v1/subjectCategory',
  SubjectCategoryTree: ExamService + '/v1/subjectCategory/categoryTree'
}

export const OperationApi = {
  Base: UserService + '/v1/operation/banner',
  BannerList: UserService + '/v1/operation/banner/list',
}

export const SpeechApi = {
  Base: UserService + '/v1/speechSynthesis',
  SynthesisList: UserService + '/v1/speechSynthesis/speechSynthesisList',
}

export const DashboardApi = {
  Dashboard: UserService + '/v1/dashboard',
  Tendency: UserService + '/v1/dashboard/examRecordTendency'
}

export const DeptApi = {
  Base: UserService + '/v1/dept',
  DeptList: UserService + '/v1/dept/deptList',
}

export const GenApi = {
  Base: UserService + '/tool/gen',
  TenantList: UserService + '/tool/gen/list',
}

export const LogApi = {
  Base: UserService + '/v1/log',
  LogList: UserService + '/v1/log/logList',
}

export const MessageApi = {
  Base: UserService + '/v1/message',
  MessageList: UserService + '/v1/message/list',
}

export const MessageReceiverApi = {
  Base: UserService + '/v1/receiver',
  MessageList: UserService + '/v1/receiver/list',
}

export const MessageReadApi = {
  Base: UserService + '/v1/read',
  ReadMessageList: UserService + '/v1/read/list',
}

export const MenuApi = {
  Base: UserService + '/v1/menu',
  Tree: UserService + '/v1/menu/menuTree',
}

export const RoleApi = {
  Base: UserService + '/v1/role',
  List: UserService + '/v1/role/roleList',
  AllList: UserService + '/v1/role/allRoles',
  RoleMenus: UserService + '/v1/role/roleMenus'
}

export const TenantApi = {
  Base: UserService + '/v1/tenant',
  TenantList: UserService + '/v1/tenant/tenantList',
}

export const UserApi = {
  Login: AuthService + '/login',
  Register: AuthService + '/v1/user/anonymousUser/register',
  Logout: AuthService + '/v1/token/logout',
  Base: UserService + '/v1/user',
  GetUserInfo: UserService + '/v1/user/info',
  GetPermCode: '/getPermCode',
  UserList: UserService + '/v1/user/userList',
  UploadAvatar: UserService + '/v1/user/uploadAvatar',
  UpdateAvatar: UserService + '/v1/user/updateAvatar',
  GetTicket: UserService + '/v1/wx/getTicket',
  OpenId: UserService + '/v1/wx/getOpenId',
}

export const ExamCourseEvaluateApi = {
  Base: UserService + '/v1/evaluate',
  ExamCourseEvaluateList: UserService + '/v1/evaluate/list',
}

export const ExamCourseMemberApi = {
  Base: UserService + '/v1/member',
  ExamCourseMemberList: UserService + '/v1/member/list',
}

export const ExamCourseKnowledgePointApi = {
  Base: UserService + '/v1/knowledgePoint',
  ExamCourseKnowledgePointList: UserService + '/v1/knowledgePoint/list',
}

export const SysConfigApi = {
  Base: UserService + '/v1/config',
  ConfigList: UserService + '/v1/config/list',
}

export const SelectApi = {
  Base: UserService + '/v1/select',
  UserList: UserService + '/v1/select/userList',
  DeptList: UserService + '/v1/select/deptList',
}
