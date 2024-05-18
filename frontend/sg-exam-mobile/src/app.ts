import { createApp } from 'vue';
import './app.scss';
import './base.scss';
import './assert/styles/answer.scss';
import './assert/styles/subject.scss';
import './assert/font/iconfont.css';
import '@nutui/nutui-taro/dist/style.css';
import { IconFont } from '@nutui/icons-vue-taro';

const App = createApp({
  onShow (options) {},
})
App.use(IconFont);

App.config.globalProperties.$filters = {
    simpleStrFilter (str, length) {
        if (length === undefined) {
            length = 15
        }
        if (str === null || str === undefined) {
            return ''
        }

        if (str.length > length) {
            return str.substring(0, length) + '...'
        }
        return str
    }
}

export default App
