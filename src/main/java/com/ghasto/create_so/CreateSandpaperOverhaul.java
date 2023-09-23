package com.ghasto.create_so;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CreateSandpaperOverhaul implements ModInitializer, DataGeneratorEntrypoint {
	public static final String ID = "create_so";
	public static final String NAME = "Create: Sandpaper Overhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreativeModeTab TAB = FabricItemGroupBuilder.create(id("tab"))
			.icon(() -> ModItems.DIAMOND_SANDPAPER.get().getDefaultInstance())
			.build();
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID).creativeModeTab(() -> TAB);
	static {
		REGISTRATE.setTooltipModifierFactory(item -> {
			return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
					.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
		});
	}

	@Override
	public void onInitialize() {
		ModItems.register();
		ModBlocks.register();
		ModBlockEntities.register();
		ModRecipeTypes.register();
		REGISTRATE.register();
		ModCustomLangEntries.register();
		LOGGER.info("{} is loading alongside Create {}!", NAME, Create.VERSION);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
		REGISTRATE.setupDatagen(fabricDataGenerator, helper);
//		fabricDataGenerator.addProvider(DamageTypeDataProvider::new);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}

