import {defHttp} from '/@/utils/http/axios';
import {GetUserInfoModel, LoginParams, LoginResultModel} from './model/userModel';
import {ErrorMessageMode, UploadFileParams} from '/#/axios';
import {getEncryptionKey} from '/@/utils/env';
import {encryption} from '/@/utils/utils';
import {UploadApiResult} from './model/uploadModel';
import {ApiRes} from "/@/api/constant";
import {UserApi} from "/@/api/api";

export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  const user = encryption({
    data: params,
    key: getEncryptionKey(),
    param: ['password']
  });
  const password = user.password.replace(/\+/g, '%2B');
  return defHttp.post<LoginResultModel>(
    {
      url: UserApi.Login + '?grant_type=password&scope=read&ignoreCode=1&username=' + params.username + '&credential=' + password + '&remember=' + params.remember,
      headers: {
        'Tenant-Code': params.tenantCode
      },
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({url: UserApi.GetUserInfo}, {errorMessageMode: 'none'});
}

export function getPermCode() {
  return defHttp.get<string[]>({url: UserApi.GetPermCode});
}

export function doLogout() {
  return defHttp.get({url: UserApi.Logout});
}

export function getUser(id: string) {
  return defHttp.get({url: UserApi.Base + '/' + id});
}

export const getUserList = (params: object) =>
  defHttp.get<ApiRes>({url: UserApi.UserList, params});

export const createUser = (params?: object
) => {
  return defHttp.post<void>(
    {
      url: UserApi.Base,
      params,
    }
  );
};

export const updateUser = (id: string, params?: object
) => {
  return defHttp.put<void>(
    {
      url: UserApi.Base + '/' + id,
      params,
    }
  );
};


export const updateUserInfo = (params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: UserApi.Base + '/updateInfo',
      params,
    }
  );
};

export const deleteUser = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: UserApi.Base + '/' + id,
    }
  );
};

export const updatePassword = (params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: UserApi.Base + '/anonymousUser/updatePassword',
      params,
    }
  );
}

export const resetPassword = (params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: UserApi.Base + '/anonymousUser/resetPassword',
      params,
    }
  );
}

export function uploadUserAvatar(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      url: UserApi.UploadAvatar,
      onUploadProgress,
    },
    params,
  );
}

export const updateUserAvatar = (params?: object) => {
  return defHttp.post<ApiRes>(
    {
      url: UserApi.UpdateAvatar,
      params,
    }
  );
}

export const getTicket = () => {
  return defHttp.get<string[]>({url: UserApi.GetTicket});
}

export const getOpenId = (params?: LoginParams) => {
  return defHttp.get<LoginResultModel>({
    url: UserApi.OpenId, params
  });
}
