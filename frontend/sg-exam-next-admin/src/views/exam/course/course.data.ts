import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h} from "vue";
import {Image, Rate} from "ant-design-vue";
import {BasicUpload} from "/@/components/Upload";
import {uploadImage} from "/@/api/exam/examMedia";

export const columns: BasicColumn[] = [
  {
    title: '课程名称',
    dataIndex: 'courseName',
    width: 160,
    align: 'left',
  },
  {
    title: '课程图片',
    dataIndex: 'imageUrl',
    width: 100,
    align: 'left',
    style: {
      cursor: 'pointer'
    },
    customRender: ({ record }) => {
      return h(Image, { src: record.imageUrl, height: '40px', alt: record.courseName });
    },
  },
  {
    dataIndex: 'college',
    title: '学院',
    width: 100,
    align: 'left',
  },
  {
    dataIndex: 'major',
    title: '专业',
    width: 100,
    align: 'left',
  },
  {
    dataIndex: 'teacher',
    title: '老师',
    width: 100,
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

export const searchFormSchema: FormSchema[] = [
  {
    field: 'courseName',
    label: '课程名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'courseName',
    label: '课程名称',
    component: 'Input',
    required: true,
  },
  {
    label: '课程图片',
    field: 'imageId',
    component: 'Input',
    render: ({ model, field }) => {
      return h(BasicUpload, {
        value: model[field],
        maxSize: 20,
        maxNumber: 1,
        emptyHidePreview: true,
        api: uploadImage,
        onChange: (value) => {
          if (value && value.length > 0) {
            model[field] = value[0].id;
          }
        },
      });
    },
  },
  {
    field: 'college',
    label: '学院',
    component: 'Input'
  },
  {
    field: 'major',
    label: '专业',
    component: 'Input'
  },
  {
    field: 'teacher',
    label: '老师',
    component: 'Input'
  },
  {
    label: '课程描述',
    field: 'courseDescription',
    component: 'InputTextArea',
  },
];

export const evaluateColumns: BasicColumn[] = [
  {
    title: '课程',
    dataIndex: 'courseName',
    width: 160,
    align: 'left',
  },
  {
    title: '评价等级',
    dataIndex: 'evaluateLevel',
    width: 120,
    align: 'left',
    customRender: ({ record }) => {
      return h(Rate, {
        value: record.evaluateLevel,
        disabled: true,
        allowHalf: false,
      });
    },
  },
  {
    title: '评价内容',
    dataIndex: 'evaluateContent',
    width: 200,
    align: 'left',
  },
  {
    title: '评价人',
    dataIndex: 'operator',
    width: 80,
    align: 'left',
  },
  {
    title: '提交时间',
    dataIndex: 'createTime',
    width: 160,
    align: 'left',
  },
]

export const evaluateSearchFormSchema: FormSchema[] = [
  {
    field: 'evaluateContent',
    label: '评价内容',
    component: 'Input',
    colProps: { span: 8 },
  }
];
