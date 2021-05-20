package dev.santimg.souppvp;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import dev.santimg.souppvp.combat.CombatManager;
import dev.santimg.souppvp.command.*;
import dev.santimg.souppvp.database.DatabaseManager;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.listener.*;
import dev.santimg.souppvp.playerdata.PlayerDataManager;
import dev.santimg.souppvp.scoreboard.SoupPvPScoreboardProvider;
import dev.santimg.souppvp.scoreboard.provider.BoardProvider;
import dev.santimg.souppvp.spawn.SpawnManager;
import dev.santimg.souppvp.task.ClearLagTask;
import dev.santimg.souppvp.utilities.FileConfig;
import dev.santimg.souppvp.utilities.command.CommandFramework;
import lombok.Getter;

@Getter
public class SoupPvP extends JavaPlugin {

	@Getter
	private static SoupPvP instance;

	private FileConfig utilitiesFile;
	private FileConfig kitsFile;

	private CommandFramework commandFramework;

	private DatabaseManager databaseManager;
	private KitManager kitManager;
	private PlayerDataManager playerDataManager;
	private SpawnManager spawnManager;
	private CombatManager combatManager;

	private BoardProvider scoreboardProvider;

	@Override
	public void onEnable() {
		instance = this;

		this.saveDefaultConfig();
		this.utilitiesFile = new FileConfig(this, "utilities.yml");
		this.kitsFile = new FileConfig(this, "kits.yml");

		// We are using CommandFramework by minnymin3 as our command executor provider
		this.commandFramework = new CommandFramework(this);

		this.registerManagers();
		this.registerListeners();
		this.registerCommands();

		// We are using Assemble by TheKawaiiSam as our scoreboard provider
		this.scoreboardProvider = new BoardProvider(this, new SoupPvPScoreboardProvider(), 2L);

		// Run clear lag task every 30 seconds
		new ClearLagTask().runTaskTimerAsynchronously(this, 30L * 20L, 30L * 20L);
	}

	@Override
	public void onDisable() {
		// Make sure all SoupPvP listeners stop listening once the plugin disables
		HandlerList.unregisterAll(this);

		this.scoreboardProvider.onDisable();

		this.combatManager.getCombatTagMap().clear();
		this.playerDataManager.onDisable();
		this.kitManager.onDisable();

		this.databaseManager.close();
	}

	private void registerManagers() {
		this.databaseManager = new DatabaseManager();
		this.kitManager = new KitManager();
		this.playerDataManager = new PlayerDataManager();
		this.spawnManager = new SpawnManager();
		this.combatManager = new CombatManager();
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new PreventionListener(), this);
		Bukkit.getPluginManager().registerEvents(new StatisticsListener(), this);
	}

	private void registerCommands() {
		this.commandFramework.registerCommands(new SoupPvPCommand());
		this.commandFramework.registerCommands(new BuildModeCommand());
		this.commandFramework.registerCommands(new StatisticsCommand());
	}
}
