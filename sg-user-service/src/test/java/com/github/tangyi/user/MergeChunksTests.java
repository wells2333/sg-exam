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

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class MergeChunksTests {

    @Test
    public void testMergeChunks() throws Exception {
        File targetFile = new File("~/Downloads/aaa.mp4");
        List<File> files = Lists.newArrayList();
        for (int i = 1; i < 7; i++) {
            files.add(new File("~/Downloads/" + i));
        }
        try (FileOutputStream outputStream = new FileOutputStream(targetFile);
             FileChannel outChannel = outputStream.getChannel()) {
            long start = 0;
            for (File file : files) {
                try (FileInputStream in = new FileInputStream(file); FileChannel inChannel = in.getChannel()) {
                    outChannel.transferFrom(inChannel, start, file.length());
                    start += file.length();
                }
            }
        }
    }
}
