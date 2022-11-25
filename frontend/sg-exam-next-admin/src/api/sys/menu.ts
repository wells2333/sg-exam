import {defHttp} from '/@/utils/http/axios';
import {getMenuListResultModel, MenuListItem} from './model/menuModel';
import {ApiRes} from "/@/api/constant";
import {MenuApi} from "/@/api/api";

export const getMenuList = () => {
  return defHttp.get<getMenuListResultModel>({url: MenuApi.Tree});
};

export const userMenuList = () => {
  return defHttp.get<getMenuListResultModel>({url: MenuApi.Base + '/userMenu'});
}

export const userPermissions = () => {
  return defHttp.get<getMenuListResultModel>({url: MenuApi.Base + '/userPermissions'});
}

export const defaultTenantMenu = () => {
  return defHttp.get<getMenuListResultModel>({url: MenuApi.Base + '/defaultTenantMenu'});
}

export const createMenu = (params?: MenuListItem
) => {
  return defHttp.post<ApiRes>(
    {
      url: MenuApi.Base,
      params,
    }
  );
};

export const updateMenu = (id: string, params?: MenuListItem
) => {
  return defHttp.put<ApiRes>(
    {
      url: MenuApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteMenu = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: MenuApi.Base + '/' + id,
    }
  );
};
