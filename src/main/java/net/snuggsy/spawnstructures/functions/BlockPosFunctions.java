package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.snuggsy.spawnstructures.data.StructureCoordinates;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.spawnWorldCentre;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.offsetSpawn;
import static net.snuggsy.spawnstructures.functions.NumberFunctions.isNumeric;

public class BlockPosFunctions {

    // Get the Players Spawn Position based on the Specified Location
    public static BlockPos getPlayerSpawnPos(ServerLevel serverLevel, int spawnX, int spawnZ) {
        int highestY = serverLevel.getHeight();
        int lowestY = serverLevel.getMinBuildHeight();

        if (!spawnWorldCentre){
            BlockPos spawnPos = offsetSpawn(structureRotation, spawnX, spawnZ);
            spawnX = spawnPos.getX();
            spawnZ = spawnPos.getZ();
        }

        BlockPos returnPos = new BlockPos(spawnX, highestY-1, spawnZ);
        BlockPos pPos = new BlockPos(spawnX, highestY, spawnZ);
        for (int y = highestY; y > lowestY; y--) {
            BlockState blockState = serverLevel.getBlockState(pPos);
            MapColor material = blockState.getMapColor(serverLevel, pPos);
            if (blockState.getLightBlock(serverLevel, pPos) >= 15 || surfacematerials.contains(material)) {
                returnPos = pPos.above().immutable();
                break;
            }
            pPos = pPos.below();
        }
        return new BlockPos(returnPos.getX(), returnPos.getY() - StructureCoordinates.spawnHeightOffset_CherryBlossom, returnPos.getZ()).immutable();
    }

    // Get the closest Starter Structure to the centre of the world
    public static BlockPos getStarterStructureFromCentre(ServerLevel serverLevel) {
        return getStarterStructure(serverLevel, new BlockPos(0, 0, 0));
    }
    // Get the closest Starter Structure from the specified coordinates
    public static BlockPos getStarterStructure(ServerLevel serverLevel, BlockPos nearPos) {
        if (!worldGenOptions.generateStructures()) {
            return null;
        }

        String rawOutput = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(nearPos), "/locate structure spawn-structures:starter_structure");

        if (rawOutput.contains("[") && rawOutput.contains("]") && rawOutput.contains(", ")) {
            String[] coords;
            try {
                if (rawOutput.contains(":")) {
                    rawOutput = rawOutput.split(":", 2)[1];
                }
                String rawcoords = rawOutput.split("\\[")[1].split("]")[0];
                coords = rawcoords.split(", ");
            } catch (IndexOutOfBoundsException ex) {
                return null;
            }

            if (coords.length == 3) {
                String sx = coords[0];
                String sz = coords[2];
                if (isNumeric(sx) && isNumeric(sz)) {
                    return getPlayerSpawnPos(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
                }
            }
        }
        return null;
    }
}
