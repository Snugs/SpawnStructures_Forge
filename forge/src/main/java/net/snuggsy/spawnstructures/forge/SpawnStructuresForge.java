package net.snuggsy.spawnstructures.forge;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.snuggsy.spawnstructures.common.SpawnStructuresCommon;
import net.snuggsy.spawnstructures.common.data.References;
import net.snuggsy.spawnstructures.forge.config.SpawnStructuresConfig_Forge;

@Mod(References.MOD_ID)
public class SpawnStructuresForge {
    public SpawnStructuresForge() {
        SpawnStructuresCommon.init();

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                SpawnStructuresConfig_Forge.SPEC,
                References.CONFIG_FILENAME
        );
    }

}