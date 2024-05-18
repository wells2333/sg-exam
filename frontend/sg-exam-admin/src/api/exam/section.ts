import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {SectionApi} from "/@/api/api";

export const getSectionList = (params?: object) =>
  defHttp.get<ApiRes>({url: SectionApi.CourseList, params});

export const createSection = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: SectionApi.Base,
      params,
    }
  );
};

export const updateSection = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: SectionApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteSection = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: SectionApi.Base + '/' + id,
    }
  );
};
