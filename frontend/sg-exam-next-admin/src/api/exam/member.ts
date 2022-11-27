import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ExamCourseMemberApi} from "/@/api/api";

export const getExamCourseMemberList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExamCourseMemberApi.ExamCourseMemberList, params});

export const createExamCourseMember = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: ExamCourseMemberApi.Base,
      params,
    }
  );
};

export const updateExamCourseMember = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: ExamCourseMemberApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteExamCourseMember = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: ExamCourseMemberApi.Base + '/' + id,
    }
  );
};
