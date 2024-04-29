<template>
  <div class="subject-box">
    <div class="subject-box-content">
      <div class="subject-left">
        <div class="tool-bar">
          <CountDownTimer :minute="10" @finished="countdownEnd"/>
        </div>
        <div class="subject-box-card">
          <div v-show="!loading">
            <div v-for="(item, index) in subjects" :key="index">
              <choices :ref="'choices_' + index" v-show="item.type === 0" :onChoice="onChoiceFn"/>
              <short-answer :ref="'shortAnswer_' + index" v-show="item.type === 1"
                            :onChoice="onChoiceFn"/>
              <judgement :ref="'judgement_' + index" v-show="item.type === 2"
                         :onChoice="onChoiceFn"/>
              <multiple-choices :ref="'multipleChoices_' + index" v-show="item.type === 3"
                                :onChoice="onChoiceFn"/>
              <fill-blank :ref="'fillBlank_' + index" v-show="item.type === 4"
                          :onChoice="onChoiceFn"/>
              <material :ref="'material_' + index" v-show="item.type === 5"
                        :onChoice="onChoiceFn"/>
            </div>
          </div>
        </div>
      </div>
      <div class="subject-right">
        <div class="subject-right-answer">
          <el-button
            :class="value.answered ? 'answered' : 'not-answered'"
            v-for="(value, index) in currentCards"
            :key="index"
            size="small"
          >
            &nbsp;{{ value.sort }}&nbsp;
          </el-button>
        </div>
        <div class="subject-right-status">
          <div class="status-item">
            <span class="answered-label"></span>{{ $t("exam.startExam.answered") }}({{ answeredCnt }})
          </div>
          <div class="status-item">
            <span class="unanswered-label"></span>{{ $t("exam.startExam.notAnswered") }}({{ currentCards.length - answeredCnt }})
          </div>
        </div>
        <div class="subject-right-footer" @click="handleSubmit">
          {{ $t("submit") }}
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapState} from 'vuex'
import CountDown from 'vue2-countdown'
import {submitAll} from '@/api/exam/answer'
import {getAllSubjects, calculateDuration} from '@/api/exam/examRecord'
import store from '@/store'
import {messageFail, messageSuccess} from '@/utils/util'
import {getSubjectRef, setSubjectInfo, beforeSaveSubject} from '@/utils/busi'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import FillBlank from '@/components/Subjects/FillBlank'
import Material from '@/components/Subjects/Material'
import CountDownTimer from "@/components/CountDownTimer/index.vue";

