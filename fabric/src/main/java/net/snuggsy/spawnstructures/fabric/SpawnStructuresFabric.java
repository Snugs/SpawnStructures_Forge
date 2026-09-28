package net.snuggsy.spawnstructures.fabric;

import net.fabricmc.api.ModInitializer;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;

public final class SpawnStructuresFabric implements ModInitializer{
    @Override
    public void onInitialize() {
        SpawnStructuresCommon.init();
    }
}