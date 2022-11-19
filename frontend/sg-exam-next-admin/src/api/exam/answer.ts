import {
  AnswerListItem,
  AnswerListGetResultModel,
} from './model/systemModel';

import { defHttp } from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  AnswerList: ExamService + '/v1/answer/answerList',
  Base: ExamService + '/v1/answer'
}

export const getAnswerList = (params?: AnswerListItem) =>
  defHttp.get<AnswerListGetResultModel>({ url: Api.AnswerList, params });

export const markAnswer = (params?: AnswerListItem) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/mark',
      params,
    }
  );
}
