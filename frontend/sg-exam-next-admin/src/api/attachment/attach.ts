import {defHttp} from '/@/utils/http/axios';
import {UploadFileParams} from '/#/axios';
import {ApiRes} from "/@/api/constant";
import {AttachmentApi} from "/@/api/api";

// 5min 超时
const uploadTimeoutSeconds = 500 * 1000;

export const getAttachmentList = (params?: object) =>
  defHttp.get<ApiRes>({url: AttachmentApi.AttachmentList, params});

export const getDownloadUrl = (id: string) =>
  defHttp.get<ApiRes>({url: AttachmentApi.Base + '/getDownloadUrl?id=' + id});

export const download = (id: string) =>
  defHttp.get<ApiRes>({url: AttachmentApi.Base + '/download?id=' + id});

export function uploadApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<ApiRes>(
    {
      url: AttachmentApi.Upload,
      onUploadProgress,
      timeout: uploadTimeoutSeconds
    },
    params,
  );
}

export const deleteAttachment = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: AttachmentApi.Base + '/' + id,
    }
  );
};

export const getPreviewUrl = (id: string) => {
  return defHttp.get<ApiRes>(
    {
      url: AttachmentApi.Base + '/' + id + '/getPreviewUrl',
    }
  );
};

export const getAttachment = (id: string) => {
  return defHttp.get<ApiRes>(
    {
      url: AttachmentApi.Base + '/' + id,
    }
  );
};

export const prepareUploadChunks = (groupCode: string, data: object) => {
  return defHttp.post<ApiRes>(
    {
      url: AttachmentApi.Base + '/prepareUploadChunks?groupCode=' + groupCode,
      data
    }
  );
};

export function uploadChunk(
  params: UploadFileParams,
  hash: string,
  index: number,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<ApiRes>(
    {
      url: AttachmentApi.UploadChunk + '?hash=' + hash + '&index=' + index,
      onUploadProgress,
      timeout: uploadTimeoutSeconds
    },
    params,
  );
}

export const mergeChunks = (hash: string) => {
  return defHttp.post<ApiRes>(
    {
      url: AttachmentApi.Base + '/mergeChunks?hash=' + hash,
      timeout: uploadTimeoutSeconds
    }
  );
}
