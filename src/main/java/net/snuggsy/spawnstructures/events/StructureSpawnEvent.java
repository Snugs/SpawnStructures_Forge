package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.ServerLevelData;
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;

public class StructureSpawnEvent {
    public static Rotation structureRotation;
    public static boolean genStructures = true;

    public static boolean onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
        WorldOptions worldGeneratorOptions = serverLevel.getServer().getWorldData().worldGenOptions();

        if (!worldGeneratorOptions.generateStructures()) {
            genStructures = false;
            return false;
        }

        SpawnStructures.spawnPos = BlockPosFunctions.getCenterStarterStructure(serverLevel);
        if (SpawnStructures.spawnPos == null) {
            return false;
        }

        serverLevel.setDefaultSpawnPos(SpawnStructures.spawnPos, SpawnStructures.spawnRot);

        return true;
    }
}