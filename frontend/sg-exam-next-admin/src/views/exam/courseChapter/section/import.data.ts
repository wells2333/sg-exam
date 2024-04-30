import {FormSchema} from "/@/components/Form";
import {h, unref} from "vue";
import {SgUpload} from "/@/components/SgUpload";
import {importSection} from "/@/api/exam/chapter";

export const formSchema: FormSchema[] = [
  {
    field: 'chapterId',
    label: '章 ID',
    component: 'Input',
    required: true,
    show: false
  },
  {
    label: '上传文件',
    field: 'file',
    component: 'Input',
    helpMessage: ['只能上传 zip 文件'],
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        api: importSection,
        type: 'zip',
        accept: '.zip',
        showFileList: false,
        handleFormData: (formData) => {
          const {data} = formData;
          Object.assign(data, {chapterId: model['chapterId']});
        },
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
          }
        },
      });
    },
  },
]
