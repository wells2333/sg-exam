import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import {Image, Tag} from 'ant-design-vue';
import { examColor } from "/@/views/exam/examination/examination.data";

export const submitStatus = {
  0: 'gold',
  1: 'blue',
  2: 'magenta',
  3: 'green'
}
export const submitStatusText = {
  0: '待批改',
  1: '待批改',
  2: '已批改',
  3: '统计完成'
}
export const examColumns: BasicColumn[] = [
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
    title: '开始时间',
    dataIndex: 'startTime',
    width: 180,
    customRender: ({record}) => {
      const startTime = record.startTime;
      let text = startTime;
      if (startTime == null || startTime == "") {
        text = '无限制';
      }
      return h(Tag,() => text);
    },
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
]

export const columns: BasicColumn[] = [
  {
    title: '考试名称',
    dataIndex: 'examinationName',
    align: 'left',
  },
  {
    title: "类型",
    dataIndex: 'type',
    width: 120,
    customRender: ({ record }) => {
      const color = examColor[record.type];
      return h(Tag, { color: color }, () => record.typeLabel);
    },
  },
  {
    title: '姓名',
    dataIndex: 'userName',
    width: 120,
  },
  {
    title: '部门',
    dataIndex: 'deptName',
    width: 120,
  },
  {
    title: "成绩",
    dataIndex: 'score',
    width: 80,
  },
  {
    title: "状态",
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      const color = submitStatus[record.submitStatus];
      return h(Tag, { color: color }, () => submitStatusText[record.submitStatus]);
    },
  },
  {
    title: "考试时间",
    dataIndex: 'startTime',
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
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'examinationName',
    label: '考试名称',
    component: 'Input',
    required: true,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
