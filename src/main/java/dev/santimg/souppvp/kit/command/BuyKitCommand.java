package dev.santimg.souppvp.kit.command;

import org.bukkit.entity.Player;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.playerdata.PlayerData;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class BuyKitCommand {

	@Command(name = "buyKit", permission = "souppvp.command.buykit", playersOnly = true)
	public void execute(CommandArgs cmd) {
		KitManager kitManager = SoupPvP.getInstance().getKitManager();

		String[] args = cmd.getArgs();
		Player player = cmd.getPlayer();

		if (args.length >= 1) {
			Kit kit = kitManager.get(args[0]);

			if (kit == null) {
				player.sendMessage(ChatUtil.translate("&cKit \"" + args[0] + "\" not found!"));
				return;
			}

			PlayerData playerData = SoupPvP.getInstance().getPlayerDataManager().get(player.getUniqueId());

			if (playerData.hasKit(kit)) {
				player.sendMessage(ChatUtil.translate("&cYou have already unlocked this kit!"));
				return;
			}

			long playerBalance = playerData.getBalance();
			long kitPrice = kit.getPrice();

			if (playerBalance < kitPrice) {
				player.sendMessage(ChatUtil.translate("&cYou need at least &l$" + kitPrice + " &cto buy this kit and you only have &l$" + playerBalance + "&c!"));
				return;
			}

			playerData.setBalance(playerBalance - kitPrice);
			playerData.getUnlockedKits().add(kit);

			player.sendMessage(ChatUtil.translate("You have &abought &b" + kit.getName() + " &fkit! You can load it using &b/kit " + kit.getName() + "&f."));
		} else {
			player.sendMessage(ChatUtil.translate("&cUsage: /" + cmd.getLabel() + " <kitName>"));
		}
	}
}
