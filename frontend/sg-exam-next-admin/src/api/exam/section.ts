import {SectionListGetResultModel, SectionListItem} from './model/systemModel';
import {defHttp} from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/section/',
  CourseList: ExamService + '/v1/section/list'
}

export const getSectionList = (params?: SectionListItem) =>
  defHttp.get<SectionListGetResultModel>({url: Api.CourseList, params});

export const createSection = (params?: SectionListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateSection = (id: string, params?: SectionListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteSection = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
