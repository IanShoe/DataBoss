/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.utils;

import com.idk.databoss.testobjects.TestDataBossObject;
import com.idk.object.ExtendedField;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author shoemaki
 */
public class DataBossUtilsTest {

    private TestDataBossObject tdbo = new TestDataBossObject();

    /**
     * Test of addColumnsAsString method, of class DataBossUtils.
     */
    @Test
    public void testAddColumnsAsString() {
        Collection<Object> items = new ArrayList<Object>();
        items.add("stringOne");
        items.add("stringTwo");
        String expResult = "stringOne, stringTwo";
        String result = DataBossUtils.addColumnsAsString(items);
        assertEquals(expResult, result);
    }

    /**
     * Test of addValuesAsString method, of class DataBossUtils.
     */
    @Test
    public void testAddValuesAsString() {
        Collection<Object> items = new ArrayList<Object>();
        items.add("stringOne");
        items.add("stringTwo");
        String expResult = "('stringOne', 'stringTwo')";
        String result = DataBossUtils.addValuesAsString(items);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAllDataBossFields() {
        Collection<ExtendedField> fields = DataBossUtils.extendedGetAllDataBossFields(tdbo);
        Collection<String> fieldNames = new ArrayList();
        fieldNames.add("myId");
        fieldNames.add("myString");
        fieldNames.add("theirId");
        fieldNames.add("coolInt");
        fieldNames.add("extension");
        fieldNames.add("nester");
        assertTrue(fields.size() == fieldNames.size());
        for (ExtendedField field : fields) {
            assertTrue(fieldNames.contains(field.getField().getName()));
            if ("myId".equals(field.getField().getName()) || "theirId".equals(field.getField().getName()) || "coolInt".equals(field.getField().getName()) || "myString".equals(field.getField().getName()) || "extension".equals(field.getField().getName())) {
                assertTrue(field.getObject().equals(tdbo));
            } else if ("nester".equals(field.getField().getName())) {
                assertTrue(field.getObject().equals(tdbo.getNester()));
            }
        }
    }
}