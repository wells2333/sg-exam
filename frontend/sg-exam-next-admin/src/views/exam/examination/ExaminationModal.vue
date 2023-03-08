<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" width="60%">
    <BasicForm @register="registerForm">
      <template #remoteSearch="{ model, field }">
        <ApiSelect
          :api="getSelectUserList"
          showSearch
          v-model:value="model[field]"
          :filterOption="false"
          resultField="list"
          labelField="name"
          valueField="id"
          mode="multiple"
          immediate="true"
          :params="searchParams"
          @search="onSearch"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm, ApiSelect } from '/@/components/Form/index';
import { formSchema } from './examination.data';
import { createExamination, getExaminationMembers, updateExamination } from "/@/api/exam/examination";
import {getSelectDeptList, getSelectUserList} from "/@/api/sys/select";

export default defineComponent({
  name: 'ExaminationModal',
  components: { BasicModal, BasicForm, ApiSelect },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const name = ref<string>('');
    const searchParams = computed<Recordable>(() => {
      return { name: unref(name) };
    });
    const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;
      const treeData = await getSelectDeptList();
      updateSchema([
        {
          field: 'deptMember',
          componentProps: { treeData },
        },
      ]);
      if (unref(isUpdate)) {
        const res = await getExaminationMembers(data.record?.id);
        if (res) {
          data.record.members = res.userMembers;
          data.record.deptMember = res.deptMember;
        } else {
          data.record.members = [];
          data.record.deptMember = '';
        }
        data.record.examTime = [];
        if (data.record.startTime) {
          data.record.examTime.push(data.record.startTime);
        }
        if (data.record.endTime) {
          data.record.examTime.push(data.record.endTime);
        }
        setFieldsValue({
          ...data.record,
        });
      }
      id = data.record?.id || null;
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({ confirmLoading: true });
        if (values.examTime) {
          values.startTime = values.examTime[0];
          values.endTime = values.examTime[1];
          values.examTime = undefined;
        }
        let course = {
          id: ''
        };
        if (values.courseId) {
          course.id = values.courseId;
          values.course = course;
        }
        if (id) {
          await updateExamination(id, values);
        } else {
          await createExamination(values);
        }
        closeModal();
        emit('success');
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }

    function onSearch(value: string) {
      name.value = value;
    }

    return {
      searchParams,
      registerModal,
      registerForm,
      getTitle,
      handleSubmit,
      getSelectUserList,
      onSearch,
    };
  },
});
</script>
