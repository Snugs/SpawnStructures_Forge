package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.events.StructureSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPlayer.class)
public abstract class SpawnLocationMixin_Forge {

    @ModifyArg(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(Lnet/minecraft/core/BlockPos;FF)V"), index = 0)
    private BlockPos injected (BlockPos par1) {
        if (!StructureSpawnEvent.genStructures) {
            return par1;
        } else if (StructureSpawnEvent.worldInit) {
            return SpawnStructures.spawnPos;
        }
        if (StructureSpawnEvent.genFailed) {
            return new BlockPos(par1.getX(), par1.getY(), par1.getZ());
        }
        return new BlockPos(par1.getX(), par1.getY() - 14, par1.getZ());
    }

    @ModifyArg(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(Lnet/minecraft/core/BlockPos;FF)V"), index = 1)
    private float injected(float par2) {
        if (!StructureSpawnEvent.genStructures) {
            return par2;
        } else if (StructureSpawnEvent.worldInit) {
            StructureSpawnEvent.worldInit = false;
            return StructureSpawnEvent.spawnRot(StructureSpawnEvent.structureRotation);
        }
        return par2;
    }
}
