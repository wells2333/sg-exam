import {defHttp} from '/@/utils/http/axios';
import {GetUserInfoModel, LoginParams, LoginResultModel} from './model/userModel';

import {ErrorMessageMode, UploadFileParams} from '/#/axios';

import {getEncryptionKey} from '/@/utils/env';

import {AuthService, UserService} from '/@/api/services';

import {encryption} from '/@/utils/utils';

import {UserListGetResultModel, UserParams} from './model/systemModel';

import {UploadApiResult} from './model/uploadModel';

const Api = {
  Login: AuthService + '/login',
  Logout: AuthService + '/v1/token/logout',
  Base: UserService + '/v1/user',
  GetUserInfo: UserService + '/v1/user/info',
  GetPermCode: '/getPermCode',
  UserList: UserService + '/v1/user/userList',
  UploadAvatar: UserService + '/v1/user/uploadAvatar',
  UpdateAvatar: UserService + '/v1/user/updateAvatar',
  GetTicket: UserService + '/v1/wx/getTicket',
  OpenId: UserService + '/v1/wx/getOpenId',
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  const user = encryption({
    data: params,
    key: getEncryptionKey(),
    param: ['password']
  });
  const password = user.password.replace(/\+/g, '%2B');
  return defHttp.post<LoginResultModel>(
    {
      url: Api.Login + '?grant_type=password&scope=read&ignoreCode=1&username=' + params.username + '&credential=' + password + '&remember=' + params.remember,
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
  return defHttp.get<GetUserInfoModel>({url: Api.GetUserInfo}, {errorMessageMode: 'none'});
}

export function getPermCode() {
  return defHttp.get<string[]>({url: Api.GetPermCode});
}

export function doLogout() {
  return defHttp.get({url: Api.Logout});
}

export function getUser(id: string) {
  return defHttp.get({url: Api.Base + '/' + id});
}

export const getUserList = (params: UserParams) =>
  defHttp.get<UserListGetResultModel>({ url: Api.UserList, params });

export const createUser = (params?: UserParams
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateUser = (id: string, params?: UserParams
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};


export const updateUserInfo = (params?: UserParams
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/updateInfo',
      params,
    }
  );
};

export const deleteUser = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};

export const updatePassword = (params?: UserParams) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/anonymousUser/updatePassword',
      params,
    }
  );
}

export const resetPassword = (params?: UserParams) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/anonymousUser/resetPassword',
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
      url: Api.UploadAvatar,
      onUploadProgress,
    },
    params,
  );
}

export const updateUserAvatar = (params?: UserParams) => {
  return defHttp.post<void>(
    {
      url: Api.UpdateAvatar,
      params,
    }
  );
}

export const getTicket = () => {
  return defHttp.get<string[]>({url: Api.GetTicket});
}

export const getOpenId = (params?: LoginParams) => {
  return defHttp.get<LoginResultModel>({
    url: Api.OpenId, params
  });
}
