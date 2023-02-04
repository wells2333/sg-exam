import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h, unref} from 'vue';
import {Image, Tag} from 'ant-design-vue';
import { getAllCourses } from "/@/api/exam/course";
import {DescItem} from "/@/components/Description";
import {uploadImage} from "/@/api/exam/examMedia";
import {SgUpload} from "/@/components/SgUpload";

export const examColor = {
  0: 'green',
  1: 'magenta',
  2: 'gold',
  3: 'blue'
}

export const columns: BasicColumn[] = [
  {
    title: '考试名称',
    dataIndex: 'examinationName',
    width: 160,
    align: 'left',
  },
  {
    title: '类型',
    dataIndex: 'type',
    width: 80,
    customRender: ({ record }) => {
      const color = examColor[record.type];
      return h(Tag, { color: color }, () => record.typeLabel);
    },
  },
  {
    title: '图片',
    dataIndex: 'imageUrl',
    width: 100,
    style: {
      cursor: 'pointer'
    },
    customRender: ({ record }) => {
      return h(Image, { src: record.imageUrl, height: '40px', alt: record.examinationName });
    },
  },
  {
    title: '课程',
    dataIndex: 'course.courseName',
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      const status = record.status;
      const enable = ~~status === 1;
      const color = enable ? 'green' : 'red';
      const text = enable ? '已发布' : '草稿';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '标签',
    dataIndex: 'tags',
    width: 100,
  },
  {
    title: '开始时间',
    dataIndex: 'startTime',
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
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'examinationName',
    label: '考试名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    componentProps: {
      options: [
        { label: '启用', value: 0 },
        { label: '停用', value: 1 },
      ],
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'examinationName',
    label: '考试名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'type',
    label: '类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '考试', value: 0 },
        { label: '练习', value: 1 },
        { label: '问卷', value: 2 },
        { label: '面试', value: 3 },
      ],
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'answerType',
    label: '答题模式',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '展示全部题目', value: 0 },
        { label: '顺序答题', value: 1 },
      ],
    },
    required: true,
    colProps: { span: 12 },
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
    colProps: { span: 12 },
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
    defaultValue: 100,
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'totalScore',
    label: '总分',
    component: 'InputNumber',
    defaultValue: 100,
    required: true,
    colProps: { span: 12 },
  },
  {
    label: '考试图片',
    field: 'imageId',
    component: 'Input',
    render: ({ model, field }) => {
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
    colProps: { span: 12 },
  },
  {
    field: 'examTime',
    label: '考试时间',
    component: 'RangePicker',
    componentProps: {
      'show-time': true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss'
    },
    colProps: { span: 12 },
  },
  {
    field: 'courseId',
    label: '课程',
    component: 'ApiSelect',
    componentProps: {
      api: getAllCourses,
      labelField: 'courseName',
      valueField: 'id',
    },
    colProps: { span: 12 },
  },
  {
    field: 'tags',
    label: '标签',
    component: 'Input',
    required: false,
    componentProps: {
      placeholder: '多个标签用逗号分隔',
    },
    colProps: { span: 12 },
  },
  {
    label: '注意事项',
    field: 'attention',
    component: 'InputTextArea',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];

export const examinationDetailFormSchema: DescItem[] = [
  {
    field: 'examinationName',
    label: '考试名称',
  },
  {
    field: 'typeLabel',
    label: '考试类型',
  },
  {
    field: 'course.courseName',
    label: '课程',
  },
  {
    field: 'startTime',
    label: '开始时间',
  },
  {
    field: 'endTime',
    label: '结束时间',
  },
  {
    field: 'totalScore',
    label: '总分',
  },
  {
    field: 'tags',
    label: '标签',
  },
  {
    field: 'attention',
    label: '注意事项',
  },
  {
    field: 'remark',
    label: '备注',
  }
];
