import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {AnswerApi} from "/@/api/api";

export const getAnswerList = (params?: object) =>
  defHttp.get<ApiRes>({url: AnswerApi.AnswerList, params});

export const markAnswer = (params?: object) => {
  return defHttp.put<void>(
    {
      url: AnswerApi.Base + '/mark',
      params,
    }
  );
}
