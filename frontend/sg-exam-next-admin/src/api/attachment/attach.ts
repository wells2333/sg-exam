import {
  AttachmentListItem,
  AttachmentListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import { UploadApiResult } from './model/uploadModel';
import { UploadFileParams } from '/#/axios';
import {UserService} from '/@/api/services';

export const Api = {
  Base: UserService + '/v1/attachment',
  AttachmentList: UserService + '/v1/attachment/attachmentList',
  Upload: UserService + 'v1/attachment/upload'
}

export const getAttachmentList = (params?: AttachmentListItem) =>
  defHttp.get<AttachmentListGetResultModel>({url: Api.AttachmentList, params});

export const getDownloadUrl = (id: string) =>
  defHttp.get<AttachmentListGetResultModel>({url: Api.Base + '/getDownloadUrl?id=' + id});

export const download = (id: string) =>
  defHttp.get<AttachmentListGetResultModel>({url: Api.Base + '/download?id=' + id});

export function uploadApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      url: Api.Upload,
      onUploadProgress,
    },
    params,
  );
}

export const deleteAttachment = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
