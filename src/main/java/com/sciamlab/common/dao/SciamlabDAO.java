/**
 * Copyright 2014 Sciamlab s.r.l.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sciamlab.common.dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sciamlab.common.exception.DAOException;
import com.sciamlab.common.util.SciamlabStreamUtils;

/**
 * 
 * @author SciamLab
 *
 */

public abstract class SciamlabDAO {
	
	private static final Logger logger = Logger.getLogger(SciamlabDAO.class);
	
	protected abstract Connection getConnection() throws SQLException;
	
	public Map<String, Properties> execQuery(String query, List<Object> params, String key, List<String> columns){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(query);
			if (params!=null)
				for (int i = 1; i <= params.size(); i++) 
					statement.setObject(i, params.get(i - 1));
			logger.debug(statement);
			statement.execute();
			result = statement.getResultSet();
			logger.debug("done");
			
			ResultSetMetaData metadata = result.getMetaData();
			
			Map<String, Properties> map = new LinkedHashMap<String, Properties>();
			int k = 0;
			while(result.next()){
				Properties values = new Properties();
				if(columns==null){
					//read all columns
					for(int i=1 ; i<=metadata.getColumnCount() ; i++){
						String c = metadata.getColumnName(i);
						Object v = result.getObject(c);
						if(v!=null)
							values.put(c, v);
					}
				}else{
					//read only input columns
					for(String c : columns){
						Object v = result.getObject(c);
						if(v!=null)
							values.put(c, v);
					}
				}
				map.put((key!=null)?result.getString(key):""+(k++),values);
			}
			return map;
			
		} catch (Exception e) {
			throw new DAOException(e);
		} finally{
			if (result != null) try { result.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
	        if (statement != null) try { statement.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
	        if (connection != null) try { connection.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
		}
	}
	
	/**
	 * Execs a select statement returning a list of record as a Properties object.
	 * Each value is labeled with the column name from the "columns" list
	 * 
	 * @param query
	 * @param columns
	 * @return
	 */
	public List<Properties> execQuery(String query, List<Object> params, List<String> columns){
		List<Properties> list = new ArrayList<Properties>();
		list.addAll(this.execQuery(query, params, null, columns).values());
		return list;
	}
	
	/**
	 * Execs a select statement returning a list of record as a Properties object.
	 * Each value is labeled with the column name from the select statement
	 * 
	 * @param query
	 * @param params, the params to be set to exec the query
	 * @return
	 */
	public List<Properties> execQuery(String query, List<Object> params){
		return this.execQuery(query, params, null);
	}
	
	/**
	 * Execs a select statement returning a list of record as a Properties object.
	 * Each value is labeled with the column name from the select statement
	 * 
	 * @param query
	 * @return
	 */
	public List<Properties> execQuery(String query){
		return this.execQuery(query, null, null);
	}
	
	/**
	 * Execs a select statement reading from InputStream returning a list of record as a Properties object.
	 * Each value is labeled with the column name from the select statement
	 * 
	 * @param query
	 * @return
	 */
	public List<Properties> execQuery(InputStream is){
		List<Properties> result = new ArrayList<Properties>();
		try {
			result = this.execQuery(SciamlabStreamUtils.convertStreamToString(is).replaceAll("\n", " "), null);
		}finally{
			if(is != null) try { is.close(); } catch (IOException e) { logger.error(e.getMessage(), e);	}
		}
		return result;
	}
	
	/**
	 * Execs a select statement reading from InputStream returning a list of record as a PairMap object.
	 * Each value is labeled with the column name from the select statement
	 * 
	 * @param query
	 * @return
	 */
	public List<Map<String, Object>> execQueryWithMapResult(InputStream is){
		return execQueryWithMapResult(is, null);
	}
	public List<Map<String, Object>> execQueryWithMapResult(InputStream is, List<Object> params){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(SciamlabStreamUtils.convertStreamToString(is).replaceAll("\n", " "));
			if (params!=null)
				for (int i = 1; i <= params.size(); i++) 
					statement.setObject(i, params.get(i - 1));
			logger.debug(statement);
			statement.execute();
			result = statement.getResultSet();
			logger.debug("done");
			ResultSetMetaData metadata = result.getMetaData();
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			int k = 0;
			while(result.next()){
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				//read all columns
				for(int i=1 ; i<=metadata.getColumnCount() ; i++){
					String c = metadata.getColumnName(i);
					Object v = result.getObject(c);
					map.put(c, v);
				}
				list.add(map);
			}
			return list;
			
		} catch (Exception e) {
			throw new DAOException(e);
		} finally{
			if (result != null) try { result.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
	        if (statement != null) try { statement.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
	        if (connection != null) try { connection.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
		}
	}
	
	/**
	 * Execs a SQL update
	 * 
	 * @param update
	 * @return
	 */
	public int execUpdate(String update, List<Object> params){
		try (Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(update);)	{
			if (params!=null)
				for (int i = 1; i <= params.size(); i++) 
					statement.setObject(i, params.get(i - 1));
			logger.debug(statement);
			statement.execute();
			int count = statement.getUpdateCount();
			logger.debug("done: "+count);
//			if (count == 0)
//	            throw new DAOException("SQL update failed, no rows affected.");
			return count;
			
		} catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * Execs a list of SQL updates in one transaction
	 * 
	 * @param update
	 * @return
	 */
	public int execUpdate(LinkedHashMap<String, List<Object>> update_list){
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			logger.debug("begin transaction");
			connection = this.getConnection();
			connection.setAutoCommit(false);
			int count = 0;
			for(String update : update_list.keySet()){
				statement = connection.prepareStatement(update);
				List<Object> params = update_list.get(update); 
	//			if (params!=null)
					for (int i = 1; i <= params.size(); i++) 
						statement.setObject(i, params.get(i - 1));
				logger.debug(statement);
				statement.execute();
				int tmp = statement.getUpdateCount();
				count += tmp;
				logger.debug("done: "+tmp);
			}
			connection.commit();
			logger.debug("commit");
			return count;
			
		} catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e);
		} finally{
	        if (statement != null) try { statement.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
	        if (connection != null) try { connection.setAutoCommit(true); connection.close(); } catch (SQLException e) { logger.error(e.getMessage(), e); }
		}
	}
	
	/**
	 * Execs a SQL insert returning the generated key, if any
	 * 
	 * @param insert
	 * @return
	 */
	public long execInsert(String key, String insert, List<Object> params){
		try (Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(insert, new String[]{key});	){
			if (params!=null)
				for (int i = 1; i <= params.size(); i++) 
					statement.setObject(i, params.get(i - 1));
			logger.debug(statement);
			int count = statement.executeUpdate();
			if (count == 0)
	            throw new DAOException("SQL insert failed, no rows affected");
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) 
	            	return generatedKeys.getLong(1);
	            else 
		            throw new DAOException("SQL insert failed, no ID obtained");
	        }
			
		} catch (DAOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException(e);
		}
	}
	
}
