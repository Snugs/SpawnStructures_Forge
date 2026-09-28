package net.snuggsy.spawnstructures.common.util;

import org.slf4j.LoggerFactory;

public final class Logger {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);

    // Development Environment
    public static final boolean devEnv = true; // Set to False before building mod

    public static void devLog(String value) {
        if (devEnv) {
            LOGGER.info(value);
        }
    }

    // Log in Prod
    public static void Log(String value) {
        LOGGER.info(value);
    }

    public static void LogError(String value) {
        LOGGER.error(value);
    }
}