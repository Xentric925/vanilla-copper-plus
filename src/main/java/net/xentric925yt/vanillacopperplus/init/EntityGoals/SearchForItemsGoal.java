package net.xentric925yt.vanillacopperplus.init.EntityGoals;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.xentric925yt.vanillacopperplus.init.Entities.Hamster;

import java.util.EnumSet;
import java.util.List;

public class SearchForItemsGoal extends Goal {
    private final Hamster mob;
    private final double speedModifier;
    private final double horizontalSearchRange;
    private final double verticalSearchRange;
    private final Ingredient ingredient;

    public SearchForItemsGoal(Hamster mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
        this.setControls(EnumSet.of(Goal.Control.MOVE));
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.ingredient = ingredient;
        this.horizontalSearchRange = horizontalSearchRange;
        this.verticalSearchRange = verticalSearchRange;
    }

    @Override
    public boolean canStart() {
        if (mob.isSitting() || mob.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            Box searchBox = mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange);
            List<ItemEntity> itemEntities = mob.getWorld().getEntitiesByClass(ItemEntity.class, searchBox, itemEntity -> ingredient.test(itemEntity.getStack()));
            return !itemEntities.isEmpty();
        }
        return false;
    }

    @Override
    public void start() {
        List<ItemEntity> list = mob.getWorld().getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
        if (!list.isEmpty()) {
            mob.getNavigation().startMovingTo(list.get(0), speedModifier);
        }

    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && !mob.isInSittingPose();
    }

    @Override
    public void tick() {
        List<ItemEntity> list = mob.getWorld().getEntitiesByClass(ItemEntity.class, mob.getBoundingBox().expand(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getStack()));
        ItemStack itemstack = mob.getStackInHand(Hand.MAIN_HAND);
        if (itemstack.isEmpty() && !list.isEmpty()) {
            mob.getNavigation().startMovingTo(list.get(0), speedModifier);
        }

    }
}
