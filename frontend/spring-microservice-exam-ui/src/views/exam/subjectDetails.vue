<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleTabChange">
      <!-- 单选题 -->
      <el-tab-pane label="单选题" name="0" :disabled="tempSubject.type !== 0 && dialogStatus !== dialogStatusType.create">
        <choices ref="choices" subjectInfo="tempSubject"></choices>
      </el-tab-pane>
      <!-- 多选题 -->
      <el-tab-pane label="多选题" name="3" :disabled="tempSubject.type !== 3 && dialogStatus !== dialogStatusType.create">
        <multiple-choices ref="multipleChoices" subjectInfo="tempSubject"></multiple-choices>
      </el-tab-pane>
      <!-- 简答题 -->
      <el-tab-pane label="简答题" name="1" :disabled="tempSubject.type !== 1 && dialogStatus !== dialogStatusType.create">
        <short-answer ref="shortAnswer" subjectInfo="tempSubject"></short-answer>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer collapse-top">
      <el-button @click="dialogSubjectFormVisible = false">{{ $t('table.cancel') }}</el-button>
      <el-button v-if="dialogStatus === dialogStatusType.create" type="primary" @click="createSubjectData">{{ $t('table.save') }}</el-button>
      <el-button v-else type="primary" @click="updateSubjectData">{{ $t('table.save') }}</el-button>
      <el-button type="primary" @click="updateAndAddSubjectData">{{ $t('table.saveAndAdd') }}</el-button>
    </div>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import { mapGetters } from 'vuex'
import { getSubject, addSubject, putSubject, delSubject, exportSubject } from '@/api/exam/subject'
import { notifySuccess, isNotEmpty, isCreate } from '@/utils/util'
import { dialogStatusConstant } from '@/utils/constant'
import SpinnerLoading from '@/components/SpinnerLoading'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'

export default {
  name: 'CourseManagement',
  components: { Tinymce, SpinnerLoading, Choices, MultipleChoices, ShortAnswer },
  directives: {
    waves
  },
  data () {
    return {
      dialogSubjectFormVisible: false,
      activeName: '0',
      dialogStatus: '',
      dialogStatusType: { ...dialogStatusConstant },
      examinationId: '',
      // 题目临时信息
      tempSubject: {
        id: '',
        examinationId: '',
        categoryId: 0,
        subjectName: '',
        type: 0,
        choicesType: 0,
        options: [
          { subjectChoicesId: '', optionName: 'A', optionContent: '' },
          { subjectChoicesId: '', optionName: 'B', optionContent: '' },
          { subjectChoicesId: '', optionName: 'C', optionContent: '' },
          { subjectChoicesId: '', optionName: 'D', optionContent: '' }
        ],
        answer: {
          subjectId: '',
          answer: '',
          answerType: '',
          score: ''
        },
        score: 5,
        analysis: '',
        level: 2
      },
    }
  },
  created () {
    this.getSubject()
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    getSubject() {
      let subjectId = this.$route.params.id
      this.examinationId = this.$route.params.examinationId
      if (isNotEmpty(subjectId)) {
        // 加载选项信息
        getSubject(subjectId, { type: this.$route.params.type }).then(response => {
          const subjectInfo = response.data.data
          this.tempSubject = subjectInfo
          this.dialogStatus = dialogStatusConstant.update
          // 切换到对应的题型选项卡
          this.updateCurrentTag(subjectInfo.type)
          setTimeout(() => {
            const ref = this.getSubjectRef()
            if (isNotEmpty(ref)) {
              // 初始化单选题
              this.$nextTick(() => {
                ref.clearValidate()
                ref.setSubjectInfo(subjectInfo)
              })
            }
          }, 200)
        })
      } else {
        // 新增题目
        // 切换tab
        this.dialogStatus = dialogStatusConstant.create
        setTimeout(() => {
          const ref = this.getSubjectRef()
          if (isNotEmpty(ref)) {
            this.$nextTick(() => {
              // 初始化单选题选项
              ref.initDefaultOptions()
            })
          }
        }, 200)
      }
    },
    // 切换题型
    handleTabChange (tab, event) {
      this.tempSubject.type = parseInt(tab.name)
      // 更新组件里的题目信息
      this.updateComponentSubjectInfo()
    },
    // 更新题目
    updateSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        const subjectInfo = ref.getSubjectInfo()
        subjectInfo.examinationId = this.examinationId
        putSubject(subjectInfo).then(() => {
          this.dialogSubjectFormVisible = false
          notifySuccess(this, '更新成功')
        })
      }
    },
    // 更新并添加题目
    updateAndAddSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        const subjectInfo = ref.getSubjectInfo()
        // 绑定考试ID
        subjectInfo.examinationId = this.examinationId
        // 创建
        if (isCreate(this.dialogStatus)) {
          addSubject(subjectInfo).then(() => {
            this.resetTempSubject(subjectInfo.score)
            this.dialogStatus = dialogStatusConstant.create
            ref.clearValidate()
            this.getSubject()
            notifySuccess(this, '创建成功')
          })
        } else {
          // 修改
          putSubject(subjectInfo).then(() => {
            this.resetTempSubject(subjectInfo.score)
            this.dialogStatus = dialogStatusConstant.create
            ref.clearValidate()
            notifySuccess(this, '更新成功')
          })
        }
      }
    },
    updateCurrentTag (type) {
      this.activeName = type + ''
    },
    // 更新组件里的题目信息
    updateComponentSubjectInfo () {
      // 单选题
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        ref.setSubjectInfo(this.tempSubject)
      }
    },
    getSubjectRef () {
      let ref
      switch (this.activeName) {
        case '0':
          ref = this.$refs['choices']
          break
        case '1':
          ref = this.$refs['shortAnswer']
          break
        case '3':
          ref = this.$refs['multipleChoices']
          break
      }
      return ref
    },
    // 保存题目
    createSubjectData () {
      const ref = this.getSubjectRef()
      if (ref.validate()) {
        let subjectInfo = ref.getSubjectInfo()
        // 绑定考试ID
        subjectInfo.examinationId = this.examinationId
        addSubject(subjectInfo).then(() => {
          this.dialogSubjectFormVisible = false
          this.getSubject()
          notifySuccess(this, '创建成功')
        })
      }
    },
    // 切换题目类型
    changeSubjectType (value) {
      console.log(value)
    },
    resetActiveName () {
      // 重置选项卡至单选题
      this.activeName = '0'
    },
    resetTempSubject (score) {
      const ref = this.getSubjectRef()
      if (isNotEmpty(ref)) {
        ref.resetTempSubject(score)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .collapse-top {
    margin-top: 20px;
  }
</style>
