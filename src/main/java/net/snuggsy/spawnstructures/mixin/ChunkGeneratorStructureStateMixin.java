package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.structure.ChunkGeneratorState_StarterStructure.hasBiomesForStructureSet;

@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixin {

    @Inject(method = "createForFlat(Lnet/minecraft/world/level/levelgen/RandomState;JLnet/minecraft/world/level/biome/BiomeSource;Ljava/util/stream/Stream;)Lnet/minecraft/world/level/chunk/ChunkGeneratorStructureState;", at = @At("HEAD"))
    private static void injection(RandomState pRandomState, long pLevelSeed, BiomeSource pBiomeSource, Stream<Holder<StructureSet>> pStructureSets, CallbackInfoReturnable<ChunkGeneratorStructureState> cir) {
        randomState = pRandomState;
        levelSeed = pLevelSeed;
        biomeSource = pBiomeSource;
        structureSets = pStructureSets.filter((p_255616_) -> {
            return hasBiomesForStructureSet(p_255616_.value(), pBiomeSource);
        }).toList();
    }

    @Inject(method = "createForNormal(Lnet/minecraft/world/level/levelgen/RandomState;JLnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/core/HolderLookup;)Lnet/minecraft/world/level/chunk/ChunkGeneratorStructureState;", at = @At("HEAD"))
    private static void injection(RandomState pRandomState, long pSeed, BiomeSource pBiomeSource, HolderLookup<StructureSet> pStructureSetLookup, CallbackInfoReturnable<ChunkGeneratorStructureState> cir) {
        randomState = pRandomState;
        levelSeed = pSeed;
        biomeSource = pBiomeSource;
        structureSets = pStructureSetLookup.listElements().filter((p_256144_) -> {
            return hasBiomesForStructureSet(p_256144_.value(), pBiomeSource);
        }).collect(Collectors.toUnmodifiableList());
    }
}