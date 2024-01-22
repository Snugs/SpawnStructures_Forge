package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.ServerLevelData;
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;
import net.snuggsy.spawnstructures.functions.CommandFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructureSpawnEvent {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureSpawnEvent.class);
    public static Rotation structureRotation;
    public static boolean genStructures = true;
    public static boolean worldInit = false;
    public static boolean genFailed = false;

    public static boolean onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
        WorldOptions worldGeneratorOptions = serverLevel.getServer().getWorldData().worldGenOptions();
        String spawnDistance = CommandFunctions.getRawCommandOutput(serverLevel, null, "/gamerule spawnRadius 0");

        if (!worldGeneratorOptions.generateStructures()) {
            genStructures = false;
            return false;
        }

        SpawnStructures.spawnPos = BlockPosFunctions.getCenterStarterStructure(serverLevel);
        if (SpawnStructures.spawnPos == null) {
            LOGGER.error("spawnPos == NULL");
            /*LOGGER.error("Placing Starter Structure...");
            double defX = serverLevel.getLevelData().getXSpawn();
            double defY = serverLevel.getLevelData().getYSpawn();
            double defZ = serverLevel.getLevelData().getZSpawn();
            String placeStructure = CommandFunctions.getRawCommandOutput(serverLevel, null, "/place structure spawn-structures:starter_structure " + defX + " " + defY + " " + defZ);
            SpawnStructures.spawnPos = BlockPosFunctions.getCenterStarterStructure(serverLevel);
            */
            if (SpawnStructures.spawnPos == null) {
                genFailed = true;
                return false;
            }
        }

        serverLevel.setDefaultSpawnPos(SpawnStructures.spawnPos, SpawnStructures.spawnRot);
        worldInit = true;

        return true;
    }
}