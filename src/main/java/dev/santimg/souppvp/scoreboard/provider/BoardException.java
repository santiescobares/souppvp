package dev.santimg.souppvp.scoreboard.provider;

@SuppressWarnings("serial")
public class BoardException extends RuntimeException {

	/**
	 * Assemble Exception.
	 *
	 * @param message attributed to exception.
	 */
	public BoardException(String message) {
		super(message);
	}

}
