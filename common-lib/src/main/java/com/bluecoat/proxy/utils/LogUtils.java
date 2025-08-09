package com.bluecoat.proxy.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {

    public static void logInfo(String message) {
        log.info(message);
    }

    public static void logError(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    public static void logDebug(String message) {
        log.debug(message);
    }

    public static void logWarn(String message) {
        log.warn(message);
    }
}
