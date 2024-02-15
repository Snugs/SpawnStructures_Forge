package net.snuggsy.spawnstructures.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.LocateCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;

@Mixin(LocateCommand.class)
public abstract class LocateCommandMixin {

    @ModifyArg(method = "showLocateResult(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/commands/arguments/ResourceOrTagKeyArgument$Result;Lnet/minecraft/core/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/time/Duration;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/LocateCommand;showLocateResult(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/core/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/lang/String;Ljava/time/Duration;)I"), index = 2)
    private static Pair<BlockPos, ? extends Holder<?>> inject1(Pair<BlockPos, ? extends Holder<?>> pResultWithoutPosition) {
        if (worldInit) {
            if (Objects.equals(pResultWithoutPosition.getFirst(), new BlockPos(spawnStructures_Forge$chunkPos.getMinBlockX(), 0, spawnStructures_Forge$chunkPos.getMinBlockZ()))) {
                return new Pair<>(globalServerLevel.getSharedSpawnPos(), pResultWithoutPosition.getSecond());
            }
        }
        return pResultWithoutPosition;
    }

    @ModifyArg(method = "showLocateResult(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/commands/arguments/ResourceOrTagArgument$Result;Lnet/minecraft/core/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/time/Duration;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/LocateCommand;showLocateResult(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/core/BlockPos;Lcom/mojang/datafixers/util/Pair;Ljava/lang/String;ZLjava/lang/String;Ljava/time/Duration;)I"), index = 2)
    private static Pair<BlockPos, ? extends Holder<?>> inject2(Pair<BlockPos, ? extends Holder<?>> pResultWithoutPosition) {
        if (worldInit) {
            if (Objects.equals(pResultWithoutPosition.getFirst(), new BlockPos(spawnStructures_Forge$chunkPos.getMinBlockX(), 0, spawnStructures_Forge$chunkPos.getMinBlockZ()))) {
                return new Pair<>(globalServerLevel.getSharedSpawnPos(), pResultWithoutPosition.getSecond());
            }
        }
        return pResultWithoutPosition;
    }
}