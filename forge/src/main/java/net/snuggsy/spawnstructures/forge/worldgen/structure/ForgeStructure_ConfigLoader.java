package net.snuggsy.spawnstructures.forge.worldgen.structure;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snuggsy.spawnstructures.common.data.starterstructure.StarterStructure_Codec;
import net.snuggsy.spawnstructures.common.data.starterstructure.StarterStructure_Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.snuggsy.spawnstructures.common.util.Logger.LogError;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeStructure_ConfigLoader extends SimpleJsonResourceReloadListener {

    public ForgeStructure_ConfigLoader() {
        super(new Gson(), "structure_config");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        StarterStructure_Registry.clear();

        map.forEach((id, json) ->
            StarterStructure_Codec.CODEC.parse(JsonOps.INSTANCE, json)
                    .result()
                    .ifPresentOrElse(
                            def -> StarterStructure_Registry.register(id.getPath().replace(".json", ""), def),
                            () -> LogError("Failed to decode metadata: " + id)
            )
        );
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ForgeStructure_ConfigLoader());
    }
}
