import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {SubjectCategoryApi} from "/@/api/api";

export const getSubjectCategoryTree = (params?: object) =>
  defHttp.get<ApiRes>({url: SubjectCategoryApi.SubjectCategoryTree, params});

export const createCategory = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: SubjectCategoryApi.Base,
      params,
    }
  );
};

export const updateCategory = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: SubjectCategoryApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteCategory = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: SubjectCategoryApi.Base + '/' + id,
    }
  );
};
