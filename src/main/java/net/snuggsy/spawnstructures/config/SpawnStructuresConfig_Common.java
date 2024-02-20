package net.snuggsy.spawnstructures.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.snuggsy.spawnstructures.util.References;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SpawnStructuresConfig_Common {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> configVersion;

    public static final ForgeConfigSpec.ConfigValue<Boolean> setWorldSpawn;
    public static final ForgeConfigSpec.ConfigValue<String> specifiedLocation;
    public static final ForgeConfigSpec.ConfigValue<String> setBiome;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleGenStructures;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleSpawnRadius;
    public static final ForgeConfigSpec.ConfigValue<Integer> setSpawnRadius;
    public static final ForgeConfigSpec.ConfigValue<String> setPlayerSpawnAngle;
    public static final List<String> spawnOrientationOptions = List.of(
            "STRUCTURE_LOCKED", "STRUCTURE LOCKED",
            "RANDOMIZED", "RANDOMISED",
            "NORTH",
            "EAST",
            "SOUTH",
            "WEST"
    );

    public static final ForgeConfigSpec.ConfigValue<String> setStarterStructure;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> exclusionList;
    public static final List<String> starterStructureOptions = List.of(
            "BIOME_DEPENDENT", "BIOME_DEPENDANT", "BIOME DEPENDENT", "BIOME DEPENDANT",
            "RANDOMIZED", "RANDOMISED",
            "CHERRY_BLOSSOM", "CHERRY BLOSSOM",
            "LOG_CABIN", "LOG CABIN",
            "SAND_CASTLE", "SAND CASTLE"
    );
    private static final List<? extends String> empty = Collections.emptyList();
    private static final Predicate<Object> validator = o -> o instanceof String && ((String) o).contains(":");

    static {
        BUILDER.push("Common Configs for " + References.NAME);

        configVersion = BUILDER.comment(" Mod Version:      v" + References.VERSION)
                .comment(" Config Version:   v" + References.CONFIG_VERSION)
                .comment("------------------------#")
                .define("Config Version", References.CONFIG_VERSION);

        BUILDER.comment("----------------------------------#");

        BUILDER.pop();
        BUILDER.push("Starter Structure Spawn Location");

        setWorldSpawn = BUILDER.comment(" Should the Starter Structure spawn at a specific location?")
                .define("Set World Spawn",true);
        specifiedLocation = BUILDER.comment(" Which specific location should the Starter Structure generate at? [x,z]")
                .define("Specify World Spawn Location", "[0,0]");
        setBiome = BUILDER.comment(" Should the Starter Structure spawn in a specific biome? (Takes priority over \"Set World Spawn\" if not set to \"ANY\")")
                .comment("    Example 1: \"MINECRAFT:CHERRY_GROVE\" will search specifically for the Cherry Grove biome.")
                .comment("    Example 2: \"TAIGA\" will search for ANY Taiga biome, including the Snowy Taiga and Old Growth Taiga biomes.")
                .define("Set Spawn Biome", "ANY");

        BUILDER.comment("--------------------#");

        BUILDER.pop();
        BUILDER.push("GameRule Overrides");

        ignoreGameruleGenStructures = BUILDER.comment(" Should the Starter Structure spawn even if Generate Structures is set to false?")
                .define("Ignore GameRule: Generate Structures",true);
        ignoreGameruleSpawnRadius = BUILDER.comment(" Should the Spawn Radius set in the World Options be ignored?")
                .define("Ignore GameRule: Spawn Radius", true);
        setSpawnRadius = BUILDER.comment(" How big should the new Spawn Radius be? (Only applies if \"Ignore GameRule: Spawn Radius\" is true)")
                .defineInRange("Spawn Radius", 0, 0, 4);
        setPlayerSpawnAngle = BUILDER.comment(" Which direction should the player spawn facing? (\"STRUCTURE_LOCKED\" usually means facing the Spawn Structure's door)")
                .comment(" Values: \"STRUCTURE_LOCKED\", \"RANDOMIZED\", \"NORTH\", \"EAST\", \"SOUTH\", \"WEST\"   -->   Default Value: \"STRUCTURE_LOCKED\"")
                .define("Spawn Orientation", spawnOrientationOptions.get(0));

        BUILDER.comment("---------------------------------#");

        BUILDER.pop();
        BUILDER.push("Starter Structure Customization");

        setStarterStructure = BUILDER.comment(" Which Starter Structure should generate at the world Spawn Location?")
                .comment(" Values: \"BIOME_DEPENDENT\", \"RANDOMIZED\", \"CHERRY_BLOSSOM\", \"LOG_CABIN\", \"SAND_CASTLE\"")
                .define("Generated Starter Structure", starterStructureOptions.get(0));
        exclusionList = BUILDER.comment(" Which Starter Structure(s) should be excluded from Biome Dependent and Randomized generation?")
                .defineListAllowEmpty("Exclusion List", empty, validator);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
