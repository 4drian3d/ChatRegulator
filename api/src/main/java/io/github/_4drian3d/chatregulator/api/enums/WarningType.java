package io.github._4drian3d.chatregulator.api.enums;

/**
 * The warning format to be executed
 */
public enum WarningType {
    /**
     * Title type format. Will send only a subtitle
     * if the ";" character is not supplied.
     */
    TITLE,
    /**
     * Actionbar type format.
     */
    ACTIONBAR,
    /**
     * Simple message format
     */
    MESSAGE
}
