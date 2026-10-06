package net.snuggsy.spawnstructures.common.config;

import net.snuggsy.spawnstructures.common.data.References;

import java.util.List;

public final class CommonConfig_Defaults {
    public static final String CONFIG_VERSION = References.CONFIG_VERSION;

    // Starter Structure Spawn Location
    public static final Boolean DEFAULT_SET_WORLD_SPAWN = true;
    public static final String DEFAULT_SPECIFIED_LOCATION = "[0,0]";
    public static final String DEFAULT_SPAWN_BIOME = "ANY";
    public static final List<String> DEFAULT_BIOME_EXCLUSIONS = List.of();

    // GameRule Overrides
    public static final Boolean DEFAULT_IGNORE_GEN_STRUCTURES = true;
    public static final Boolean DEFAULT_IGNORE_SPAWN_RADIUS = true;
    public static final int DEFAULT_SPAWN_RADIUS = 0;
    public static final String DEFAULT_PLAYER_SPAWN_ANGLE = "STRUCTURE_LOCKED";

    public static final List<String> PLAYER_SPAWN_ANGLE_OPTIONS = List.of(
            "STRUCTURE_LOCKED", "STRUCTURE LOCKED",
            "RANDOMIZED", "RANDOMISED",
            "NORTH", "EAST", "SOUTH", "WEST"
    );

    // Starter Structure Selection
    public static final String DEFAULT_STARTER_STRUCTURE = "BIOME_DEPENDANT";
    public static final List<String> DEFAULT_STRUCTURE_EXCLUSIONS = List.of();

    public static final List<String> STARTER_STRUCTURE_OPTIONS = List.of(
            "BIOME_DEPENDENT", "BIOME_DEPENDANT", "BIOME DEPENDENT", "BIOME DEPENDANT",
            "RANDOMIZED", "RANDOMISED",
            "CHERRY_BLOSSOM", "CHERRY BLOSSOM",
            "LOG_CABIN", "LOG CABIN",
            "SAND_CASTLE", "SAND CASTLE"
    );

    private CommonConfig_Defaults() {}
}