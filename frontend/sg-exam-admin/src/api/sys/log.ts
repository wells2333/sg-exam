import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {LogApi} from "/@/api/api";

export const getLogList = (params?: object) =>
  defHttp.get<ApiRes>({url: LogApi.LogList, params});

export const deleteLog = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: LogApi.Base + '/' + id,
    }
  );
};


