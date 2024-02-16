package net.snuggsy.spawnstructures.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;

import java.util.List;

import static net.snuggsy.spawnstructures.data.GlobalVariables.newLog;

public class StructureCoordinates {

    public static BlockPos getStructureOffset(String structureName) {
        return spawnOffsets.get(structureNames.indexOf(structureName));
    }

    public static BlockPos getStructureSize(String structureName) {
        return boundingSizes.get(structureNames.indexOf(structureName));
    }

    public static Integer getHeightOffset(String structureName) {
        return spawnHeightOffsets.get(structureNames.indexOf(structureName));
    }

    public static Rotation getStructureOrientation(String structureName) {
        String orientation = orientations.get(structureNames.indexOf(structureName));
        switch (orientation) {
            case "NORTH" -> {return Rotation.NONE;}
            case "EAST" -> {return Rotation.CLOCKWISE_90;}
            case "SOUTH" -> {return Rotation.CLOCKWISE_180;}
            case "WEST" -> {return Rotation.COUNTERCLOCKWISE_90;}
        }
        newLog("[Spawn Structures] Structure Orientation returned as NULL");
        return null;
    }

    public static List<String> getStructuresForBiome(String currentBiome) {
        List<String> availableStructures = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < defaultBiomes.size(); i++) {
            for (int j = 0; j < defaultBiomes.get(i).size(); j++) {
                if (currentBiome.contains(defaultBiomes.get(i).get(j))) {
                    availableStructures.add(structureNames.get(i));
                    break;
                }
            }
        }
        return availableStructures;
    }


    //   Variable Standardisation
    //   ------------------------
    //   spawnOffset_"..."         -->  The bounding box offset from the player spawn position
    //   boundingSize_"..."        -->  The bounding box size of the structure
    //   spawnHeightOffset_"..."   -->  The vertical offset between the spawn position and the highest block above the spawn position

    private static final String structureName_CherryBlossom = "CHERRY_BLOSSOM";
    private static final BlockPos spawnOffset_CherryBlossom = new BlockPos(-15,-12,-15);
    private static final BlockPos boundingSize_CherryBlossom = new BlockPos(31,31,31);
    private static final int spawnHeightOffset_CherryBlossom = 14;
    private static final String orientation_CherryBlossom = "NORTH";
    private static final List<String> biome_CherryBlossom = List.of("cherry", "birch");

    private static final String structureName_LogCabin = "LOG_CABIN";
    private static final BlockPos spawnOffset_LogCabin = new BlockPos(-15,-12,-15);
    private static final BlockPos boundingSize_LogCabin = new BlockPos(31,31,31);
    private static final int spawnHeightOffset_LogCabin = 11;
    private static final String orientation_LogCabin = "NORTH";
    private static final List<String> biome_LogCabin = List.of("taiga", "snowy");

    private static final String structureName_SandCastle = "SAND_CASTLE";
    private static final BlockPos spawnOffset_SandCastle = new BlockPos(-15,-12,-15);
    private static final BlockPos boundingSize_SandCastle = new BlockPos(31,31,31);
    private static final int spawnHeightOffset_SandCastle = 0;
    private static final String orientation_SandCastle = "NORTH";
    private static final List<String> biome_SandCastle = List.of("desert", "badlands", "beach");


    //   Structure Variables compiled
    public static final List<String> structureNames = List.of(
            structureName_CherryBlossom,
            structureName_LogCabin,
            structureName_SandCastle
    );
    private static final List<BlockPos> spawnOffsets = List.of(
            spawnOffset_CherryBlossom,
            spawnOffset_LogCabin,
            spawnOffset_SandCastle
    );
    private static final List<BlockPos> boundingSizes = List.of(
            boundingSize_CherryBlossom,
            boundingSize_LogCabin,
            boundingSize_SandCastle
    );
    private static final List<Integer> spawnHeightOffsets = List.of(
            spawnHeightOffset_CherryBlossom,
            spawnHeightOffset_LogCabin,
            spawnHeightOffset_SandCastle
    );
    private static final List<String> orientations = List.of(
            orientation_CherryBlossom,
            orientation_LogCabin,
            orientation_SandCastle
    );
    private static final List<List<String>> defaultBiomes = List.of(
            biome_CherryBlossom,
            biome_LogCabin,
            biome_SandCastle
    );
}
