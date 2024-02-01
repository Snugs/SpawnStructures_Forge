package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.GameRules.RULE_SPAWN_RADIUS;
import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.*;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.*;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.spawnRot;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.placeStarterStructure;

public class StructureSpawnEvent {

    public static boolean onWorldLoad(@NotNull ServerLevel serverLevel) {
        initReset();
        // Get WorldGen Options
        worldGenOptions = serverLevel.getServer().getWorldData().worldGenOptions();

        // Set Spawn Radius Rules
        if (ignoreGameruleSpawnRadius){
            globalServerLevel.getGameRules().getRule(RULE_SPAWN_RADIUS).set(spawnRadius, null);
        }

        // Set Structure Gen Rules
        if (!ignoreGameruleGenStructures && !worldGenOptions.generateStructures()) {
            genStructures = false;
            return false;
        } else {
            changePos = true;
        }

        // Set the Starter Structure Location
        findStartingLocation(globalServerLevel, 100);

        return true;
    }

    public static void postWorldGen() {
        // Place the Starter Structure
        structPos = structureLocation;
        placeStarterStructure(globalServerLevel, structureLocation);

        // Set the Default Spawning Location
        spawnPos = BlockPosFunctions.getPlayerDefaultSpawnPos(globalServerLevel, structPos.getX(), structPos.getZ());
        if (genFailed) {
            LOGGER.error("[Spawn Structures] Structure Generation FAILED");
            globalServerLevel.setDefaultSpawnPos(getHeighestBlock(globalServerLevel, 0, 0), 0.0F);
        } else {
            globalServerLevel.setDefaultSpawnPos(spawnPos, spawnRot(structureRotation));
        }

        // Declare the World as Freshly Generated
        placementReady = false;
        changePos = false;
        worldInit = true;
    }
}