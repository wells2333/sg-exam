<template>
  <div class="subject-box">
    <div class="subject-box-content">
      <div class="subject-left">
        <div class="tool-bar">
          <CountDownTimer :minute="10" @finished="countdownEnd"/>
        </div>
        <div class="subject-box-card" v-show="subject">
          <choices ref="choices" v-show="subject.type === 0" :onChoice="onChoiceFn" />
          <short-answer ref="shortAnswer" v-show="subject.type === 1" :onChoice="onChoiceFn" />
          <judgement ref="judgement" v-show="subject.type === 2" :onChoice="onChoiceFn" />
          <multiple-choices
            ref="multipleChoices"
            v-show="subject.type === 3"
            :onChoice="onChoiceFn"
          />
          <fill-blank ref="fillBlank" v-show="subject.type === 4" :onChoice="onChoiceFn" />
          <material ref="material" v-show="subject.type === 5" :onChoice="onChoiceFn" />
        </div>
        <div class="subject-buttons">
          <div>
            <el-button plain @click="goLast" :loading="loadingLast"
              >{{ $t("exam.startExam.last") }}
            </el-button>
          </div>
          <div>
            <el-button plain @click="goNext" :loading="loadingNext"
              >{{ $t("exam.startExam.next") }}
            </el-button>
          </div>
        </div>
      </div>
      <div class="subject-right">
        <div class="subject-right-answer">
          <el-button
            :class="value.answered ? 'answered' : 'not-answered'"
            v-for="(value, index) in currentCards"
            :key="index"
            @click="toSubject(value.subjectId, value.sort)"
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
        <el-button class="subject-right-footer" @click="submitExam" :loading="loadingSubmit">
          {{ $t("submit") }}
        </el-button>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapState } from "vuex";
import { saveAndNext } from "@/api/exam/answer";
import store from "@/store";
import moment from "moment";
import { isNotEmpty, messageFail, messageSuccess, messageWarn } from "@/utils/util";
import Tinymce from "@/components/Tinymce";
import Choices from "@/components/Subjects/Choices";
import MultipleChoices from "@/components/Subjects/MultipleChoices";
import ShortAnswer from "@/components/Subjects/ShortAnswer";
import Judgement from "@/components/Subjects/Judgement";
import FillBlank from "@/components/Subjects/FillBlank";
import { nextSubjectType } from "@/const/constant";
import Material from "@/components/Subjects/Material";
import CountDownTimer from '@/components/CountDownTimer'

