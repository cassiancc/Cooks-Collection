package com.baisylia.cookscollection.world.feature;

import com.baisylia.cookscollection.CooksCollection;
import com.baisylia.cookscollection.block.ModBlocks;
import com.baisylia.cookscollection.block.custom.FruitingLeaves;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SALT_KEY = registerKey("salt");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEMON_KEY = registerKey("lemon");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldSalt = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.SALTED_DRIPSTONE_BLOCK.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.SALTED_DRIPSTONE_BLOCK.get().defaultBlockState()));

        register(context, OVERWORLD_SALT_KEY, Feature.ORE, new OreConfiguration(overworldSalt, 30));

        register(context, LEMON_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.LEMON_LOG.get()),
                //new LemonTrunkPlacer(5, 2, 2),
                new StraightTrunkPlacer(5, 2, 2),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(ModBlocks.LEMON_LEAVES.get().defaultBlockState(),
                        8).add(ModBlocks.FRUITING_LEMON_LEAVES.get().defaultBlockState().setValue(FruitingLeaves.AGE, 4), 1)),
                //new MegaJungleFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), 2),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                //new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(2)),
                //new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4),
                //new AcaciaFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build());


    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CooksCollection.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}