package dev.santimg.souppvp.utilities;

import lombok.Getter;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

@Getter
public class FileConfig {

	private File file;
	private FileConfiguration configuration;

	public FileConfig(JavaPlugin plugin, String fileName) {
		this.file = new File(plugin.getDataFolder(), fileName);

		if (!this.file.exists()) {
			this.file.getParentFile().mkdirs();

			if (plugin.getResource(fileName) == null) {
				try {
					this.file.createNewFile();
				} catch (IOException e) {
					plugin.getLogger().severe("Failed to create new file " + fileName);
				}
			} else {
				plugin.saveResource(fileName, false);
			}
		}

		this.configuration = YamlConfiguration.loadConfiguration(this.file);
	}

	public void save() {
		try {
			this.configuration.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		this.configuration = YamlConfiguration.loadConfiguration(file);
	}
}