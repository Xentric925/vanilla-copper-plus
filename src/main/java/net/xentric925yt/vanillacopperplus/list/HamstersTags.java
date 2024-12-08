package net.xentric925yt.vanillacopperplus.list;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.xentric925yt.vanillacopperplus.init.ItemInit;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.MOD_ID;

public interface HamstersTags {

    public static final Ingredient HAMSTER_FOOD = Ingredient.ofItems(
            Items.WHEAT_SEEDS,
            Items.BEETROOT_SEEDS,
            Items.PUMPKIN_SEEDS,
            Items.MELON_SEEDS,
            ItemInit.LETTUCE // Your custom lettuce item
    );

    TagKey<Biome> HAS_HAMSTER = TagKey.of(RegistryKeys.BIOME, Identifier.of(MOD_ID, "has_hamster"));

}
