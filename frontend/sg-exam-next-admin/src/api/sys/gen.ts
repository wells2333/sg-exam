import {
  GenListItem,
  GenListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/tool/gen',
  TenantList: UserService + '/tool/gen/list',
}

export const getGenList = (params?: GenListItem) =>
  defHttp.get<GenListGetResultModel>({url: Api.TenantList, params});

export const createGen = (params?: GenListItem
) => {
  return defHttp.get<void>(
    {
      url: Api.Base + '/importTable',
      params,
    }
  );
};

export const updateGen = (id: string, params?: GenListItem) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const genCode = (table: string) => {
  const url = Api.Base + '/download/' + table;
  window.open(url);
};

export const deleteGen = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};

