package net.snuggsy.spawnstructures.util;

import net.minecraft.core.BlockPos;

public class StructureCoordinates {

    public BlockPos getStructureSize() {
        return new BlockPos(0,0,0);
    }

    public BlockPos getStructureOffset() {
        return new BlockPos(0,0,0);
    }

    // With the Structure Block placed directly the spawn point of the Starter Structure,
    // the following values are the bounding box offset and bounding box size of said Starter Structure.

    public static BlockPos offsetCherryBlossom = new BlockPos(-15,-4,-15);
    public static BlockPos sizeCherryBlossom = new BlockPos(31,20,31);

}
