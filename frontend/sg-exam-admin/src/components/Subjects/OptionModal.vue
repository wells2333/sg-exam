<template>
  <BasicModal
    v-bind="$attrs"
    @register="register"
    @ok="handleSubmit"
    @visibleChange="handleVisibleChange"
    title="请输入选项名称"
    :width="500"
  >
    <div class="pt-3px pr-3px">
      <BasicForm @register="registerForm" :model="model" />
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {defineComponent, ref, watch} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, FormSchema, useForm } from '/@/components/Form/index';
const schemas: FormSchema[] = [
  {
    field: 'optionName',
    component: 'Input',
    label: '选项名称',
    colProps: {
      span: 24,
    },
    defaultValue: '',
    required: true,
  },
];
export default defineComponent({
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  props: {
    optionField: { type: String }
  },
  setup(props, { emit }) {
    const modelRef = ref({});
    let optionField = props.optionField;
    const [
      registerForm,
      {
        resetFields, validate
      },
    ] = useForm({
      labelWidth: 120,
      schemas,
      showActionButtonGroup: false,
      actionColOptions: {
        span: 24,
      },
    });

    const [register, { closeModal }] = useModalInner((data) => {

    });

    function handleVisibleChange(visible: boolean) {
      if (visible) {
        setTimeout(() => {
          resetFields();
        }, 50);
      }
    }

    async function handleSubmit() {
      const values = await validate();
      values.field = optionField;
      closeModal();
      emit('success', values);
    }

    watch(
      () => props.optionField,
      (v) => {
        optionField = v;
      },
    );

    return { register, schemas, registerForm, model: modelRef, handleVisibleChange, handleSubmit };
  },
});
</script>
