package net.xentric925yt.vanillacopperplus.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.xentric925yt.vanillacopperplus.init.BlockInit;
import net.xentric925yt.vanillacopperplus.init.ItemInit;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider  {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ItemStack WaterBottle=new ItemStack(Items.POTION);
        PotionContentsComponent potionContentsComponent = new PotionContentsComponent(Potions.WATER);
        WaterBottle.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ItemInit.VINEGAR)
                .pattern(" A ")
                .pattern("ASA")
                .pattern(" W ")
                .input('A', Items.APPLE)
                .input('S', Items.SUGAR)
                .input('W', Items.GLASS_BOTTLE)
                .criterion(hasItem(Items.APPLE), conditionsFromItem(Items.APPLE))
                .criterion(hasItem(Items.SUGAR), conditionsFromItem(Items.SUGAR))
                .criterion(hasItem(Items.GLASS_BOTTLE), conditionsFromItem(Items.GLASS_BOTTLE))
                .offerTo(exporter);
        //generators
        registerGenerator(BlockInit.COPPER_GENERATOR,Items.CHISELED_COPPER,exporter);
        registerGenerator(BlockInit.EXPOSED_COPPER_GENERATOR,Items.EXPOSED_CHISELED_COPPER,exporter);
        registerGenerator(BlockInit.WEATHERED_COPPER_GENERATOR,Items.WEATHERED_CHISELED_COPPER,exporter);
        registerGenerator(BlockInit.OXIDIZED_COPPER_GENERATOR,Items.OXIDIZED_CHISELED_COPPER,exporter);
        //wheels
        registerWheel(BlockInit.COPPER_WHEEL,Items.CUT_COPPER,Items.CUT_COPPER_SLAB,exporter);
        registerWheel(BlockInit.EXPOSED_COPPER_WHEEL,Items.EXPOSED_CUT_COPPER,Items.EXPOSED_CUT_COPPER_SLAB,exporter);
        registerWheel(BlockInit.WEATHERED_COPPER_WHEEL,Items.WEATHERED_CUT_COPPER,Items.WEATHERED_CUT_COPPER_SLAB,exporter);
        registerWheel(BlockInit.OXIDIZED_COPPER_WHEEL,Items.OXIDIZED_CUT_COPPER,Items.OXIDIZED_CUT_COPPER_SLAB,exporter);
    }
    public void registerGenerator(ItemConvertible generator,Item chiseled,RecipeExporter exporter){
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE,generator)
                .pattern("rqr")
                .pattern("qcq")
                .pattern("rqr")
                .input('r', Items.REDSTONE)
                .input('q', Items.QUARTZ)
                .input('c', chiseled)
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .criterion(hasItem(chiseled), conditionsFromItem(chiseled))
                .offerTo(exporter);
    }
    public void registerWheel(ItemConvertible wheel,Item cutCopper,Item cutCopperSlab,RecipeExporter exporter){
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE,wheel)
                .pattern("isi")
                .pattern("c c")
                .input('c', cutCopper)
                .input('s', cutCopperSlab)
                .input('i', Items.COPPER_INGOT)
                .criterion(hasItem(cutCopper), conditionsFromItem(cutCopper))
                .criterion(hasItem(cutCopperSlab), conditionsFromItem(cutCopperSlab))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter);
    }
}
