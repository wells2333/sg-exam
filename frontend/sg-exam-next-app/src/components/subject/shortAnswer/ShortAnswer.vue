<template>
  <view v-bind="$attrs">
    <view class="short-answer-area">
      <at-textarea :value="value" v-if="showInput" @change="handleChange" placeholder="请输入" :disabled="disabled" :max-length="maxLength"/>
    </view>
  </view>
</template>

<script lang="ts">
import {defineComponent, ref, unref} from 'vue';
import {isNotEmpty, transformToArray} from '../../../utils/util';

export default defineComponent({
  name: 'ShortAnswer',
  components: {},
  props: {
    subject: {
      type: Object,
      default: () => {
      }
    },
    disabled: {
      type: Boolean,
      default: () => false
    },
    showInput: {
      type: Boolean,
      default: () => true
    },
    answer: {
      type: Object,
      default: () => undefined
    },
    standAnswer: {
      type: Object,
      default: () => undefined
    },
    maxLength: {
      type: Number,
      default: () => 500
    }
  },
  emits: [
    'update-selected'
  ],
  setup(props, {emit}) {
    const subject = ref<object>(props.subject);
    const value = ref<String>('');
    const standAnswer = ref<String>('');
    const disabled = ref<boolean>(props.disabled);
    const maxLength = ref<number>(props.maxLength);
    const showInput = ref<boolean>(props.showInput);

    function handleChange(v) {
      value.value = v;
      props.subject.answerValue = v;
      props.subject.checked = true;
      emit('update-selected', props.subject, v)
    }

    function update(obj) {
      subject.value = unref(obj);
      if (isNotEmpty(subject.value) && isNotEmpty(subject.value.answerValue)) {
        value.value = subject.value.answerValue;
      } else {
        value.value = '';
      }
    }

    function initAnswerValue() {
      if (props.answer !== undefined) {
        value.value = props.answer;
      }
      if (props.standAnswer !== undefined) {
        standAnswer.value = props.standAnswer;
      }
    }

    function init() {
      initAnswerValue();
    }

    init();
    return {
      maxLength,
      subject,
      value,
      disabled,
      showInput,
      handleChange,
      update
    }
  }
})
</script>
<style>
.short-answer-area {
  padding-bottom: 8px;
}
</style>