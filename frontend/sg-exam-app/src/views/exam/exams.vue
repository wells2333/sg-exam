<template>
  <div class="content-container">
    <div class="search-form">
      <el-form ref="examForm" :inline="true" :model="query" label-width="100px" class="examForm">
        <el-form-item label="" prop="examinationName">
          <el-input v-model="query.examinationName" autocomplete="off" :placeholder="$t('exam.examinationName')"
          @keyup.enter.native="submitForm('examForm')" size="small"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('examForm')" size="small">{{ $t('search') }}</el-button>
          <el-button @click="resetForm('examForm')" size="small">{{ $t('reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="category-list">
      <ul>
        <li :class="activeTag === '1' ? 'active' : ''" @click="changeTag('1')">{{$t('exam.exams.all')}}</li>
        <li :class="activeTag === '2' ? 'active' : ''" @click="changeTag('2')">{{$t('exam.exams.latestRelease')}}</li>
        <li :class="activeTag === '3' ? 'active' : ''" @click="changeTag('3')">{{$t('exam.exams.mostClicks')}}</li>
        <li :class="activeTag === '4' ? 'active' : ''" @click="changeTag('4')">{{$t('exam.exams.parameters')}}</li>
        <li :class="activeTag === '5' ? 'active' : ''" @click="changeTag('5')">{{$t('fav.myFavorite')}}</li>
      </ul>
    </div>
    <div class="exam-card-list">
      <transition name="fade-transform" mode="out-in" v-for="exam in examList" :key="exam.id">
        <div class="card-item single-popular-course" v-show="exam.show">
          <div @click="handleClickExam(exam)">
            <img class="card-item-snapshoot" :src="exam.imageUrl" />
          </div>
          <div class="card-item-detail">
            <div>
              <a href="javascript:void(-1);" @click="handleClickExam(exam)"></a>
              <h3>
                <div class="card-item-name" :title="exam.examinationName">
                  {{ exam.examinationName | simpleStrFilter }}
                </div>
              </h3>
            </div>
            <div class="card-item-course">
              <div class="card-item-course-detail" v-if="exam.course !== undefined && exam.course !== null">
                <a href="#" :title="exam.course.courseName">{{ exam.course.courseName | simpleStrFilter }}</a>
              </div>
              <div class="card-item-course-detail" v-else>
                {{$t('exam.exams.unrelatedCourse')}}
              </div>
              <div class="card-item-course-detail" v-if="exam.startTime !== undefined && exam.startTime !== null">
                {{ $t('exam.exams.startTime') + exam.startTime }}
              </div>
              <div class="card-item-course-detail" v-else>
                {{ $t('exam.exams.withoutTimeLimit') }}
              </div>
            </div>
          </div>
          <div class="seat-rating-fee d-flex justify-content-between">
              <div class="seat-rating h-100 d-flex align-items-center">
                <div class="seat" :title="$t('exam.course.courses.registerStudentsCnt')">
                  <i class="el-icon-user-solid" aria-hidden="true"></i> {{ exam.joinNum }}
                </div>
              </div>
          </div>
        </div>
      </transition>
      <i v-if="examList !== undefined && examList.length > 0" v-for="count in (examList.length)" :key="count"></i>
    </div>
    <el-row style="text-align: center; margin-bottom: 50px;">
      <el-col :span="24">
        <el-button v-if="!isLastPage" type="default" @click="scrollList" :loading="loading" style="margin-bottom: 100px;">{{ $t('load.loadMore') }}</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {mapGetters, mapState} from 'vuex'
import { fetchList } from '@/api/exam/exam'
import { getUserFavorites } from '@/api/exam/favorites'
import {
  isNotEmpty,
  messageWarn,
  getAttachmentPreviewUrl,
  formatDate,
  notifyFail
} from '@/utils/util'
import PanThumb from '@/components/PanThumb'

const EXAM_ACTIVE_TAG_MY_FAVORITE = '5'

export default {
  components: { PanThumb },
  filters: {
    examTypeFilter (type) {
      const typeMap = {
        0: '考试',
        1: '练习',
        2: '问卷',
        3: '面试'
      }
      return typeMap[type]
    },
    timeFilter (time) {
      return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
    }
  },
  data () {
    return {
      total: 0,
      loading: true,
      examList: [],
      isLastPage: false,
      query: {
        sortField: 'id',
        order: ' asc',
        page: 1,
        pageSize: 8,
        examinationName: '',
        status: 1
      },
      activeTag: '1'
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
      course: state => state.course.course,
      sysConfig: state => state.sysConfig.sysConfig,
      examRecord: state => state.exam.examRecord
    }),
    ...mapGetters([
      'subject'
    ])
  },
  created () {
    if (isNotEmpty(this.course)) {
      this.query.courseId = this.course.id
    }
    if (this.$route.query.query !== '') {
      this.query.examinationName = this.$route.query.query
    }
    this.query.page = 1
    this.getExamList()
  },
  methods: {
    getExamList (reset = false) {
      this.loading = true
      fetchList(this.query).then(response => {
        this.handleExamListResponse(response, reset)
        this.loading = false
      }).catch(() => {
        messageWarn(this, this.$t('load.loadFailed'))
        this.loading = false
      })
    },
    scrollList () {
      if (this.isLastPage) {
        messageWarn(this, this.$t('load.noMoreData'))
        return
      }
      if (this.loading) {
        messageWarn(this, this.$t('load.loading'))
        return
      }
      this.loading = true
      setTimeout(() => {
        this.query.page++
        fetchList(this.query).then(response => {
          const { total, isLastPage, list } = response.data.result
          this.updateExamList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.loading = false
        }).catch(() => {
          messageWarn(this, this.$t('load.loadFailed'))
        })
      }, 1000)
    },
    handleClickExam (exam) {
      this.$router.push({name: 'exam-details', query: {examId: exam.id}})
    },
    submitForm () {
      this.query.page = 1
      this.getExamList(true)
    },
    resetForm () {
      this.query.examinationName = ''
      this.getExamList(true)
    },
    changeTag (tag) {
      this.activeTag = tag
      // 我的收藏
      if (tag === EXAM_ACTIVE_TAG_MY_FAVORITE) {
        const params = {...this.query}
        // 查询收藏的考试
        params.targetType = '0'
        this.loading = true
        getUserFavorites(params).then(response => {
          this.handleExamListResponse(response, true)
          this.loading = false
        }).catch((err) => {
          console.error(err)
          notifyFail(this, this.$t('load.loadFailed'))
          this.loading = false
        })
        return
      }
      if (tag === '2') {
        this.query.sortField = 'create_time'
      } else if (tag === '4') {
        this.query.sortField = 'join'
      } else {
        this.query.sortField = 'id'
      }
      this.getExamList(true)
    },
    handleExamListResponse (response, reset) {
      const { total, isLastPage, list } = response.data.result
      this.total = total
      this.isLastPage = isLastPage
      if (reset) {
        this.examList = []
      }
      this.updateExamList(list)
    },
    updateExamList (list) {
      if (list === undefined || list === null || list.length === 0) {
        return list
      }

      list.forEach(item => {
        item.show = false
      })
      if (this.examList.length === 0) {
        this.examList = list
      } else {
        list.forEach(item => {
          let exist = false
          for (let i = 0; i < this.examList.length; i++) {
            if (this.examList[i].id === item.id) {
              exist = true
            }
          }
          if (!exist) {
            this.examList.push(item)
          }
        })
      }
      for (let i = 0; i < list.length; i++) {
        list[i].show = true
      }
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
 .examForm {
  margin-bottom: 0;
  .el-form-item {
    margin-bottom: 12px;
  }
 }
  .exam-empty {
    font-size: 13px;
    color: #999;
    text-align: center;
  }
  .category-cont {
    height: 100%;
    border: 1px solid red;
  }

  .exam-card-list {
    width: 100%;
    height: auto;
    overflow: auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    .card-item {
      width: calc((100% - 72px) / 4);
      box-sizing: border-box;
      position: relative;
      margin: 0 24px 24px 0;
      border-radius: 6px;
      box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
      border: 1px solid #ebebeb;
      &:nth-of-type(4n+0) {
        margin-right: 0;
      }
      .card-item-snapshoot {
        background-origin: border-box;
        background-size: cover;
        background-color: #f0f0f0;
        background-position: 49% 38% ;
        height: 152px;
        display: block;
        border-radius: 6px 6px 0 0;
      }
      .card-item-detail {
        padding: 12px 12px;
        height: 130px;
        .card-item-name {
          color: #545c63;
          display: -webkit-box;
          overflow: hidden;
          margin-bottom: 5px;
        }
        .card-item-course {
          --x-height-multiplier: 0.342;
          --baseline-multiplier: 0.22;
          font-weight: 300;
          font-style: normal;
          letter-spacing: 0;
          .card-item-course-detail {
            color: #9199a1;
            font-size: 14px;
            margin-bottom: 8px;
            a {
              color: #9199a1;
              display: inline-block;
              font-weight: 400;
              margin-right: 10px;
            }
          }
        }
      }
    }
    >i {
      width: 30%;
      padding-right: 12px;
    }
  }
</style>
