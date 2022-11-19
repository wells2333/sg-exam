package com.github.tangyi.log.event;

import com.github.tangyi.common.model.Log;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author tangyi
 * @date 2019/3/12 23:58
 */
public class SgLogEvent extends ApplicationEvent {
    public SgLogEvent(Log source) {
        super(source);
    }
}
