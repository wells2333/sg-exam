import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import {h} from "vue";
import {Tag} from "ant-design-vue";
import { Tinymce } from '/@/components/Tinymce';
import { BasicUpload } from '/@/components/Upload';
import {uploadVideo} from '/@/api/exam/examMedia';

const editorHeight = 120;

const videoTypes = ['mp4'];

export interface TabItem {
  key: string;
  name: string;
  component: string;
}

const tinymcePlugins = [
  'advlist anchor autolink autosave code codesample  directionality  fullscreen hr insertdatetime link lists media nonbreaking noneditable pagebreak paste preview save searchreplace tabfocus  template  textpattern visualblocks visualchars',
];

const tinymceToolbar = [
  'fontsizeselect bold italic underline strikethrough blockquote subscript superscript link  preview insertdatetime forecolor backcolor fullscreen'
];

export const subjectType: any = {
  SubjectChoices: 0,
  SubjectShortAnswer: 1,
  SubjectJudgement: 2,
  SubjectMultiChoices: 3,
  SubjectSpeech: 4,
  SubjectVideo: 5
}

export const subjectTypeList: TabItem[] = [
  {
    key: subjectType.SubjectChoices,
    name: '单选题',
    component: 'SubjectChoices',
  },
  {
    key: subjectType.SubjectShortAnswer,
    name: '简答题',
    component: 'SubjectShortAnswer',
  },
  {
    key: subjectType.SubjectJudgement,
    name: '判断题',
    component: 'SubjectJudgement',
  },
  {
    key: subjectType.SubjectMultiChoices,
    name: '多选题',
    component: 'SubjectChoices',
  },
  {
    key: subjectType.SubjectSpeech,
    name: '语音题',
    component: 'SubjectSpeech',
  },
  {
    key: subjectType.SubjectVideo,
    name: '视频题',
    component: 'SubjectVideo',
  }
];

export const COLOR = {
  ZERO: '#87d068',
  ONE: '#2db7f5',
  TWO: '#108ee9',
  THREE: '#f50',
  FOUR: '#531dab',
  FIVE: '#c41d7f'
}
export const subjectColor:any = {
  '0': COLOR.ZERO,
  '1': COLOR.ONE,
  '2': COLOR.TWO,
  '3': COLOR.THREE,
  '4': COLOR.FOUR,
  '5': COLOR.FIVE
}

export const speechSubjectNameSchemas: FormSchema[] = [
  {
    field: 'subjectName',
    label: '题目名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 20
    }
  },
  {
    field: 'speechUrl',
    component: 'Input',
    labelWidth: '16px',
    label: ' ',
    colProps: {
      span: 4,
    },
    slot: 'play'
  }
]

export const simpleSubjectNameSchemas: FormSchema[] = [
  {
    field: 'subjectName',
    label: '题目名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 24
    }
  }
]

export const subjectNameSchemas: FormSchema[] = [
  {
    field: 'subjectName',
    label: '题目名称',
    component: 'Input',
    required: true,
    render: ({ model, field }) => {
      return h(Tinymce, {
        value: model[field],
        height: editorHeight,
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
    colProps: {
      span: 24
    }
  }
]

export const basicSchemas: FormSchema[] = [
  {
    field: 'level',
    label: '难度等级',
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: [
        { label: '简单', value: 0 },
        { label: '一般', value: 1 },
        { label: '较难', value: 2 },
        { label: '非常难', value: 3 },
      ],
    },
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'score',
    label: '分值',
    component: 'InputNumber',
    defaultValue: 5,
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'sort',
    label: '序号',
    component: 'InputNumber',
    defaultValue: 1,
    required: true,
    colProps: {
      span: 12,
    },
  }
];

export const judgeTypeSchemas: FormSchema[] = [
  {
    field: 'judgeType',
    label: '判分类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '自动判分', value: 0 },
        { label: '人工判分', value: 1 },
      ],
    },
    required: true,
    colProps: {
      span: 12,
    },
  },
];

export const optionDividerSchemas: FormSchema[] = [
  {
    field: 'divider-options',
    component: 'Divider',
    label: '选项列表',
  }
]

export const answerSchemas: FormSchema[] = [
  {
    label: '答案解析',
    field: 'analysis',
    component: 'InputTextArea',
    colProps: {
      span: 24
    }
  }
];

