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
    private String nester = "nester";
    private String nestedOmit = "nestOmit";

    public String getNester() {
        return nester;
    }

    public void setNester(String nester) {
        this.nester = nester;
    }

    public String getNestedOmit() {
        return nestedOmit;
    }

    public void setNestedOmit(String nestedOmit) {
        this.nestedOmit = nestedOmit;
    }
}