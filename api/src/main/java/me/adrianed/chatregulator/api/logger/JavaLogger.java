package me.adrianed.chatregulator.api.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class JavaLogger implements RegulatorLogger {
    private final Logger logger;

    public JavaLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String string) {
        this.logger.info(string);
        
    }

    @Override
    public void info(String string, Throwable throwable) {
        this.logger.log(Level.INFO, string, throwable);
    }

    @Override
    public void info(String string, Object... arguments) {
        this.logger.log(Level.INFO, string, arguments);
    }

    @Override
    public void warn(String string) {
        this.logger.log(Level.WARNING, string);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        this.logger.log(Level.WARNING, string, throwable);
    }

    @Override
    public void warn(String string, Object... arguments) {
        this.logger.log(Level.WARNING, string, arguments);
    }

    @Override
    public void error(String string) {
        this.logger.log(Level.SEVERE, string);
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.logger.log(Level.SEVERE, string, throwable);
    }

    @Override
    public void error(String string, Object... arguments) {
        this.logger.log(Level.SEVERE, string, arguments);
    }
    
}
