package net.snuggsy.spawnstructures.common.config;

public final class CommonConfig_Validators {

    // Biomes
    public static boolean isValidBiome(String biome) {
        return biome != null && biome.contains(":");
    }

    // Structures
    public static boolean isValidStructureName(String name) {
        return name != null && !name.isEmpty();
    }
}