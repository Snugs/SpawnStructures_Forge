package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.GameRules.RULE_SPAWN_RADIUS;
import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.*;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.findStartingLocation;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.getHeighestBlock;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.spawnRot;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.getBiome;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.placeStarterStructure;

public class StructureSpawnEvent {

    public static boolean onWorldLoad(@NotNull ServerLevel serverLevel) {
        globalReset();

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

        // Get the Biome at the Starter Structure Location
        getBiome(serverLevel, structureLocation);

        // Set the Default Spawning Location
        if (worldGenOptions.generateStructures()) {
            spawnPos = BlockPosFunctions.getPlayerDefaultSpawnPos(globalServerLevel, structureLocation.getX(), structureLocation.getZ());
            if (genFailed) {
                LOGGER.error("[Spawn Structures] Structure Generation FAILED. Reverting to secondary method...");
                globalServerLevel.setDefaultSpawnPos(getHeighestBlock(globalServerLevel, 0, 0), 0.0F);
            } else {
                globalServerLevel.setDefaultSpawnPos(spawnPos, spawnRot(structureRotation));
                // Declare the World as Freshly Generated
                DeclareInit();
            }
        } else {
            genFailed = true;
        }

        return true;
    }

    public static void postWorldGen() {
        // Do we require using the Place command?
        if (!worldGenOptions.generateStructures()) {
            // Place the Starter Structure
            placeStarterStructure(globalServerLevel, structureLocation);

            // Attempt to regenerate the features around the Starter Structure
            /*BlockPos regenPos;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    regenPos = new BlockPos(structureLocation.getX()-64+(16*i), structureLocation.getY(), structureLocation.getZ()-64+(16*j));
                    globalServerLevel.setChunkForced(globalServerLevel.getChunkAt(regenPos).getPos().x, globalServerLevel.getChunkAt(regenPos).getPos().z,true);
                    globalServerLevel.startTickingChunk(globalServerLevel.getChunk(regenPos.getX(), regenPos.getZ()));
                    //newLog("ChunkStatus: " + globalServerLevel.getChunk(regenPos).getStatus());
                    //newLog("ChunkType: " + globalServerLevel.getChunk(regenPos).getStatus().getChunkType());
                    if (globalServerLevel.getChunk(regenPos).getStatus().toString().equals("minecraft:full")) {
                        try {
                            globalServerLevel.getChunkSource().getGenerator().applyBiomeDecoration(globalServerLevel, globalServerLevel.getChunk(regenPos), globalServerLevel.structureManager());
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.forThrowable(throwable, "[Spawn Structures] Reapplying Biome Decoration");
                            CrashReportCategory crashreportcategory = crashreport.addCategory("Chunk generation error");
                            crashreportcategory.setDetail("ChunkPos", new ChunkPos(regenPos));
                            //crashreportcategory.setDetail("Name", () -> this.getName().getString());
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }*/

            // Set the Default Spawning Location
            spawnPos = BlockPosFunctions.getPlayerDefaultSpawnPos(globalServerLevel, structureLocation.getX(), structureLocation.getZ());
            if (genFailed) {
                LOGGER.error("[Spawn Structures] Structure Placement FAILED");
                globalServerLevel.setDefaultSpawnPos(getHeighestBlock(globalServerLevel, 0, 0), 0.0F);
            } else {
                globalServerLevel.setDefaultSpawnPos(spawnPos, spawnRot(structureRotation));
            }
        }

        // Declare the World as Freshly Generated
        DeclareInit();
    }

    private static void DeclareInit() {
        placementReady = false;
        changePos = false;
        worldInit = true;
    }
}