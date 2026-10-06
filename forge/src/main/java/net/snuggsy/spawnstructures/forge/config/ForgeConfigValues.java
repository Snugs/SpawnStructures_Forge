package net.snuggsy.spawnstructures.forge.config;

import java.util.List;

public final class ForgeConfigValues {
    // Starter Structure Spawn Location
    public static boolean setWorldSpawn() {
        return SpawnStructuresConfig_Forge.setWorldSpawn.get();
    }

    public static String specifiedLocation() {
        return SpawnStructuresConfig_Forge.specifiedLocation.get();
    }

    public static String setBiome() {
        return SpawnStructuresConfig_Forge.setBiome.get();
    }

    public static List<? extends String> biomeExclusionList() {
        return SpawnStructuresConfig_Forge.biomeExclusionList.get();
    }

    // Gamerule Overrides
    public static boolean ignoreGameruleGenStructures() {
        return SpawnStructuresConfig_Forge.ignoreGameruleGenStructures.get();
    }

    public static boolean ignoreGameruleSpawnRadius() {
        return SpawnStructuresConfig_Forge.ignoreGameruleSpawnRadius.get();
    }

    public static int setSpawnRadius() {
        return SpawnStructuresConfig_Forge.setSpawnRadius.get();
    }

    public static String setPlayerSpawnAngle() {
        return SpawnStructuresConfig_Forge.setPlayerSpawnAngle.get();
    }

    // Starter Structure Selection
    public static String setStarterStructure() {
        return SpawnStructuresConfig_Forge.setStarterStructure.get();
    }

    public static List<? extends String> structureExclusionList() {
        return SpawnStructuresConfig_Forge.structureExclusionList.get();
    }
}
