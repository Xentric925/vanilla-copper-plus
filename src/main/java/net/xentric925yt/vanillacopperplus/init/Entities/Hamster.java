package net.xentric925yt.vanillacopperplus.init.Entities;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.xentric925yt.vanillacopperplus.init.BlockEntities.CopperWheelBlockEntity;
import net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel;
import net.xentric925yt.vanillacopperplus.init.EntityGoals.SearchForItemsGoal;
import net.xentric925yt.vanillacopperplus.init.EntityInit;
import net.xentric925yt.vanillacopperplus.list.HamstersSoundEvents;
import net.xentric925yt.vanillacopperplus.list.HamstersTags;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Predicate;

import static net.xentric925yt.vanillacopperplus.init.Blocks.CopperWheel.FACING;

public class Hamster extends TameableEntity implements GeoEntity {
    // region
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.run");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.sleep");
    protected static final RawAnimation STANDING = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.standing");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final TrackedData<Integer> EAT_COUNTER = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.INTEGER);

    private static final Ingredient FOOD_ITEMS = HamstersTags.HAMSTER_FOOD;
    private static final TrackedData<Boolean> DATA_INTERESTED = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> DATA_VARIANT = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> FROM_HAND = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> WAIT_TIME_BEFORE_RUN = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> WAIT_TIME_WHEN_RUNNING = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IN_SLEEPING_POSE = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_RIDING = DataTracker.registerData(Hamster.class, TrackedDataHandlerRegistry.BOOLEAN);


    Hamster.HamsterGoToWheelGoal hamsterGoToWheelGoal;
    // endregion

    public Hamster(EntityType<? extends Hamster> entityType, World world) {
        super(entityType, world);
        if (!this.isInSleepingPose()) {
            this.setCanPickUpLoot(true);
        }
        this.lookControl = new Hamster.HamsterLookControl();
        // region PATHFINDING
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_CAUTIOUS, 1.0F);
        this.setPathfindingPenalty((PathNodeType.DANGER_POWDER_SNOW), 1.0F);
        this.setPathfindingPenalty((PathNodeType.DANGER_OTHER), 1.0F);
        this.setPathfindingPenalty((PathNodeType.DAMAGE_OTHER), 1.0F);
        this.setPathfindingPenalty((PathNodeType.WATER_BORDER), 1.0F);
        registerGoals();
        // endregion
    }

    // region BASIC ENTITY

    protected void registerGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new TameableEscapeDangerGoal( 1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1.25, FOOD_ITEMS, false));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        //this.goalSelector.add(3, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(3, new SleepGoal());
        this.hamsterGoToWheelGoal = new HamsterGoToWheelGoal();
        this.goalSelector.add(4, this.hamsterGoToWheelGoal);
        this.goalSelector.add(5, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F));
        this.goalSelector.add(6, new RunInWheelGoal());
        this.goalSelector.add(7, new SearchForItemsGoal(this, 1.25F, FOOD_ITEMS, 8.0D, 8.0D));
        this.goalSelector.add(8, new WanderAroundFarGoal (this, 1.0));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
    }

    @Override
    public void mobTick() {
        if (this.getMoveControl().isMoving()) {
            this.setSprinting(this.getMoveControl().getSpeed() >= 1.3D);
        } else {
            this.setSprinting(false);
        }
        super.mobTick();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        ActionResult actionResult;
        if (this.isTamed()) {
            if (this.isOwner(player)) {
                if (this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                    if (!this.getWorld().isClient()) {
                        this.eat(player, hand, itemStack);
                        FoodComponent foodComponent = (FoodComponent)itemStack.get(DataComponentTypes.FOOD);
                        this.heal(foodComponent != null ? (float)foodComponent.nutrition() : 1.0F);
                    }

                    return ActionResult.success(this.getWorld().isClient());
                }

                actionResult = super.interactMob(player, hand);
                if (!actionResult.isAccepted()) {
                    this.setSitting(!this.isSitting());
                    return ActionResult.success(this.getWorld().isClient());
                }

                return actionResult;
            }
        } else if (this.isBreedingItem(itemStack)) {
            if (!this.getWorld().isClient()) {
                this.eat(player, hand, itemStack);
                this.tryTame(player);
                this.setPersistent();
            }

            return ActionResult.success(this.getWorld().isClient());
        }

        actionResult = super.interactMob(player, hand);
        if (actionResult.isAccepted()) {
            this.setPersistent();
        }
        return actionResult;
    }
    private void tryTame(PlayerEntity player) {
        if (this.random.nextInt(3) == 0) {
            this.setOwner(player);
            this.navigation.stop();
            this.setTarget((LivingEntity)null);
            this.setSitting(true);
            this.setInSittingPose(true);
            this.getWorld().sendEntityStatus(this, (byte)7);
        } else {
            this.getWorld().sendEntityStatus(this, (byte)6);
        }

    }


    protected float getStandingEyeHeight(EntityPose pose, EntityDimensions entityDimensions) {
        return this.isBaby() ? 0.2F : 0.3F;
    }

    // endregion

    // region CATCHING

    /*public ActionResult catchHamster(PlayerEntity player) {
        ItemStack output = this.getCaughtItemStack();
        saveDefaultDataToItemTag(this, output);
        if (!player.().add(output)) {
            ItemEntity itemEntity = new ItemEntity(level(), this.getX(), this.getY() + 0.5, this.getZ(), output);
            itemEntity.setPickUpDelay(0);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
            level().addFreshEntity(itemEntity);
        }
        this.discard();
        player.getInventory().add(output);
        return InteractionResult.sidedSuccess(true);
    }

    private static void saveDefaultDataToItemTag(Hamster mob, ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (mob.hasCustomName()) {
            itemStack.setHoverName(mob.getCustomName());
        }
        try {
            compoundTag.putShort("Air", (short)mob.getAirSupply());
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
            if (mob.isCustomNameVisible()) compoundTag.putBoolean("CustomNameVisible", mob.isCustomNameVisible());
            if (mob.isSilent()) compoundTag.putBoolean("Silent", mob.isSilent());
            if (mob.isNoGravity()) compoundTag.putBoolean("NoGravity", mob.isNoGravity());
            if (mob.hasGlowingTag()) compoundTag.putBoolean("Glowing", true);
            mob.addAdditionalSaveData(compoundTag);
        }
        catch (Throwable var9) {
            CrashReport crashReport = CrashReport.forThrowable(var9, "Saving entity NBT");
            CrashReportCategory crashReportCategory = crashReport.addCategory("Entity being saved");
            mob.fillCrashReportCategory(crashReportCategory);
            throw new ReportedException(crashReport);
        }
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }*/

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    /*public ItemStack getCaughtItemStack() {
        return new ItemStack(HamstersItems.HAMSTER);
    }*/
    // endregion

    // region SOUNDS
    protected SoundEvent getAmbientSound() {
        return HamstersSoundEvents.HAMSTER_AMBIENT;
    }

    protected SoundEvent getSleepSound(){
        return HamstersSoundEvents.HAMSTER_SLEEP;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return HamstersSoundEvents.HAMSTER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HamstersSoundEvents.HAMSTER_DEATH;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 3.0F);
    }

    protected void playBegSound() {
        this.playSound(HamstersSoundEvents.HAMSTER_BEG, this.getSoundVolume(), this.getSoundPitch());
    }

    protected float getSoundVolume() {
        return 0.4F;
    }
    // endregion

    // region DATA

    void clearStates(boolean clearSitting) {
        this.setIsInterested(false);
        this.setInSleepingPose(false);
        if(clearSitting)
            this.setSitting(false);
    }

    public void setIsInterested(boolean bl) {
        this.dataTracker.set(DATA_INTERESTED, bl);
    }
    public boolean isInterested() {
        return this.dataTracker.get(DATA_INTERESTED);
    }

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        return false;
    }


    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        Hamster hamster = EntityInit.HAMSTER.create(world);
        if (hamster != null && entity instanceof Hamster hamster1) {
            if (this.random.nextBoolean()) {
                hamster.setVariant(this.getVariant());
            } else {
                hamster.setVariant(hamster1.getVariant());
            }

            if (this.isTamed()) {
                hamster.setOwner((PlayerEntity) this.getOwner());
                hamster.setTamed(true,true);
            }
        }
        return hamster;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
         super.initDataTracker(builder);
        builder.add(EAT_COUNTER, 0);
        builder.add(DATA_INTERESTED, false);
        builder.add(DATA_VARIANT, 2);
        builder.add(WAIT_TIME_BEFORE_RUN, 0);
        builder.add(WAIT_TIME_WHEN_RUNNING, 0);
        builder.add(FROM_HAND, false);
        builder.add(IN_SLEEPING_POSE, false);
        builder.add(IS_RIDING, false);
    }
    public boolean isRiding(){
        return this.dataTracker.get(IS_RIDING);
    }

    public void setIsRiding(boolean isRiding){
        this.dataTracker.set(IS_RIDING,isRiding);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compoundTag) {
        super.readCustomDataFromNbt(compoundTag);
        this.setVariant(Hamster.Variant.BY_ID[compoundTag.getInt("Variant")]);
        this.setWaitTimeBeforeRunTicks(compoundTag.getInt("RunTicks"));
        /*this.setWaitTimeWhenRunningTicks(compoundTag.getInt("RunningTicks"));*/
        this.setFromHand(compoundTag.getBoolean("FromHand"));
        if (compoundTag.contains("lastPosX")) {
            this.lastPosX = compoundTag.getDouble("lastPosX");
        }
        if (compoundTag.contains("lastPosY")) {
            this.lastPosY = compoundTag.getDouble("lastPosY");
        }
        if (compoundTag.contains("lastPosZ")) {
            this.lastPosZ = compoundTag.getDouble("lastPosZ");
        }
        if (compoundTag.getBoolean("hasSeat")) {
            setIsRiding(true);
            //this.setPos(lastPosX,lastPosY,lastPosZ);
            /*if(this.getY()!=this.getVehicle().getY()){
                this.setPos(this.getVehicle().getPos().x,this.getVehicle().getPos().y,this.getVehicle().getPos().z);
            }*/
        }

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compoundTag) {
        super.writeCustomDataToNbt(compoundTag);
        compoundTag.putInt("Variant", getVariant().getId());
        compoundTag.putInt("RunTicks", this.getWaitTimeBeforeRunTicks());
        /*compoundTag.putInt("RunningTicks", this.getWaitTimeWhenRunningTicks());*/
        compoundTag.putBoolean("FromHand", this.fromHand());
        compoundTag.putDouble("lastPosX", this.lastPosX==null?this.getX():this.lastPosX);
        compoundTag.putDouble("lastPosY", this.lastPosY==null?this.getY():this.lastPosY);
        compoundTag.putDouble("lastPosZ", this.lastPosZ==null?this.getZ():this.lastPosZ);
        compoundTag.putBoolean("hasSeat",this.isRiding());
    }

    public int getWaitTimeBeforeRunTicks() {
        var i=this.dataTracker.get(WAIT_TIME_BEFORE_RUN);
        return i;
    }
    public void setWaitTimeBeforeRunTicks(int ticks) {
        this.dataTracker.set(WAIT_TIME_BEFORE_RUN, ticks);
    }

    /*public int getWaitTimeWhenRunningTicks() {
        return this.dataTracker.get(WAIT_TIME_WHEN_RUNNING);
    }
    public void setWaitTimeWhenRunningTicks(int ticks) {
        this.dataTracker.set(WAIT_TIME_WHEN_RUNNING, ticks);
    }*/

    public void setInSleepingPose(boolean sleeping) {
        this.dataTracker.set(IN_SLEEPING_POSE, sleeping);
    }

    public boolean isInSleepingPose() {
        return (Boolean)this.dataTracker.get(IN_SLEEPING_POSE);
    }

    public void wakeHamsterUp() {
        this.setInSleepingPose(false);
    }
    private Double lastPosX=null;
    private Double lastPosY=null;
    private Double lastPosZ=null;

    private boolean isSleepingInBed(){
        return true;
    }

    public void setYaw(float yaw) {
        super.setYaw(yaw);
    }

    public void tick() {
        super.tick();
        if(this.getWorld().isClient&&getWorld().getBlockState(this.getBlockPos()).getBlock() instanceof CopperWheel&&this.isRiding()&&this.getVehicle()!=null&&this.bodyYaw!=this.getVehicle().getYaw()){
            this.setBodyYaw(this.getVehicle().getYaw());
            this.setHeadYaw(this.getVehicle().getYaw());
        }
        if(lastPosX==null){
            lastPosX=this.getX();
            lastPosY=this.getY();
            lastPosZ=this.getZ();
        }
        if (this.getX() != lastPosX || this.getY() != lastPosY || this.getZ() != lastPosZ) {
            double distance = this.squaredDistanceTo(lastPosX, lastPosY, lastPosZ);

            double TELEPORT_THRESHOLD = 5;
            if (distance > TELEPORT_THRESHOLD) {  // TELEPORT_THRESHOLD is a large enough distance to detect a teleport
                onHamsterTeleported();
            }

            // Update last known position
            lastPosX = this.getX();
            lastPosY = this.getY();
            lastPosZ = this.getZ();
        }
        if (this.canMoveVoluntarily()) {
            if (this.isInsideWaterOrBubbleColumn() || this.getTarget() != null || this.getWorld().isThundering()) {
                this.wakeHamsterUp();
            }

            if (this.isInsideWaterOrBubbleColumn() || this.isInSleepingPose()) {
                this.setSitting(false);
            }
        }
    }

    public void onHamsterTeleported() {
        if (this.hasVehicle() && this.getVehicle() instanceof SeatEntity seat) {
            this.setWaitTimeBeforeRunTicks(this.random.nextInt(400) + 2000);
            this.stopRiding();
            /*var pos=seat.updatePassengerForDismount(this);
            this.setPos(pos.x,pos.y,pos.z);*/
            seat.discard();
            this.clearStates(false);
        }
    }

    public int getMaxLookPitchChange() {
        return this.isInSittingPose() ? 20 : super.getMaxLookPitchChange();
    }


    @Override
    public void tickMovement() {
        super.tickMovement();
        //this.getWorld();

        //this.blockPosition();
        if (this.getWorld().getBlockState(this.getBlockPos()).getBlock() instanceof CopperWheel) {
            this.setVelocity(0, 0, 0);
        }

        // System.out.println(getWaitTimeWhenRunningTicks());

        if (!this.hasVehicle() && this.getWaitTimeBeforeRunTicks() > 0) {
            this.setWaitTimeBeforeRunTicks(this.getWaitTimeBeforeRunTicks() - 1);
        }
        if (this.hasVehicle() && this.getVehicle() instanceof SeatEntity seat && (CopperWheel.isPowered(getWorld(),seat.getBlockPos())||getWorld().hasRain(seat.getBlockPos().up()))) {
            this.setWaitTimeBeforeRunTicks(this.random.nextInt(400) + 2000);
            this.stopRiding();
            var pos=seat.updatePassengerForDismount(this);
            this.setPos(pos.x,pos.y,pos.z);
            seat.discard();
            this.clearStates(true);
        }

        if (this.isInterested()) {
            if (this.age % 40 == 0) {
                this.playBegSound();
            }
        }

        List<PlayerEntity> list = this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(8.0D, 4.0D, 8.0D),playerEntity -> true);
        if (this.isAlive() && !this.isInterested() && !this.isInSleepingPose() && !this.isImmobile() && this.getTarget() == null) {
            for (PlayerEntity player : list) {
                if (!player.isSpectator() && player.isHolding(FOOD_ITEMS) && squaredDistanceTo(player) < 2.0D) {
                    //this.setIsInterested(true);
                    this.getNavigation().stop();
                }
            }
        }

        if (this.isInterested() && (this.getTarget() == null)) {
            for (PlayerEntity player : list) {
                if (!player.isSpectator() && player.isHolding(FOOD_ITEMS) && squaredDistanceTo(player) > 2.0D) {
                    this.setIsInterested(false);
                }
            }
        }

        if (this.isInSleepingPose() || this.isImmobile()) {
            this.jumping = false;
            this.sidewaysSpeed = 0.0F;
            this.forwardSpeed = 0.0F;
        }
    }
    // endregion

    // region BREEDING / VARIANTS / MIXING

    public boolean canBreedWith(AnimalEntity animal) {
        if(animal==this){
            return false;
        }
        if (!this.isTamed()) {
            return false;
        } else if (!(animal instanceof Hamster hamster)) {
            return false;
        } else {
            if (!hamster.isTamed()) {
                return false;
            } else if (hamster.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && hamster.isInLove();
            }
        }
    }


    public Hamster.Variant getVariant() {
        return Hamster.Variant.BY_ID[this.dataTracker.get(DATA_VARIANT)];
    }

    public void setVariant(Hamster.Variant variant) {
        this.dataTracker.set(DATA_VARIANT, variant.getId());
    }


    public boolean fromHand() {
        return this.dataTracker.get(FROM_HAND);
    }

    public void setFromHand(boolean fromHand) {
        this.dataTracker.set(FROM_HAND, fromHand);
    }

    public enum Variant {
        //WHITE (2, "white"),
        //PEACHES_AND_CREAM (1, "peaches_and_cream"),
        //GREY_WHITE (3, "grey_white"),
        //BROWN (4, "brown"),
        //BLACK_WHITE (5, "black_white"),
        //BLACK (6, "black"),
        ORANGE (0, "orange");

        public static final Hamster.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final String name;

        private Variant(int j, String string2) {
            this.id = j;
            this.name = string2;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static Variant getTypeById(int id) {
            for (Variant type : values()) {
                if (type.id == id) return type;
            }
            return Variant.ORANGE;
        }
    }

    @Nullable

    // endregion


    // region PICK UP ITEMS

    @Override
    public boolean canEquip(ItemStack pItemstack) {
        EquipmentSlot slot = getPreferredEquipmentSlot(pItemstack);
        if (!this.getEquippedStack(slot).isEmpty()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canEquip(pItemstack);
        }
    }

    @Override
    protected void loot(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getStack();
        if (!this.isInSleepingPose()) {
            if (this.getMainHandStack().isEmpty() && FOOD_ITEMS.test(stack)) {
                this.triggerItemPickedUpByEntityCriteria(pItemEntity);
                this.setStackInHand(Hand.MAIN_HAND, stack);
                this.handDropChances[EquipmentSlot.MAINHAND.getEntitySlotId()] = 2.0F;
                this.sendPickup(pItemEntity, stack.getCount());
                pItemEntity.discard();
            }
        }
    }

    @Override
    public void applyDamage(DamageSource pSource, float pAmount) {
        if (!this.getMainHandStack().isEmpty() && !this.getWorld().isClient) {
            ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX() + this.getLookControl().getLookX(), this.getY() + 1.0D, this.getZ() + this.getLookControl().getLookZ(), this.getMainHandStack());
            itemEntity.setPickupDelay(40);
            itemEntity.setThrower(this);
            //this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            this.getWorld().spawnEntity(itemEntity);
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.applyDamage(pSource, pAmount);
    }

    // endregion
    /*@Override
    public void setYaw(float yaw){
        super.setYaw(yaw);
        this.setBodyYaw(yaw);
        this.setHeadYaw(yaw);
        this.serverYaw=yaw;
    }*/
    // region SPAWNING

    @Override
    public EntityData initialize(ServerWorldAccess pLevel, LocalDifficulty pDifficulty, SpawnReason pReason, @Nullable EntityData pSpawnData) {
        this.initEquipment(random, pDifficulty);
        if (pSpawnData == null) {
            Random randomSource = pLevel.getRandom();
            //this.setVariant(Variant.values()[randomSource.nextInt(Variant.values().length)]);
            this.setVariant(Variant.ORANGE);
        }
        return pSpawnData;
    }

    @Override
    public void onPlayerCollision(PlayerEntity entity) {
        super.onPlayerCollision(entity);
        // If the hamster is in the sleeping pose, wake it up on collision
        if (this.isInSleepingPose()) {
            this.wakeHamsterUp();
        }
    }

    @Override
    public void onDamaged(DamageSource source){
        super.onDamaged(source);
        if (this.isInSleepingPose()) {
            this.wakeHamsterUp();
        }
    }


    @Override
    protected void initEquipment(Random random, LocalDifficulty pDifficulty) {
        if (random.nextFloat() < 0.2F) {
            float chance = random.nextFloat();
            ItemStack stack;
            if (chance < 0.1F) {
                stack = new ItemStack(Items.BEETROOT_SEEDS);
            } else if (chance < 0.15F) {
                stack = new ItemStack(Items.PUMPKIN_SEEDS);
            } else if (chance < 0.3F) {
                stack = new ItemStack(Items.MELON_SEEDS);
            } else {
                stack = new ItemStack(Items.WHEAT_SEEDS);
            }

            this.setStackInHand(Hand.MAIN_HAND, stack);
        }
    }

    // endregion
    protected boolean canStartRiding(Entity entity) {
        boolean a=this.isSneaking();
        return !a && this.ridingCooldown <= 0;
    }

    // region GECKOLIB

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animController));
    }

    protected <E extends Hamster> PlayState animController(final AnimationState<E> event) {
        if (this.isInSleepingPose()) {
            event.setAnimation(SLEEP);
        } else if (this.isInterested() || this.isInSittingPose()) {
            event.setAnimation(STANDING);
        }  else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.setControllerSpeed(1.3F);
                event.setAnimation(RUN);
            } else {
                event.setControllerSpeed(1.1F);
                event.setAnimation(WALK);
            }
        }  else if (this.hasVehicle() && this.getVehicle() instanceof SeatEntity seat) {
            CopperWheelBlockEntity wheel= (CopperWheelBlockEntity) getWorld().getBlockEntity(seat.getBlockPos());
            if(wheel==null){
                event.setAnimation(IDLE);
                return PlayState.CONTINUE;
            }else{
                switch (wheel.getOxidationLevel()){
                    case UNAFFECTED:
                        event.setControllerSpeed(1.25F);
                        event.setAnimation(RUN);
                        break;
                    case EXPOSED:
                        event.setControllerSpeed(1F);
                        event.setAnimation(RUN);
                        break;
                    case WEATHERED:
                        event.setControllerSpeed(0.75F);
                        event.setAnimation(RUN);
                        break;
                    case OXIDIZED:
                        event.setControllerSpeed(0.5F);
                        event.setAnimation(RUN);
                        break;
                }
            }
        }else {
            event.setAnimation(IDLE);
        }

        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    // endregion

    // region GOALS

    public class HamsterLookControl extends LookControl {
        public HamsterLookControl() {
            super(Hamster.this);
        }

        public void tick() {
            if (!(Hamster.this.getVehicle() instanceof SeatEntity)) {
                super.tick();
            } else {
                BlockState state = Hamster.this.getWorld().getBlockState(Hamster.this.getBlockPos());
                if (state.getBlock() instanceof CopperWheel) {
                    BlockPos pos1;

                    if (state.get(FACING) == Direction.SOUTH) {
                        pos1 = Hamster.this.getBlockPos().south(1);
                    } else if (state.get(FACING) == Direction.NORTH) {
                        pos1 = Hamster.this.getBlockPos().north(1);
                    } else if (state.get(FACING) == Direction.EAST) {
                        pos1 = Hamster.this.getBlockPos().east(1);
                    } else {
                        pos1 = Hamster.this.getBlockPos().south(1);
                    }
                    Hamster.this.setInSleepingPose(false);
                    Hamster.this.setSitting(false);
                    Hamster.this.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, new Vec3d(pos1.getX() + 0.5f, pos1.getY() + 0.5f, pos1.getZ() + 0.5f));
                    if(Hamster.this.getYaw()%360!=Hamster.this.getVehicle().getYaw()%360) {
                        // Force synchronize the yaw of the hamster with the seat yaw
                        Hamster.this.setYaw(Hamster.this.getVehicle().getYaw());
                        Hamster.this.setBodyYaw(Hamster.this.getVehicle().getYaw());
                        Hamster.this.setHeadYaw(Hamster.this.getVehicle().getYaw());
                    }
                }
            }
        }

        protected boolean resetXRotOnTick() {
            return !(Hamster.this.getVehicle() instanceof SeatEntity);
        }
    }

        class HamsterGoToWheelGoal extends Goal {
        private final Predicate<BlockState> VALID_GATHERING_BLOCKS;
        @Nullable
        private Vec3d wheelPos;

        HamsterGoToWheelGoal() {
            this.VALID_GATHERING_BLOCKS = blockState -> {
                if (blockState.getBlock() instanceof CopperWheel) {
                    return !blockState.contains(Properties.WATERLOGGED) || !blockState.get(Properties.WATERLOGGED);
                }
                return false;
            };
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {

            Optional<BlockPos> optional = this.findNearbyResource();
            if (optional.isPresent() && !CopperWheel.isOccupied(Hamster.this.getWorld(), optional.get(),Hamster.this) && Hamster.this.getWaitTimeBeforeRunTicks() == 0) {
                if(!Hamster.this.getWorld().hasRain(optional.get().up()) && !Hamster.this.isInSleepingPose() && !Hamster.this.isInSittingPose() &&!CopperWheel.isPowered(Hamster.this.getWorld(),optional.get())){
                    Hamster.this.navigation.startMovingTo((double) optional.get().getX() + 0.5, optional.get().getY(), (double) optional.get().getZ() + 0.5, 1.2f);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean shouldContinue() {
            Optional<BlockPos> optional = this.findNearbyResource();
            if (optional.isPresent() && !CopperWheel.isOccupied(Hamster.this.getWorld(), optional.get()) && Hamster.this.getWaitTimeBeforeRunTicks() == 0) {
                return !Hamster.this.getWorld().hasRain(optional.get().up()) && !Hamster.this.isInSleepingPose() && !Hamster.this.isInSittingPose() &&!CopperWheel.isPowered(Hamster.this.getWorld(),optional.get());
            }
            return false;
        }

        @Override
        public void stop() {
            Hamster.this.navigation.stop();
            Hamster.this.setWaitTimeBeforeRunTicks(Hamster.this.random.nextInt(400) + 2000);
            Hamster.this.setIsRiding(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            Optional<BlockPos> optional = this.findNearbyResource();
            if (optional.isEmpty() || CopperWheel.isOccupied(Hamster.this.getWorld(), optional.get()) || CopperWheel.isPowered(Hamster.this.getWorld(), optional.get())) {
                stop();
            }

            if (optional.isPresent() && !CopperWheel.hasSeat(Hamster.this.getWorld(), optional.get()) && Hamster.this.getWaitTimeBeforeRunTicks() == 0) {

                Vec3d vec3 = Vec3d.ofBottomCenter(optional.get());
                if (vec3.distanceTo(Hamster.this.getPos()) > 1.4) {
                    wheelPos = vec3;
                    this.setWantedPos();
                    return;
                }
                if (wheelPos == null) {
                    this.wheelPos = vec3;
                }
                if (Hamster.this.getPos().distanceTo(this.wheelPos) <= 1.4&&!Hamster.this.isRiding()&&!Hamster.this.isInSleepingPose()&&!Hamster.this.getWorld().hasRain(optional.get().up())) {
                    /*Hamster.this.setWaitTimeWhenRunningTicks(Hamster.this.random.nextInt(300) + 100);*/
                    CopperWheel.sitDown(Hamster.this.getWorld(), optional.get(), Hamster.this);
                    Hamster.this.clearStates(true);
                    Hamster.this.setIsRiding(true);
                    /*if (Hamster.this.hasVehicle() && Hamster.this.getVehicle() instanceof SeatEntity seat) {
                        // Get the block the SeatEntity is attached to (which should be the Copper Wheel block)
                        BlockPos seatPos = optional.get();
                        BlockState blockState = Hamster.this.getWorld().getBlockState(seatPos);

                        // Check if the block is a Copper Wheel
                        if (blockState.getBlock() instanceof CopperWheel) {
                            // Get the facing direction of the Copper Wheel
                            Direction facing = blockState.get(CopperWheel.FACING);

                            // Set the hamster's yaw to face the Copper Wheel direction
                            switch (facing) {
                                case NORTH:
                                    Hamster.this.setYaw(180); // Face South
                                    break;
                                case SOUTH:
                                    Hamster.this.setYaw(0);   // Face North
                                    break;
                                case WEST:
                                    Hamster.this.setYaw(90);  // Face East
                                    break;
                                case EAST:
                                    Hamster.this.setYaw(270); // Face West
                                    break;
                            }
                            Hamster.this.setPitch(0);
                            // Optionally set the pitch if the hamster needs to look up/down (e.g., for ceiling-mounted wheels)
                            // this.setPitch(0); // Reset pitch if necessary
                        }
                    }*/
                    this.stop();
                }
            }
        }

        private void setWantedPos() {
            Hamster.this.getMoveControl().moveTo(this.wheelPos.x, this.wheelPos.y, this.wheelPos.z, 0.7f);
        }


        private Optional<BlockPos> findNearbyResource() {
            return this.findNearestBlock(this.VALID_GATHERING_BLOCKS);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate) {
            BlockPos blockPos = Hamster.this.getBlockPos();
            BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
            int i = 0;
            while ((double)i <= 5.0) {
                int j = 0;
                while ((double)j < 5.0) {
                    int k = 0;
                    while (k <= j) {
                        int l = k < j && k > -j ? j : 0;
                        while (l <= j) {
                            mutableBlockPos.set(blockPos, k, i - 1, l);
                            if (blockPos.isWithinDistance(mutableBlockPos, 5.0) && predicate.test(Hamster.this.getWorld().getBlockState(mutableBlockPos))) {
                                return Optional.of(mutableBlockPos);
                            }
                            l = l > 0 ? -l : 1 - l;
                        }
                        k = k > 0 ? -k : 1 - k;
                    }
                    ++j;
                }
                i = i > 0 ? -i : 1 - i;
            }
            return Optional.empty();
        }
    }

    private class RunInWheelGoal extends Goal {

        public RunInWheelGoal() {
            super();
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        }

        @Override
        public boolean canStart() {
            return !Hamster.this.isInSleepingPose() && !Hamster.this.inPowderSnow && (Hamster.this.hasVehicle() && Hamster.this.getVehicle() instanceof SeatEntity);

        }

        public boolean shouldContinue() {
            return (Hamster.this.hasVehicle() && Hamster.this.getVehicle() instanceof SeatEntity);
        }
    }

    private class SleepGoal extends Goal {
        private final TargetPredicate alertableTargeting = TargetPredicate.createNonAttackable().setBaseMaxDistance(6.0).ignoreDistanceScalingFactor().setPredicate(new HamsterAlertableEntitiesSelector());
        private final int WAIT_TIME_BEFORE_SLEEP = random.nextInt(100) + 100;
        private int WAIT_TIME_BEFORE_SOUND;
        private int countdown;

        public SleepGoal() {
            super();
            this.countdown = Hamster.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        }

        public boolean canStart() {
            if (Hamster.this.sidewaysSpeed == 0.0F && Hamster.this.upwardSpeed == 0.0F && Hamster.this.forwardSpeed == 0.0F&&(!isTamed()||(Hamster.this.getOwner()!=null&&Hamster.this.distanceTo(Hamster.this.getOwner())<10))) {
                return !Hamster.this.getWorld().hasRain(Hamster.this.getBlockPos())&&!Hamster.this.isInSittingPose()&& this.canSleep() || Hamster.this.isInSleepingPose();
            } else {
                return false;
            }
        }

        public boolean shouldContinue() {
            return !Hamster.this.getWorld().hasRain(Hamster.this.getBlockPos())&&Hamster.this.isInSleepingPose()&&this.canSleep();
        }
        public void tick(){
            if(WAIT_TIME_BEFORE_SOUND==countdown){
                Hamster.this.getWorld().playSound(null, Hamster.this.getX(), Hamster.this.getY(), Hamster.this.getZ(),
                        HamstersSoundEvents.HAMSTER_SLEEP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                WAIT_TIME_BEFORE_SOUND = random.nextInt(40) + 40;
            }else {
                WAIT_TIME_BEFORE_SOUND--;
            }
        }
        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return ((Hamster.this.getWorld().getTimeOfDay() >= 1000 && Hamster.this.getWorld().getTimeOfDay() <= 10000) || (Hamster.this.getWorld().getTimeOfDay() >= 16000 && Hamster.this.getWorld().getTimeOfDay() <= 21000)) && !Hamster.this.inPowderSnow && !this.alertable() && !Hamster.this.hasVehicle()&&(!isTamed()||(Hamster.this.getOwner()!=null&&Hamster.this.distanceTo(Hamster.this.getOwner())<10));
            }
        }

        public void stop() {
            this.countdown = Hamster.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            clearStates(false);
        }

        public void start() {
            Hamster.this.setSitting(false);
            Hamster.this.setIsInterested(false);
            Hamster.this.setJumping(false);
            Hamster.this.setInSleepingPose(true);
            Hamster.this.getNavigation().stop();
            Hamster.this.getMoveControl().moveTo(Hamster.this.getX(), Hamster.this.getY(), Hamster.this.getZ(), 0.0);
            Hamster.this.getWorld().playSound(null, Hamster.this.getX(), Hamster.this.getY(), Hamster.this.getZ(),
                    HamstersSoundEvents.HAMSTER_SLEEP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            WAIT_TIME_BEFORE_SOUND=random.nextInt(40) + 40;
        }


        protected boolean alertable() {
            return !Hamster.this.getWorld().getTargets(LivingEntity.class, this.alertableTargeting, Hamster.this, Hamster.this.getBoundingBox().expand(12.0, 6.0, 12.0)).isEmpty();
        }
    }

    public static class HamsterAlertableEntitiesSelector implements Predicate<LivingEntity> {
        public HamsterAlertableEntitiesSelector() {
        }

        public boolean test(LivingEntity livingEntity) {
            if (livingEntity instanceof Hamster) {
                return false;
            } else if (livingEntity instanceof PlayerEntity && (livingEntity.isSpectator() || ((PlayerEntity)livingEntity).isCreative())) {
                return false;
            } else {
                return !livingEntity.isSleeping() && !livingEntity.isSneaky();
            }
        }
    }
}

