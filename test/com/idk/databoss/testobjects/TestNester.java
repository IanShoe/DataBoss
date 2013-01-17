/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.testobjects;

import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import com.idk.databoss.dataobject.DataBossObject;

/**
 *
 * @author shoemaki
 */
@DataBossTable(shorthand= "tn")
public class TestNester extends DataBossObject {

    @DataBossColumn
    private String nesterString = "nesterString";
    private String nestedOmit = "nestOmit";

    public String getNesterString() {
        return nesterString;
    }

    public void setNesterString(String nesterString) {
        this.nesterString = nesterString;
    }

    public String getNestedOmit() {
        return nestedOmit;
    }

    public void setNestedOmit(String nestedOmit) {
        this.nestedOmit = nestedOmit;
    }
}