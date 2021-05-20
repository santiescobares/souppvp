package dev.santimg.souppvp.playerdata.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.database.impl.MySQLDatabase;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.playerdata.PlayerData;

public class MySQLPlayerDataRepository implements PlayerDataRepository {

	private final MySQLDatabase database;

	public static final String DATA_TABLE = "souppvp_player_data";
	public static final String KITS_TABLE = "souppvp_player_kits";

	public MySQLPlayerDataRepository() {
		this.database = (MySQLDatabase) SoupPvP.getInstance().getDatabaseManager().getDatabase();

		// Creating the table where we will save all players' data
		this.database.runStatement(
				"CREATE TABLE IF NOT EXISTS " + DATA_TABLE + "(uuid VARCHAR(40), kills INT, deaths INT, killstreak INT, balance LONG) ENGINE = InnoDB CHARACTER SET utf8;");

		// Creating the table where we will save all unlocked players' kits
		this.database.runStatement("CREATE TABLE IF NOT EXISTS " + KITS_TABLE + "(uuid VARCHAR(40), kit VARCHAR(64)) ENGINE = InnoDB CHARACTER SET utf8;");
	}

	@Override
	public PlayerData load(UUID uuid) {
		PlayerData playerData = new PlayerData(uuid);

		// We check if the player's data was saved some time
		try {
			PreparedStatement statement = this.database.prepareStatementAndGet("SELECT * FROM " + DATA_TABLE + " WHERE uuid=?", uuid.toString());
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				playerData.setKills(result.getInt("kills"));
				playerData.setDeaths(result.getInt("deaths"));
				playerData.setKillstreak(result.getInt("killstreak"));

				playerData.setBalance(result.getLong("balance"));
			}

			// It's important to close every MySQL statement after its use to prevent memory
			// leaks
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			PreparedStatement statement = this.database.prepareStatementAndGet("SELECT * FROM " + KITS_TABLE + " WHERE uuid=?", uuid.toString());
			ResultSet result = statement.executeQuery();

			KitManager kitManager = SoupPvP.getInstance().getKitManager();

			while (result.next()) {
				Kit kit = kitManager.get(result.getString("kit"));

				// We will not load unexisting kits
				if (kit != null) {
					playerData.getUnlockedKits().add(kit);
				}
			}

			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// We instantly update the player's kdr after loading kills and deaths
		playerData.updateKDR();

		return playerData;
	}

	@Override
	public void save(PlayerData playerData) {
		boolean exists = false;

		// We check if the player's data was already saved at least one time or we have
		// to insert a new one
		try {
			PreparedStatement statement = this.database.prepareStatementAndGet("SELECT * FROM " + DATA_TABLE + " WHERE uuid=?", playerData.getUuid().toString());
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				exists = true;
			}

			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!exists) {
			// We will insert the data

			// Note: I'm not using async statements because the data saving will be
			// already asynchronous
			this.database.runStatement("INSERT INTO " + DATA_TABLE + " VALUE (?,?,?,?,?)", playerData.getUuid().toString(), playerData.getKills(), playerData.getDeaths(),
					playerData.getKillstreak(), playerData.getBalance());
		} else {
			// We will override the data

			this.database.runStatement("UPDATE " + DATA_TABLE + " SET kills=?, deaths=?, killstreak=?, balance=? WHERE uuid=?", playerData.getKills(), playerData.getDeaths(),
					playerData.getKillstreak(), playerData.getBalance(), playerData.getUuid().toString());
		}

		// We will re-insert all unlocked kits data
		this.database.runStatement("DELETE FROM " + KITS_TABLE + " WHERE uuid=?", playerData.getUuid().toString());

		playerData.getUnlockedKits().forEach(kit -> {
			this.database.runStatement("INSERT INTO " + KITS_TABLE + " VALUE (?,?)", playerData.getUuid().toString(), kit.getName());
		});
	}
}
