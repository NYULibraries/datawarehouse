/**
 * 
 */
package edu.nyu.library.datawarehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
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
public class DataWarehouseTest {
	private final String SQL_SELECT_STUB = 
		"SELECT OCLC_MASTER FROM HARVARD_PROJECT_OCLC_KEYS WHERE ALEPH_BSN = ";
	private final String ALEPH_BSN = "000000001";
	private final String SQL_SELECT = SQL_SELECT_STUB + ALEPH_BSN;
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
	public void testIntantiate() 
			throws SQLException, FileNotFoundException, IOException {
		DataWarehouse dataWarehouse = 
			injector.getInstance(DataWarehouse.class);
		assertTrue(dataWarehouse instanceof DataWarehouse);
	}
	
	@Test
	public void testExecuteQuery_select() 
			throws FileNotFoundException, IOException, SQLException {
		DataWarehouse dataWarehouse = 
			injector.getInstance(DataWarehouse.class);
		ResultSet results = dataWarehouse.executeQuery(SQL_SELECT);
		assertTrue(results.next());
		assertEquals("154703639", results.getString(1));
		assertFalse(results.next());
		dataWarehouse.close();
		dataWarehouse.closeConnections();
	}
}