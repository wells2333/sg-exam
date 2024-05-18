<template>
  <div class="md:flex">
    <template v-for="(item, index) in cardList" :key="item.title">
      <Card
        size="small"
        :loading="loading"
        :title="item.title"
        class="md:w-1/4 w-full !md:mt-0 !mt-4"
        :class="[index + 1 < 4 && '!md:mr-4']"
        :canExpan="false"
      >
        <template #extra>
          <Tag :color="item.color">{{ item.action }}</Tag>
        </template>

        <div class="py-4 px-4 flex justify-between">
          <CountTo :startVal="0" :endVal="item.value" class="text-2xl" />
          <Icon :icon="item.icon" :size="40" />
        </div>

        <div class="p-2 px-4 flex justify-between">
          <span>总{{ item.title }}</span>
          <CountTo prefix="" :startVal="1" :endVal="item.total" />
        </div>
      </Card>
    </template>
  </div>
</template>
<script lang="ts">
import { CountTo } from '/@/components/CountTo';
import { Icon } from '/@/components/Icon';
import { Tag, Card } from 'ant-design-vue';
import { generateDashboardCard, GrowCardItem } from '../data';
import {defineComponent, onMounted} from "vue";
import { getDashboardData } from "/@/api/sys/dashboard";

export default defineComponent({
  name: 'DashboardCard',
  components: { CountTo, Icon, Tag, Card},
  props: {
    loading: {
      type: Boolean,
    }
  },
  setup() {
    const cardList: GrowCardItem[] = [];
    async function fetch() {
      const result = await getDashboardData();
      const {
        examUserNumber,
        examinationNumber,
        examinationRecordNumber,
        tenantCount
      } = result;
      const userNumber = Number(examUserNumber);
      const examNumber = Number(examinationNumber);
      const recordNumber = Number(examinationRecordNumber);
      const tenantNumber = Number(tenantCount);

      cardList.push(generateDashboardCard('单位数', 'visit-count|svg', tenantNumber,tenantNumber,'green', '月'));
      cardList.push(generateDashboardCard('用户数', 'total-sales|svg', userNumber,userNumber,'blue', '月'));
      cardList.push(generateDashboardCard('考试数', 'download-count|svg', examNumber,examNumber,'green', '周'));
      cardList.push(generateDashboardCard('考试次数', 'transaction|svg', recordNumber,recordNumber,'green', '年'));
    }
    onMounted(() => {
      fetch();
    });
    return {
      cardList
    }
  }
});
</script>
