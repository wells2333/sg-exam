import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h} from "vue";
import {Tag} from "ant-design-vue";
import {subjectColor} from '/@/components/Subjects/subject.constant';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    dataIndex: 'sort',
    width: 80,
  },
  {
    title: '题目名称',
    dataIndex: 'subjectName',
    align: 'left',
  },
  {
    title: '类型',
    dataIndex: 'typeLabel',
    width: 80,
    customRender: ({ record }) => {
      const type = record.type + '';
      let color = subjectColor[type];
      return h(Tag, { color: color }, () => record.typeLabel);
    },
  },
  {
    title: '分数',
    dataIndex: 'score',
    width: 80,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 180,
  },
  {
    title: '操作人',
    dataIndex: 'operator'
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'subjectName',
    label: '题目名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];
