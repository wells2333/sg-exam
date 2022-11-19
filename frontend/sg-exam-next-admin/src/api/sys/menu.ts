import { defHttp } from '/@/utils/http/axios';
import { MenuListItem, getMenuListResultModel} from './model/menuModel';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/menu',
  Tree: UserService + '/v1/menu/menuTree',
}

export const getMenuList = () => {
  return defHttp.get<getMenuListResultModel>({ url: Api.Tree });
};

export const userMenuList = () => {
  return defHttp.get<getMenuListResultModel>({ url: Api.Base + '/userMenu' });
}

export const userPermissions = () => {
  return defHttp.get<getMenuListResultModel>({ url: Api.Base + '/userPermissions' });
}

export const defaultTenantMenu = () => {
  return defHttp.get<getMenuListResultModel>({ url: Api.Base + '/defaultTenantMenu' });
}

export const createMenu = (params?: MenuListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateMenu = (id: string, params?: MenuListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteMenu = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
