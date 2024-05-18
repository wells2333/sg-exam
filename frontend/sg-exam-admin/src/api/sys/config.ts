import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {SysConfigApi} from "/@/api/api";

export const getSysConfigList = (params?: object) =>
  defHttp.get<ApiRes>({url: SysConfigApi.ConfigList, params});

export const getSysDefaultConfig = () =>
  defHttp.get<ApiRes>({url: SysConfigApi.Base + '/getDefaultSysConfig'});

export const createSysConfig = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: SysConfigApi.Base,
      params,
    }
  );
};

export const updateSysConfig = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: SysConfigApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteSysConfig = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: SysConfigApi.Base + '/' + id,
    }
  );
};
