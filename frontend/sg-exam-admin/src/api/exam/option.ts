import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ExamOptionApi} from "/@/api/api";

export const getDefaultOptionList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExamOptionApi.OptionList, params});
