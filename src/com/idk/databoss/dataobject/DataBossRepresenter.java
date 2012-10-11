/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.dataobject;

/**
 * This class allows me to make intelligent checks to sync queries and
 * requirements with the database by specifying java class fields with their
 * database representations.
 *
 * @author shoemaki
 */
public class DataBossRepresenter {

    public String key;
    public DatabaseType type;
    public String joinTable = null;

    public DataBossRepresenter(String key, DatabaseType type) {
        this.key = key;
        this.type = type;
    }

    public DataBossRepresenter(String key, DatabaseType type, Class joinClass) {
        this.key = key;
        this.type = type;
        this.joinTable = joinClass.getSimpleName();
    }

    public enum DatabaseType {

        ID, Optional, Join, Required, PartialRequired;
        // partial required would be if a table requires 1 of 2 or more attributes
        // not implemented yet
    }
}
