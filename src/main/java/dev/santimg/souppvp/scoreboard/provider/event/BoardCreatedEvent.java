package dev.santimg.souppvp.scoreboard.provider.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.santimg.souppvp.scoreboard.provider.Board;

@Getter
@Setter
public class BoardCreatedEvent extends Event {

	@Getter
	public static HandlerList handlerList = new HandlerList();

	private boolean cancelled = false;
	private final Board board;

	/**
	 * Assemble Board Created Event.
	 *
	 * @param board of player.
	 */
	public BoardCreatedEvent(Board board) {
		this.board = board;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
