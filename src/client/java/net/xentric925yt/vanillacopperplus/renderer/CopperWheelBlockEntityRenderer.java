package net.xentric925yt.vanillacopperplus.renderer;

import net.minecraft.block.enums.BlockFace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;
import net.xentric925yt.vanillacopperplus.init.BlockEntities.CopperWheelBlockEntity;
import net.minecraft.state.property.Properties;
import net.xentric925yt.vanillacopperplus.init.BlockInit;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;

public class CopperWheelBlockEntityRenderer implements BlockEntityRenderer<CopperWheelBlockEntity> {
    private final BlockEntityRendererFactory.Context context;
    private final EntityRenderDispatcher entityRenderer;

    public CopperWheelBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super();
        this.context = context;
        this.entityRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher();
    }

    public void render(CopperWheelBlockEntity blockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        /*World world = blockEntity.getWorld();
        if (world != null) {
            MobEntity mob = blockEntity.getDisplayedMob();
            if (mob != null) {
                Direction facing = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
                BlockFace blockFace=blockEntity.getCachedState().get(CopperWheel.FACE);
                render(f, matrixStack, vertexConsumerProvider, i, mob, this.entityRenderer, blockEntity.lastRotation, blockEntity.rotation, facing,blockFace);
            }
        }*/
    }

    /*public static void render(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, EntityRenderDispatcher entityRenderDispatcher, double lastRotation, double rotation, Direction facing, BlockFace blockFace) {
        matrices.push();
        matrices.translate(0.5F, 0.0F, 0.5F);
        float f = 0.53125F;
        float g = Math.max(entity.getWidth(), entity.getHeight());
        if ((double) g > 0.65) {
            f /= g;
        }

        // Translation adjustments
        switch (blockFace){
            case FLOOR -> matrices.translate(0.0F, 0.5F, 0.0F);
            case CEILING -> matrices.translate(0.0F, 0.25F, 0.0F);
            case WALL -> {
                if(facing == Direction.SOUTH){
                    matrices.translate(0.0F, 0.375F, 0.2F);
                }else if(facing == Direction.EAST){
                    matrices.translate(0.2F, 0.375F, 0.0F);
                }else if(facing == Direction.WEST){
                    matrices.translate(-0.2F, 0.375F, 0.0F);
                }else if(facing == Direction.NORTH){
                    matrices.translate(0.0F, 0.375F, -0.2F);
                }
            }
            default -> matrices.translate(0.0F, 0.6F, 0.0F);
        }

        // Rotate the entity according to the rotation angle (0° to 180°)
        float rotationDegrees = (float) MathHelper.lerp(tickDelta, lastRotation, rotation);

        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            if(entity.getYaw()!=0){
                entity.setYaw(0);
            }
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationDegrees*-1));
        } else {
            if(entity.getYaw()!=90){
                //entity.setYaw(90);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationDegrees*-1));
            }else
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationDegrees));
        }



        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0F));
        matrices.scale(f, f, f);
        entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light);        matrices.pop();
    }*/

}