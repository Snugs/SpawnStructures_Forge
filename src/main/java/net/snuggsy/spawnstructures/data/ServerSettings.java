package net.snuggsy.spawnstructures.data;

import net.snuggsy.spawnstructures.config.SpawnStructuresConfig_Common;

public class ServerSettings {

    public static boolean setWorldSpawn = SpawnStructuresConfig_Common.setWorldSpawn.get();

    public static String specifiedLocation = SpawnStructuresConfig_Common.specifiedLocation.get();

    public static int spawnRadius = SpawnStructuresConfig_Common.setSpawnRadius.get();

    public static String spawnOrientation = SpawnStructuresConfig_Common.setPlayerSpawnAngle.get(); // Options: STRUCTURE_LOCKED (Facing the door), RANDOMIZED, NORTH, EAST, SOUTH, WEST

    public static String biomeSelected = SpawnStructuresConfig_Common.setBiome.get(); // Options: ANY, (A list of all loaded biomes)

    public static boolean biomeDependantStructures = true;

    public static String structureSelected = "BIOME_DEPENDANT"; // Options: BIOME_DEPENDANT, CHERRY_BLOSSOM

    public static String substructure = "SUBSTRATE"; // Options: SUBSTRATE, MAP_ROOM

    public static boolean ignoreGameruleGenStructures = SpawnStructuresConfig_Common.ignoreGameruleGenStructures.get();

    public static boolean ignoreGameruleSpawnRadius = SpawnStructuresConfig_Common.ignoreGameruleSpawnRadius.get();

}
