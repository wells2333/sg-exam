import {BasicColumn} from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { Tag } from 'ant-design-vue';

import { subjectColor, COLOR } from '/@/components/Subjects/subject.constant';
import { h } from 'vue';

const commonTagRender = (color: string) => (curVal) => h(Tag, { color }, () => curVal);

export const scoreDetailSchema: DescItem[] = [
  {
    field: 'examinationName',
    label: '考试名称',
  },
  {
    field: 'typeLabel',
    label: '类型',
    render: commonTagRender(COLOR.ZERO),
  },
  {
    field: 'totalScore',
    label: '试卷总分',
  },
  {
    field: 'userName',
    label: '考生',
  },
  {
    field: 'deptName',
    label: '部门',
  },
  {
    field: 'score',
    label: '考生得分',
  },
  {
    field: 'startTime',
    label: '开始时间',
  },
  {
    field: 'duration',
    label: '耗时',
  },
  {
    field: 'correctNumber',
    label: '正确题目数',
  },
  {
    field: 'inCorrectNumber',
    label: '错误题目数',
  },
  {
    field: 'submitStatusName',
    label: '状态',
    render: commonTagRender(COLOR.ZERO),
  },
];

export const answerColumns: BasicColumn[] = [
  {
    title: '序号',
    dataIndex: 'subject.sort',
    align: 'left',
    width: 50,
  },
  {
    title: '题目名称',
    dataIndex: 'subject.subjectName',
    align: 'left',
  },
  {
    dataIndex: 'subject.typeLabel',
    title: '类型',
    width: 80,
    align: 'left',
    customRender: ({ record }) => {
      const { subject } = record;
      if (subject) {
        let color = subjectColor[subject.type];
        return h(Tag, { color: color }, () => subject.typeLabel);
      }
      return '';
    },
  },
  {
    dataIndex: 'answer',
    title: '考生答案',
    width: 80,
    align: 'left',
  },
  {
    dataIndex: 'subject.answer.answer',
    title: '参考答案',
    width: 80,
    align: 'left',
  },
  {
    dataIndex: 'markStatus',
    title: '状态',
    width: 80,
    align: 'left',
    customRender:({ record }) => {
      let color = 'green';
      let text = '已批改';
      if (record.markStatus === 0) {
        color = 'red';
        text = '待批改';
      }
      return h(Tag, { color:  color}, () => text);
    }
  },
  {
    dataIndex: 'markOperator',
    title: '批改人',
    width: 80,
    align: 'left',
  },
  {
    dataIndex: 'answerType',
    title: '批改结果',
    width: 80,
    align: 'left',
    customRender:({ record }) => {
      let color = 'green';
      let text = '正确';
      if (record.answerType === 1) {
        color = 'red';
        text = '错误';
      }
      return h(Tag, { color:  color}, () => text);
    }
  },
  {
    title: '开始时间',
    dataIndex: 'startTime',
    width: 180,
  },
  {
    dataIndex: 'duration',
    title: '耗时',
    width: 100,
    align: 'left',
  },
  {
    title: '得分',
    dataIndex: 'score',
    width: 100,
  },
];
