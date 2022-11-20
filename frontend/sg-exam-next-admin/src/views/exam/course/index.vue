<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:add'])" type="primary" @click="handleCreate"> 新增课程 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:upload-outlined',
              tooltip: '上传图片',
              onClick: handleUpload.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
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
    <CourseModal @register="registerModal" @success="handleSuccess" />
    <CourseImageModal @register="registerImageModal" @success="handleUploadSuccess"/>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { getCourseList, deleteCourse } from '/@/api/exam/course';
import { useModal } from '/@/components/Modal';
import CourseModal from './CourseModal.vue';
import CourseImageModal from "./CourseImageModal.vue";
import { columns, searchFormSchema } from './course.data';
import { usePermission } from '/@/hooks/web/usePermission';

export default defineComponent({
  name: 'CourseManagement',
  components: { BasicTable, CourseModal, CourseImageModal, TableAction },
  setup() {
    const { hasPermission } = usePermission();
    const [registerModal, { openModal }] = useModal();
    const [registerImageModal, { openModal: openImageModal }] = useModal();
    const [registerTable, { reload }] = useTable({
      title: '课程列表',
      api: getCourseList,
      columns,
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
        slots: { customRender: 'action' },
        fixed: undefined,
      },
    });

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
    function handleUpload(record: Recordable) {
      openImageModal(true, record);
    }
    async function handleDelete(record: Recordable) {
      await deleteCourse(record.id);
      await reload();
    }
    function handleSuccess() {
      reload();
    }
    function handleUploadSuccess() {
      reload();
    }
    return {
      hasPermission,
      registerTable,
      registerModal,
      registerImageModal,
      handleCreate,
      handleEdit,
      handleUpload,
      handleDelete,
      handleSuccess,
      handleUploadSuccess,
    };
  },
});
</script>
