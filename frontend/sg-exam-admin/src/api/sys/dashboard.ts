import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {DashboardApi} from "/@/api/api";

export function getDashboardData() {
  return defHttp.get<ApiRes>({url: DashboardApi.Dashboard});
}

export function getDashboardTendency(params?: object) {
  return defHttp.get<ApiRes>({url: DashboardApi.Tendency, params});
}
