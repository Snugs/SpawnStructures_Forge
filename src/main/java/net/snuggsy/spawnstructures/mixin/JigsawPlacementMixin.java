package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.*;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.functions.BlockRotFunctions.offsetLocation;

@Mixin(JigsawPlacement.class)
public abstract class JigsawPlacementMixin {

    @Unique @Final
    private static final String addPieces = "addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;I)Ljava/util/Optional;";

    @Inject(method = addPieces, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injected(Structure.GenerationContext pContext, Holder<StructureTemplatePool> pStartPool, Optional<ResourceLocation> pStartJigsawName, int pMaxDepth, BlockPos pPos, boolean pUseExpansionHack, Optional<Heightmap.Types> pProjectStartToHeightmap, int pMaxDistanceFromCenter, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir, RegistryAccess registryaccess, ChunkGenerator chunkgenerator, StructureTemplateManager structuretemplatemanager, LevelHeightAccessor levelheightaccessor, WorldgenRandom worldgenrandom, Registry registry, Rotation rotation) {
        startPool = pStartPool;
        if (startPool.is(startPoolLocation) && !worldInit){
            structureRotation = rotation;
            LOGGER.error("Starter Structure rotation found...");

            // Record the parameters of the Starter Structure
            pContextJP = pContext;
            pStartPoolJP = pStartPool;
            pStartJigsawNameJP = pStartJigsawName;
            pMaxDepthJP = pMaxDepth;
            pPosJP = pPos;
            pUseExpansionHackJP = pUseExpansionHack;
            pProjectStartToHeightmapJP = pProjectStartToHeightmap;
            pMaxDistanceFromCenterJP = pMaxDistanceFromCenter;
        }
    }

    @ModifyVariable(method = addPieces, at = @At("STORE"), ordinal = 2)
    private static BlockPos injected(BlockPos value) {
        if (startPool.is(startPoolLocation)) {
            if (changePos && placementReady) {
                changePos = false;
                worldInit = true;
                return offsetLocation(structureRotation);
            }
            //LOGGER.error("Injection value received as: " + value + " while ChangePos was equal to TRUE");
        }
        //LOGGER.error("Injection value received as: " + value);
        //LOGGER.error("Injection value sent as: " + structPos);
        return value;
    }

}
