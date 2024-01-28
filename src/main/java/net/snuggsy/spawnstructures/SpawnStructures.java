package net.snuggsy.spawnstructures;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.snuggsy.spawnstructures.events.ForgeStructureSpawnEvent;
import net.snuggsy.spawnstructures.util.References;

@Mod(References.MOD_ID)
public class SpawnStructures {
    public static BlockPos spawnPos;
    public static boolean changePos = false;

    public SpawnStructures() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadComplete);
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeStructureSpawnEvent());
    }
}
