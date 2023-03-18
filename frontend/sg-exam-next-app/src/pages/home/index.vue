<template>
  <view>
    <view class="banner-swiper-view">
      <nut-swiper :init-page="banners.length" :pagination-visible="true" pagination-color="#426543" auto-play="3000"
                  height="150">
        <nut-swiper-item v-for="(item, idx) in banners" :key="idx">
          <view @click="handleClickBanner(item)">
            <img :src="item.imageUrl" class="banner-swiper-image"/>
          </view>
        </nut-swiper-item>
      </nut-swiper>
    </view>
  </view>
  <view v-if="noticeValue !== undefined">
    <nut-noticebar :text="noticeValue"></nut-noticebar>
  </view>
  <view class="home-view">
    <view class="home-view-tips">热门功能</view>
    <nut-grid :column-num="3" square clickable>
      <nut-grid-item text="考试" @click="handleGridClickExam0">
        <IconFont font-class-name="iconfont" class-prefix="icon" name="kaoshi" size="30px" color="#FF9800"></IconFont>
      </nut-grid-item>
      <nut-grid-item text="课程" @click="handleGridClickCourse">
        <IconFont font-class-name="iconfont" class-prefix="icon" name="kecheng" size="30px" color="#5ab6ff"></IconFont>
      </nut-grid-item>
      <nut-grid-item text="练习" @click="handleGridClickExam1">
        <IconFont font-class-name="iconfont" class-prefix="icon" name="jiachang_lianxi" size="30px" color="#ff217c"></IconFont>
      </nut-grid-item>
      <nut-grid-item text="问卷" @click="handleGridClickExam2">
        <IconFont font-class-name="iconfont" class-prefix="icon" name="tiaochawenjuan" size="30px" color="#F44336"></IconFont>
      </nut-grid-item>
      <nut-grid-item text="面试" @click="handleGridClickExam3">
        <IconFont font-class-name="iconfont" class-prefix="icon" name="zhenrenmianshi" size="30px" color="#FF9800"></IconFont>
      </nut-grid-item>
      <nut-grid-item text="收藏" @click="handleGridClickFav">
        <IconFont font-class-name="iconfont" class-prefix="icon" name="shoucang" size="30px" color="red"></IconFont>
      </nut-grid-item>
    </nut-grid>
    <view class="home-view-tips">热门课程</view>
    <view class="popular-course-item box-show-item mb-bottom-20" v-for="course in courses">
      <course-item :item="course" @click="handleClickCourse" @fav="handleFav"></course-item>
    </view>
  </view>
</template>
<script lang="ts">
import Taro from '@tarojs/taro';
import {IconFont} from '@nutui/icons-vue-taro';
import {onMounted, ref} from 'vue';
import userApi from '../../api/user.api';
import examApi from '../../api/exam.api';
import operationApi from '../../api/operation.api';
import {shardMessage} from '../../constant/constant';
import {checkLogin} from "../../utils/filter";
import {CourseItem} from '../../components/course-item';
import {showLoading, hideLoading, successMessage} from '../../utils/util';

export default {
  components: {
    IconFont,
    'course-item': CourseItem
  },
  setup() {
    const current = ref<number>(0);
    let searchValue = ref<string>("");
    const banners = ref<any>([]);
    const courses = ref<any>([]);
    const noticeValue = ref<any>(undefined);

    async function fetchBanners() {
      banners.value = [];
      const res = await operationApi.bannerList({});
      const {code, result} = res;
      if (code == 0 && result && result.list && result.list.length > 0) {
        banners.value = [...result.list];
      }
    }

    async function fetchNotice() {
      const res = await userApi.getNotice();
      const {code, result} = res;
      if (code == 0 && result) {
        noticeValue.value = result;
      }
    }

    function handleSearchChange(value) {
      searchValue.value = value;
    }

    function handleSearch() {
    }

    async function fetchPopularCourses() {
      courses.value = [];
      const res = await examApi.popularCourses();
      const {code, result} = res;
      if (code === 0) {
        courses.value = result;
      }
    }

    function handleGridClickExam0() {
      Taro.navigateTo({url: "/pages/exam_pages/exam/index?active=0"});
    }

    function handleGridClickExam1() {
      Taro.navigateTo({url: "/pages/exam_pages/exam/index?active=1"});
    }

    function handleGridClickExam2() {
      Taro.navigateTo({url: "/pages/exam_pages/exam/index?active=2"})
    }

    function handleGridClickExam3() {
      Taro.navigateTo({url: "/pages/exam_pages/exam/index?active=3"})
    }

    function handleGridClickCourse() {
      Taro.navigateTo({url: "/pages/exam_pages/course/index"});
    }

    function handleGridClickFav() {
      Taro.navigateTo({url: "/pages/exam_pages/favorite/index"});
    }

    function handleClickCourse(course) {
      Taro.navigateTo({url: "/pages/exam_pages/course_detail/index?courseId=" + course.id})
    }

    function handleClickBanner(item) {
      const {redirectUrl} = item;
      if (redirectUrl === null || redirectUrl === '') {
        return;
      }
      if (redirectUrl.startsWith('http')) {
        Taro.navigateTo({url: "/pages/exam_pages/webview/index?url=" + redirectUrl})
      } else {
        Taro.navigateTo({url: redirectUrl});
      }
    }

    function handleFav(item) {
      const text = item.favorite ? '收藏' : '取消收藏';
      successMessage(text + '成功');
    }

    async function init() {
      try {
        await showLoading();
        await fetchBanners();
        await fetchNotice();
        await fetchPopularCourses();
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });

    return {
      noticeValue,
      current,
      searchValue,
      courses,
      banners,
      init,
      handleSearchChange,
      handleSearch,
      handleGridClickExam0,
      handleGridClickExam1,
      handleGridClickExam2,
      handleGridClickExam3,
      handleGridClickCourse,
      handleClickCourse,
      handleGridClickFav,
      handleClickBanner,
      handleFav,
      fetchPopularCourses
    };
  },
  onLoad() {
    checkLogin();
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  onShareAppMessage() {
    return shardMessage;
  },
  async onShareTimeline() {
    return shardMessage;
  }
}
</script>

<style>
.popular-course-item {
  background: white;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}
</style>