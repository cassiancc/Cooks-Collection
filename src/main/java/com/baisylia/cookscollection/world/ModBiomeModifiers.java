package com.baisylia.cookscollection.world;

import com.baisylia.cookscollection.CooksCollection;
import com.baisylia.cookscollection.world.feature.ModPlacedFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_SALT = registerKey("add_salt");
    public static final ResourceKey<BiomeModifier> ADD_TREE_LEMON = registerKey("add_tree_lemon");


    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        // CF -> PF -> BM
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_SALT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DRIPSTONE_CAVES)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SALT_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_TREE_LEMON, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SAVANNA), biomes.getOrThrow(Biomes.SAVANNA_PLATEAU), biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LEMON_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CooksCollection.MOD_ID, name));
    }
}