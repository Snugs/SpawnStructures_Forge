package net.snuggsy.spawnstructures.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.snuggsy.spawnstructures.common.config.CommonConfig_Defaults;
import net.snuggsy.spawnstructures.common.config.CommonConfig_Validators;
import net.snuggsy.spawnstructures.common.data.References;

import java.util.List;

public final class SpawnStructuresConfig_Forge {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> configVersion;

    // Starter Structure Spawn Location
    public static final ForgeConfigSpec.ConfigValue<Boolean> setWorldSpawn;
    public static final ForgeConfigSpec.ConfigValue<String> specifiedLocation;
    public static final ForgeConfigSpec.ConfigValue<String> setBiome;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> biomeExclusionList;

    // GameRule Overrides
    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleGenStructures;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleSpawnRadius;
    public static final ForgeConfigSpec.ConfigValue<Integer> setSpawnRadius;
    public static final ForgeConfigSpec.ConfigValue<String> setPlayerSpawnAngle;

    // Starter Structure Selection
    public static final ForgeConfigSpec.ConfigValue<String> setStarterStructure;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> structureExclusionList;

    // BUILDER
    static {
        // Config Version
        BUILDER.push("Common Configs for " + References.NAME);

        configVersion = BUILDER.comment(" Mod Version:      v" + References.VERSION)
                .comment(" Config Version:   v" + CommonConfig_Defaults.CONFIG_VERSION)
                .comment("------------------------#")
                .define("Config Version", CommonConfig_Defaults.CONFIG_VERSION);

        BUILDER.comment("----------------------------------#");
        BUILDER.pop();

        // Starter Structure Spawn Location
        BUILDER.push("Starter Structure Spawn Location");

        setWorldSpawn = BUILDER.comment(" Should the Starter Structure spawn at a specific location?")
                .define(
                        "Set World Spawn",
                        CommonConfig_Defaults.DEFAULT_SET_WORLD_SPAWN
                );
        specifiedLocation = BUILDER.comment(" Which specific location should the Starter Structure generate at? [x,z]")
                .define(
                        "Specify World Spawn Location",
                        CommonConfig_Defaults.DEFAULT_SPECIFIED_LOCATION
                );
        setBiome = BUILDER.comment(" Should the Starter Structure spawn in a specific biome? (Takes priority over \"Set World Spawn\" if not set to \"ANY\")")
                .comment("    Example 1: \"MINECRAFT:CHERRY_GROVE\" will search specifically for the Cherry Grove biome.")
                .comment("    Example 2: \"TAIGA\" will search for ANY Taiga biome, including the Snowy Taiga and Old Growth Taiga biomes.")
                .define(
                        "Set Spawn Biome",
                        CommonConfig_Defaults.DEFAULT_SPAWN_BIOME
                );
        biomeExclusionList = BUILDER.comment(" Which Biome(s) should be excluded from the search when \"Set Spawn Biome\" is NOT set to \"ANY\" or \"ALL\"?")
                .defineListAllowEmpty(
                        "Exclusion List",
                        CommonConfig_Defaults.DEFAULT_BIOME_EXCLUSIONS,
                        o -> o instanceof String s && CommonConfig_Validators.isValidBiome(s)
                );

        BUILDER.comment("--------------------#");
        BUILDER.pop();

        // GameRule Overrides
        BUILDER.push("GameRule Overrides");

        ignoreGameruleGenStructures = BUILDER.comment(" Should the Starter Structure spawn even if Generate Structures is set to false?")
                .define(
                        "Ignore GameRule: Generate Structures",
                        CommonConfig_Defaults.DEFAULT_IGNORE_GEN_STRUCTURES
                );
        ignoreGameruleSpawnRadius = BUILDER.comment(" Should the Spawn Radius set in the World Options be ignored?")
                .define(
                        "Ignore GameRule: Spawn Radius",
                        CommonConfig_Defaults.DEFAULT_IGNORE_SPAWN_RADIUS
                );
        setSpawnRadius = BUILDER.comment(" How big should the new Spawn Radius be? (Only applies if \"Ignore GameRule: Spawn Radius\" is true)")
                .defineInRange(
                        "Spawn Radius",
                        CommonConfig_Defaults.DEFAULT_SPAWN_RADIUS,
                        0,
                        4
                );
        setPlayerSpawnAngle = BUILDER.comment(" Which direction should the player spawn facing? (\"STRUCTURE_LOCKED\" usually means facing the Spawn Structure's door)")
                .comment(" Values: \"STRUCTURE_LOCKED\", \"RANDOMIZED\", \"NORTH\", \"EAST\", \"SOUTH\", \"WEST\"   -->   Default Value: \"STRUCTURE_LOCKED\"")
                .define(
                        "Spawn Orientation",
                        CommonConfig_Defaults.DEFAULT_PLAYER_SPAWN_ANGLE
                );

        BUILDER.comment("---------------------------------#");
        BUILDER.pop();

        // Starter Structure Selection
        BUILDER.push("Starter Structure Customization");

        setStarterStructure = BUILDER.comment(" Which Starter Structure should generate at the world Spawn Location?")
                .comment(" Values: \"BIOME_DEPENDENT\", \"RANDOMIZED\", \"CHERRY_BLOSSOM\", \"LOG_CABIN\", \"SAND_CASTLE\"")
                .define(
                        "Generated Starter Structure",
                        CommonConfig_Defaults.DEFAULT_STARTER_STRUCTURE
                );
        structureExclusionList = BUILDER.comment(" Which Starter Structure(s) should be excluded from Biome Dependent and Randomized generation?")
                .defineListAllowEmpty(
                        "Exclusion List",
                        CommonConfig_Defaults.DEFAULT_STRUCTURE_EXCLUSIONS,
                        o -> o instanceof String s && CommonConfig_Validators.isValidStructureName(s)
                );

        BUILDER.pop();

        // Build Config
        SPEC = BUILDER.build();
    }
}