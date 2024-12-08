package net.xentric925yt.vanillacopperplus.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;
import net.xentric925yt.vanillacopperplus.init.BlockInit;
import net.xentric925yt.vanillacopperplus.init.ItemGroupInit;
import net.xentric925yt.vanillacopperplus.init.ItemInit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value) {
        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
            builder.add(translatableTextContent.getKey(), value);
        } else {
            VanillaCopperPlus.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemInit.LETTUCE, "Lettuce");
        translationBuilder.add(ItemInit.VINEGAR, "Vinegar");
        translationBuilder.add("item.vanilla-copper-plus.vinegar.effect.empty", "Vinegar");
        translationBuilder.add(BlockInit.VINEGAR_CAULDRON, "Cauldron");
        addText(translationBuilder, ItemGroupInit.TITLE, "Vanilla Copper Plus");
        translationBuilder.add(BlockInit.COPPER_GENERATOR, "Copper Generator");
        translationBuilder.add(BlockInit.COPPER_WHEEL, "Copper Wheel");
        translationBuilder.add(BlockInit.EXPOSED_COPPER_GENERATOR, "Exposed Copper Generator");
        translationBuilder.add(BlockInit.EXPOSED_COPPER_WHEEL, "Exposed Copper Wheel");
        translationBuilder.add(BlockInit.OXIDIZED_COPPER_GENERATOR, "Oxidized Copper Generator");
        translationBuilder.add(BlockInit.OXIDIZED_COPPER_WHEEL, "Oxidized Copper Wheel");
        translationBuilder.add(BlockInit.WEATHERED_COPPER_GENERATOR, "Weathered Copper Generator");
        translationBuilder.add(BlockInit.WEATHERED_COPPER_WHEEL, "Weathered Copper Wheel");
        translationBuilder.add(BlockInit.WAXED_COPPER_GENERATOR, "Waxed Copper Generator");
        translationBuilder.add(BlockInit.WAXED_COPPER_WHEEL, "Waxed Copper Wheel");
        translationBuilder.add(BlockInit.WAXED_EXPOSED_COPPER_GENERATOR, "Waxed Exposed Copper Generator");
        translationBuilder.add(BlockInit.WAXED_EXPOSED_COPPER_WHEEL, "Waxed Exposed Copper Wheel");
        translationBuilder.add(BlockInit.WAXED_OXIDIZED_COPPER_GENERATOR, "Waxed Oxidized Copper Generator");
        translationBuilder.add(BlockInit.WAXED_OXIDIZED_COPPER_WHEEL, "Waxed Oxidized Copper Wheel");
        translationBuilder.add(BlockInit.WAXED_WEATHERED_COPPER_GENERATOR, "Waxed Weathered Copper Generator");
        translationBuilder.add(BlockInit.WAXED_WEATHERED_COPPER_WHEEL, "Waxed Weathered Copper Wheel");
    }
}