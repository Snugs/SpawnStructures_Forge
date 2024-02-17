package net.snuggsy.spawnstructures.util;

import com.mojang.datafixers.util.Pair;

import java.util.List;

public class References {
    public static final String MOD_ID = "spawnstructures_forge";
    public static final String NAME = "Spawn Structures";
    public static final String VERSION = "1.0.8";
    public static final String ACCEPTED_VERSIONS = "[1.20.1]";
    public static final String CONFIG_VERSION = "1.0.5";

    private static final List<Pair<String, List<String>>> CONFIG_VERSION_ASSOCIATION = List.of(
            // Pair.of( CONFIG_VERSION , List.of( VERSIONS ))
            Pair.of("1.0.4", List.of("1.0.7")),
            Pair.of("1.0.5", List.of("1.0.8"))
    );
}
