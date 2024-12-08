package net.xentric925yt.vanillacopperplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.xentric925yt.vanillacopperplus.init.BlockInit;
import net.xentric925yt.vanillacopperplus.init.EntityInit;
import net.xentric925yt.vanillacopperplus.renderer.*;

import java.util.function.Supplier;

import static net.xentric925yt.vanillacopperplus.init.BlockEntityInit.COPPER_WHEEL_BLOCK_ENTITY;

public class VanillaCopperPlusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerRenderers();
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockInit.LETTUCES);
		/*ItemProperties.register(HamstersItems.HAMSTER, new ResourceLocation("variant"), (stack, world, entity, num) -> {
			CompoundTag compoundTag = stack.getTag();
			if (compoundTag != null && compoundTag.contains("Variant")) {
				return (float)compoundTag.getInt("Variant") / 7;
			}
			return 0.333F;
		});*/
	}

	public static void registerRenderers() {
		BlockEntityRendererFactories.register(COPPER_WHEEL_BLOCK_ENTITY, context -> new CopperWheelRenderer());
		BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.COPPER_WHEEL, RenderLayer.getCutout());
		EntityRendererRegistry.register(EntityInit.HAMSTER, HamsterRenderer::new);
		registerEntityRenderers(EntityInit.SEAT, SeatRenderer::new);
	}

	public static void clientInit() {
		registerModelLayers();
		registerBlockRenderLayers();
		registerRenderers();
	}

	private static void registerModelLayers() {
		EntityRendererRegistry.register(EntityInit.HAMSTER, HamsterRenderer::new);
	}

	private static void registerBlockRenderLayers() {
		// BlockRenderLayerMap.INSTANCE.putBlock(HamstersBlocks.TUNNEL, RenderType.translucent());
	}


	public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererFactory<T> renderProvider) {
		EntityRendererRegistry.register(type.get(), renderProvider);
	}
}