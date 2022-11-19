<template>
  <ScrollContainer>
    <div ref="wrapperRef" :class="prefixCls">
      <component is="BaseSetting" />
    </div>
  </ScrollContainer>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { Tabs } from 'ant-design-vue';

  import { ScrollContainer } from '/@/components/Container';
  import { settingList } from './data';

  import BaseSetting from './BaseSetting.vue';

  export default defineComponent({
    name: "PersonalDetails",
    components: {
      ScrollContainer,
      Tabs,
      TabPane: Tabs.TabPane,
      BaseSetting,
    },
    setup() {
      return {
        prefixCls: 'account-setting',
        settingList,
        tabBarStyle: {
          width: '220px',
        },
      };
    },
  });
</script>
<style lang="less">
  .account-setting {
    margin: 12px;
    background-color: @component-background;

    .base-title {
      padding-left: 0;
    }

    .ant-tabs-tab-active {
      background-color: @item-active-bg;
    }
  }
</style>
