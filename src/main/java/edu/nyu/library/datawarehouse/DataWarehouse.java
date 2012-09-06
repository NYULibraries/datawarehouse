/**
 * 
 */
package edu.nyu.library.datawarehouse;

import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.inject.Singleton;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * DataWarehouse provides an interface to query a Data Warehouse.
 * 
 * @author Scot Dalton
 * 
 */
@Singleton
public class DataWarehouse {
	private ComboPooledDataSource dataSource;
	
	/**
	 * Public constructor takes a DataWarehouseProperties object with the
	 * connnection information for the DataWarehouse.
	 * @param connection
	 * @throws PropertyVetoException 
	 */
	public DataWarehouse(DataWarehouseProperties properties) throws PropertyVetoException {
		dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(properties.getDriverClass());           
		dataSource.setJdbcUrl(properties.getConnectionURL());
		dataSource.setUser(properties.getUsername());                                  
		dataSource.setPassword(properties.getPassword());
		dataSource.setMaxStatements(properties.getMaxStatements());
		dataSource.setMinPoolSize(30);
		dataSource.setMaxPoolSize(100);
		dataSource.setInitialPoolSize(50);
		dataSource.setMaxConnectionAge(60);
		dataSource.setMaxIdleTime(30);
		dataSource.setMaxIdleTimeExcessConnections(10);
	}
	
	/**
	 * Close the DataWarehouse connections.
	 * @throws SQLException 
	 */
	public void closeConnections() throws SQLException {
		DataSources.destroy(dataSource);
	}
	
	/**
	 * Execute the given SQL command
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return dataSource.getConnection().prepareStatement(sql).executeQuery();
	}
	
	public void close() {
		dataSource.close();
	}
	
	@Override
	protected void finalize() {
		try {
			closeConnections();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}