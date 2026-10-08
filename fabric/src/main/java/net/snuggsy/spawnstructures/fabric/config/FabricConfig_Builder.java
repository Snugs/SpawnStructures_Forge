package net.snuggsy.spawnstructures.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.snuggsy.spawnstructures.common.config.CommonConfig_Defaults;
import net.snuggsy.spawnstructures.common.data.References;

import java.util.List;

@Config(name = References.CONFIG_FILENAME)
public class FabricConfig_Builder implements ConfigData {
    // Starter Structure Spawn Location
    public boolean setWorldSpawn = CommonConfig_Defaults.DEFAULT_SET_WORLD_SPAWN;
    public String specifiedLocation = CommonConfig_Defaults.DEFAULT_SPECIFIED_LOCATION;
    public String setBiome = CommonConfig_Defaults.DEFAULT_SPAWN_BIOME;
    public List<String> biomeExclusionList = CommonConfig_Defaults.DEFAULT_BIOME_EXCLUSIONS;

    // GameRule Overrides
    public boolean ignoreGameruleGenStructures = CommonConfig_Defaults.DEFAULT_IGNORE_GEN_STRUCTURES;
    public boolean ignoreGameruleSpawnRadius = CommonConfig_Defaults.DEFAULT_IGNORE_SPAWN_RADIUS;
    public int setSpawnRadius = CommonConfig_Defaults.DEFAULT_SPAWN_RADIUS;
    public String setPlayerSpawnAngle = CommonConfig_Defaults.DEFAULT_PLAYER_SPAWN_ANGLE;

    // Starter Structure Selection
    public String setStarterStructure = CommonConfig_Defaults.DEFAULT_STARTER_STRUCTURE;
    public List<String> structureExclusionList = CommonConfig_Defaults.DEFAULT_STRUCTURE_EXCLUSIONS;
}
