package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.events.StructureSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPlayer.class)
public class SpawnLocationMixin_Forge {

    @ModifyArg(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(Lnet/minecraft/core/BlockPos;FF)V"), index = 0)
    private BlockPos injected (BlockPos par1) {
        if (!StructureSpawnEvent.genStructures) {
            return par1;
        }
        return SpawnStructures.spawnPos;
    }

    @ModifyArg(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(Lnet/minecraft/core/BlockPos;FF)V"), index = 1)
    private float injected(float par2) {
        if (!StructureSpawnEvent.genStructures) {
            return par2;
        }
        return SpawnStructures.spawnRot;
    }

}
