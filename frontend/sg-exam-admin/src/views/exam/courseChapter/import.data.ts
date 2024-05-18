import {FormSchema} from "/@/components/Form";
import {h, unref} from "vue";
import {SgUpload} from "/@/components/SgUpload";
import {importChapter} from "/@/api/exam/course";

export const formSchema: FormSchema[] = [
  {
    field: 'courseId',
    label: '课程 ID',
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
        api: importChapter,
        type: 'zip',
        accept: '.zip',
        showFileList: false,
        handleFormData: (formData) => {
          const {data} = formData;
          Object.assign(data, {courseId: model['courseId']});
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
