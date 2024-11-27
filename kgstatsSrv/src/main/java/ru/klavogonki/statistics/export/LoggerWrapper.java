package ru.klavogonki.statistics.export;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

@Log4j2
public class LoggerWrapper {

    private final Logger internalLogger;
    private final String loggerPrefix;
    private final String loggerPrefixFull;

    public LoggerWrapper(Logger internalLogger, String loggerPrefix) {
        this.internalLogger = internalLogger;
        this.loggerPrefix = loggerPrefix;
        this.loggerPrefixFull = String.format("[%s]: ", loggerPrefix);

        logger.info("LoggerWrapper initialized for loggerPrefix = \"{}\".", loggerPrefix);
    }

    private String formatMessage(String message) { // appends prefix
        // todo: maybe String.format will work for message as well, but let's not risk
        return loggerPrefixFull + message;
    }

    public void debug(String message, Object p0) {
        internalLogger.debug(formatMessage(message), p0);
    }

    public void debug(String message, Object p0, Object p1) {
        internalLogger.debug(formatMessage(message), p0, p1);
    }

    public void warn(String message, Object p0) {
        internalLogger.warn(formatMessage(message), p0);
    }

    public void warn(String message, Object p0, Object p1) {
        internalLogger.warn(formatMessage(message), p0, p1);
    }
}
