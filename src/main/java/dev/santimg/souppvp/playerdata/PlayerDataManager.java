package dev.santimg.souppvp.playerdata;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.common.collect.Maps;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.playerdata.repository.MySQLPlayerDataRepository;
import dev.santimg.souppvp.playerdata.repository.PlayerDataRepository;
import dev.santimg.souppvp.utilities.PlayerUtil;
import dev.santimg.souppvp.utilities.TaskUtil;
import lombok.Getter;

public class PlayerDataManager {

	@Getter
	private final Map<UUID, PlayerData> loadedDataMap;

	/**
	 * {@link PlayerDataRepository} will allow us to load-save players' data using
	 * our implemented database in {@link DatabaseManager}
	 */
	@Getter
	private final PlayerDataRepository repository;

	private final long AUTO_SAVE_INTERVAL = 300L * 20L; // In ticks (5 minutes)

	public PlayerDataManager() {
		// We will be adding and removing objects from the map multiple times, so It's
		// better to use a LinkedHashMap instead of a normal HashMap
		this.loadedDataMap = Maps.newLinkedHashMap();

		// We are only using a MySQL storage so obviously, we are going to use the MySQL
		// data repository
		this.repository = new MySQLPlayerDataRepository();

		Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), SoupPvP.getInstance());

		// Automatic data save every 5 minutes
		TaskUtil.runAsyncTimer(() -> this.saveAll(), this.AUTO_SAVE_INTERVAL, this.AUTO_SAVE_INTERVAL);
	}

	/**
	 * Save all loaded players' data
	 */
	public void saveAll() {
		long start = System.currentTimeMillis();
		int size = this.loadedDataMap.size();

		this.loadedDataMap.values().forEach(repository::save);

		long end = System.currentTimeMillis();

		System.out.println(size + " players' data were saved in " + (end - start) + "ms.");
	}

	/**
	 * Get a player's data
	 * 
	 * @param uuid - the player's uuid
	 * @return - the player data object or null if not loaded
	 */
	public PlayerData get(UUID uuid) {
		return this.loadedDataMap.get(uuid);
	}

	/**
	 * Get a player's data
	 * 
	 * @param name - the player's name
	 * @return - the player data object or null if not loaded
	 */
	public PlayerData get(String name) {
		return this.get(PlayerUtil.uuid(name));
	}

	/**
	 * On plugin disable class logic
	 */
	public void onDisable() {
		this.saveAll();
		this.loadedDataMap.clear();
	}
}
