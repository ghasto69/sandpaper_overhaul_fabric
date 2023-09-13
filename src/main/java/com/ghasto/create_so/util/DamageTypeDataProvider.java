package com.ghasto.create_so.util;


import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import com.ghasto.create_so.CreateSandpaperOverhaul;
import com.ghasto.create_so.ModDamageTypes;

import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public class DamageTypeDataProvider extends DatapackBuiltinEntriesProvider {
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);

	public DamageTypeDataProvider(PackOutput output, CompletableFuture<Provider> registries) {
		super(output, registries, BUILDER, Set.of(CreateSandpaperOverhaul.ID));
	}

	public static DataProvider.Factory<DamageTypeDataProvider> makeFactory(CompletableFuture<Provider> registries) {
		return output -> new DamageTypeDataProvider(output, registries);
	}

	@Override
	@NotNull
	public String getName() {
		return "Create Sandpaper Overhaul's Damage Type Data";
	}
}
