/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.testobjects;

import com.idk.annotation.ReflectionIgnore;
import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import com.idk.databoss.dataobject.DataBossObject;

/**
 *
 * @author shoemaki
 */
@DataBossTable(shorthand = "vh")
public class Vehicle extends DataBossObject {

    @DataBossColumn
    private String maker;
    @DataBossColumn
    private String name;
    @DataBossColumn
    @ReflectionIgnore
    private VehicleType vehicleType;
    @DataBossColumn
    private int year;
    
    public Vehicle(){
        super();
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
