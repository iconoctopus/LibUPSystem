/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author iconoctopus
 */
public class ArmeTest
{

    static ArmeCaC arme1;
    static ArmeCaC arme2;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass()
    {
	arme1 = new ArmeCaC(0, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
	arme2 = new ArmeCaC(1, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
    }

    @Test
    public void testArme()
    {
	Assert.assertEquals(1, arme1.getBonusInit());
	Assert.assertEquals(1, arme1.getCategorie());
	Assert.assertEquals(3, arme1.getDesGardes());
	Assert.assertEquals(2, arme1.getDesLances());
	Assert.assertEquals(0, arme1.getMalusAttaque());
	Assert.assertEquals(0, arme1.getTypeArme());
	Assert.assertEquals(0, arme1.getphysMin());

	Assert.assertEquals(0, arme2.getBonusInit());
	Assert.assertEquals(3, arme2.getCategorie());
	Assert.assertEquals(3, arme2.getDesGardes());
	Assert.assertEquals(6, arme2.getDesLances());
	Assert.assertEquals(0, arme2.getMalusAttaque());
	Assert.assertEquals(0, arme2.getTypeArme());
	Assert.assertEquals(2, arme2.getphysMin());

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("degats:-1 type:2");
	new Arme.Degats(-1, 2);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("degats:2 type:-1");
	new Arme.Degats(2, -1);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("increments:2 physque:-1");
	arme1.genererDegats(2, -1, true);

    }
}
