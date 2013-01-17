/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.database;

import com.idk.databoss.utils.DataBossConnector;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton Manager used to connect to databases. Needs connection pooling.
 * *NOTE* Currently, this is more of a postgres connection manager. The select,
 * update, insert, and delete methods should be abstract
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

    public int update(String query) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeUpdate(query);
    }

    public ResultSet select(String query) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery(query);
    }
}