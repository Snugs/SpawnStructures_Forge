package net.snuggsy.spawnstructures.quilt;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.quilt.config.SpawnStructuresConfig_Quilt;

public final class SpawnStructuresQuilt implements ModInitializer {
    @Override
    public void onInitialize() {
        SpawnStructuresCommon.init();

        AutoConfig.register(SpawnStructuresConfig_Quilt.class, GsonConfigSerializer::new);
    }
}