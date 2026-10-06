package net.snuggsy.spawnstructures.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.common.data.References;
import net.snuggsy.spawnstructures.forge.config.ForgeConfig_Reload;
import net.snuggsy.spawnstructures.forge.config.SpawnStructuresConfig_Forge;

@Mod(References.MOD_ID)
public class SpawnStructuresForge {
    public SpawnStructuresForge() {
        SpawnStructuresCommon.init();

        // Forge Config Registration
        MinecraftForge.EVENT_BUS.register(ForgeConfig_Reload.class);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                SpawnStructuresConfig_Forge.SPEC,
                References.CONFIG_FILENAME
        );

    }
}