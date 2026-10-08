package net.snuggsy.spawnstructures.common.data.starterstructure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.snuggsy.spawnstructures.common.data.struct.StructureData;
import net.snuggsy.spawnstructures.common.data.struct.StructureRotation;
import net.snuggsy.spawnstructures.common.data.struct.Vec3i;

public final class StarterStructure_Codec {
    public static final Codec<StructureData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("structure_name").forGetter(StructureData::structureName),
                    Vec3i.CODEC.fieldOf("spawn_offset").forGetter(StructureData::spawnOffset),
                    Vec3i.CODEC.fieldOf("bounding_size").forGetter(StructureData::boundingSize),
                    Codec.INT.fieldOf("spawn_height_offset").forGetter(StructureData::spawnHeightOffset),
                    StructureRotation.CODEC.fieldOf("orientation").forGetter(StructureData::orientation),
                    Codec.STRING.listOf().fieldOf("biome_tags").forGetter(StructureData::biomeTags)
            ).apply(instance, StructureData::new));
}
