package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.snuggsy.spawnstructures.data.ServerSettings;

import static net.snuggsy.spawnstructures.data.StructureCoordinates.*;
import static net.snuggsy.spawnstructures.data.GlobalVariables.*;

public class BlockRotFunctions {

    // Set the Player Spawn Rotation
    public static float spawnRot(Rotation structRot) {
        float spawnRot = 0.0F;
        switch (ServerSettings.spawnOrientation.toUpperCase()) {
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
        if (structureRotation == Rotation.NONE){
            spawnX -= spawnOffset_CherryBlossom.getX();
            spawnZ -= spawnOffset_CherryBlossom.getZ();
        } else if (structureRotation == Rotation.COUNTERCLOCKWISE_90) {
            spawnX -= spawnOffset_CherryBlossom.getX();
            spawnZ += spawnOffset_CherryBlossom.getZ();
        } else if (structureRotation == Rotation.CLOCKWISE_180) {
            spawnX += spawnOffset_CherryBlossom.getX();
            spawnZ += spawnOffset_CherryBlossom.getZ();
        } else {
            spawnX += spawnOffset_CherryBlossom.getX();
            spawnZ -= spawnOffset_CherryBlossom.getZ();
        }
        return new BlockPos(spawnX, 0, spawnZ);
    }

    // Offset the Starter Structure Spawn Location dependent on Structure Rotation
    public static BlockPos offsetLocation (Rotation structureRotation) {
        int structX = structureLocation.getX();
        int structZ = structureLocation.getZ();
        if (structureRotation == Rotation.CLOCKWISE_90) {
            structX += boundingSize_CherryBlossom.getX() + spawnOffset_CherryBlossom.getX() - 1;
            structZ += spawnOffset_CherryBlossom.getZ();
        } else if (structureRotation == Rotation.CLOCKWISE_180) {
            structX += boundingSize_CherryBlossom.getX() + spawnOffset_CherryBlossom.getX() - 1;
            structZ += boundingSize_CherryBlossom.getZ() + spawnOffset_CherryBlossom.getZ() - 1;
        } else if (structureRotation == Rotation.COUNTERCLOCKWISE_90) {
            structX += spawnOffset_CherryBlossom.getX();
            structZ += boundingSize_CherryBlossom.getZ() + spawnOffset_CherryBlossom.getZ() - 1;
        } else {
            structX += spawnOffset_CherryBlossom.getX();
            structZ += spawnOffset_CherryBlossom.getZ();
        }
        return new BlockPos(structX, 64, structZ);
    }
}
