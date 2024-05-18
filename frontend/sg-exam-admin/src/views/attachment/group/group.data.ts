import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h} from "vue";
import {Tag} from "ant-design-vue";

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
    title: '存储类型',
    dataIndex: 'storageType',
    width: 120,
    align: 'left',
    customRender: ({record}) => {
      const storageType = record.storageType;
      let color = 'green';
      let text = 'Minio';
      if (storageType !== null && storageType === 1) {
        color = 'blue';
        text = '七牛云';
      }
      return h(Tag, {color}, () => text);
    },
  },
  {
    title: 'url 过期时间',
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
    field: 'storageType',
    label: '存储类型',
    required: true,
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: [
        { label: '七牛云', value: 1 },
        { label: 'Minio', value: 2 },
      ],
    },
  },
  {
    field: 'urlExpire',
    label: 'url 过期时间',
    component: 'Input',
    required: true,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
