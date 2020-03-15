<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleTabChange">
      <!-- 单选题 -->
      <el-tab-pane label="单选题" name="0" :disabled="tempSubject.type !== 0 && dialogStatus !== dialogStatusType.create">
        <transition name="el-fade-in">
          <choices ref="choices" subjectInfo="tempSubject"/>
        </transition>
      </el-tab-pane>
      <!-- 多选题 -->
      <el-tab-pane label="多选题" name="3" :disabled="tempSubject.type !== 3 && dialogStatus !== dialogStatusType.create">
        <transition name="el-fade-in">
          <multiple-choices ref="multipleChoices" subjectInfo="tempSubject"/>
        </transition>
      </el-tab-pane>
      <!-- 判断题 -->
      <el-tab-pane label="判断题" name="2" :disabled="tempSubject.type !== 2 && dialogStatus !== dialogStatusType.create">
        <transition name="el-fade-in">
          <judgement ref="judgement" subjectInfo="tempSubject"/>
        </transition>
      </el-tab-pane>
      <!-- 简答题 -->
      <el-tab-pane label="简答题" name="1" :disabled="tempSubject.type !== 1 && dialogStatus !== dialogStatusType.create">
        <transition name="el-fade-in">
          <short-answer ref="shortAnswer" subjectInfo="tempSubject"/>
        </transition>
      </el-tab-pane>

      <!-- 从题库新增 -->
      <el-tab-pane name="4" v-if="subjectsType === '0'" :disabled="dialogStatus !== dialogStatusType.create">
        <span slot="label"><i class="el-icon-document"/> 从题库新增</span>
        <transition name="el-fade-in">
          <category-subjects ref="addFromSubjects" @selected="handleSelectSubject"/>
        </transition>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer collapse-top" v-show="activeName !== '4'">
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
import { getSubject, addSubject, putSubject } from '@/api/exam/subject'
import { notifySuccess, isNotEmpty, isCreate } from '@/utils/util'
import { dialogStatusConstant } from '@/utils/constant'
import SpinnerLoading from '@/components/SpinnerLoading'
import Tinymce from '@/components/Tinymce'
import Choices from '@/components/Subjects/Choices'
import MultipleChoices from '@/components/Subjects/MultipleChoices'
import ShortAnswer from '@/components/Subjects/ShortAnswer'
import Judgement from '@/components/Subjects/Judgement'
import CategorySubjects from '@/components/Subjects/CategorySubjects'

export default {
  name: 'SubjectDetails',
  components: { Tinymce, SpinnerLoading, Choices, MultipleChoices, ShortAnswer, Judgement, CategorySubjects },
  directives: {
    waves
  },
  data () {
    return {
      dialogSubjectFormVisible: false,
      activeName: '0',
      dialogStatus: '',
      dialogStatusType: { ...dialogStatusConstant },
      examinationId: undefined,
      categoryId: undefined,
      // 题目临时信息
      tempSubject: {
        id: '',
        examinationId: undefined,
        categoryId: undefined,
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
      // 0：试卷的题目，1：题库的题目
      subjectsType: '0'
    }
  },
  created () {
    let subjectInfo = this.$route.params.id
    if (isNotEmpty(subjectInfo)) {
      let subjectInfoArr = subjectInfo.split('-')
      // 0：试卷的题目，1：题库的题目
      this.subjectsType = subjectInfoArr[3]
      if (subjectInfoArr[3] === '0') {
        this.examinationId = subjectInfoArr[0]
      } else {
        this.categoryId = subjectInfoArr[0]
      }
      this.getSubject(subjectInfoArr[1], subjectInfoArr[2])
    }
  },
  computed: {
    ...mapGetters([
      'elements',
      'permissions'
    ])
  },
  methods: {
    getSubject (id, type) {
      if (isNotEmpty(id)) {
        // 加载选项信息
        getSubject(id, { type: type }).then(response => {
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
        subjectInfo.categoryId = this.categoryId
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
        subjectInfo.categoryId = this.categoryId
        // 创建
        if (isCreate(this.dialogStatus)) {
          addSubject(subjectInfo).then(() => {
            this.resetTempSubject(subjectInfo.score)
            this.dialogStatus = dialogStatusConstant.create
            ref.clearValidate()
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
        case '2':
          ref = this.$refs['judgement']
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
        subjectInfo.categoryId = this.categoryId
        addSubject(subjectInfo).then((response) => {
          this.dialogSubjectFormVisible = false
          // 获取题目
          const { id, type } = response.data.data
          this.updateRouteSubjectId(id, type)
          notifySuccess(this, '创建成功')
        })
      }
    },
    // 切换题目类型
    changeSubjectType (value) {
    },
    resetActiveName () {
      // 重置选项卡至单选题
      this.activeName = '0'
    },
    resetTempSubject (score) {
      this.tempSubject = {
        id: '',
        examinationId: undefined,
        categoryId: undefined,
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
        score: score,
        analysis: '',
        level: 2
      }
      this.$refs['choices'].resetTempSubject(score)
      this.$refs['shortAnswer'].resetTempSubject(score)
      this.$refs['multipleChoices'].resetTempSubject(score)
      this.$refs['judgement'].resetTempSubject(score)
    },
    updateRouteSubjectId (subjectId, type) {
      let subjectInfoArr = this.$route.params.id.split('-')
      // 当subjectId为undefined时才刷新页面
      if (subjectInfoArr[1] !== subjectId) {
        this.$router.push({
          path: `/exam/subjects/detail/${subjectInfoArr[0]}-${subjectId}-${type}-${subjectInfoArr[3]}`
        })
      }
    },
    // 选择题目回调
    handleSelectSubject (selected) {
      if (isNotEmpty(selected)) {
        this.resetSubject(selected)
        this.tempSubject = selected
        this.categoryId = selected.categoryId
        // 切换到对应的题型选项卡
        this.updateCurrentTag(selected.type)
        setTimeout(() => {
          const ref = this.getSubjectRef()
          if (isNotEmpty(ref)) {
            this.$nextTick(() => {
              ref.clearValidate()
              ref.setSubjectInfo(selected)
            })
          }
        }, 200)
      }
    },
    resetSubject (subjected) {
      subjected.id = undefined
      subjected.examinationId = undefined
      if (isNotEmpty(subjected.options)) {
        subjected.options.forEach(option => {
          option.id = ''
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .dialog-footer {
    margin-top: 20px;
  }
</style>
