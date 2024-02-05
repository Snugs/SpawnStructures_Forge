package net.snuggsy.spawnstructures;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.snuggsy.spawnstructures.config.SpawnStructuresConfig_Common;
import net.snuggsy.spawnstructures.events.StructureSpawnEvent_Forge;
import net.snuggsy.spawnstructures.util.References;

@Mod(References.MOD_ID)
public class SpawnStructures {

    public SpawnStructures() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpawnStructuresConfig_Common.SPEC, "Spawn Structures - Common.toml");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadComplete);
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(new StructureSpawnEvent_Forge());
    }
}