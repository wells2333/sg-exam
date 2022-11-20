import {
  LogListItem,
  LogListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/log',
  LogList: UserService + '/v1/log/logList',
}

export const getLogList = (params?: LogListItem) =>
  defHttp.get<LogListGetResultModel>({url: Api.LogList, params});

export const deleteLog = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};


