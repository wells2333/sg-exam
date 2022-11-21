import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '表名称',
    dataIndex: 'tableName',
    width: 160,
    align: 'left',
  },
  {
    title: '实体类名称',
    dataIndex: 'className',
    width: 160,
    align: 'left',
  },
  {
    title: '包路径',
    dataIndex: 'packageName',
    width: 200,
  },
  {
    title: '模块名称',
    dataIndex: 'moduleName',
    width: 120,
  },
  {
    title: '业务名',
    dataIndex: 'businessName',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'tableName',
    label: '表名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'table',
    label: '表名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'comment',
    label: '表注释',
    component: 'Input',
    required: true,
  },
  {
    field: 'tenantCode',
    label: '租户标识',
    component: 'Input',
    required: true,
  },
];
