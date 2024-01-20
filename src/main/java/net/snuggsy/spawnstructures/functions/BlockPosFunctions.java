package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.events.StructureSpawnEvent;

import java.util.Arrays;
import java.util.List;

public class BlockPosFunctions {
    public static List<MapColor> surfacematerials = Arrays.asList(MapColor.WATER, MapColor.ICE);

    public static BlockPos getPlayerSpawnPos(ServerLevel serverLevel, int x, int z) {
        int highestY = serverLevel.getHeight();
        int lowestY = serverLevel.getMinBuildHeight();

        // Set X and Z to be offset, adjusting for the central spawning location of the structure
        int spawnX;
        int spawnZ;
        if (StructureSpawnEvent.structureRotation == Rotation.NONE){
            spawnX = x + 13;
            spawnZ = z + 13;
            SpawnStructures.spawnRot = 180.0F;
        } else if (StructureSpawnEvent.structureRotation == Rotation.COUNTERCLOCKWISE_90) {
            spawnX = x + 13;
            spawnZ = z - 13;
            SpawnStructures.spawnRot = 90.0F;
        } else if (StructureSpawnEvent.structureRotation == Rotation.CLOCKWISE_180) {
            spawnX = x - 13;
            spawnZ = z - 13;
            SpawnStructures.spawnRot = 0.0F;
        } else {
            spawnX = x - 13;
            spawnZ = z + 13;
            SpawnStructures.spawnRot = -90.0F;
        }

        BlockPos returnpos = new BlockPos(spawnX, highestY-1, spawnZ);
        BlockPos pos = new BlockPos(spawnX, highestY, spawnZ);
        for (int y = highestY; y > lowestY; y--) {
            BlockState blockState = serverLevel.getBlockState(pos);
            MapColor material = blockState.getMapColor(serverLevel, pos);
            if (blockState.getLightBlock(serverLevel, pos) >= 15 || surfacematerials.contains(material)) {
                returnpos = pos.above().immutable();
                break;
            }
            pos = pos.below();
        }

        // Lower Y coordinate by height of structure roof compared to the intended spawn location
        int loweredY = returnpos.getY() - 14;

        returnpos = new BlockPos(returnpos.getX(), loweredY, returnpos.getZ()).immutable();
        return returnpos;
    }

    public static BlockPos getCenterStarterStructure(ServerLevel serverLevel) {
        return getStarterStructure(serverLevel, new BlockPos(0, 0, 0));
    }
    public static BlockPos getStarterStructure(ServerLevel serverLevel, BlockPos nearPos) {
        BlockPos starterStructure = null;
        if (!serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
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
            }
            catch (IndexOutOfBoundsException ex) {
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
        return starterStructure;
    }

    public static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        }
        catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
