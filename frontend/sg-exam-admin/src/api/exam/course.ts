import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {CourseApi} from "/@/api/api";
import {UploadFileParams} from "/#/axios";

export const getCourseList = (params?: object) =>
  defHttp.get<ApiRes>({url: CourseApi.CourseList, params});

export const getAllCourses = (params?: object) =>
  defHttp.get<ApiRes>({url: CourseApi.AllCourses, params});

export const getCourseMembers = (id?: object) =>
  defHttp.get<ApiRes>({url: CourseApi.Base + '/' + id + '/getMembers'});

export const createCourse = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: CourseApi.Base,
      params,
    }
  );
};

export const updateCourse = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: CourseApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteBatchCourse = (obj) => {
  return defHttp.post<ApiRes>(
    {
      url: CourseApi.Base + '/deleteAll',
      data: obj
    }
  );
};

export function importChapter(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile(
    {
      url: CourseApi.Base + '/importChapter',
      onUploadProgress,
    },
    params,
  );
}
