import { formatDate, commonFilter, isNotEmpty } from '../utils/util'

export function fmtDate (date, format) {
  if (!isNotEmpty(date)) {
    return ''
  }
  return formatDate(new Date(date), format)
}

export function simpleStrFilter (str, length) {
  if (length === undefined) {
    length = 15
  }
  return commonFilter(str, length)
}

export function simpleTagStatusFilter (status, expectStatus) {
  return status === expectStatus ? 'success' : 'warning'
}
