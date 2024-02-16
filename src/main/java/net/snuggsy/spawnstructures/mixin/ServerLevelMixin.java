package net.snuggsy.spawnstructures.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static net.snuggsy.spawnstructures.data.GlobalVariables.globalServerLevel;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {

    protected ServerLevelMixin(
            WritableLevelData pLevelData,
            ResourceKey<Level> pDimension,
            RegistryAccess pRegistryAccess,
            Holder<DimensionType> pDimensionTypeRegistration,
            Supplier<ProfilerFiller> pProfiler,
            boolean pIsClientSide,
            boolean pIsDebug,
            long pBiomeZoomSeed,
            int pMaxChainedNeighborUpdates
    ) {
        super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
    }

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void injection(
            MinecraftServer pServer,
            Executor pDispatcher,
            LevelStorageSource.LevelStorageAccess pLevelStorageAccess,
            ServerLevelData pServerLevelData,
            ResourceKey pDimension,
            LevelStem pLevelStem,
            ChunkProgressListener pProgressListener,
            boolean pIsDebug,
            long pBiomeZoomSeed,
            List pCustomSpawners,
            boolean pTickTime,
            RandomSequences pRandomSequences,
            CallbackInfo ci
    ) {
        globalServerLevel = ServerLevel.class.cast(this);
    }

}