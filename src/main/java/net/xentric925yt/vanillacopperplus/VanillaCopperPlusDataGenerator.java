package net.xentric925yt.vanillacopperplus;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.xentric925yt.vanillacopperplus.data.provider.*;

public class VanillaCopperPlusDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		//pack.addProvider(ModEnglishLanguageProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		//pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModRecipeProvider::new);
	}
}
