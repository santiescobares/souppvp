package dev.santimg.souppvp.playerdata;

import java.util.Set;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import dev.santimg.souppvp.kit.Kit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerData {

	private final UUID uuid;

	private int kills;
	private int deaths;
	private int killstreak;
	private double kdr = 0.0D;

	private long balance;

	private final Set<Kit> unlockedKits;

	public PlayerData(UUID uuid) {
		Preconditions.checkNotNull(uuid, "UUID can't be null.");

		this.uuid = uuid;

		this.kills = 0;
		this.deaths = 0;
		this.killstreak = 0;

		this.balance = 0L;

		this.unlockedKits = Sets.newHashSet();
	}

	/**
	 * Update the player's KDR
	 */
	public void updateKDR() {
		double kills = this.kills;
		double deaths = this.deaths;

		// We don't want a negative KDR
		double kdr = kills / Math.max(1, deaths);

		// Then, we round the KDR number like in TimeUtil class (##.#)
		this.kdr = Math.round(10.0D * kdr) / 10.0D;
	}

	/**
	 * Check if the player has unlocked a kit
	 * 
	 * @param kit - the kit
	 * @return - has unlocked value
	 */
	public boolean hasKit(Kit kit) {
		return kit != null && (kit.isDefault() || this.unlockedKits.contains(kit));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PlayerData)) {
			return false;
		}

		return ((PlayerData) obj).getUuid().equals(this.uuid);
	}

	@Override
	public int hashCode() {
		return this.uuid.toString().hashCode();
	}
}
