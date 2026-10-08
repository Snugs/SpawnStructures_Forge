package net.snuggsy.spawnstructures.common.data.starterstructure;

import net.snuggsy.spawnstructures.common.data.struct.StructureRotation;
import net.snuggsy.spawnstructures.common.data.struct.Vec3i;

public final class StarterStructure_Defaults {

    public static final Vec3i DEFAULT_OFFSET = new Vec3i(-15,-12,-15);
    public static final Vec3i DEFAULT_BOUNDING_SIZE = new Vec3i(31,31,31);

    public static final int DEFAULT_SPAWN_HEIGHT_OFFSET = 0;
    public static final StructureRotation DEFAULT_ORIENTATION = StructureRotation.NORTH;

    private StarterStructure_Defaults() {}
}