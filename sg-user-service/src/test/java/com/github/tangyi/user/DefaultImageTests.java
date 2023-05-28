package com.github.tangyi.user;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DefaultImageTests {

    @Test
    public void testGenerateImages() {
        List<String> list = Lists.newArrayList();
        for (int i = 1; i < 30; i++) {
            list.add("default_image/" + i + ".jpeg");
        }
        System.out.println(JSON.toJSONString(list));
    }
}
