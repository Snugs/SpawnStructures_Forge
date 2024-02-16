package net.snuggsy.spawnstructures.functions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.snuggsy.spawnstructures.config.SpawnStructuresConfig_Common;
import net.snuggsy.spawnstructures.data.StructureCoordinates;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.structureSelected;
import static net.snuggsy.spawnstructures.data.StructureCoordinates.getStructuresForBiome;
import static net.snuggsy.spawnstructures.data.StructureCoordinates.structureNames;
import static net.snuggsy.spawnstructures.functions.NumberFunctions.getOptimalHeight;

public abstract class GenerationFunctions {

    // Place the Starter Structure
    public static void placeStarterStructure(ServerLevel serverLevel, BlockPos pPos) {
        Pair<String, ResourceLocation> newTemplate = setStartPool(serverLevel, pPos);
        pPos = getOptimalHeight(serverLevel, pPos, 10);
        String posX = String.valueOf(pPos.getX());
        String posY = String.valueOf(pPos.getY() + StructureCoordinates.getStructureOffset(chosenStructure).getY());
        String posZ = String.valueOf(pPos.getZ());
        //pPos = new BlockPos(pPos.getX(), pPos.getY() + StructureCoordinates.getStructureOffset(chosenStructure).getY(), pPos.getZ());
        for (int i = 0; i < 10; i++) {
            String placementConfirmation = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(pPos), "/place structure spawn-structures:starter_structures/" + newTemplate.getFirst()/* + " ~ ~ ~" */+ " " + posX + " " + posY + " " + posZ);
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
        getBiomeViaCommand(serverLevel, new BlockPos(pPos.getX(), 300, pPos.getZ()), "overworld");

        // Convert the Selected Structure to account for potential configuration errors
        String structName = structureSelected.toUpperCase();
        if (structName.contains(" ")) {
            structName = structName.replace(" ", "_");
        }
        if (!SpawnStructuresConfig_Common.starterStructureOptions.contains(structName)) {
            LOGGER.error("[Spawn Structures] \"Selected Structure\" config option invalid! Reverting to Biome Dependent...");
            structName = "BIOME_DEPENDENT";
        }

        // Select a Starter Structure according to the specified Configurations
        if (structName.equals("BIOME_DEPENDENT") || structName.equals("BIOME_DEPENDANT")) {
            List<String> potentialStructures = getStructuresForBiome(currentBiome);
            if (potentialStructures.isEmpty()) {
                chosenStructure = getRandomStringFromList(structureNames);
            } else if (potentialStructures.size() == 1) {
                chosenStructure = potentialStructures.get(0);
            } else {
                chosenStructure = getRandomStringFromList(potentialStructures);
            }
        } else if (structName.equals("RANDOMIZED") || structName.equals("RANDOMISED")) {
            chosenStructure = getRandomStringFromList(structureNames);
        } else {
            for (String structureName : structureNames) {
                if (structName.equals(structureName)) {
                    chosenStructure = structureName;
                    break;
                }
            }
        }

        // Select a Template Pool Resource Location for the selected Starter Structure according to the current Biome
        String structure = chosenStructure.toLowerCase().replace("_", "-");
        String substrate = getSubstrate();
        startPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/" + structure + "/" + structure);
        newPoolLocation = new ResourceLocation("spawn-structures", "starter-structures/" + structure + "/" + substrate + structure);
        return new Pair<>(structure, newPoolLocation);
    }

    // Get the Substrate required for the Template Pool
    @NotNull
    private static String getSubstrate() {
        String substrate = "";
        List<String> biomeTags = List.of("desert", "beach", "badlands", "mesa", "snowy", "peaks", "mushroom", "frozen", "ice", "old_growth_spruce");
        for (String biomeTag : biomeTags) {
            if (currentBiome.contains(biomeTag)) {
                switch (biomeTag) {
                    case "desert", "beach"      -> substrate = "sand";
                    case "badlands", "mesa"     -> substrate = "red_sand";
                    case "snowy", "peaks"       -> substrate = "snow";
                    case "mushroom"             -> substrate = "myc";
                    case "frozen", "ice"        -> substrate = "ice";
                    case "old_growth_spruce"    -> substrate = "pod";
                }
            }
        }
        if (!substrate.isEmpty()) {
            substrate = substrate + "_";
        }
        return substrate;
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
        //BlockPos biomeLocation = NumberFunctions.convertCoordString(serverLevel, rawOutput, "XYZ");
        currentBiome = rawOutput.split("\\(")[1].split("\\)")[0];
        //assert biomeLocation != null;
        //newLog("Found " + currentBiome + " at [" + biomeLocation.getX() + ", " + biomeLocation.getY() + ", " + biomeLocation.getZ() + "]");
        newLog("Found " + currentBiome + " at [" + pPos.getX() + ", " + pPos.getY() + ", " + pPos.getZ() + "]");
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

    // Get random String from a List of strings
    private static String getRandomStringFromList(List<String> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}