package net.xentric925yt.vanillacopperplus.init.Blocks;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.xentric925yt.vanillacopperplus.VanillaCopperPlus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class CopperGenerator extends OxidizableBlock {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty OVERLOADED = BooleanProperty.of("overloaded");
    private final int overload;
    public CopperGenerator(OxidationLevel oxidationLevel, Settings settings) {
        super(oxidationLevel, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OVERLOADED, false));
        switch (oxidationLevel) {
            case OXIDIZED -> overload = 6;
            case WEATHERED -> overload = 9;
            case EXPOSED -> overload = 12;
            default -> overload = 15;
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING,ctx.getPlayerLookDirection().getOpposite()).with(OVERLOADED, false);
    }
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updatePower(state, world, pos);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getWeakRedstonePower(world, pos, direction);
        //AbstractBlock
    }
    public int power=0;

    public void updatePower(BlockState state, World world, BlockPos pos){
        Direction outputDirection = state.get(FACING);
        power = 0;
        for (Direction side : Direction.values()) {
            if (side != outputDirection) {
                int neighborPower = world.getEmittedRedstonePower(pos.offset(side), side);
                power += neighborPower;
            }
        }

        // Check if it's overloaded
        boolean overloaded = state.get(OVERLOADED);
        if (power <= overload && overloaded) {
            // Reset overloaded state if power is within the threshold
            BlockState newState = state.with(OVERLOADED, false);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        } else if (power > overload) {
            // Limit power to the maximum value (overload)
            power = overload;

            // Update state to overloaded if not already overloaded
            if (!overloaded) {
                BlockState newState = state.with(OVERLOADED, true);
                world.setBlockState(pos, newState, Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        updatePower(state, world, pos);
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        Direction outputDirection = state.get(FACING);
        if (outputDirection == direction.getOpposite()) {
            return power;
        }
        return 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OVERLOADED);
    }
}
