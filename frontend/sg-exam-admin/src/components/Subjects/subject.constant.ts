export const editorHeight = 260;

export const speechTypes = ['.mp3'];

export const videoTypes = ['.mp4'];

export const tinymcePlugins = [
  'kityformula-editor image imagetools advlist anchor autolink autosave code codesample  directionality  fullscreen hr insertdatetime link lists media nonbreaking noneditable pagebreak paste preview save searchreplace spellchecker tabfocus  template  textpattern visualblocks visualchars wordcount table',
];

export const tinymceToolbar = [
  'fontsizeselect lineheight searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample',
  'hr bullist numlist link  preview anchor pagebreak insertdatetime media  forecolor backcolor fullscreen image',
  'table kityformula-editor'
];

export const optionPrefix = 'options.';

export const addOptionBtnSlot = 'slot_';

export interface TabItem {
  key: number;
  name: string;
  component: string;
  disabled: boolean;
}

export interface OptionItem {
  optionName: string;
  optionContent: string;
  sort: number
}

export interface SubjectSubmitData {
  type: number | undefined;
  categoryId: string | undefined;
  examinationId: string | undefined;
  materialId: string | undefined;
  options: OptionItem[];
  answer: object
}

export const subjectType: any = {
  SubjectChoices: 0,
  SubjectShortAnswer: 1,
  SubjectJudgement: 2,
  SubjectMultiChoices: 3,
  SubjectFillBlank: 4,
  SubjectMaterial: 5
}

export const subjectTypeList: TabItem[] = [
  {
    key: subjectType.SubjectChoices,
    name: '单选题',
    component: 'SubjectChoices',
    disabled: false
  },
  {
    key: subjectType.SubjectShortAnswer,
    name: '简答题',
    component: 'SubjectShortAnswer',
    disabled: false
  },
  {
    key: subjectType.SubjectJudgement,
    name: '判断题',
    component: 'SubjectJudgement',
    disabled: false
  },
  {
    key: subjectType.SubjectMultiChoices,
    name: '多选题',
    component: 'SubjectChoices',
    disabled: false
  },
  {
    key: subjectType.SubjectFillBlank,
    name: '填空题',
    component: 'SubjectFillBlank',
    disabled: false
  },
  {
    key: subjectType.SubjectMaterial,
    name: '材料题',
    component: 'SubjectMaterial',
    disabled: false
  }
];

export const COLOR = {
  ZERO: '#87d068',
  ONE: '#2db7f5',
  TWO: '#108ee9',
  THREE: '#f50',
  FOUR: '#08979c',
  FIVE: '#7db7f5',
}

export const subjectColor:any = {
  '0': COLOR.ZERO,
  '1': COLOR.ONE,
  '2': COLOR.TWO,
  '3': COLOR.THREE,
  '4': COLOR.FOUR,
  '5': COLOR.FIVE,
}
