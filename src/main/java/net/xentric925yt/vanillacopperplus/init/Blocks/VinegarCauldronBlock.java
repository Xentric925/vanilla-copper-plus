package net.xentric925yt.vanillacopperplus.init.Blocks;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.world.event.GameEvent;
import net.xentric925yt.vanillacopperplus.init.Behaviors.VinegarCauldronBehavior;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static net.minecraft.block.Oxidizable.getIncreasedOxidationBlock;

public class VinegarCauldronBlock extends LeveledCauldronBlock {
    private static final int COOLDOWN_TICKS = 60; // 3 seconds * 20 ticks/second
    private final HashMap<UUID, Long> oxidationCooldowns = new HashMap<>();
    public VinegarCauldronBlock(Settings settings) {
        super(Biome.Precipitation.NONE, new CauldronBehavior.CauldronBehaviorMap("vinegar_cauldron_behavior", VinegarCauldronBehavior.VINEGAR_CAULDRON_BEHAVIOR), settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 1));
    }
    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity.isOnFire() && this.isEntityTouchingFluid(state, pos, entity)) {
            entity.extinguish();
            if (entity.canModifyAt(world, pos)) {
                this.onFireCollision(state, world, pos);
            }
        } else if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            ItemStack stack = itemEntity.getStack();
            UUID entityUUID = itemEntity.getUuid();

            // Check for cooldown
            long currentWorldTime = world.getTime();
            if (oxidationCooldowns.containsKey(entityUUID)) {
                long lastOxidizedTime = oxidationCooldowns.get(entityUUID);
                if (currentWorldTime - lastOxidizedTime < COOLDOWN_TICKS) {
                    return; // Cooldown period has not elapsed
                }
            }

            // Check if stack is a copper block and implements OxidizableBlock
            if (stack.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                if (block instanceof Oxidizable) {
                    Optional<Block> nextOxidation = getIncreasedOxidationBlock(block);

                    // Check if oxidation level can increase (has a next level)
                    if (nextOxidation.isPresent()) {
                        // Update oxidation level of all blocks in the stack
                        stack = new ItemStack(nextOxidation.get(), stack.getCount());
                        itemEntity.setStack(stack);

                        // Update cooldown
                        oxidationCooldowns.put(entityUUID, currentWorldTime);

                        // Decrement fluid level if oxidation occurred
                        decrementFluidLevel(state, world, pos);
                    }
                }
            }
        }
    }

    public static void decrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = (Integer)state.get(LEVEL) - 1;
        BlockState blockState = i == 0 ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, Integer.valueOf(i));
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
    }
    private void onFireCollision(BlockState state, World world, BlockPos pos) {
        decrementFluidLevel(state, world, pos);
    }
    public static void incrementFluidLevel(BlockState state, World world, BlockPos pos) {
        int i = (Integer)state.get(LEVEL) + 1;
        if(i>3){
            return;
        }
        BlockState blockState = i == 0 ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, Integer.valueOf(i));
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
    }
    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var b=VinegarCauldronBehavior.VINEGAR_CAULDRON_BEHAVIOR.get(stack.getItem());
        if(b==null){
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        CauldronBehavior cauldronBehavior = (CauldronBehavior) b;
        return cauldronBehavior.interact(state, world, pos, player, hand, stack);
    }

}
