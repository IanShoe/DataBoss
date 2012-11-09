/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.utils;

import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import com.idk.databoss.dataobject.DataBossObject;
import com.idk.databoss.dataobject.DataBossRepresenter;
import com.idk.databoss.exception.IllegalRequiredAttribute;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author shoemaki
 */
public class DataBossUtilsTest {

    public DataBossUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addColumnsAsString method, of class DataBossUtils.
     */
    @Test
    public void testAddColumnsAsString() {
        System.out.println("addColumnsAsString");
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
        System.out.println("addValuesAsString");
        Collection<Object> items = new ArrayList<Object>();
        items.add("stringOne");
        items.add("stringTwo");
        String expResult = "('stringOne', 'stringTwo')";
        String result = DataBossUtils.addValuesAsString(items);
        assertEquals(expResult, result);
    }

    /**
     * Test of prepareSelect method, of class DataBossUtils.
     * The prepareSelect method ensures the columns and jointables are set
     * beforeadding them to the select query string
     *
     * @throws IllegalRequiredAttribute
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void testPrepareSelect() {
        System.out.println("prepareSelect");
        TestDataBossObject tester = new TestDataBossObject();
        tester.setMyId(5);
        tester.setTheirId(6);
        tester.setMyString("testString");
        Collection<Object> myObjects = new ArrayList<Object>();
        myObjects.add(new Object());
        tester.setMyObjects(myObjects);
        try {
            DataBossUtils.prepareSelect(tester);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InvocationTargetException e) {
            System.out.println(e);
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        } catch (IllegalRequiredAttribute e) {
            System.out.println(e);
        }

        Collection<Object> columns = tester.getColumns();
        Collection<Object> joinTables = tester.getJoinTables();
        assertTrue(columns.contains("TestDataBossObject.my_id") && columns.contains("TestDataBossObject.my_string") && columns.contains("TestDataBossObject.my_objects"));
        assertTrue(joinTables.contains("TestDataBossObjectJoin"));
    }

    /**
     * Test of prepareInsert method, of class DataBossUtils.
     * The prepareInsert needs to ensure the columns and values are properly set
     * before adding them to the insert query string
     *
     * @throws IllegalRequiredAttribute
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void testPrepareInsert() {
        System.out.println("prepareInsert");
        TestDataBossObject tester = new TestDataBossObject();
        tester.setMyId(5);
        tester.setTheirId(6);
        tester.setCoolInt(3);
        tester.setMyString("testString");
        Collection<Object> myObjects = new ArrayList<Object>();
        myObjects.add(new Object());
        tester.setMyObjects(myObjects);
        try {
            DataBossUtils.prepareInsert(tester);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InvocationTargetException e) {
            System.out.println(e);
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        } catch (IllegalRequiredAttribute e) {
            System.out.println(e);
        }
        Collection<Object> columns = tester.getColumns();
        Collection<Object> values = tester.getValues();
        assertTrue(columns.contains("my_string") && columns.contains("my_objects"));
        assertTrue(values.contains(3) && values.contains("testString"));
    }

    /**
     * Test of buildGenericSelectQuery method, of class DataBossUtils.
     */
    @Test
    public void testBuildGenericSelectQuery() {
        System.out.println("buildGenericSelectQuery\n");
        TestDataBossObject fullObject = new TestDataBossObject();
        TestDataBossObjectJoin partialObject = new TestDataBossObjectJoin();
//        fullObject.setMyId(5);
//        fullObject.setTheirId(6);
//        fullObject.setCoolInt(3);
//        fullObject.setMyString("testString");
//        Collection<Object> myObjects = new ArrayList<Object>();
//        myObjects.add(new Object());
//        fullObject.setMyObjects(myObjects);

        String fullObjectSelect = DataBossUtils.buildGenericSelectQuery(fullObject);
        String fullObjectExpected = "SELECT tdbo.my_id, tdbo.their_id, tdbo.cool_int, tdbo.my_string FROM test_data_boss_object tdbo";
//        System.out.println("FullObject generic select:\n " + fullObjectSelect);
        assertEquals(fullObjectExpected, fullObjectSelect);

        // Test something that has select off
        String partialObjectSelect = DataBossUtils.buildGenericSelectQuery(partialObject);
        String partialObjectExpected = "SELECT tdboj.my_id, tdboj.my_string FROM test_data_boss_object_join tdboj";
//        System.out.println("\nPartialObject generic select: (Testing an object with a select field off)\n " + partialObjectSelect);
        assertEquals(partialObjectExpected, partialObjectSelect);

        // Test something that doesn't have the annotations
        String emptySelect = DataBossUtils.buildGenericSelectQuery(new Object());
        String emptyExpected = "SELECT ";
//        System.out.println("\nEmpty generic select: (Testing an object without annotations)\n " + emptySelect);
        assertEquals(emptyExpected, emptySelect);

    }

