package net.xentric925yt.vanillacopperplus.renderer;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.xentric925yt.vanillacopperplus.init.BlockEntities.CopperWheelBlockEntity;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;
import net.xentric925yt.vanillacopperplus.model.CopperWheelModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumer;

public class CopperWheelRenderer extends GeoBlockRenderer<CopperWheelBlockEntity> {

    public CopperWheelRenderer() {
        super(new CopperWheelModel());
    }

    @Override
    public void preRender(
            MatrixStack poseStack,
            CopperWheelBlockEntity animatable,
            BakedGeoModel model,
            @Nullable VertexConsumerProvider bufferSource,
            @Nullable VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int colour) {

        // Call the parent class's preRender method
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        // Get the block state to determine how the block is placed (face)
        BlockState state = animatable.getCachedState();
        BlockFace face = state.get(CopperWheel.FACE);
        Direction facing = state.get(CopperWheel.FACING);
        // Adjust model rotation based on block placement
        switch (face) {
            case FLOOR:
                // Default orientation for floor placement, no additional rotation needed
                break;
            case WALL:
                switch (facing){
                    case NORTH ->{
                        poseStack.multiply(new Quaternionf().rotateX((float) Math.toRadians(-90)));
                        poseStack.translate(0, -0.5, 0.5);
                    }
                    case SOUTH ->{
                        poseStack.multiply(new Quaternionf().rotateX((float) Math.toRadians(90)));
                        poseStack.translate(0, -0.5, -0.5);
                    }
                    case EAST ->{
                        poseStack.multiply(new Quaternionf().rotateZ((float) Math.toRadians(-90)));
                        poseStack.translate(-0.5, -0.5, 0);
                    }
                    case WEST ->{
                        poseStack.multiply(new Quaternionf().rotateZ((float) Math.toRadians(90)));
                        poseStack.translate(0.5, -0.5, 0);
                    }
                }
                break;
            case CEILING:
                poseStack.multiply(new Quaternionf().rotateX((float) Math.toRadians(180)));
                poseStack.translate(0, -1, 0);
                break;
        }
    }
}
