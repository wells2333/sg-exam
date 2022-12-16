<template>
  <view>
    <at-search-bar :value="searchValue" @action-click="handleSearch" @change="handleSearchChange" placeholder="搜索"/>
    <view class="course-list">
      <view class="box-show-item mb-bottom-20" v-if="courseList !== undefined" v-for="course in courseList">
        <course-item :item="course" @click="handleClickCourse(course)"></course-item>
      </view>
    </view>
  </view>
</template>

<script lang="ts">
import Taro from "@tarojs/taro";
import {onMounted, ref} from 'vue';
import examApi from '../../api/exam.api';
import {CourseItem} from '../../components/course-item';
import {filterLogin} from "../../utils/filter";

export default {
  components: {
    'course-item': CourseItem
  },
  setup() {
    let searchValue = ref<string>("");
    const courseList = ref<any>(undefined);

    async function fetch(courseName: string = '') {
      const res = await examApi.courseList({courseName});
      const {code, result} = res;
      if (code === 0) {
        courseList.value = result.list;
      }
    }

    function handleSearch() {
      fetch(searchValue.value);
    }

    function handleSearchChange(value) {
      searchValue.value = value;
      if (value === '') {
        fetch();
      }
    }

    function handleClickCourse(course) {
      Taro.navigateTo({url: "/pages/course_detail/index?courseId=" + course.id})
    }

    onMounted(() => {
      fetch();
    });
    return {searchValue, courseList, handleSearch, handleSearchChange, handleClickCourse}
  }
}
</script>

<style>
.course-list {
  margin-top: 8px;
  margin-left: 8px;
  margin-right: 8px;
}
</style>