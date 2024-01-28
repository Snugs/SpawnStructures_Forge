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
import net.snuggsy.spawnstructures.SpawnStructures;
import net.snuggsy.spawnstructures.config.ServerSettings;
import net.snuggsy.spawnstructures.events.StructureSpawnEvent;
import net.snuggsy.spawnstructures.util.StructureCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(JigsawPlacement.class)
public abstract class JigsawPlacementMixin {

    @Shadow @Final
    static final Logger LOGGER = LoggerFactory.getLogger(StructureSpawnEvent.class);

    @Unique
    private static Holder<StructureTemplatePool> newPool;

    @Unique
    private static ResourceLocation nbtLocation = new ResourceLocation("spawn-structures", "starter-structure");

    @Inject(method = "addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;I)Ljava/util/Optional;", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void injected(Structure.GenerationContext pContext, Holder<StructureTemplatePool> pStartPool, Optional<ResourceLocation> pStartJigsawName, int pMaxDepth, BlockPos pPos, boolean pUseExpansionHack, Optional<Heightmap.Types> pProjectStartToHeightmap, int pMaxDistanceFromCenter, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir, RegistryAccess registryaccess, ChunkGenerator chunkgenerator, StructureTemplateManager structuretemplatemanager, LevelHeightAccessor levelheightaccessor, WorldgenRandom worldgenrandom, Registry registry, Rotation rotation) {
        newPool = pStartPool;
        if (newPool.is(nbtLocation)){
            StructureSpawnEvent.structureRotation = rotation;
            LOGGER.error("ROTATION DETECTED EARLY:  " + rotation);
        }
    }

    @ModifyVariable(method = "addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;I)Ljava/util/Optional;", at = @At("STORE"), ordinal = 2)
    private static BlockPos injected(BlockPos value){
        if (SpawnStructures.changePos) {
            if (ServerSettings.spawnWorldCentre && newPool.is(nbtLocation)) {
                LOGGER.error("Structure Location Changed!!!");
                int structX;
                int structZ;
                if (StructureSpawnEvent.structureRotation == Rotation.CLOCKWISE_90) {
                    structX = StructureCoordinates.sizeCherryBlossom.getX() + StructureCoordinates.offsetCherryBlossom.getX() - 1;
                    structZ = StructureCoordinates.offsetCherryBlossom.getZ();
                } else if (StructureSpawnEvent.structureRotation == Rotation.CLOCKWISE_180) {
                    structX = StructureCoordinates.sizeCherryBlossom.getX() + StructureCoordinates.offsetCherryBlossom.getX() - 1;
                    structZ = StructureCoordinates.sizeCherryBlossom.getZ() + StructureCoordinates.offsetCherryBlossom.getZ() - 1;
                } else if (StructureSpawnEvent.structureRotation == Rotation.COUNTERCLOCKWISE_90) {
                    structX = StructureCoordinates.offsetCherryBlossom.getX();
                    structZ = StructureCoordinates.sizeCherryBlossom.getZ() + StructureCoordinates.offsetCherryBlossom.getZ() - 1;
                } else {
                    structX = StructureCoordinates.offsetCherryBlossom.getX();
                    structZ = StructureCoordinates.offsetCherryBlossom.getZ();
                }
                SpawnStructures.changePos = false;
                return new BlockPos(structX, 64, structZ);
            }
        }
        return value;
    }
}
