/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.testobjects;

import com.idk.annotation.ReflectionIgnore;
import com.idk.databoss.annotation.DataBossColumn;
import com.idk.databoss.annotation.DataBossTable;
import com.idk.databoss.dataobject.DataBossObject;
import org.joda.time.DateTime;

/**
 *
 * @author shoemaki
 */
@DataBossTable(shorthand = "pe")
public class Person extends DataBossObject {

    @DataBossColumn
    private int age;
    @DataBossColumn
    @ReflectionIgnore
    private DateTime birthDay;
    @DataBossColumn
    private String firstName;
    @DataBossColumn
    private String lastName;
    @DataBossColumn
    private Vehicle vehicle;

    public Person() {
        super();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public DateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(DateTime birthDay) {
        this.birthDay = birthDay;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
