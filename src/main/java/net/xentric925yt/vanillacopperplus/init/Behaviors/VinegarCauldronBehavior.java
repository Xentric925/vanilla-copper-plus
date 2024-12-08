package net.xentric925yt.vanillacopperplus.init.Behaviors;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;
import net.xentric925yt.vanillacopperplus.init.Blocks.VinegarCauldronBlock;
import net.xentric925yt.vanillacopperplus.init.ItemInit;
import net.xentric925yt.vanillacopperplus.init.Items.VinegarItem;

import java.util.HashMap;
import java.util.Map;

public class VinegarCauldronBehavior implements CauldronBehavior{
    public static final Map<Item, CauldronBehavior> VINEGAR_CAULDRON_BEHAVIOR = new HashMap<>();

    static {
        VINEGAR_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (state.get(Properties.LEVEL_3) > 0) {
                if(!player.getAbilities().creativeMode)
                    player.setStackInHand(hand,ItemUsage.exchangeStack(stack, player, new ItemStack(ItemInit.VINEGAR)));

                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                VinegarCauldronBlock.decrementFluidLevel(state, world, pos);
                return ItemActionResult.success(world.isClient);
            }
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        });

        VINEGAR_CAULDRON_BEHAVIOR.put(ItemInit.VINEGAR, (state, world, pos, player, hand, stack) -> {
            if (state.get(Properties.LEVEL_3) < 3) {
                if(!player.getAbilities().creativeMode)
                    player.setStackInHand(hand,ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                VinegarCauldronBlock.incrementFluidLevel(state, world, pos);
                return ItemActionResult.success(world.isClient);
            }
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        });
    }

    @Override
    public ItemActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        Item item = stack.getItem();
        CauldronBehavior behavior = VINEGAR_CAULDRON_BEHAVIOR.get(item);
        if (behavior != null) {
            return behavior.interact(state, world, pos, player, hand, stack);
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}

