package net.snuggsy.spawnstructures.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;

import java.util.*;

public class GlobalVariables {
    // HashMaps to generate
    public static Map<Block, BlockEntityType<?>> blocksWithTileEntity = new HashMap<Block, BlockEntityType<?>>();

    public static void generateHashMaps() {
        // FAB tile entities.
        blocksWithTileEntity.put(Blocks.CAMPFIRE, BlockEntityType.CAMPFIRE);
        blocksWithTileEntity.put(Blocks.OAK_SIGN, BlockEntityType.SIGN);
    }

    // Block and item collections
    public static List<MapColor> surfacematerials = Arrays.asList(MapColor.WATER, MapColor.ICE);
}
