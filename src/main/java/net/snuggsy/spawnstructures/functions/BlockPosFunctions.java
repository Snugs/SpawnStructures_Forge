package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.snuggsy.spawnstructures.data.StructureCoordinates;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.setWorldSpawn;
import static net.snuggsy.spawnstructures.data.ServerSettings.specifiedLocation;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.getBiome;
import static net.snuggsy.spawnstructures.functions.NumberFunctions.*;

public class BlockPosFunctions {

    // Find Starter Structure Spawning Location
    public static void findStartingLocation(ServerLevel serverLevel, int n) {
        if (setWorldSpawn) {
            structureLocation = convertSpecifiedLocation(serverLevel, specifiedLocation);
        } else {
            structureLocation = BlockPosFunctions.loopRandomPosFromCenter(serverLevel, n);
        }
        // Revert the Starter Structure Spawning Location if necessary
        if (structureLocation == null) {
            LOGGER.error("[Spawn Structures] Starter Structure failed to find a suitable spawn location. Reverting to world center...");
            structureLocation = getHeighestBlock(serverLevel, 0, 0);
        }
        structureLocation = new BlockPos(structureLocation.getX(), structureLocation.getY() - 1, structureLocation.getZ());
        changePos = true;
        LOGGER.error("Structure Location set to: " + structureLocation);
    }

    // Get the Players Spawn Position based on the Specified Location
    public static BlockPos getPlayerDefaultSpawnPos(ServerLevel serverLevel, int spawnX, int spawnZ) {
        return new BlockPos(getHeighestBlock(serverLevel, spawnX, spawnZ).below(StructureCoordinates.spawnHeightOffset_CherryBlossom)).immutable();
    }
    public static BlockPos getPlayerRespawnPos(ServerLevel serverLevel, int spawnX, int spawnZ){
        int newX = spawnX + getRndSpawnRadius(serverLevel);
        int newZ = spawnZ + getRndSpawnRadius(serverLevel);
        int spawnY = getHeighestBlock(serverLevel, spawnX, spawnZ).getY();
        if (spawnY > serverLevel.getSharedSpawnPos().getY()) {
            spawnY = serverLevel.getSharedSpawnPos().getY();
        }
        return new BlockPos(newX, spawnY, newZ);
    }
/*
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
                    return getPlayerDefaultSpawnPos(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
                }
            }
        }
        return null;
    }
*/
    // Get the highest solid block at the specified X,Z coordinates
    public static BlockPos getHeighestBlock(ServerLevel serverLevel, int xPos, int zPos) {
        int highestY = serverLevel.getHeight();
        int lowestY = serverLevel.getMinBuildHeight();

        BlockPos returnPos = new BlockPos(xPos, highestY-1, zPos);
        BlockPos pPos = new BlockPos(xPos, highestY, zPos);
        for (int y = highestY; y > lowestY; y--) {
            BlockState blockState = serverLevel.getBlockState(pPos);
            MapColor material = blockState.getMapColor(serverLevel, pPos);
            if (blockState.getLightBlock(serverLevel, pPos) >= 15 || surfacematerials.contains(material)) {
                returnPos = pPos.above().immutable();
                break;
            }
            pPos = pPos.below();
        }
        return returnPos.immutable();
    }

    // Get the material at the specified coordinates
    public static MapColor getBlockMaterial(ServerLevel serverLevel, BlockPos pPos) {
        BlockState blockState = serverLevel.getBlockState(pPos);
        return blockState.getMapColor(serverLevel, pPos);
    }

    // Try finding a random location near the world spawn a set amount of times
    public static BlockPos loopRandomPosFromCenter(ServerLevel serverLevel, int n) {
        return loopRandomPosNearby(serverLevel, new BlockPos(0,0,0), n);
    }
    // Try finding a random location near the specified coordinates a set amount of time
    public static BlockPos loopRandomPosNearby(ServerLevel serverLevel, BlockPos nearPos, int n) {
        BlockPos possibleLocation = null;
        for (int i = 0; i < n; i++) {
            possibleLocation = BlockPosFunctions.getRandomPosNearby(serverLevel, nearPos);
            if (possibleLocation != null) {
                break;
            }
        }
        return possibleLocation;
    }
    // Find a random location near the world spawn
    public static BlockPos getRandomPosFromCenter(ServerLevel serverLevel) {
        return getRandomPosNearby(serverLevel, new BlockPos(0,0,0));
    }
    // Find a random location near the specified coordinates
    public static BlockPos getRandomPosNearby(ServerLevel serverLevel, BlockPos nearPos) {
        BlockPos rndCoord = new BlockPos(nearPos.getX() + getRndStructureCoordinates().getX(), serverLevel.getHeight(), nearPos.getZ() + getRndStructureCoordinates().getZ());
        BlockPos rndHighest = getHeighestBlock(serverLevel, rndCoord.getX(), rndCoord.getZ());
        String currentBiome = getBiome(serverLevel, rndHighest);
        LOGGER.error("Random position at: " + rndHighest);
        LOGGER.error("Biome at random position: " + currentBiome);
        CharSequence ocean = "ocean";
        if (currentBiome.contains(ocean)) {
            LOGGER.error("Biome was an ocean... OOPS!");
            return null;
        }

        int newX = rndHighest.below().getX();;
        int newZ = rndHighest.below().getZ();;
        int scanDist = 10;
        for (int i = 0; i < 10; i++) {
            if (surfacematerials.contains(getBlockMaterial(serverLevel, rndHighest.below()))) {
                int bX = newX;
                int bZ = newZ;
                int fullCheck = 0;
                if (!surfacematerials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX + scanDist, bZ + scanDist).below()))) {
                    newX += scanDist;
                    newZ += scanDist;
                    fullCheck++;
                }
                if (!surfacematerials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX - scanDist, bZ + scanDist).below()))) {
                    newX -= scanDist;
                    newZ += scanDist;
                    fullCheck++;
                }
                if (!surfacematerials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX + scanDist, bZ - scanDist).below()))) {
                    newX += scanDist;
                    newZ -= scanDist;
                    fullCheck++;
                }
                if (!surfacematerials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX - scanDist, bZ - scanDist).below()))) {
                    newX -= scanDist;
                    newZ -= scanDist;
                    fullCheck++;
                }
                if (newX == bX && newZ == bZ && fullCheck < 4) {
                    LOGGER.error("Biome was moist... OOPS!");
                    return null;
                } else if (fullCheck <= 1) {
                    break;
                }
            }
        }
        BlockPos k0 = getHeighestBlock(serverLevel, newX, newZ);
        BlockPos k1 = getHeighestBlock(serverLevel, newX+scanDist, newZ+scanDist);
        BlockPos k2 = getHeighestBlock(serverLevel, newX-scanDist, newZ+scanDist);
        BlockPos k3 = getHeighestBlock(serverLevel, newX+scanDist, newZ-scanDist);
        BlockPos k4 = getHeighestBlock(serverLevel, newX-scanDist, newZ-scanDist);
        return new BlockPos(newX, (k0.getY()+k1.getY()+k2.getY()+k3.getY()+k4.getY())/5, newZ);
    }
}
