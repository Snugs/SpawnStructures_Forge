package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.snuggsy.spawnstructures.data.StructureCoordinates;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.offsetLocation;

@Mixin(JigsawPlacement.class)
public abstract class JigsawPlacementMixin {

    @Unique @Final
    private static final String spawnStructures_Forge$addPieces = "addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;I)Ljava/util/Optional;";

    @Inject(method = spawnStructures_Forge$addPieces, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injected(Structure.GenerationContext pContext, Holder<StructureTemplatePool> pStartPool, Optional<ResourceLocation> pStartJigsawName, int pMaxDepth, BlockPos pPos, boolean pUseExpansionHack, Optional<Heightmap.Types> pProjectStartToHeightmap, int pMaxDistanceFromCenter, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir, RegistryAccess registryaccess, ChunkGenerator chunkgenerator, StructureTemplateManager structuretemplatemanager, LevelHeightAccessor levelheightaccessor, WorldgenRandom worldgenrandom, Registry registry, Rotation rotation) {
        startPool = pStartPool;
        if (startPool.is(startPoolLocation) && !worldInit){
            structureRotation = rotation;
            newLog("Starter Structure rotation found...");
        }
    }

    @ModifyVariable(method = spawnStructures_Forge$addPieces, at = @At("STORE"), ordinal = 2)
    private static BlockPos injected(BlockPos value) {
        if (startPool.is(startPoolLocation)) {
            if (changePos && placementReady) {
                changePos = false;
                worldInit = true;
                structureLocation = new BlockPos(structureLocation.getX(), structureLocation.getY() + StructureCoordinates.getStructureOffset(chosenStructure).getY(), structureLocation.getZ());
                return offsetLocation(structureLocation, structureRotation);
            }
        }
        return value;
    }
}