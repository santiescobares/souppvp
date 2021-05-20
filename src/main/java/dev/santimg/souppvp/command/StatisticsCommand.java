package dev.santimg.souppvp.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.playerdata.PlayerData;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class StatisticsCommand {

	@Command(name = "statistics", aliases = { "stats", "information", "info" })
	public void execute(CommandArgs cmd) {
		String[] args = cmd.getArgs();
		CommandSender sender = cmd.getSender();

		if (args.length >= 1) {
			Player target = Bukkit.getPlayer(args[0]);

			// Note this plugin will not support offline data
			if (target == null) {
				sender.sendMessage(ChatUtil.translate("&cPlayer \"" + args[0] + "\" not found!"));
				return;
			}

			this.sendInfoMessage(sender, target);
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatUtil.translate("&cUsage: /" + cmd.getLabel() + " <player>"));
				return;
			}

			this.sendInfoMessage(sender, (Player) sender);
		}
	}

	private void sendInfoMessage(CommandSender sender, Player target) {
		PlayerData playerData = SoupPvP.getInstance().getPlayerDataManager().get(target.getUniqueId());

		sender.sendMessage(ChatUtil.SHORT_LINE);
		sender.sendMessage(ChatUtil.translate("&b&l" + target.getName() + "'s Statistics"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fBalance: &b$" + playerData.getBalance()));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fKills: &b" + playerData.getKills()));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fDeaths: &b" + playerData.getDeaths()));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fKDR: &b" + playerData.getKdr()));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fKillstreak: &b" + playerData.getKillstreak()));
		sender.sendMessage(ChatUtil.SHORT_LINE);
	}
}
