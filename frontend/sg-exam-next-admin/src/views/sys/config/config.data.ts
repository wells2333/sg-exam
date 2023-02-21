import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '配置名称',
    dataIndex: 'configKey',
  }, {
    title: '配置值',
    dataIndex: 'configValue',
  }, {
    title: '描述',
    dataIndex: 'configDesc',
  }, {
    title: '更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作人',
    dataIndex: 'operator',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'configKey',
    label: '配置名称',
    component: 'Input',
    colProps: {span: 8},
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'configKey',
    label: '配置名称',
    component: 'Input',
    required: true,
  }, {
    field: 'configValue',
    label: '配置值',
    component: 'Input',
    required: true,
  }, {
    field: 'configDesc',
    label: '描述',
    component: 'Input',
    required: true,
  }
];
