package net.xentric925yt.vanillacopperplus.init.Entities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;
import net.xentric925yt.vanillacopperplus.init.EntityInit;

public class SeatEntity extends Entity {
    public SeatEntity(World level) {
        super(EntityInit.SEAT.get(), level);
        this.noClip = true;
    }

    public SeatEntity(World level, BlockPos pos) {
        this(level);
        switch (level.getBlockState(pos).get(CopperWheel.FACE)) {
            case FLOOR -> this.setPos(pos.getX() + 0.5, pos.getY() + 6.0/16.0+0.01, pos.getZ() + 0.5);
            case WALL -> {
                var b=new Vec3d(pos.getX() + 0.5, pos.getY() + 4.0/16.0+0.01, pos.getZ() + 0.5).offset(level.getBlockState(pos).get(CopperWheel.FACING), 2.0/16.0);
                this.setPos(b.x,b.y,b.z);
            }

            case CEILING -> this.setPos(pos.getX() + 0.5, pos.getY() + 2.0/16.0+0.01, pos.getZ() + 0.5);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }
    //boolean sync=false;
    @Override
    public void tick() {
        if (!this.getPassengerList().isEmpty()) {
            Entity passenger = this.getFirstPassenger(); // Assuming one passenger

            if (passenger instanceof Hamster && passenger.getYaw()!=this.getYaw()) {
                // Force synchronize the yaw of the hamster with the seat yaw
                passenger.setYaw(this.getYaw());
                passenger.setBodyYaw(this.getYaw());
                passenger.setHeadYaw(this.getYaw());
            }

        }
        if (this.getWorld().isClient) return;
        /*if(!sync){
            sync=true;
            this.getWorld().getServer().getPlayerManager().sendToAll(new EntityPositionS2CPacket(this));
        }*/
        BlockState state = this.getWorld().getBlockState(this.getBlockPos());
        boolean canMount;
        if (state.getBlock() instanceof CopperWheel hamsterWheelBlock) canMount = !this.getPassengerList().isEmpty()&&hamsterWheelBlock.isMountable(getWorld(),state,getBlockPos(),(Hamster)this.getPassengerList().getFirst());
        else canMount = false;
        if (hasPassengers() && canMount) {
            Direction facing = state.get(CopperWheel.FACING);

            float expectedYaw;
            switch (facing) {
                case NORTH:
                    expectedYaw = 180; // Face South
                    break;
                case SOUTH:
                    expectedYaw = 0;   // Face North
                    break;
                case WEST:
                    expectedYaw = 90;  // Face East
                    break;
                case EAST:
                    expectedYaw = 270; // Face West
                    break;
                default:
                    expectedYaw = this.getYaw(); // Default to current yaw
                    break;
            }

            // Check if yaw is incorrect and correct it
            if (this.getYaw() != expectedYaw) {
                this.setYaw(expectedYaw);

                // Also adjust the yaw of the passenger
                for (Entity passenger : this.getPassengerList()) {
                    passenger.setBodyYaw(expectedYaw);
                    passenger.setHeadYaw(expectedYaw);
                }
            }
            return;
        }
        if(!this.getPassengerList().isEmpty()){
            var hamster=(Hamster)this.getPassengerList().getFirst();
           /* var pos=this.updatePassengerForDismount(hamster);
            hamster.setPos(pos.x,pos.y,pos.z);*/
            hamster.stopRiding();
        }
        this.discard();
        this.getWorld().updateComparators(this.getBlockPos(), this.getWorld().getBlockState(this.getBlockPos()).getBlock());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        double seatHeight = 0.0;
        BlockState state = getWorld().getBlockState(this.getBlockPos());
        if (state.getBlock() instanceof CopperWheel hamsterWheelBlock) seatHeight = hamsterWheelBlock.seatHeight(state);

        seatHeight += getEntitySeatOffset(passenger);
        return this.getPos().offset(Direction.UP,seatHeight);
    }

    public static double getEntitySeatOffset(Entity entity) {
        return 0;
    }

    @Override
    protected boolean canStartRiding(Entity entity)
    {
        return true;
    }
    @Override
    public boolean canAddPassenger(Entity entity){
        if(entity instanceof Hamster)
            return true;
        return false;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        // Save the current yaw of the seat entity
        nbt.putFloat("SeatYaw", this.getYaw());
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("SeatYaw")) {
            float storedYaw = nbt.getFloat("SeatYaw");
            System.out.println("Restored Yaw: " + storedYaw); // Debugging line

            // Apply yaw with or without offset
            this.setYaw(storedYaw);
        }
    }


    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return new EntitySpawnS2CPacket(this, entityTrackerEntry);
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity entity) {
        BlockPos pos = this.getBlockPos();
        Vec3d safeVec;
        BlockState state = this.getWorld().getBlockState(pos);
        if (state.getBlock() instanceof CopperWheel hamsterWheelBlock) {
            safeVec = Dismounting.findRespawnPos(entity.getType(), this.getWorld(), hamsterWheelBlock.primaryDismountLocation(this.getWorld(), state, pos), false);
            if (safeVec != null) return safeVec.add(0, 0.25, 0);
        }

        Direction original = this.getFacing();
        Direction[] offsets = {original, original.rotateYClockwise(), original.rotateYCounterclockwise(), original.getOpposite()};
        for(Direction dir : offsets) {
            safeVec = Dismounting.findRespawnPos(entity.getType(), this.getWorld(), pos.offset(dir), false);
            if (safeVec != null) return safeVec.add(0, 0.25, 0);
        }
        return super.updatePassengerForDismount(entity);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        BlockPos pos = this.getBlockPos();
        BlockState state = this.getWorld().getBlockState(pos);

        if (state.getBlock() instanceof CopperWheel) {
            Direction facing = state.get(CopperWheel.FACING);

            switch (facing) {
                case NORTH:
                    this.setYaw(180); // Face South
                    break;
                case SOUTH:
                    this.setYaw(0);   // Face North
                    break;
                case WEST:
                    this.setYaw(90);  // Face East
                    break;
                case EAST:
                    this.setYaw(270); // Face West
                    break;
            }

            // Set the passenger's yaw to match the seat's yaw
            passenger.setPitch(0);
            passenger.setYaw(this.getYaw());
        }

        super.addPassenger(passenger);
    }

    public void addPassenger(Entity passenger, boolean force) {
        super.addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if (entity instanceof TameableEntity ta) ta.setInSittingPose(false);
    }
}