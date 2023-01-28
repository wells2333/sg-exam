export const editorHeight = 120;

export const videoTypes = ['mp4'];

export const tinymcePlugins = [
  'image imagetools advlist anchor autolink autosave code codesample  directionality  fullscreen hr insertdatetime link lists media nonbreaking noneditable pagebreak paste preview save searchreplace tabfocus  template  textpattern visualblocks visualchars',
];

export const tinymceToolbar = [
  'fontsizeselect bold italic underline strikethrough blockquote subscript superscript link  preview insertdatetime forecolor backcolor fullscreen image'
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
  options: OptionItem[];
  answer: object
}

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
    key: subjectType.SubjectSpeech,
    name: '语音题',
    component: 'SubjectSpeech',
    disabled: false
  },
  {
    key: subjectType.SubjectVideo,
    name: '视频题',
    component: 'SubjectVideo',
    disabled: false
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
