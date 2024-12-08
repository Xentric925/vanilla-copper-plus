package net.xentric925yt.vanillacopperplus.init.Items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.xentric925yt.vanillacopperplus.init.BlockInit;

public class VinegarItem extends Item {

    public VinegarItem(Settings properties) {
        super(properties
                .maxCount(16)
        );
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            var effect = new StatusEffectInstance(StatusEffects.NAUSEA, 200, 2);
            if (effect.getEffectType().value().isInstant()) {
                effect.getEffectType().value().applyInstantEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
            } else {
                user.addStatusEffect(effect);
            }
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrementUnlessCreative(1, playerEntity);
        }

        if (playerEntity == null || !playerEntity.isInCreativeMode()) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockState blockState = world.getBlockState(blockPos);

        // Check if the block at the BlockPos is a normal cauldron
        if (blockState.isOf(Blocks.CAULDRON)) {
            int currentLevel = 0;
            try {
                currentLevel = blockState.get(LeveledCauldronBlock.LEVEL);
            } catch (Exception e) {
                // Handle exception if needed
            }

            if (currentLevel < 3) { // Check if the cauldron isn't already full
                // Simulate the behavior of VinegarCauldronBlock
                world.setBlockState(blockPos, BlockInit.VINEGAR_CAULDRON.getDefaultState());

                // Consume the vinegar item (exchange with an empty bottle)
                playerEntity.setStackInHand(context.getHand(), ItemUsage.exchangeStack(itemStack, playerEntity, new ItemStack(Items.GLASS_BOTTLE)));

                // Increment statistics
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));

                if (!world.isClient) {
                    ServerWorld serverWorld = (ServerWorld) world;

                    // Spawn particles
                    for (int i = 0; i < 5; i++) {
                        serverWorld.spawnParticles(
                                ParticleTypes.SPLASH,
                                blockPos.getX() + world.random.nextDouble(),
                                blockPos.getY() + 1,
                                blockPos.getZ() + world.random.nextDouble(),
                                1,
                                0.0,
                                0.0,
                                0.0,
                                1.0
                        );
                    }
                }

                // Play sound of emptying the bottle into the cauldron
                world.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

                // Emit game event for placing fluid
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
