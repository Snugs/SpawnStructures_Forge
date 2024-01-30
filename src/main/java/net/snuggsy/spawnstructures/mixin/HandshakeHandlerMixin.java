package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraftforge.network.HandshakeHandler;
import net.snuggsy.spawnstructures.functions.BlockPosFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.functions.GenerationFunctions.placeStarterStructure;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.spawnRot;

@Mixin(HandshakeHandler.class)
public abstract class HandshakeHandlerMixin {

    @Inject(method = "tickServer()Z", at = @At("RETURN"), remap = false, cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if (placementReady) {
            placeStarterStructure(globalServerLevel, new BlockPos(0, 64, 0));
            spawnPos = BlockPosFunctions.getPlayerSpawnPos(globalServerLevel, 0, 0);
            globalServerLevel.setDefaultSpawnPos(spawnPos, spawnRot(structureRotation));
            worldInit = true;
            changePos = false;
            placementReady = false;
        }
        cir.setReturnValue(cir.getReturnValue());
    }
}
