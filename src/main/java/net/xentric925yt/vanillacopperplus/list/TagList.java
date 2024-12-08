package net.xentric925yt.vanillacopperplus.list;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;

public class TagList {
    public static class Blocks {
        public static final TagKey<Block> EXAMPLE_TAG = TagKey.of(RegistryKeys.BLOCK, VanillaCopperPlus.id("example"));

        public static final TagKey<Block> INCORRECT_FOR_EXAMPLE_TOOL =
                TagKey.of(RegistryKeys.BLOCK, VanillaCopperPlus.id("incorrect_for_example_tool"));
    }
}