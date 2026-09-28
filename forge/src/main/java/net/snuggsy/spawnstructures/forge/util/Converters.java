package net.snuggsy.spawnstructures.forge.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.snuggsy.spawnstructures.common.data.struct.StructureRotation;
import net.snuggsy.spawnstructures.common.data.struct.Vec3i;

public class Converters {
    // BlockPos
    public static BlockPos toBlockPos(Vec3i vec) {
        return new BlockPos(vec.x(), vec.y(), vec.z());
    }

    // Rotation
    public static Rotation toMcRotation(StructureRotation rot) {
        return switch (rot) {
            case NORTH -> Rotation.NONE;
            case EAST -> Rotation.CLOCKWISE_90;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
        };
    }
}
