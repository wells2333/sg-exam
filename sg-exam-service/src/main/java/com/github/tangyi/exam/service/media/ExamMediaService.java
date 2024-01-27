package com.github.tangyi.exam.service.media;

import com.github.tangyi.api.exam.dto.SpeechPlayDto;
import com.github.tangyi.api.exam.dto.SubjectDto;
import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.exam.service.data.RedisCounterService;
import com.github.tangyi.exam.service.subject.SubjectsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ExamMediaService {

    private static final String SPEECH_PLAY_CNT_KEY = "speech_play_cnt:";
    private static final int SPEECH_PLAY_CNT_TIMEOUT_SECOND = EnvUtils.getInt("SPEECH_PLAY_CNT_TIMEOUT_SECOND", 600);

    private final AttachmentManager attachmentManager;
    private final SubjectsService subjectsService;
    private final RedisCounterService counterService;

    public Attachment uploadSpeech(MultipartFile file) {
        return upload(file, AttachTypeEnum.EXAM_SPEECH);
    }

    public Attachment uploadVideo(MultipartFile file) {
        return upload(file, AttachTypeEnum.EXAM_VIDEO);
    }

    public Attachment uploadImage(MultipartFile file) {
        return upload(file, AttachTypeEnum.EXAM_IMAGE);
    }

    public Attachment upload(MultipartFile file, AttachTypeEnum type) {
        try {
            return attachmentManager.upload(MultipartFileUploadContext.of(type, file));
        } catch (IOException e) {
            log.error("upload exam media failed, type: {}", type.getDesc(), e);
        }
        return null;
    }

    public String imageUrl(Long id) {
		return attachmentManager.getPreviewUrlIgnoreException(id);
    }

    public String videoUrl(Long id) {
		return attachmentManager.getPreviewUrlIgnoreException(id);
    }

    public SpeechPlayDto playSpeech(Long userId, Long subjectId) {
        SpeechPlayDto play = new SpeechPlayDto();
        SubjectDto dto = subjectsService.getSubject(subjectId);
        if (dto != null && dto.getSpeechPlayLimit() != null) {
            Long cnt = getSpeechPlayCnt(userId, subjectId);
            if (cnt == null || cnt <= dto.getSpeechPlayLimit()) {
                String key = SPEECH_PLAY_CNT_KEY + userId;
                Long res = counterService.incrCount(key, subjectId);
                counterService.expire(key, subjectId, SPEECH_PLAY_CNT_TIMEOUT_SECOND);
                play.setCnt(res);
                play.setLimit(Boolean.TRUE);
            }
        }
        return play;
    }

    public Long getSpeechPlayCnt(Long userId, Long subjectId) {
        String key = SPEECH_PLAY_CNT_KEY + userId;
        return counterService.get(key, subjectId);
    }
}
