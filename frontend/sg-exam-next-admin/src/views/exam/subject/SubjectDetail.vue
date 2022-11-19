<template>
  <div :class="prefixCls">
    <div :class="`${prefixCls}-bottom`">
      <Tabs :activeKey="activeKey" :onChange="onChange">
        <template v-if="ready" v-for="item in subjectTypeList" :key="item.key">
          <TabPane :tab="item.name">
            <component :is="item.component" :subjectData="subjectData" :categoryId="categoryId" :examinationId="examinationId" :defaultOptions="defaultOptions" :nextSubjectNo="nextSubjectNo" :isMulti="isMulti"/>
          </TabPane>
        </template>
      </Tabs>
    </div>
  </div>
</template>
<script lang="ts">
import {Tabs, Tag} from 'ant-design-vue';
import {defineComponent, onMounted, ref, unref} from 'vue';
import {useRoute} from 'vue-router';
import SubjectChoices from './SubjectChoices.vue';
import SubjectShortAnswer from './SubjectShortAnswer.vue';
import SubjectJudgement from './SubjectJudgement.vue';
import SubjectSpeech from "./SubjectSpeech.vue";
import SubjectVideo from "./SubjectVideo.vue";
import {subjectTypeList, subjectType} from './subject.data';
import {getSubjectInfo} from '/@/api/exam/subject';
import { getDefaultOptionList } from '/@/api/exam/option';
import { useRouter } from 'vue-router';
import {nexSubjectNo} from "/@/api/exam/examination";

export default defineComponent({
  name: 'SubjectModal',
  components: { Tag, Tabs, TabPane: Tabs.TabPane, SubjectChoices, SubjectShortAnswer, SubjectJudgement, SubjectSpeech, SubjectVideo},
  emits: ['success', 'register', 'subjectEvent'],
  setup() {
    const route = useRoute();
    const router = useRouter();
    const subjectId = ref<any>(route.params?.id);
    const { query } = unref(router.currentRoute);
    const categoryId = query.categoryId;
    const examinationId = query.examinationId;
    const subjectData = ref<any>(null);
    const defaultOptions = ref<any>(null);
    const nextSubjectNo = ref<number>(1);
    const ready = ref<boolean>(false);
    // 默认单选题
    const activeKey = ref<number>(subjectType.SubjectChoices);
    // 是否多选
    const isMulti = ref<boolean>(false);
    async function fetch() {
      const unRef = unref(subjectId);
      if (unRef !== '0') {
        let subjectInfo = await getSubjectInfo(unRef);
        subjectData.value = subjectInfo;
        if (subjectInfo && subjectInfo.options) {
         let options: any[] = [];
          subjectInfo.options.forEach(o => {
            options.push(o.optionName);
          });
          defaultOptions.value = options;
        }
        activeKey.value = subjectInfo.type;
      } else {
        defaultOptions.value = await getDefaultOptionList();
        if (examinationId) {
          const no = await nexSubjectNo(examinationId);
          if (no) {
            nextSubjectNo.value = no;
          }
        }
      }
      ready.value = true;
    }

    function onChange(key) {
      isMulti.value = key === subjectType.SubjectMultiChoices;
      activeKey.value = key;
    }

    onMounted(() => {
      fetch();
    });
    return {
      prefixCls: 'subject',
      subjectTypeList,
      subjectId,
      subjectData,
      categoryId,
      examinationId,
      defaultOptions,
      nextSubjectNo,
      ready,
      activeKey,
      isMulti,
      onChange
    };
  },
});
</script>

<style lang="less" scoped>
.subject {
  &-col:not(:last-child) {
    padding: 0 10px;

    &:not(:last-child) {
      border-right: 1px dashed rgb(206 206 206 / 50%);
    }
  }

  &-bottom {
    padding: 10px;
    margin: 16px 16px 16px;
    background-color: @component-background;
    border-radius: 3px;
  }
}
</style>
