package net.snuggsy.spawnstructures.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public static BlockPos originalPos;

    // Mixin Variables
    public static Holder<StructureTemplatePool> startPool;
    public static ResourceLocation startPoolLocation = new ResourceLocation("spawn-structures", "starter-structure");

    // JigsawPlacement Parameters
    public static Structure.GenerationContext pContextJP;
    public static Holder<StructureTemplatePool> pStartPoolJP;
    public static Optional<ResourceLocation> pStartJigsawNameJP;
    public static int pMaxDepthJP;
    public static BlockPos pPosJP;
    public static boolean pUseExpansionHackJP;
    public static Optional<Heightmap.Types> pProjectStartToHeightmapJP;
    public static int pMaxDistanceFromCenterJP;

    // Biome Catching
    public static String currentBiome;              // The biome at specified coordinates, converted to a String

    // Materials
    public static final List<MapColor> surfacematerials = Arrays.asList(MapColor.WATER, MapColor.ICE);

    // Logger
    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariables.class);
    private static final boolean devEnv = true;
    public static void newLog(String value) {
        if (devEnv) {
            LOGGER.error(value);
        }
    }

    public static void initReset() {
        genStructures = true;
        changePos = false;
        placementReady = false;
        genFailed = false;
        worldInit = false;
    }
}
