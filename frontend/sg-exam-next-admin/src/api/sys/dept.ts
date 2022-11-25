import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {DeptApi} from "/@/api/api";

export const getDeptList = (params?: object) =>
  defHttp.get<ApiRes>({url: DeptApi.DeptList, params});

export const createDept = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: DeptApi.Base,
      params,
    }
  );
};

export const updateDept = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: DeptApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteDept = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: DeptApi.Base + '/' + id,
    }
  );
};
