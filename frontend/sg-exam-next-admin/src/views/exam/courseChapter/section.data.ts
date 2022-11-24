import {BasicColumn, FormSchema} from '/@/components/Table';
import {h} from "vue";
import {BasicUpload} from "/@/components/Upload";
import {uploadVideo} from "/@/api/exam/examMedia";

export const searchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const columns: BasicColumn[] = [
  {
    title: '标题',
    dataIndex: 'title',
    width: 300,
    align: 'left',
  },
  {
    dataIndex: 'learnHour',
    title: '学习时长',
    width: 80,
    align: 'left',
  },
  {
    dataIndex: 'sort',
    title: '序号',
    width: 80,
    align: 'left',
  },
  {
    dataIndex: 'videoName',
    title: '视频名称',
    width: 200,
    align: 'left',
  },
  {
    dataIndex: 'sectionDesc',
    title: '描述',
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
    width: 100,
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    required: true,
  },
  {
    field: 'learnHour',
    label: '学习时长',
    component: 'InputNumber',
    defaultValue: 1,
    required: true,
  },
  {
    field: 'sort',
    label: '序号',
    component: 'InputNumber',
    defaultValue: 100,
    required: true,
  },
  {
    label: '上传视频',
    field: 'videoId',
    component: 'Input',
    render: ({ model, field }) => {
      return h(BasicUpload, {
        value: model[field],
        maxSize: 20,
        maxNumber: 1,
        emptyHidePreview: true,
        api: uploadVideo,
        onChange: (value) => {
          if (value && value.length > 0) {
            model[field] = value[0].id;
            model['videoName'] = value[0].attachName;
          }
        },
      });
    },
    colProps: { span: 12 },
  },
  {
    field: 'videoName',
    label: '视频名称',
    component: 'Input'
  },
  {
    field: 'sectionDesc',
    label: '描述',
    component: 'InputTextArea'
  },
];
