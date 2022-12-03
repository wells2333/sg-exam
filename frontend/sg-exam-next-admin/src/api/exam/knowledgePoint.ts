import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ExamCourseKnowledgePointApi} from "/@/api/api";

export const getExamCourseKnowledgePointList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExamCourseKnowledgePointApi.ExamCourseKnowledgePointList, params});

export const createExamCourseKnowledgePoint = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: ExamCourseKnowledgePointApi.Base,
      params,
    }
  );
};

export const updateExamCourseKnowledgePoint = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: ExamCourseKnowledgePointApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteExamCourseKnowledgePoint = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: ExamCourseKnowledgePointApi.Base + '/' + id,
    }
  );
};
