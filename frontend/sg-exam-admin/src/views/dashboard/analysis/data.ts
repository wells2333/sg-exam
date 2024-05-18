export interface GrowCardItem {
  icon: string;
  title: string;
  value: number;
  total: number;
  color: string;
  action: string;
}

export const growCardList: GrowCardItem[] = [
  {
    title: '单位数',
    icon: 'visit-count|svg',
    value: 0,
    total: 0,
    color: 'green',
    action: '月',
  },
  {
    title: '用户数',
    icon: 'total-sales|svg',
    value: 0,
    total: 0,
    color: 'blue',
    action: '月',
  },
  {
    title: '考试数',
    icon: 'download-count|svg',
    value: 0,
    total: 0,
    color: 'orange',
    action: '周',
  },
  {
    title: '考试次数',
    icon: 'transaction|svg',
    value: 0,
    total: 0,
    color: 'purple',
    action: '年',
  },
];

export function generateDashboardCard<GrowCardItem>(title: string, icon: string, value: number, total: number, color: string, action: string) {
  return {
    title,
    icon,
    value,
    total,
    color,
    action
  }
}
