import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import SgAudioPlayer from '/@/components/SgAudioPlayer';
import {h} from "vue";

export const columns: BasicColumn[] = [
  {
    title: '语音名称',
    dataIndex: 'name',
    width: 160,
    align: 'left',
  },
  {
    title: '语音文本',
    dataIndex: 'text',
    width: 160,
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
  },
  {
    title: '播放',
    dataIndex: 'url',
    width: 160,
    align: 'left',
    customRender: ({record}) => {
      return h(SgAudioPlayer, {
        option: {
          src: record.url
        }
      });
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '语音名称',
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
    field: 'name',
    label: '语音名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'text',
    label: '语音文本',
    component: 'Input',
    required: true,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  }
];
