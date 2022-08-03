package me.adrianed.chatregulator.api.logger;

import org.slf4j.Logger;

public final class SLF4JLogger implements RegulatorLogger {
    private final Logger logger;
    public SLF4JLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String string) {
        logger.info(string);
    }

    @Override
    public void info(String string, Throwable throwable) {
        logger.info(string, throwable);
    }

    @Override
    public void info(String string, Object... arguments) {
        logger.info(string, arguments);
    }

    @Override
    public void warn(String string) {
        logger.warn(string);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        logger.warn(string, throwable);
    }

    @Override
    public void warn(String string, Object... arguments) {
        logger.warn(string, arguments);
    }

    @Override
    public void error(String string) {
        logger.error(string);
    }

    @Override
    public void error(String string, Throwable throwable) {
        logger.error(string, throwable);
    }

    @Override
    public void error(String string, Object... arguments) {
        logger.error(string, arguments);
    }
    
}
