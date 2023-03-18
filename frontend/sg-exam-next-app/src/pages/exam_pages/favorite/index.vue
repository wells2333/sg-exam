<template>
  <view class="fav-panel">
    <nut-tabs v-model="current" @change="handleTabClick">
      <nut-tab-pane title="考试">
        <view class="exam-item box-show-item flex-col" v-for="item in items">
          <exam-item :item="item" @fav="handleFav(item)" @start="handleStartExam"></exam-item>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="题目">
        <view class="exam-item box-show-item flex-col subject-content bg-white" v-for="(item, index) in items">
          <view class="subject-type-label" @click="handleFavSubject(item, index)">
            <nut-tag size="small" type="success">{{ item.typeLabel }}</nut-tag>
            <IconFont v-if="item.favorite" name='star-fill-n' color="#FFC82C"></IconFont>
            <IconFont v-else name='star-n' color="#AAAAAA"></IconFont>
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
              <short-answer :disabled="true" :showInput="false"></short-answer>
            </view>
            <view v-else-if="item.type === 4">
              <judgement :subject="refItem(item)" :disabled="true" :answer="item.answer !== undefined ? item.answer.answer: undefined"></judgement>
            </view>
          </view>
          <view class="mb-22" v-if="item.answer !== undefined && item.answer !== null">
            <text class="answer-text-title">答案：</text>
            <text v-if="item.type === 2" class="answer-text-value">
              {{ item.answer.answer === '0' ? '正确' : '错误'}}
            </text>
            <view v-else>
              <wxparse class="answer-text-value" :html="item.answer.answer" key={Math.random()}></wxparse>
            </view>
          </view>
          <view class="mb-22" v-if="item.analysis !== undefined && item.analysis !== null">
            <text class="answer-text-title">解析：</text>
            <wxparse class="answer-text-value" :html="item.analysis" key={Math.random()}></wxparse>
          </view>
        </view>
      </nut-tab-pane>
      <nut-tab-pane title="课程">
        <view class="exam-item box-show-item flex-col" v-for="item in items">
          <course-item :item="item" @fav="handleFav(item)" @click="handleClickCourse"></course-item>
        </view>
      </nut-tab-pane>
    </nut-tabs>
  </view>
</template>

<script lang="ts">
import {onMounted, ref, unref} from 'vue';
import examApi from '../../../api/exam.api';
import {ExamItem} from '../../../components/exam-item';
import {CourseItem} from '../../../components/course-item';
import api from '../../../api/api';
import Taro from "@tarojs/taro";
import {Choice} from '../../../components/subject/choice/index';
import {Judgement} from '../../../components/subject/judgement/index';
import {ShortAnswer} from '../../../components/subject/shortAnswer/index';
import {successMessage} from '../../../utils/util';
import {IconFont} from '@nutui/icons-vue-taro';

export default {
  components: {
    IconFont,
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

    function handleTabClick({paneKey}) {
      current.value = Number(paneKey);
      items.value = [];
      fetch();
    }

    function handleClickCourse(item) {
      Taro.navigateTo({url: "/pages/exam_pages/course_detail/index?courseId=" + item.id})
    }

    function handleStartExam(item) {
      Taro.navigateTo({url: "/pages/exam_pages/exam_detail/index?id=" + item.id})
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
        await successMessage(text + '成功');
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

}

.fav-panel .nut-tab-pane {
  padding: 0;
}

.flex-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.exam-item {
  margin: 26px 22px 26px 22px;
  background: white;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
}

.subject-type-label {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
}
</style>