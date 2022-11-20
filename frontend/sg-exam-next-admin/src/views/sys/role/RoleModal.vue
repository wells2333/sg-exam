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
    const allCheckedMenuIds = ref<string[]>([]);
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
        // 角色已勾选的菜单，包含了父子节点
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
        setFieldsValue({
          ...data.record,
        });
      } else {
        checkedMenuIds.value = [];
        allCheckedMenuIds.value = [];
      }
    });

    const getTitle = computed(() => (!unref(isUpdate) ? '新增角色' : '编辑角色'));

    async function handleSubmit() {
      try {
        const values = await validate();
        if (allCheckedMenuIds.value.length > 0) {
          values.menuIds = allCheckedMenuIds.value.join(',');
        }
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
    function filterMenuIds(data, menuIds) {
      if (data.value.length > 0) {
        data.value.forEach(e => {
          handleFilterMenuIds(unref(data), unref(e), menuIds);
        });
      }
    }
    function handleFilterMenuIds(data, e, menuIds) {
      let includeAllChildren = undefined;
      if (e.children && e.children.length > 0) {
        includeAllChildren = isIncludeAll(menuIds, e.children);
        e.children.forEach(ee => {
          handleFilterMenuIds(unref(e), unref(ee), menuIds);
        });
      }
      // 删除这个ID
      if (includeAllChildren !== undefined && !includeAllChildren && menuIds.includes(e.id)) {
        menuIds.splice(menuIds.findIndex(item => item === e.id), 1);
      }
    }
    // 判断是否全部包含
    function isIncludeAll(menuIds, data) {
      let include = true;
      if (data) {
        data.forEach(e => {
          if (e.children !== undefined && e.children.length > 0) {
            include = false;
          }
          if (!menuIds.includes(e.id)) {
            include = false;
          }
        });
      }
      return include;
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
