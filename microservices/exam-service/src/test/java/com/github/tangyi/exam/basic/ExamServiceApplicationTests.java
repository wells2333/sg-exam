package com.github.tangyi.exam.basic;

import com.github.tangyi.common.utils.okhttp.OkHttpUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExamServiceApplicationTests {

    @Test
    public void testQrCode() throws Exception {
        String msg = new String("https://gitee.com/wells2333".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.ISO_8859_1);
        //QRCodeUtils.generateQRCodeImage(msg, 360, 360, "/Users/gaungyi.tan/Downloads/1.png");
    }

    /**
     * 爬取题库
     *
     * @throws Exception
     */
    @Test
    public void testScanSubjects() throws Exception {
        //scanNbdp();
    }

    private void scanNbdp() throws Exception {
        Map<String, List<String>> subjectMap = Maps.newLinkedHashMap();
        String baseUrl = "http://lib.nbdp.net/paper/%s.html";
        for (int i = 1; i < 128; i++) {
            System.out.println("start scan " + i);
            String url = String.format(baseUrl, i);
            String content = OkHttpUtil.getInstance().get(url, Maps.newHashMap());
            if (StringUtils.isNotEmpty(content)) {
                Document parse = Jsoup.parse(content);
                Element body = parse.body();
                Elements elements = body.getElementsByAttributeValue("s", "math3");
                if (CollectionUtils.isNotEmpty(elements)) {
                    for (Element element : elements) {
                        // subject name
                        Elements names = element.getElementsByAttributeValue("class", "pt1");
                        if (CollectionUtils.isNotEmpty(names)) {
                            Element name = names.get(0);
                            String subjectName = name.html();
                            subjectName = subjectName.substring(subjectName.indexOf(".") + 1).trim();
                            // options
                            Elements lis = element.getElementsByTag("li");
                            List<String> options = Lists.newArrayList();
                            if (CollectionUtils.isNotEmpty(lis)) {
                                String answer = null;
                                for (Element li : lis) {
                                    String option = li.html().trim();
                                    if (!option.contains("\n")) {
                                        options.add(option);
                                    }
                                    // answer
                                    Elements clazz = li.getElementsByClass("xz");
                                    if (CollectionUtils.isNotEmpty(clazz)) {
                                        answer = option.split("\\.")[0];
                                    }
                                }

                                if (answer != null) {
                                    options.add(answer);
                                    // score
                                    options.add("5");
                                }
                            }
                            // only 4 options
                            if (StringUtils.isNotEmpty(subjectName) && !subjectName.contains("img") && !subjectName.contains("\n") && options.size() == 6) {
                                subjectMap.put(subjectName, options);
                            }
                        }
                    }
                }
            }
        }
        // write to txt
        System.out.println("start to write txt");
        File out = new File("/tmp/subjects/nbdp.txt");
        FileUtils.deleteQuietly(out);
        subjectMap.forEach((key, value) -> {
            try {
                FileUtils.writeLines(out, Lists.newArrayList(key), true);
                FileUtils.writeLines(out, value, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