    /**
     * Test of buildGenericInsertQuery method, of class DataBossUtils.
     */
    @Test
    public void testBuildGenericInsertQuery() {
        System.out.println("buildGenericInserttQuery\n");
        TestDataBossObject fullObject = new TestDataBossObject();
        TestDataBossObjectJoin partialObject = new TestDataBossObjectJoin();

        String fullObjectInsert = DataBossUtils.buildGenericInsertQuery(fullObject);
        String fullObjectExpected = "INSERT INTO test_data_boss_object (my_id, their_id, cool_int, my_string) VALUES ( :my_id, :their_id, :cool_int, :my_string)";
//	System.out.println("TransactionLog generic insert:\n " + fullObjectInsert);
        assertEquals(fullObjectExpected, fullObjectInsert);

        // Test something that has insert off
        String partialObjectInsert = DataBossUtils.buildGenericInsertQuery(partialObject);
        String partialObjectExpected = "INSERT INTO test_data_boss_object_join (my_id, my_string) VALUES ( :my_id, :my_string)";
//        System.out.println("\nPartialObject generic insert: (Testing an object with an insert field off)\n " + partialObjectInsert);
        assertEquals(partialObjectExpected, partialObjectInsert);


        // Test something that doesn't have the annotations
        String emptyInsert = DataBossUtils.buildGenericInsertQuery(new Object());
        String emptyExpected = "INSERT INTO ";
//        System.out.println("\nEmpty generic insert: (Testing an object without annotations)\n " + emptyInsert);
        assertEquals(emptyExpected, emptyInsert);
    }

    @DataBossTable(tableShortHand = "tdbo")
    public class TestDataBossObject extends DataBossObject {

        public TestDataBossObject() {
            super();
            // Temporary way of doing this
            this.joinLink = "TestDataBossObject.their_Id=TestDataBossObjectJoin.my_Id";
        }
        @DataBossColumn
        private int myId;
        @DataBossColumn
        private int theirId;
        @DataBossColumn
        private int coolInt;
        @DataBossColumn
        private String myString;
        private Collection<Object> myObjects = new ArrayList<Object>();

        public int getMyId() {
            return myId;
        }

        public void setMyId(int myId) {
            this.myId = myId;
        }

        public int getTheirId() {
            return theirId;
        }

        public void setTheirId(int theirId) {
            this.theirId = theirId;
        }

        public int getCoolInt() {
            return coolInt;
        }

        public void setCoolInt(int coolInt) {
            this.coolInt = coolInt;
        }

        public String getMyString() {
            return myString;
        }

        public void setMyString(String myString) {
            this.myString = myString;
        }

        public Collection<Object> getMyObjects() {
            return myObjects;
        }

        public void setMyObjects(Collection<Object> myObjects) {
            this.myObjects = myObjects;
        }

        @Override
        protected void setDBRetrievableItems() {
            this.dbRetrievableItems.add(new DataBossRepresenter("myId", DataBossRepresenter.DataBossType.ID));
            this.dbRetrievableItems.add(new DataBossRepresenter("theirId", DataBossRepresenter.DataBossType.Join, TestDataBossObjectJoin.class));
            this.dbRetrievableItems.add(new DataBossRepresenter("coolInt", DataBossRepresenter.DataBossType.Optional));
            this.dbRetrievableItems.add(new DataBossRepresenter("myString", DataBossRepresenter.DataBossType.Required));
            this.dbRetrievableItems.add(new DataBossRepresenter("myObjects", DataBossRepresenter.DataBossType.Optional));
        }
    }

    @DataBossTable(tableShortHand = "tdboj")
    public class TestDataBossObjectJoin extends DataBossObject {

        public TestDataBossObjectJoin() {
            super();
        }
        @DataBossColumn
        private int myId;
        @DataBossColumn
        private String myString;
        @DataBossColumn(select = false, insert = false)
        private Collection<Object> myObjects = new ArrayList<Object>();

        public int getMyId() {
            return myId;
        }

        public void setMyId(int myId) {
            this.myId = myId;
        }

        public String getMyString() {
            return myString;
        }

        public void setMyString(String myString) {
            this.myString = myString;
        }

        public Collection<Object> getMyObjects() {
            return myObjects;
        }

        public void setMyObjects(Collection<Object> myObjects) {
            this.myObjects = myObjects;
        }

        @Override
        protected void setDBRetrievableItems() {
            this.dbRetrievableItems.add(new DataBossRepresenter("myId", DataBossRepresenter.DataBossType.ID));
            this.dbRetrievableItems.add(new DataBossRepresenter("myString", DataBossRepresenter.DataBossType.Required));
            this.dbRetrievableItems.add(new DataBossRepresenter("myObjects", DataBossRepresenter.DataBossType.Optional));
        }
    }
    /**
     * Test of createDataBossObjects method, of class DataBossUtils.
     */
    // @Test
    // public void testCreateDataBossObjects() throws Exception {
    // System.out.println("createDataBossObjects");
    // Class<?> clazz = null;
    // ResultSet rs = null;
    // Collection expResult = null;
    // Collection result = DataBossUtils.createDataBossObjects(clazz, rs);
    // assertEquals(expResult, result);
    // }
}
