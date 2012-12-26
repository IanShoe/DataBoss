/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.testobjects;

import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.dataobject.DataBossObject;

/**
 *
 * @author shoemaki
 */
public abstract class TestExtension extends DataBossObject {

    @DataBossColumn
    private String extension = "extension";

    public TestExtension(){
        super();
    }
    
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}