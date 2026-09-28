package net.snuggsy.spawnstructures.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GlobalVariables {
    // Logger
    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariables.class);
    public static final boolean devEnv = true;
    public static void devLog(String value) {
        if (devEnv) {
            LOGGER.info(value);
        }
    }
}
