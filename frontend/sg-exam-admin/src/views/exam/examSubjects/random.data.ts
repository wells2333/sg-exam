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
    field: 'type',
    label: '组题方式',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '题目数量', value: 0 },
        { label: '考试总分', value: 1 }
      ],
    },
    required: true,
  },
  {
    field: 'target',
    label: '目标值',
    component: 'InputNumber',
    required: true,
    componentProps: {
      min: 0,
      max: 10000
    }
  }
]
