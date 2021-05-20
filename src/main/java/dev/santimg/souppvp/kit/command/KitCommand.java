package dev.santimg.souppvp.kit.command;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.playerdata.PlayerData;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitCommand {

	@Command(name = "kit", aliases = { "kits", "loadKit", "applyKit" }, permission = "souppvp.command.kit")
	public void execute(CommandArgs cmd) {
		KitManager kitManager = SoupPvP.getInstance().getKitManager();

		String[] args = cmd.getArgs();
		CommandSender sender = cmd.getSender();

		if (args.length >= 2) {
			if (!sender.hasPermission("souppvp.command.kit.others")) {
				sender.sendMessage(ChatUtil.translate("&cYou don't have permission to perform this action!"));
				return;
			}

			Kit kit = kitManager.get(args[0]);
			Player target = Bukkit.getPlayer(args[1]);

			if (kit == null) {
				sender.sendMessage(ChatUtil.translate("&cKit \"" + args[0] + "\" not found!"));
				return;
			}

			if (target == null) {
				sender.sendMessage(ChatUtil.translate("&cPlayer \"" + args[1] + "\" not found!"));
				return;
			}

			kit.apply(target, true);

			sender.sendMessage(ChatUtil.translate("You have &aapplied &b" + kit.getName() + " &fto &b" + target.getName() + "&f!"));
		} else if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatUtil.translate("&cUsage: /" + cmd.getLabel() + " <kitName> <player>"));
				return;
			}

			Player player = (Player) sender;
			Kit kit = kitManager.get(args[0]);

			if (kit == null) {
				player.sendMessage(ChatUtil.translate("&cKit \"" + args[0] + "\" not found!"));
				return;
			}

			if (!SoupPvP.getInstance().getPlayerDataManager().get(player.getUniqueId()).hasKit(kit)) {
				player.sendMessage(ChatUtil.translate("&cYou didn't unlock this kit yet!"));
				return;
			}

			kit.apply(player, true);
		} else {
			Set<Kit> kits = kitManager.getKits();

			// If there are no created kits we will send to the player the command usage
			if (kits.isEmpty()) {
				sender.sendMessage(ChatUtil.translate("&cUsage: /" + cmd.getLabel() + " <kitName> " + (sender instanceof Player ? "[player]" : "<player>")));
				return;
			}

			// If there are kits, we will display them
			sender.sendMessage(ChatUtil.SHORT_LINE);
			sender.sendMessage(ChatUtil.translate("&b&lKits List &7(" + kits.size() + ")"));
			sender.sendMessage(ChatUtil.SHORT_LINE);

			// If the sender is a player we will display the kits as KitName - Price -
			// Unlocked/Locked, if not, we will only show the kit name and price
			if (sender instanceof Player) {
				PlayerData playerData = SoupPvP.getInstance().getPlayerDataManager().get(((Player) sender).getUniqueId());

				for (Kit kit : kits) {
					sender.sendMessage(
							ChatUtil.translate(" &7&l► &f" + kit.getName() + " &7- &b$" + kit.getPrice() + " &7- " + (playerData.hasKit(kit) ? "&aUnlocked" : "&cLocked")));
				}
			} else {
				for (Kit kit : kits) {
					sender.sendMessage(ChatUtil.translate(" &7&l► &f" + kit.getName() + " &7- &b$" + kit.getPrice()));
				}
			}

			sender.sendMessage(ChatUtil.SHORT_LINE);
		}
	}
}
