import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ChapterApi} from "/@/api/api";
import {UploadFileParams} from "/#/axios";

export const getChapterList = (params?: object) =>
  defHttp.get<ApiRes>({url: ChapterApi.CourseList, params});

export const createChapter = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: ChapterApi.Base,
      params,
    }
  );
};

export const updateChapter = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: ChapterApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteChapter = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: ChapterApi.Base + '/' + id,
    }
  );
};

export function importSection(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: ChapterApi.Base + '/importSection',
      onUploadProgress,
    },
    params,
  );
}
