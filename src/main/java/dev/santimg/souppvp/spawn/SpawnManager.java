package dev.santimg.souppvp.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.spawn.command.*;
import dev.santimg.souppvp.utilities.BukkitUtil;
import lombok.Getter;

public class SpawnManager {

	@Getter
	private Location spawn = null;

	public SpawnManager() {
		FileConfiguration utilitiesFile = SoupPvP.getInstance().getUtilitiesFile().getConfiguration();

		if (utilitiesFile.contains("SPAWN")) {
			this.spawn = BukkitUtil.deserializeLocation(utilitiesFile.getString("SPAWN"));
		}

		Bukkit.getPluginManager().registerEvents(new SpawnListener(), SoupPvP.getInstance());

		SoupPvP.getInstance().getCommandFramework().registerCommands(new SetSpawnCommand());
		SoupPvP.getInstance().getCommandFramework().registerCommands(new SpawnCommand());
	}

	/**
	 * Save a new spawn location in config.yml
	 * 
	 * @param newSpawn - the location
	 */
	public void saveSpawn(Location newSpawn) {
		this.spawn = newSpawn;

		SoupPvP.getInstance().getUtilitiesFile().getConfiguration().set("SPAWN", BukkitUtil.serializeLocation(newSpawn));
		SoupPvP.getInstance().getUtilitiesFile().save();
	}

	/**
	 * Teleport a player to the spawn
	 * 
	 * @param player - the player
	 * @return - true if the spawn is defined and could teleport or false if not
	 */
	public boolean teleportToSpawn(Player player) {
		if (this.spawn == null) {
			return false;
		}

		player.teleport(this.spawn);
		player.getInventory().setHeldItemSlot(0);

		return true;
	}
}
