import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: 'id',
    dataIndex: 'id',
    width: 80,
    align: 'left',
  },
  {
    title: '附件名称',
    dataIndex: 'attachName',
    width: 200,
    align: 'left',
  },
  {
    title: '附件类型',
    dataIndex: 'attachType',
    align: 'left',
  },
  {
    title: '附件大小',
    dataIndex: 'attachSize',
    width: 80,
    align: 'left',
  },
  {
    title: '附件分组',
    dataIndex: 'groupCode',
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
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'attachName',
    label: '附件名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'attachType',
    label: '附件类型',
    component: 'Input',
    colProps: { span: 4 },
  },
  {
    field: 'groupCode',
    label: '附件分组',
    component: 'Input',
    colProps: { span: 4 },
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
    field: 'tenantName',
    label: '租户名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'tenantCode',
    label: '租户标识',
    component: 'Input',
    required: true,
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '启用', value: 0 },
        { label: '停用', value: 1 },
      ],
    },
    required: true,
  },
  {
    label: '备注',
    field: 'tenantDesc',
    component: 'InputTextArea',
  },
];
