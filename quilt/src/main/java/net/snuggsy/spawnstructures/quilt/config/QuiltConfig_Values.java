package net.snuggsy.spawnstructures.quilt.config;

import me.shedaniel.autoconfig.AutoConfig;

import java.util.List;

public final class QuiltConfig_Values {
    // Getter
    private static QuiltConfig_Builder get() {
        return AutoConfig.getConfigHolder(QuiltConfig_Builder.class).getConfig();
    }

    // Accessors
    public static boolean setWorldSpawn() {
        return get().setWorldSpawn;
    }
    public static String specifiedLocation() {
        return get().specifiedLocation;
    }
    public static String setBiome() {
        return get().setBiome;
    }
    public static List<String> biomeExclusionList() {
        return get().biomeExclusionList;
    }

    public static boolean ignoreGameruleGenStructures() {
        return get().ignoreGameruleGenStructures;
    }
    public static boolean ignoreGameruleSpawnRadius() {
        return get().ignoreGameruleSpawnRadius; }
    public static int setSpawnRadius() { return get().setSpawnRadius;
    }
    public static String setPlayerSpawnAngle() {
        return get().setPlayerSpawnAngle;
    }

    public static String setStarterStructure() {
        return get().setStarterStructure;
    }
    public static List<String> structureExclusionList() {
        return get().structureExclusionList;
    }
}
