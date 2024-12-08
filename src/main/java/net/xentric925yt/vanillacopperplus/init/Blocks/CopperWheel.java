package net.xentric925yt.vanillacopperplus.init.Blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import net.xentric925yt.vanillacopperplus.init.BlockEntities.CopperWheelBlockEntity;
import net.xentric925yt.vanillacopperplus.init.BlockEntityInit;
import net.xentric925yt.vanillacopperplus.init.Entities.Hamster;
import net.xentric925yt.vanillacopperplus.init.Entities.SeatEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CopperWheel extends BlockWithEntity implements Oxidizable,BlockEntityProvider {
    public final  Oxidizable.OxidationLevel OXIDATIONLEVEL;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockFace> FACE = EnumProperty.of("face", BlockFace.class);
    //public static final BooleanProperty POWERED = BooleanProperty.of("powered");

    private final int outPower;

    public CopperWheel(OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.OXIDATIONLEVEL =oxidationLevel;
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FACE, BlockFace.FLOOR));
                //.with(POWERED, false));

        switch (oxidationLevel) {
            case OXIDIZED -> outPower = 1;
            case WEATHERED -> outPower = 2;
            case EXPOSED -> outPower = 3;
            default -> outPower = 4;
        }
    }

    //Z Floor Shape
    public static final VoxelShape WEST_FLOOR_LEG = Block.createCuboidShape(2.0, 0.0, 7.0, 4.0, 8.0, 9.0);
    public static final VoxelShape EAST_FLOOR_LEG = Block.createCuboidShape(12.0, 0.0, 7.0, 14.0, 8.0, 9.0);
    public static final VoxelShape WEST_FLOOR_HINGE = Block.createCuboidShape(2.0, 8.0, 6.0, 4.0, 12.0, 10.0);
    public static final VoxelShape EAST_FLOOR_HINGE = Block.createCuboidShape(12.0, 8.0, 6.0, 14.0, 12.0, 10.0);
    public static final VoxelShape WEST_FLOOR_SIDE = VoxelShapes.union(WEST_FLOOR_LEG, WEST_FLOOR_HINGE);
    public static final VoxelShape EAST_FLOOR_SIDE = VoxelShapes.union(EAST_FLOOR_LEG, EAST_FLOOR_HINGE);
    public static final VoxelShape Z_FLOOR_SIDES = VoxelShapes.union(WEST_FLOOR_SIDE, EAST_FLOOR_SIDE);
    public static final VoxelShape Z_FLOOR_SHAPE = VoxelShapes.union(Z_FLOOR_SIDES, Block.createCuboidShape(4.0, 4.0, 2.0, 12.0, 16.0, 14.0));
    public static final VoxelShape Z_FLOOR_NEW_SHAPE = VoxelShapes.combineAndSimplify(Z_FLOOR_SHAPE, Block.createCuboidShape(4.0, 6.0, 4.0, 12.0, 14.0, 12.0), BooleanBiFunction.ONLY_FIRST);

    //X Floor Shape
    public static final VoxelShape NORTH_FLOOR_LEG = Block.createCuboidShape(7.0, 0.0, 2.0, 9.0, 8.0, 4.0);
    public static final VoxelShape SOUTH_FLOOR_LEG = Block.createCuboidShape(7.0, 0.0, 12.0, 9.0, 8.0, 14.0);
    public static final VoxelShape NORTH_FLOOR_HINGE = Block.createCuboidShape(6.0, 8.0, 2.0, 10.0, 12.0, 4.0);
    public static final VoxelShape SOUTH_FLOOR_HINGE = Block.createCuboidShape(6.0, 8.0, 12.0, 10.0, 12.0, 14.0);
    public static final VoxelShape NORTH_FLOOR_SIDE = VoxelShapes.union(NORTH_FLOOR_LEG, NORTH_FLOOR_HINGE);
    public static final VoxelShape SOUTH_FLOOR_SIDE = VoxelShapes.union(SOUTH_FLOOR_LEG, SOUTH_FLOOR_HINGE);
    public static final VoxelShape X_FLOOR_SIDES = VoxelShapes.union(NORTH_FLOOR_SIDE, SOUTH_FLOOR_SIDE);
    public static final VoxelShape X_FLOOR_SHAPE = VoxelShapes.union(X_FLOOR_SIDES, Block.createCuboidShape(2.0, 4.0, 4.0, 14.0, 16.0, 12.0));
    public static final VoxelShape X_FLOOR_NEW_SHAPE = VoxelShapes.combineAndSimplify(X_FLOOR_SHAPE, Block.createCuboidShape(4.0, 6.0, 4.0, 12.0, 14.0, 12.0), BooleanBiFunction.ONLY_FIRST);

    // South Wall Shape
    public static final VoxelShape SOUTH_WALL_WEST_LEG = Block.createCuboidShape(2.0, 7.0, 0.0, 4.0, 9.0, 8.0);
    public static final VoxelShape SOUTH_WALL_EAST_LEG = Block.createCuboidShape(12.0, 7.0, 0.0, 14.0, 9.0, 8.0);
    public static final VoxelShape SOUTH_WALL_WEST_HINGE = Block.createCuboidShape(2.0, 6.0, 8.0, 4.0, 10.0, 12.0);
    public static final VoxelShape SOUTH_WALL_EAST_HINGE = Block.createCuboidShape(12.0, 6.0, 8.0, 14.0, 10.0, 12.0);
    public static final VoxelShape SOUTH_WALL_WEST_SIDE = VoxelShapes.union(SOUTH_WALL_WEST_LEG, SOUTH_WALL_WEST_HINGE);
    public static final VoxelShape SOUTH_WALL_EAST_SIDE = VoxelShapes.union(SOUTH_WALL_EAST_LEG, SOUTH_WALL_EAST_HINGE);
    public static final VoxelShape SOUTH_WALL_SIDES = VoxelShapes.union(SOUTH_WALL_WEST_SIDE, SOUTH_WALL_EAST_SIDE);
    public static final VoxelShape SOUTH_WALL_SHAPE = VoxelShapes.union(SOUTH_WALL_SIDES, Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 14.0, 16.0));
    public static final VoxelShape SOUTH_WALL_NEW_SHAPE = VoxelShapes.combineAndSimplify(SOUTH_WALL_SHAPE, Block.createCuboidShape(4.0, 4.0, 6.0, 12.0, 12.0, 14.0), BooleanBiFunction.ONLY_FIRST);

    // North Wall Shape
    public static final VoxelShape NORTH_WALL_WEST_LEG = Block.createCuboidShape(2.0, 7.0, 7.0, 4.0, 9.0, 16.0);
    public static final VoxelShape NORTH_WALL_EAST_LEG = Block.createCuboidShape(12.0, 7.0, 7.0, 14.0, 9.0, 16.0);
    public static final VoxelShape NORTH_WALL_WEST_HINGE = Block.createCuboidShape(2.0, 6.0, 4.0, 4.0, 10.0, 8.0);
    public static final VoxelShape NORTH_WALL_EAST_HINGE = Block.createCuboidShape(12.0, 6.0, 4.0, 14.0, 10.0, 8.0);
    public static final VoxelShape NORTH_WALL_WEST_SIDE = VoxelShapes.union(NORTH_WALL_WEST_LEG, NORTH_WALL_WEST_HINGE);
    public static final VoxelShape NORTH_WALL_EAST_SIDE = VoxelShapes.union(NORTH_WALL_EAST_LEG, NORTH_WALL_EAST_HINGE);
    public static final VoxelShape NORTH_WALL_SIDES = VoxelShapes.union(NORTH_WALL_WEST_SIDE, NORTH_WALL_EAST_SIDE);
    public static final VoxelShape NORTH_WALL_SHAPE = VoxelShapes.union(NORTH_WALL_SIDES, Block.createCuboidShape(4.0, 2.0, 0.0, 12.0, 14.0, 12.0));
    public static final VoxelShape NORTH_WALL_NEW_SHAPE = VoxelShapes.combineAndSimplify(NORTH_WALL_SHAPE, Block.createCuboidShape(4.0, 4.0, 2.0, 12.0, 12.0, 10.0), BooleanBiFunction.ONLY_FIRST);

    // West Wall Shape
    public static final VoxelShape WEST_WALL_NORTH_LEG = Block.createCuboidShape(7.0, 7.0, 2.0, 16.0, 9.0, 4.0);
    public static final VoxelShape WEST_WALL_SOUTH_LEG = Block.createCuboidShape(7.0, 7.0, 12.0, 16.0, 9.0, 14.0);
    public static final VoxelShape WEST_WALL_NORTH_HINGE = Block.createCuboidShape(4.0, 6.0, 2.0, 8.0, 10.0, 4.0);
    public static final VoxelShape WEST_WALL_SOUTH_HINGE = Block.createCuboidShape(4.0, 6.0, 12.0, 8.0, 10.0, 14.0);
    public static final VoxelShape WEST_WALL_NORTH_SIDE = VoxelShapes.union(WEST_WALL_NORTH_LEG, WEST_WALL_NORTH_HINGE);
    public static final VoxelShape WEST_WALL_SOUTH_SIDE = VoxelShapes.union(WEST_WALL_SOUTH_LEG, WEST_WALL_SOUTH_HINGE);
    public static final VoxelShape WEST_WALL_SIDES = VoxelShapes.union(WEST_WALL_NORTH_SIDE, WEST_WALL_SOUTH_SIDE);
    public static final VoxelShape WEST_WALL_SHAPE = VoxelShapes.union(WEST_WALL_SIDES, Block.createCuboidShape(0.0, 2.0, 4.0, 12.0, 14.0, 12.0));
    public static final VoxelShape WEST_WALL_NEW_SHAPE = VoxelShapes.combineAndSimplify(WEST_WALL_SHAPE, Block.createCuboidShape(2.0, 4.0, 4.0, 10.0, 12.0, 12.0), BooleanBiFunction.ONLY_FIRST);

    // East Wall Shape
    public static final VoxelShape EAST_WALL_NORTH_LEG = Block.createCuboidShape(0.0, 7.0, 2.0, 8.0, 9.0, 4.0);
    public static final VoxelShape EAST_WALL_SOUTH_LEG = Block.createCuboidShape(0.0, 7.0, 12.0, 8.0, 9.0, 14.0);
    public static final VoxelShape EAST_WALL_NORTH_HINGE = Block.createCuboidShape(8.0, 6.0, 2.0, 12.0, 10.0, 4.0);
    public static final VoxelShape EAST_WALL_SOUTH_HINGE = Block.createCuboidShape(8.0, 6.0, 12.0, 12.0, 10.0, 14.0);
    public static final VoxelShape EAST_WALL_NORTH_SIDE = VoxelShapes.union(EAST_WALL_NORTH_LEG, EAST_WALL_NORTH_HINGE);
    public static final VoxelShape EAST_WALL_SOUTH_SIDE = VoxelShapes.union(EAST_WALL_SOUTH_LEG, EAST_WALL_SOUTH_HINGE);
    public static final VoxelShape EAST_WALL_SIDES = VoxelShapes.union(EAST_WALL_NORTH_SIDE, EAST_WALL_SOUTH_SIDE);
    public static final VoxelShape EAST_WALL_SHAPE = VoxelShapes.union(EAST_WALL_SIDES, Block.createCuboidShape(4.0, 2.0, 4.0, 16.0, 14.0, 12.0));
    public static final VoxelShape EAST_WALL_NEW_SHAPE = VoxelShapes.combineAndSimplify(EAST_WALL_SHAPE, Block.createCuboidShape(6.0, 4.0, 4.0, 14.0, 12.0, 12.0), BooleanBiFunction.ONLY_FIRST);

    //Z Ceiling Shape
    public static final VoxelShape WEST_CEILING_LEG = Block.createCuboidShape(2.0, 8.0, 7.0, 4.0, 16.0, 9.0);
    public static final VoxelShape EAST_CEILING_LEG = Block.createCuboidShape(12.0, 8.0, 7.0, 14.0, 16.0, 9.0);
    public static final VoxelShape WEST_CEILING_HINGE = Block.createCuboidShape(2.0, 4.0, 6.0, 4.0, 8.0, 10.0);
    public static final VoxelShape EAST_CEILING_HINGE = Block.createCuboidShape(12.0, 4.0, 6.0, 14.0, 8.0, 10.0);
    public static final VoxelShape WEST_CEILING_SIDE = VoxelShapes.union(WEST_CEILING_LEG, WEST_CEILING_HINGE);
    public static final VoxelShape EAST_CEILING_SIDE = VoxelShapes.union(EAST_CEILING_LEG, EAST_CEILING_HINGE);
    public static final VoxelShape Z_CEILING_SIDES = VoxelShapes.union(WEST_CEILING_SIDE, EAST_CEILING_SIDE);
    public static final VoxelShape Z_CEILING_SHAPE = VoxelShapes.union(Z_CEILING_SIDES, Block.createCuboidShape(4.0, 0.0, 2.0, 12.0, 12.0, 14.0));
    public static final VoxelShape Z_CEILING_NEW_SHAPE = VoxelShapes.combineAndSimplify(Z_CEILING_SHAPE, Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 10.0, 12.0), BooleanBiFunction.ONLY_FIRST);

    //X Ceiling Shape
    public static final VoxelShape NORTH_CEILING_LEG = Block.createCuboidShape(7.0, 8.0, 2.0, 9.0, 16.0, 4.0);
    public static final VoxelShape SOUTH_CEILING_LEG = Block.createCuboidShape(7.0, 8.0, 12.0, 9.0, 16.0, 14.0);
    public static final VoxelShape NORTH_CEILING_HINGE = Block.createCuboidShape(6.0, 4.0, 2.0, 10.0, 8.0, 4.0);
    public static final VoxelShape SOUTH_CEILING_HINGE = Block.createCuboidShape(6.0, 4.0, 12.0, 10.0, 8.0, 14.0);
    public static final VoxelShape NORTH_CEILING_SIDE = VoxelShapes.union(NORTH_CEILING_LEG, NORTH_CEILING_HINGE);
    public static final VoxelShape SOUTH_CEILING_SIDE = VoxelShapes.union(SOUTH_CEILING_LEG, SOUTH_CEILING_HINGE);
    public static final VoxelShape X_CEILING_SIDES = VoxelShapes.union(NORTH_CEILING_SIDE, SOUTH_CEILING_SIDE);
    public static final VoxelShape X_CEILING_SHAPE = VoxelShapes.union(X_CEILING_SIDES, Block.createCuboidShape(2.0, 0.0, 4.0, 14.0, 12.0, 12.0));
    public static final VoxelShape X_CEILING_NEW_SHAPE = VoxelShapes.combineAndSimplify(X_CEILING_SHAPE, Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 10.0, 12.0), BooleanBiFunction.ONLY_FIRST);


    private VoxelShape getShape(BlockState state) {
        Direction direction = state.get(FACING);
        return switch ((BlockFace) state.get(FACE)) {
            case FLOOR -> {
                if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                    yield Z_FLOOR_NEW_SHAPE;
                }
                yield X_FLOOR_NEW_SHAPE;
            }
            case WALL -> switch (direction) {
                case NORTH -> NORTH_WALL_NEW_SHAPE;
                case SOUTH -> SOUTH_WALL_NEW_SHAPE;
                case EAST -> EAST_WALL_NEW_SHAPE;
                default -> WEST_WALL_NEW_SHAPE;
            };
            case CEILING -> {
                if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                    yield Z_CEILING_NEW_SHAPE;
                }
                yield X_CEILING_NEW_SHAPE;
            }
            default -> X_FLOOR_NEW_SHAPE;
        };
    }

    @Override
    public MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE/*, POWERED*/);
    }

    public int getOutPower() {
        return this.outPower;
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        for (Direction direction : ctx.getPlacementDirections()) {
            BlockState blockState;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockState = this.getDefaultState()
                        .with(FACE, direction == Direction.UP ? BlockFace.CEILING : BlockFace.FLOOR)
                        .with(FACING, ctx.getHorizontalPlayerFacing());
            } else {
                blockState = this.getDefaultState().with(FACE, BlockFace.WALL).with(FACING, direction.getOpposite());
            }

            if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                return blockState;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperWheelBlockEntity(pos, state,OXIDATIONLEVEL);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.COPPER_WHEEL_BLOCK_ENTITY, world.isClient ? CopperWheelBlockEntity::clientTick : CopperWheelBlockEntity::serverTick);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        return super.getGameEventListener(world, blockEntity);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return this.OXIDATIONLEVEL;
    }

    public boolean isMountable(World world,BlockState state,BlockPos pos,Hamster entity) {
        return !isPowered(world,pos)&&!isOccupied(world, pos,entity);
    }

    public float setRiderRotation(BlockState state, Entity entity) {
        return entity.getYaw();
    }

    public BlockPos primaryDismountLocation(World level, BlockState state, BlockPos pos) {
        return pos;
    }
    public static boolean hasSeat(BlockView level, BlockPos pos) {
        //BlockState state = level.getBlockState(pos);
        // level.setBlock(pos, state.setValue(POWERED, true), 3);
        if(level instanceof World world)
            return !world.getEntitiesByClass(SeatEntity.class, new Box(new Vec3d(pos.getX()+0.8,pos.getY()+0.8,pos.getZ()+0.8),new Vec3d(pos.getX()+0.2,pos.getY()+0.2,pos.getZ()+0.2)), seat->true).isEmpty();
        return false;
    }

    public static boolean isOccupied(BlockView level, BlockPos pos/*, Hamster curr*/) {
        if(level instanceof World world){
            var list= world.getEntitiesByClass(Hamster.class,  new Box(new Vec3d(pos.getX()+0.8,pos.getY()+0.8,pos.getZ()+0.8),new Vec3d(pos.getX()+0.2,pos.getY()+0.2,pos.getZ()+0.2)),hamster->true);
            if(!list.isEmpty()){
                //world.updateComparators(pos, level.getBlockState(pos).getBlock());
                return true;
            }
        }
        return false;
    }
    public static boolean isOccupied(BlockView level, BlockPos pos, Hamster curr) {
        if(level instanceof World world){
            var list= world.getEntitiesByClass(Hamster.class,  new Box(new Vec3d(pos.getX()+0.8,pos.getY()+0.8,pos.getZ()+0.8),new Vec3d(pos.getX()+0.2,pos.getY()+0.2,pos.getZ()+0.2)),hamster->true);
            return !list.isEmpty()&& list.get(0)!=curr;
        }
        return false;
    }

    public static void sitDown(World level, BlockPos pos, Entity entity) {
        if (level.isClient) return;
        if (entity == null) return;

        SeatEntity seat = new SeatEntity(level, pos);
        level.spawnEntity(seat);
        entity.startRiding(seat, true);

        //level.updateComparators(pos, level.getBlockState(pos).getBlock());
        //updateNeighbors(level,pos);
        //level.updateListeners(pos, level.getBlockState(pos), level.getBlockState(pos), Block.NOTIFY_ALL);
        level.scheduleBlockTick(pos, level.getBlockState(pos).getBlock(), 2);
    }
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        // Perform delayed comparator update
        world.updateComparators(pos, this);
    }


    private static void updateNeighbors(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);

            // Notify the neighbor
            //world.updateNeighbor(neighborPos, world.getBlockState(pos).getBlock(), pos);

            // Check if the neighboring block is a comparator and force an update
            if (neighborState.getBlock() instanceof ComparatorBlock) {
                world.updateComparators(neighborPos, neighborState.getBlock());
            }
        }
    }


    public float seatHeight(BlockState state) {
        return 0F;
    }
    /*public static Optional<Entity> getLeashed(PlayerEntity player) {
        List<Entity> entities = player.getWorld().getOtherEntities((Entity) null, player.getBoundingBox().expand(10), e -> true);
        for (Entity e : entities)
            if (e instanceof MobEntity mob && mob.getLeashHolder() == player && canBePickedUp(e)) return Optional.of(mob);
        return Optional.empty();
    }

    public static boolean ejectSeatedExceptPlayer(World level, SeatEntity seatEntity) {
        List<Entity> passengers = seatEntity.getPassengerList();
        if (passengers.isEmpty()) return false;
        if (!level.isClient) seatEntity.removeAllPassengers();
        seatEntity.discard();
        return true;
    }*/

    public static boolean canBePickedUp(Entity passenger) {
        if (passenger instanceof PlayerEntity) return false;
        return passenger instanceof LivingEntity;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true; // Enable comparator output
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperWheelBlockEntity wheelEntity && isOccupied(world,pos)) {
            return outPower;
        }
        return 0;
    }
    public static boolean isPowered(World world, BlockPos pos) {
        var b=world.isReceivingRedstonePower(pos);
        return b;
    }

    /*@Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isEmpty() || !player.isSneaking()) {
            if (!world.canPlayerModifyAt(player, pos)) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            if (!isMountable(state) || player.hasVehicle() || player.isSneaking()) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            if (hasSeat(world, pos)) {


                List<SeatEntity> seats = world.getEntitiesByClass(SeatEntity.class, new Box(pos),seat->true);

                if (seats.getFirst().getFirstPassenger() instanceof Hamster hamster) {
                    hamster.setWaitTimeWhenRunningTicks(0);
                    hamster.setWaitTimeBeforeRunTicks(hamster.getRandom().nextInt(200) + 600);
                }

                if (ejectSeatedExceptPlayer(world, seats.getFirst())) return ItemActionResult.SUCCESS;
                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (getLeashed(player).isPresent() && getLeashed(player).get() instanceof Hamster hamster) {
                hamster.setWaitTimeBeforeRunTicks(0);
                sitDown(world, pos, hamster);
            }
            return ItemActionResult.SUCCESS;
        }
        return super.onUseWithItem(stack,state, world, pos, player, hand, hit);
    }*/

}
