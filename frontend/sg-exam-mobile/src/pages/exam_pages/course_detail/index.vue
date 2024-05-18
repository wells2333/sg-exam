<template>
   <div v-if="courseInfo !== undefined" class="course-detail ml-22 mr-22">
    <image class="course-detail-image" :src="courseInfo.imageUrl"></image>
     <div class="course-detail-info">
       <div class="course-detail-name">
        <text>{{ courseInfo.courseName }}</text>
        <nut-tag class="course-charge-type" type="success" size="small">{{
            courseInfo.chargeType === 0 ? '免费' : '$' + courseInfo.chargePrice
          }}
        </nut-tag>
       </div>
      <nut-rate class="course-detail-rate course-detail-item-info" v-model="courseInfo.level" size="small" spacing="3" readonly></nut-rate>
       <div class="course-detail-items course-detail-item-info">
         <div>
           <div class="course-detail-item-info">
            <text class="course-teacher" v-if="courseInfo.memberCount > 0">
              报名人数：{{ courseInfo.memberCount }}
            </text>
           </div>
           <div class="course-detail-item-info">
            <text class="course-teacher" v-if="courseInfo.teacher !== null">
              {{ courseInfo.teacher }}
            </text>
           </div>
         </div>
         <div>
          <nut-button type="primary" size="small" :loading="joining" @click="handleStartLearn">
            开始学习
          </nut-button>
         </div>
       </div>
     </div>
     <div>
      <nut-tabs v-model="currentTag">
        <nut-tab-pane title="课程介绍">
           <div class="course-pane course-desc">
            <div v-html="courseInfo.courseDescription"></div>
           </div>
        </nut-tab-pane>
        <nut-tab-pane title="课程章节">
           <div class="course-pane">
             <div v-for="chapter in chapters">
              <p class="course-chapter">{{ chapter.chapter.title }}</p>
               <div v-for="section in chapter.sections">
                <p class="course-section" @click="handleClickSection(section.section)">{{ section.section.title }}</p>
                 <div v-for="point in section.points">
                  <p class="course-point" @click="handleClickPoint(point)">{{ point.title }}</p>
                 </div>
               </div>
             </div>
           </div>
        </nut-tab-pane>
        <nut-tab-pane title="课程评价">
           <div class="course-pane">
             <div>
               <div>
                 <div>
                  <nut-textarea
                      v-model="evaluateValue"
                      :maxLength="100"
                      height="80px"
                      placeholder='请输入评价内容'
                  />
                 </div>
                <nut-rate class="course-evaluate-input-rate" v-model="evaluateRateValue" size="small" spacing="3"></nut-rate>
                <nut-button class="course-evaluate-input-btn" type='primary' block :loading="submitting" @click="handleSubmitEvaluate">提交
                </nut-button>
               </div>
               <div>
                 <div v-for="item in evaluates" class="course-evaluates">
                   <div class="course-evaluate-operator">
                     <div>
                       <div>{{ item.operatorName }} </div>
                       <div>
                        <nut-rate class="course-flex-item" v-model="item.evaluateLevel" size="small" spacing="3"></nut-rate>
                       </div>
                     </div>
                     <div class="course-flex-item course-evaluate-time">{{ item.evaluateTime }} </div>
                   </div>
                   <div class="course-evaluate-content">
                    {{ item.evaluateContent }}
                   </div>
                 </div>
               </div>
             </div>
           </div>
        </nut-tab-pane>
      </nut-tabs>
     </div>
    <nut-dialog v-model:visible="isOpenedJoinModal" content="确定开始学习吗？"
                @cancel="handleCloseJoinModal"
                @ok="handleConfirmJoinModal"></nut-dialog>
   </div>
</template>
<script lang="ts">
import Taro from '@tarojs/taro';
import {onMounted, ref} from 'vue';
import examApi from '../../../api/exam.api';
import {showLoading, hideLoading, successMessage, warnMessage} from '../../../utils/util';

