package net.snuggsy.spawnstructures.common.data;

import net.snuggsy.spawnstructures.common.data.struct.StructureData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructureDataConfig {
    // Structure Lookup
    public static StructureData getStructure(String name) {
        return STRUCTURES.get(name);
    }

    // Biome-Dependant Structure Lookup
    public static List<String> getStructuresForBiome(String biomeName) {
        List<String> result = new ArrayList<>();

        for (StructureData data : STRUCTURES.values()) {
            for (String tag : data.biome()) {
                if (biomeName.contains(tag)) {
                    result.add(data.structureName());
                    break;
                }
            }
        }

        return result;
    }

    //   Variable Standardisation
    //   ------------------------
    //   spawnOffset_"..."         -->  The bounding box offset from the player spawn position
    //   boundingSize_"..."        -->  The bounding box size of the structure
    //   spawnHeightOffset_"..."   -->  The vertical offset between the spawn position and the highest block above the spawn position

    // CHERRY_BLOSSOM
    private static final StructureData CherryBlossom = new StructureData(
            "CHERRY_BLOSSOM",
            null,
            null,
            14,
            null,
            List.of("cherry", "birch")
    );

    // LOG_CABIN
    private static final StructureData LogCabin = new StructureData(
            "LOG_CABIN",
            null,
            null,
            11,
            null,
            List.of("taiga", "snowy")
    );

    // SAND_CASTLE
    private static final StructureData SandCastle = new StructureData(
            "SAND_CASTLE",
            null,
            null,
            0,
            null,
            List.of("desert", "badlands", "beach")
    );

    // Structure Map
    private static final Map<String, StructureData> STRUCTURES = Map.of(
            CherryBlossom.structureName(), CherryBlossom,
            LogCabin.structureName(), LogCabin,
            SandCastle.structureName(), SandCastle
    );
}
