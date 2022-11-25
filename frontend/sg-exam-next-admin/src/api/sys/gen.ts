import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {GenApi} from "/@/api/api";

export const getGenList = (params?: object) =>
  defHttp.get<ApiRes>({url: GenApi.TenantList, params});

export const createGen = (params?: object
) => {
  return defHttp.get<ApiRes>(
    {
      url: GenApi.Base + '/importTable',
      params,
    }
  );
};

export const updateGen = (id: string, params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: GenApi.Base + '/' + id,
      params,
    }
  );
};

export const genCode = (table: string) => {
  const url = GenApi.Base + '/download/' + table;
  window.open(url);
};

export const deleteGen = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: GenApi.Base + '/' + id,
    }
  );
};

