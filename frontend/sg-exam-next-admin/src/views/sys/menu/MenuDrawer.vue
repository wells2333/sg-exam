<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="50%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
import { defineComponent, ref, computed, unref } from 'vue';
import { BasicForm, useForm } from '/@/components/Form/index';
import { formSchema } from './menu.data';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';

import { getMenuList, createMenu, updateMenu  } from '/@/api/sys/menu';

export default defineComponent({
  name: 'MenuDrawer',
  components: { BasicDrawer, BasicForm },
  emits: ['success', 'register'],
  setup(_, { emit }) {
    const isUpdate = ref(true);
    let id: string;
    const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
      labelWidth: 100,
      schemas: formSchema,
      showActionButtonGroup: false,
      baseColProps: { lg: 12, md: 24 },
    });

    const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
      resetFields();
      setDrawerProps({ confirmLoading: false });
      isUpdate.value = !!data?.isUpdate;

      if (unref(isUpdate)) {
        setFieldsValue({
          ...data.record,
        });
      }
      id = data.record?.id || null;
      const treeData = await getMenuList();
      updateSchema({
        field: 'parentId',
        componentProps: { treeData },
      });
    });

    const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));

    async function handleSubmit() {
      try {
        const values = await validate();
        setDrawerProps({ confirmLoading: true });
        if (id) {
          await updateMenu(id, values);
        } else {
          await createMenu(values);
        }
        closeDrawer();
        emit('success');
      } finally {
        setDrawerProps({ confirmLoading: false });
      }
    }

    return { registerDrawer, registerForm, getTitle, handleSubmit };
  },
});
</script>
