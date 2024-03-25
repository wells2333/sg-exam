import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {AnswerApi, MaterialApi} from "/@/api/api";

export const getMaterialList = (params?: object) =>
  defHttp.get<ApiRes>({url: MaterialApi.MaterialList, params});

export const getMaterialSubjectList = (params?: object) =>
  defHttp.get<ApiRes>({url: MaterialApi.SubjectList, params});

export const getMaterialMembers = (id?: object) =>
  defHttp.get<ApiRes>({url: MaterialApi.Base + '/' + id + '/getMembers'});

export const createMaterial = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: MaterialApi.Base,
      params,
    }
  );
};

export const updateMaterial = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: MaterialApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteMaterial = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: MaterialApi.Base + '/' + id,
    }
  );
};

export const nexSubjectNo = (id?: string) => {
  return defHttp.get<string>(
    {
      url: MaterialApi.Base + '/nexSubjectNo/' + id,
    }
  );
}

export const batchAddSubjects = (id?: string, subjects?: Recordable[]) => {
  return defHttp.post<ApiRes>(
    {
      url: MaterialApi.Base + '/batchAddSubjects/' + id,
      data: subjects
    }
  );
}

export const randomAddSubjects = (id?: string, params?: object) => {
  return defHttp.post<ApiRes>(
    {
      url: MaterialApi.Base + '/randomAddSubjects/' + id,
      params: params
    }
  );
}

export const getScoreAnalysis = (MaterialId?: string) =>
  defHttp.get<ApiRes>({url: AnswerApi.Analysis, params: {MaterialId}});

export const generateQrCodeMessage = (MaterialId?: string) =>
  defHttp.get<string>({url: MaterialApi.Base + '/generateQrCodeMessage/' + MaterialId, params: {}});


