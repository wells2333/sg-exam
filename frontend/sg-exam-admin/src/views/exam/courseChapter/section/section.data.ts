import {BasicColumn, FormSchema} from '/@/components/Table';
import {h, unref} from "vue";
import {Tag} from "ant-design-vue";
import {Tinymce} from "/@/components/Tinymce";
import {
  tinymcePlugins,
  tinymceToolbar
} from "/@/components/Subjects/subject.constant";
import {ExamMediaApi} from "/@/api/api";
import {SgUpload} from "/@/components/SgUpload";

export const searchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: {span: 8},
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
      if (contentType !== null && contentType === 2) {
        color = 'magenta';
        text = 'PDF';
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
        {label: '视频', value: 0},
        {label: '图文', value: 1},
      ],
    },
    required: true,
    colProps: {span: 12},
  },
  {
    field: 'content',
    label: '内容',
    component: 'Input',
    render: ({model, field}) => {
      return h(Tinymce, {
        value: model[field],
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        height: 300,
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
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        groupCode: 'exam/video',
        type: 'video',
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
            model['videoName'] = unref(value).name;
          }
        },
      });
    },
    colProps: {span: 12},
  },
  {
    field: 'videoUrl',
    label: '视频 URL',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'videoName',
    label: '视频名称',
    component: 'Input'
  },
  {
    label: '上传音频',
    field: 'speechId',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        type: 'video',
        accept: '.mp3',
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
            model['videoName'] = unref(value).name;
          }
        },
      });
    },
    colProps: {span: 12},
  },
  {
    field: 'speechUrl',
    label: '音频 URL',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'sectionDesc',
    label: '描述',
    component: 'InputTextArea'
  },
];
