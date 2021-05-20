package dev.santimg.souppvp.kit.command.manager;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.TaskUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerSaveAllCommand extends KitManagerCommand {

	@Command(name = "kitManager.saveAll", permission = "souppvp.command.kitmanager")
	public void execute(CommandArgs cmd) {
		// We will save all asynchronously to prevent the server lagging
		TaskUtil.runAsync(() -> {
			SoupPvP.getInstance().getKitManager().save();

			cmd.getSender().sendMessage(ChatUtil.translate("All kits have been &asaved &fin &bkits.yml&f!"));
		});
	}
}
