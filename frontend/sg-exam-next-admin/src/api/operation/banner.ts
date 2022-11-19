import {
  BannerListItem,
  BannerListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/operation/banner',
  BannerList: UserService + '/v1/operation/banner/list',
}

export const getBannerList = (params?: BannerListItem) =>
  defHttp.get<BannerListGetResultModel>({url: Api.BannerList, params});

export const createBanner = (params?: BannerListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateBanner = (id: string, params?: BannerListItem) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteBanner = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};


