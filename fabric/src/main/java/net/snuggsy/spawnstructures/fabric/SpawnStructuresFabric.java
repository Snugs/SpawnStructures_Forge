package net.snuggsy.spawnstructures.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.fabric.config.SpawnStructuresConfig_Fabric;

public final class SpawnStructuresFabric implements ModInitializer{
    @Override
    public void onInitialize() {
        SpawnStructuresCommon.init();

        AutoConfig.register(SpawnStructuresConfig_Fabric.class, GsonConfigSerializer::new);
    }
}