/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.testobjects;

import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import com.idk.databoss.dataobject.DataBossObject;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author shoemaki
 */
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
}