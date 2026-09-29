package net.snuggsy.spawnstructures.common.data.struct;

import java.util.List;

public record StructureData(
        String structureName,
        Vec3i spawnOffset,
        Vec3i boundingSize,
        int spawnHeightOffset,
        StructureRotation orientation,
        List<String> biome
) {
    public StructureData {
        // Default offset if null
        if (spawnOffset == null) {
            spawnOffset = new Vec3i(-15,-12,-15);
        }

        // Default bounding size if null
        if (boundingSize == null) {
            boundingSize = new Vec3i(31,31,31);
        }

        // Default orientation if null
        if (orientation == null) {
            orientation = StructureRotation.NORTH;
        }

        // Default biome list if null
        if (biome == null) {
            biome = List.of();
        }
    }
}