import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { Icon } from '/@/components/Icon';

export const columns: BasicColumn[] = [
  {
    title: '菜单名称',
    dataIndex: 'name',
    width: 200,
    align: 'left',
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: 50,
    customRender: ({ record }) => {
      return h(Icon, { icon: record.icon });
    },
  },
  {
    title: '路由地址',
    dataIndex: 'path',
  },
  {
    title: '组件路径',
    dataIndex: 'component',
  },
  {
    title: '权限标识',
    dataIndex: 'permission',
    width: 180,
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: 50,
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
  },
];

const isMenu = (type: number) => type === 0;
const isButton = (type: number) => type === 1;

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '菜单名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'type',
    label: '菜单类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '菜单', value: 0 },
        { label: '按钮', value: 1 },
      ],
    },
    colProps: { lg: 24, md: 24 },
  },
  {
    field: 'name',
    label: '菜单名称',
    component: 'Input',
    required: true,
  },

  {
    field: 'parentId',
    label: '上级菜单',
    component: 'TreeSelect',
    componentProps: {
      replaceFields: {
        label: 'name',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'icon',
    label: '图标',
    component: 'IconPicker',
    ifShow: ({ values }) => !isButton(values.type),
  },

  {
    field: 'path',
    label: '路由地址',
    component: 'Input',
    required: true,
    ifShow: ({ values }) => !isButton(values.type),
  },
  {
    field: 'component',
    label: '组件路径',
    component: 'Input',
    ifShow: ({ values }) => isMenu(values.type),
  },
  {
    field: 'redirect',
    label: '重定向',
    component: 'Input',
    ifShow: ({ values }) => isMenu(values.type),
  },
  {
    field: 'permission',
    label: '权限标识',
    component: 'Input',
    ifShow: ({ values }) => !isButton(values.type),
  },
  {
    field: 'currentActiveMenu',
    label: '当前tag',
    component: 'Input',
    ifShow: ({ values }) => isMenu(values.type),
  },
  {
    field: 'isExt',
    label: '是否外链',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 },
      ],
    },
    ifShow: ({ values }) => !isButton(values.type),
  },
  {
    field: 'keepalive',
    label: '是否缓存',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 },
      ],
    },
    ifShow: ({ values }) => isMenu(values.type),
  },

  {
    field: 'hideMenu',
    label: '是否隐藏',
    component: 'RadioButtonGroup',
    defaultValue: false,
    componentProps: {
      options: [
        { label: '否', value: false },
        { label: '是', value: true }
      ],
    },
    ifShow: ({ values }) => !isButton(values.type),
  },
];
