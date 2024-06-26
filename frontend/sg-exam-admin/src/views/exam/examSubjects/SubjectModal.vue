<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit"
              width="80%">
    <div :class="prefixCls">
      <div :class="`${prefixCls}-bottom`">
        <Tabs :activeKey="activeKey" :onChange="onChange">
          <template v-for="item in types" :key="item.key">
            <TabPane :tab="item.name" :disabled="item.disabled" forceRender="true">
              <component ref="subRef" :is="item.component"/>
              <div v-if="item.key == 5">
              <a-button
                type="primary"
                @click="handleClick"
              >
                题目管理
              </a-button>
            </div>
            </TabPane>
          </template>
        </Tabs>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {Tabs, Tag} from 'ant-design-vue';
import {computed, defineComponent, ref, unref} from 'vue';
import SubjectChoices from '/@/components/Subjects/SubjectChoices.vue';
import SubjectShortAnswer from '/@/components/Subjects/SubjectShortAnswer.vue';
import SubjectJudgement from '/@/components/Subjects/SubjectJudgement.vue';
import SubjectFillBlank from '/@/components/Subjects/SubjectFillBlank.vue';
import SubjectMaterial from '/@/components/Subjects/SubjectMaterial.vue';
import {subjectType, subjectTypeList, TabItem} from '/@/components/Subjects/subject.constant';
import {createSubject, getSubjectInfo, updateSubject} from '/@/api/exam/subject';
import {getDefaultOptionList} from '/@/api/exam/option';
import {nexSubjectNo} from "/@/api/exam/examination";
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm} from '/@/components/Form/index';
import {useRoute} from "vue-router";
import {useMessage} from "/@/hooks/web/useMessage";
import {useGo} from "/@/hooks/web/usePage";
export default defineComponent({
  name: 'SubjectDataModal',
  components: {
    Tabs,
    Tag,
    TabPane: Tabs.TabPane,
    BasicModal,
    BasicForm,
    SubjectChoices,
    SubjectShortAnswer,
    SubjectJudgement,
    SubjectFillBlank,
    SubjectMaterial
  },
  emits: ['success', 'register'],
  setup(_, {emit}) {
    const {t} = useI18n();
    const {createMessage} = useMessage();
    const subRef = ref<any>();
    const isUpdate = ref(true);
    let id: string;
    const route = useRoute();
    const go = useGo();
    const examinationId = ref<string>(route.params?.id + '');
    // 题目类型 tab
    const types = ref<Array<TabItem>>([...subjectTypeList]);
    // 默认单选题
    const activeKey = ref<number>(subjectType.SubjectChoices);
    // 是否多选
    const isMulti = ref<boolean>(false);
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
      setModalProps({
        afterClose: function () {
          // 启用全部 tab
          unref(types).forEach(e => {
            e.disabled = false;
          });
        }
      });
      setModalProps({confirmLoading: false});
      isUpdate.value = !!data?.isUpdate;
      id = data.record?.id || null;
      // 题目数据
      let subjectData: object = {};
      if (unref(isUpdate)) {
        const data = await fetch();
        Object.assign(subjectData, data);
        disabledOtherTab(activeKey.value);
      } else {
        const {
          defaultOptions,
          nextSubjectNo
        } = await initDefaultData();
        Object.assign(subjectData, {
          defaultOptions,
          nextSubjectNo
        });
        // 新增题目，默认切换到单选题 tab
        activeKey.value = subjectType.SubjectChoices;
      }
      setSubjectValue(subjectData);
    });
    const getTitle = computed(() => (!unref(isUpdate) ? t('common.addText') :
      t('common.editText')));

    // 重新查询题目信息
    async function fetch() {
      let data: object = {};
      if (id) {
        let subjectInfo = await getSubjectInfo(unref(id));
        activeKey.value = subjectInfo.type;
        if (subjectInfo && subjectInfo.options) {
          let options: any[] = [];
          subjectInfo.options.forEach(o => {
            options.push(o.optionName);
          });
          Object.assign(data, {defaultOptions: options});
        }
        Object.assign(data, subjectInfo);
      } else {
        const {defaultOptions, nextSubjectNo} = await initDefaultData();
        Object.assign(data, {defaultOptions, nextSubjectNo});
      }
      return data;
    }

    async function initDefaultData() {
      const defaultOptions = await getDefaultOptionList();
      let nextSubjectNo: string = '1';
      if (examinationId) {
        const no = await nexSubjectNo(examinationId.value);
        if (no) {
          nextSubjectNo = no;
        }
      }
      return {
        defaultOptions,
        nextSubjectNo
      }
    }

    async function onChange(key) {
      const subjectRef = getSubjectRefByKey(key);
      if (subjectRef) {
        isMulti.value = key === subjectType.SubjectMultiChoices;
        // 切换 tab，从新初始化选项，题目序号
        const {defaultOptions, nextSubjectNo} = await initDefaultData();
        const data = {
          defaultOptions,
          nextSubjectNo,
          examinationId: unref(examinationId),
          isMulti: unref(isMulti),
        };
        subjectRef.resetSchemas(data);
      }
      setTimeout(() => {
        activeKey.value = key;
      }, 20);
    }

    // 禁用其它 tab
    function disabledOtherTab(key: number) {
      unref(types).forEach(e => {
        if (e.key !== key) {
          e.disabled = true;
        }
      });
    }

    async function handleSubmit() {
      try {
        setModalProps({confirmLoading: true});
        const value = await getSubjectRef().getSubjectValue();
        if (id) {
          await updateSubject(id, value);
        } else {
          await createSubject(value);
        }
        createMessage.success('保存成功');
        closeModal();
        emit('success');
      } catch (error) {
        console.error(error);
        createMessage.error('保存失败');
      } finally {
        setModalProps({confirmLoading: false});
      }
    }

    function getSubjectRef() {
      return unref(subRef)[activeKey.value];
    }

    function getSubjectRefByKey(key) {
      return unref(subRef)[key];
    }

    function setSubjectValue(subjectData: object) {
      const {defaultOptions, nextSubjectNo} = subjectData;
      const data = {
        subjectData,
        defaultOptions,
        nextSubjectNo,
        isMulti: unref(isMulti),
        examinationId: unref(examinationId),
      };
      const subjectRef = getSubjectRef();
      subjectRef.setSubjectValue(data);
    }
    function handleClick(){
      go('/exam/material_subjects/' + id +"/"+ examinationId.value);
    }
    return {
      t,
      prefixCls: 'subject',
      subRef,
      types,
      examinationId,
      activeKey,
      isMulti,
      onChange,
      registerModal,
      getTitle,
      handleSubmit,
      handleClick
    };
  },
});
</script>

<style lang="less">
.ant-modal-wrap .ant-modal {
  top: 20px;
}

// 按钮居中
.ant-modal-footer {
  text-align: center !important;
}
</style>
