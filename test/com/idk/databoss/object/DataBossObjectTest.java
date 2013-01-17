/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.object;

import com.idk.databoss.testobjects.Person;
import com.idk.databoss.testobjects.Vehicle;
import com.idk.databoss.testobjects.VehicleType;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author shoemaki
 */
public class DataBossObjectTest {

    private Person ian = new Person();
    private Vehicle lancer = new Vehicle();

    @Before
    public void startUp() {
        lancer.setMaker("Mitsubishi");
        lancer.setName("Lancer");
        lancer.setVehicleType(VehicleType.Car);
        lancer.setYear(2012);
        lancer.setId(UUID.fromString("ca44b3eb-3b65-49d5-b1ee-ffbbf14d685"));
        ian.setAge(22);
        ian.setFirstName("Ian");
        ian.setLastName("Shoemaker");
        ian.setBirthDay(new DateTime(1990, 05, 18, 00, 00));
        ian.setVehicle(lancer);
        ian.setId(UUID.randomUUID());
    }

    /**
     * Test of save method, of class DataBossObject.
     */
    @Test
    public void testSave() {
        try {
            ian.save();
        } catch (Exception ex) {
            Logger.getLogger(DataBossObjectTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
