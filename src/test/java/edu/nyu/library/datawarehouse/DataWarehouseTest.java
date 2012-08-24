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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.perf4j.StopWatch;

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
		for(int i = 0; i<10; i++) {
			StopWatch stopWatch = 
				new StopWatch();
			stopWatch.start("Select " + i);
			Entry<Connection, ResultSet> resultsEntry =
				dataWarehouse.executeQuery(SQL_SELECT);
			stopWatch.stop();
			System.out.println(stopWatch.getTag() + ": " + stopWatch.getElapsedTime());
			stopWatch.start("Process " + i);
			Connection connection = resultsEntry.getKey();
			ResultSet results = resultsEntry.getValue();
			assertTrue(results.next());
			assertEquals("154703639", results.getString(1));
			assertFalse(results.next());
			stopWatch.stop();
			System.out.println(stopWatch.getTag() + ": " + stopWatch.getElapsedTime());
			stopWatch.start("Close " + i);
			results.close();
//			connection.close();
			stopWatch.stop();
			System.out.println(stopWatch.getTag() + ": " + stopWatch.getElapsedTime());
		}
		dataWarehouse.close();
		dataWarehouse.closeConnections();
	}
}