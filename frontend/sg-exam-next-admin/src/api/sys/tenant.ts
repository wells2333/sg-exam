import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {TenantApi} from "/@/api/api";

export const getTenantList = (params?: object) =>
  defHttp.get<ApiRes>({url: TenantApi.TenantList, params});

export const createTenant = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: TenantApi.Base,
      params,
    }
  );
};

export const updateTenant = (id: string, params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: TenantApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteTenant = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: TenantApi.Base + '/' + id,
    }
  );
};


