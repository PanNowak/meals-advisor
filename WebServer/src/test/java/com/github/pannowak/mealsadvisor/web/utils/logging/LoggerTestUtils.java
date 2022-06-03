package com.github.pannowak.mealsadvisor.web.utils.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoggerTestUtils {

    public static List<ILoggingEvent> getLoggingEventsForClass(Class<?> clazz) {
        ListAppender<ILoggingEvent> loggingEventListAppender = new ListAppender<>();
        loggingEventListAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(clazz);
        logger.addAppender(loggingEventListAppender);

        return loggingEventListAppender.list;
    }
}
