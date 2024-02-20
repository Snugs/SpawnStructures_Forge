package net.snuggsy.spawnstructures.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.snuggsy.spawnstructures.data.GlobalVariables.globalServerLevel;
import static net.snuggsy.spawnstructures.data.GlobalVariables.newLog;

@Mod.EventBusSubscriber
public class StructureSpawnEvent_Forge {
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
        Level level = getWorld_IfInstance(e.getLevel());
        if (level == null) {
            return;
        }
        globalServerLevel = (ServerLevel)level;
        newLog("Server Level = " + globalServerLevel);
        if (StructureSpawnEvent.onWorldLoad(globalServerLevel)) {
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