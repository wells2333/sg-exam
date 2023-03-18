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

export default App
