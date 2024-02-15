package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static net.snuggsy.spawnstructures.data.GlobalVariables.structPos;

@Mixin(StructurePlacement.class)
public class StructurePlacementMixin {

    @Inject(method = "getLocatePos(Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/core/BlockPos;", at = @At("RETURN"), cancellable = true)
    private void injected(ChunkPos pChunkPos, CallbackInfoReturnable<BlockPos> cir) {
        if (Objects.equals(pChunkPos, new ChunkPos(structPos))) {
            cir.setReturnValue(structPos);
        } else {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}