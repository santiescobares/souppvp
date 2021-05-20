package dev.santimg.souppvp.kit;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Sets;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.command.*;
import dev.santimg.souppvp.kit.command.manager.*;
import dev.santimg.souppvp.utilities.BukkitUtil;
import lombok.Getter;

public class KitManager {

	@Getter
	private final Set<Kit> kits;

	private final FileConfiguration FILE = SoupPvP.getInstance().getKitsFile().getConfiguration();

	public KitManager() {
		this.kits = Sets.newHashSet();

		this.load();

		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new BuyKitCommand());

		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerCreateCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerDeleteCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerSetPriceCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerSetDefaultCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerSetContentsCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new KitManagerSaveAllCommand());
	}

	/**
	 * Load all kits from kits.yml
	 */
	public void load() {
		// We will use this method to re-load kits aswell, so we have to clear the list
		// before loading all again
		this.kits.clear();

		if (!this.FILE.contains("KITS")) {
			return;
		}

		this.FILE.getConfigurationSection("KITS").getKeys(false).forEach(key -> {
			Kit kit = new Kit(key);

			kit.setPrice(this.FILE.getLong("KITS." + key + ".PRICE"));
			kit.setDefault(this.FILE.getBoolean("KITS." + key + ".DEFAULT"));
			kit.setContents(BukkitUtil.deserializeItemStackArray(this.FILE.getString("KITS." + key + ".CONTENTS")));
			kit.setArmorContents(BukkitUtil.deserializeItemStackArray(this.FILE.getString("KITS." + key + ".ARMOR_CONTENTS")));

			this.kits.add(kit);
		});
	}

	/**
	 * Save all kits in kits.yml
	 */
	public void save() {
		// Clear past saved kits
		this.FILE.set("KITS", null);

		this.kits.forEach(kit -> {
			String name = kit.getName();

			this.FILE.set("KITS." + name + ".PRICE", kit.getPrice());
			this.FILE.set("KITS." + name + ".DEFAULT", kit.isDefault());
			this.FILE.set("KITS." + name + ".CONTENTS", BukkitUtil.serializeItemStackArray(kit.getContents()));
			this.FILE.set("KITS." + name + ".ARMOR_CONTENTS", BukkitUtil.serializeItemStackArray(kit.getArmorContents()));
		});

		SoupPvP.getInstance().getKitsFile().save();
	}

	/**
	 * Get a kit
	 * 
	 * @param name - the kit's name
	 * @return - the kit object or null if it doesn't exist
	 */
	public Kit get(String name) {
		return this.kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	/**
	 * Get the first kit defined as default
	 * 
	 * @return - the kit object or null if there is no default kit
	 */
	public Kit getDefault() {
		return this.kits.stream().filter(Kit::isDefault).findFirst().orElse(null);
	}

	/**
	 * On plugin disable class logic
	 */
	public void onDisable() {
		this.save();
		this.kits.clear();
	}
}
