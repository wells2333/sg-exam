import {FormSchema} from "/@/components/Form";
import {h, unref} from "vue";
import {SgUpload} from "/@/components/SgUpload";
import {uploadJSON, uploadEXCEL} from "/@/api/exam/subject";

export const formSchema: FormSchema[] = [
  {
    field: 'categoryId',
    label: '分类ID',
    component: 'Input',
    required: true,
    show: false
  },
  {
    label: 'JSON格式',
    field: 'jsonFile',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        api: uploadJSON,
        type: 'json',
        accept: '.json',
        showFileList: false,
        handleFormData: (formData) => {
          const {data} = formData;
          // 增加分类ID参数
          Object.assign(data, {categoryId: model['categoryId']});
        },
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
          }
        },
      });
    },
  },
  {
    label: 'EXCEL格式',
    field: 'excelFile',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        api: uploadEXCEL,
        type: 'excel',
        accept: '.xlsx',
        showFileList: false,
        handleFormData: (formData) => {
          const {data} = formData;
          // 增加分类ID参数
          Object.assign(data, {categoryId: model['categoryId']});
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