// 视频上传
export const uploadVideoSchemas: FormSchema[] = [
  {
    label: '上传视频',
    field: 'videoId',
    component: 'Input',
    required: true,
    render: ({ model, field }) => {
      return h(BasicUpload, {
        value: model[field],
        maxSize: 20,
        maxNumber: 1,
        emptyHidePreview: true,
        api: uploadVideo,
        onChange: (value) => {
          debugger
          if (value && value.length > 0) {
            model[field] = value[0].id;
            model['videoName'] = value[0].attachName;
          }
        },
        accept: videoTypes
      });
    },
    colProps: {
      span: 12
    }
  },
  {
    field: 'videoName',
    label: '视频名称',
    component: 'Input',
    colProps: {
      span: 12
    }
  }
];

export function generateTinymceField(labelName: string, fieldName: string) {
  return {
    label: labelName,
    field: fieldName,
    component: 'Input',
    required: true,
    render: ({ model, field }) => {
      return h(Tinymce, {
        value: model[field],
        height: editorHeight,
        menubar: false,
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
    colProps: {
      span: 20
    }
  };
}

export function generateAddOption(field: string) {
  return {
    field: field,
    component: 'Input',
    labelWidth: '16px',
    label: ' ',
    colProps: {
      span: 4,
    },
    slot: 'add',
  };
}

export function generateAnswer(options: [], defaultVal: string, isMulti: boolean = false) {
  const component = isMulti ? 'CheckboxGroup' : 'RadioButtonGroup';
  return  {
    field: 'answer',
    label: '参考答案',
    component: component,
    defaultValue: defaultVal,
    componentProps: {
      options: options,
    },
    required: true,
    colProps: {
      span: 12,
    },
  }
}

export function generateTinymceAnswer() {
  return {
    label: '参考答案',
    field: 'answer',
    component: 'Input',
    render: ({ model, field }) => {
      return h(Tinymce, {
        value: model[field],
        height: editorHeight,
        menubar: false,
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
    colProps: {
      span: 20
    }
  };
}

export function generateTextAnswer() {
  return {
    label: '参考答案',
    field: 'answer',
    component: 'InputTextArea',
    colProps: {
      span: 24
    }
  };
}

export function generateJudgementAnswer(component: string = 'RadioButtonGroup') {
  return {
    field: 'answer',
    label: '参考答案',
    component: component,
    defaultValue: 0,
    componentProps: {
      options: [
        {
          label: '正确',
          value: 0
        },
        {
          label: '错误',
          value: 1
        }
      ],
    },
    required: true,
    colProps: {
      span: 12,
    },
  }
}

export const columns: BasicColumn[] = [
  {
    title: '题目名称',
    dataIndex: 'subjectName',
    align: 'left',
  },
  {
    title: '类型',
    dataIndex: 'typeLabel',
    width: 100,
    customRender: ({ record }) => {
      const type = record.type + '';
      let color = subjectColor[type];
      return h(Tag, { color: color }, () => record.typeLabel);
    },
  },
  {
    title: '分类',
    dataIndex: 'categoryName',
    width: 80
  },
  {
    title: '分数',
    dataIndex: 'score',
    width: 80,
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: 80,
  },
  {
    title: '创建人',
    dataIndex: 'creator',
    width: 180,
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

export const formSchema: FormSchema[] = [
  {
    field: 'type',
    label: '类型',
    component: 'RadioButtonGroup',
    defaultValue: '0',
    componentProps: {
      options: [
        { label: '单选题', value: '0' },
        { label: '简答题', value: '1' },
        { label: '判断题', value: '2' },
        { label: '多选题', value: '3' },
        { label: '语音题', value: '4' },
        { label: '视频题', value: '5' },
      ],
    },
    required: true,
  },
  {
    field: 'subjectName',
    label: '题目名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'level',
    label: '难度等级',
    component: 'RadioButtonGroup',
    defaultValue: '1',
    componentProps: {
      options: [
        { label: '简单', value: '0' },
        { label: '一般', value: '1' },
        { label: '较难', value: '2' },
        { label: '非常难', value: '3' },
      ],
    },
    required: true,
  },
  {
    field: 'score',
    label: '分值',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'answer',
    label: '参考答案',
    component: 'Input',
    required: true,
  },
  {
    label: '解析',
    field: 'analysis',
    component: 'InputTextArea',
  },
];
