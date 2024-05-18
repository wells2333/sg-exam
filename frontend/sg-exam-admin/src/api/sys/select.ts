import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {SelectApi} from "/@/api/api";

export const getSelectUserList = (params?: object) =>
  defHttp.get<ApiRes>({url: SelectApi.UserList, params});

export const getSelectDeptList = (params?: object) =>
  defHttp.get<ApiRes>({url: SelectApi.DeptList, params});

