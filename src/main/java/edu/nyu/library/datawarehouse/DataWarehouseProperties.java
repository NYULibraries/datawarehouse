/**
 * 
 */
package edu.nyu.library.datawarehouse;

import java.io.Reader;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author Scot Dalton
 *
 */
public class DataWarehouseProperties {
	private PropertiesConfiguration propertiesConfiguration;
	private String driverClass;
	private String connectionURL;
	private String username;
	private String password;
	private int maxStatements;

	private DataWarehouseProperties(PropertiesConfiguration propertiesConfiguration) {
		this.propertiesConfiguration = propertiesConfiguration;
		if (this.propertiesConfiguration.isEmpty()) loadFromEnvironment();
		driverClass = propertiesConfiguration.getString("driverClass");
		connectionURL = propertiesConfiguration.getString("connectionURL");
		username = propertiesConfiguration.getString("username");
		password = propertiesConfiguration.getString("password");
		maxStatements = 
			(propertiesConfiguration.containsKey("maxStatements")) ? 
				propertiesConfiguration.getInt("maxStatements") : 180;
	}
	
	public String getDriverClass() {
		return driverClass;
	}
	
	public String getConnectionURL() {
		return connectionURL;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public int getMaxStatements() {
		return maxStatements;
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