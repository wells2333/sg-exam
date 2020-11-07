package com.github.tangyi.user.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 *
 * @author tangyi
 * @date 2018-09-14-20:34
 */
@Configuration
public class ValidateCodeConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        // 验证码字体颜色
        properties.put("kaptcha.textproducer.font.color", "black");
        // 验证码字体大小
        properties.put("kaptcha.textproducer.font.size", "40");
        // 验证码文本字符间距
        properties.put("kaptcha.textproducer.char.space", "6");
        // 验证码宽度
        properties.put("kaptcha.image.width", "130");
        // 验证码高度
        properties.put("kaptcha.image.height", "50");
        properties.put("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
