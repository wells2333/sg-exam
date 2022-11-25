import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {CourseApi} from "/@/api/api";

export const getCourseList = (params?: object) =>
  defHttp.get<ApiRes>({url: CourseApi.CourseList, params});

export const getAllCourses = (params?: object) =>
  defHttp.get<ApiRes>({url: CourseApi.AllCourses, params});

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

export const deleteCourse = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: CourseApi.Base + '/' + id,
    }
  );
};
