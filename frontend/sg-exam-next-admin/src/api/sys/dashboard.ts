import {defHttp} from '/@/utils/http/axios';

import {
  DashboardParams,
  DashboardGetResultModel
} from './model/systemModel';

import {UserService} from '/@/api/services';

const Api = {
  Dashboard: UserService + '/v1/dashboard',
  Tendency: UserService + '/v1/dashboard/examRecordTendency'
}

export function getDashboardData() {
  return defHttp.get<DashboardGetResultModel>({url: Api.Dashboard});
}

export function getDashboardTendency(params?: DashboardParams) {
  return defHttp.get<DashboardGetResultModel>({url: Api.Tendency, params});
}
