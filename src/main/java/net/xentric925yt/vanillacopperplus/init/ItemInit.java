package net.xentric925yt.vanillacopperplus.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;
import net.xentric925yt.vanillacopperplus.init.Items.VinegarItem;
import net.xentric925yt.vanillacopperplus.list.FoodList;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class ItemInit {
    public static final Item VINEGAR=register("vinegar",new VinegarItem(new Item.Settings()));
    public static final Item LETTUCE = register((String)"lettuce", (Item)(new AliasedBlockItem(BlockInit.LETTUCES, (new Item.Settings()).food(FoodList.LETTUCE_COMPONENT))));

    public static final Item HAMSTER_SPAWN_EGG = register("hamster_spawn_egg", new SpawnEggItem(EntityInit.HAMSTER, 16747824, 16775119, new Item.Settings()));
    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    public static void load() {

    }
}
