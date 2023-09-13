package com.ghasto.create_so;

import static com.ghasto.create_so.CreateSandpaperOverhaul.REGISTRATE;

public class ModCustomLangEntries {
    static {
        //Death Messages
        lang("death.attack.create_so.sandpaper", "%1$s was brutally scrubbed to death with a sandpaper");

        //Tooltips
        lang("block.create_so.polishing_wheel.tooltip", "POLISHING WHEEL");
        lang("block.create_so.polishing_wheel.tooltip.summary", "The _Polishing Wheel_ is a block similar to the _Crushing Wheel_ that can _Polish_ items in large amounts.");

		lang("item.create_so.obsidian_sandpaper.tooltip", "STURDY OBSIDIAN SANDPAPER");
		lang("item.create_so.obsidian_sandpaper.tooltip.summary", "A _very sturdy_ _sandpaper_. Can be used to _refine materials_. The process can be automated with a Deployer.");
		lang("item.create_so.obsidian_sandpaper.tooltip.condition1", "When Used");
		lang("item.create_so.obsidian_sandpaper.tooltip.behaviour1", "Applies polish to items held in the _offhand_ or lying on the _floor_ when _looking at them_");

		//Misc
		lang("item.create.powdered_obsidian", "Crushed Obsidian");
    }
    public static void lang(String key, String translation) {
        REGISTRATE.addRawLang(key, translation);
    }
    public static void register() {}
}
