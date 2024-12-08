package net.xentric925yt.vanillacopperplus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.xentric925yt.vanillacopperplus.init.*;
import net.xentric925yt.vanillacopperplus.list.HamstersSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanillaCopperPlus implements ModInitializer {
	public static final String MOD_ID = "vanilla-copper-plus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize()
	{
		LOGGER.info("Vanilla Copper Plus has been initialized!");
		ItemInit.load();
		BlockInit.load();
		ItemGroupInit.load();
		BlockEntityInit.load();
		EntityInit.load();
		HamstersSoundEvents.load();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
			entries.addAfter(Items.MELON_SLICE, ItemInit.LETTUCE);
			entries.addBefore(Items.HONEY_BOTTLE,ItemInit.VINEGAR);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
			entries.addBefore(Items.LEVER,BlockInit.COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.EXPOSED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.WEATHERED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.OXIDIZED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_EXPOSED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_WEATHERED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_OXIDIZED_COPPER_GENERATOR);
			entries.addBefore(Items.LEVER,BlockInit.COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.EXPOSED_COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.WEATHERED_COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.OXIDIZED_COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_EXPOSED_COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_WEATHERED_COPPER_WHEEL);
			entries.addBefore(Items.LEVER,BlockInit.WAXED_OXIDIZED_COPPER_WHEEL);

		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
			entries.addBefore(Items.GLOWSTONE,BlockInit.COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.EXPOSED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WEATHERED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.OXIDIZED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_EXPOSED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_WEATHERED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_OXIDIZED_COPPER_GENERATOR);
			entries.addBefore(Items.GLOWSTONE,BlockInit.COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.EXPOSED_COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WEATHERED_COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.OXIDIZED_COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_EXPOSED_COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_WEATHERED_COPPER_WHEEL);
			entries.addBefore(Items.GLOWSTONE,BlockInit.WAXED_OXIDIZED_COPPER_WHEEL);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
			entries.addAfter(Items.FOX_SPAWN_EGG,ItemInit.HAMSTER_SPAWN_EGG);
			entries.addAfter(Items.CHISELED_COPPER,BlockInit.COPPER_GENERATOR);
			entries.addAfter(Items.EXPOSED_CHISELED_COPPER,BlockInit.EXPOSED_COPPER_GENERATOR);
			entries.addAfter(Items.WEATHERED_CHISELED_COPPER,BlockInit.WEATHERED_COPPER_GENERATOR);
			entries.addAfter(Items.OXIDIZED_CHISELED_COPPER,BlockInit.OXIDIZED_COPPER_GENERATOR);
			entries.addAfter(Items.WAXED_CHISELED_COPPER,BlockInit.WAXED_COPPER_GENERATOR);
			entries.addAfter(Items.WAXED_EXPOSED_CHISELED_COPPER,BlockInit.WAXED_EXPOSED_COPPER_GENERATOR);
			entries.addAfter(Items.WAXED_WEATHERED_CHISELED_COPPER,BlockInit.WAXED_WEATHERED_COPPER_GENERATOR);
			entries.addAfter(Items.WAXED_OXIDIZED_CHISELED_COPPER,BlockInit.WAXED_OXIDIZED_COPPER_GENERATOR);
			entries.addAfter(Items.COPPER_TRAPDOOR,BlockInit.COPPER_WHEEL);
			entries.addAfter(Items.EXPOSED_COPPER_TRAPDOOR,BlockInit.EXPOSED_COPPER_WHEEL);
			entries.addAfter(Items.WEATHERED_COPPER_TRAPDOOR,BlockInit.WEATHERED_COPPER_WHEEL);
			entries.addAfter(Items.OXIDIZED_COPPER_TRAPDOOR,BlockInit.OXIDIZED_COPPER_WHEEL);
			entries.addAfter(Items.WAXED_COPPER_TRAPDOOR,BlockInit.WAXED_COPPER_WHEEL);
			entries.addAfter(Items.WAXED_EXPOSED_COPPER_TRAPDOOR,BlockInit.WAXED_EXPOSED_COPPER_WHEEL);
			entries.addAfter(Items.WAXED_WEATHERED_COPPER_TRAPDOOR,BlockInit.WAXED_WEATHERED_COPPER_WHEEL);
			entries.addAfter(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR,BlockInit.WAXED_OXIDIZED_COPPER_WHEEL);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> content.addAfter(Items.FOX_SPAWN_EGG,ItemInit.HAMSTER_SPAWN_EGG));
		LootTableEvents.MODIFY.register((lootTableRegistryKey, tableBuilder, lootTableSource, wrapperLookup) -> {
			// Define identifiers for the target loot tables
			Identifier simpleDungeon = Identifier.of("minecraft", "chests/simple_dungeon");
			Identifier villageHouse = Identifier.of("minecraft", "chests/village/village_plains_house");
			Identifier villageSavanaHouse = Identifier.of("minecraft", "chests/village/village_savanna_house");
			Identifier villageSnowyHouse = Identifier.of("minecraft", "chests/village/village_snowy_house");

			// Check if the loot table matches any of the targets
			if (lootTableRegistryKey.equals(RegistryKey.of(RegistryKeys.LOOT_TABLE, simpleDungeon)) ||
					lootTableRegistryKey.equals(RegistryKey.of(RegistryKeys.LOOT_TABLE, villageHouse)) ||
					lootTableRegistryKey.equals(RegistryKey.of(RegistryKeys.LOOT_TABLE, villageSavanaHouse)) ||
					lootTableRegistryKey.equals(RegistryKey.of(RegistryKeys.LOOT_TABLE, villageSnowyHouse))
			) {

				// Add a pool with a chance for lettuce in specified chests
				LootPool pool = LootPool.builder()
						.rolls(UniformLootNumberProvider.create(1, 2))
						.with(ItemEntry.builder(ItemInit.LETTUCE).weight(3))
						.build();

				tableBuilder.pool(pool);
			}
		});
	}
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}