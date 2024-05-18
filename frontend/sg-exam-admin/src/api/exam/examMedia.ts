import {defHttp} from '/@/utils/http/axios';
import {UploadFileParams} from "/#/axios";
import {ExamMediaApi} from "/@/api/api";

// 5min 超时
const uploadTimeoutSeconds = 500 * 1000;

export function uploadSpeech(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: ExamMediaApi.UploadSpeech,
      onUploadProgress,
      timeout: uploadTimeoutSeconds
    },
    params,
  );
}

export function uploadVideo(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: ExamMediaApi.UploadVideo,
      onUploadProgress,
      timeout: uploadTimeoutSeconds
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
      timeout: uploadTimeoutSeconds
    },
    params,
  );
}
