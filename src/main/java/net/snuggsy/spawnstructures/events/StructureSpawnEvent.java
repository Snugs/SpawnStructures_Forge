package net.snuggsy.spawnstructures.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.ServerLevelData;
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;
import net.snuggsy.spawnstructures.functions.CommandFunctions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.snuggsy.spawnstructures.config.ServerSettings.*;

public class StructureSpawnEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructureSpawnEvent.class);

    public static boolean genStructures = true;
    public static boolean worldInit = false;

    public static Rotation structureRotation;

    public static BlockPos defaultSpawn;

    public static boolean genFailed = false;
    public static String currentBiome;
    public static Holder<Biome> currentBiomeHolder;


    public static boolean onWorldLoad(@NotNull ServerLevel serverLevel, ServerLevelData serverLevelData) {
        // Get WorldGen Options
        WorldOptions worldGeneratorOptions = serverLevel.getServer().getWorldData().worldGenOptions();

        // Set Structure Gen Rules
        if (!ignoreGameruleGenStructures && !worldGeneratorOptions.generateStructures()) {
            genStructures = false;
            return false;
        } else if (!ignoreGameruleGenStructures && worldGeneratorOptions.generateStructures()) {
            SpawnStructures.changePos = true;
            //LOGGER.error("ChangePos set to TRUE");
        } else if (ignoreGameruleGenStructures) {
            SpawnStructures.changePos = true;
            //LOGGER.error("ChangePos set to TRUE");
        }

        // Set Spawn Radius Rules
        if (ignoreGameruleSpawnRadius){
            String spawnDistance = CommandFunctions.getRawCommandOutput(serverLevel, null, "/gamerule spawnRadius 0");
        }

        // Find Default Spawn
        defaultSpawn = new BlockPos(0,300,0);
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

        // Get Biome at Default Spawn
        currentBiomeHolder = serverLevel.getBiome(defaultSpawn);
        currentBiome = printBiome(currentBiomeHolder);
        //LOGGER.error("Current biome: " + currentBiome);

        // Get New Spawn Position
        if (!spawnWorldCentre) {
            SpawnStructures.spawnPos = BlockPosFunctions.getStarterStructureFromCentre(serverLevel);
        } else {
            SpawnStructures.spawnPos = BlockPosFunctions.getPlayerSpawnPos(serverLevel, 0, 0); // <-- Need to throw defaultSpawn through the BlockPosFunction that calculates top block
        }
        if (SpawnStructures.spawnPos == null) {
            LOGGER.error("Structure Generation FAILED");
            genFailed = true;
            return false;
        }

        serverLevel.setDefaultSpawnPos(SpawnStructures.spawnPos, spawnRot(structureRotation));
        worldInit = true;

        return true;
    }

    // Set the Player spawn rotation
    public static float spawnRot(Rotation structRot) {
        float spawnRot;
        if (structRot == Rotation.NONE){
            spawnRot = 180.0F;
        } else if (structRot == Rotation.COUNTERCLOCKWISE_90) {
            spawnRot = 90.0F;
        } else if (structRot == Rotation.CLOCKWISE_180) {
            spawnRot = 0.0F;
        } else {
            spawnRot = -90.0F;
        }
        return spawnRot;
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