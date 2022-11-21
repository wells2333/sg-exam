import {CourseListGetResultModel, CourseListItem,} from './model/systemModel';
import {defHttp} from '/@/utils/http/axios';
import {ExamService} from '/@/api/services';

const Api = {
  Base: ExamService + '/v1/course/',
  CourseList: ExamService + '/v1/course/courseList',
  AllCourses: ExamService + '/v1/course/allCourses'
}

export const getCourseList = (params?: CourseListItem) =>
  defHttp.get<CourseListGetResultModel>({url: Api.CourseList, params});

export const getAllCourses = (params?: CourseListItem) =>
  defHttp.get<CourseListGetResultModel>({url: Api.AllCourses, params});

export const createCourse = (params?: CourseListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateCourse = (id: string, params?: CourseListItem
) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteCourse = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};
