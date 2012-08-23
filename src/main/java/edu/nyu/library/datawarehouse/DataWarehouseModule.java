/**
 */
package edu.nyu.library.datawarehouse;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * DataWarehouseModule leverages Guice to provide a singleton instance of 
 * a DataWarehouse.  DataWarehouseModule is instantiated with a Properties 
 * object that includes the datawarehouse connection information.
 *
 * @author Scot Dalton
 * 
 */
public class DataWarehouseModule extends AbstractModule {
	private DataWarehouseProperties properties;

	/**
	 * 
	 * @param properties
	 */
	public DataWarehouseModule(DataWarehouseProperties properties) {
		super();
		this.properties = properties;
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
			dataWarehouse = new DataWarehouse(properties);
		} catch (Exception e) {
			addError(e);
		}
		return dataWarehouse;
	}
}