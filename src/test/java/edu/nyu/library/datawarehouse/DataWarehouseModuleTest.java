/**
 * 
 */
package edu.nyu.library.datawarehouse;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

import org.apache.commons.configuration.ConfigurationException;
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
	public void setup() throws ConfigurationException, FileNotFoundException {
		File propertiesFile = new File(propertiesFilename);
		DataWarehouseProperties properties = new DataWarehouseProperties.Builder().build();
		if(propertiesFile.exists())
			properties = new DataWarehouseProperties.Builder(new FileReader(propertiesFile)).build();
		injector = 
			Guice.createInjector(new DataWarehouseModule(properties));
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