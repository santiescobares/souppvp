package dev.santimg.souppvp.command;

import org.bukkit.command.CommandSender;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.TaskUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class SoupPvPCommand {

	@Command(name = "soupPvP", aliases = { "author" })
	public void execute(CommandArgs cmd) {
		String[] args = cmd.getArgs();
		CommandSender sender = cmd.getSender();

		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("souppvp.admin") || sender.isOp()) {
					// We will reload all asynchronously to prevent the server lagging
					TaskUtil.runAsync(() -> {
						long start = System.currentTimeMillis();

						SoupPvP.getInstance().reloadConfig();
						SoupPvP.getInstance().getUtilitiesFile().reload();
						SoupPvP.getInstance().getKitsFile().reload();
						SoupPvP.getInstance().getKitManager().load();

						long end = System.currentTimeMillis();

						sender.sendMessage(ChatUtil.translate("Plugin was &asuccessfully &freloaded in &b" + (end - start) + "ms&f!"));
					});

					return;
				}
			}
		}

		sender.sendMessage("");
		sender.sendMessage(ChatUtil.translate("&b&lSoupPvP"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fAuthor: &bSantiMG#7450"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &fVersion: &b" + SoupPvP.getInstance().getDescription().getVersion()));
		sender.sendMessage("");
	}
}
