import {BasicColumn, FormSchema} from '/@/components/Table';
import {h, unref} from "vue";
import {Tinymce} from "/@/components/Tinymce";
import {
  editorHeight,
  tinymcePlugins,
  tinymceToolbar
} from "/@/components/Subjects/subject.constant";
import {ExamMediaApi} from "/@/api/api";
import {Tag} from "ant-design-vue";
import {uploadVideo} from "/@/api/exam/examMedia";
import {SgUpload} from "/@/components/SgUpload";

export const columns: BasicColumn[] = [
  {
    title: '序号',
    dataIndex: 'sort',
    width: 50,
    align: 'left',
  },
  {
    title: '标题',
    dataIndex: 'title',
    width: 160,
    align: 'left',
  },
  {
    title: '学习时长',
    dataIndex: 'learnHour',
    width: 80,
  },
  {
    dataIndex: 'contentType',
    title: '内容类型',
    width: 100,
    align: 'left',
    customRender: ({record}) => {
      const contentType = record.contentType;
      let color = 'green';
      let text = '视频';
      if (contentType !== null && contentType === 1) {
        color = 'blue';
        text = '图文';
      }
      return h(Tag, {color: color}, () => text);
    },
  },
  {
    title: '视频名称',
    dataIndex: 'videoName',
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

export const searchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: {span: 8},
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
    field: 'content',
    label: '内容',
    component: 'Input',
    required: true,
    render: ({model, field}) => {
      return h(Tinymce, {
        value: model[field],
        height: editorHeight,
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        uploadUrl: ExamMediaApi.UploadImage,
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
  },
  {
    field: 'sort',
    label: '序号',
    component: 'InputNumber',
    required: true,
    defaultValue: 100
  },
  {
    field: 'learnHour',
    label: '学习时长',
    component: 'InputNumber',
    required: true,
    defaultValue: 1
  },
  {
    field: 'contentType',
    label: '内容类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '视频', value: 0 },
        { label: '图文', value: 1 },
      ],
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '上传视频',
    field: 'videoId',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        api: uploadVideo,
        type: 'video',
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
            model['videoName'] = unref(value).name;
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
];
