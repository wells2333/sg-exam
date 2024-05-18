<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="{ type: 'checkbox' }">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:add'])" type="primary" @click="handleCreate">
          {{ t('common.addText') }}
        </a-button>
        <PopConfirmButton
          v-if="hasPermission(['exam:course:del'])"
          title="确定删除么？"
          okText="确认"
          cancelText="取消"
          @confirm="handleDelete"
          color="error"
        >删除
        </PopConfirmButton>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
             {
              icon: 'ant-design:upload-outlined',
              tooltip: '修改发布状态',
              auth: 'exam:course:edit',
              popConfirm: {
                title: '确定修改发布状态吗？',
                confirm: handlePublic.bind(null, record),
              },
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: t('common.editText'),
              onClick: handleEdit.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:align-right-outlined',
              tooltip: '章节管理',
              onClick: handleEditChapter.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:user-outlined',
              tooltip: '学员管理',
              onClick: handleEditMembers.bind(null, record),
              auth: 'exam:course:edit'
            },
            {
              icon: 'ant-design:like-outlined',
              tooltip: '评价管理',
              onClick: handleEditEvaluate.bind(null, record),
              auth: 'exam:course:edit'
            },
          ]"
        />
      </template>
    </BasicTable>
    <CourseModal @register="registerModal" @success="handleSuccess"/>
    <CourseImageModal @register="registerImageModal" @success="handleUploadSuccess"/>
    <EvaluateModal @register="registerEvaluateModal"/>
    <MemberModal @register="registerMemberModal"></MemberModal>
  </div>
</template>
<script lang="ts">
import {defineComponent, unref} from 'vue';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {getCourseList, deleteBatchCourse, updateCourse} from '/@/api/exam/course';
import {useModal} from '/@/components/Modal';
import CourseModal from './CourseModal.vue';
import CourseImageModal from './CourseImageModal.vue';
import EvaluateModal from './EvaluateModal.vue';
import MemberModal from './MemberModal.vue';
import {columns, searchFormSchema} from './course.data';
import {useI18n} from '/@/hooks/web/useI18n';
import {usePermission} from '/@/hooks/web/usePermission';
import {useGo} from "/@/hooks/web/usePage";
import {useMessage} from "/@/hooks/web/useMessage";
import {PopConfirmButton} from "/@/components/Button";

export default defineComponent({
  name: 'CourseManagement',
  components: {
    PopConfirmButton,
    BasicTable, CourseModal, CourseImageModal, EvaluateModal, MemberModal, TableAction},
  setup() {
    const {t} = useI18n();
    const {hasPermission} = usePermission();
    const {createMessage} = useMessage();
    const [registerModal, {openModal}] = useModal();
    const [registerImageModal] = useModal();
    const [registerEvaluateModal, {openModal: openEvaluateModal}] = useModal();
    const [registerMemberModal, {openModal: openMemberModal}] = useModal();
    const go = useGo();
    const [registerTable, {reload, getSelectRows, clearSelectedRowKeys}] = useTable({
      title: t('common.modules.exam.course') + t('common.list'),
      api: getCourseList,
      columns,
      formConfig: {
        labelWidth: 120,
        schemas: searchFormSchema,
      },
      pagination: true,
      striped: false,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      canResize: false,
      actionColumn: {
        width: 200,
        title: t('common.operationText'),
        dataIndex: 'action',
        slots: {customRender: 'action'},
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

    function handleEditChapter(record: Recordable) {
      go('/exam/course_chapter/' + record.id);
    }

    function handleEditEvaluate(record: Recordable) {
      openEvaluateModal(true, {
        record,
        isUpdate: true,
        courseId: record.id
      });
    }

    function handleEditMembers(record: Recordable) {
      openMemberModal(true, {
        record,
        isUpdate: true,
        courseId: record.id
      });
    }

    async function handleDelete() {
      const ids = getSelectedRowIds();
      if (!ids || ids.length === 0) {
        return;
      }

      await deleteBatchCourse(ids);
      createMessage.success(t('common.operationSuccessText'));
      clearSelectedRowKeys();
      await reload();
    }

    function handleSuccess(isUpdate) {
      let msg = t('common.operationSuccessText');
      if (isUpdate && !unref(isUpdate)) {
        msg = '新增成功，状态未未发布，需要发布后才生效';
      }
      createMessage.success(msg);
      reload();
    }

    async function handlePublic(record: Recordable) {
      const data = {...record}
      if (data.courseStatus === 0) {
        data.courseStatus = 1;
      } else {
        data.courseStatus = 0;
      }

      await updateCourse(data.id, data);
      createMessage.success(t('common.operationSuccessText'));
      await reload();
    }

    function handleUploadSuccess() {
      reload();
    }

    function getSelectedRowIds() {
      const rows = getSelectRows();
      if (!rows || rows.length === 0) {
        return undefined;
      }

      const ids = [];
      rows.forEach(e => {
        ids.push(e.id);
      });
      return ids;
    }

    return {
      t,
      hasPermission,
      registerTable,
      registerModal,
      registerImageModal,
      registerEvaluateModal,
      registerMemberModal,
      handleCreate,
      handleEdit,
      handleEditChapter,
      handleEditEvaluate,
      handleEditMembers,
      handleDelete,
      handleSuccess,
      handlePublic,
      handleUploadSuccess,
    };
  },
});
</script>
