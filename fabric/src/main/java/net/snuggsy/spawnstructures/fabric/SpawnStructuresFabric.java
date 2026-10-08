package net.snuggsy.spawnstructures.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.fabric.config.FabricConfig_Builder;
import net.snuggsy.spawnstructures.fabric.worldgen.structure.FabricStructure_ConfigLoader;

public final class SpawnStructuresFabric implements ModInitializer{
    @Override
    public void onInitialize() {
        // Register MetaData Reload Listener
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
            .registerReloadListener(new FabricStructure_ConfigLoader());

        SpawnStructuresCommon.init();

        AutoConfig.register(FabricConfig_Builder.class, GsonConfigSerializer::new);
    }
}