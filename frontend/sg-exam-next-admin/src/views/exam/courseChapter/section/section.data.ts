import {BasicColumn, FormSchema} from '/@/components/Table';
import {h} from "vue";
import {BasicUpload} from "/@/components/Upload";
import {uploadVideo} from "/@/api/exam/examMedia";
import {Tag} from "ant-design-vue";
import {Tinymce} from "/@/components/Tinymce";
import {
  editorHeight,
  tinymcePlugins,
  tinymceToolbar
} from "/@/components/Subjects/subject.constant";
import {ExamMediaApi} from "/@/api/api";

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
    dataIndex: 'sort',
    title: '序号',
    width: 50,
    align: 'left',
  },
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
    dataIndex: 'videoName',
    title: '视频名称',
    width: 200,
    align: 'left',
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
    field: 'content',
    label: '内容',
    component: 'Input',
    render: ({model, field}) => {
      return h(Tinymce, {
        value: model[field],
        height: editorHeight,
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        height: 300,
        // 指定上传URL
        uploadUrl: ExamMediaApi.UploadImage,
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
    colProps: {
      span: 24
    }
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
