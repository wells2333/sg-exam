import Taro from "@tarojs/taro";
import { unref } from 'vue';
import userApi from '../../../api/user.api';

export const initRecorderManager = (recordFlag, recordBtnText, recordBtnIcon, recordRes, recordCompleteBtnText, recordCompleteBtnIcon) => {
    const recorderManager = Taro.getRecorderManager();
    // 监听录音开始
    recorderManager.onStart(() => {
        onRecordStart(recordFlag, recordBtnText, recordBtnIcon);
    });
    // 监听录音暂停
    recorderManager.onPause(() => {
        onRecordPause(recordFlag, recordBtnText, recordBtnIcon);
    });
    // 监听录音继续
    recorderManager.onResume(() => {
        onRecordStart(recordFlag, recordBtnText, recordBtnIcon);
    });
    // 监听录音停止
    recorderManager.onStop((res) => {
        onRecordStop(recordFlag, recordBtnText, recordBtnIcon, res, recordRes, recordCompleteBtnText, recordCompleteBtnIcon);
    });
    // 异常
    recorderManager.onError(() => {
        Taro.showToast({
            title: '录音失败',
            duration: 1000,
            icon: 'none',
        });
    });
    return recorderManager;
}

export const onRecordStart = (recordFlag, recordBtnText, recordBtnIcon) => {
    recordFlag.value = 1;
    recordBtnText.value = '暂停';
    recordBtnIcon.value = 'pause-circle';
}

export const onRecordPause = (recordFlag, recordBtnText, recordBtnIcon) => {
    recordFlag.value = 2;
    recordBtnText.value = '继续录音';
    recordBtnIcon.value = 'play-circle';
}

export const onRecordStop = (recordFlag, recordBtnText, recordBtnIcon, res, recordRes, recordCompleteBtnText, recordCompleteBtnIcon) => {
    if (res.duration < 1000) {
        Taro.showToast({
            title: '录音时间太短',
            duration: 1000,
            icon: 'none',
        });
    } else {
        resetRecord(recordFlag, recordBtnText, recordBtnIcon);
        recordRes.value = res;
        recordCompleteBtnText.value = '播放';
        recordCompleteBtnIcon.value = 'play-circle';
    }
}

export const resetRecord = (recordFlag, recordBtnText, recordBtnIcon) => {
    recordFlag.value = 0;
    recordBtnText.value = '重新录音';
    recordBtnIcon.value = 'play-circle';
}

export const pleaseStartRecord = () => {
    Taro.showToast({
        title: '请先开始录音',
        duration: 1000,
        icon: 'none',
    });
}

export const startRecordAudio = (recorderManager, recordFlag, recordCompleteBtnText, recordCompleteBtnIcon) => {
    const flag = unref(recordFlag);
    if (flag === 0) {
        // 开始录音
        recorderManager.start({
            duration: 60000,
            sampleRate: 44100,
            numberOfChannels: 1,
            encodeBitRate: 192000,
            format: 'mp3',
            frameSize: 50,
        });

        Taro.showToast({
            title: '开始录音',
            duration: 1000,
            icon: 'none',
        });
    } else if (flag === 1) {
        // 暂停录音
        recorderManager.pause();
    } else if (flag === 2) {
        // 继续录音
        recorderManager.resume();
    }
    recordCompleteBtnText.value = '完成';
    recordCompleteBtnIcon.value = 'stop-circle';
}

export const stopRecordAudio = (recorderManagerRef) => {
    const recorderManager = unref(recorderManagerRef);
    if (recorderManager !== undefined) {
        recorderManager.stop();
    }
}

export const playAudio = (speechUrl) => {
    const innerAudioContext = Taro.createInnerAudioContext();
    innerAudioContext.autoplay = true;
    innerAudioContext.src = speechUrl;
    innerAudioContext.onError((res) => {
      console.log(res.errMsg);
      console.log(res.errCode);
    })
    Taro.playVoice({
      filePath: speechUrl,
      complete: function () { }
    })
}

export const uploadAudioFile = async (tempFilePath) => {
    await userApi.uploadAudio(tempFilePath);
}