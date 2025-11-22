package com.baisylia.cookscollection.tab;

import com.baisylia.cookscollection.CooksCollection;
import com.baisylia.cookscollection.block.ModBlocks;
import com.baisylia.cookscollection.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CooksCollection.MOD_ID);

    public static final Supplier<CreativeModeTab> COOKSCOLLECTION_TAB = CREATIVE_MODE_TAB.register("cookscollections_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.LEMON.get()))
            .title(Component.translatable("creativetab.cookscollection.cookscollections_tab"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.LEMON.get());
                output.accept(ModItems.SALT.get());
                output.accept(ModItems.COOKING_OIL.get());
                output.accept(ModItems.CHOCOLATE_MUFFIN.get());
                output.accept(ModItems.LEMON_MUFFIN.get());
                output.accept(ModItems.FRIED_POTATO.get());
                output.accept(ModItems.FISH_AND_CHIPS.get());
                output.accept(ModItems.LEMONADE.get());
                output.accept(ModBlocks.RUSTIC_LOAF.get());
                output.accept(ModItems.RUSTIC_LOAF_SLICE.get());
                output.accept(ModBlocks.LEMON_SAPLING.get());
                output.accept(ModBlocks.LEMON_LOG.get());
                output.accept(ModBlocks.LEMON_WOOD.get());
                output.accept(ModBlocks.LEMON_LEAVES.get());
                output.accept(ModBlocks.FRUITING_LEMON_LEAVES.get());
                output.accept(ModBlocks.SALTED_DRIPSTONE_BLOCK.get());
                output.accept(ModBlocks.LEMON_CRATE.get());
                output.accept(ModBlocks.OVEN.get());

            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}