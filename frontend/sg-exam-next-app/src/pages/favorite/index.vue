<template>
  <AtMessage/>
  <view class="fav-panel">
    <at-tabs :tab-list="favTabList" :current="current" @click="handleTabClick" color="rgb(7, 193, 96)">
      <at-tabs-pane title="考试">
        <view class="exam-item box-show-item flex-col" v-for="item in items">
          <exam-item :item="item" @fav="handleFav(item)" @start="handleStartExam"></exam-item>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="题目">
        <view class="exam-item box-show-item flex-col subject-content bg-white" v-for="(item, index) in items">
          <view class="subject-type-label" @click="handleFavSubject(item, index)">
            <at-tag size="small" type="primary">{{ item.typeLabel }}</at-tag>
            <at-icon value="star-2" size='10' :color="item.favorite === true ? '#FFC82C': '#AAAAAA'"></at-icon>
          </view>
          <view class="subject-title">
            <wxparse class="subject-title-content" :html="item.subjectName" key={Math.random()}></wxparse>
          </view>
          <view>
            <view v-if="item.type === 0">
              <choice :subject="refItem(item)" :disabled="true" :answer="item.answer !== undefined ? item.answer.answer: undefined"></choice>
            </view>
            <view v-else-if="item.type === 3">
              <choice :subject="refItem(item)" :multi="true" :disabled="true" :answer="item.answer !== undefined ? item.answer.answer: undefined"></choice>
            </view>
            <view v-else-if="item.type === 1">
              <short-answer :disabled="true" :answer="item.answer !== undefined ? item.answer.answer: undefined"></short-answer>
            </view>
            <view v-else-if="item.type === 4">
              <judgement :subject="refItem(item)" :disabled="true" :answer="item.answer !== undefined ? item.answer.answer: undefined"></judgement>
            </view>
          </view>
          <view class="answer-text">
            <text>参考答案：</text>
            <text class="answer-text-value">
              {{ item.answer !== undefined ? item.answer.answer : '' }}
            </text>
          </view>
          <view class="answer-text answer-text-analysis">
            <view>
              <text>解析：</text>
            </view>
            <view>
              <wxparse class="answer-text-value" :html="item.analysis" key={Math.random()} />
            </view>
          </view>
        </view>
      </at-tabs-pane>
      <at-tabs-pane title="课程">
        <view class="exam-item box-show-item flex-col" v-for="item in items">
          <course-item :item="item" @fav="handleFav(item)" @click="handleClickCourse"></course-item>
        </view>
      </at-tabs-pane>
    </at-tabs>
    <at-load-more v-if="items.length === 0 || (items.length > 0 && hasNextPageRef)" :status="loadMoreStatus"
                  @click="loadMore" moreBtnStyle="size=5px;"></at-load-more>
  </view>
</template>

<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import examApi from '../../api/exam.api';
import {ExamItem} from '../../components/exam-item';
import {CourseItem} from '../../components/course-item';
import api from "../../api/api";
import Taro from "@tarojs/taro";
import {Choice} from '../../components/subject/choice/index';
import {Judgement} from '../../components/subject/judgement/index';
import {ShortAnswer} from '../../components/subject/shortAnswer/index';
import {successMessage} from "../../utils/util";

export default {
  components: {
    'exam-item': ExamItem,
    'course-item': CourseItem,
    'choice': Choice,
    'judgement': Judgement,
    'short-answer': ShortAnswer,
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
  onReachBottom() {
  },
  setup() {
    const favTabList = [{title: '考试'}, {title: '题目'}, {title: '课程'}];
    const current = ref<number>(0);
    let items = ref<any>([]);
    const hasNextPageRef = ref<boolean>(true);
    const nextPageRef = ref<number>(1);
    const loadMoreStatus = ref<string>('more');
    const pageNumRef = ref<number>(1);

    async function fetch(append: boolean = false) {
      if (loadMoreStatus.value === 'loading') {
        return;
      }
      loadMoreStatus.value = 'loading';
      let targetType = current.value;
      try {
        const {id} = api.getUserInfo();
        const res = await examApi.userFavorites({
          userId: id,
          page: unref(nextPageRef),
          targetType
        });
        const {code, result} = res
        if (code === 0) {
          const {list, hasNextPage, nextPage, pageNum} = result;
          if (list !== null && list.length > 0) {
            if (append) {
              items.value.push(...list);
            } else {
              items.value = [...list];
            }
          }
          hasNextPageRef.value = hasNextPage;
          nextPageRef.value = nextPage;
          pageNumRef.value = pageNum;
        }
      } finally {
        loadMoreStatus.value = hasNextPageRef.value ? 'more' : 'noMore';
      }
    }

    function handleFav(item) {
      items.value = [];
      hasNextPageRef.value = true;
      item.favorite = !item.favorite;
      const text = item.favorite ? '收藏' : '取消收藏';
      successMessage(text + '成功');
      fetch();
    }

    function handleTabClick(value) {
      current.value = value;
      items.value = [];
      fetch();
    }

    function handleClickCourse(item) {
      Taro.navigateTo({url: "/pages/course_detail/index?courseId=" + item.id})
    }

    function handleStartExam(item) {
      Taro.navigateTo({url: "/pages/exam_detail/index?id=" + item.id})
    }

    function init() {
      items.value = [];
      fetch();
    }

    function refItem(item) {
      return ref<any>(item);
    }

    async function handleFavSubject(item, index) {
      item.favorite = !item.favorite;
      const type = item.favorite ? '1' : '0';
      const text = item.favorite ? '收藏' : '取消收藏';
      items.value.splice(index, 1);
      const {id} = await api.getUserInfo();
      const res = await examApi.favoriteSubject(id, item.id, type);
      const {code} = res;
      if (code === 0) {
        successMessage(text + '成功');
      }
    }

    async function loadMore() {
      await fetch(true);
    }

    onMounted(() => {
      fetch();
    });

    return {
      current,
      favTabList,
      items,
      loadMoreStatus,
      hasNextPageRef,
      pageNumRef,
      init,
      refItem,
      handleFav,
      handleTabClick,
      handleClickCourse,
      handleStartExam,
      handleFavSubject,
      loadMore
    }
  }
}
</script>

<style>
.fav-panel {
  margin-left: 8px;
  margin-right: 8px;
}

.flex-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.exam-item {
  margin: 16px 6px 16px 6px;
  background: white;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}

.subject-type-label {
  display: flex;
  justify-content: space-between;
}
</style>