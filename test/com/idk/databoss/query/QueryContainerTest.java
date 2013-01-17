/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.query;

import com.ctc.databoss.query.QueryContainer;
import com.idk.databoss.exception.DataBossException;
import com.idk.databoss.object.DataBossObjectTest;
import com.idk.databoss.testobjects.Person;
import com.idk.databoss.testobjects.Vehicle;
import com.idk.databoss.testobjects.VehicleType;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shoemaki
 */
public class QueryContainerTest {

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
        ian.setId(UUID.fromString("ad44b3eb-3b65-49d5-b1ee-ffbbf14d6894"));
    }

    /**
     * Test of save method, of class DataBossObject.
     */
    @Test
    public void testQueryContainer() {
        try {
            QueryContainer lancerContainer = lancer.getQueryContainer();
            QueryContainer ianContainer = ian.getQueryContainer();
            assertEquals(lancerContainer.buildInsert(), "INSERT INTO vehicle (maker, name, vehicle_type, year, id)  VALUES ('Mitsubishi', 'Lancer', 'Car', 2012, 'ca44b3eb-3b65-49d5-b1ee-0ffbbf14d685' ) ");
            assertEquals(ianContainer.buildInsert(), "INSERT INTO person (age, birth_day, first_name, last_name, vehicle_id, id)  VALUES (22, '1990-05-18T00:00:00.000-04:00', 'Ian', 'Shoemaker', 'ca44b3eb-3b65-49d5-b1ee-0ffbbf14d685', 'ad44b3eb-3b65-49d5-b1ee-ffbbf14d6894' ) ");
            System.out.println(lancerContainer.buildSelect());
            System.out.println(ianContainer.buildSelect());
            assertEquals(lancerContainer.buildSelect(), "SELECT vh.maker, vh.name, vh.vehicle_type, vh.year, vh.id FROM vehicle vh ");
            
            assertEquals(ianContainer.buildSelect(), "SELECT pe.age, pe.birth_day, pe.first_name, pe.last_name, pe.id, vh.maker, vh.name, vh.vehicle_type, vh.year, vh.id FROM person pe INNER JOIN vehicle vh ON pe.vehicle_id=vh.id ");
        } catch (DataBossException ex) {
            fail(ex.getMessage());
        }

    }
}
