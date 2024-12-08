package net.xentric925yt.vanillacopperplus.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.xentric925yt.vanillacopperplus.init.BlockInit;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(BlockInit.VINEGAR_CAULDRON, Blocks.CAULDRON);
        addDrop(BlockInit.COPPER_GENERATOR);
        addDrop(BlockInit.EXPOSED_COPPER_GENERATOR);
        addDrop(BlockInit.WEATHERED_COPPER_GENERATOR);
        addDrop(BlockInit.OXIDIZED_COPPER_GENERATOR);
        addDrop(BlockInit.WAXED_COPPER_GENERATOR);
        addDrop(BlockInit.WAXED_EXPOSED_COPPER_GENERATOR);
        addDrop(BlockInit.WAXED_WEATHERED_COPPER_GENERATOR);
        addDrop(BlockInit.WAXED_OXIDIZED_COPPER_GENERATOR);
        addDrop(BlockInit.COPPER_WHEEL);
        addDrop(BlockInit.EXPOSED_COPPER_WHEEL);
        addDrop(BlockInit.OXIDIZED_COPPER_WHEEL);
        addDrop(BlockInit.WEATHERED_COPPER_WHEEL);
        addDrop(BlockInit.WAXED_COPPER_WHEEL);
        addDrop(BlockInit.WAXED_EXPOSED_COPPER_WHEEL);
        addDrop(BlockInit.WAXED_OXIDIZED_COPPER_WHEEL);
        addDrop(BlockInit.WAXED_WEATHERED_COPPER_WHEEL);
    }
}
