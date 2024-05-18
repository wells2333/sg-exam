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

export const batchAddSubjects = (materialId?: string,examinationId?: string, subjects?: Recordable[]) => {
  var hashMap = {};
  hashMap["examinationId"] = examinationId;
  hashMap["subjects"] = subjects;
  return defHttp.post<ApiRes>(
    {
      url: MaterialApi.Base + '/batchAddSubjects/' + materialId,
      data: hashMap
    }
  );
}

export const randomAddSubjects = (materialId?: string,examinationId?: string, params?: object) => {
  params['examinationId'] = examinationId;
  return defHttp.post<ApiRes>(
    {
      url: MaterialApi.Base + '/randomAddSubjects/' + materialId,
      params: params
    }
  );
}

export const getScoreAnalysis = (MaterialId?: string) =>
  defHttp.get<ApiRes>({url: AnswerApi.Analysis, params: {MaterialId}});

export const generateQrCodeMessage = (MaterialId?: string) =>
  defHttp.get<string>({url: MaterialApi.Base + '/generateQrCodeMessage/' + MaterialId, params: {}});


