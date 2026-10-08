package net.snuggsy.spawnstructures.quilt;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.quilt.config.QuiltConfig_Builder;
import net.snuggsy.spawnstructures.quilt.worldgen.structure.QuiltStructure_ConfigLoader;

public final class SpawnStructuresQuilt implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register MetaData Reload Listener
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new QuiltStructure_ConfigLoader());

        SpawnStructuresCommon.init();

        AutoConfig.register(QuiltConfig_Builder.class, GsonConfigSerializer::new);
    }
}