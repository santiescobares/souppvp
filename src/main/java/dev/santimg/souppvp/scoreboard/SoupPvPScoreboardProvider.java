package dev.santimg.souppvp.scoreboard;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.combat.CombatManager;
import dev.santimg.souppvp.playerdata.PlayerData;
import dev.santimg.souppvp.scoreboard.provider.BoardAdapter;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.TimeUtil;

public class SoupPvPScoreboardProvider implements BoardAdapter {

	@Override
	public String getTitle(Player player) {
		return "&b&lSoupPvP";
	}

	@Override
	public List<String> getLines(Player player) {
		List<String> lines = Lists.newArrayList();
		PlayerData playerData = SoupPvP.getInstance().getPlayerDataManager().get(player.getUniqueId());

		CombatManager combatManager = SoupPvP.getInstance().getCombatManager();

		lines.add(ChatUtil.SB_LINE);
		lines.add("Balance: &b$" + playerData.getBalance());
		lines.add("Kills: &b" + playerData.getKills());
		lines.add("Deaths: &b" + playerData.getDeaths());
		lines.add("KDR: &b" + playerData.getKdr());

		if (playerData.getKillstreak() > 0) {
			lines.add("Killstreak: &b" + playerData.getKillstreak());
		}

		if (combatManager.isInCombat(player)) {
			long timeleft = combatManager.getMillisLeft(player);

			// We do this to prevent showing a -0.7 time for example
			if (timeleft > 0) {
				lines.add("");
				lines.add("&c&lCombat Tag: &f" + TimeUtil.roundMillisecondsToDouble(timeleft));
			}
		}

		lines.add("");
		lines.add("&bsantimg.dev");
		lines.add(ChatUtil.SB_LINE);

		return lines;
	}
}
