import Vue from 'vue'
import VueI18n from 'vue-i18n'
import zh from './lang/zh-CN/zh'
import en from './lang/en/en'

Vue.use(VueI18n)
const i18n = new VueI18n({
  locale: 'en',
  messages: {zh, en}
})

export default i18n
