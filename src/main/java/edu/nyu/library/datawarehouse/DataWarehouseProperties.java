/**
 * 
 */
package edu.nyu.library.datawarehouse;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbutils.DbUtils;

/**
 * @author Scot Dalton
 *
 */
public class DataWarehouseProperties {
	private PropertiesConfiguration propertiesConfiguration;
	private Connection connection;

	private DataWarehouseProperties(PropertiesConfiguration propertiesConfiguration) {
		this.propertiesConfiguration = propertiesConfiguration;
		if (this.propertiesConfiguration.isEmpty()) loadFromEnvironment();
	}

	public Connection getConnection() throws SQLException {
		if(connection == null) {
			DbUtils.loadDriver(propertiesConfiguration.getString("driverClass"));
			String connectionURL = propertiesConfiguration.getString("connectionURL");
			String username = propertiesConfiguration.getString("username");
			String password = propertiesConfiguration.getString("password");
			connection = DriverManager.getConnection(connectionURL, username, password);
		}
		return connection;
	}
	
	public PropertiesConfiguration getPropertiesConfiguration() {
		return propertiesConfiguration;
	}
	
	/**
	 * @return
	 */
	public boolean isEmpty() {
		return propertiesConfiguration.isEmpty();
	}

	private void loadFromEnvironment() {
		if (propertiesConfiguration.isEmpty())
			for(Entry<String,String> property : System.getenv().entrySet())
				propertiesConfiguration.setProperty(property.getKey(), property.getValue());
	}

	public static class Builder {
		private PropertiesConfiguration propertiesConfiguration;
		
		public Builder() {
			this(new PropertiesConfiguration());
		}
		
		public Builder(Reader reader) throws ConfigurationException {
			propertiesConfiguration = new PropertiesConfiguration();
			propertiesConfiguration.load(reader);
		}
		
		public Builder(PropertiesConfiguration propertiesConfiguration) {
			this.propertiesConfiguration = propertiesConfiguration;
		}
		
		public Builder driverClass(String driverClass) {
			propertiesConfiguration.addProperty("driverClass", driverClass);
			return this;
		}

		public Builder connectionURL(String connectionURL) {
			propertiesConfiguration.addProperty("connectionURL", connectionURL);
			return this;
		}

		public Builder username(String username) {
			propertiesConfiguration.addProperty("username", username);
			return this;
		}

		public Builder password(String password) {
			propertiesConfiguration.addProperty("password", password);
			return this;
		}
		
		public DataWarehouseProperties build() {
			return new DataWarehouseProperties(propertiesConfiguration);
		}
	}
}