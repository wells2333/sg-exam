import {UserService} from '/@/api/services';
import {defHttp} from '/@/utils/http/axios';
import {UploadFileParams} from "/#/axios";

export const Api = {
  UploadVideo: UserService + 'v1/subjectMedia/uploadVideo'
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
