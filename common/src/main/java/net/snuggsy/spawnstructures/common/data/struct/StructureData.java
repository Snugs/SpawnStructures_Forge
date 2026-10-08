package net.snuggsy.spawnstructures.common.data.struct;

import net.snuggsy.spawnstructures.common.worldgen.structure.StarterStructure_Defaults;

import java.util.List;

public record StructureData(
        String structureName,
        Vec3i spawnOffset, // Optional
        Vec3i boundingSize, // Optional
        int spawnHeightOffset,
        StructureRotation orientation, // Optional
        List<String> biomeTags // Optional
) {
    // Defaults if Null
    public StructureData {
        //~
        if (spawnOffset == null) {
            spawnOffset = StarterStructure_Defaults.DEFAULT_OFFSET;
        }
        if (boundingSize == null) {
            boundingSize = StarterStructure_Defaults.DEFAULT_BOUNDING_SIZE;
        }
        //~
        if (orientation == null) {
            orientation = StarterStructure_Defaults.DEFAULT_ORIENTATION;
        }
        if (biomeTags == null) {
            biomeTags = List.of();
        }
    }
}