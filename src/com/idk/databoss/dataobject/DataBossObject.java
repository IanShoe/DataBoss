/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.dataobject;

import com.idk.databoss.exception.IllegalRequiredAttribute;
import com.idk.databoss.utils.DataBossUtils;
import com.idk.utils.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * POJO properties must follow java naming conventions (mixedCase) for
 * reflection to work
 *
 * @author shoemaki
 */
public abstract class DataBossObject {

    protected Collection<DataBossRepresenter> dbRetrievableItems = new ArrayList<DataBossRepresenter>();
    //Maybe make a mapping instead of 3 collections with the key being how a field relates
    protected Collection<Object> columns;
    protected Collection<Object> values;
    protected Collection<Object> joinTables;
    protected String joinLink;

    public DataBossObject() {
        //Ensure all DataBossObjects call their setDBRetrievableItems by always 
        //Calling this.super(); A better way would be a startup annotation, but 
        //depending on implementation style and type of server determines startup procedures
        this.setDBRetrievableItems();
    }

    public Collection<Object> getColumns() {
        return this.columns;
    }

    public void setColumns(Collection<Object> columns) {
        this.columns = columns;
    }

    public Collection<Object> getValues() {
        return this.values;
    }

    public void setValues(Collection<Object> values) {
        this.values = values;
    }

    public Collection<Object> getJoinTables() {
        return this.joinTables;
    }

    public void setJoinTables(Collection<Object> joinTables) {
        this.joinTables = joinTables;
    }

    public Collection<DataBossRepresenter> getDbRetrievableItems() {
        return this.dbRetrievableItems;
    }

    protected abstract void setDBRetrievableItems();

    /**
     * Method to generate delete string. (Not fully implemented)
     *
     * @return delete string
     */
    public String createDeleteQuery() {
        StringBuilder query = new StringBuilder().append("DELETE FROM ").append(this.getClass().getSimpleName());
        return query.toString();
    }

    /**
     * Method to generate insert string. Will always call an item's
     * prepareInsert method
     *
     * @return insert string
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalRequiredAttribute
     */
    public String createInsertQuery() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
        DataBossUtils.prepareInsert(this);
        StringBuilder query = new StringBuilder().append("INSERT INTO ").append(this.getClass().getSimpleName()).append(" (").append(DataBossUtils.addColumnsAsString(columns)).append(")").append(" VALUES ").append(DataBossUtils.addValuesAsString(values));
        return query.toString();
    }

    /**
     * Method to generate select string. Will always call an item's
     * prepareSelect method
     *
     * @return select query
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalRequiredAttribute
     */
    public String createSelectQuery() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
        DataBossUtils.prepareSelect(this);
        StringBuilder query = new StringBuilder();

        if (!this.joinTables.isEmpty()) {
            query.append("SELECT ").append(DataBossUtils.addColumnsAsString(columns)).append(" FROM ").append(this.getClass().getSimpleName()).append(" INNER JOIN ").append(DataBossUtils.addColumnsAsString(joinTables)).append(" ").append("ON ").append(this.joinLink);
        } else {
            query.append("SELECT ").append(DataBossUtils.addColumnsAsString(columns)).append(" FROM ").append(this.getClass().getSimpleName());
        }
        return query.toString();
    }

    public String createUpdateQuery() {
        return "not implemented yet";
    }

    /**
     * Currently used for inserts. You can override this method in the concrete
     * class for specific validation.
     *
     * @return if a DataBossObject is valid for insert
     */
    public boolean isValid() {
        for (DataBossRepresenter item : dbRetrievableItems) {
            Object checker;
            if (item.type == DataBossRepresenter.DataBossType.Required) {
                try {
                    //Add any other validation here
                    checker = ReflectionUtils.getProperty(this, item.key);
                    if ("".equals(checker.toString()) || checker == null) {
                        return false;
                    }
                } catch (Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }
}