package net.snuggsy.spawnstructures;

public class SpawnStructures {
/*
    private static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, MOD_ID);
    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, MOD_ID);

    public static final RegistryObject<StructurePlacementType<StructurePlacementTypeCodec_StarterStructure>> CUSTOM_PLACEMENT_CODEC = STRUCTURE_PLACEMENT.register("custom_placement", () -> registerStructurePlacementType(StructurePlacementTypeCodec_StarterStructure.CODEC));
    public static final RegistryObject<StructureType<StructureTypeCodec_StarterStructure>> STARTER_STRUCTURE_CODEC = STRUCTURE_TYPE.register("starter_structure", () -> registerStructureType(StructureTypeCodec_StarterStructure.CODEC));

    public SpawnStructures() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpawnStructuresConfig_Common.SPEC, "Spawn Structures - Common.toml");
        globalReset();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        STRUCTURE_PLACEMENT.register(modEventBus);
        STRUCTURE_TYPE.register(modEventBus);
        modEventBus.addListener(this::loadComplete);
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(new StructureSpawnEvent_Forge());
    }

    private static <SP extends StructurePlacement> StructurePlacementType<SP> registerStructurePlacementType(Codec<SP> placementCodec) {
        return () -> placementCodec;
    }

    private static <S extends Structure> StructureType<S> registerStructureType(Codec<S> structureCodec) {
        return () -> structureCodec;
    }
*/
}