package com.github.tangyi.user.service.attach;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.tangyi.api.user.service.IDefaultImageService;
import com.github.tangyi.common.utils.EnvUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DefaultImageService implements IDefaultImageService {

    public static final String DEFAULT_IMAGE_SUFFIX = EnvUtils.getValue("DEFAULT_IMAGE_SUFFIX", ".jpeg");

    private final List<byte[]> images = Lists.newArrayList();

    public DefaultImageService() {
        try {
            for (int i = 1; i <= 10; i++) {
                try (InputStream stream = ResourceUtil.getStream("images/" + i + DEFAULT_IMAGE_SUFFIX)) {
                    byte[] bytes = FileCopyUtils.copyToByteArray(stream);
                    if (bytes.length > 0) {
                        images.add(bytes);
                    }
                }
            }
            log.info("Init default image finished, size: {}", images.size());
        } catch (Exception e) {
            log.error("Failed to init default image", e);
        }
    }

    @Override
    public byte[] randomImage() {
        if (CollectionUtils.isNotEmpty(images)) {
            return images.get(ThreadLocalRandom.current().nextInt(images.size()));
        }
        return null;
    }
}
