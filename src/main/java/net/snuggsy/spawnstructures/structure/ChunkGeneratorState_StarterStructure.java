package net.snuggsy.spawnstructures.structure;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.Vec3;
import net.snuggsy.spawnstructures.data.GlobalVariables;
import net.snuggsy.spawnstructures.functions.CommandFunctions;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static net.snuggsy.spawnstructures.data.GlobalVariables.*;
import static net.snuggsy.spawnstructures.data.ServerSettings.biomeSelected;
import static net.snuggsy.spawnstructures.data.ServerSettings.setWorldSpawn;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.checkMoistBiome;
import static net.snuggsy.spawnstructures.functions.BlockPosFunctions.setSearchLocation;
import static net.snuggsy.spawnstructures.functions.NumberFunctions.*;

public class ChunkGeneratorState_StarterStructure {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static RandomState randomState;
    private static BiomeSource biomeSource;
    private static long levelSeed;
    private static long concentricRingsSeed = 0;
    private static final Map<Structure, List<StructurePlacement>> placementsForStructure = new Object2ObjectOpenHashMap<>();
    private static final Map<StructurePlacementTypeCodec_StarterStructure, CompletableFuture<List<ChunkPos>>> ringPositions = new Object2ObjectArrayMap<>();
    public static boolean hasGeneratedPositions = false;
    private static List<Holder<StructureSet>> possibleStructureSets = List.of();

    public static boolean hasBiomesForStructureSet(StructureSet pStructureSet, BiomeSource pBiomeSource) {
        Stream<Holder<Biome>> stream = pStructureSet.structures().stream().flatMap((p_255738_) -> {
            Structure structure = p_255738_.structure().value();
            return structure.biomes().stream();
        });
        return stream.anyMatch(pBiomeSource.possibleBiomes()::contains);
    }

    public ChunkGeneratorState_StarterStructure(RandomState pRandomState, BiomeSource pBiomeSource, long pLevelSeed, long pCocentricRingsSeed, List<Holder<StructureSet>> pPossibleStructureSets) {
        randomState = pRandomState;
        levelSeed = pLevelSeed;
        biomeSource = pBiomeSource;
        concentricRingsSeed = pCocentricRingsSeed;
        possibleStructureSets = pPossibleStructureSets;
    }

    public static List<Holder<StructureSet>> possibleStructureSets() {
        return possibleStructureSets;
    }

    private static void generatePositions() {
        Set<Holder<Biome>> set = biomeSource.possibleBiomes();
        possibleStructureSets().forEach((p_255638_) -> {
            StructureSet structureset = p_255638_.value();
            boolean flag = false;

            for(StructureSet.StructureSelectionEntry structureset$structureselectionentry : structureset.structures()) {
                Structure structure = structureset$structureselectionentry.structure().value();
                if (structure.biomes().stream().anyMatch(set::contains)) {
                    placementsForStructure.computeIfAbsent(structure, (p_256235_) -> {
                        return new ArrayList();
                    }).add(structureset.placement());
                    flag = true;
                }
            }

            if (flag) {
                StructurePlacement structureplacement = structureset.placement();
                if (structureplacement instanceof StructurePlacementTypeCodec_StarterStructure) {
                    StructurePlacementTypeCodec_StarterStructure structurePlacementTypeCodecStarterStructure = (StructurePlacementTypeCodec_StarterStructure)structureplacement;
                    ringPositions.put(structurePlacementTypeCodecStarterStructure, generateRingPositions(p_255638_, structurePlacementTypeCodecStarterStructure));
                }
            }

        });
    }

