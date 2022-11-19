import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { examColor } from "/@/views/exam/examination/examination.data";

export const submitStatus = {
  0: 'gold',
  1: 'blue',
  2: 'magenta',
  3: 'green'
}

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
    dataIndex: 'submitStatusName',
    width: 80,
    customRender: ({ record }) => {
      const color = submitStatus[record.submitStatus];
      return h(Tag, { color: color }, () => record.submitStatusName);
    },
  },
  {
    title: "考试时间",
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
