import {
  ExaminationListItem,
  ExaminationListGetResultModel,
  SubjectListItem,
  SubjectListGetResultModel,
} from './model/systemModel';
import { defHttp } from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/examination',
  ExaminationList: ExamService + '/v1/examination/examinationList',
  SubjectList: ExamService + '/v1/examination/subjectList'
}

export const getExaminationList = (params?: ExaminationListItem) =>
  defHttp.get<ExaminationListGetResultModel>({ url: Api.ExaminationList, params });

export const getExaminationSubjectList = (params?: SubjectListItem) =>
  defHttp.get<SubjectListGetResultModel>({ url: Api.SubjectList, params });

export const createExamination = (params?: ExaminationListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateExamination = (id: string, params?: ExaminationListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteExamination = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};

export const nexSubjectNo = (id?: string) => {
  return defHttp.get<string>(
    {
      url: Api.Base + '/nexSubjectNo/' + id,
    }
  );
}

