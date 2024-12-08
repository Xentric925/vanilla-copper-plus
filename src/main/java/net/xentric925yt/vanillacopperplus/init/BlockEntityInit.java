package net.xentric925yt.vanillacopperplus.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.xentric925yt.vanillacopperplus.init.BlockEntities.CopperWheelBlockEntity;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class BlockEntityInit {
    public static BlockEntityType<CopperWheelBlockEntity> COPPER_WHEEL_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        COPPER_WHEEL_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                id( "copper_wheel"),
                BlockEntityType.Builder.create(CopperWheelBlockEntity::new,
                        BlockInit.COPPER_WHEEL,
                        BlockInit.EXPOSED_COPPER_WHEEL,
                        BlockInit.WEATHERED_COPPER_WHEEL,
                        BlockInit.OXIDIZED_COPPER_WHEEL,
                        BlockInit.WAXED_COPPER_WHEEL,
                        BlockInit.WAXED_EXPOSED_COPPER_WHEEL,
                        BlockInit.WAXED_WEATHERED_COPPER_WHEEL,
                        BlockInit.WAXED_OXIDIZED_COPPER_WHEEL
                ).build(null)
        );
    }
    public static void load() {
        registerBlockEntities();
    }
}
