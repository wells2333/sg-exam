<template>
  <AtMessage/>
  <view>
    <view class="banner-swiper-view">
      <swiper
          class='banner-swiper'
          indicatorColor='#999'
          indicatorActiveColor='#333'
          current='current'
          duration='500'
          interval='5000'
          circular='false'
          autoplay='false'
          indicatorDots='true'>
        <swiper-item v-for="(item, idx) in banners" :key="idx">
          <view @click="handleClickBanner(item)">
            <image :src="item.imageUrl" class="banner-swiper-image"/>
          </view>
        </swiper-item>
      </swiper>
    </view>
  </view>
  <view v-if="noticeValue !== undefined">
    <AtNoticebar icon='volume-plus' marquee>
      {{noticeValue}}
    </AtNoticebar>
  </view>
  <view class="home-view">
    <view class="home-view-tips">常用功能</view>
    <AtGrid @click="handleGridClick" :data="homeGridData"/>
    <view class="home-view-tips">热门课程</view>
    <view class="popular-course-item box-show-item mb-bottom-20" v-for="course in courses">
      <course-item :item="course" @click="handleClickCourse" @fav="handleFav"></course-item>
    </view>
  </view>
</template>
<script lang="ts">
import {onMounted, ref} from 'vue';
import userApi from '../../api/user.api';
import examApi from '../../api/exam.api';
import operationApi from '../../api/operation.api';
import Taro from "@tarojs/taro";
import {shardMessage} from '../../constant/constant';
import {checkLogin} from "../../utils/filter";
import {CourseItem} from '../../components/course-item';
import {showLoading, hideLoading, successMessage} from '../../utils/util';

export default {
  components: {
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

    function handleGridClick(item) {
      if (item.value === '课程') {
        Taro.navigateTo({url: "/pages/course/index"})
      } else if (item.value === '考试') {
        Taro.navigateTo({url: "/pages/exam/index?active=0"})
      } else if (item.value === '练习') {
        Taro.navigateTo({url: "/pages/exam/index?active=1"})
      } else if (item.value === '问卷') {
        Taro.navigateTo({url: "/pages/exam/index?active=2"})
      } else if (item.value === '面试') {
        Taro.navigateTo({url: "/pages/exam/index?active=3"})
      } else if (item.value === '收藏') {
        Taro.navigateTo({url: "/pages/favorite/index"})
      }
    }

    const homeGridData = [
      {
        image: 'https://img20.360buyimg.com/jdphoto/s72x72_jfs/t15151/308/1012305375/2300/536ee6ef/5a411466N040a074b.png',
        value: '考试'
      },
      {
        image: 'https://img12.360buyimg.com/jdphoto/s72x72_jfs/t6160/14/2008729947/2754/7d512a86/595c3aeeNa89ddf71.png',
        value: '课程'
      },
      {
        image: 'https://img10.360buyimg.com/jdphoto/s72x72_jfs/t5872/209/5240187906/2872/8fa98cd/595c3b2aN4155b931.png',
        value: '练习'
      },
      {
        image: 'https://img12.360buyimg.com/jdphoto/s72x72_jfs/t10660/330/203667368/1672/801735d7/59c85643N31e68303.png',
        value: '问卷'
      },
      {
        image: 'https://img14.360buyimg.com/jdphoto/s72x72_jfs/t17251/336/1311038817/3177/72595a07/5ac44618Na1db7b09.png',
        value: '面试'
      },
      {
        image: 'https://img30.360buyimg.com/jdphoto/s72x72_jfs/t5770/97/5184449507/2423/294d5f95/595c3b4dNbc6bc95d.png',
        value: '收藏'
      }
    ];

    function handleClickCourse(course) {
      Taro.navigateTo({url: "/pages/course_detail/index?courseId=" + course.id})
    }

    function handleClickBanner(item) {
      const {redirectUrl} = item;
      if (redirectUrl === null || redirectUrl === '') {
        return;
      }
      if (redirectUrl.startsWith('http')) {
        Taro.navigateTo({url: "/pages/webview/index?url=" + redirectUrl})
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
      homeGridData,
      searchValue,
      courses,
      banners,
      init,
      handleSearchChange,
      handleSearch,
      handleGridClick,
      handleClickCourse,
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