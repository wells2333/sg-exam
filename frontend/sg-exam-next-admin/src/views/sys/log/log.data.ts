import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '日志标题',
    dataIndex: 'title',
    width: 180,
    align: 'left',
  },
  {
    title: 'IP',
    dataIndex: 'ip',
    width: 160,
    align: 'left',
  },
  {
    title: 'userAgent',
    dataIndex: 'userAgent',
    width: 200,
    align: 'left',
  },
  {
    title: 'requestUri',
    dataIndex: 'requestUri',
    width: 160,
    align: 'left',
  },
  {
    title: 'method',
    dataIndex: 'method',
    width: 80,
    align: 'left',
  },
  {
    title: 'took',
    dataIndex: 'took',
    width: 50,
    align: 'left',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '日志标题',
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
    field: 'title',
    label: '日志标题',
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
    field: 'IP',
    label: 'IP',
    component: 'Input',
    required: true,
  },
];
