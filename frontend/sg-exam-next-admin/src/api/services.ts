import { getAuthService, getUserService, getExamService} from "/@/utils/env";

export const AuthService = getAuthService();

export const UserService = getUserService();

export const ExamService = getExamService();
