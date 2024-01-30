package net.snuggsy.spawnstructures.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
        CommandFunctions.getRawCommandOutput(serverLevel, null, "/place structure spawn-structures:starter_structure " + posX + " " + posY + " " + posZ);
        LOGGER.error("Structure placement attempted!");
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
