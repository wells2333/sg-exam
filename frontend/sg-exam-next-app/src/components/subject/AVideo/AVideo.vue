<template>
    <view>
        <view class="subject-audio" v-if="subject.speechUrl && subject.speechUrl !== ''">
            <audio
                    :id="'audio_' + subject.id"
                    :src="subject.speechUrl"
                    poster="http://cdn.yunmianshi.com/assert/audio_poster.png?e=1685886852&token=8-9rcJPtTrLOJP4fLNWXy_qwiLVc3Exu52iuGlxt:BvfdTE3lilkEsqDLc6zrkMUrzGE="
                    name="请播放语音"
                    :controls="true"
                    :autoplay="false"
                    :loop="false"
                    :muted="true"
            />
        </view>
        <view class="subject-video" v-if="subject.subjectVideoUrl && subject.subjectVideoUrl !== ''">
            <video
                    :id="'video_'+ subject.id"
                    :src="subject.subjectVideoUrl"
                    initial-time="0"
                    :controls="true"
                    :autoplay="false"
                    :loop="false"
                    :muted="false"
            />
        </view>
    </view>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";

export default defineComponent({
    name: 'AVideo',
    components: {},
    props: {
        subject: {
            type: Object,
            default: () => {
            }
        },
    },
    setup(props) {
        const subject = ref<object>(props.subject);
        return {
            subject
        }
    }
})
</script>