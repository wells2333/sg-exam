import { formatDate, commonFilter, isNotEmpty } from '../utils/util'
import { statusType, examType, subjectType, subjectTypeTag } from '../utils/constant'

/**
 * 日期格式化
 * @param date
 * @param format
 * @returns {*}
 */
export function fmtDate (date, format) {
  if (!isNotEmpty(date)) {
    return ''
  }
  return formatDate(new Date(date), format)
}

/**
 * 通用状态
 * @param status
 * @returns {*}
 */
export function statusTypeFilter (status) {
  return statusType[status]
}

/**
 * 截取指定长度
 * @param str
 * @param length
 * @returns {string}
 */
export function simpleStrFilter (str, length) {
  if (length === undefined) {
    length = 20
  }
  return commonFilter(str, length)
}

/**
 * 考试类型
 * @param type
 * @returns {*}
 */
export function examTypeFilter (type) {
  return examType[type]
}

/**
 * 发布类型
 * @param status
 * @returns {string}
 */
export function publicStatusFilter (status) {
  return parseInt(status) === 0 ? '已发布' : '草稿'
}

/**
 * 题目类型
 * @param type
 * @returns {*}
 */
export function subjectTypeFilter (type) {
  return subjectType[type]
}

/**
 * 题目标签
 * @param type
 * @returns {*}
 */
export function subjectTypeTagFilter (type) {
  return subjectTypeTag[type]
}

/**
 * 考试提交状态
 * @param type
 * @returns {*}
 */
export function submitStatusFilter (type) {
  const typeMap = {
    0: '待批改',
    1: '已批改',
    2: '待批改',
    3: '统计完成'
  }
  return typeMap[type]
}

/**
 * success状态
 * @param status, 自动传入
 * @param expectStatus
 * @returns {string}
 */
export function simpleTagStatusFilter (status, expectStatus) {
  return status === expectStatus ? 'success' : 'warning'
}
