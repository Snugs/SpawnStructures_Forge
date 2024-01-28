package net.snuggsy.spawnstructures.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpawnStructuresCommonConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> spawnWorldCentre;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ignoreGameruleGenStructures;

    static {
        BUILDER.push("Configs for Spawn Structures");

        spawnWorldCentre = BUILDER.comment("Should the Starter Structure spawn at the world centre? [0,~,0]")
                .define("Spawn World Centre",true);
        ignoreGameruleGenStructures = BUILDER.comment("Should the Starter Structure spawn even if Generate Structures is set to false?")
                .define("Ignore GameRule: Generate Structures",true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
