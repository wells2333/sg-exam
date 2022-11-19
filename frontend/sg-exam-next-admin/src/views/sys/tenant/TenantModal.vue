<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" width="60%" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #menu>
        <BasicTree
          v-model:value="menuIds"
          :treeData="treeData"
          :replaceFields="{ title: 'name', key: 'id' }"
          checkable
          toolbar
          title="菜单分配"
        />
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import {BasicTree, TreeItem} from "/@/components/Tree";
import { formSchema } from './tenant.data';
import { createTenant, updateTenant } from '/@/api/sys/tenant';
import {defaultTenantMenu} from "/@/api/sys/menu";
import {getRoleMenus} from "/@/api/sys/role";

export default defineComponent({
  name: 'TenantModal',
  components: { BasicModal, BasicForm,BasicTree },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const treeData = ref<TreeItem[]>([]);
    const checkedMenuIds = ref<string[]>([]);
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      if (unref(treeData).length === 0) {
        treeData.value = (await defaultTenantMenu()) as any as TreeItem[];
      }
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
        let menuIds = [];
        if (data.record.roleId) {
          const roleMenus = (await getRoleMenus(data.record.roleId)) as any;
          if (roleMenus) {
            roleMenus.forEach(menu => {
              menuIds.push(menu.menuId);
            });
          }
        }
        checkedMenuIds.value = menuIds;
        setFieldsValue({
          ...data.record,
        });
      } else {
        checkedMenuIds.value = [];
      }
      id = data.record?.id || null;
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增租户' : '编辑租户'));
    async function handleSubmit() {
      try {
        const values = await validate();
        values.menuIds = checkedMenuIds.value.join(',');
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateTenant(id, values);
        } else {
          await createTenant(values);
        }
        closeModal();
        emit('success');
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }
    return {
      registerModal,
      registerForm,
      getTitle,
      handleSubmit,
      treeData,
      menuIds: checkedMenuIds
    };
  },
});
</script>
