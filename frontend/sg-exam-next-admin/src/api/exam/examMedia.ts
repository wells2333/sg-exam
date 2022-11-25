import {defHttp} from '/@/utils/http/axios';
import {UploadFileParams} from "/#/axios";
import {ExamMediaApi} from "/@/api/api";

export function uploadVideo(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: ExamMediaApi.UploadVideo,
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
      url: ExamMediaApi.UploadImage,
      onUploadProgress,
    },
    params,
  );
}
