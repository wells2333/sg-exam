import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h, unref} from 'vue';
import {Image, Tag} from 'ant-design-vue';
import {SgUpload} from "/@/components/SgUpload";
import {uploadImage} from "/@/api/exam/examMedia";

export const tenantStatus = {
  0: '待审核',
  1: '审核通过',
  2: '审核不通过'
}

export const tenantColor = {
  0: 'blue',
  1: 'green',
  2: 'red'
}

export const tenantInitStatus = {
  0: '未初始化',
  1: '初始化中',
  2: '初始化成功',
  3: '初始化失败'
}

export const tenantInitColor = {
  0: 'blue',
  1: 'blue',
  2: 'green',
  3: 'red'
}

export const columns: BasicColumn[] = [
  {
    title: '单位标识',
    dataIndex: 'tenantCode',
    width: 160,
    align: 'left',
  },
  {
    title: '单位名称',
    dataIndex: 'tenantName',
    width: 160,
    align: 'left',
  },
  {
    title: '图片',
    dataIndex: 'imageUrl',
    width: 160,
    align: 'left',
    style: {
      cursor: 'pointer'
    },
    customRender: ({record}) => {
      return h(Image, {src: record.imageUrl, height: '40px', alt: record.tenantName});
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      const status = record.status;
      return h(Tag, { color: tenantColor[status] }, () => tenantStatus[status]);
    },
  },
  {
    title: '初始化状态',
    dataIndex: 'initStatus',
    width: 120,
    customRender: ({ record }) => {
      const status = record.initStatus;
      return h(Tag, { color: tenantInitColor[status] }, () => tenantInitStatus[status]);
    },
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
    dataIndex: 'tenantDesc',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'tenantName',
    label: '单位名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    componentProps: {
      options: [
        { label: tenantStatus['0'], value: 0 },
        { label: tenantStatus['1'], value: 1 },
        { label: tenantStatus['2'], value: 2 },
      ],
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Render',
    show: false,
  },
  {
    field: 'roleId',
    label: 'roleId',
    component: 'Render',
    show: false,
  },
  {
    field: 'tenantName',
    label: '单位名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'tenantCode',
    label: '单位标识',
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
        { label: tenantStatus['0'], value: 0 },
        { label: tenantStatus['1'], value: 1 },
        { label: tenantStatus['2'], value: 2 },
      ],
    },
    required: true,
  },
  {
    field: 'imageId',
    label: '图片',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        api: uploadImage,
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
          }
        },
      });
    },
    colProps: {span: 12},
  },
  {
    label: '备注',
    field: 'tenantDesc',
    component: 'InputTextArea',
  },
  {
    label: ' ',
    field: 'menuIds',
    slot: 'menu',
    component: 'Input',
  },
];
