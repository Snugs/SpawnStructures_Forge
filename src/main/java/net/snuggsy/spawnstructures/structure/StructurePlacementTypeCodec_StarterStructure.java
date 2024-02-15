package net.snuggsy.spawnstructures.structure;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.snuggsy.spawnstructures.SpawnStructures;

import java.util.List;
import java.util.Optional;

public class StructurePlacementTypeCodec_StarterStructure extends StructurePlacement {

    public static final Codec<StructurePlacementTypeCodec_StarterStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((instance) -> {
        return codec(instance).apply(instance, StructurePlacementTypeCodec_StarterStructure::new);
    }), StructurePlacementTypeCodec_StarterStructure::validate).codec();

    private static DataResult<StructurePlacementTypeCodec_StarterStructure> validate(StructurePlacementTypeCodec_StarterStructure pDistribution) {
        return pDistribution.spacing <= pDistribution.separation ? DataResult.error(() -> {
            return "Spacing has to be larger than separation";
        }) : DataResult.success(pDistribution);
    }

    private final int spacing;
    private final int separation;
    private final RandomSpreadType spreadType;
    private final int distance;
    private final int spread;
    private final int count;
    private final HolderSet<Biome> preferredBiomes;

    private static Products.P12<RecordCodecBuilder.Mu<StructurePlacementTypeCodec_StarterStructure>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>, Integer, Integer, RandomSpreadType, Integer, Integer, Integer, HolderSet<Biome>> codec(RecordCodecBuilder.Instance<StructurePlacementTypeCodec_StarterStructure> pInstance) {
        Products.P5<RecordCodecBuilder.Mu<StructurePlacementTypeCodec_StarterStructure>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>> p5 = placementCodec(pInstance);
        Products.P7<RecordCodecBuilder.Mu<StructurePlacementTypeCodec_StarterStructure>, Integer, Integer, RandomSpreadType, Integer, Integer, Integer, HolderSet<Biome>> p7 = pInstance.group(
                Codec.intRange(0, 4096).fieldOf("spacing").forGetter(StructurePlacementTypeCodec_StarterStructure::spacing),
                Codec.intRange(0, 4096).fieldOf("separation").forGetter(StructurePlacementTypeCodec_StarterStructure::separation),
                RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(StructurePlacementTypeCodec_StarterStructure::spreadType),
                Codec.intRange(0, 1023).fieldOf("distance").forGetter(StructurePlacementTypeCodec_StarterStructure::distance),
                Codec.intRange(0, 1023).fieldOf("spread").forGetter(StructurePlacementTypeCodec_StarterStructure::spread),
                Codec.intRange(1, 4095).fieldOf("count").forGetter(StructurePlacementTypeCodec_StarterStructure::count),
                RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("preferred_biomes").forGetter(StructurePlacementTypeCodec_StarterStructure::preferredBiomes));
        return new Products.P12<>(p5.t1(), p5.t2(), p5.t3(), p5.t4(), p5.t5(), p7.t1(), p7.t2(), p7.t3(), p7.t4(), p7.t5(), p7.t6(), p7.t7());
    }

    public StructurePlacementTypeCodec_StarterStructure(Vec3i pLocateOffset, FrequencyReductionMethod pFrequencyReductionMethod, float pFrequency, int pSalt, Optional<ExclusionZone> pExclusionZone, int spacing, int separation, RandomSpreadType spreadType, int distance, int spread, int count, HolderSet<Biome> preferredBiomes) {
        super(pLocateOffset, pFrequencyReductionMethod, pFrequency, pSalt, pExclusionZone);
        this.spacing = spacing;
        this.separation = separation;
        this.spreadType = spreadType;
        this.distance = distance;
        this.spread = spread;
        this.count = count;
        this.preferredBiomes = preferredBiomes;
    }

    public StructurePlacementTypeCodec_StarterStructure(int pSalt, int pSpacing, int pSeparation, RandomSpreadType pSpreadType, int pDistance, int pSpread, int pCount, HolderSet<Biome> pPreferredBiomes) {
        this(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1.0F, pSalt, Optional.empty(), pSpacing, pSeparation, pSpreadType, pDistance, pSpread, pCount, pPreferredBiomes);
    }

    public int spacing() {
        return this.spacing;
    }
    public int separation() {
        return this.separation;
    }
    public RandomSpreadType spreadType() {
        return this.spreadType;
    }
    public int distance() {
        return this.distance;
    }
    public int spread() {
        return this.spread;
    }
    public int count() {
        return this.count;
    }
    public HolderSet<Biome> preferredBiomes() {
        return this.preferredBiomes;
    }

    /*
    public CompletableFuture<List<ChunkPos>> getPotentialStructureChunk(long pSeed, int pRegionX, int pRegionZ) {
        //StarterStructure_ChunkGeneratorState.ensureStructuresGenerated();
        if (this.count() == 0) {
            return CompletableFuture.completedFuture(List.of());
        } else {
            if (ServerSettings.setWorldSpawn) {
                structureLocation = convertCoordString(globalServerLevel, specifiedLocation, "XZ-C");
                assert structureLocation != null;
                return new ChunkPos(new BlockPos(structureLocation.getX(), 0, structureLocation.getZ()));
            }
        }

        int i = Math.floorDiv(pRegionX, this.spacing);
        int j = Math.floorDiv(pRegionZ, this.spacing);
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureWithSalt(pSeed, i, j, this.salt());
        int k = this.spacing - this.separation;
        int l = this.spreadType.evaluate(worldgenrandom, k);
        int i1 = this.spreadType.evaluate(worldgenrandom, k);
        return new ChunkPos(i * this.spacing + l, j * this.spacing + i1);
    }*/

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState pStructureState, int pX, int pZ) {
        List<ChunkPos> list = ChunkGeneratorState_StarterStructure.getRingPositionsFor(this);
        return list != null && list.contains(new ChunkPos(pX, pZ));
    }

    @Override
    public StructurePlacementType<?> type() {
        return SpawnStructures.CUSTOM_PLACEMENT_CODEC.get();
    }
}

