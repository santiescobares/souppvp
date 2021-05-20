package dev.santimg.souppvp.listener;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.google.common.cache.CacheBuilder;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.playerdata.PlayerData;
import dev.santimg.souppvp.utilities.ChatUtil;

public class StatisticsListener implements Listener {

	/**
	 * In this variable we will cache all players' damagers so we can get them when
	 * a player dies for falling (for example). We are using {@link CacheBuilder} to
	 * create a ConcurrentMap with values that will be removed after 10 seconds.
	 */
	public static final ConcurrentMap<Object, Object> lastDamager = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build().asMap();

	private final long MONEY_ON_KILL = 250L;
	private final long KILLSTREAK_REWARD = 50L;

	@EventHandler(priority = EventPriority.MONITOR) // Let some other events run before
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Entity killerEntity = player.getKiller();

		event.setDeathMessage(null);
		event.setDroppedExp(0);

		// If the player's killer is null we will try to find the player's last damager
		// from the lastDamager Map
		if (killerEntity == null) {
			killerEntity = (Entity) lastDamager.get(player.getUniqueId());
		}

		PlayerData playerData = SoupPvP.getInstance().getPlayerDataManager().get(player.getUniqueId());

		if (killerEntity != null && killerEntity instanceof Player) {
			Player killer = (Player) killerEntity;
			PlayerData killerData = SoupPvP.getInstance().getPlayerDataManager().get(killer.getUniqueId());

			killerData.setKills(killerData.getKills() + 1);
			killerData.setKillstreak(killerData.getKillstreak() + 1);
			killerData.setBalance(killerData.getBalance() + this.MONEY_ON_KILL);
			killerData.updateKDR();

			killer.sendMessage(ChatUtil.translate("&aYou earned &l$" + this.MONEY_ON_KILL + " &afor killing &l" + player.getName() + "&a."));
			player.sendMessage(ChatUtil.translate("&cYou were slain by &l" + killer.getName() + "&c."));

			// Killstreak logic
			// Players will receive a reward every 10 kills streak
			if (killerData.getKillstreak() % 10 == 0) {
				long gainedMoney = this.KILLSTREAK_REWARD * killerData.getKillstreak();

				killerData.setBalance(killerData.getBalance() + gainedMoney);

				killer.sendMessage(ChatUtil.translate("&aYou have received &l$" + gainedMoney + " &afor your &l" + killerData.getKillstreak() + " &akills streak."));

				ChatUtil.broadcast("&7[&b&l!&7] &b" + killer.getName() + " &fhas &areached &fa &b" + killerData.getKillstreak() + " &fkills streak.");
			}
		}

		if (playerData.getKillstreak() > 0) {
			player.sendMessage(ChatUtil.translate("&cYou lost your killstreak of &l" + playerData.getKillstreak() + "&c!"));
		}

		playerData.setDeaths(playerData.getDeaths() + 1);
		playerData.setKillstreak(0);
		playerData.updateKDR();

		lastDamager.remove(player.getUniqueId());
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		// We don't need to cache them if the event is cancelled
		if (event.isCancelled()) {
			return;
		}

		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		if (event.getDamager() == null) {
			return;
		}

		Player player = (Player) event.getEntity();

		lastDamager.put(player.getUniqueId(), event.getDamager());
	}
}
