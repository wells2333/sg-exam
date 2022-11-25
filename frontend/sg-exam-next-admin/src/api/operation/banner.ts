import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {OperationApi} from "/@/api/api";

export const getBannerList = (params?: object) =>
  defHttp.get<ApiRes>({url: OperationApi.BannerList, params});

export const createBanner = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: OperationApi.Base,
      params,
    }
  );
};

export const updateBanner = (id: string, params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: OperationApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteBanner = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: OperationApi.Base + '/' + id,
    }
  );
};


