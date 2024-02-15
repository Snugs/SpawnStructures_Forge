package net.snuggsy.spawnstructures.data;

import net.snuggsy.spawnstructures.config.SpawnStructuresConfig_Common;

public class ServerSettings {

    public static boolean setWorldSpawn = SpawnStructuresConfig_Common.setWorldSpawn.get();

    public static String specifiedLocation = SpawnStructuresConfig_Common.specifiedLocation.get();

    public static int spawnRadius = SpawnStructuresConfig_Common.setSpawnRadius.get();

    public static String spawnOrientation = SpawnStructuresConfig_Common.setPlayerSpawnAngle.get(); // Options: STRUCTURE_LOCKED (Facing the door), RANDOMIZED, NORTH, EAST, SOUTH, WEST

    public static String biomeSelected = SpawnStructuresConfig_Common.setBiome.get(); // Options: ANY, (A list of all loaded biomes)

    public static String structureSelected = SpawnStructuresConfig_Common.setStarterStructure.get(); // Options: BIOME_DEPENDANT, RANDOMIZED, CHERRY_BLOSSOM, LOG_CABIN

    public static String substructure = "SUBSTRATE"; // Options: SUBSTRATE, MAP_ROOM

    public static boolean ignoreGameruleGenStructures = SpawnStructuresConfig_Common.ignoreGameruleGenStructures.get();

    public static boolean ignoreGameruleSpawnRadius = SpawnStructuresConfig_Common.ignoreGameruleSpawnRadius.get();

}
