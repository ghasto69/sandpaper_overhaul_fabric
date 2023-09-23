package com.ghasto.create_so;

import static com.ghasto.create_so.CreateSandpaperOverhaul.REGISTRATE;

public class ModCustomLangEntries {
    static {
        //Death Messages
        lang("death.attack.create_so.sandpaper", "%1$s was brutally scrubbed to death with a sandpaper");
		lang("death.attack.create_so.polish", "%1$s curiously walked into a pair of polishing wheels, it didn't end well");

        //Tooltips
        lang("block.create_so.polishing_wheel.tooltip", "POLISHING WHEEL");
        lang("block.create_so.polishing_wheel.tooltip.summary", "The _Polishing Wheel_ is a block similar to the _Crushing Wheel_ that can _Polish_ items in large amounts.");

		lang("item.create_so.obsidian_sandpaper.tooltip", "STURDY OBSIDIAN SANDPAPER");
		lang("item.create_so.obsidian_sandpaper.tooltip.summary", "A _very sturdy_ _sandpaper_. Can be used to _refine materials_ or _polish blocks_. The process can be automated with a Deployer.");
		lang("item.create_so.obsidian_sandpaper.tooltip.condition1", "When used on an item");
		lang("item.create_so.obsidian_sandpaper.tooltip.behaviour1", "Applies polish to items held in the _offhand_ or lying on the _floor_ when _looking at them_");
		lang("item.create_so.obsidian_sandpaper.tooltip.condition2", "When used on a block");
		lang("item.create_so.obsidian_sandpaper.tooltip.behaviour2","Directly polishes the _block you are looking at_");

		lang("item.create.sand_paper.tooltip", "SAND PAPER");
		lang("item.create.sand_paper.tooltip.summary", "Can be used to _refine materials_ and _polish blocks_. The process can be automated with a Deployer.");
		lang("item.create.sand_paper.tooltip.condition1", "When used on an item");
		lang("item.create.sand_paper.tooltip.behaviour1", "Polishes items held in the _offhand_ or lying on the _floor_ when _looking at them_");
		lang("item.create.sand_paper.tooltip.condition2", "When used on a block");
		lang("item.create.sand_paper.tooltip.behaviour2","Directly polishes the _block you are looking at_");

        //Emi
        lang("emi.category.create_so.polishing", "Polishing Wheels");
		//Rei
		lang("rei.category.create_so.polishing", "Polishing Wheels");

		//Misc
		lang("item.create.powdered_obsidian", "Crushed Obsidian");
		lang("itemGroup.create_so.tab", "Create: Sandpaper Overhaul");
    }
    public static void lang(String key, String translation) {
        REGISTRATE.addRawLang(key, translation);
    }
    public static void register() {}
}
