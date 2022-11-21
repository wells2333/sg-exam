<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <div>
      <div class="subject-content">
        <div class="subject-title">
          <span class="subject-title-content">{{subject.sort}}.&nbsp;</span>
          <span class="subject-title-content" v-html="subject.subjectName"/>
          <span class="subject-title-content" v-if="subject.score !== 0">&nbsp;({{subject.score}})&nbsp;分</span>
        </div>
        <ul class="subject-options" v-for="option in subject.options" :key="option.id">
          <li class="subject-option">
            <label :for="'option' + option.id">
              <span class="subject-option-prefix">{{ option.optionName }}&nbsp;</span>
              <span v-html="option.optionContent" class="subject-option-prefix" />
            </label>
          </li>
        </ul>
        <a-divider />
        <div class="subject-title">
          <span class="subject-title-content">考生答案：</span>
          <span class="subject-title-content" v-if="subject.type === 2 && answer.answer === '0'">正确</span>
          <span class="subject-title-content" v-else-if="subject.type === 2 && answer.answer === '1'">错误</span>
          <span class="subject-title-content" v-else v-html="answer.answer"></span>

        </div>
        <div class="subject-title">
          <span class="subject-title-content">参考答案：</span>
          <span class="subject-title-content" v-if="subject.type === 2 && record.answer === '0'">正确</span>
          <span class="subject-title-content" v-else-if="subject.type === 2 && record.answer === '1'">错误</span>
          <span class="subject-title-content" v-else v-html="record.answer"></span>
        </div>
        <div class="subject-title">
          <span class="subject-title-content">解析：</span>
          <span class="subject-title-content" v-html="subject.analysis"></span>
        </div>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm } from '/@/components/Form/index';
import { Description } from '/@/components/Description';
import { Divider } from 'ant-design-vue';

const record = ref({});
const subject = ref({});
const answer = ref({});

export default defineComponent({
  name: 'CourseChapterModal',
  components: { BasicModal, BasicForm, Description, [Divider.name]: Divider},
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      record.value = data.record;
      subject.value = data.record.subject;
      answer.value = data.record.subject.answer;
      setModalProps({ confirmLoading: false });
    });
    const getTitle = computed(() => ('查看答题'));

    async function handleSubmit() {
      setModalProps({ confirmLoading: true });
      closeModal();
      emit('success');
      setModalProps({ confirmLoading: false });
    }
    return { record, subject, answer, registerModal, getTitle, handleSubmit };
  },
});
</script>

<style lang="less">
.subject-exam-title{
  font-size: 18px;
  line-height: 25px;
  padding: 18px 20px;
  border-bottom: 1px solid #DEDEDE;
  margin-bottom: 12px;
}
.subject {
  padding-left: 30px;
  padding-right: 75px;
}
.subject-content{
  padding-left: 10px;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-weight: 300;
  background: #fff;
  z-index: 1;
  position: relative;
}
.subject-title {
  font-size: 18px;
  line-height: 22px;
  .subject-title-number {
    display: inline-block;
    line-height: 22px;
  }
  .subject-title-content {
    display: inline-block;
    padding: 3px;
  }
}
.subject-options {
  margin: 0;
  padding: 0;
  list-style: none;
  > li {
    position: relative;
    font-size: 24px;
    label {
      word-break: break-all;
      padding-left: 12px;
      display: block;
      line-height: 1.0;
      transition: color 0.4s;
    }
    .subject-option-prefix {
      font-size: 16px;
      display: inline-block
    }
  }
}
.subject-answer {
  padding: 16px;
}
</style>
