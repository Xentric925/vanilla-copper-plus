package net.xentric925yt.vanillacopperplus.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.xentric925yt.vanillacopperplus.init.BlockInit;
import net.xentric925yt.vanillacopperplus.init.Blocks.LettucesBlock;
import net.xentric925yt.vanillacopperplus.init.ItemInit;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //blockStateModelGenerator.registerDispenserLikeOrientable(BlockInit.COPPER_GENERATOR);
        //blockStateModelGenerator.registerCrop(BlockInit.LETTUCES, LettucesBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemInit.LETTUCE, Models.GENERATED);
    }


}
