package net.snuggsy.spawnstructures.config;

public class ServerSettings {

    public static boolean spawnWorldCentre = SpawnStructuresCommonConfigs.spawnWorldCentre.get();

    public static int spawnRadius = 0; // Options: -1 < spawnRadius < 4

    public static String spawnOrientation = "STRUCTURE_LOCKED"; // Options: STRUCTURE_LOCKED (Facing the door), RANDOMIZED, NORTH, EAST, SOUTH, WEST

    public static String biomeSelected = "ANY"; // Options: ANY, (A list of all loaded biomes)

    public static boolean biomeDependantStructures = true;

    public static String structureSelected = "BIOME_DEPENDANT"; // Options: BIOME_DEPENDANT, CHERRY_BLOSSOM

    public static String substructure = "SUBSTRATE"; // Options: SUBSTRATE, MAP_ROOM

    public static boolean ignoreGameruleGenStructures = SpawnStructuresCommonConfigs.ignoreGameruleGenStructures.get();

    public static boolean ignoreGameruleSpawnRadius = true;

}
