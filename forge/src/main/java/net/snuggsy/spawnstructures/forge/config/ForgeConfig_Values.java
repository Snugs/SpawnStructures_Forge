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
        setWorldSpawn = SpawnStructuresConfig_Forge.setWorldSpawn.get();
        specifiedLocation = SpawnStructuresConfig_Forge.specifiedLocation.get();
        setBiome = SpawnStructuresConfig_Forge.setBiome.get();
        biomeExclusionList = List.copyOf(SpawnStructuresConfig_Forge.biomeExclusionList.get());

        ignoreGameruleGenStructures = SpawnStructuresConfig_Forge.ignoreGameruleGenStructures.get();
        ignoreGameruleSpawnRadius = SpawnStructuresConfig_Forge.ignoreGameruleSpawnRadius.get();
        setSpawnRadius = SpawnStructuresConfig_Forge.setSpawnRadius.get();
        setPlayerSpawnAngle = SpawnStructuresConfig_Forge.setPlayerSpawnAngle.get();

        setStarterStructure = SpawnStructuresConfig_Forge.setStarterStructure.get();
        structureExclusionList = List.copyOf(SpawnStructuresConfig_Forge.structureExclusionList.get());
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
