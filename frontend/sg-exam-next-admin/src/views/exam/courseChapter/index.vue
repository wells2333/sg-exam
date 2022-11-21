<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button v-if="hasPermission(['exam:course:edit'])" type="primary" @click="handleCreate"> 新增
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:eye-outlined',
              tooltip: '查看详情',
              onClick: handleView.bind(null, record),
              auth: 'exam:exam:view'
            },
            {
              icon: 'clarity:note-edit-line',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
              auth: 'exam:exam:edit'
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: '删除',
              color: 'error',
              auth: 'exam:exam:del',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <CourseChapterModal width="80%" @register="registerModal" @success="handleSuccess"/>
  </div>
</template>

<script>
import {defineComponent, ref} from 'vue';
import {Divider} from 'ant-design-vue';
import {useRoute} from 'vue-router';
import {PageWrapper} from '/@/components/Page';
import {useGo} from '/@/hooks/web/usePage';
import {getChapterList} from '/@/api/exam/chapter';
import {Description} from '/@/components/Description/index';
import {BasicTable, TableAction, useTable} from '/@/components/Table';
import {answerColumns, scoreDetailSchema} from './chapter.data';
import {useMessage} from "/@/hooks/web/useMessage";
import CourseChapterModal from './CourseChapterModal.vue';
import {useModal} from '/@/components/Modal';
import {usePermission} from '/@/hooks/web/usePermission';
import {searchFormSchema} from "../course/course.data";

export default defineComponent({
  name: 'CourseChapter',
  components: {
    PageWrapper,
    Description,
    BasicTable,
    TableAction,
    [Divider.name]: Divider,
    CourseChapterModal
  },
  setup() {
    const {hasPermission} = usePermission();
    const [registerModal, {openModal}] = useModal();
    const route = useRoute();
    const go = useGo();
    const {createMessage} = useMessage();
    const recordId = ref(route.params?.id);
    const currentKey = ref('detail');
    const score = ref({});
    const [registerTable, {reload}] = useTable({
      title: '章列表',
      api: getChapterList,
      columns: answerColumns,
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

    function handleView(record) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }

    return {
      hasPermission,
      registerModal,
      scoreDetailSchema,
      score,
      recordId,
      currentKey,
      registerTable,
      handleView
    };
  },
});
</script>

<style></style>