let eventListeners = {}
export default {
  components: {
    CountDownTimer,
    CountDown,
    Tinymce,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement,
    FillBlank,
    Material
  },
  data() {
    return {
      examinationId: undefined,
      recordId: undefined,
      loading: true,
      loadingSubmit: false,
      startTime: 0,
      endTime: 0,
      duration: '',
      dialogVisible: false,
      subjects: [],
      answeredCnt: 0,
      currentCards: []
    }
  },
  computed: {
    ...mapState({
      examRecord: state => state.exam.examRecord
    }),
    ...mapGetters([
      'exam',
      'cards',
      'subject'
    ])
  },
  created() {
    this.examinationId = this.$route.params.id
    this.recordId = this.$route.query.recordId
    if (this.examinationId && this.recordId) {
      const tmpCards = []
      if (this.cards && this.cards.length > 0) {
        this.cards.forEach(c => {
          tmpCards.push(c)
        })
        this.currentCards = tmpCards
      }
      this.startExam()
    } else {
      this.$router.push({name: 'exams', query: {redirect: true}})
    }
  },
  mounted() {
    if (window.history && window.history.pushState) {
      window.history.pushState(null, null, document.URL)
      window.addEventListener('popstate', this.goBack, false)
    }
  },
  destroyed() {
    this.removeAllEventListeners()
    window.removeEventListener('popstate', this.goBack, false)
  },
  methods: {
    countdownEnd() {
      console.log('倒计时结束🔚')
    },
    onChoiceFn(sort) {
      debugger
      // 标识已答题状态
      if (sort) {
        this.currentCards[sort - 1].answered = true
      }
      // 更新答题进度
      let cnt = 0
      for (let i = 0; i < this.currentCards.length; i++) {
        const c = this.currentCards[i]
        if (c.answered !== undefined && c.answered === true) {
          cnt++
        }
      }
      this.answeredCnt = cnt
    },
    goBack() {
      this.$router.push({name: 'exams'})
    },
    startExam() {
      this.loading = true
      this.antiCheat();
      messageSuccess(this, this.$t('exam.startExam.startExam'))
      getAllSubjects(this.examinationId).then(res => {
        const {data} = res
        if (data && data.code === 0 && data.result) {
          this.subjects = data.result
        }
      }).catch(e => {
        console.error(e)
      }).finally(() => {
        this.loading = false
        if (this.subjects.length > 0) {
          setTimeout(() => {
            for (let i = 0; i < this.subjects.length; i++) {
              setSubjectInfo(this.$refs, i, this.subjects[i], this.subjects)
            }
          }, 100)
        }
      })
    },
    showAnswerCard() {
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$confirm(this.$t('confirmSubmit'), this.$t('tips'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.loadingSubmit = true
        beforeSaveSubject(this.$refs, this.subjects)
        this.doSubmitExam(this.examRecord.id)
      }).catch(() => {
      })
    },
    doSubmitExam(examRecordId) {
      const data = []
      for (let i = 0; i < this.subjects.length; i++) {
        const item = this.subjects[i]
        const ref = getSubjectRef(this.$refs, i, item)
        if (ref) {
          const answer = ref.getAnswer()
          data.push({examRecordId, subjectId: item.id, answer})
        }
      }
      if (data.length === 0) {
        this.loadingSubmit = false
        return
      }

      submitAll(data).then(() => {
        this.loadingSubmit = false
        messageSuccess(this, this.$t('submit') + this.$t('success'))
        setTimeout(() => {
          store.dispatch('ClearExam')
          this.$router.push({name: 'exam-record'})
        }, 800)
      }).catch(() => {
        messageFail(this, this.$t('submit') + this.$t('failed'))
        this.loadingSubmit = false
      })
    },
    antiCheat() {
      // 模拟按下 F11 键
      this.requestFullscreen()
      // 定义一个对象来保存所有的事件监听器
      // 定义函数来添加事件监听器并保存到 eventListeners 对象中
      function addEventListenerAndSave(eventType, listener) {
        document.addEventListener(eventType, listener);
        // 将监听器保存到 eventListeners 对象中，以便后续移除
        if (!eventListeners[eventType]) {
          eventListeners[eventType] = [];
        }
        eventListeners[eventType].push(listener);
      }

      // 添加所有事件监听器
      addEventListenerAndSave("keydown", function (event) {
        if (event.key === "F12") {
          event.preventDefault();
          console.warn("抱歉，禁止使用 F12 开发者工具。");
        } else if (event.key === "F11" || event.key === "Escape") {
          event.preventDefault();
          console.warn("抱歉，禁止小屏");
        }
      });

      addEventListenerAndSave("copy", function (event) {
        event.preventDefault();
        console.warn("抱歉，禁止复制内容。");
      });

      addEventListenerAndSave("cut", function (event) {
        event.preventDefault();
        console.warn("抱歉，禁止剪切内容。");
      });

      addEventListenerAndSave("paste", function (event) {
        event.preventDefault();
        console.warn("抱歉，禁止粘贴内容。");
      });

      addEventListenerAndSave("click", function (event) {
        console.log("鼠标点击位置：", event.pageX, event.pageY);
      });

      addEventListenerAndSave("keydown", function (event) {
        console.log("按下键盘按键：", event.key);
      });

      addEventListenerAndSave("contextmenu", function (event) {
        event.preventDefault();
        console.warn("抱歉，禁止右键菜单。");
      });

      window.addEventListener("beforeunload", function (event) {
        event.preventDefault();
        console.warn("抱歉，禁止刷新页面。");
      });
    },
    removeAllEventListeners() { // 定义函数来移除所有事件监听器
      for (let eventType in eventListeners) {
        eventListeners[eventType].forEach(function (listener) {
          document.removeEventListener(eventType, listener);
        });
      }
      // 清空 eventListeners 对象
      eventListeners = {};
      this.exitFullscreen();
    },
    // 自动启动全屏
    requestFullscreen() {
      let el = document.documentElement;
      let rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullscreen;
      if (typeof rfs !== "undefined" && rfs) {
        rfs.call(el);
      }
    },
    // 退出全屏
    exitFullscreen() {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      }
    }
  },
  beforeRouteLeave(to, from, next) {
    this.removeAllEventListeners();
    const {name, query} = to
    if (name === 'exam-record' || query.redirect) {
      next()
      return
    }

    this.$confirm(this.$t('exam.startExam.confirmExit'), this.$t('tips'), {
      confirmButtonText: this.$t('sure'),
      cancelButtonText: this.$t('cancel'),
      type: 'warning'
    }).then(() => {
      next()
    }).catch(() => {
      next(false)
    })
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
@import "../../assets/css/start_exam.scss";
</style>
