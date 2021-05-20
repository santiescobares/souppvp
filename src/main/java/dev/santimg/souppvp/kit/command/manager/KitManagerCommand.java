package dev.santimg.souppvp.kit.command.manager;

import org.bukkit.command.CommandSender;

import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerCommand {

	@Command(name = "kitManager", permission = "souppvp.command.kitmanager")
	public void execute(CommandArgs cmd) {
		this.sendHelpMessage(cmd.getSender());
	}

	private void sendHelpMessage(CommandSender sender) {
		sender.sendMessage(ChatUtil.SHORT_LINE);
		sender.sendMessage(ChatUtil.translate("&b&lKit Commands"));
		sender.sendMessage(ChatUtil.SHORT_LINE);
		sender.sendMessage(ChatUtil.translate(" &7&l► &f/kitManager create <kitName>"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &f/kitManager delete <kitName>"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &f/kitManager setPrice <kitName> <price>"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &f/kitManager setDefault <kitName>"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &f/kitManager setContents <kitName>"));
		sender.sendMessage(ChatUtil.translate(" &7&l► &f/kitManager saveAll"));
		sender.sendMessage(ChatUtil.SHORT_LINE);
	}
}
