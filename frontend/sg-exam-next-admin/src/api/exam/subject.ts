import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {SubjectsApi} from "/@/api/api";
import {UploadFileParams} from "/#/axios";

export const getSubjectList = (params?: object) =>
  defHttp.get<ApiRes | undefined>({url: SubjectsApi.SubjectList, params});

export const getSubjectInfo = (id: string, params?: object) =>
  defHttp.get<ApiRes>({url: SubjectsApi.Base + '/' + id, params});

export const createSubject = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: SubjectsApi.Base,
      params,
    }
  );
};

export const updateSubject = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: SubjectsApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteSubject = (id: string, params?: object
) => {
  return defHttp.delete<ApiRes>(
    {
      url: SubjectsApi.Base + '/' + id,
      params,
    }
  );
};

export const cateNexSubjectNo = (id?: string) => {
  return defHttp.get<string>(
    {
      url: SubjectsApi.Base + '/nexSubjectNo/' + id,
    }
  );
}

export const getSubjectCountByCategoryId = (id?: string) => {
  return defHttp.get<string>(
    {
      url: SubjectsApi.Base + '/getSubjectCountByCategoryId?categoryId=' + id,
    }
  );
}

export function uploadJSON(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: SubjectsApi.Base + '/importJson',
      onUploadProgress,
    },
    params,
  );
}

export function uploadEXCEL(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: SubjectsApi.Base + '/importExcel',
      onUploadProgress,
    },
    params,
  );
}
