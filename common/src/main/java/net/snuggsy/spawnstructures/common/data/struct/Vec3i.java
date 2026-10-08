package net.snuggsy.spawnstructures.common.data.struct;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Vec3i (int x, int y, int z) {
    public static final Codec<Vec3i> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Vec3i::x),
            Codec.INT.fieldOf("y").forGetter(Vec3i::y),
            Codec.INT.fieldOf("z").forGetter(Vec3i::z)
    ).apply(instance, Vec3i::new));
}