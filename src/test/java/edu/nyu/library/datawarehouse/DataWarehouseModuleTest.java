/**
 * 
 */
package edu.nyu.library.datawarehouse;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Scot Dalton
 *
 */
public class DataWarehouseModuleTest {
	
	private final static String propertiesFilename = 
		"./src/test/resources/META-INF/datawarehouse.properties";
	private Injector injector;
	
	@Before
	public void setup() throws ConfigurationException {
		PropertiesConfiguration propertiesConfiguration = 
			new PropertiesConfiguration(propertiesFilename);
		if (propertiesConfiguration.isEmpty()) {
			for(Entry<String,String> property : System.getenv().entrySet())
				propertiesConfiguration.setProperty(property.getKey(), property.getValue());
		}
		injector = 
			Guice.createInjector(new DataWarehouseModule(propertiesConfiguration));
	}
	
	@Test
	public void testGetInstance() throws SQLException  {
		DataWarehouse dataWarehouse = 
			injector.getInstance(DataWarehouse.class);
		assertTrue(dataWarehouse instanceof DataWarehouse);
	}
	
	@Test
	public void testSingleton() {
		DataWarehouse dataWarehouse1 = 
			injector.getInstance(DataWarehouse.class);
		DataWarehouse dataWarehouse2 = 
			injector.getInstance(DataWarehouse.class);
		assertSame(dataWarehouse1, dataWarehouse2);
	}
}