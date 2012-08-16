/**
 */
package edu.nyu.library.datawarehouse;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * @author Scot Dalton
 * DataWarehouseModule leverages Guice to provide a singleton instance of 
 * a DataWarehouse.  DataWarehouseModule is instantiated with a Properties 
 * object that includes the datawarehouse connection information.
 *
 */
public class DataWarehouseModule extends AbstractModule {
	private PropertiesConfiguration propertiesConfiguration;

	/**
	 * 
	 * @param properties
	 */
	public DataWarehouseModule(PropertiesConfiguration propertiesConfiguration) {
		super();
		this.propertiesConfiguration = propertiesConfiguration;
	}
	
	@Override
	protected void configure() {
	}
	
	/**
	 * Returns a singleton instance of a DataWarehouse based on the
	 * connection information passed in when the DataWarehouseModule
	 * was instantiated. 
	 * @return
	 */
	@Provides @Singleton
	DataWarehouse provideDataWarehouse() {
		DataWarehouse dataWarehouse = null;
		try {
			Driver driver = 
				(Driver) Class.forName(
					propertiesConfiguration.getString("driverClass")).newInstance();
			String connectionURL = propertiesConfiguration.getString("connectionURL");
			String username = propertiesConfiguration.getString("username");
			String password = propertiesConfiguration.getString("password");
			DriverManager.registerDriver(driver);
			Connection connection = 
				DriverManager.getConnection(connectionURL, username, password);
			dataWarehouse = new DataWarehouse(connection);
		} catch (Exception e) {
			addError(e);
		}
		return dataWarehouse;
	}
}