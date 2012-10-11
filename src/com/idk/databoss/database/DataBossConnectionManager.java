/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.database;

import com.idk.databoss.dataobject.DataBossObject;
import com.idk.databoss.exception.IllegalRequiredAttribute;
import com.idk.databoss.utils.DataBossConnector;
import com.idk.databoss.utils.DataBossUtils;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton Manager used to connect to databases. Needs connection pooling.
 *
 * @author shoemaki
 */
public class DataBossConnectionManager {

    private Connection conn;

    /**
     * Drivers need to be passed in by user. Make the constructor take in a list
     * of either strings or the driver class themselves and load them.
     */
    public DataBossConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
            setupConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupConnection() {
        String url = DataBossConnector.INSTANCE.getProperty("url");
        Properties props = new Properties();
        props.setProperty("user", DataBossConnector.INSTANCE.getProperty("user"));
        props.setProperty("password", DataBossConnector.INSTANCE.getProperty("password"));
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Not really tested
     *
     * @param item
     * @param query
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Collection<DataBossObject> customQuery(DataBossObject item, String query) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Statement st = conn.createStatement();
        try {
            return DataBossUtils.createDataBossObjects(item.getClass(), st.executeQuery(query));
        } catch (IllegalRequiredAttribute ex) {
            System.out.println("Object with a required attribute could not be set");
            Logger.getLogger(DataBossConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Delete Wrapper
     *
     * @param item item to delete
     * @return number of updated rows
     * @throws SQLException
     */
    public int delete(DataBossObject item) throws SQLException {
        return executeUpdate(item.createDeleteQuery());
    }

    /**
     * Insert Wrapper
     *
     * @param item item to insert
     * @return number of updated rows
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public int insert(DataBossObject item) throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException {
        try {
            return executeUpdate(item.createInsertQuery());
        } catch (IllegalRequiredAttribute ex) {
            Logger.getLogger(DataBossConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Select Wrapper
     *
     * @param item to select
     * @return list of items matching select query
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Collection<DataBossObject> select(DataBossObject item) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        try {
            ResultSet rs = executeSelect(item.createSelectQuery());
            return DataBossUtils.createDataBossObjects(item.getClass(), rs);
        } catch (IllegalRequiredAttribute ex) {
            System.out.println("Object with a required attribute could not be set");
            Logger.getLogger(DataBossConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Update Wrapper
     *
     * @param item item to update
     * @return number of updated rows
     * @throws SQLException
     */
    public int update(DataBossObject item) throws SQLException {
        return executeUpdate(item.createUpdateQuery());
    }

    /**
     * Update execution method that make the database requests
     *
     * @param query query to execute
     * @return number of updated rows
     * @throws SQLException
     */
    private int executeUpdate(String query) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeUpdate(query);
    }

    /**
     * Select execution method that makes the database request
     *
     * @param query query to execute
     * @return results of query
     * @throws SQLException
     */
    private ResultSet executeSelect(String query) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery(query);
    }
}