import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ExamCourseEvaluateApi} from "/@/api/api";

export const getExamCourseEvaluateList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExamCourseEvaluateApi.ExamCourseEvaluateList, params});

export const createExamCourseEvaluate = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: ExamCourseEvaluateApi.Base,
      params,
    }
  );
};

export const updateExamCourseEvaluate = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: ExamCourseEvaluateApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteExamCourseEvaluate = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: ExamCourseEvaluateApi.Base + '/' + id,
    }
  );
};
