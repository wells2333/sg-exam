package com.github.tangyi.user.service.attach;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.tangyi.api.user.service.IDefaultImageService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DefaultImageService implements IDefaultImageService {

    private final List<String> images = Lists.newArrayList();

    public DefaultImageService() {
        try {
            String str = FileUtils.readFileToString(ResourceUtils.getFile("classpath:default_image.json"), StandardCharsets.UTF_8);
            JSONArray array = JSON.parseArray(str);
            if (array != null && array.size() > 0) {
                for (Object obj : array) {
                    images.add(obj.toString());
                }
            }
            log.info("Init default image finished, size: {}", images.size());
        } catch (IOException e) {
            log.error("Failed to init default image", e);
        }
    }

    @Override
    public String randomImage() {
        if (CollectionUtils.isNotEmpty(images)) {
            return images.get(ThreadLocalRandom.current().nextInt(images.size()));
        }
        return "";
    }
}
