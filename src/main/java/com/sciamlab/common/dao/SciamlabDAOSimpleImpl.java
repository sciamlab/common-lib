package com.sciamlab.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mchange.v2.c3p0.PooledDataSource;

public class SciamlabDAOSimpleImpl extends SciamlabDAO{
	
	private String DB_URL;
	private String USER;
	private String PASS;
	
	public SciamlabDAOSimpleImpl(String DB_URL, String USER, String PASS) {	 
		this.DB_URL = DB_URL;
		this.USER = USER;
		this.PASS = PASS;
	}
 
	@Override
	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL,USER,PASS);
	}
	
}
