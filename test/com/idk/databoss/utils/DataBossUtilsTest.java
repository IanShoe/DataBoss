/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.utils;

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
	}
	catch (IllegalAccessException e) {
	    System.out.println(e);
	}
	catch (InvocationTargetException e) {
	    System.out.println(e);
	}
	catch (NoSuchMethodException e) {
	    System.out.println(e);
	}
	catch (IllegalRequiredAttribute e) {
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
	}
	catch (IllegalAccessException e) {
	    System.out.println(e);
	}
	catch (InvocationTargetException e) {
	    System.out.println(e);
	}
	catch (NoSuchMethodException e) {
	    System.out.println(e);
	}
	catch (IllegalRequiredAttribute e) {
	    System.out.println(e);
	}
	Collection<Object> columns = tester.getColumns();
	Collection<Object> values = tester.getValues();
	assertTrue(columns.contains("my_string") && columns.contains("my_objects"));
	assertTrue(values.contains(3) && values.contains("testString"));
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

    public class TestDataBossObject extends DataBossObject {

	public TestDataBossObject() {
	    super();
	    // Temporary way of doing this
	    this.joinLink = "TestDataBossObject.their_Id=TestDataBossObjectJoin.my_Id";
	}

	private int myId;
	private int theirId;
	private int coolInt;
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

    public class TestDataBossObjectJoin extends DataBossObject {

	public TestDataBossObjectJoin() {
	    super();
	}

	private int myId;
	private String myString;
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
//    @Test
//    public void testCreateDataBossObjects() throws Exception {
//        System.out.println("createDataBossObjects");
//        Class clazz = null;
//        ResultSet rs = null;
//        Collection expResult = null;
//        Collection result = DataBossUtils.createDataBossObjects(clazz, rs);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
