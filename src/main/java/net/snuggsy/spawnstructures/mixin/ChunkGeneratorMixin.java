package net.snuggsy.spawnstructures.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.snuggsy.spawnstructures.structure.ChunkGeneratorState_StarterStructure;
import net.snuggsy.spawnstructures.structure.StructurePlacementTypeCodec_StarterStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.snuggsy.spawnstructures.data.GlobalVariables.newLog;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

    @Unique
    private static boolean spawnStructures_Forge$changeReturn = false;
    @Unique
    private static Pair<BlockPos, Holder<Structure>> spawnStructures_Forge$newPair;

    @Inject(method = "findNearestMapStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;", at = @At("RETURN"), cancellable = true)
    private void injected(ServerLevel pLevel, HolderSet<Structure> pStructure, BlockPos pPos, int pSearchRadius, boolean pSkipKnownStructures, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        if (spawnStructures_Forge$changeReturn) {
            spawnStructures_Forge$changeReturn = false;
            cir.setReturnValue(spawnStructures_Forge$newPair);
        }
    }

    @Inject(
        method = "findNearestMapStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;",
        at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", shift = At.Shift.BEFORE),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void injected(
            ServerLevel pLevel, HolderSet<Structure> pStructure, BlockPos pPos, int pSearchRadius, boolean pSkipKnownStructures, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir, ChunkGeneratorStructureState chunkgeneratorstructurestate, Map map, Pair pair2, double d2, StructureManager structuremanager, List list
    ) {
        Map<StructurePlacement, Set<Holder<Structure>>> newMap = map;
        newLog(newMap.entrySet().toString());
        for(Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : newMap.entrySet()) {
            newLog("Test");
            StructurePlacement structureplacement1 = entry.getKey();
            if (structureplacement1 instanceof StructurePlacementTypeCodec_StarterStructure) {
                newLog("Starter Structure detected in chunkmap");
                StructurePlacementTypeCodec_StarterStructure structurePlacementTypeCodecStarterStructure = (StructurePlacementTypeCodec_StarterStructure) structureplacement1;
                Pair<BlockPos, Holder<Structure>> pair = spawnStructures_Forge$getNearestGeneratedStarterStructure(entry.getValue(), pLevel, structuremanager, pPos, pSkipKnownStructures, structurePlacementTypeCodecStarterStructure);
                if (pair != null) {
                    BlockPos blockpos = pair.getFirst();
                    double d0 = pPos.distSqr(blockpos);
                    if (d0 < d2) {
                        d2 = d0;
                        spawnStructures_Forge$changeReturn = true;
                        spawnStructures_Forge$newPair = pair;
                    }
                }
            }
        }
    }

    @Unique
    @Nullable
    private Pair<BlockPos, Holder<Structure>> spawnStructures_Forge$getNearestGeneratedStarterStructure(Set<Holder<Structure>> pStructureHoldersSet, ServerLevel pLevel, StructureManager pStructureManager, BlockPos pPos, boolean pSkipKnownStructures, StructurePlacementTypeCodec_StarterStructure pPlacement) {
        // !!! - This list causes locating errors after restarting the world. Save the ChunkGeneratorState to the level for usage as per ChunkGenerator.java - !!! //
        List<ChunkPos> list = ChunkGeneratorState_StarterStructure.getRingPositionsFor(pPlacement);
        newLog("Attempting to get Starter Structure locations...");
        if (list == null) {
            throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
        } else {
            newLog(list.toString());

            Pair<BlockPos, Holder<Structure>> pair = null;
            double d0 = Double.MAX_VALUE;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(ChunkPos chunkpos : list) {
                blockpos$mutableblockpos.set(SectionPos.sectionToBlockCoord(chunkpos.x, 8), 32, SectionPos.sectionToBlockCoord(chunkpos.z, 8));
                double d1 = blockpos$mutableblockpos.distSqr(pPos);
                boolean flag = pair == null || d1 < d0;
                newLog("" + flag);
                if (flag) {
                    Pair<BlockPos, Holder<Structure>> pair1 = spawnStructures_Forge$getStructureGeneratingAt(pStructureHoldersSet, pLevel, pStructureManager, pSkipKnownStructures, pPlacement, chunkpos);
                    newLog("" + pair1);
                    if (pair1 != null) {
                        pair = pair1;
                        d0 = d1;
                    }
                }
            }

            return pair;
        }
    }

    @Unique
    @Nullable
    private static Pair<BlockPos, Holder<Structure>> spawnStructures_Forge$getStructureGeneratingAt(Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, boolean pSkipKnownStructures, StructurePlacement pPlacement, ChunkPos pChunkPos) {
        for(Holder<Structure> holder : pStructureHoldersSet) {
            StructureCheckResult structurecheckresult = pStructureManager.checkStructurePresence(pChunkPos, holder.value(), pSkipKnownStructures);
            if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
                if (!pSkipKnownStructures && structurecheckresult == StructureCheckResult.START_PRESENT) {
                    return Pair.of(pPlacement.getLocatePos(pChunkPos), holder);
                }

                ChunkAccess chunkaccess = pLevel.getChunk(pChunkPos.x, pChunkPos.z, ChunkStatus.STRUCTURE_STARTS);
                StructureStart structurestart = pStructureManager.getStartForStructure(SectionPos.bottomOf(chunkaccess), holder.value(), chunkaccess);
                if (structurestart != null && structurestart.isValid() && (!pSkipKnownStructures || spawnStructures_Forge$tryAddReference(pStructureManager, structurestart))) {
                    return Pair.of(pPlacement.getLocatePos(structurestart.getChunkPos()), holder);
                }
            }
        }

        return null;
    }

    @Unique
    private static boolean spawnStructures_Forge$tryAddReference(StructureManager pStructureManager, StructureStart pStructureStart) {
        if (pStructureStart.canBeReferenced()) {
            pStructureManager.addReference(pStructureStart);
            return true;
        } else {
            return false;
        }
    }
}