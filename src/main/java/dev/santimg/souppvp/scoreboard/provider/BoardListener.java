package dev.santimg.souppvp.scoreboard.provider;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.santimg.souppvp.scoreboard.provider.event.BoardCreateEvent;
import dev.santimg.souppvp.scoreboard.provider.event.BoardDestroyEvent;

@Getter
public class BoardListener implements Listener {

	private BoardProvider assemble;

	/**
	 * Assemble Listener.
	 *
	 * @param assemble instance.
	 */
	public BoardListener(BoardProvider assemble) {
		this.assemble = assemble;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		BoardCreateEvent createEvent = new BoardCreateEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(createEvent);
		if (createEvent.isCancelled()) {
			return;
		}

		getAssemble().getBoards().put(event.getPlayer().getUniqueId(), new Board(event.getPlayer(), getAssemble()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		BoardDestroyEvent destroyEvent = new BoardDestroyEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(destroyEvent);
		if (destroyEvent.isCancelled()) {
			return;
		}

		getAssemble().getBoards().remove(event.getPlayer().getUniqueId());
		if (Bukkit.getScoreboardManager().getMainScoreboard() != null)
			event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

}
