import {ChapterListGetResultModel, ChapterListItem} from './model/systemModel';
import {defHttp} from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/chapter/',
  CourseList: ExamService + '/v1/chapter/list'
}

export const getChapterList = (params?: ChapterListItem) =>
  defHttp.get<ChapterListGetResultModel>({url: Api.CourseList, params});

export const createChapter = (params?: ChapterListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateChapter = (id: string, params?: ChapterListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteChapter = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
