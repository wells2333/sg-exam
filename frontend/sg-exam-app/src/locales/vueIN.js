import Vue from 'vue'
import VueI18n from 'vue-i18n'
import zh from './lang/zh-CN/zh'
import en from './lang/en/en'
import {VUE_APP_I18N_LOCAL} from '@/utils/env'

console.log('The i18n local: ' + VUE_APP_I18N_LOCAL)

Vue.use(VueI18n)
const i18n = new VueI18n({
  locale: VUE_APP_I18N_LOCAL || 'zh',
  messages: {zh, en}
})

export default i18n