export default {
  components: {
    Tinymce,
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement,
    FillBlank,
    Material,
    CountDownTimer
  },
  data() {
    return {
      examinationId: undefined,
      recordId: undefined,
      timer: undefined,
      loadingLast: false,
      loadingNext: false,
      loadingSubmit: false,
      saving: false,
      startTime: 0,
      endTime: 0,
      duration: "",
      subjectStartTime: undefined,
      answer: "",
      dialogVisible: false,
      answeredCnt: 0,
      currentCards: []
    };
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo,
      examRecord: state => state.exam.examRecord
    }),
    ...mapGetters(["cards", "subject"])
  },
  created() {
    if (this.subject === undefined || Object.keys(this.subject).length === 0) {
      this.$router.push({ name: "exams", query: { redirect: true } })
      return
    }

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
      this.startExam();
      this.updateAnsweredCnt();
    } else {
      this.$router.push({ name: "exams", query: { redirect: true } })
    }
  },
  mounted() {
    if (window.history && window.history.pushState) {
      window.history.pushState(null, null, document.URL);
      window.addEventListener("popstate", this.goBack, false)
    }
  },
  destroyed() {
    clearInterval(this.timer);
    window.removeEventListener("popstate", this.goBack, false)
  },
  methods: {
    countdownEnd() {
      console.log('倒计时结束🔚')
    },
    updateAnsweredCnt() {
      let answered = 0;
      this.currentCards.forEach(c => {
        if (c.answered && c.answered === true) {
          answered++;
        }
      });
      this.answeredCnt = answered;
    },
    onChoiceFn(sort) {
      if (sort) {
        this.currentCards[sort - 1].answered = true;
      }
      let answered = 0;
      this.currentCards.forEach(c => {
        if (c.answered && c.answered === true) {
          answered++;
        }
      });
      this.answeredCnt = answered;
      this.$forceUpdate()
    },
    startExam() {
      messageSuccess(this, this.$t("exam.startExam.startExam"));
      setTimeout(() => {
        this.setSubjectInfo(this.subject);
      }, 100);
    },
    goBack() {
      this.$router.push({ name: "exams" });
    },
    goLast() {
      for (let i = 0; i < this.currentCards.length; i++) {
        if (this.currentCards[i].subjectId === this.subject.id) {
          if (i === 0) {
            messageWarn(this, this.$t("exam.startExam.isFirst"));
            break;
          }

          let { sort } = this.currentCards[i - 1];
          this.saveAndGetNext(nextSubjectType.last, sort);
          break;
        }
      }
    },
    goNext() {
      for (let i = 0; i < this.currentCards.length; i++) {
        if (this.currentCards[i].subjectId === this.subject.id) {
          if (i === this.currentCards.length - 1) {
            messageWarn(this, this.$t("exam.startExam.isLast"));
            break;
          }

          let { sort } = this.currentCards[i + 1];
          this.saveAndGetNext(nextSubjectType.next, sort);
          break;
        }
      }
    },
    // 保存当前题目，同时根据序号加载下一题
    saveAndGetNext(nextType, nextSubjectSort, subjectId = undefined) {
      if (this.saving) {
        return;
      }

      try {
        this.saving = true;
        const answer = this.getAnswer();
        const ref = this.getSubjectRef();
        if (ref) {
          ref.beforeSave();
        }
        this.startLoading(nextType);
        saveAndNext(answer, nextType, nextSubjectSort, subjectId)
          .then(response => {
            if (response.data.result !== null) {
              const subject = response.data.result;
              store.dispatch("SetSubjectInfo", subject).then(() => {
                this.setSubjectInfo(subject);
              });
            }
            this.subjectStartTime = moment().format("YYYY-MM-DD HH:mm:ss");
            this.endLoading(nextType);
          }).catch(error => {
            console.log(error);
            messageFail(this, this.$t("exam.startExam.getSubjectFailed"));
            this.endLoading(nextType);
          });
      } finally {
        this.saving = false;
      }
    },
    answerCard() {
      this.dialogVisible = true;
    },
    toSubject(subjectId, subjectSortNo) {
      this.saveAndGetNext(nextSubjectType.current, subjectSortNo, subjectId);
      this.dialogVisible = false;
    },
    submitExam() {
      this.$confirm(this.$t("confirmSubmit"), this.$t("tips"), {
        confirmButtonText: this.$t("sure"),
        cancelButtonText: this.$t("cancel"),
        type: "warning"
      }).then(() => {
        this.doSubmitExam(this.examinationId, this.recordId, this.userInfo, true);
      }).catch(() => {});
    },
    doSubmitExam(examinationId, examRecordId, userInfo, toExamRecord) {
      const ref = this.getSubjectRef();
      if (ref) {
        ref.beforeSave();
      }

      this.loadingSubmit = true
      saveAndNext(this.getAnswer(), 0).then(() => {
        store
          .dispatch("SubmitExam", {
            examinationId,
            examRecordId,
            userId: userInfo.id
          })
          .then(() => {
            messageSuccess(this, this.$t("submit") + this.$t("success"));
            store.dispatch("ClearExam");
            if (toExamRecord) {
              this.$router.push({ name: "exam-record" });
            }
          }).catch(() => {
            messageFail(this, this.$t("submit") + this.$t("failed"));
          }).finally(() => {
            this.loadingSubmit = false
          });
      }).catch(() => {
        this.loadingSubmit = false
        messageFail(this, this.$t("submit") + this.$t("failed"));
      });
    },
    getAnswer() {
      const ref = this.getSubjectRef();
      if (isNotEmpty(ref)) {
        const answer = ref.getAnswer();
        this.answer = answer;
        return {
          id: "",
          userId: this.userInfo.id,
          examinationId: this.examinationId,
          examRecordId: this.recordId,
          subjectId: this.subject.id,
          answer: answer,
          startTime: this.subjectStartTime
        };
      }
      return {};
    },
    startLoading(nextType) {
      if (nextType === nextSubjectType.next) {
        this.loadingNext = true;
      } else if (nextType === nextSubjectType.last) {
        this.loadingLast = true;
      } else {
        this.loadingNext = true;
      }
    },
    endLoading(nextType) {
      if (nextType === nextSubjectType.next) {
        this.loadingNext = false;
      } else if (nextType === nextSubjectType.last) {
        this.loadingLast = false;
      } else {
        this.loadingNext = false;
      }
    },
    getSubjectRef() {
      let ref;
      switch (this.subject.type) {
        case 0:
          ref = this.$refs.choices;
          break;
        case 1:
          ref = this.$refs.shortAnswer;
          break;
        case 2:
          ref = this.$refs.judgement;
          break;
        case 3:
          ref = this.$refs.multipleChoices;
          break;
        case 4:
          ref = this.$refs.fillBlank;
          break;
        case 5:
          ref = this.$refs.material;
          break;
        default:
          ref = this.$refs.sVideo;
          break;
      }
      return ref;
    },
    setSubjectInfo(sub) {
      const ref = this.getSubjectRef();
      if (isNotEmpty(ref)) {
        try {
          ref.setSubjectInfo(sub, this.currentCards.length, null);
        } catch (error) {
          console.error(error);
        }
      }
    }
  },
  beforeRouteLeave(to, from, next) {
    const { name, query } = to;
    if (name === "exam-record" || query.redirect) {
      next();
      return;
    }

    this.$confirm(this.$t("exam.startExam.confirmExit"), this.$t("tips"), {
      confirmButtonText: this.$t("sure"),
      cancelButtonText: this.$t("cancel"),
      type: "warning"
    }).then(() => {
      next();
    }).catch(() => {
      next(false);
    });
  }
};
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
</style>
