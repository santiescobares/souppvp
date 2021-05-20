package dev.santimg.souppvp.scoreboard.provider.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class BoardCreateEvent extends Event implements Cancellable {

	@Getter
	public static HandlerList handlerList = new HandlerList();

	private Player player;
	private boolean cancelled = false;

	/**
	 * Assemble Board Create Event.
	 *
	 * @param player that the board is being created for.
	 */
	public BoardCreateEvent(Player player) {
		this.player = player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
