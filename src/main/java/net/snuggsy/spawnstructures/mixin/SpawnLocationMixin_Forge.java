package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.getPlayerRespawnPos;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.spawnRot;

@Mixin(ServerPlayer.class)
public abstract class SpawnLocationMixin_Forge {

    @Inject(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At("HEAD"))
    private void injected(ServerLevel pLevel, CallbackInfo ci){
        globalServerLevel = pLevel;
    }

    @ModifyArg(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(Lnet/minecraft/core/BlockPos;FF)V"), index = 0)
    private BlockPos injected(BlockPos par1) {
        if (genFailed || !genStructures) {
            return par1;
        } else if (worldInit) {
            return spawnPos;
        }
        return getPlayerRespawnPos(globalServerLevel, par1.getX(), par1.getZ());
    }

    @ModifyArg(method = "fudgeSpawnLocation(Lnet/minecraft/server/level/ServerLevel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(Lnet/minecraft/core/BlockPos;FF)V"), index = 1)
    private float injected(float par2) {
        if (!genStructures) {
            return par2;
        } else if (worldInit) {
            worldInit = false;
            return spawnRot(structureRotation);
        }
        return globalServerLevel.getSharedSpawnAngle();
    }
}
