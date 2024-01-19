package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeStructureSpawnEvent {
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
        Level level = getWorld_IfInstance(e.getLevel());
        if (level == null) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        if (StructureSpawnEvent.onWorldLoad(serverLevel, (ServerLevelData)serverLevel.getLevelData())) {
            e.setCanceled(true);
        }
    }

    public static Level getWorld_IfInstance(LevelAccessor world) {
        if (world.isClientSide()) {
            return null;
        }
        if (world instanceof Level) {
            return ((Level)world);
        }
        return null;
    }
}
