package net.xentric925yt.vanillacopperplus.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;
import net.xentric925yt.vanillacopperplus.init.Entities.Hamster;

import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.xentric925yt.vanillacopperplus.init.Entities.SeatEntity;
import net.xentric925yt.vanillacopperplus.list.HamstersTags;

import java.util.function.Supplier;

public class EntityInit {

    public static final EntityType<Hamster> HAMSTER = register(
            "hamster",
            EntityType.Builder
            .create(Hamster::new, SpawnGroup.CREATURE)
            .dimensions(0.5F, 0.5F)  // Sets the dimensions (width and height)
            .maxTrackingRange(10)  // Sets the tracking range in chunks
            .alwaysUpdateVelocity(true)  // Ensures it receives velocity updates
            .trackingTickInterval(1)  // Sets the update interval (ticks between updates)
            );
    public static final Supplier<EntityType<SeatEntity>> SEAT = registerEntityType("seat", (type, world) -> new SeatEntity(world), SpawnGroup.MISC, 0.0F, 0.0F);

    static {
        SpawnRestriction.register(HAMSTER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.WORLD_SURFACE_WG, AnimalEntity::isValidNaturalSpawn);
    }

    public static void load() {
        FabricDefaultAttributeRegistry.register(HAMSTER, Hamster.createAttributes());
    }


    static {
        BiomeModifications.addSpawn(BiomeSelectors.tag(HamstersTags.HAS_HAMSTER), SpawnGroup.CREATURE, HAMSTER, 30, 1, 1);
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id(id), type.build(id(id).toString()));
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, SpawnGroup category, float width, float height) {
        var registry = Registry.register(Registries.ENTITY_TYPE, id(name), EntityType.Builder.create(factory,category).dimensions(width, height).build());
        return () -> registry;
    }

}

