import {createApp} from 'vue';
import {createUI} from 'taro-ui-vue3';
import './base.less';
import './app.less';
import './assert/styles/subject.less';
import './assert/styles/answer.less';
import 'taro-ui-vue3/dist/style/index.scss';

const App = createApp({
    onShow(options) {
    },
});

const tuv3 = createUI();
App.use(tuv3);

export default App
