/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
