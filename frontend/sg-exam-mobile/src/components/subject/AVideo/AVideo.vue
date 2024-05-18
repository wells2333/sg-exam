<template>
    <div>
         <div class="subject-audio" v-if="subject.speechUrl && subject.speechUrl !== ''">
            <audio
                    :id="'audio_' + subject.id"
                    :src="subject.speechUrl"
                    poster="https://yunmianshi.com.cn/storage/assert/audio_poster.png"
                    name="请播放语音"
                    :controls="true"
                    :autoplay="subject.autoPlaySpeech"
                    :loop="false"
                    :muted="true"
            />
        </div>
         <div class="subject-video" v-if="subject.subjectVideoUrl && subject.subjectVideoUrl !== ''">
            <video
                    :id="'video_'+ subject.id"
                    :src="subject.subjectVideoUrl"
                    initial-time="0"
                    :controls="true"
                    :autoplay="false"
                    :loop="false"
                    :muted="false"
            />
        </div>
    </div>
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