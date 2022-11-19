import {
  OptionListItem,
  OptionListGetResultModel
} from './model/systemModel';
import { defHttp } from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/option/',
  OptionList: ExamService + '/v1/option/defaultOptions',
}

export const getDefaultOptionList = (params?: OptionListItem) =>
  defHttp.get<OptionListGetResultModel>({ url: Api.OptionList, params });
