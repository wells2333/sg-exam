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
import {defineComponent, ref} from 'vue';

export default defineComponent({
  name: 'Speech',
  components: {},
  props: {
    subject: {
      type: Object,
      default: () => {}
    },
    multi: {
      type: Boolean,
      default: () => false
    }
  },
  emits: [
      'update-selected'
  ],
  setup(props, { emit }) {
    // 选项列表
    const options = ref<Array>([]);
    const selectedList = ref<Array>([]);
    const subjectOptions = props.subject.options;
    if (subjectOptions !== undefined) {
      subjectOptions.forEach(option => {
        const label = option.optionName + '. ' + option.optionContent;
        const value = option.optionName;
        options.value.push({label, value});
      });
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
      let selectedSet;
      // 多选
      if (props.multi) {
        selectedSet = new Set(selectedList.value);
        if (!selectedSet.has(value)) {
          selectedSet.add(value);
        } else {
          selectedSet.delete(value);
        }
      } else {
        // 单选
        selectedSet = Array.of(value);
      }
      selectedList.value = Array.from(selectedSet);

      // 回传选中的内容和选中状态
      props.subject.answer = selectedList.value.join(',');
      props.subject.checked = true;
      emit('update-selected', props.subject, selectedList.value.join(','))
    }

    return {
      options,
      selectedList,
      handleChange,
      genOptionClasses,
      handleClick
    }
  }
})
</script>

<style>
.choice-option-label {
  display: inline-flex;
}
</style>