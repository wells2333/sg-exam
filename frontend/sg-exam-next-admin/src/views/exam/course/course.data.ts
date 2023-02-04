import {BasicColumn, FormSchema} from '/@/components/Table';
import {h, unref} from "vue";
import {Image, Rate, Tag} from "ant-design-vue";
import {SgUpload} from "/@/components/SgUpload";
import {uploadImage} from "/@/api/exam/examMedia";
import {Tinymce} from "/@/components/Tinymce";
import {
  editorHeight,
  tinymcePlugins,
  tinymceToolbar
} from "/@/components/Subjects/subject.constant";
import {ExamMediaApi} from "/@/api/api";

export const columns: BasicColumn[] = [
  {
    title: '课程名称',
    dataIndex: 'courseName',
    width: 160,
    align: 'left',
  },
  {
    title: '图片',
    dataIndex: 'imageUrl',
    width: 100,
    align: 'left',
    style: {
      cursor: 'pointer'
    },
    customRender: ({record}) => {
      return h(Image, {src: record.imageUrl, height: '40px', alt: record.courseName});
    },
  },
  {
    dataIndex: 'level',
    title: '难度等级',
    width: 150,
    align: 'left',
    customRender: ({record}) => {
      return h(Rate, {
        value: record.level,
        disabled: true,
        allowHalf: false,
      });
    },
  },
  {
    dataIndex: 'chargeType',
    title: '类型',
    width: 70,
    align: 'left',
    customRender: ({record}) => {
      const chargeType = record.chargeType;
      let color = 'green';
      let text = '免费';
      if (chargeType !== null && chargeType !== 0) {
        color = 'blue';
        text = '收费';
      }
      return h(Tag, {color: color}, () => text);
    },
  },
  {
    dataIndex: 'chargePrice',
    title: '价格',
    width: 80,
    align: 'left',
  },
  {
    dataIndex: 'courseStatus',
    title: '状态',
    width: 70,
    align: 'left',
    customRender: ({record}) => {
      const courseStatus = record.courseStatus;
      let color = 'green';
      let text = '已发布';
      if (courseStatus !== null && courseStatus === 0) {
        color = 'blue';
        text = '草稿';
      }
      return h(Tag, {color: color}, () => text);
    },
  },
  {
    title: '老师',
    dataIndex: 'teacher',
    width: 100,
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
    field: 'courseName',
    label: '课程名称',
    component: 'Input',
    colProps: {span: 8},
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'courseName',
    label: '课程名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'courseStatus',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '已发布', value: 1 },
      ],
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'chargeType',
    label: '类型',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '免费', value: 0 },
        { label: '收费', value: 1 },
      ],
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'level',
    label: '难度等级',
    component: 'Rate',
    defaultValue: 3,
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'chargePrice',
    label: '收费价格',
    component: 'InputNumber',
    defaultValue: 0,
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
    required: true,
    defaultValue: 100,
    colProps: { span: 12 },
  },
  {
    label: '课程图片',
    field: 'imageId',
    component: 'Input',
    render: ({model, field}) => {
      return h(SgUpload, {
        value: model[field],
        url: model['imageUrl'],
        api: uploadImage,
        handleDone: (value) => {
          if (value) {
            model[field] = unref(value).id;
          }
        },
      });
    },
    colProps: { span: 12 },
  },
  {
    field: 'college',
    label: '学院',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'major',
    label: '专业',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'teacher',
    label: '老师',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'simpleDesc',
    label: '简短描述',
    component: 'InputTextArea',
  },
  {
    label: '详细描述',
    field: 'courseDescription',
    component: 'Input',
    render: ({model, field}) => {
      return h(Tinymce, {
        value: model[field],
        height: editorHeight,
        plugins: tinymcePlugins,
        toolbar: tinymceToolbar,
        height: 150,
        // 指定上传URL
        uploadUrl: ExamMediaApi.UploadImage,
        onChange: (value: string) => {
          model[field] = value;
        },
      });
    },
  },
];

export const evaluateColumns: BasicColumn[] = [
  {
    title: '课程',
    dataIndex: 'courseName',
    width: 160,
    align: 'left',
  },
  {
    title: '评价等级',
    dataIndex: 'evaluateLevel',
    width: 120,
    align: 'left',
    customRender: ({record}) => {
      return h(Rate, {
        value: record.evaluateLevel,
        disabled: true,
        allowHalf: false,
      });
    },
  },
  {
    title: '评价内容',
    dataIndex: 'evaluateContent',
    width: 200,
    align: 'left',
  },
  {
    title: '评价人',
    dataIndex: 'operator',
    width: 80,
    align: 'left',
  },
  {
    title: '提交时间',
    dataIndex: 'createTime',
    width: 160,
    align: 'left',
  },
]

export const evaluateSearchFormSchema: FormSchema[] = [
  {
    field: 'evaluateContent',
    label: '评价内容',
    component: 'Input',
    colProps: {span: 8},
  }
];

export const memberColumns: BasicColumn[] = [
  {
    title: '学员ID',
    dataIndex: 'userId',
    width: 160,
    align: 'left',
  },
  {
    title: '名称',
    dataIndex: 'userName',
    width: 120,
    align: 'left',
  },
  {
    title: '性别',
    dataIndex: 'gender',
    width: 50,
    align: 'left',
    customRender: ({record}) => {
      const gender = record.gender;
      return h(Tag, {color: gender === 0 ? 'green' : 'blue'}, () => gender === 0 ? '男' : '女');
    },
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    width: 160,
    align: 'left',
  },
  {
    title: '电话',
    dataIndex: 'phone',
    width: 160,
    align: 'left',
  },
  {
    title: '加入时间',
    dataIndex: 'createTime',
    width: 160,
    align: 'left',
  }
];

export const memberSearchFormSchema: FormSchema[] = [
  {
    field: 'userId',
    label: '学员ID',
    component: 'Input',
    colProps: {span: 8},
  }
];
