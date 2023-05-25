package io.github._4drian3d.chatregulator.api.enums;

/**
 * Caps Check Detection Algorithm
 */
public enum CapsAlgorithm {
    /**
     * This algorithm will check if there is a higher percentage of uppercase characters
     * in the provided string than the maximum indicated
     */
    PERCENTAGE,
    /**
     * This algorithm will check if there are more uppercase characters
     * in the provided string than the maximum indicated
     */
    AMOUNT
}
