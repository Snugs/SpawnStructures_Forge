package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.snuggsy.spawnstructures.config.SpawnStructuresConfig_Common;
import net.snuggsy.spawnstructures.data.ServerSettings;

import static net.snuggsy.spawnstructures.data.GlobalVariables.LOGGER;
import static net.snuggsy.spawnstructures.data.GlobalVariables.chosenStructure;
import static net.snuggsy.spawnstructures.data.StructureCoordinates.getStructureOffset;
import static net.snuggsy.spawnstructures.data.StructureCoordinates.getStructureSize;

public class BlockRotFunctions {

    // Set the Player Spawn Rotation
    public static float spawnRot(Rotation structRot) {
        float spawnRot = 0.0F;
        String spawnOrientation = ServerSettings.spawnOrientation.toUpperCase();
        if (!SpawnStructuresConfig_Common.spawnOrientationOptions.contains(spawnOrientation)) {
            LOGGER.error("[Spawn Structures] \"Spawn Orientation\" config option invalid! Reverting to Structure Locked...");
            spawnOrientation = "STRUCTURE_LOCKED";
        }
        switch (spawnOrientation) {
            // Randomization happens during respawn
            case "STRUCTURE_LOCKED", "STRUCTURE LOCKED", "RANDOMIZED", "RANDOMISED" -> {
                if (structRot == Rotation.NONE) {
                    spawnRot = 180.0F;
                } else if (structRot == Rotation.COUNTERCLOCKWISE_90) {
                    spawnRot = 90.0F;
                } else if (structRot == Rotation.CLOCKWISE_180) {
                    spawnRot = 0.0F;
                } else {
                    spawnRot = -90.0F;
                }
            }
            case "NORTH" -> spawnRot = 180.0F;
            case "EAST" -> spawnRot = 90.0F;
            case "SOUTH" -> spawnRot = 0.0F;
            case "WEST" -> spawnRot = -90.0F;
        }
        return spawnRot;
    }

    // Offset the Player Spawn Location dependent on Structure Rotation
    public static BlockPos offsetSpawn (Rotation structureRotation, int spawnX, int spawnZ) {
        BlockPos offset = getStructureOffset(chosenStructure);
        if (structureRotation == Rotation.NONE){
            spawnX -= offset.getX();
            spawnZ -= offset.getZ();
        } else if (structureRotation == Rotation.COUNTERCLOCKWISE_90) {
            spawnX -= offset.getX();
            spawnZ += offset.getZ();
        } else if (structureRotation == Rotation.CLOCKWISE_180) {
            spawnX += offset.getX();
            spawnZ += offset.getZ();
        } else {
            spawnX += offset.getX();
            spawnZ -= offset.getZ();
        }
        return new BlockPos(spawnX, 0, spawnZ);
    }

    // Offset the Starter Structure Spawn Location dependent on Structure Rotation
    public static BlockPos offsetLocation (BlockPos structureLocation, Rotation structureRotation) {
        BlockPos offset = getStructureOffset(chosenStructure);
        BlockPos size = getStructureSize(chosenStructure);
        int structX = structureLocation.getX();
        int structZ = structureLocation.getZ();
        if (structureRotation == Rotation.CLOCKWISE_90) {
            structX += size.getX() + offset.getX() - 1;
            structZ += offset.getZ();
        } else if (structureRotation == Rotation.CLOCKWISE_180) {
            structX += size.getX() + offset.getX() - 1;
            structZ += size.getZ() + offset.getZ() - 1;
        } else if (structureRotation == Rotation.COUNTERCLOCKWISE_90) {
            structX += offset.getX();
            structZ += size.getZ() + offset.getZ() - 1;
        } else {
            structX += offset.getX();
            structZ += offset.getZ();
        }
        return new BlockPos(structX, structureLocation.getY(), structZ);
    }
}
