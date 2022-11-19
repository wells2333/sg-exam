import {
  AttachGroupListItem,
  AttachGroupListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/attachGroup',
  GroupList: UserService + '/v1/attachGroup/groupList',
}

export const getAttachGroupList = (params?: AttachGroupListItem) =>
  defHttp.get<AttachGroupListGetResultModel>({url: Api.GroupList, params});

export const createAttachGroup = (params?: AttachGroupListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateAttachGroup = (id: string, params?: AttachGroupListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteAttachGroup = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};


