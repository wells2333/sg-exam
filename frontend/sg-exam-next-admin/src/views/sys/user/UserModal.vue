<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { userFormSchema } from './user.data';
import { getDeptList } from '/@/api/sys/dept';
import { createUser, updateUser } from '/@/api/sys/user';

export default defineComponent({
  name: 'UserModal',
  components: { BasicModal, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    const rowId = ref('');
    let id: string;
    const [registerForm, { setFieldsValue, updateSchema, resetFields, validate }] = useForm({
      labelWidth: 100,
      schemas: userFormSchema,
      showActionButtonGroup: false,
      actionColOptions: {
        span: 23,
      },
    });

    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;

      if (unref(isUpdate)) {
        rowId.value = data.record.id;
        setFieldsValue({
          ...data.record
        });
      }
      id = data.record?.id || null;
      const treeData = await getDeptList();
      updateSchema([
        {
          field: 'pwd',
          show: !unref(isUpdate),
        },
        {
          field: 'deptId',
          componentProps: { treeData },
        },
      ]);
    });

    const getTitle = computed(() => (!unref(isUpdate) ? '新增账号' : '编辑账号'));

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({ confirmLoading: true });
        if (!Array.isArray(values.role)) {
          values.role = new Array(values.role);
        }
        if (id) {
          await updateUser(id, values);
        } else {
          await createUser(values);
        }
        closeModal();
        emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }

    return { registerModal, registerForm, getTitle, handleSubmit };
  },
});
</script>
