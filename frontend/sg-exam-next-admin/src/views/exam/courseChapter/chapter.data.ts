import {BasicColumn, FormSchema} from '/@/components/Table';

export const searchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const columns: BasicColumn[] = [
  {
    title: '标题',
    dataIndex: 'title',
    width: 300,
    align: 'left',
  },
  {
    dataIndex: 'sort',
    title: '序号',
    width: 60,
    align: 'left',
  },
  {
    dataIndex: 'chapterDesc',
    title: '描述',
    width: 120,
    align: 'left',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 180,
  },
  {
    title: '操作人',
    dataIndex: 'operator',
    width: 100,
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    required: true,
  },
  {
    field: 'sort',
    label: '序号',
    component: 'InputNumber',
    defaultValue: 100,
    required: true,
  },
  {
    field: 'chapterDesc',
    label: '描述',
    component: 'InputTextArea'
  },
];
