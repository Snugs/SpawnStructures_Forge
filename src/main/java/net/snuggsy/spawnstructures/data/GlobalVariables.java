package net.snuggsy.spawnstructures.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.material.MapColor;
import net.snuggsy.spawnstructures.structure.ChunkGeneratorState_StarterStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalVariables {

    // Global Variables
    public static ServerLevel globalServerLevel;    // Server Level
    public static WorldOptions worldGenOptions;     // World Options of the current Server Level

    // GameRules
    public static boolean genStructures = true;     // Should the Starter Structure be generated

    // Player Spawning
    public static BlockPos spawnPos;                // The location where the Player spawns

    // Structure Generation
    public static boolean changePos = false;        // Is the Starter Structure allowed to move location
    public static boolean placementReady = false;   // Is the world ready to place the Starter Structure via /place command
    public static boolean genFailed = false;        // Did the Starter Structure fail to generate
    public static boolean worldInit = false;        // Was the world just initialised for the first time
    public static Rotation structureRotation;       // What direction did the Starter Structure generate facing
    public static BlockPos structureLocation;       // Coordinates selected for spawning the Starter Structure
    public static BlockPos structPos;
    public static String chosenStructure;
    public static BlockPos possibleLocation = BlockPos.ZERO;

    // Chunk Generator State
    public static List<BlockPos> posList = new ArrayList<>();
    public static BlockPos searchLocation;
    public static final List<String> moistExceptions = List.of("OCEAN", "RIVER", "SWAMP");

    // Mixin Variables
    public static Holder<StructureTemplatePool> startPool;
    public static ResourceLocation startPoolLocation = new ResourceLocation("spawn-structures", "starter-structure");
    public static boolean logChunkAccess = false;
    public static int logChunkAccessCount = 0;
    public static ChunkPos spawnStructures_Forge$chunkPos;

    public static RandomState randomState;
    public static long levelSeed;
    public static BiomeSource biomeSource;
    public static List<Holder<StructureSet>> structureSets;

    // Biome Catching
    public static String currentBiome;              // The biome at specified coordinates, converted to a String

    // Materials
    public static final List<MapColor> surfaceMaterials = Arrays.asList(MapColor.WATER, MapColor.ICE);

    // Logger
    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariables.class);
    public static final boolean devEnv = true;
    public static void newLog(String value) {
        if (devEnv) {
            LOGGER.info(value);
        }
    }

    public static void globalReset() {
        genStructures = true;
        changePos = false;
        placementReady = false;
        genFailed = false;
        worldInit = false;
        logChunkAccess = false;
        logChunkAccessCount = 0;
        posList = new ArrayList<>();
        ChunkGeneratorState_StarterStructure.hasGeneratedPositions = false;
        chosenStructure = StructureCoordinates.structureNames.get(0);
        possibleLocation = BlockPos.ZERO;
    }
}
