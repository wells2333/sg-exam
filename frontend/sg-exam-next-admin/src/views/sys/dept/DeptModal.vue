<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './dept.data';
import { getDeptList, createDept, updateDept } from '/@/api/sys/dept';
import {createTenant, updateTenant} from "/@/api/sys/tenant";
export default defineComponent({
  name: 'DeptModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
        setFieldsValue({
          ...data.record,
        });
      }
      id = data.record?.id || null;
      const treeData = await getDeptList();
      updateSchema({
        field: 'parentId',
        componentProps: { treeData },
      });
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增部门' : '编辑部门'));
    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateDept(id, values);
        } else {
          await createDept(values);
        }
        closeModal();
        emit('success');
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }
    return { registerModal, registerForm, getTitle, handleSubmit };
  },
});
</script>
