import {
  SubjectCategoryListItem,
  SubjectCategoryListGetResultModel,
} from './model/systemModel';
import { defHttp } from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/subjectCategory',
  SubjectCategoryTree: ExamService + '/v1/subjectCategory/categoryTree'
}

export const getSubjectCategoryTree = (params?: SubjectCategoryListItem) =>
  defHttp.get<SubjectCategoryListGetResultModel>({ url: Api.SubjectCategoryTree, params });

export const createCategory = (params?: SubjectCategoryListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateCategory = (id: string, params?: SubjectCategoryListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteCategory = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
