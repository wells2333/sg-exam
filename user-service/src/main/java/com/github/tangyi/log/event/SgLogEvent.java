package com.github.tangyi.log.event;

import com.github.tangyi.common.model.Log;
import org.springframework.context.ApplicationEvent;

public class SgLogEvent extends ApplicationEvent {
    public SgLogEvent(Log source) {
        super(source);
    }
}
