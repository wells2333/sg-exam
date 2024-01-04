import {BasicColumn} from "/@/components/Table";

export const rankColumns: BasicColumn[] = [
  {
    title: '排名',
    dataIndex: 'rankNum',
    width: 30,
    align: 'left',
  },
  {
    title: '姓名',
    dataIndex: 'userName',
    width: 30,
    align: 'left',
  },
  {
    title: '总分',
    dataIndex: 'score',
    width: 30,
    align: 'left',
  },
]
