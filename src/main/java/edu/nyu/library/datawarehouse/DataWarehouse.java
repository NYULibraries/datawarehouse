/**
 * 
 */
package edu.nyu.library.datawarehouse;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
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
		dataSource.setMaxPoolSize(100);
		dataSource.setInitialPoolSize(50);
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
	public Entry<Connection, ResultSet> executeQuery(String sql) throws SQLException {
		Connection connection = dataSource.getConnection();
		ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
		return Maps.immutableEntry(connection, resultSet);
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