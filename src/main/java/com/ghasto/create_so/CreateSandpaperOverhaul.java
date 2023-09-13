package com.ghasto.create_so;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghasto.create_so.util.DamageTypeDataProvider;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CreateSandpaperOverhaul implements ModInitializer, DataGeneratorEntrypoint {
	public static final String ID = "create_so";
	public static final String NAME = "Create: Sandpaper Overhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);
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
		REGISTRATE.simple("tab", Registries.CREATIVE_MODE_TAB, () -> FabricItemGroup.builder()
				.title(Component.literal(NAME))
				.icon(ModItems.DIAMOND_SANDPAPER::asStack)
				.displayItems((c,p) -> {
					REGISTRATE.getAll(Registries.ITEM).forEach(e -> {
						p.accept(e.get());
					});
				})
				.build());
		REGISTRATE.register();
		ModCustomLangEntries.register();
		LOGGER.info("{} is loading alongside Create {}!", NAME, Create.VERSION);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
		REGISTRATE.setupDatagen(pack, helper);
		pack.addProvider(DamageTypeDataProvider::new);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}

