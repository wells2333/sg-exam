<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:edit'])" type="primary" @click="handleCreate">
          新增
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:align-right-outlined',
              tooltip: '节管理',
              onClick: handleSectionManage.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: '删除',
              color: 'error',
              auth: 'exam:course:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <ChapterModal width="90%" @register="registerModal" @success="handleSuccess"/>
    <SectionModal width="90%" @register="registerSectionModal"
                  @success="handleSuccess"></SectionModal>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref, unref} from 'vue';
import {Divider} from 'ant-design-vue';
import {useRoute} from 'vue-router';
import {PageWrapper} from '/@/components/Page';
import {deleteChapter, getChapterList} from '/@/api/exam/chapter';
import {Description} from '/@/components/Description/index';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {columns, searchFormSchema} from './chapter/chapter.data';
import ChapterModal from './chapter/ChapterModal.vue';
import SectionModal from './section/SectionModal.vue';
import {useModal} from '/@/components/Modal';
import {usePermission} from '/@/hooks/web/usePermission';
import {useMessage} from "/@/hooks/web/useMessage";

export default defineComponent({
  name: 'CourseChapter',
  components: {
    PageWrapper,
    Description,
    BasicTable,
    TableAction,
    [Divider.name]: Divider,
    ChapterModal,
    SectionModal
  },
  setup() {
    const {hasPermission} = usePermission();
    const { createMessage } = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerSectionModal, {openModal: openSectionModal}] = useModal();
    const route = useRoute();
    const courseId = ref(route.params?.id);
    const [registerTable, {reload}] = useTable({
      title: '章列表',
      api: (arg) => {
        const params = {courseId: unref(courseId)};
        Object.assign(params, arg);
        return getChapterList(params);
      },
      columns: columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: false,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 120,
        title: '操作',
        dataIndex: 'action',
        slots: {customRender: 'action'},
        fixed: undefined,
      },
    });

    function handleView(record) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    function handleCreate() {
      openModal(true, {
        isUpdate: false,
      });
    }

    function handleEdit(record: Recordable) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    async function handleDelete(record: Recordable) {
      await deleteChapter(record.id);
      createMessage.success('操作成功');
      await reload();
    }

    function handleSuccess() {
      createMessage.success('操作成功');
      reload();
    }

    function handleSectionManage(record) {
      openSectionModal(true, {
        record,
        isUpdate: true,
      });
    }

    return {
      hasPermission,
      registerModal,
      registerSectionModal,
      registerTable,
      handleView,
      handleCreate,
      handleEdit,
      handleDelete,
      handleSuccess,
      handleSectionManage
    };
  },
});
</script>

<style lang="less">
.ant-modal-wrap .ant-modal {
  top: 20px;
}
</style>
