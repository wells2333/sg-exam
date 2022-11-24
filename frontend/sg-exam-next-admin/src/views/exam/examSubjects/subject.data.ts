import {FormSchema} from '/@/components/Table';
import {h} from "vue";
import {Tinymce} from "/@/components/Tinymce";
import {BasicUpload} from "/@/components/Upload";
import {uploadVideo} from "/@/api/exam/examMedia";
import {editorHeight, tinymcePlugins, tinymceToolbar, videoTypes, TabItem} from '/@/components/Subjects/subject.constant';

export const formSchema: FormSchema[] = [
  {
    field: 'examinationName',
    label: '考试名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'parentDept',
    label: '上级部门',
    component: 'TreeSelect',

    componentProps: {
      fieldNames: {
        label: 'deptName',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: true,
  },
  {
    field: 'orderNo',
    label: '序号',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: '0',
    componentProps: {
      options: [
        {label: '启用', value: '0'},
        {label: '停用', value: '1'},
      ],
    },
    required: true,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
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
    disabled: false,
  },
  {
    key: subjectType.SubjectShortAnswer,
    name: '简答题',
    component: 'SubjectShortAnswer',
    disabled: false,
  },
  {
    key: subjectType.SubjectJudgement,
    name: '判断题',
    component: 'SubjectJudgement',
    disabled: false,
  },
  {
    key: subjectType.SubjectMultiChoices,
    name: '多选题',
    component: 'SubjectChoices',
    disabled: false,
  },
  {
    key: subjectType.SubjectSpeech,
    name: '语音题',
    component: 'SubjectSpeech',
    disabled: false,
  },
  {
    key: subjectType.SubjectVideo,
    name: '视频题',
    component: 'SubjectVideo',
    disabled: false,
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
export const subjectColor: any = {
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
    render: ({model, field}) => {
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
        {label: '简单', value: 0},
        {label: '一般', value: 1},
        {label: '较难', value: 2},
        {label: '非常难', value: 3},
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
        {label: '自动判分', value: 0},
        {label: '人工判分', value: 1},
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
    render: ({model, field}) => {
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

export function generateTinymceField(labelName: string, fieldName: string): FormSchema {
  return {
    label: labelName,
    field: fieldName,
    component: 'Input',
    required: true,
    render: ({model, field}) => {
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

export function generateAddOption(field: string): FormSchema {
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

export function generateAnswer(options: [], defaultVal: string, isMulti: boolean = false): FormSchema {
  const component = isMulti ? 'CheckboxGroup' : 'RadioButtonGroup';
  return {
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
    render: ({model, field}) => {
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
