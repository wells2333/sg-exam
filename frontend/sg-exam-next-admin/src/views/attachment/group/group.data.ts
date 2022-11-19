import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: 'id',
    dataIndex: 'id',
    width: 120,
    align: 'left',
  },
  {
    title: '分组名称',
    dataIndex: 'groupName',
    width: 120,
    align: 'left',
  },
  {
    title: '分组标识',
    dataIndex: 'groupCode',
    width: 120,
    align: 'left',
  },
  {
    title: 'url过期时间',
    dataIndex: 'urlExpire',
    width: 120,
    align: 'left',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作人',
    dataIndex: 'operator',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'groupName',
    label: '分组名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Render',
    show: false,
  },
  {
    field: 'groupName',
    label: '分组名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'groupCode',
    label: '分组标识',
    component: 'Input',
    required: true,
  },
  {
    field: 'urlExpire',
    label: 'url过期时间',
    component: 'Input',
    required: true,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
