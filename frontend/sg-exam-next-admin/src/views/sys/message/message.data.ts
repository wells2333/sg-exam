import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h} from "vue";
import {Tag} from "ant-design-vue";

export const messageType = {
  0: '站内信',
  1: '邮件',
  2: '待办',
  3: '公告',
}

export const messageColor = {
  0: 'blue',
  1: 'green',
  2: 'cyan',
  3: 'purple'
}

const isPartUser = (receiverType: number) => receiverType === 1;

export const columns: BasicColumn[] = [
  {
    title: '消息类型',
    dataIndex: 'type',
    width: 100,
    align: 'left',
    customRender: ({ record }) => {
      const type = record.type;
      return h(Tag, { color: messageColor[type] }, () => messageType[type]);
    },
  },
  {
    title: '消息标题',
    dataIndex: 'title',
    align: 'left',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    align: 'left',
    customRender: ({ record }) => {
      const status = record.status === 1 ? '已发布' : '草稿';
      const color = record.status === 1 ? '#87d068' : '#2db7f5';
      return h(Tag, { color: color }, () => status);
    },
  },
  {
    title: '发送人',
    dataIndex: 'sender',
    align: 'left',
  },
  {
    title: '操作人',
    dataIndex: 'operator',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
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
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '已发布', value: 1 },
      ],
    },
    required: true,
  },
  {
    field: 'type',
    label: '消息类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '站内信', value: 0 },
        { label: '邮件', value: 1 },
        { label: '待办', value: 2 },
        { label: '公告', value: 3 },
      ],
    },
    required: true,
  },
  {
    field: 'title',
    label: '消息标题',
    component: 'Input',
    required: true,
  },
  {
    field: 'content',
    label: '消息内容',
    component: 'Input',
    required: true,
  },
  {
    field: 'receiverType',
    label: '接收人类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '全部用户', value: 0 },
        { label: '部分用户', value: 1 },
      ],
    },
    required: true,
  },
  {
    field: 'receivers',
    label: '接收人',
    component: 'Input',
    slot: 'remoteSearch',
    ifShow: ({ values }) => isPartUser(values.receiverType),
  }
];
