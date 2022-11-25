import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ExaminationApi} from "/@/api/api";

export const getExaminationList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExaminationApi.ExaminationList, params});

export const getExaminationSubjectList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExaminationApi.SubjectList, params});

export const createExamination = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: ExaminationApi.Base,
      params,
    }
  );
};

export const updateExamination = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: ExaminationApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteExamination = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: ExaminationApi.Base + '/' + id,
    }
  );
};

export const nexSubjectNo = (id?: string) => {
  return defHttp.get<string>(
    {
      url: ExaminationApi.Base + '/nexSubjectNo/' + id,
    }
  );
}

