package dev.santimg.souppvp.playerdata.repository;

import java.util.UUID;

import dev.santimg.souppvp.playerdata.PlayerData;

public interface PlayerDataRepository {

	PlayerData load(UUID uuid);

	void save(PlayerData playerData);
}
