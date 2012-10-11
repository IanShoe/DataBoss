/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.utils;

import com.idk.databoss.dataobject.DataBossObject;
import com.idk.databoss.dataobject.DataBossRepresenter;
import com.idk.databoss.exception.IllegalRequiredAttribute;
import ian.utils.FormatUtils;
import ian.utils.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//******NOTE********
//95% of this is already genericly setup, but I have it tailored to a
//database format I created. Somehow, I need to interupt the flows of the preps
//so that they reflect the database type
//******NOTE********
/**
 * Utils that SQL databases should have access to **NOTE** maybe try to format
 * into java naming conventions before reflection
 *
 * @author shoemaki
 */
public class DataBossUtils {

    /**
     * Value strings require single quotes around each value. Columns do not I
     * might want to try variable binding
     *
     * @param items columns to SQL stringify
     * @return SQL string version of columns
     */
    public static String addColumnsAsString(Collection<Object> items) {
        StringBuilder listForm = new StringBuilder();
        boolean first = true;
        for (Object current : items) {
            if (!first) {
                listForm.append(", ");
            }
            first = false;
            listForm.append(current);
        }
        return listForm.toString();
    }

    /**
     * Value strings require single quotes around each value. Columns do not I
     * might want to try variable binding
     *
     * @param items values to SQL stringify
     * @return SQL string version of values
     */
    public static String addValuesAsString(Collection<Object> items) {
        StringBuilder listForm = new StringBuilder("(");
        boolean first = true;
        for (Object current : items) {
            if (!first) {
                listForm.append(", ");
            }
            first = false;
            if (current.getClass() == int.class) {
                listForm.append(current);
            } else {
                listForm.append("'").append(current.toString()).append("'");
            }
        }
        return listForm.append(")").toString();
    }

    /**
     * Sets select columns and join tables of a dataBossObject. This method will
     * determine if an attribute is from a joining table and add appropriately
     * to join list. *IMPORTANT* Join columns must have the same name in the DB
     * *Example* MissionID has a column named mission_id MVD must then use the
     * foreign key name mission_id
     *
     * @param dataBossObject object type to select
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalRequiredAttribute
     */
    public static void prepareSelect(DataBossObject dataBossObject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
        Collection<Object> columns = new ArrayList<Object>();
        Collection<Object> joinTables = new ArrayList<Object>();
        Collection<Object> joinColumns = new ArrayList<Object>();
        for (Iterator<DataBossRepresenter> it = dataBossObject.getDbRetrievableItems().iterator(); it.hasNext();) {
            DataBossRepresenter item = it.next();
            StringBuilder column = new StringBuilder();
            if (item.type == DataBossRepresenter.DatabaseType.Required || item.type == DataBossRepresenter.DatabaseType.ID || item.type == DataBossRepresenter.DatabaseType.Optional) {
                column.append(dataBossObject.getClass().getSimpleName()).append(".").append(FormatUtils.formatUpperToUnderscore(item.key));
                columns.add(column.toString());
            } else if (item.type == DataBossRepresenter.DatabaseType.Join) {
                column.append(item.joinTable).append(".").append(FormatUtils.formatUpperToUnderscore(item.key));
                columns.add(column.toString());
                column.append("=").append(item.joinTable).append(".").append(FormatUtils.formatUpperToUnderscore(item.key));
                joinColumns.add(column.toString());
                if (!joinTables.contains(item.joinTable)) {
                    joinTables.add((item.joinTable));
                }

            }
        }
        dataBossObject.setColumns(columns);
        dataBossObject.setJoinTables(joinTables);
        dataBossObject.setJoinColumns(joinColumns);
    }

    /**
     * Set columns and values of dataBoss objects for insert query preparation
     *
     * @param dataBossObject object to prepare
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalRequiredAttribute
     */
    public static void prepareInsert(DataBossObject dataBossObject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
        Collection<Object> columns = new ArrayList<Object>();
        Collection<Object> values = new ArrayList<Object>();
        for (Iterator<DataBossRepresenter> it = dataBossObject.getDbRetrievableItems().iterator(); it.hasNext();) {
            DataBossRepresenter item = it.next();
            Object checker;
            if (item.type == DataBossRepresenter.DatabaseType.ID) {
                //Never insert IDs
                continue;
            } else if (item.type == DataBossRepresenter.DatabaseType.Required) {
                checker = ReflectionUtils.getProperty(dataBossObject, item.key);
                if (checker != null) {
                    values.add(checker);
                    columns.add(FormatUtils.formatUpperToUnderscore(item.key));
                } else {
                    // If a required field fails, blow up the insert.
                    throw new IllegalRequiredAttribute("Required attribute " + item.key + " was not found.");
                }// || item.type == DataBossRepresenter.DatabaseType.JoinID
            } else if (item.type == DataBossRepresenter.DatabaseType.Optional) {
                checker = ReflectionUtils.getProperty(dataBossObject, item.key);
                if (checker != null) {
                    values.add(checker);
                    columns.add(FormatUtils.formatUpperToUnderscore(item.key));
                }
                //If an optional field fails, don't blow up the insert. 
            }

        }
        dataBossObject.setColumns(columns);
        dataBossObject.setValues(values);
    }

    /**
     * Creates a list of any dataBoss object returned from a dataBoss database
     * and assigns the returned values to it's properties
     *
     * @param clazz type of objects to create
     * @param rs result set from executed query
     * @return list of new dataBoss objects
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Collection<DataBossObject> createDataBossObjects(Class clazz, ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
        Collection<DataBossObject> results = new ArrayList<DataBossObject>();
        while (rs.next()) {
            DataBossObject result = (DataBossObject) clazz.newInstance();
            for (Iterator<DataBossRepresenter> it = result.getDbRetrievableItems().iterator(); it.hasNext();) {
                DataBossRepresenter item = it.next();
                Object value = rs.getObject(FormatUtils.formatUpperToUnderscore(item.key));
                if (value != null) {
                    ReflectionUtils.setProperty(result, item.key, value);
                } else if (item.type == DataBossRepresenter.DatabaseType.Required) {
                    //Could not set a required field.
                    throw new IllegalRequiredAttribute("Required attribute " + item.key + " was not found.");
                }
            }
            results.add(result);
        }
        return results;
    }
}
