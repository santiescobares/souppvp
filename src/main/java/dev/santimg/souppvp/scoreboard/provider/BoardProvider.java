package dev.santimg.souppvp.scoreboard.provider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import dev.santimg.souppvp.scoreboard.provider.event.BoardCreateEvent;

@Getter
@Setter
public class BoardProvider {

	private JavaPlugin plugin;

	private BoardAdapter adapter;
	private BoardThread thread;
	private BoardListener listeners;
	private BoardStyle assembleStyle = BoardStyle.MODERN;

	private Map<UUID, Board> boards;

	private long ticks;
	private boolean hook = false, debugMode = true;

	/**
	 * Assemble.
	 *
	 * @param plugin  instance.
	 * @param adapter
	 */
	public BoardProvider(JavaPlugin plugin, BoardAdapter adapter, long ticks) {
		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.assembleStyle = BoardStyle.MODERN;
		this.ticks = ticks;

		this.setup();
	}

	/**
	 * Setup Assemble.
	 */
	@SuppressWarnings("deprecation")
	public void setup() {
		// Register Events.
		this.listeners = new BoardListener(this);
		this.plugin.getServer().getPluginManager().registerEvents(listeners, this.plugin);

		// Ensure that the thread has stopped running.
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		// Register new boards for existing online players.
		for (Player player : Bukkit.getOnlinePlayers()) {
			// Make sure it doesn't double up.
			BoardCreateEvent createEvent = new BoardCreateEvent(player);

			Bukkit.getPluginManager().callEvent(createEvent);
			if (createEvent.isCancelled()) {
				return;
			}

			getBoards().putIfAbsent(player.getUniqueId(), new Board(player, this));
		}

		// Start Thread.
		this.thread = new BoardThread(this);
	}

	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public void onDisable() {
		// Stop thread.
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		// Unregister listeners.
		if (listeners != null) {
			HandlerList.unregisterAll(listeners);
			listeners = null;
		}

		// Destroy player scoreboards.
		for (UUID uuid : getBoards().keySet()) {
			Player player = Bukkit.getPlayer(uuid);

			if (player == null || !player.isOnline()) {
				continue;
			}

			getBoards().remove(uuid);
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}

}
