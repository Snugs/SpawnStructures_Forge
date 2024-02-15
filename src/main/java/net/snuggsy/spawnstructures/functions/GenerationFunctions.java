package net.snuggsy.spawnstructures.functions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.functions.NumberFunctions.getOptimalHeight;

public abstract class GenerationFunctions {

    // Place the Starter Structure
    public static void placeStarterStructure(ServerLevel serverLevel, BlockPos pPos) {
        pPos = getOptimalHeight(serverLevel, pPos, 10);
        String posX = String.valueOf(pPos.getX());
        String posY = String.valueOf(pPos.getY());
        String posZ = String.valueOf(pPos.getZ());
        Pair<String, ResourceLocation> newTemplate = setStartPool(serverLevel, pPos);
        for (int i = 0; i < 10; i++) {
            String placementConfirmation = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(pPos), "/place structure spawn-structures:" + newTemplate.getFirst() + " " + posX + " " + posY + " " + posZ);
            LOGGER.error("Structure placement attempted!");
            LOGGER.error("Placement confirmation: " + placementConfirmation);
            if (!placementConfirmation.contains("That position is not loaded")) {
                break;
            } else if (i < 9) {
                LOGGER.error("[Spawn Structures] That position is not loaded yet. Trying again...");
                int chunkX = serverLevel.getChunkAt(pPos).getPos().x;
                int chunkZ = serverLevel.getChunkAt(pPos).getPos().z;
                for (int a = chunkX-1; a < chunkX+2; a++) {
                    for (int b = chunkZ-1; b < chunkZ+2; b++) {
                        serverLevel.setChunkForced(a , b,true);
                        serverLevel.startTickingChunk(serverLevel.getChunk(a, b));
                    }
                }
                LOGGER.error("Chunk Location: " + serverLevel.getChunkAt(pPos).getPos());
            } else {
                genFailed = true;
            }
        }
    }

    // Change Template Pool according to the current Biome
    public static Pair<String, ResourceLocation> setStartPool(ServerLevel serverLevel, BlockPos pPos) {
        getBiomeViaCommand(serverLevel, pPos, "overworld");
        String structure = "starter_structure";
        if (currentBiome.contains("desert") || currentBiome.contains("beach")) {
            structure = "biome-dependent/sand_starter_structure";
            startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/sand_starter-structure");
            substrateLocation = new ResourceLocation("spawn-structures", "substructures/sand_substrate");
        } else if (currentBiome.contains("badlands") || currentBiome.contains("mesa")) {
            structure = "biome-dependent/red_sand_starter_structure";
            startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/red_sand_starter-structure");
            substrateLocation = new ResourceLocation("spawn-structures", "substructures/red_sand_substrate");
        } else if (currentBiome.contains("snowy")) {
            structure = "biome-dependent/snow_starter_structure";
            startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/snow_starter-structure");
            substrateLocation = new ResourceLocation("spawn-structures", "substructures/snow_substrate");
        } else if (currentBiome.contains("mushroom")) {
            structure = "biome-dependent/myc_starter_structure";
            startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/myc_starter-structure");
            substrateLocation = new ResourceLocation("spawn-structures", "substructures/myc_substrate");
        } else if (currentBiome.contains("frozen") || currentBiome.contains("ice")) {
            structure = "biome-dependent/ice_starter_structure";
            startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/ice_starter-structure");
            substrateLocation = new ResourceLocation("spawn-structures", "substructures/ice_substrate");
        } else if (currentBiome.contains("old_growth_spruce")) {
            structure = "biome-dependent/pod_starter_structure";
            startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/pod_starter-structure");
            substrateLocation = new ResourceLocation("spawn-structures", "substructures/pod_substrate");
        }
        return new Pair<>(structure, startPoolLocation);
    }

    // Get Biome at Specified Location
    public static String getBiome(ServerLevel serverLevel, BlockPos pPos) {
        Holder<Biome> currentBiomeHolder = serverLevel.getBiome(pPos);
        currentBiome = printBiome(currentBiomeHolder);
        newLog("Current biome: " + currentBiome);
        return currentBiome;
    }
    public static String getBiomeViaCommand(ServerLevel serverLevel, BlockPos pPos, String dimension) {
        String rawOutput = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(pPos), "/locate biome #is_" + dimension.toLowerCase());
        BlockPos biomeLocation = NumberFunctions.convertCoordString(serverLevel, rawOutput, "XYZ");
        currentBiome = rawOutput.split("\\(")[1].split("\\)")[0];
        assert biomeLocation != null;
        newLog("Found " + currentBiome + " at [" + biomeLocation.getX() + ", " + biomeLocation.getY() + ", " + biomeLocation.getZ() + "]");
        return currentBiome;
    }

    // Method borrowed from Minecraft Parchment Mappings
    private static String printBiome(@NotNull Holder<Biome> pBiomeHolder) {
        return pBiomeHolder.unwrap().map((p_205377_) -> {
            return p_205377_.location().toString();
        }, (p_205367_) -> {
            return "[unregistered " + p_205367_ + "]";
        });
    }
}