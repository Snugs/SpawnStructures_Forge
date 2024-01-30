package net.snuggsy.spawnstructures.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;
import net.snuggsy.spawnstructures.functions.CommandFunctions;
import org.jetbrains.annotations.NotNull;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.*;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.spawnRot;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.getBiome;

public class StructureSpawnEvent {

    public static boolean onWorldLoad(@NotNull ServerLevel serverLevel) {
        // Get WorldGen Options
        worldGenOptions = serverLevel.getServer().getWorldData().worldGenOptions();

        // Set Spawn Radius Rules
        if (ignoreGameruleSpawnRadius){
            CommandFunctions.getRawCommandOutput(serverLevel, null, "/gamerule spawnRadius 0");
            // Can we set this without a command block?
        }

        // Set Structure Gen Rules
        if (!ignoreGameruleGenStructures && !worldGenOptions.generateStructures()) {
            genStructures = false;
            return false;
        } else if (ignoreGameruleGenStructures && !worldGenOptions.generateStructures()) {
            changePos = true;
            return false;
        } else if (spawnWorldCentre) {
            changePos = true;
            LOGGER.error("Spawning in World Centre...");
        }

        // Find Starter Structure Spawning Location
        selectedSpawn = new BlockPos(0,300,0);
        /* Find spawn location for structure prior to structure placement, so that structure can be selected based on whether biome-dependant is selected or not... OR can use predicates for this shet?
        if (!spawnWorldCentre){
            LOGGER.error("Shared Spawn: " + serverLevel.getSharedSpawnPos());
            defaultSpawn = BlockPosFunctions.getPlayerSpawnPos(serverLevel, serverLevel.getSharedSpawnPos().getX(), serverLevel.getSharedSpawnPos().getZ());
            LOGGER.error("Default spawn: " + defaultSpawn);
            if (defaultSpawn.getY() <= 63){
                defaultSpawn = new BlockPos(defaultSpawn.getX(), 64, defaultSpawn.getZ());
            }
            defaultSpawn = serverLevel.getSharedSpawnPos();
        }
        LOGGER.error("Default spawn: " + defaultSpawn);
        */

        // Get Biome at Spawning Location
        getBiome(serverLevel, selectedSpawn);

        // Get Player Spawn Position
        if (!spawnWorldCentre) {
            spawnPos = BlockPosFunctions.getStarterStructureFromCentre(serverLevel);
        } else {
            spawnPos = BlockPosFunctions.getPlayerSpawnPos(serverLevel, 0, 0); // <-- Need to throw defaultSpawn through the BlockPosFunction that calculates top block
        }

        if (spawnPos == null) {
            LOGGER.error("Structure Generation FAILED");
            genFailed = true;
            return false;
        }

        // Set Default Spawning Location
        serverLevel.setDefaultSpawnPos(spawnPos, spawnRot(structureRotation));

        // Declare the World as Freshly Generated
        worldInit = true;

        return true;
    }
}