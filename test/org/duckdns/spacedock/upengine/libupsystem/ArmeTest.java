/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class ArmeTest
{

    static Arme arme1;
    static Arme arme2;

    @BeforeClass
    public static void setUpClass()
    {
	arme1 = new Arme(0);
	arme2 = new Arme(1);
    }

    @Test
    public void testArme()
    {
	Assert.assertEquals(1, arme1.getBonusInit());
	Assert.assertEquals(0, arme1.getCategorie());
	Assert.assertEquals(3, arme1.getDesGardes());
	Assert.assertEquals(2, arme1.getDesLances());
	Assert.assertEquals(0, arme1.getMalusAttaque());
	Assert.assertEquals(0, arme1.getTypeArme());
	Assert.assertEquals(0, arme1.getphysMin());

	Assert.assertEquals(0, arme2.getBonusInit());
	Assert.assertEquals(1, arme2.getCategorie());
	Assert.assertEquals(3, arme2.getDesGardes());
	Assert.assertEquals(6, arme2.getDesLances());
	Assert.assertEquals(0, arme2.getMalusAttaque());
	Assert.assertEquals(0, arme2.getTypeArme());
	Assert.assertEquals(2, arme2.getphysMin());

    }
}
