<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" width="70%">
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
import { useI18n } from '/@/hooks/web/useI18n';
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm, ApiSelect } from '/@/components/Form/index';
import { formSchema } from './course.data';
import { getDeptList } from '/@/api/sys/dept';
import { getCourseMembers, createCourse, updateCourse} from "/@/api/exam/course";
import { getSelectDeptList, getSelectUserList } from "/@/api/sys/select";

export default defineComponent({
  name: 'CourseModal',
  components: { BasicModal, BasicForm, ApiSelect },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const { t } = useI18n();
    const isUpdate = ref(true);
    let id: string;
    const name = ref<string>('');
    const searchParams = computed<Recordable>(() => {
      return {name: unref(name)};
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
      if (unref(isUpdate)) {
        const res = await getCourseMembers(data.record?.id);
        if (res) {
          data.record.members = res.userMembers;
          data.record.deptMember = res.deptMember;
        } else {
          data.record.members = [];
          data.record.deptMember = '';
        }
        setFieldsValue({
          ...data.record,
        });
      }
      id = data.record?.id || null;

      const deptMemberTreeData = await getSelectDeptList();
      const treeData = await getDeptList();
      updateSchema([
        {
          field: 'deptMember',
          componentProps: {treeData: deptMemberTreeData},
        },
        {
          field: 'parentDept',
          componentProps: { treeData },
        },
      ]);
    });
    const getTitle = computed(() => (!unref(isUpdate) ? t('common.addText') :
      t('common.editText')));

    async function handleSubmit() {
      try {
        const values = await validate();
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateCourse(id, values);
        } else {
          await createCourse(values);
        }
        closeModal();
        emit('success', isUpdate);
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }


    function onSearch(value: string) {
      name.value = value;
    }

    return { searchParams, registerModal, registerForm, getTitle, handleSubmit, getSelectUserList, onSearch };
  },
});
</script>
