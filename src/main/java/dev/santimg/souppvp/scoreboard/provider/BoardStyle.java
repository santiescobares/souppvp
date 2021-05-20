package dev.santimg.souppvp.scoreboard.provider;

import lombok.Getter;

@Getter
public enum BoardStyle {

	KOHI(true, 15), VIPER(true, -1), MODERN(false, 1);

	private boolean descending;
	private int startNumber;

	/**
	 * Assemble Style.
	 *
	 * @param descending  whether the positions are going down or up.
	 * @param startNumber from where to loop from.
	 */
	BoardStyle(boolean descending, int startNumber) {
		this.descending = descending;
		this.startNumber = startNumber;
	}

}
