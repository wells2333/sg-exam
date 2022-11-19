import {
  RoleListItem,
  RoleListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/role',
  List: UserService + '/v1/role/roleList',
  AllList: UserService + '/v1/role/allRoles',
  RoleMenus: UserService + '/v1/role/roleMenus'
}

export const getRoleList = (params?: RoleListItem) =>
  defHttp.get<RoleListGetResultModel>({ url: Api.List, params });

export const getAllRoleList = (params?: RoleListItem) =>
  defHttp.get<RoleListGetResultModel>({ url: Api.AllList, params });

export const getRoleMenus = (id: string, params?: RoleListItem) =>
  defHttp.get<RoleListGetResultModel>({ url: Api.RoleMenus + '/' + id, params });

export const createRole = (params?: RoleListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateRole = (id: string, params?: RoleListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const updateRoleStatus = (id: string, params?: RoleListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base + '/setRoleStatus/' + id,
      params,
    }
  );
};

export const deleteRole = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