    private static CompletableFuture<List<ChunkPos>> generateRingPositions(Holder<StructureSet> pStructureSet, StructurePlacementTypeCodec_StarterStructure pPlacement) {
        int count = pPlacement.count();
        if (count == 0) {
            return CompletableFuture.completedFuture(List.of());
        } else {
            // Limit Count to 1 for now
            if (count > 1 || count < 0) {
                count = 1;
            }

            Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
            List<CompletableFuture<ChunkPos>> posList = new ArrayList<>(count);
            ChunkPos structChunk = new ChunkPos(0,0);
            String biomeLocation = CommandFunctions.getRawCommandOutput(globalServerLevel, Vec3.atBottomCenterOf(BlockPos.ZERO), "/locate biome minecraft:desert");
            newLog(biomeLocation);

            // Set the Search Location
            setSearchLocation();

            if (!Objects.equals(biomeSelected, "ANY") && !Objects.equals(biomeSelected, "ALL")) {
                AtomicInteger atomicBiomeCount = new AtomicInteger();
                List<ResourceKey<Biome>> selectedBiomes = new ArrayList<>();
                var biomes = globalServerLevel.registryAccess().registryOrThrow(Registries.BIOME);
                //biomes.getTagNames().forEach(biomeTagKey -> LOGGER.info(biomeTagKey.toString()));
                biomes.asLookup().listElementIds().forEach(biomeResourceKey -> {
                    if (biomeResourceKey.toString().contains(biomeSelected.toLowerCase())) {
                        atomicBiomeCount.getAndIncrement();
                        selectedBiomes.add(biomeResourceKey);
                    }
                });
                int selectedBiomeCount = atomicBiomeCount.intValue();
                if (selectedBiomeCount == 0) {
                    LOGGER.error("[Spawn Structures] Value for 'Set Spawn Biome' could not be parsed! Please adjust the Config file...");
                } else {
                    ArrayList<BlockPos> biomeCoords = new ArrayList<>();
                    ArrayList<Integer> biomeDist = new ArrayList<>();
                    String biome = "";
                    for (int i = 0; i < selectedBiomeCount; i++) {
                        String biomeKey = selectedBiomes.get(i).toString();
                        if (biomeKey.contains("[") && biomeKey.contains("]") && biomeKey.contains(" / ")) {
                            biome = biomeKey.split("/ ")[1].split("]")[0];
                        }
                        String rawBiomeCoords = CommandFunctions.getRawCommandOutput(globalServerLevel, Vec3.atBottomCenterOf(searchLocation), "/locate biome " + biome);
                        biomeCoords.add(convertCoordString(globalServerLevel, rawBiomeCoords, "XYZ"));
                        biomeDist.add(get3DCoordDist(searchLocation, biomeCoords.get(i)));
                    }
                    biome = selectedBiomes.get(getSmallestIntIndex(biomeDist)).toString().split("/ ")[1].split("]")[0];
                    newLog("Biome selected by Chunk Generator is: " + biome);
                    BlockPos closestBiome = biomeCoords.get(getSmallestIntIndex(biomeDist));
                    structPos = triangulateBiome(globalServerLevel, searchLocation, closestBiome, biome, true);
                    boolean ignoreMoist = false;
                    for (String moistException : moistExceptions) {
                        if (biome.contains(moistException.toLowerCase())) {
                            ignoreMoist = true;
                            break;
                        }
                    }
                    if (!ignoreMoist) {
                        structPos = checkMoistBiome(globalServerLevel, structPos, 10, "overworld");
                    }
                    assert structPos != null;
                    //structChunk = new ChunkPos(new BlockPos(structPos.getX(), 0, structPos.getZ()));
                }
            } else {
                if (setWorldSpawn) {
                    structPos = searchLocation;
                    assert structPos != null;
                    //structChunk = new ChunkPos(new BlockPos(structPos.getX(), 0, structPos.getZ()));
                } else {
                    structPos = BlockPos.ZERO;
                    int sizeOfRing = pPlacement.distance();
                    HolderSet<Biome> holderset = pPlacement.preferredBiomes();
                    RandomSource randomSource = RandomSource.create();
                    randomSource.setSeed((long) (1000000 * Math.random()));
                    double r1 = randomSource.nextDouble() * Math.PI * 2.0D;
                    double d1 = (double)(4 * sizeOfRing) + (randomSource.nextDouble() - 0.5D) * (double)sizeOfRing * 2.5D;
                    int x1 = (int)Math.round(Math.cos(r1) * d1);
                    int z1 = (int)Math.round(Math.sin(r1) * d1);
                    RandomSource randomSource1 = randomSource.fork();
                    Pair<BlockPos, Holder<Biome>> pair = biomeSource.findBiomeHorizontal(SectionPos.sectionToBlockCoord(x1, 8), 64, SectionPos.sectionToBlockCoord(z1, 8), 112, holderset::contains, randomSource1, randomState.sampler());
                    BlockPos potentialPos;
                    if (pair != null) {
                        potentialPos = pair.getFirst();
                    } else {
                        potentialPos = new BlockPos(x1*3, 0, z1*3);
                    }
                    GlobalVariables.posList.add(potentialPos);
                    if (GlobalVariables.posList.size() > 1) {
                        for (BlockPos pos : GlobalVariables.posList) {
                            if (pos != BlockPos.ZERO) {
                                structPos = pos;
                                break;
                            }
                        }
                    } else {
                        structPos = potentialPos;
                    }
                    //structChunk = new ChunkPos(structPos);
                }
            }
            if (structPos == null) {
                LOGGER.error("[Spawn Structures] Starter Structure failed to find a suitable spawn location. Reverting to world center...");
                structPos = BlockPos.ZERO;
                //structChunk = new ChunkPos(structPos);
            }
            structChunk = new ChunkPos(structPos);
            spawnStructures_Forge$chunkPos = structChunk;
            ChunkPos finalStructChunk = structChunk;
            posList.add(CompletableFuture.supplyAsync(() -> {
                return finalStructChunk;
            }, Util.backgroundExecutor()));

            /*else {
            int sizeOfRing = pPlacement.distance();
            int maxGenAttemptsThisRing = pPlacement.spread();
            HolderSet<Biome> holderset = pPlacement.preferredBiomes();
            RandomSource randomSource = RandomSource.create();
            randomSource.setSeed(concentricRingsSeed);
            double r1 = randomSource.nextDouble() * Math.PI * 2.0D;
            int genAttemptsThisRing = 0;
            int ringNumber = 0;

            for(int countI = 0; countI < count; ++countI) {
                // RingNumber(0) has Max Distance of 5.25x SizeOfRing and Min Distance of 2.75x SizeOfRing
                // --> Example: if distance = 100, then d1 can be from 275 to 525
                double d1 = (double)(4 * sizeOfRing + sizeOfRing * ringNumber * 6) + (randomSource.nextDouble() - 0.5D) * (double)sizeOfRing * 2.5D;
                newLog(String.valueOf(d1));
                int x1 = (int)Math.round(Math.cos(r1) * d1);
                int z1 = (int)Math.round(Math.sin(r1) * d1);
                RandomSource randomSource1 = randomSource.fork();
                list.add(CompletableFuture.supplyAsync(() -> {
                    Pair<BlockPos, Holder<Biome>> pair = biomeSource.findBiomeHorizontal(SectionPos.sectionToBlockCoord(x1, 8), 0, SectionPos.sectionToBlockCoord(z1, 8), 112, holderset::contains, randomSource1, randomState.sampler());
                    if (pair != null) {
                        BlockPos blockpos = pair.getFirst();
                        return new ChunkPos(SectionPos.blockToSectionCoord(blockpos.getX()), SectionPos.blockToSectionCoord(blockpos.getZ()));
                    } else {
                        return new ChunkPos(x1, z1);
                    }
                }, Util.backgroundExecutor()));
                r1 += (Math.PI * 2D) / (double)maxGenAttemptsThisRing;
                ++genAttemptsThisRing;
                if (genAttemptsThisRing == maxGenAttemptsThisRing) {
                    ++ringNumber;
                    genAttemptsThisRing = 0;
                    maxGenAttemptsThisRing += 2 * maxGenAttemptsThisRing / (ringNumber + 1);
                    maxGenAttemptsThisRing = Math.min(maxGenAttemptsThisRing, count - countI);
                    r1 += randomSource.nextDouble() * Math.PI * 2.0D;
                }}
            }*/

            // Return List of Positions
            return Util.sequence(posList).thenApply((logTime) -> {
                // Log Timer
                double time = (double)stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0D;
                LOGGER.debug("Calculation for {} took {}s", pStructureSet, time);
                return logTime;
            });
        }
    }

    public static void ensureStructuresGenerated() {
        if (!hasGeneratedPositions) {
            generatePositions();
            hasGeneratedPositions = true;
        }
    }

    @Nullable
    public static List<ChunkPos> getRingPositionsFor(StructurePlacementTypeCodec_StarterStructure pPlacement) {
        randomState = GlobalVariables.randomState;
        levelSeed = GlobalVariables.levelSeed;
        biomeSource = GlobalVariables.biomeSource;
        possibleStructureSets = structureSets;
        ensureStructuresGenerated();
        CompletableFuture<List<ChunkPos>> completablefuture = ringPositions.get(pPlacement);
        return completablefuture != null ? completablefuture.join() : null;
    }

    public List<StructurePlacement> getPlacementsForStructure(Holder<Structure> pStructure) {
        ensureStructuresGenerated();
        return placementsForStructure.getOrDefault(pStructure.value(), List.of());
    }

    public RandomState randomState() {
        return randomState;
    }

    public long getLevelSeed() {
        return levelSeed;
    }
}