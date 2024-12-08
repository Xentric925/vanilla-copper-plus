package net.xentric925yt.vanillacopperplus.list;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FoodList {
    public static final FoodComponent LETTUCE_COMPONENT = new FoodComponent
            .Builder()
            .nutrition(1)
            .saturationModifier(0.1f)
            .build();


}
