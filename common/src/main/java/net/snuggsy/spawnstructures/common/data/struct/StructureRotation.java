package net.snuggsy.spawnstructures.common.data.struct;

import com.mojang.serialization.Codec;

public enum StructureRotation {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static final Codec<StructureRotation> CODEC = Codec.STRING.xmap(
            StructureRotation::valueOf,
            Enum::name
    );
}