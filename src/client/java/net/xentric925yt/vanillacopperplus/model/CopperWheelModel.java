package net.xentric925yt.vanillacopperplus.model;

import net.minecraft.block.Oxidizable;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.xentric925yt.vanillacopperplus.init.BlockEntities.CopperWheelBlockEntity;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

import static net.minecraft.block.Oxidizable.OxidationLevel.*;
import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public class CopperWheelModel extends DefaultedBlockGeoModel<CopperWheelBlockEntity> {
    public CopperWheelModel() {
        super(id("copper_wheel"));
    }

    @Override
    public Identifier getAnimationResource(CopperWheelBlockEntity hamsterWheel) {
        return id("animations/copper_wheel.animation.json");
    }

    @Override
    public Identifier getTextureResource(CopperWheelBlockEntity hamsterWheel) {

        // Switch textures based on the oxidation level
        switch (hamsterWheel.getOxidationLevel()) {
            case UNAFFECTED:
                return id("textures/block/copper_wheel.png");
            case EXPOSED:
                return id("textures/block/exposed_copper_wheel.png");
            case WEATHERED:
                return id("textures/block/weathered_copper_wheel.png");
            case OXIDIZED:
                return id("textures/block/oxidized_copper_wheel.png");
            default:
                return super.getTextureResource(hamsterWheel);
        }
    }

    @Override
    public @Nullable RenderLayer getRenderType(CopperWheelBlockEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutout(getTextureResource(animatable));
    }
}