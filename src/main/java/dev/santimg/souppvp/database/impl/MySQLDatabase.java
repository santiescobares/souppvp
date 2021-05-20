package dev.santimg.souppvp.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.database.Database;
import dev.santimg.souppvp.utilities.TaskUtil;

/**
 * MySQL database implementation
 * 
 * @author SantiMG
 */
public class MySQLDatabase implements Database {

	private Connection connection;

	private final String host;
	private final int port;

	private final String database;
	private final String username;
	private final String password;

	public MySQLDatabase() {
		FileConfiguration configFile = SoupPvP.getInstance().getConfig();

		this.host = configFile.getString("MYSQL.HOST").split(":")[0];
		this.port = Integer.parseInt(configFile.getString("MYSQL.HOST").split(":")[1]);

		this.database = configFile.getString("MYSQL.DATABASE");
		this.username = configFile.getString("MYSQL.AUTH.USERNAME");
		this.password = configFile.getString("MYSQL.AUTH.PASSWORD");
	}

	@Override
	public void connect() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				return;
			}

			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);

			System.out.println("[SoupPvP] MySQL database successfully connected.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if (this.connection == null) {
			return;
		}

		try {
			if (!this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.connection = null;
	}

	/**
	 * Prepare a synchronized MySQL statement
	 * 
	 * @param sql    - the sql statement string
	 * @param values - values to set in the sql statement
	 */
	public void runStatement(String sql, Object... values) {
		try {
			PreparedStatement statement = this.connection.prepareStatement(sql);
			Iterator<Object> valuesIterator = Arrays.asList(values).iterator();
			int i = 1;

			while (valuesIterator.hasNext()) {
				statement.setObject(i, valuesIterator.next());
				i++;
			}

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prepare a non-synchronized MySQL statement
	 * 
	 * @param sql    - the sql statement string
	 * @param values - values to set in the sql statement
	 */
	public void runStatementAsync(String sql, Object... values) {
		TaskUtil.runAsync(() -> this.runStatement(sql, values));
	}

	/**
	 * Run a synchronized MySQL statement
	 * 
	 * @param sql - the sql statement string
	 */
	public void runStatement(String sql) {
		try {
			PreparedStatement statement = this.connection.prepareStatement(sql);

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run a non-synchronized MySQL statement
	 * 
	 * @param sql - the sql statement string
	 */
	public void runStatementAsync(String sql) {
		TaskUtil.runAsync(() -> this.runStatement(sql));
	}

	/**
	 * Prepare a MySQL statement and return the PreparedStatement object
	 * 
	 * @param sql - the sql statement string
	 * @return - the PreparedStatement object instance
	 */
	public PreparedStatement prepareStatementAndGet(String sql) {
		try {
			PreparedStatement statement = this.connection.prepareStatement(sql);

			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Prepare a MySQL statement and return the PreparedStatement object
	 * 
	 * @param sql    - the sql statement string
	 * @param values - values to set in the sql statement
	 * @return - the PreparedStatement object instance
	 */
	public PreparedStatement prepareStatementAndGet(String sql, Object... values) {
		try {
			PreparedStatement statement = this.connection.prepareStatement(sql);
			Iterator<Object> valuesIterator = Arrays.asList(values).iterator();
			int i = 1;

			while (valuesIterator.hasNext()) {
				statement.setObject(i, valuesIterator.next());
				i++;
			}

			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
