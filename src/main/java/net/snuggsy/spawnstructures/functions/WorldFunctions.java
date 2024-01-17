package net.snuggsy.spawnstructures.functions;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class WorldFunctions {
    public static Level getWorld_IfInstance(LevelAccessor iworld) {
        if (iworld.isClientSide()) {
            return null;
        }
        if (iworld instanceof Level) {
            return ((Level)iworld);
        }
        return null;
    }
}
