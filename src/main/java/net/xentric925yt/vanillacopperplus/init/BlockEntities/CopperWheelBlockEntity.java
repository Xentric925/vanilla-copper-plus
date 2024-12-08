package net.xentric925yt.vanillacopperplus.init.BlockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xentric925yt.vanillacopperplus.init.BlockEntityInit;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;

public class CopperWheelBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final Oxidizable.OxidationLevel oxidationLevel;
    public double rotation=0;

    public CopperWheelBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COPPER_WHEEL_BLOCK_ENTITY, pos, state);
        this.oxidationLevel = Oxidizable.OxidationLevel.UNAFFECTED;
    }

    public CopperWheelBlockEntity(BlockPos pos, BlockState state, OxidizableBlock.OxidationLevel oxidationLevel) {
        super(BlockEntityInit.COPPER_WHEEL_BLOCK_ENTITY, pos, state);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        return super.onSyncedBlockEvent(type, data);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!this.world.isClient) {
            this.sync();
        }else {
            // Request a re-render on the client
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    public void sync() {
        if (this.world != null && !this.world.isClient) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, CopperWheelBlockEntity blockEntity) {

    }

    public float getRotationSpeed() {
        switch (this.oxidationLevel) {
            case OXIDIZED:
                return 0.25f;
            case WEATHERED:
                return 0.5f;
            case EXPOSED:
                return 0.75f;
            default:
                return 1.0f;
        }
    }

    public Oxidizable.OxidationLevel getOxidationLevel() {
        return oxidationLevel;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CopperWheelBlockEntity) {
            return this.pos.equals(((CopperWheelBlockEntity) other).pos);
        }
        return false;
    }

    protected static final RawAnimation SPIN = RawAnimation.begin().thenLoop("animation.sf_nba.copper_wheel.spin");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::controller));
    }
    private <E extends CopperWheelBlockEntity> PlayState controller(final AnimationState<E> event) {
        assert world != null;
        BlockPos blockPos = this.pos;
        if (CopperWheel.isOccupied(world, blockPos)) {
            event.getController().setAnimation(SPIN);
            switch (this.oxidationLevel) {
                case OXIDIZED:
                    event.getController().setAnimationSpeed(0.5f);
                    break;
                case WEATHERED:
                    event.getController().setAnimationSpeed(0.75f);
                    break;
                case EXPOSED:
                    event.getController().setAnimationSpeed(1.0f);
                    break;
                default:
                    event.getController().setAnimationSpeed(1.25f);
                    break;
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    // Inside your CopperWheelBlockEntity class
    private long lastTickTime = System.currentTimeMillis();
    private boolean wasPaused = false;

    public static void clientTick(World world, BlockPos pos, BlockState state, CopperWheelBlockEntity blockEntity) {
        blockEntity.detectPauseResume();
    }

    private void detectPauseResume() {
        long currentTime = System.currentTimeMillis();
        // Check if more than a typical tick interval has passed (50ms in Minecraft for 20 ticks per second)
        if (currentTime - lastTickTime > 100) { // 100ms threshold to detect a pause
            // Game was likely paused and is now resuming
            onGameResume();
            wasPaused = true;
        } else {
            wasPaused = false;
        }
        lastTickTime = currentTime;
    }

    private void onGameResume() {
        // Reset or reinitialize animation controller
        resetAnimationController();
    }

    private void resetAnimationController() {
        ArrayList<AnimationController<? extends GeoAnimatable>> controllers = new ArrayList<>();
        controllers.add(new AnimationController<>(this, "controller", 0, this::controller));
        AnimatableManager.ControllerRegistrar controllerRegistrar = new AnimatableManager.ControllerRegistrar(controllers);
        registerControllers(controllerRegistrar); // Re-register controllers to restart animations
    }

}
