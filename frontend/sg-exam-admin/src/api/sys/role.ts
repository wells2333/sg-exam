import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {RoleApi} from "/@/api/api";

export const getRoleList = (params?: object) =>
  defHttp.get<ApiRes>({url: RoleApi.List, params});

export const getAllRoleList = (params?: object) =>
  defHttp.get<ApiRes>({url: RoleApi.AllList, params});

export const getRoleMenus = (id: string, params?: object) =>
  defHttp.get<ApiRes>({url: RoleApi.RoleMenus + '/' + id, params});

export const createRole = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: RoleApi.Base,
      params,
    }
  );
};

export const updateRole = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: RoleApi.Base + '/' + id,
      params,
    }
  );
};

export const updateRoleStatus = (id: string, params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: RoleApi.Base + '/setRoleStatus/' + id,
      params,
    }
  );
};

export const deleteRole = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: RoleApi.Base + '/' + id,
    }
  );
};
