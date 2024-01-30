package net.snuggsy.spawnstructures.data;

import net.minecraft.core.BlockPos;

public class StructureCoordinates {

    public static BlockPos getStructureSize() {
        return new BlockPos(0,0,0);
    }

    public static BlockPos getStructureOffset() {
        return new BlockPos(0,0,0);
    }


    //   Variable Standardisation
    //   ------------------------
    //   spawnOffset_"..."         -->  The bounding box offset from the player spawn position
    //   boundingSize_"..."        -->  The bounding box size of the structure
    //   spawnHeightOffset_"..."   -->  The vertical offset between the spawn position and the highest block above the spawn position

    public static BlockPos spawnOffset_CherryBlossom = new BlockPos(-15,-4,-15);
    public static BlockPos boundingSize_CherryBlossom = new BlockPos(31,20,31);
    public static int spawnHeightOffset_CherryBlossom = 14;

}
