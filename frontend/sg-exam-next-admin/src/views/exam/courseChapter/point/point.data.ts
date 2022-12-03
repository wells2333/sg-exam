import {BasicColumn, FormSchema} from '/@/components/Table';
import {h} from "vue";
import {Tinymce} from "/@/components/Tinymce";
import {
  editorHeight,
  tinymcePlugins,
  tinymceToolbar
} from "/@/components/Subjects/subject.constant";
import {ExamMediaApi} from "/@/api/api";

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
        height: 120,
        // 指定上传URL
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
  }
];
