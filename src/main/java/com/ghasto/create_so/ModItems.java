package com.ghasto.create_so;

import static com.ghasto.create_so.CreateSandpaperOverhaul.REGISTRATE;
import static com.simibubi.create.AllItems.SAND_PAPER;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItemRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final ItemEntry<SandPaperItem> IRON_SANDPAPER =
            REGISTRATE.item("iron_sandpaper", SandPaperItem::new)
                    .lang("Crushed Iron Sandpaper")
                    .properties(p -> p
                            .durability(128)
                    )
                    .recipe((c, p) -> {
						ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, c.getEntry())
								.requires(AllItems.IRON_SHEET)
								.requires(ModItems.CRUSHED_IRON)
								.unlockedBy("has_iron_sheet", RegistrateRecipeProvider.has(AllItems.IRON_SHEET))
								.save(p);
                    })
					.tag(AllTags.AllItemTags.SANDPAPER.tag)
					.transform(CreateRegistrate.customRenderedItem(() -> SandPaperItemRenderer::new))
					.onRegister(s -> ItemDescription.referKey(s, SAND_PAPER))
                    .register();
	public static final ItemEntry<SandPaperItem> DIAMOND_SANDPAPER =
			REGISTRATE.item("diamond_sandpaper", SandPaperItem::new)
					.lang("Crushed Diamond Sandpaper")
					.properties(p -> p
							.durability(256)
					)
					.recipe((c, p) -> {
						ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, c.getEntry())
								.requires(AllItems.IRON_SHEET)
								.requires(ModItems.CRUSHED_DIAMONDS)
								.unlockedBy("has_iron_sheet", RegistrateRecipeProvider.has(AllItems.IRON_SHEET))
								.save(p);
					})
					.tag(AllTags.AllItemTags.SANDPAPER.tag)
					.transform(CreateRegistrate.customRenderedItem(() -> SandPaperItemRenderer::new))
					.onRegister(s -> ItemDescription.referKey(s, SAND_PAPER))
					.register();
	public static final ItemEntry<SandPaperItem> OBSIDIAN_SANDPAPER =
			REGISTRATE.item("obsidian_sandpaper", SandPaperItem::new)
					.lang("Sturdy Obsidian Sandpaper")
					.properties(p -> p
							.durability(512)
					)
					.recipe((c, p) -> {
						ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, c.getEntry())
								.requires(AllItems.STURDY_SHEET)
								.requires(AllItems.POWDERED_OBSIDIAN)
								.unlockedBy("has_sturdy_sheet", RegistrateRecipeProvider.has(AllItems.STURDY_SHEET))
								.save(p);
					})
					.tag(AllTags.AllItemTags.SANDPAPER.tag)
					.transform(CreateRegistrate.customRenderedItem(() -> SandPaperItemRenderer::new))
					.register();

	public static final ItemEntry<Item> CRUSHED_IRON =
			REGISTRATE.item("crushed_iron", Item::new)
					.properties(p -> p.stacksTo(16))
					.register();
	public static final ItemEntry<Item> CRUSHED_DIAMONDS =
			REGISTRATE.item("crushed_diamonds", Item::new)
					.properties(p -> p.stacksTo(16))
					.register();
	public static final ItemEntry<Item> UNREFINED_LAPIS_LAZULI =
			REGISTRATE.item("unrefined_lapis_lazuli", Item::new)
					.register();
    public static void register() {}
}
