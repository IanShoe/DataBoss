/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.testobjects;

import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author shoemaki
 */
@DataBossTable(shorthand= "tdbo")
public class TestDataBossObject extends TestExtension {

    @DataBossColumn
    private int myId = 1;
    @DataBossColumn
    private int theirId = 2;
    @DataBossColumn
    private int coolInt = 3;
    @DataBossColumn
    private String myString = "string";
    @DataBossColumn
    private TestNester nester = new TestNester(); // INNER JOIN this guy?
    private String ommited = "omit";
    private Collection<Object> myObjects = new ArrayList<Object>();

    public TestDataBossObject() {
        super();
    }

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

    public TestNester getNester() {
        return nester;
    }

    public void setNester(TestNester nester) {
        this.nester = nester;
    }

    public String getOmmited() {
        return ommited;
    }

    public void setOmmited(String ommited) {
        this.ommited = ommited;
    }
}