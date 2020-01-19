import { formatDate, commonFilter, isNotEmpty } from '../utils/util'
import { statusType, examType, subjectType } from '../utils/constant'


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
export function examStatusFilter (status) {
  return status === 0 ? '已发布' : '未发布'
}

/**
 * 题目类型
 * @param type
 * @returns {*}
 */
export function subjectTypeFilter (type) {
  return subjectType[type]
}

