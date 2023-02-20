<template>
  <div :class="prefixCls">
    <Popover title="" trigger="click" :overlayClassName="`${prefixCls}__overlay`">
      <Badge :count="count" dot :numberStyle="numberStyle">
        <BellOutlined />
      </Badge>
      <template #content>
        <Tabs>
          <template v-for="item in listData" :key="item.key">
            <TabPane>
              <template #tab>
                {{ item.name }}
                <span v-if="item.list.length !== 0">({{ item.list.length }})</span>
              </template>
              <NoticeList :list="item.list" @title-click="onNoticeClick" />
            </TabPane>
          </template>
        </Tabs>
      </template>
    </Popover>
    <NoticeModal @register="registerModal" />
  </div>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref, unref} from 'vue';
import { Popover, Tabs, Badge } from 'ant-design-vue';
import { BellOutlined } from '@ant-design/icons-vue';
import { tabListData, ListItem } from './data';
import NoticeList from './NoticeList.vue';
import { useDesign } from '/@/hooks/web/useDesign';
import {getUserMessageList, readMessage} from "/@/api/sys/message";
import {useUserStore} from "/@/store/modules/user";
import NoticeModal from './NoticeModal.vue';
import { useModal } from '/@/components/Modal';

  export default defineComponent({
    components: { Popover, BellOutlined, Tabs, TabPane: Tabs.TabPane, Badge, NoticeList, NoticeModal },
    setup() {
      const { prefixCls } = useDesign('header-notify');
      const listData = ref(tabListData);
      const count = ref<number>(0);
      const [registerModal, { openModal }] = useModal();

      async function onNoticeClick(record: ListItem) {
        if (!record.hasRead) {
          const userStore = useUserStore();
          const userInfo = userStore.getUserInfo;
          await readMessage({
            messageId: record.id,
            receiverId: unref(userInfo).id
          });
          record.titleDelete = true;
        }
        await openModal(true, {
          record
        });
      }

      async function fetch() {
        const userStore = useUserStore();
        const userInfo = userStore.getUserInfo;
        const userId = unref(userInfo).id;
        await fetchNotice(userId);
        await fetchTodo(userId);
      }

      async function fetchNotice(userId: string) {
        const result = await getUserMessageList({receiverId: userId, type: 0});
        if (result) {
          const {list, total} = result;
          count.value = total;
          listData.value[0].list = [];
          list.forEach(e => {
            listData.value[0].list.push({
              id: e.id,
              avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
              title: e.title,
              content: e.content,
              description: e.content,
              datetime: e.updateTime,
              updateTime: e.updateTime,
              hasRead: e.hasRead
            });
          });
        }
      }

      async function fetchTodo(userId: string) {
        const result = await getUserMessageList({receiverId: userId, type: 2});
        if (result) {
          const {list, total} = result;
          count.value = total;
          listData.value[1].list = [];
          list.forEach(e => {
            listData.value[1].list.push({
              id: e.id,
              avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
              title: e.title,
              content: e.content,
              description: e.content,
              datetime: e.updateTime,
              updateTime: e.updateTime,
              hasRead: e.hasRead
            });
          });
        }
      }

      onMounted(async () => {
        await fetch();
      });

      return {
        registerModal,
        openModal,
        prefixCls,
        listData,
        count,
        onNoticeClick,
        numberStyle: {},
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-header-notify';

  .@{prefix-cls} {
    padding-top: 2px;

    &__overlay {
      max-width: 400px;
      min-width: 380px;
    }

    .ant-tabs-content {
      width: 300px;
    }

    .ant-badge {
      font-size: 18px;

      .ant-badge-multiple-words {
        padding: 0 4px;
      }

      svg {
        width: 0.9em;
      }
    }
  }
</style>
