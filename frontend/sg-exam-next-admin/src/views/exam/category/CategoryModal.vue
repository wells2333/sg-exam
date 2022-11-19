<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './category.data';
import { getSubjectCategoryTree, createCategory, updateCategory } from '/@/api/exam/subjectCategory';
export default defineComponent({
  name: 'CategoryModal',
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
      const treeData = await getSubjectCategoryTree();
      updateSchema({
        field: 'parentId',
        componentProps: { treeData },
      });
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增分类' : '编辑分类'));
    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateCategory(id, values);
        } else {
          await createCategory(values);
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
