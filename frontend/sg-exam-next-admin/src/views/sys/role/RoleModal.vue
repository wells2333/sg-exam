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
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './role.data';
import { BasicModal, useModalInner } from '/@/components/Modal';
import {BasicTree, TreeItem} from "/@/components/Tree";
import {getMenuList} from "/@/api/sys/menu";
import {createRole, getRoleMenus, updateRole} from "/@/api/sys/role";
export default defineComponent({
  name: 'RoleModal',
  components: { BasicModal, BasicForm, BasicTree },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const treeData = ref<TreeItem[]>([]);
    const checkedMenuIds = ref<string[]>([]);
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 90,
      schemas: formSchema,
      showActionButtonGroup: false,
    });

    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      setModalProps({ confirmLoading: false });
      if (unref(treeData).length === 0) {
        treeData.value = (await getMenuList()) as any as TreeItem[];
      }
      isUpdate.value = !!data?.isUpdate;
      id = data.record?.id || null;
      if (unref(isUpdate)) {
        const roleMenus = (await getRoleMenus(id)) as any;
        let menuIds = [];
        if (roleMenus) {
          roleMenus.forEach(menu => {
            menuIds.push(menu.menuId);
          });
        }
        checkedMenuIds.value = menuIds;
        setFieldsValue({
          ...data.record,
        });
      } else {
        checkedMenuIds.value = [];
      }
    });

    const getTitle = computed(() => (!unref(isUpdate) ? '新增角色' : '编辑角色'));

    async function handleSubmit() {
      try {
        const values = await validate();
        values.menuIds = checkedMenuIds.value.join(',');
        setModalProps({ confirmLoading: true });
        if (id) {
          await updateRole(id, values);
        } else {
          await createRole(values);
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
      menuIds: checkedMenuIds,
    };
  },
});
</script>
