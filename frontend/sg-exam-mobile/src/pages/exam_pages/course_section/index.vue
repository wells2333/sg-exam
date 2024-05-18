<template>
  <div class="course-section">
    <div class="at-article">
      <div v-if="pointDetail !== undefined">
        <div class="at-article__h1">{{ pointDetail.title }}</div>
        <div class='at-article__info'>
          更新时间：{{ pointDetail.updateTime }}&nbsp;&nbsp;&nbsp;&nbsp;学习时长：{{ pointDetail.learnHour }}小时
        </div>
        <div class="at-article__content">
          <div class="section-content">
            <div class="section-video">
              <video class="section-video-content" v-if="videoUrl !== undefined" :src="videoUrl"></video>
            </div>
            <div class="at-article__section">
              <div v-html="pointDetail.content"></div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="sectionDetail !== undefined">
        <div class="at-article__h2">{{ sectionDetail.section.title }}</div>
        <div class='at-article__info'>
          更新时间：{{ sectionDetail.section.updateTime }}&nbsp;&nbsp;&nbsp;&nbsp;学习时长：{{
            sectionDetail.section.learnHour
          }}小时
        </div>
        <div class="at-article__content">
          <div class="section-content">
            <div class="section-video">
              <video class="section-video-content" v-if="videoUrl !== undefined" :src="videoUrl"></video>
            </div>
            <div class="at-article__section">
              <div v-if="sectionDetail.section.contentType === 2 && pdf.pdfData !== undefined">
                <pdf :src="pdf.pdfData" :page="pdf.page"
                     :scale.sync="pdf.scale" style="width:100%; margin:20px auto;"
                     :annotation="true"
                     :resize="true"></pdf>
                <div class="pdf-buttons">
                  <nut-button type="default" size="mini" @click="pdf.page > 1 ? pdf.page-- : 1">上一页</nut-button>
                  <a style="margin-left: 10px; margin-right: 10px;">
                    {{ pdf.page }} / {{ pdf.numPages ? pdf.numPages : '∞' }}
                  </a>
                  <nut-button type="default" size="mini" @click="pdf.page < pdf.numPages ? pdf.page++ : 1">下一页
                  </nut-button>
                </div>
              </div>
              <div v-else v-html="sectionDetail.section.content"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Taro from '@tarojs/taro';
import {onMounted, ref} from 'vue';
import examApi from '../../../api/exam.api';
import {showLoading, hideLoading} from '../../../utils/util';
import pdfvuer from 'pdfvuer'

export default {
  components: {
    pdf: pdfvuer
  },
  setup() {
    const currentInstance = Taro.getCurrentInstance();
    const params = currentInstance.router.params;
    const courseId = ref<any>(params.courseId);
    const sectionId = ref<any>(params.sectionId);
    const pointId = ref<any>(params.pointId);

    const courseDetail = ref<object>();
    const sectionDetail = ref<object>();
    const pointDetail = ref<object>();
    const videoUrl = ref<string>();
    const pdf = ref<object>({
      pdfData: undefined,
      page: 1,
      numPages: 0,
      scale: 'page-width'
    });

    async function getCourseDetail() {
      const res = await examApi.courseDetail(courseId.value);
      const {code, result} = res;
      if (code === 0) {
        courseDetail.value = result;
      }
    }

    async function getSection() {
      if (sectionId.value === null || sectionId.value === undefined) {
        return;
      }

      const res = await examApi.getSectionDetail(sectionId.value);
      const {code, result} = res;
      if (code === 0) {
        sectionDetail.value = result;
        pdf.value.pdfData = undefined;
        if (result.videoUrl !== undefined && result.videoUrl !== null && result.videoUrl !== '') {
          videoUrl.value = result.videoUrl;
        }

        // PDF
        if (sectionDetail.value.section.contentType === 2) {
          const self = pdf.value;
          self.pdfData = pdfvuer.createLoadingTask(sectionDetail.value.section.content);
          self.pdfData.then(p => {
            self.numPages = p.numPages;
          });
        }
      }
    }

    async function getPoint() {
      if (pointId.value === null || pointId.value === undefined) {
        return;
      }

      const res = await examApi.getPointDetail(pointId.value);
      const {code, result} = res;
      if (code === 0) {
        pointDetail.value = result;
        if (result.videoUrl !== undefined && result.videoUrl !== null && result.videoUrl !== '') {
          videoUrl.value = result.videoUrl;
        }
      }
    }

    function handlePdfPageLoaded() {

    }

    async function init() {
      try {
        await showLoading();
        await getCourseDetail();
        if (pointId.value !== undefined) {
          await getPoint();
        } else {
          await getSection();
        }
      } finally {
        hideLoading();
      }
    }

    onMounted(() => {
      init();
    });
    return {
      pdf,
      init,
      courseDetail,
      sectionDetail,
      pointDetail,
      videoUrl,
      handlePdfPageLoaded
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

<style lang="css" scoped>
.course-section {
  margin-left: 22px;
  margin-right: 22px;
}

.at-article {
  word-break: break-all;
  word-wrap: break-word;
  line-height: 1.5;
}

.at-article__h2 {
  margin-top: 0.64rem;
  font-size: 36px;
  line-height: 1.28rem;
}

.at-article__h3 {
  margin-top: 20px;
  font-size: 32px;
  line-height: 1.024rem;
}

.at-article__h1, .at-article__h2, .at-article__h3 {
  color: #333;
}

.at-article__info {
  margin-top: 20px;
  color: #CCC;
  font-size: 30px;
  line-height: 0.896rem;
}

.at-article__content {

}

.section-content {
  margin-top: 20px;
  font-size: 30px;
  color: #666;
  overflow: hidden;
}

.section-video {

}

.section-video-content {
  width: 100%;
}

.pdf-buttons {
  margin-bottom: 100px;
  text-align: center;
}
</style>