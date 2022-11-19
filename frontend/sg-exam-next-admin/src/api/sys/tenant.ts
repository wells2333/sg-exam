import {
  TenantListItem,
  TenantListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/tenant',
  TenantList: UserService + '/v1/tenant/tenantList',
}

export const getTenantList = (params?: TenantListItem) =>
  defHttp.get<TenantListGetResultModel>({url: Api.TenantList, params});

export const createTenant = (params?: TenantListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateTenant = (id: string, params?: TenantListItem) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteTenant = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};

export const initTenant = (id: string) => {
  return defHttp.post<void>(
    {
      url: Api.Base + '/init/' + id,
    }
  );
};


