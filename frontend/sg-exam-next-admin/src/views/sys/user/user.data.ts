import { getAllRoleList } from '/@/api/sys/role';
import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import {h} from "vue";
import {Image} from "ant-design-vue";

export const columns: BasicColumn[] = [
  {
    title: '账号',
    dataIndex: 'identifier',
    width: 180,
  },
  {
    title: '姓名',
    dataIndex: 'name',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    width: 100,
    style: {
      cursor: 'pointer'
    },
    customRender: ({ record }) => {
      return h(Image, { src: record.avatarUrl, height: '40px', alt: record.name });
    },
  },
  {
    title: '部门',
    dataIndex: 'deptName',
    width: 100,
  },
  {
    title: '角色',
    dataIndex: 'roleNames',
    width: 100,
  },
  {
    title: '登录时间',
    dataIndex: 'loginTime',
    width: 180,
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '姓名',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const retrieveDetailFormSchema: DescItem[] = [
  {
    field: 'identifier',
    label: '账号',
  },
  {
    field: 'name',
    label: '姓名',
  },
  {
    field: 'roleNames',
    label: '角色',
  },
  {
    field: 'deptName',
    label: '部门',
  },
  {
    field: 'genderName',
    label: '性别',
  },
  {
    field: 'email',
    label: '邮箱',
  },
  {
    field: 'phone',
    label: '手机号',
  },
  {
    field: 'loginTime',
    label: '最后登录时间',
  },
  {
    field: 'createTime',
    label: '创建时间',
  },
  {
    field: 'updateTime',
    label: '更新时间',
  },
  {
    field: 'userDesc',
    label: '备注',
  },
];

export const userFormSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Render',
    show: false,
  },
  {
    field: 'identifier',
    label: '账号',
    component: 'Input',
    rules: [
      {
        required: true,
        message: '请输入账号',
      }
    ],
  },
  {
    field: 'name',
    label: '姓名',
    component: 'Input',
    required: true
  },
  {
    label: '角色',
    field: 'role',
    component: 'ApiSelect',
    componentProps: {
      api: getAllRoleList,
      labelField: 'roleName',
      valueField: 'id',
    },
    required: true,
  },
  {
    field: 'deptId',
    label: '所属部门',
    component: 'TreeSelect',
    componentProps: {
      replaceFields: {
        label: 'deptName',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: true,
  },
  {
    field: 'gender',
    label: '性别',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '男', value: 0 },
        { label: '女', value: 1 },
      ],
    },
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
    label: '电话',
    field: 'phone',
    component: 'Input'
  },
  {
    label: '邮箱',
    field: 'email',
    component: 'Input'
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
