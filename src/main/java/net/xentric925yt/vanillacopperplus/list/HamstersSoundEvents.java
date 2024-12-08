package net.xentric925yt.vanillacopperplus.list;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import static net.xentric925yt.vanillacopperplus.VanillaCopperPlus.id;

public interface HamstersSoundEvents {



    SoundEvent HAMSTER_AMBIENT = register("entity.hamster.ambient");
    SoundEvent HAMSTER_HURT = register("entity.hamster.hurt");
    SoundEvent HAMSTER_DEATH = register("entity.hamster.death");
    SoundEvent HAMSTER_BEG = register("entity.hamster.beg");
    SoundEvent HAMSTER_SLEEP = register("entity.hamster.sleep");


    private static BlockSoundGroup register(String name, float volume, float pitch) {
        return new BlockSoundGroup(volume, pitch, register("block." + name + ".break"), register("block." + name + ".step"), register("block." + name + ".place"), register("block." + name + ".hit"), register("block." + name + ".fall"));
    }
    public static void load(){

    }

    static SoundEvent register(String name) {
        Identifier id = id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
