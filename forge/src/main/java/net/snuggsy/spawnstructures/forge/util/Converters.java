package net.snuggsy.spawnstructures.forge.util;

import net.minecraft.core.BlockPos;
import net.snuggsy.spawnstructures.common.util.Vec3i;

public class Converters {
    public static BlockPos toBlockPos(Vec3i vec) {
        return new BlockPos(vec.x(), vec.y(), vec.z());
    }
}
