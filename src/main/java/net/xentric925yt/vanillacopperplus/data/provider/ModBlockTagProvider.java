package net.xentric925yt.vanillacopperplus.data.provider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.xentric925yt.vanillacopperplus.init.BlockInit;
import net.xentric925yt.vanillacopperplus.list.TagList;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        //getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
        //        .add(BlockInit.VINEGAR_CAULDRON);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockInit.VINEGAR_CAULDRON)
                .add(BlockInit.COPPER_GENERATOR)
                .add(BlockInit.EXPOSED_COPPER_GENERATOR)
                .add(BlockInit.WEATHERED_COPPER_GENERATOR)
                .add(BlockInit.OXIDIZED_COPPER_GENERATOR)
                .add(BlockInit.WAXED_COPPER_GENERATOR)
                .add(BlockInit.WAXED_EXPOSED_COPPER_GENERATOR)
                .add(BlockInit.WAXED_WEATHERED_COPPER_GENERATOR)
                .add(BlockInit.WAXED_OXIDIZED_COPPER_GENERATOR)
                .add(BlockInit.COPPER_WHEEL)
                .add(BlockInit.EXPOSED_COPPER_WHEEL)
                .add(BlockInit.WEATHERED_COPPER_WHEEL)
                .add(BlockInit.OXIDIZED_COPPER_WHEEL)
                .add(BlockInit.WAXED_COPPER_WHEEL)
                .add(BlockInit.WAXED_EXPOSED_COPPER_WHEEL)
                .add(BlockInit.WAXED_WEATHERED_COPPER_WHEEL)
                .add(BlockInit.WAXED_OXIDIZED_COPPER_WHEEL);

        /*getOrCreateTagBuilder(TagList.Blocks.EXAMPLE_TAG)
                .add(BlockInit.VINEGAR_CAULDRON)
                .add(Blocks.BLUE_ORCHID);
        getOrCreateTagBuilder(TagList.Blocks.INCORRECT_FOR_EXAMPLE_TOOL);*/
    }
}
