package net.snuggsy.spawnstructures.forge.config;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static net.snuggsy.spawnstructures.common.util.Logger.Log;

public class ForgeConfig_Reload {
    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != SpawnStructuresConfig_Forge.SPEC) {
            return;
        }

        // Refresh Values
        ForgeConfig_Values.Refresh();

        Log("Forge config reloaded.");
    }
}