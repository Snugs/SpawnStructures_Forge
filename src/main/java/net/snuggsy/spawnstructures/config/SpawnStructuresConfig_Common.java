package net.snuggsy.spawnstructures.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpawnStructuresConfig_Common {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> setWorldSpawn;
    public static final ForgeConfigSpec.ConfigValue<String> specifiedLocation;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleGenStructures;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleSpawnRadius;
    public static final ForgeConfigSpec.ConfigValue<Integer> setSpawnRadius;

    static {
        BUILDER.push("Configs for Spawn Structures");

        BUILDER.comment("---------------------------------#");

        BUILDER.pop();
        BUILDER.push("Starter Structure Spawn Location");

        setWorldSpawn = BUILDER.comment(" Should the Starter Structure spawn at a specific location?")
                .define("Set World Spawn",true);
        specifiedLocation = BUILDER.comment(" Which specific location should the Starter Structure generate at? [x,z]")
                .define("Specify World Spawn Location", "[0,0]");

        BUILDER.comment("--------------------#");
        BUILDER.pop();
        BUILDER.push("GameRule Overrides");

        ignoreGameruleGenStructures = BUILDER.comment(" Should the Starter Structure spawn even if Generate Structures is set to false?")
                .define("Ignore GameRule: Generate Structures",true);
        ignoreGameruleSpawnRadius = BUILDER.comment(" Should the Spawn Radius set in the World Options be ignored?")
                .define("Ignore GameRule: Spawn Radius", true);
        setSpawnRadius = BUILDER.comment(" How big should the new Spawn Radius be? (Only applies if 'Ignore GameRule: Spawn Radius' is true)")
                .defineInRange("Spawn Radius", 0, 0, 4);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
