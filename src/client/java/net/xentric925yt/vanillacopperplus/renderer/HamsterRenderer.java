package net.xentric925yt.vanillacopperplus.renderer;

import com.google.common.collect.Maps;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.xentric925yt.vanillacopperplus.init.Entities.Hamster;
import net.xentric925yt.vanillacopperplus.model.HamsterModel;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class HamsterRenderer extends GeoEntityRenderer<Hamster> {

    public HamsterRenderer(EntityRendererFactory.Context context) {
        super(context, new HamsterModel());
        this.shadowRadius = 0.3F;
    }

    private static final Map<Hamster.Variant, Identifier> TEXTURES = Util.make(Maps.newHashMap(), hashMap -> {
        /*hashMap.put(Hamster.Variant.WHITE, id("textures/entity/hamster/white.png"));
        hashMap.put(Hamster.Variant.PEACHES_AND_CREAM, id("textures/entity/hamster/peaches_and_cream.png"));*/
        hashMap.put(Hamster.Variant.ORANGE, id("textures/entity/hamster/orange.png"));/*
        hashMap.put(Hamster.Variant.GREY_WHITE, id("textures/entity/hamster/grey_white.png"));
        hashMap.put(Hamster.Variant.BROWN, id("textures/entity/hamster/brown.png"));
        hashMap.put(Hamster.Variant.BLACK_WHITE, id("textures/entity/hamster/black_white.png"));
        hashMap.put(Hamster.Variant.BLACK, id("textures/entity/hamster/black.png"));*/
    });

    @Override
    public @NotNull Identifier getTextureLocation(Hamster entity) {
        return TEXTURES.get(entity.getVariant());
    }

    @Override
    public void render(Hamster animatable, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
            if (animatable.isBaby()) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            } else {
                poseStack.scale(1F, 1F, 1F);
            }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
