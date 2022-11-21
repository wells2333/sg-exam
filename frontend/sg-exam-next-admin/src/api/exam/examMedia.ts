import {UserService} from '/@/api/services';
import {defHttp} from '/@/utils/http/axios';
import {UploadFileParams} from "/#/axios";

export const Api = {
  UploadVideo: UserService + 'v1/examMedia/uploadVideo',
  UploadImage: UserService + 'v1/examMedia/uploadImage'
}

export function uploadVideo(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: Api.UploadVideo,
      onUploadProgress,
    },
    params,
  );
}

export function uploadImage(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: Api.UploadImage,
      onUploadProgress,
    },
    params,
  );
}