export default {
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const courseId = params.courseId;
    const courseDetail = ref<any>(undefined);
    const courseInfo = ref<any>(undefined);
    const chapters = ref<any>(undefined);
    const evaluates = ref<any>(undefined);
    const currentTag = ref<number>(0);
    const evaluateValue = ref<String>('');
    const evaluateRateValue = ref<number>(5);
    const isSubmitted = ref<boolean>(false);
    const submitting = ref<boolean>(false);
    const isOpenedJoinModal = ref<boolean>(false);

    async function fetch() {
      const res = await examApi.courseDetail(courseId);
      const {code, result} = res;
      if (code === 0) {
        courseDetail.value = result;
        courseInfo.value = result.course;
        chapters.value = result.chapters;
      }
    }

    async function fetchEvaluate() {
      const res = await examApi.courseEvaluate(courseId, {pageSize: 5});
      const {code, result} = res;
      if (code === 0) {
        evaluates.value = result.list;
      }
    }

    const tagsList = [{title: '课程介绍'}, {title: '课程章节'}, {title: '课程评价'}];

    async function handleSubmitEvaluate() {
      if (!isSubmitted.value) {
        let value = evaluateValue.value;
        if (value === '') {
          value = '用户默认好评';
        }
        try {
          const data = {
            courseId,
            evaluateContent: value,
            evaluateLevel: evaluateRateValue.value
          };
          submitting.value = true;
          isSubmitted.value = true;
          const res = await examApi.addEvaluate(data);
          const {code} = res;
          if (code === 0) {
            await successMessage('提交成功');
            await fetchEvaluate();
            setTimeout(() => {
              isSubmitted.value = false;
            }, 3000);
          } else {
            await warnMessage('提交失败');
            isSubmitted.value = false;
          }
        } finally {
          submitting.value = false;
        }
      } else {
        await warnMessage('请勿频繁提交');
      }
    }

    function handleClickSection(section) {
      toCourseSection("sectionId=" + section.id)
    }

    function handleClickPoint(point) {
      toCourseSection("pointId=" + point.id)
    }

    function toCourseSection(params: string) {
      Taro.navigateTo({url: "/pages/exam_pages/course_section/index?courseId=" + courseId + "&" + params})
    }

    function handleStartLearn() {
      isOpenedJoinModal.value = true;
    }

    function handleCloseJoinModal() {
      isOpenedJoinModal.value = false;
    }

    async function handleConfirmJoinModal() {
      handleCloseJoinModal();
      const section = chapters.value[0].sections[0].section;
      handleClickSection(section);
    }

    async function init() {
      try {
        await showLoading();
        evaluateValue.value = '';
        evaluateRateValue.value = 5;
        await fetch();
        await fetchEvaluate();
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });

    return {
      courseDetail,
      courseInfo,
      chapters,
      evaluates,
      tagsList,
      currentTag,
      evaluateValue,
      evaluateRateValue,
      isOpenedJoinModal,
      submitting,
      init,
      handleSubmitEvaluate,
      handleClickSection,
      handleClickPoint,
      handleStartLearn,
      handleCloseJoinModal,
      handleConfirmJoinModal
    }
  },
  onPullDownRefresh() {
    try {
      this.init();
    } finally {
      Taro.stopPullDownRefresh();
    }
  },
}
</script>

<style>
.course-detail {

}

.course-detail-image {
  width: 100%;
  height: 300px;
  border-radius: 6px;
}

.course-detail-info {
  padding: 8px;
  margin-bottom: 12px;
}

.course-detail-item-info {
  margin-top: 8px;
  display: flex;
  align-items: center;
}

.course-teacher {
  font-size: 30px;
  color: gray;
}

.course-pane {
  margin-left: 8px;
  margin-top: 22px;
}

.course-section {
  margin-left: 20px;
  font-size: 28px;
}

.course-point {
  margin-left: 50px;
  font-size: 26px;
}

.course-chapter {
  font-size: 36px;
  margin-top: 26px;
}

.course-chapter:hover, .course-section:hover, .course-point:hover {
  color: #6190E8;
}

.course-section {
  font-size: 32px;
  margin-top: 6px;
}

.course-evaluate-time {
  font-size: 22px;
  font-weight: 300;
  color: gray;
}

.course-flex-item {
  padding: 2px;
}

.course-evaluate-input {
  display: flex;
  align-items: center;
}

.course-evaluate-input-rate, .course-evaluate-input-btn {
  margin-top: 10px;
  margin-bottom: 10px;
}

.course-evaluates {
  margin-top: 36px;
  border-bottom: 1px solid rgb(238 238 238);
}

.course-evaluate-operator {
  display: flex;
  justify-content: space-between;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 5px;
}

.course-chapter, .course-section, .course-point {

}

.join-button {
  margin-left: auto;
}

.course-charge-type {
  margin-left: 20px;
}

.course-detail-name {
  font-size: 36px;
  display: flex;
  align-items: center;
}

.course-detail-rate {
  margin-top: 8px;
  margin-bottom: 8px;
}

.course-detail .nut-tab-pane {
  padding: 0;
}

.course-detail .nut-textarea {
  padding: 0;
}

.course-evaluate-content {
  margin-bottom: 20px;
}

.course-detail-items {
  display: flex;
  justify-content: space-between;
}

.course-desc {
  font-size: 36px;
  margin-top: 20px;
}

</style>