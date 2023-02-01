<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" width="60%" @ok="handleSubmit">
    <BasicForm @register="registerForm">
      <template #menu>
        <BasicTree
          v-model:value="menuIds"
          :treeData="treeData"
          :replaceFields="{ title: 'name', key: 'id' }"
          checkable
          @check="handleSelectChange"
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
import {filterMenuIds} from "/@/utils/menuUtil";

export default defineComponent({
  name: 'TenantModal',
  components: { BasicModal, BasicForm,BasicTree },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const treeData = ref<TreeItem[]>([]);
    const checkedMenuIds = ref<string[]>([]);
    const allCheckedMenuIds = ref<string[]>([]);
    const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
    });
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      resetFields();
      checkedMenuIds.value = [];
      setModalProps({ confirmLoading: false });
      if (unref(treeData).length === 0) {
        treeData.value = (await defaultTenantMenu()) as any as TreeItem[];
      }
      isUpdate.value = !!data?.isUpdate;
      if (unref(isUpdate)) {
        if (data.record.roleId) {
          const roleMenus = (await getRoleMenus(data.record.roleId)) as any;
          let roleAllMenuIds = [];
          if (roleMenus) {
            roleMenus.forEach(menu => {
              roleAllMenuIds.push(menu.menuId);
            });
          }
          // 完整菜单树的全部菜单ID
          let menuIds = [...roleAllMenuIds];
          // 过滤处理掉不完全勾选的父节点ID
          filterMenuIds(treeData, menuIds);
          checkedMenuIds.value = menuIds;
          allCheckedMenuIds.value = [...roleAllMenuIds];
        }
        setFieldsValue({
          ...data.record,
        });
      } else {
        checkedMenuIds.value = [];
        allCheckedMenuIds.value = [];
      }
      id = data.record?.id || null;
    });
    const getTitle = computed(() => (!unref(isUpdate) ? '新增单位' : '编辑单位'));

    async function handleSubmit() {
      try {
        const values = await validate();
        if (allCheckedMenuIds.value.length > 0) {
          values.menuIds = allCheckedMenuIds.value.join(',');
        }
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

    function handleSelectChange(selectedKeys, info, allSelectKeys) {
      allCheckedMenuIds.value = [...allSelectKeys];
    }

    return {
      registerModal,
      registerForm,
      getTitle,
      handleSubmit,
      treeData,
      menuIds: checkedMenuIds,
      handleSelectChange
    };
  },
});
</script>
