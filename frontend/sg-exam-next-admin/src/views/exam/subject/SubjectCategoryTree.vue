<template>
  <div class="m-4 mr-0 overflow-hidden bg-white">
    <BasicTree
      title="题目分类"
      toolbar
      :clickRowToExpand="false"
      :treeData="treeData"
      :replaceFields="{ key: 'id', title: 'categoryName' }"
      @select="handleSelect"
    />
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { BasicTree, TreeItem } from '/@/components/Tree';
import { getSubjectCategoryTree } from '/@/api/exam/subjectCategory';

export default defineComponent({
  name: 'SubjectCategoryTree',
  components: { BasicTree },

  emits: ['select'],
  setup(_, { emit }) {
    const treeData = ref<TreeItem[]>([]);
    async function fetch() {
      treeData.value = (await getSubjectCategoryTree()) as unknown as TreeItem[];
    }

    function handleSelect(keys) {
      emit('select', keys[0]);
    }

    onMounted(() => {
      fetch();
    });
    return { treeData, handleSelect };
  },
});
</script>
