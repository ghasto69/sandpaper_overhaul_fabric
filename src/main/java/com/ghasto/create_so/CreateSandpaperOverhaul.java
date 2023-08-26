package com.ghasto.create_so;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class CreateSandpaperOverhaul implements ModInitializer{
	public static final String ID = "create_so";
	public static final String NAME = "Create: Sandpaper Overhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);
	public static final CreativeModeTab TAB = FabricItemGroup.builder()
			.title(Component.literal(NAME))
			.icon(AllItems.RED_SAND_PAPER::asStack)
			.displayItems((c,p) -> {
				REGISTRATE.getAll(Registries.ITEM).forEach(e -> {
					p.accept(e.get());
				});
			})
			.build();

	@Override
	public void onInitialize() {
		ModItems.register();
		ModBlocks.register();
		ModBlockEntities.register();
		ModRecipeTypes.register();
		REGISTRATE.simple("tab", Registries.CREATIVE_MODE_TAB, () -> TAB);
		REGISTRATE.register();
		LOGGER.info("{} is loading alongside Create {}!", NAME, Create.VERSION);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}
