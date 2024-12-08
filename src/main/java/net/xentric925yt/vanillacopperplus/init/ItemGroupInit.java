package net.xentric925yt.vanillacopperplus.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.Optional;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.MOD_ID;
import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class ItemGroupInit {
    public static final Text TITLE=Text.translatable("itemGroup."+MOD_ID+".copper_plus");
    public static final ItemGroup COPPER_PLUS=register("copper_plus", FabricItemGroup
            .builder()
            .displayName(TITLE)
            .icon(Items.COPPER_INGOT::getDefaultStack)
            .entries((displayContext, entries) ->
                Registries.ITEM.getIds()
                        .stream()
                        .filter(key->key.getNamespace().equals(MOD_ID))
                        .map(Registries.ITEM::getOrEmpty)
                        .map(Optional::orElseThrow)
                        .forEach(entries::add))
            .build());

    public static <T extends ItemGroup> T register(String name, T itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, id(name), itemGroup);
    }
    public static void load() {

    }
}
