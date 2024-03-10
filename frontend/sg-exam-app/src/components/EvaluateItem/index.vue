<template>
    <div class="user-evaluate-item" v-if="item">
        <el-row class="user-evaluate-item-bg">
            <el-col :span="2">
                <img width="40" v-if="item.avatarUrl" height="40" class="user-evaluate-item-avatar" :src="item.avatarUrl">
                <i class="iconfont icon-user" v-else style="font-size: 40px; color: #5a5a5a;"></i>
            </el-col>
            <el-col :span="22">
                <div class="user-evaluate-item-top">
                    <span style="color: #333; margin-right: 15px;">{{ item.operatorName }}</span>
                    <el-rate v-model="item.evaluateLevel" :disabled="true" style="height: 100%; line-height: initial;"></el-rate>
                </div>
                <div class="user-evaluate-item-content" style="color:#666;">
                    {{ item.evaluateContent }}
                </div>
                <div class="user-evaluate-item-time">
                    {{ item.createTime }}
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
  name: 'EvaluateItem',
  props: {
    item: {
      type: Object,
      required: true
    }
  }
}
</script>

<style lang="scss" scoped>
.user-evaluate-item {
  margin-top: 18px;
  .user-evaluate-item-bg {
    border-bottom: 1px solid rgba(233,233,233,.6);
    padding-bottom: 12px;
  }
  .user-evaluate-item-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }
  .user-evaluate-item-top{
    font-size: 13px;
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 18px;
  }
}
.user-evaluate-item-content {
  margin-top: 4px;
}
.user-evaluate-item-time {
  font-size: 12px;
  margin-top: 2px;
  color: #999;
}
</style>
