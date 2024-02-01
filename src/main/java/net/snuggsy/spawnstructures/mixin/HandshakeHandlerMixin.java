package net.snuggsy.spawnstructures.mixin;

import net.minecraftforge.network.HandshakeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.events.StructureSpawnEvent.postWorldGen;

@Mixin(HandshakeHandler.class)
public abstract class HandshakeHandlerMixin {

    @Inject(method = "tickServer()Z", at = @At("RETURN"), remap = false, cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if (placementReady) {
            postWorldGen();
        }
        cir.setReturnValue(cir.getReturnValue());
    }
}
