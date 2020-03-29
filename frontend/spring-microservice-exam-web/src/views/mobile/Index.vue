<template>
  <div class="m-subject-content">
    <transition name="fade-transform" mode="out-in">
      <el-row class="m-exam-title" v-show="!loading">
        <el-col :span="24">
          <h2>{{ exam.examinationName }}</h2>
          <p v-if="exam.attention !== undefined">{{ exam.attention }}</p>
        </el-col>
      </el-row>
    </transition>
    <transition name="fade-transform" mode="out-in" v-for="subject in subjectList" :key="subject.id">
      <el-row v-show="subject.show">
        <el-col :span="24">
          <!-- 题目内容 -->
          <choices :ref="`choices${subject.id}`" v-if="subject.type === 0"/>
          <short-answer :ref="`shortAnswer${subject.id}`" :height="100" v-if="subject.type === 1"/>
          <judgement :ref="`judgement${subject.id}`" v-if="subject.type === 2"/>
          <multiple-choices :ref="`multipleChoices${subject.id}`" v-if="subject.type === 3"/>
        </el-col>
      </el-row>
    </transition>
    <el-row class="m-subject-button">
      <el-button type="success" size="medium" @click="handleSubmitExam" v-show="!loading">提交</el-button>
    </el-row>
  </div>
</template>

<script>
import { anonymousUserGetObj, fetchAllSubjectList } from '@/api/exam/exam'
import { anonymousUserSubmitAll } from '@/api/exam/answer'
import { isNotEmpty, messageWarn, messageFail, messageSuccess } from '@/utils/util'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'

export default {
  components: {
    Choices,
    MultipleChoices,
    ShortAnswer,
    Judgement
  },
  data () {
    return {
      identifier: '',
      loading: true,
      exam: {},
      subjectList: [],
      query: {
        examinationId: undefined
      }
    }
  },
  created () {
    this.query.examinationId = this.$route.query.id
    this.startExam()
  },
  methods: {
    startExam () {
      if (this.query.examinationId === undefined) {
        return
      }
      this.$prompt('请输入考生账号', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '自行注册或联系管理员创建账号',
        inputValidator: function (value) {
          if (value.length < 4) {
            return false
          }
          const reg = /^[A-Za-z]+$/
          return reg.test(value)
        },
        inputErrorMessage: '请输入合法英文字符且长度>4！',
        center: true
      }).then(({ value }) => {
        this.identifier = value
        this.doStart()
      }).catch(() => {
        console.log('取消考试')
        messageWarn(this, '取消考试')
        window.close()
      })
    },
    doStart () {
      // 查询考试信息
      this.loading = true
      anonymousUserGetObj(this.query.examinationId).then(response => {
        this.exam = response.data.data
        this.getSubjectList()
      }).catch((error) => {
        console.error(error)
        messageFail(this, '系统出了点问题')
        this.loading = false
      })
    },
    getSubjectList () {
      fetchAllSubjectList(this.query).then(response => {
        if (response.data.data.length > 0) {
          this.updateSubjectList(response.data.data)
        }
      }).catch((error) => {
        console.error(error)
        messageFail(this, '系统出了点问题')
      })
    },
    getSubjectRef (subject) {
      let ref
      switch (subject.type) {
        case 0:
          ref = this.$refs[`choices${subject.id}`][0]
          break
        case 1:
          ref = this.$refs[`shortAnswer${subject.id}`][0]
          break
        case 2:
          ref = this.$refs[`judgement${subject.id}`][0]
          break
        case 3:
          ref = this.$refs[`multipleChoices${subject.id}`][0]
          break
      }
      return ref
    },
    updateSubjectList (list) {
      if (list === undefined || list.length === 0) {
        return list
      }
      list.forEach(item => {
        item.show = false
      })
      this.subjectList = list
      for (let i = 0; i < list.length; i++) {
        const subject = list[i]
        setTimeout(() => {
          // 设置答题详情
          const ref = this.getSubjectRef(subject)
          subject.show = true
          if (isNotEmpty(ref)) {
            ref.setSubjectInfo(subject, this.subjectList.length, i + 1)
          }
        }, 250 + (100 * i))
      }
      setTimeout(() => {
        this.loading = false
      }, 250)
    },
    // 提交考试
    handleSubmitExam () {
      this.$confirm('确定要提交吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.doSubmitExam()
      }).catch(() => {})
    },
    doSubmitExam () {
      if (this.subjectList.length > 0) {
        for (let i = 0; i < this.subjectList.length; i++) {
          const subject = this.subjectList[i]
          const ref = this.getSubjectRef(subject)
          if (isNotEmpty(ref)) {
            // 获取答案
            const answer = ref.getAnswer()
            subject.answer = { answer: answer }
          }
        }
        // 提交到后台
        anonymousUserSubmitAll(this.subjectList, this.exam.id, this.identifier).then(response => {
          if (response.data.data) {
            messageSuccess(this, '提交成功，5s后自动退出')
            setTimeout(() => {
              window.close()
            }, 5000)
          } else {
            messageFail(this, '提交失败，请稍后重试！')
          }
        }).catch(error => {
          console.error(error)
        })
      }
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .m-subject-content {
    margin: 14px;
    .m-exam-title {
      text-align: left;
    }
   .m-subject-button {
     margin: 16px;
     text-align: center;
   }
  }
</style>
