package net.snuggsy.spawnstructures.functions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.snuggsy.spawnstructures.data.GlobalVariables;
import net.snuggsy.spawnstructures.data.StructureCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.*;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.getBiomeViaCommand;
import static net.snuggsy.spawnstructures.functions.NumberFunctions.*;

public class BlockPosFunctions {

    // Find Starter Structure Spawning Location
    public static void findStartingLocation(ServerLevel serverLevel, int n) {
        // Set the Search Location
        setSearchLocation();
        // Search for the selected Biome
        if (!Objects.equals(biomeSelected, "ANY") && !Objects.equals(biomeSelected, "ALL")) {
            AtomicInteger atomicBiomeCount = new AtomicInteger();
            List<ResourceKey<Biome>> selectedBiomes = new ArrayList<>();
            var biomes = globalServerLevel.registryAccess().registryOrThrow(Registries.BIOME);
            //biomes.getTagNames().forEach(biomeTagKey -> LOGGER.info(biomeTagKey.toString()));
            biomes.asLookup().listElementIds().forEach(biomeResourceKey -> {
                if (biomeResourceKey.toString().contains(biomeSelected.toLowerCase())) {
                    atomicBiomeCount.getAndIncrement();
                    selectedBiomes.add(biomeResourceKey);
                }
            });
            int selectedBiomeCount = atomicBiomeCount.intValue();
            if (selectedBiomeCount == 0) {
                LOGGER.error("[Spawn Structures] Value for 'Set Spawn Biome' could not be parsed! Please adjust the Config file...");
            } else {
                ArrayList<BlockPos> biomeCoords = new ArrayList<>();
                ArrayList<Integer> biomeDist = new ArrayList<>();
                String biome = "";
                for (int i = 0; i < selectedBiomeCount; i++) {
                    String biomeKey = selectedBiomes.get(i).toString();
                    if (biomeKey.contains("[") && biomeKey.contains("]") && biomeKey.contains(" / ")) {
                        biome = biomeKey.split("/ ")[1].split("]")[0];
                    }
                    String rawBiomeCoords = CommandFunctions.getRawCommandOutput(globalServerLevel, Vec3.atBottomCenterOf(searchLocation), "/locate biome " + biome);
                    biomeCoords.add(convertCoordString(globalServerLevel, rawBiomeCoords, "XYZ"));
                    biomeDist.add(get3DCoordDist(searchLocation, biomeCoords.get(i)));
                }
                biome = selectedBiomes.get(getSmallestIntIndex(biomeDist)).toString().split("/ ")[1].split("]")[0];
                newLog("Biome selected by Player is: " + biome);
                BlockPos closestBiome = biomeCoords.get(getSmallestIntIndex(biomeDist));
                BlockPos centreBiome = triangulateBiome(globalServerLevel, searchLocation, closestBiome, biome, false);
                structureLocation = getHeighestBlock(serverLevel, centreBiome.getX(), centreBiome.getZ());
                boolean ignoreMoist = false;
                for (String moistException : moistExceptions) {
                    if (biome.contains(moistException.toLowerCase())) {
                        ignoreMoist = true;
                        break;
                    }
                }
                if (!ignoreMoist) {
                    if (genStructures && !worldGenOptions.generateStructures()) {
                        structureLocation = checkMoist(globalServerLevel, structureLocation, 10);
                    } else {
                        structureLocation = checkMoistBiome(globalServerLevel, structureLocation, 10, "overworld");
                    }
                }
            }
        } else {
            // Set the Structure Location to be that of the Specified Coordinates
            if (setWorldSpawn) {
                structureLocation = convertCoordString(globalServerLevel, specifiedLocation, "XZ");
            }
            // Set the Structure Location to be that of a random location near the world centre
            else {
                structureLocation = BlockPos.ZERO;
                BlockPos potentialPos = loopRandomPosFromCenter(serverLevel, n);
                GlobalVariables.posList.add(potentialPos);
                if (GlobalVariables.posList.size() > 1) {
                    for (BlockPos pos : GlobalVariables.posList) {
                        if (pos != BlockPos.ZERO) {
                            structureLocation = pos;
                            break;
                        }
                    }
                } else {
                    structureLocation = potentialPos;
                }
                structureLocation = getHeighestBlock(serverLevel, structureLocation.getX(), structureLocation.getZ());
            }
        }

        // Revert the Starter Structure Spawning Location if necessary
        if (structureLocation == null) {
            LOGGER.error("[Spawn Structures] Starter Structure failed to find a suitable spawn location. Reverting to world center...");
            structureLocation = getHeighestBlock(serverLevel, 0, 0);
        }
        // --> Use the optimal height method for when placing structure, not generating
        if (genStructures && !worldGenOptions.generateStructures()) {
            structureLocation = getOptimalHeight(serverLevel, structureLocation, 10);
            structureLocation.above(2);
            spawnStructures_Forge$chunkPos = new ChunkPos(structureLocation);
        }
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

    // Get the highest solid block at the specified X,Z coordinates
    public static BlockPos getHeighestBlock(ServerLevel serverLevel, int xPos, int zPos) {
        int highestY = serverLevel.getHeight();
        int lowestY = serverLevel.getMinBuildHeight();

        BlockPos returnPos = new BlockPos(xPos, highestY-1, zPos);
        BlockPos pPos = new BlockPos(xPos, highestY, zPos);
        for (int y = highestY; y > lowestY; y--) {
            BlockState blockState = serverLevel.getBlockState(pPos);
            MapColor material = blockState.getMapColor(serverLevel, pPos);
            if (blockState.getLightBlock(serverLevel, pPos) >= 15 || surfaceMaterials.contains(material)) {
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
        BlockPos possibleLocation = BlockPos.ZERO;
        for (int i = 0; i < n; i++) {
            possibleLocation = BlockPosFunctions.getRandomPosNearby(serverLevel, nearPos);
            if (possibleLocation != BlockPos.ZERO && !Objects.equals(possibleLocation, new BlockPos(0, possibleLocation.getY(), 0))) {
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
        Pair<Integer, Integer> rndValues = getRndStructureCoordinates();
        int x = nearPos.getX() + rndValues.getFirst();
        int y = serverLevel.getHeight();
        int z = nearPos.getZ() + rndValues.getSecond();
        BlockPos rndCoord = new BlockPos(x, y, z);
        BlockPos rndHighest = getHeighestBlock(serverLevel, rndCoord.getX(), rndCoord.getZ());
        String currentBiome = getBiomeViaCommand(serverLevel, rndHighest, "overworld");
        LOGGER.error("Random position at: " + rndCoord);
        LOGGER.error("Biome at random position: " + currentBiome);
        CharSequence ocean = "ocean";
        if (currentBiome.contains(ocean)) {
            LOGGER.error("Biome was an ocean... OOPS!");
            return BlockPos.ZERO;
        }
        return checkMoist(serverLevel, rndHighest.above(), 10);
    }

    public static BlockPos checkMoist(ServerLevel serverLevel, BlockPos searchPos, Integer scanDist) {
        int newX = searchPos.below().getX();
        int newZ = searchPos.below().getZ();
        for (int i = 0; i < 10; i++) {
            int bX = newX;
            int bZ = newZ;
            int fullCheck = 0;
            if (!surfaceMaterials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX + scanDist, bZ + scanDist).below()))) {
                newX += scanDist;
                newZ += scanDist;
                fullCheck++;
            }
            if (!surfaceMaterials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX - scanDist, bZ + scanDist).below()))) {
                newX -= scanDist;
                newZ += scanDist;
                fullCheck++;
            }
            if (!surfaceMaterials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX + scanDist, bZ - scanDist).below()))) {
                newX += scanDist;
                newZ -= scanDist;
                fullCheck++;
            }
            if (!surfaceMaterials.contains(getBlockMaterial(serverLevel, getHeighestBlock(serverLevel, bX - scanDist, bZ - scanDist).below()))) {
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
        return new BlockPos(newX, searchPos.getY(), newZ);
    }

    public static BlockPos checkMoistBiome(ServerLevel serverLevel, BlockPos searchPos, Integer scanDist, String dimension) {
        int newX = searchPos.getX();
        int newZ = searchPos.getZ();
        boolean continueCheck = false;
        String nearBiome = getBiomeViaCommand(serverLevel, new BlockPos(searchPos.getX(), 300, searchPos.getZ()), dimension);
        for (String moistException : moistExceptions) {
            if (nearBiome.contains(moistException.toLowerCase())) {
                continueCheck = true;
                break;
            }
        }
        if (continueCheck) {
            for (int i = 0; i < 10; i++) {
                int bX = newX;
                int bZ = newZ;
                int fullCheck = 0;
                String nearBiome1 = getBiomeViaCommand(serverLevel, new BlockPos(bX + scanDist, 300, bZ + scanDist), dimension);
                for (String moistException : moistExceptions) {
                    if (nearBiome1.contains(moistException.toLowerCase())) {
                        newX += scanDist;
                        newZ += scanDist;
                        fullCheck++;
                    }
                }
                String nearBiome2 = getBiomeViaCommand(serverLevel, new BlockPos(bX + scanDist, 300, bZ - scanDist), dimension);
                for (String moistException : moistExceptions) {
                    if (nearBiome2.contains(moistException.toLowerCase())) {
                        newX += scanDist;
                        newZ -= scanDist;
                        fullCheck++;
                    }
                }
                String nearBiome3 = getBiomeViaCommand(serverLevel, new BlockPos(bX - scanDist, 300, bZ + scanDist), dimension);
                for (String moistException : moistExceptions) {
                    if (nearBiome3.contains(moistException.toLowerCase())) {
                        newX -= scanDist;
                        newZ += scanDist;
                        fullCheck++;
                    }
                }
                String nearBiome4 = getBiomeViaCommand(serverLevel, new BlockPos(bX - scanDist, 300, bZ - scanDist), dimension);
                for (String moistException : moistExceptions) {
                    if (nearBiome4.contains(moistException.toLowerCase())) {
                        newX -= scanDist;
                        newZ -= scanDist;
                        fullCheck++;
                    }
                }
                if (newX == bX && newZ == bZ && fullCheck < 4) {
                    LOGGER.error("Biome was moist... OOPS!");
                    return null;
                } else if (fullCheck <= 1) {
                    break;
                }
            }
        }
        return new BlockPos(newX, searchPos.getY(), newZ);
    }

    public static void setSearchLocation() {
        if (setWorldSpawn) {
            searchLocation = convertCoordString(globalServerLevel, specifiedLocation, "XZ-C");
            assert searchLocation != null;
        } else {
            searchLocation = new BlockPos(0, 64, 0);
        }
    }
}
