import {FormSchema} from "/@/components/Form";

export const formSchema: FormSchema[] = [
  {
    field: 'categoryId',
    label: '题目分类',
    component: 'Input',
    required: true,
    show: false,
  },
  {
    field: 'subjectCount',
    label: '目标题目数量',
    component: 'InputNumber',
    required: true,
  }
]
