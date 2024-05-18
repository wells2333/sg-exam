import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {AnswerApi, ExaminationApi} from "/@/api/api";

export const getExaminationList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExaminationApi.ExaminationList, params});

export const getExaminationSubjectList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExaminationApi.SubjectList, params});

export const getExaminationMembers = (id?: object) =>
  defHttp.get<ApiRes>({url: ExaminationApi.Base + '/' + id + '/getMembers'});

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

export const deleteBatchExamination = (obj) => {
  return defHttp.post<ApiRes>(
    {
      url: ExaminationApi.Base + '/deleteAll',
      data: obj
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

export const batchAddSubjects = (id?: string, subjects?: Recordable[]) => {
  return defHttp.post<ApiRes>(
    {
      url: ExaminationApi.Base + '/batchAddSubjects/' + id,
      data: subjects
    }
  );
}

export const randomAddSubjects = (id?: string, params?: object) => {
  return defHttp.post<ApiRes>(
    {
      url: ExaminationApi.Base + '/randomAddSubjects/' + id,
      params: params
    }
  );
}

export const getScoreAnalysis = (examinationId?: string) =>
  defHttp.get<ApiRes>({url: AnswerApi.Analysis, params: {examinationId}});

export const generateQrCodeMessage = (examinationId?: string) =>
  defHttp.get<string>({url: ExaminationApi.Base + '/generateQrCodeMessage/' + examinationId, params: {}});


