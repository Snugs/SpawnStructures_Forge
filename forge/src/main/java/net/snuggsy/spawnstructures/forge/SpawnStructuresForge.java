package net.snuggsy.spawnstructures.forge;

import net.minecraftforge.fml.common.Mod;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.common.util.References;

@Mod(References.MOD_ID)
public class SpawnStructuresForge {
    public SpawnStructuresForge() {
        SpawnStructuresCommon.init();
    }
}