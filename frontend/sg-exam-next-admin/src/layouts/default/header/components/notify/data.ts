export interface ListItem {
  id: string;
  avatar: string;
  title: string;
  content: string;
  titleDelete?: boolean;
  datetime: string;
  updateTime: string;
  type: string;
  read?: boolean;
  hasRead?: boolean;
  description: string;
  clickClose?: boolean;
  extra?: string;
  color?: string;
}

export interface TabItem {
  key: string;
  name: string;
  list: ListItem[];
  unreadlist?: ListItem[];
}

export const tabListData: TabItem[] = [
  {
    key: '1',
    name: '通知',
    list: [],
  },
  {
    key: '3',
    name: '待办',
    list: [],
  },
];
