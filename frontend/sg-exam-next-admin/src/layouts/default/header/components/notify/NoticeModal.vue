<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" width="38%" @ok="handleOk">
    <div v-if="record !== undefined">
      <h2>{{record.title}}</h2>
      <p class="message-update-time">{{record.updateTime}}</p>
      <p>{{record.content}}</p>
    </div>
  </BasicModal>
</template>
<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
export default defineComponent({
  components: { BasicModal },
  setup() {
    const record = ref<any>(undefined);

    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      record.value = data.record;
      setModalProps({ confirmLoading: false });
    });
    const getTitle = computed(() => '查看');

    function handleOk() {
      closeModal();
    }
    return {
      record,
      getTitle,
      registerModal,
      handleOk
    };
  },
});
</script>

<style>
.message-update-time {
  color: gray;
  font-size: 12px;
}
</style>
