package net.snuggsy.spawnstructures.fabric.worldgen.structure;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.snuggsy.spawnstructures.common.worldgen.structure.StarterStructure_Codec;
import net.snuggsy.spawnstructures.common.worldgen.structure.StarterStructure_Registry;

import java.io.InputStreamReader;
import java.util.Map;

import static net.snuggsy.spawnstructures.common.util.Logger.LogError;

public class FabricStructure_ConfigLoader implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return new Identifier("spawnstructures", "structure_config");
    }

    @Override
    public void reload(ResourceManager manager) {
        StarterStructure_Registry.clear();

        Map<Identifier, Resource> resources =
                manager.findResources("structure_config", id -> id.getPath().endsWith(".json"));

        resources.forEach((id, resource) -> {
            try (var reader = new InputStreamReader(resource.getInputStream())) {
                JsonElement json = JsonParser.parseReader(reader);

                StarterStructure_Codec.CODEC.parse(JsonOps.INSTANCE, json)
                        .result().ifPresentOrElse(
                                def -> StarterStructure_Registry.register(id.getPath().replace(".json", ""), def),
                                () -> LogError("Failed to decode metadata: " + id)
                );
            } catch (Exception e) {
                LogError("Failed to load metadata: " + id + " — " + e);
            }
        });
    }
}
