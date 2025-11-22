package com.baisylia.cookscollection.world.feature;

import com.baisylia.cookscollection.CooksCollection;
import com.baisylia.cookscollection.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SALT_PLACED_KEY = registerKey("salt_placed");
    public static final ResourceKey<PlacedFeature> LEMON_PLACED_KEY = registerKey("lemon_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, SALT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_SALT_KEY),
                ModOrePlacement.commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.top())));

        register(context, LEMON_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LEMON_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1f, 1),
                        ModBlocks.LEMON_SAPLING.get()));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CooksCollection.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}