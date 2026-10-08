package net.snuggsy.spawnstructures.forge.config;

import java.util.List;

public final class ForgeConfig_Values {
    // Cached Values
    private static boolean setWorldSpawn;
    private static String specifiedLocation;
    private static String setBiome;
    private static List<String> biomeExclusionList;

    private static boolean ignoreGameruleGenStructures;
    private static boolean ignoreGameruleSpawnRadius;
    private static int setSpawnRadius;
    private static String setPlayerSpawnAngle;

    private static String setStarterStructure;
    private static List<String> structureExclusionList;

    // Cache Refresher
    public static void Refresh() {
        setWorldSpawn = ForgeConfig_Builder.setWorldSpawn.get();
        specifiedLocation = ForgeConfig_Builder.specifiedLocation.get();
        setBiome = ForgeConfig_Builder.setBiome.get();
        biomeExclusionList = List.copyOf(ForgeConfig_Builder.biomeExclusionList.get());

        ignoreGameruleGenStructures = ForgeConfig_Builder.ignoreGameruleGenStructures.get();
        ignoreGameruleSpawnRadius = ForgeConfig_Builder.ignoreGameruleSpawnRadius.get();
        setSpawnRadius = ForgeConfig_Builder.setSpawnRadius.get();
        setPlayerSpawnAngle = ForgeConfig_Builder.setPlayerSpawnAngle.get();

        setStarterStructure = ForgeConfig_Builder.setStarterStructure.get();
        structureExclusionList = List.copyOf(ForgeConfig_Builder.structureExclusionList.get());
    }

    // Accessors
    public static boolean setWorldSpawn() {
        return setWorldSpawn;
    }
    public static String specifiedLocation() {
        return specifiedLocation;
    }
    public static String setBiome() {
        return setBiome;
    }
    public static List<String> biomeExclusionList() {
        return biomeExclusionList;
    }

    public static boolean ignoreGameruleGenStructures() {
        return ignoreGameruleGenStructures;
    }
    public static boolean ignoreGameruleSpawnRadius() {
        return ignoreGameruleSpawnRadius;
    }
    public static int setSpawnRadius() {
        return setSpawnRadius;
    }
    public static String setPlayerSpawnAngle() {
        return setPlayerSpawnAngle;
    }

    public static String setStarterStructure() {
        return setStarterStructure;
    }
    public static List<String> structureExclusionList() {
        return structureExclusionList;
    }
}
