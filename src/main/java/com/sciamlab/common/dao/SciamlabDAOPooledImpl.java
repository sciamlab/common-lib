package com.sciamlab.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import com.sciamlab.common.exception.DAOException;

public class SciamlabDAOPooledImpl extends SciamlabDAO implements AutoCloseable{
	
	private static final Logger logger = Logger.getLogger(SciamlabDAOPooledImpl.class);
	
	public  String JDBC_DRIVER;
	public  String JDBC_URL;
	public  String JDBC_USER;
	public  String JDBC_PASSWORD;
	
	private PooledDataSource ds_pooled;
	
	public SciamlabDAOPooledImpl(String JDBC_DRIVER, String JDBC_URL, String JDBC_USER, String JDBC_PASSWORD) {	 
		this.JDBC_DRIVER = JDBC_DRIVER;
		this.JDBC_URL = JDBC_URL;
		this.JDBC_USER = JDBC_USER;
		this.JDBC_PASSWORD = JDBC_PASSWORD;
		this.init();
	}
 
	@Override
	protected Connection getConnection() throws SQLException {
		return this.ds_pooled.getConnection();
	}
	
	private void init(){
		logger.debug("-------- PostgreSQL Connection Testing ------------");
		try {
			logger.debug(JDBC_DRIVER);
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			throw new DAOException("Where is your PostgreSQL Driver? Include in your library path!");
		}
		logger.debug("PostgreSQL Driver Registered!");
		try{
			DataSource ds_unpooled = DataSources.unpooledDataSource(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
			ds_pooled = (PooledDataSource) DataSources.pooledDataSource( ds_unpooled );
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);){
			//test the connection
			logger.debug(JDBC_URL);
			
			if (connection != null) {
				logger.debug("You made it, take control your database now!");
			} else {
				throw new DAOException("Failed to make connection!");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public void close(){
		try {
			if(ds_pooled!=null)
				ds_pooled.close();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
}
