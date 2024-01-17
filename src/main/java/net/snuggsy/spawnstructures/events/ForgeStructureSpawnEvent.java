package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snuggsy.spawnstructures.functions.WorldFunctions;

@Mod.EventBusSubscriber
public class ForgeStructureSpawnEvent {
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
        Level level = WorldFunctions.getWorld_IfInstance(e.getLevel());
        if (level == null) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        if (StructureSpawnEvent.onWorldLoad(serverLevel, (ServerLevelData)serverLevel.getLevelData())) {
            e.setCanceled(true);
        }
    }
}
