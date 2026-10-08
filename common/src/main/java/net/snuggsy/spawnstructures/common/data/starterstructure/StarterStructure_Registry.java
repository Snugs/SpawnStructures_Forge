package net.snuggsy.spawnstructures.common.data.starterstructure;

import net.snuggsy.spawnstructures.common.data.struct.StructureData;

import java.util.HashMap;
import java.util.Map;

public final class StarterStructure_Registry {
    // Structure MetaData HashMap
    private static final Map<String, StructureData> DEFINITIONS = new HashMap<>();

    // HashMap Handlers
    public static void clear() {
        DEFINITIONS.clear();
    }

    public static void register(String id, StructureData data) {
        DEFINITIONS.put(id, data);
    }

    public static StructureData get(String id) {
        return DEFINITIONS.get(id);
    }

    public static Map<String, StructureData> all() {
        return DEFINITIONS;
    }
}
