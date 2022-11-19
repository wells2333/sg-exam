<template>
  <view v-bind="$attrs">
    <view class="at-checkbox">
      <view v-for="(option, idx) in options" :key="option.value" :class="genOptionClasses(option)"
            @tap="handleClick(idx)">
        <view class="at-checkbox__option-wrap">
          <view class="at-checkbox__option-cnt">
            <view class="at-checkbox__icon-cnt">
              <text class="at-icon at-icon-check"/>
            </view>
            <view class="at-checkbox__title">
              <view v-html="option.label" class="choice-option-label"></view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import {defineComponent, ref, unref } from 'vue';

export default defineComponent({
  name: 'Judgement',
  components: {},
  props: {
    subject: {
      type: Object,
      default: () => {}
    },
    disabled: {
      type: Boolean,
      default: () => false
    },
    answer: {
      type: Object,
      default: () => undefined
    },
    standAnswer: {
      type: Object,
      default: () => undefined
    }
  },
  emits: [
    'update-selected'
  ],
  setup(props, { emit }) {
    const options = ref<Array>([]);
    const selectedList = ref<Array>([]);
    const subject = ref<object>(props.subject);

    function init(obj) {
      subject.value = unref(obj);
      options.value = [];
      selectedList.value = [];
      if (subject.value.answerValue !== undefined && subject.value.answerValue !== '') {
        selectedList.value.push(subject.value.answerValue);
      }
      options.value.push({label: '正确', value: '正确'});
      options.value.push({label: '错误', value: '错误'});
    }


    function handleChange(value) {
      selectedList.value = value;
    }

    function genOptionClasses(option) {
      return {
        'at-checkbox__option': true,
        'at-checkbox__option--disabled': option.disabled,
        'at-checkbox__option--selected': selectedList.value.includes(option.value)
      }
    }

    function handleClick(idx: number) {
      const option = options.value[idx];
      const {value} = option;
      selectedList.value = Array.of(value);
      // 回传选中的内容和选中状态
      subject.value.answerValue = selectedList.value.join(',');
      subject.value.checked = true;
      emit('update-selected', subject, selectedList.value.join(','))
    }

    function update(obj) {
      init(obj);
    }

    init(ref<any>(props.subject));
    return {
      subject,
      options,
      selectedList,
      handleChange,
      genOptionClasses,
      handleClick,
      update
    }
  }
})
</script>

<style>
.choice-option-label {
  display: inline-flex;
}
</style>