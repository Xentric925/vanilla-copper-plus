package net.xentric925yt.vanillacopperplus.model;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.xentric925yt.vanillacopperplus.init.Entities.Hamster;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class HamsterModel extends DefaultedEntityGeoModel<Hamster> {

    public HamsterModel() {
        super(id("hamster"), true);
    }

    @Override
    public Identifier getTextureResource(Hamster animatable) {
        return id("textures/entity/hamster/orange.png");
    }

    @Override
    public Identifier getAnimationResource(Hamster animatable) {
        return id("animations/hamster.animation.json");
    }

    @Override
    public RenderLayer getRenderType(Hamster animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }

    @Override
    public void setCustomAnimations(Hamster animatable, long instanceId, AnimationState<Hamster> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;
        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone sleep = this.getAnimationProcessor().getBone("sleep");
        GeoBone cheeks = this.getAnimationProcessor().getBone("cheeks");

        cheeks.setHidden(animatable.getMainHandStack().isEmpty());


        // Ensures there are no strange eye glitches when the hamster is sleeping or awake.
        if (animatable.isInSleepingPose()) {
            sleep.setHidden(false);
        } else {
            sleep.setHidden(true);
        }
        if (animatable.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        } else {
            // Setting values to 1.0F here prevents conflicts with GeckoLib and optimization mods.
            // Without this, adult heads will also size up. It's a bug :(
            head.setPosY(0F);
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }
    }


}
