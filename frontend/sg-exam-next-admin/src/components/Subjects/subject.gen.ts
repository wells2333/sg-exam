import {FormSchema} from "/@/components/Form";
import {h, unref} from "vue";
import {Tinymce} from "/@/components/Tinymce";
import {uploadVideo} from "/@/api/exam/examMedia";
import {
  addOptionBtnSlot,
  editorHeight,
  optionPrefix,
  tinymcePlugins,
  tinymceToolbar,
  videoTypes
} from './subject.constant';
import {SgUpload} from "/@/components/SgUpload";

export function emptySubject() {
  return {
    subjectName: '',
    analysis: '',
    answer: '',
    level: 1,
    'options.A': '',
    'options.B': '',
    'options.C': '',
    'options.D': '',
  }
}

// 选择题
export function generateChoicesSchemas(subjectData: object, defaultOptions: object, isMulti: boolean, isUpdate: boolean) {
  const schemas: any[] = [];
  const optionsSchemas: FormSchema[] = [];
  const answerOptions = [];
  if (isUpdate) {
    const subjectDataVal = unref(subjectData);
    const {options, answer} = subjectDataVal;
    initOptionsAndAnswer(subjectDataVal, options, answerOptions, optionsSchemas, answer, isMulti);
  } else {
    // 新增题目
    const defaultOptionsVal = unref(defaultOptions);
    if (defaultOptionsVal !== undefined && defaultOptionsVal !== null) {
      if (defaultOptionsVal.length > 0) {
        defaultOptionsVal.forEach(item => {
          const fieldName = optionPrefix + item.labelName;
          optionsSchemas.push(generateTinymceField(item.labelName, fieldName));
          optionsSchemas.push(generateAddOption(addOptionBtnSlot + fieldName));
          answerOptions.push({label: item.labelName, value: item.labelName});
        });
      }
    }
  }
  schemas.push(...gentSubjectNameSchemas());
  schemas.push(...genBasicSchemas());
  schemas.push(...genOptionDividerSchemas());
  if (answerOptions.length > 0) {
    optionsSchemas.push(generateAnswer(answerOptions, answerOptions[0].value, unref(isMulti)));
  }
  schemas.push(...optionsSchemas);
  schemas.push(...genAnswerSchemas());
  return schemas;
}

export function initOptionsAndAnswer(subjectData, options, answerOptions, optionsSchemas, answer, isMulti: boolean) {
  if (options && options.length > 0) {
    if (optionsSchemas.length === 0) {
      options.forEach(item => {
        const fieldName = optionPrefix + item.optionName;
        optionsSchemas.push(generateTinymceField(item.optionName, fieldName));
        optionsSchemas.push(generateAddOption(addOptionBtnSlot + fieldName));
        subjectData[fieldName] = item.optionContent;
        answerOptions.push({label: item.optionName, value: item.optionName});
      });
    }
  }
  if (answer) {
    if (unref(isMulti)) {
      subjectData.answer = answer.answer.split(',');
    } else {
      subjectData.answer = answer.answer;
    }
  }
}

export function gentSubjectNameSchemas() {
  return [
    {
      field: 'sort',
      label: '序号',
      component: 'InputNumber',
      defaultValue: 1,
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
}

export function genBasicSchemas() {
  return [
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
    }
  ]
}

export function genOptionDividerSchemas() {
  return [
    {
      field: 'divider-options',
      component: 'Divider',
      label: '选项列表',
    }
  ]
}

export function genAnswerSchemas() {
  return [
    {
      label: '答案解析',
      field: 'analysis',
      component: 'Input',
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
}

export function judgeTypeSchemas() {
  return [
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
  ]
}

export function generateTextAnswer() {
  return [{
    label: '参考答案',
    field: 'answer',
    component: 'Input',
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
  }]
}

// 简答题
export function genShortAnswerSchemas() {
  const schemas: any[] = [];
  schemas.push(...gentSubjectNameSchemas());
  schemas.push(...genBasicSchemas());
  schemas.push(...judgeTypeSchemas());
  schemas.push(...generateTextAnswer());
  schemas.push(...genAnswerSchemas());
  return schemas;
}

// 判断题
export function genJudgementSchemas() {
  const schemas: any[] = [];
  schemas.push(...gentSubjectNameSchemas());
  schemas.push(...genBasicSchemas());
  schemas.push(...judgeTypeSchemas());
  schemas.push(...generateJudgementAnswer());
  schemas.push(...genAnswerSchemas());
  return schemas;
}

// 语音题
export function genSpeechSchemas() {
  const schemas: any[] = [];
  schemas.push(...genSpeechSubjectNameSchemas());
  schemas.push(...genBasicSchemas());
  schemas.push(...judgeTypeSchemas());
  schemas.push(...generateTextAnswer());
  schemas.push(...genAnswerSchemas());
  return schemas;
}

// 视频题
export function genVideoSchemas() {
  const schemas: any[] = [];
  schemas.push(...gentSubjectNameSchemas());
  schemas.push(...genUploadVideoSchemas());
  schemas.push(...genBasicSchemas());
  schemas.push(...judgeTypeSchemas());
  schemas.push(...generateTextAnswer());
  schemas.push(...genAnswerSchemas());
  return schemas;
}

export function genSpeechSubjectNameSchemas() {
  return [
    {
      field: 'sort',
      label: '序号',
      component: 'InputNumber',
      defaultValue: 1,
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
}

export function generateJudgementAnswer(component: string = 'RadioButtonGroup') {
  return [{
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
  }]
}

// 视频上传
export function genUploadVideoSchemas() {
  return [
    {
      label: '上传视频',
      field: 'videoId',
      component: 'Input',
      required: false,
      render: ({model, field}) => {
        return h(SgUpload, {
          value: model[field],
          api: uploadVideo,
          accept: videoTypes,
          type: 'video',
          handleDone: (value) => {
            if (value && unref(value)) {
              model[field] = unref(value).id;
              model['videoName'] = unref(value).name;
            }
          },
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
  ]
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
