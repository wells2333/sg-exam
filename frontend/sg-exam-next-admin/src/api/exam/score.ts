import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {ExamRecordApi} from "/@/api/api";

export const getScoreList = (params?: object) =>
  defHttp.get<ApiRes>({url: ExamRecordApi.ScoreList, params});

export const getScoreDetail = (id: string) => {
  return defHttp.get<void>(
    {
      url: ExamRecordApi.Base + '/' + id + '/details',
    }
  );
};
