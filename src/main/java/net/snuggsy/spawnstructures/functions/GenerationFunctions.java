package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.GlobalVariables.currentBiome;

public class GenerationFunctions {

    // Place the Starter Structure
    public static void placeStarterStructure(ServerLevel serverLevel, BlockPos pPos) {
        String posX = String.valueOf(pPos.getX());
        String posY = String.valueOf(pPos.getY());
        String posZ = String.valueOf(pPos.getZ());
        for (int i = 0; i < 10; i++) {
            String placementConfirmation = CommandFunctions.getRawCommandOutput(serverLevel, null, "/place structure spawn-structures:starter_structure " + posX + " " + posY + " " + posZ);
            LOGGER.error("Structure placement attempted!");
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

    // Get Biome at Specified Location
    public static String getBiome(ServerLevel serverLevel, BlockPos pPos){
        Holder<Biome> currentBiomeHolder = serverLevel.getBiome(pPos);
        currentBiome = printBiome(currentBiomeHolder);
        LOGGER.error("Current biome: " + currentBiome);
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
