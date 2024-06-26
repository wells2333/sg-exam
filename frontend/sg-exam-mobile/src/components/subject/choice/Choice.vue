<template>
   <div v-bind="$attrs">
    <a-video :subject="subject"></a-video>
     <div class="subject-choice-checkbox">
       <div v-for="(option, idx) in options" :key="option.value" :class="genOptionClasses(option)"
            @click="handleClick(idx)">
         <div class="subject-choice-checkbox__option-wrap">
           <div class="subject-choice-checkbox__option-cnt">
             <div :class="genOptionLabelClasses(option)">
              <text class="at-icon at-icon-check"/>
            </div>
             <div class="subject-choice-checkbox__title">
               <div class="subject-choice-option-name">
                 <div class="subject-choice-option-tag">{{ option.value }}</div>
              </div>
              <div class="choice-option-label" v-html="option.label"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref, unref} from 'vue';
import {AVideo} from '../AVideo';
import {transformToArray, joinArray} from '../../../utils/util';

export default defineComponent({
  name: 'Choice',
  components: {AVideo},
  props: {
    subject: {
      type: Object,
      default: () => {
      }
    },
    multi: {
      type: Boolean,
      default: () => false
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
  setup(props, {emit}) {
    const options = ref<Array>([]);
    const selectedList = ref<Array>([]);
    const subject = ref<object>(props.subject);
    const standAnswer = ref<Array>([]);

    function reset() {
      options.value = [];
      selectedList.value = [];
    }

    function initAnswerValue() {
      let value = subject.value.answerValue;
      if (props.answer !== undefined) {
        value = props.answer;
      }
      selectedList.value = transformToArray(value);
      if (props.standAnswer !== undefined) {
        standAnswer.value = transformToArray(props.standAnswer);
      }
    }

    function initOptions(subjectOptions) {
      if (subjectOptions !== undefined) {
        subjectOptions.forEach(option => {
          const label = option.optionContent;
          const value = option.optionName;
          options.value.push({label, value, disabled: props.disabled});
        });
      }
    }

    function init(obj) {
      subject.value = unref(obj);
      reset();
      initAnswerValue();
      initOptions(unref(obj).options);
    }

    function handleChange(value) {
      selectedList.value = value;
    }

    function genOptionClasses(option) {
      return {
        'at-checkbox__option': true,
        'at-checkbox__option--selected': selectedList.value.includes(option.value)
      }
    }

    function genOptionLabelClasses(option) {
      return {
        'at-checkbox__icon-cnt': true,
        'red-color': standAnswer.value.includes(option.value)
      }
    }

    function handleClick(idx: number) {
      if (props.disabled) {
        return;
      }
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
      const joinVal = joinArray(selectedList.value);
      subject.value.answerValue = joinVal;
      subject.value.checked = true;
      emit('update-selected', subject, joinVal)
    }

    function update(obj) {
      init(obj);
    }

    function updateAnswer(value) {
      selectedList.value = transformToArray(value);
    }

    init(ref<any>(props.subject));
    return {
      subject,
      options,
      selectedList,
      handleChange,
      genOptionClasses,
      genOptionLabelClasses,
      handleClick,
      update,
      updateAnswer
    }
  }
})
</script>

<style lang="scss">
.choice-option-label {
  font-size: 34px;
  display: inline-flex;
}

.subject-choice-checkbox {
  position: relative;
  background-color: #FFF;
  padding-bottom: 8px;
  image {
    pointer-events: none;
  }
}

.subject-choice-checkbox__option-wrap {
  margin-top: 20px;
  padding-top: 20px;
  padding-bottom: 20px;
}

.subject-choice-checkbox__option-cnt {
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
}

.subject-choice-checkbox__title {
  margin-left: 10px;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  line-height: 1.5;
  text-align: left;
}

.subject-choice-option-name {
  margin-right: 16px;
  .subject-choice-option-tag {
    border: 1px solid grey;
    background: #fff;
    border-radius: 50%;
    width: 34px;
    height: 34px;
    text-align: center;
    line-height: 32px;
  }
}

.at-checkbox__option--selected {
  background-color: #9e9e9e30;
  border-radius: 8px;
}

</style>