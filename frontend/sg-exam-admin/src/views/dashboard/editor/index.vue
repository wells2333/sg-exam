<template>
  <div class="dashboard-user-container">
    <div class="clearfix">
      <div class="info-container">
        <el-row :gutter="8">
          <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
            <todo-list/>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import TodoList from './components/TodoList'

export default {
  name: 'DashboardEditor',
  components: {
    TodoList
  },
  data () {
    return {}
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles'
    ])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .emptyGif {
    display: block;
    width: 45%;
    margin: 0 auto;
  }

  .dashboard-user-container {
    background-color: #e3e3e3;
    min-height: 100vh;
    padding: 50px 60px 0px;
    .info-container {
      position: relative;
      margin-left: 190px;
      height: 150px;
      line-height: 200px;
      .display-name {
        font-size: 28px;
        line-height: 28px;
        color: #212121;
        position: absolute;
        top: 25px;
      }
    }
  }
</style>
