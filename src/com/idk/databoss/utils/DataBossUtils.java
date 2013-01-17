/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.utils;

import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.dataobject.DataBossObject;
import com.idk.object.ExtendedField;
import com.idk.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

//******NOTE********
//95% of this is already genericly setup, but I have it tailored to a
//database format I created. Somehow, I need to interupt the flows of the prepare methods
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
     * Method to convert a list of columns into appropriate SQL column string
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
     * Method to convert a list of values into appropriate SQL value string
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
     * Method to convert single value into appropriate SQL value string
     *
     * @param items values to SQL stringify
     * @return SQL string version of values
     */
    public static String addValueAsString(Object item) {
        StringBuilder valueBuilder = new StringBuilder();
        if (item == null) {
            return "";
        }
        if (item.getClass() == Integer.class) {
            return valueBuilder.append(item).toString();
        } else {
            return valueBuilder.append("'").append(item.toString()).append("'").toString();
        }
    }

    public static Collection<Field> getDataBossFields(Class<? extends DataBossObject> clazz) {
        Collection<Field> fields = new CopyOnWriteArrayList<Field>(ReflectionUtils.getPrimitiveFields(clazz));
        for (Field field : fields) {
            if (field.getAnnotation(DataBossColumn.class) == null) {
                fields.remove(field);
            }
        }
        return fields;
    }

    public static Collection<Field> getAllDataBossFields(Class<? extends DataBossObject> clazz) {
        Collection<Field> fields = new CopyOnWriteArrayList<Field>(ReflectionUtils.getAllPrimitiveFields(clazz));
        for (Field field : fields) {
            if (field.getAnnotation(DataBossColumn.class) == null) {
                fields.remove(field);
            }
        }
        return fields;
    }

    public static Collection<ExtendedField> extendedGetDataBossFields(DataBossObject baseObject) {
        Collection<ExtendedField> extendedFields = new CopyOnWriteArrayList<ExtendedField>(ReflectionUtils.extendedGetAllClassFields(baseObject));
        for (ExtendedField extendedField : extendedFields) {
            if (extendedField.getField().getAnnotation(DataBossColumn.class) == null) {
                extendedFields.remove(extendedField);
            }
        }
        return extendedFields;
    }

    public static Collection<ExtendedField> extendedGetAllDataBossFields(DataBossObject baseObject) {
        Collection<ExtendedField> extendedFields = new CopyOnWriteArrayList<ExtendedField>(ReflectionUtils.extendedGetAllFields(baseObject));
        for (ExtendedField extendedField : extendedFields) {
            if (extendedField.getField().getAnnotation(DataBossColumn.class) == null) {
                extendedFields.remove(extendedField);
            }
        }
        return extendedFields;
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
//    public static void prepareSelect(DataBossObject dataBossObject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
//        Collection<Object> columns = new ArrayList<Object>();
//        Collection<Object> joinTables = new ArrayList<Object>();
//        for (Iterator<DataBossRepresenter> it = dataBossObject.getDbRetrievableItems().iterator(); it.hasNext();) {
//            DataBossRepresenter item = it.next();
//            StringBuilder column = new StringBuilder();
//            if (item.type == DataBossRepresenter.DataBossType.Required || item.type == DataBossRepresenter.DataBossType.ID || item.type == DataBossRepresenter.DataBossType.Optional) {
//                column.append(dataBossObject.getClass().getSimpleName()).append(".").append(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                columns.add(column.toString());
//            } else if (item.type == DataBossRepresenter.DataBossType.Join) {
//                column.append(item.joinTable).append(".").append(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                columns.add(column.toString());
//                column.append("=").append(item.joinTable).append(".").append(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                if (!joinTables.contains(item.joinTable)) {
//                    joinTables.add((item.joinTable));
//                }
//            }
//        }
//        dataBossObject.setColumns(columns);
//        dataBossObject.setJoinTables(joinTables);
//    }
    /**
     * Set columns and values of dataBoss objects for insert query preparation
     *
     * @param dataBossObject object to prepare
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalRequiredAttribute
     */
//    public static void prepareInsert(DataBossObject dataBossObject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
//        Collection<Object> columns = new ArrayList<Object>();
//        Collection<Object> values = new ArrayList<Object>();
//        for (Iterator<DataBossRepresenter> it = dataBossObject.getDbRetrievableItems().iterator(); it.hasNext();) {
//            DataBossRepresenter item = it.next();
//            Object checker;
//            if (item.type == DataBossRepresenter.DataBossType.ID) {
//                try {
//                    checker = ReflectionUtils.getProperty(dataBossObject, item.key);
//                    if (checker != null) { // probably unnecessary now with
//                        // FieldNotFoundException
//                        values.add(checker);
//                        columns.add(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                    } else {
//                        // Postgres server may have default values for ID so
//                        // continue with insert
//                    }
//                } catch (FieldNotFoundException ex) {
//                    // Postgres server may have default values for ID so
//                    // continue with insert
//                }
//            } else if (item.type == DataBossRepresenter.DataBossType.Required) {
//                try {
//                    checker = ReflectionUtils.getProperty(dataBossObject, item.key);
//                    if (checker != null) { // probably unnecessary now with
//                        // FieldNotFoundException
//                        values.add(checker);
//                        columns.add(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                    } else {
//                    }
//                } catch (FieldNotFoundException ex) {
//                    // If a required field fails, blow up the insert.
//                    throw new IllegalRequiredAttribute("Required attribute " + item.key + " was not found.");
//                }
//
//            } else if (item.type == DataBossRepresenter.DataBossType.Optional) {
//                try {
//                    checker = ReflectionUtils.getProperty(dataBossObject, item.key);
//                    if (checker != null) { // probably unnecessary now with
//                        // FieldNotFoundException
//                        values.add(checker);
//                        columns.add(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                    }
//                } catch (FieldNotFoundException e) {
//                    // If an optional field fails, don't blow up the insert.
//                }
//            }
//        }
//        dataBossObject.setColumns(columns);
//        dataBossObject.setValues(values);
//    }
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
//    public static Collection<DataBossObject> createDataBossObjects(Class<? extends DataBossObject> clazz, ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalRequiredAttribute {
//        Collection<DataBossObject> results = new ArrayList<DataBossObject>();
//        while (rs.next()) {
//            DataBossObject result = (DataBossObject) clazz.newInstance();
//            for (Iterator<DataBossRepresenter> it = result.getDbRetrievableItems().iterator(); it.hasNext();) {
//                DataBossRepresenter item = it.next();
//                Object value = rs.getObject(FormatUtils.formatSmartAllUpperToUnderscore(item.key));
//                if (value != null) {
//                    try {
//                        ReflectionUtils.setProperty(result, item.key, value);
//                    } catch (FieldNotFoundException e) {
//                        if (item.type == DataBossRepresenter.DataBossType.Required) {
//                            throw new IllegalRequiredAttribute("Could not set property " + item.key + " with value " + value);
//                        } else {
//                            // not a required field so don't blow it up
//                        }
//                    }
//                } else if (item.type == DataBossRepresenter.DataBossType.Required) {
//                    // Could not set a required field.
//                    throw new IllegalRequiredAttribute("Required attribute " + item.key + " was not found in database.");
//                }
//            }
//            results.add(result);
//        }
//        return results;
//    }
}