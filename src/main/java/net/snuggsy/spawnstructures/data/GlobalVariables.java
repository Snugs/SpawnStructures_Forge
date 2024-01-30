package net.snuggsy.spawnstructures.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class GlobalVariables {

    // Global Variables
    public static ServerLevel globalServerLevel;    // Server Level
    public static WorldOptions worldGenOptions;     // World Options of the current Server Level

    // Player Spawning
    public static BlockPos spawnPos;                // The location where the Player spawns

    // Structure Generation
    public static boolean genStructures = true;     // Should the Starter Structure be generated
    public static boolean changePos = false;        // Is the Starter Structure allowed to move location
    public static boolean placementReady = false;   // Is the world ready to place the Starter Structure via /place command
    public static boolean genFailed = false;        // Did the Starter Structure fail to generate
    public static boolean worldInit = false;        // Was the world just initialised for the first time
    public static Rotation structureRotation;       // What direction did the Starter Structure generate facing
    public static BlockPos selectedSpawn;           // Coordinates selected for spawning the Starter Structure when {spawnWorldCentre = true}

    // Mixin Variables
    public static Holder<StructureTemplatePool> startPool;
    public static final ResourceLocation startPoolLocation = new ResourceLocation("spawn-structures", "starter-structure");
    public static BlockPos structPos;

    // Biome Catching
    public static String currentBiome;              // The biome at specified coordinates, converted to a String

    // Materials
    public static List<MapColor> surfacematerials = Arrays.asList(MapColor.WATER, MapColor.ICE);

    // Logger
    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariables.class);

}
