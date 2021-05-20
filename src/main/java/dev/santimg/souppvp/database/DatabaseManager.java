package dev.santimg.souppvp.database;

import dev.santimg.souppvp.database.impl.MySQLDatabase;
import lombok.Getter;

/**
 * The objective of the {@link Database} implementation is allow you to add as
 * much databases types as you want, in this case I'm only implementing MySQL.
 * 
 * @author SantiMG
 */
public class DatabaseManager {

	@Getter
	private final Database database;

	public DatabaseManager() {
		this.database = new MySQLDatabase();

		this.database.connect();
	}

	/**
	 * Close the created database connection
	 */
	public void close() {
		if (this.database != null) {
			this.database.close();
		}
	}
}
