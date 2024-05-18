<template>
  <div class="m-4 mr-0 overflow-hidden bg-white">
    <BasicTree
      ref="basicTreeRef"
      :title="t('common.modules.exam.category')"
      toolbar
      :clickRowToExpand="false"
      :treeData="treeData"
      :replaceFields="{ key: 'id', title: 'categoryName' }"
      @select="handleSelect"
    />
  </div>
</template>
<script lang="ts">
import {useI18n} from '/@/hooks/web/useI18n';
import {defineComponent, onMounted, ref, unref} from 'vue';
import {BasicTree, TreeItem} from '/@/components/Tree';
import {getSubjectCategoryTree} from '/@/api/exam/subjectCategory';

export default defineComponent({
  name: 'SubjectCategoryTree',
  components: {BasicTree},
  emits: ['select'],
  setup(_, {emit}) {
    const {t} = useI18n();
    const treeData = ref<TreeItem[]>([]);
    const basicTreeRef = ref<any>(undefined);

    async function fetch() {
      treeData.value = (await getSubjectCategoryTree()) as unknown as TreeItem[];
      if (treeData.value.length > 0) {
        handleSelect(treeData.value[0].id)
      }
    }

    function handleSelect(keys) {
      emit('select', keys[0]);
    }

    function resetSelectedKeys() {
      const obj = unref(basicTreeRef);
      if (obj) {
        obj.setSelectedKeys([]);
      }
    }

    onMounted(() => {
      fetch();
    });

    function reloadTree() {
      fetch();
    }

    return {t, basicTreeRef, treeData, handleSelect, resetSelectedKeys, reloadTree};
  },
});
</script>
