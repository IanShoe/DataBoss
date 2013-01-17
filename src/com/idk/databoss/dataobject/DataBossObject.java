/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.dataobject;

import com.ctc.databoss.query.QueryContainer;
import com.idk.annotation.ReflectionIgnore;
import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import com.idk.databoss.database.DataBossConnectionManager;
import com.idk.databoss.exception.DataBossException;
import com.idk.databoss.utils.DataBossUtils;
import com.idk.object.ExtendedField;
import com.idk.utils.FormatUtils;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * POJO properties must follow java naming conventions (mixedCase) for
 * reflection to work
 *
 * @author shoemaki
 */
public abstract class DataBossObject {

    @DataBossColumn
    @ReflectionIgnore
    protected UUID id = UUID.randomUUID();
    @ReflectionIgnore
    private String databaseTableName;
    @ReflectionIgnore
    private QueryContainer container;
    @ReflectionIgnore
    private static DataBossConnectionManager connectionManager = new DataBossConnectionManager();

    public DataBossObject() {
        DataBossTable tableAnnotation = this.getClass().getAnnotation(DataBossTable.class);
        if (tableAnnotation == null) {
            throw new RuntimeException("Required annotation DataBossTable not found for class " + this.getClass().getSimpleName() + ". DataBoss Objects require the use of @DataBossTable annotation to link the object to the table.");
        } else {
            if ("default".equals(tableAnnotation.databaseTableName())) {
                databaseTableName = FormatUtils.formatSmartAllUpperToUnderscore(this.getClass().getSimpleName());
            } else {
                databaseTableName = tableAnnotation.databaseTableName();
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void prepareDataBossObject() throws DataBossException {
        //Maybe make a Query Manager that maintains a list of which objects have already been created and use that as a flag
        if (container == null) {
            this.constructQueryContainer();
        }
        this.constructInsertQuery();
    }

    public QueryContainer getQueryContainer() throws DataBossException {
        this.prepareDataBossObject();
        return this.container;
    }

    public void delete() {
        //TODO
    }

    public void save() throws SQLException, IllegalAccessException, DataBossException {
        prepareDataBossObject();
        connectionManager.update(container.buildInsert());
    }

    public void select() {
        //TODO
    }

    public void update() {
        //TODO
    }

    /**
     * Currently used for inserts. You can override this method in the concrete
     * class for specific validation.
     *
     * @return if a DataBossObject is valid for insert
     */
    public boolean isValid() {
//        for (DataBossRepresenter item : dbRetrievableItems) {
//            Object checker;
//            if (item.type == DataBossRepresenter.DataBossType.Required) {
//                try {
//                    //Add any other validation here
//                    checker = ReflectionUtils.getProperty(this, item.key);
//                    if ("".equals(checker.toString()) || checker == null) {
//                        return false;
//                    }
//                } catch (Exception ex) {
//                    return false;
//                }
//            }
//        }
        return true;
    }

    /**
     * Initialization method for queryContainer.
     * Currently handles from, select
     *
     */
    private void constructQueryContainer() throws DataBossException {
        DataBossTable thisTableAnnotation = this.getClass().getAnnotation(DataBossTable.class);
        String thisShortHand = thisTableAnnotation.shorthand();

        StringBuilder deleteBuilder = new StringBuilder("DELETE ");
        StringBuilder fromBuilder = new StringBuilder("FROM ").append(this.databaseTableName).append(" ").append(thisShortHand);
        StringBuilder innerJoinBuilder = new StringBuilder();
        StringBuilder outerBuilder = new StringBuilder("OUTER JOIN ");
        StringBuilder selectBuilder = new StringBuilder("SELECT ");
        StringBuilder updateBuilder = new StringBuilder("UPDATE ");
        StringBuilder whereBuilder = new StringBuilder("WHERE ");
        StringBuilder listBuilder = new StringBuilder();

        boolean first = true;
        boolean innerFirst = true;
        for (ExtendedField extendedField : DataBossUtils.extendedGetAllDataBossFields(this)) {
            DataBossObject dataBossObject = (DataBossObject) extendedField.getObject();
            DataBossTable tableAnnotation = (DataBossTable) dataBossObject.getClass().getAnnotation(DataBossTable.class);
            Field field = extendedField.getField();
            field.setAccessible(true);
            DataBossColumn columnAnnotation = (DataBossColumn) field.getAnnotation(DataBossColumn.class);
            if (columnAnnotation.select()) {
                String columnName = columnAnnotation.databaseColumnName();

                if (field.getType().getSuperclass() == DataBossObject.class) {
                    if (innerFirst) {
                        innerJoinBuilder.append("INNER JOIN ");
                        innerFirst = false;
                    }
                    try {
                        DataBossObject dbo = (DataBossObject) field.get(dataBossObject);
                        DataBossTable tA = (DataBossTable) dbo.getClass().getAnnotation(DataBossTable.class);
                        innerJoinBuilder.append(dbo.databaseTableName).append(" ").append(tA.shorthand());
                        innerJoinBuilder.append(" ON ");
                        innerJoinBuilder.append(tableAnnotation.shorthand()).append(".").append(dbo.databaseTableName).append("_id=");
                        innerJoinBuilder.append(tA.shorthand()).append(".id ");
                    } catch (Exception ex) {
                        Logger.getLogger(DataBossObject.class.getName()).log(Level.SEVERE, null, ex);
                        throw new DataBossException(ex.getMessage());
                    }
                } else {
                    if (columnName.equals("default")) {
                        columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getName());
                    } else if (columnName.equals("defaultName")) {
                        columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getType().getSimpleName()) + "_name";
                    } else if (columnName.equals("defaultId")) {
                        columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getType().getSimpleName()) + "_id";
                    }
                    if (!first) {
                        listBuilder.append(", ").append(tableAnnotation.shorthand()).append(".").append(columnName);
                    } else {
                        listBuilder.append(tableAnnotation.shorthand()).append(".").append(columnName);
                        first = false;
                    }
                }
            }
        }
        selectBuilder.append(listBuilder.toString());
        container = new QueryContainer(
                deleteBuilder.toString(),
                fromBuilder.toString(),
                innerJoinBuilder.toString(),
                listBuilder.toString(),
                outerBuilder.toString(),
                selectBuilder.toString(),
                updateBuilder.toString(),
                whereBuilder.toString());
    }

//      StringBuilder sb = new StringBuilder(selectContainer.getSelect());
//	sb.append(", mvd.mvd_id, mvd.mvd_ip, mvd.mvd_serial_number, mvd.mvd_name, mvd.mvd_configuration_id, ");
//	sb.append("lc.location_code_id, lc.location_code_name, ");
//	sb.append("sc.scan_category_id, sc.scan_category_name, ");
//	sb.append("c.card_id, c.card_expiration_date, c.fascn, c.valid, c.uuid, c.full_name, c.ca_issuer, ");
//	sb.append("e.event_id, e.event_name, e.event_description, ");
//	sb.append(" mo.username, mo.mvd_operator_id, mo.pin ");
//	sb.append(selectContainer.getFrom());
//	sb.append(" LEFT OUTER JOIN  mvd mvd ON mvd.mvd_id = tl.mvd_id ");
//	sb.append(" LEFT OUTER JOIN location_code lc ON lc.location_code_id = tl.location_code_id ");
//	sb.append(" LEFT OUTER JOIN scan_category sc ON sc.scan_category_id = tl.scan_category_id ");
//	sb.append(" LEFT OUTER JOIN card c ON c.card_id = tl.card_id ");
//	sb.append(" LEFT OUTER JOIN event e ON e.event_id = tl.event_id ");
//	sb.append(" LEFT OUTER JOIN mvd_operator_linker mol ON mol.mvd_operator_id = tl.mvd_operator_id ");
//	sb.append(" LEFT OUTER JOIN mvd_operator mo ON mo.mvd_operator_id = mol.mvd_operator_id ");
//	sb.append(" ORDER BY e.event_name, tl.start_time ");
    /**
     * This method generates the generic portion of an insert query from a
     * DataBossObject
     *
     * @param baseObject base object to create insert query from
     */
    private void constructInsertQuery() throws DataBossException {
        StringBuilder insertBuilder = new StringBuilder("INSERT INTO ").append(this.databaseTableName).append(" (");
        StringBuilder valueBuilder = new StringBuilder("VALUES (");
        boolean first = true;

        for (ExtendedField extendedField : DataBossUtils.extendedGetDataBossFields(this)) {
            DataBossObject dataBossObject = (DataBossObject) extendedField.getObject();
            Field field = extendedField.getField();
            field.setAccessible(true);
            DataBossColumn columnAnnotation = (DataBossColumn) field.getAnnotation(DataBossColumn.class);
            if (columnAnnotation.insert()) {
                String columnName = columnAnnotation.databaseColumnName();
                try {
                    if (field.getType().getSuperclass() == DataBossObject.class) {
                        DataBossObject dbo = (DataBossObject) field.get(dataBossObject);
                        if (columnName.equals("default")) {
                            columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getType().getSimpleName() + "_id");
                        } else {
                            columnName = columnName + "_id";
                        }
                        if (!first) {
                            insertBuilder.append(", ").append(columnName);
                            valueBuilder.append(", ").append(DataBossUtils.addValueAsString(dbo.getId()));
                        } else {
                            insertBuilder.append(columnName);
                            valueBuilder.append(DataBossUtils.addValueAsString(dbo.getId()));
                            first = false;
                        }
                    } else {
                        if (columnName.equals("default")) {
                            columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getName());
                        } else if (columnName.equals("defaultName")) {
                            columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getType().getSimpleName()) + "_name";
                        } else if (columnName.equals("defaultId")) {
                            columnName = FormatUtils.formatSmartAllUpperToUnderscore(field.getType().getSimpleName()) + "_id";
                        }

                        if (!first) {
                            insertBuilder.append(", ").append(columnName);
                            valueBuilder.append(", ").append(DataBossUtils.addValueAsString(field.get(dataBossObject)));
                        } else {
                            insertBuilder.append(columnName);
                            valueBuilder.append(DataBossUtils.addValueAsString(field.get(dataBossObject)));
                            first = false;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(DataBossObject.class.getName()).log(Level.SEVERE, null, ex);
                    throw new DataBossException(ex.getMessage());
                }
            }
        }
        insertBuilder.append(") ");
        valueBuilder.append(" ) ");
        container.setInsert(insertBuilder.toString());
        container.setValue(valueBuilder.toString());
    }
}