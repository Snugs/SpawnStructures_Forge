package net.snuggsy.spawnstructures.mixin;

import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;

@Mixin(LoggerChunkProgressListener.class)
public abstract class LoggerListenerMixin {

    @Inject(method = "stop()V", at = @At("TAIL"))
    private void injected(CallbackInfo ci) {
        if (!worldInit && changePos && genStructures) {
            placementReady = true;
        }
    }
}
