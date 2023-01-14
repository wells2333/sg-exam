import {BasicColumn, FormSchema} from '/@/components/Table';
import {h, unref} from "vue";
import {Image, Tag} from "ant-design-vue";
import {SgUpload} from "/@/components/SgUpload";
import {uploadImage} from "/@/api/exam/examMedia";

export const operationBannerColor = {
  0: 'green',
  1: 'magenta',
  2: 'gold',
}

export const operationBannerName = {
  0: '自定义',
  1: '课程',
  2: '考试',
}

export const columns: BasicColumn[] = [
  {
    title: '运营名称',
    dataIndex: 'operationName',
    width: 160,
    align: 'left',
  },
  {
    title: '图片',
    dataIndex: 'imageUrl',
    width: 160,
    align: 'left',
    style: {
      cursor: 'pointer'
    },
    customRender: ({record}) => {
      return h(Image, {src: record.imageUrl, height: '40px', alt: record.courseName});
    },
  },
  {
    dataIndex: 'operationType',
    title: '类型',
    width: 80,
    align: 'left',
    customRender: ({record}) => {
      const color = operationBannerColor[record.operationType];
      return h(Tag, {color: color}, () => operationBannerName[record.operationType]);
    },
  },
  {
    dataIndex: 'redirectUrl',
    title: '重定向URL',
    width: 160,
    align: 'left',
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
    dataIndex: 'operator',
    width: 100,
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'operationName',
    label: '运营名称',
    component: 'Input',
    colProps: {span: 8},
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'operationName',
    label: '运营名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'operationType',
    label: '类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        {label: '自定义', value: 0},
        {label: '课程', value: 1},
        {label: '考试', value: 2},
      ],
    },
    required: true,
  },
  {
    field: 'imageId',
    label: '图片',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        api: uploadImage,
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
            model['imageUrl'] = unref(value).url;
          }
        },
      });
    },
    colProps: {span: 12},
  },
  {
    field: 'imageUrl',
    label: '图片URL',
    component: 'Input',
    colProps: {span: 12}
  },
  {
    field: 'redirectUrl',
    label: '跳转的URL',
    component: 'Input'
  },
  {
    label: '描述',
    field: 'operationDesc',
    component: 'InputTextArea',
  },
];
