import {
  ScoreListItem,
  ScoreListGetResultModel,
} from './model/systemModel';

import { defHttp } from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  ScoreList: ExamService + '/v1/examRecord/examRecordList',
  Base: ExamService + '/v1/examRecord'
}

export const getScoreList = (params?: ScoreListItem) =>
  defHttp.get<ScoreListGetResultModel>({ url: Api.ScoreList, params });

export const getScoreDetail = (id: string) => {
  return defHttp.get<void>(
    {
      url: Api.Base + '/' + id + '/details',
    }
  );
};
