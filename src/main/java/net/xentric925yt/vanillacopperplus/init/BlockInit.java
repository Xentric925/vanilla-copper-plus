package net.xentric925yt.vanillacopperplus.init;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperGenerator;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;
import net.xentric925yt.vanillacopperplus.init.Blocks.LettucesBlock;
import net.xentric925yt.vanillacopperplus.init.Blocks.VinegarCauldronBlock;
import net.xentric925yt.vanillacopperplus.init.Items.VinegarItem;
import net.xentric925yt.vanillacopperplus.list.FoodList;

import java.util.ArrayList;
import java.util.HashSet;

import static net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry.registerOxidizableBlockPair;
import static net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry.registerWaxableBlockPair;
import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class BlockInit {
    public static final Block VINEGAR_CAULDRON=register("vinegar_cauldron",new VinegarCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON)));
    public static final Block OXIDIZED_COPPER_GENERATOR = registerWithItem(
            "oxidized_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(Blocks.OXIDIZED_COPPER).solidBlock(Blocks::never))
    );
    public static final Block WEATHERED_COPPER_GENERATOR = registerWithItem(
            "weathered_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER).solidBlock(Blocks::never))
    );
    public static final Block EXPOSED_COPPER_GENERATOR = registerWithItem(
            "exposed_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(Blocks.EXPOSED_COPPER).solidBlock(Blocks::never))
    );
    public static final Block COPPER_GENERATOR = registerWithItem(
            "copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK).solidBlock(Blocks::never))
    );
    public static final Block WAXED_OXIDIZED_COPPER_GENERATOR = registerWithItem(
            "waxed_oxidized_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.OXIDIZED,AbstractBlock.Settings.copy(OXIDIZED_COPPER_GENERATOR).solidBlock(Blocks::never))
    );
    public static final Block WAXED_WEATHERED_COPPER_GENERATOR = registerWithItem(
            "waxed_weathered_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.WEATHERED,AbstractBlock.Settings.copy(WEATHERED_COPPER_GENERATOR).solidBlock(Blocks::never))
    );
    public static final Block WAXED_EXPOSED_COPPER_GENERATOR = registerWithItem(
            "waxed_exposed_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.EXPOSED,AbstractBlock.Settings.copy(EXPOSED_COPPER_GENERATOR).solidBlock(Blocks::never))
    );
    public static final Block WAXED_COPPER_GENERATOR = registerWithItem("waxed_copper_generator", new CopperGenerator(Oxidizable.OxidationLevel.UNAFFECTED,AbstractBlock.Settings.copy(COPPER_GENERATOR).solidBlock(Blocks::never)));

    public static final Block OXIDIZED_COPPER_WHEEL = registerWithItem(
            "oxidized_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(Blocks.OXIDIZED_COPPER))
    );
    public static final Block WEATHERED_COPPER_WHEEL = registerWithItem(
            "weathered_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER))
    );
    public static final Block EXPOSED_COPPER_WHEEL = registerWithItem(
            "exposed_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(Blocks.EXPOSED_COPPER))
    );
    public static final Block COPPER_WHEEL = registerWithItem(
            "copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK))
    );
    public static final Block WAXED_OXIDIZED_COPPER_WHEEL = registerWithItem(
            "waxed_oxidized_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.OXIDIZED,AbstractBlock.Settings.copy(OXIDIZED_COPPER_WHEEL))
    );
    public static final Block WAXED_WEATHERED_COPPER_WHEEL = registerWithItem(
            "waxed_weathered_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.WEATHERED,AbstractBlock.Settings.copy(WEATHERED_COPPER_WHEEL))
    );
    public static final Block WAXED_EXPOSED_COPPER_WHEEL = registerWithItem(
            "waxed_exposed_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.EXPOSED,AbstractBlock.Settings.copy(EXPOSED_COPPER_WHEEL))
    );
    public static final Block WAXED_COPPER_WHEEL = registerWithItem("waxed_copper_wheel", new CopperWheel(Oxidizable.OxidationLevel.UNAFFECTED,AbstractBlock.Settings.copy(COPPER_WHEEL)));

    public static final Block LETTUCES = register((String)"lettuces", new LettucesBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY)));


    public static HashSet<Block> Wheels=new HashSet<>();
    static {
        Wheels.add(COPPER_WHEEL);
        Wheels.add(EXPOSED_COPPER_WHEEL);
        Wheels.add(WEATHERED_COPPER_WHEEL);
        Wheels.add(OXIDIZED_COPPER_WHEEL);
        Wheels.add(WAXED_COPPER_WHEEL);
        Wheels.add(WAXED_EXPOSED_COPPER_WHEEL);
        Wheels.add(WAXED_WEATHERED_COPPER_WHEEL);
        Wheels.add(WAXED_OXIDIZED_COPPER_WHEEL);
    }


    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, id(name), block);
    }
    public static <T extends Block> T registerWithItem(String name, T block,Item.Settings settings) {
        T registered=register(name,block);
        ItemInit.register(name,new BlockItem(registered,settings));
        return registered;
    }
    public static <T extends Block> T registerWithItem(String name, T block){
        return registerWithItem(name,block,new Item.Settings());
    }
    public static void load() {

        registerOxidizableBlockPair(COPPER_GENERATOR, EXPOSED_COPPER_GENERATOR);
        registerOxidizableBlockPair(EXPOSED_COPPER_GENERATOR, WEATHERED_COPPER_GENERATOR);
        registerOxidizableBlockPair(WEATHERED_COPPER_GENERATOR, OXIDIZED_COPPER_GENERATOR);

        registerOxidizableBlockPair(COPPER_WHEEL, EXPOSED_COPPER_WHEEL);
        registerOxidizableBlockPair(EXPOSED_COPPER_WHEEL, WEATHERED_COPPER_WHEEL);
        registerOxidizableBlockPair(WEATHERED_COPPER_WHEEL, OXIDIZED_COPPER_WHEEL);



        registerWaxableBlockPair(COPPER_GENERATOR, WAXED_COPPER_GENERATOR);
        registerWaxableBlockPair(EXPOSED_COPPER_GENERATOR, WAXED_EXPOSED_COPPER_GENERATOR);
        registerWaxableBlockPair(WEATHERED_COPPER_GENERATOR, WAXED_WEATHERED_COPPER_GENERATOR);
        registerWaxableBlockPair(OXIDIZED_COPPER_GENERATOR, WAXED_OXIDIZED_COPPER_GENERATOR);

        registerWaxableBlockPair(COPPER_WHEEL, WAXED_COPPER_WHEEL);
        registerWaxableBlockPair(EXPOSED_COPPER_WHEEL, WAXED_EXPOSED_COPPER_WHEEL);
        registerWaxableBlockPair(WEATHERED_COPPER_WHEEL, WAXED_WEATHERED_COPPER_WHEEL);
        registerWaxableBlockPair(OXIDIZED_COPPER_WHEEL, WAXED_OXIDIZED_COPPER_WHEEL);
    }
}
