import {
  SubjectListItem,
  SubjectListGetResultModel,
} from './model/systemModel';
import { defHttp } from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/subjects',
  SubjectList: ExamService + '/v1/subjects/subjectList',
}

export const getSubjectList = (params?: SubjectListItem) =>
  defHttp.get<SubjectListGetResultModel | undefined>({ url: Api.SubjectList, params });

export const getSubjectInfo = (id: string, params?: SubjectListItem) =>
  defHttp.get<SubjectListGetResultModel>({ url: Api.Base + '/' + id, params });

export const createSubject = (params?: SubjectListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateSubject = (id: string, params?: SubjectListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteSubject = (id: string, params?: SubjectListItem
) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const cateNexSubjectNo = (id?: string) => {
  return defHttp.get<string>(
    {
      url: Api.Base + '/nexSubjectNo/' + id,
    }
  );
}
