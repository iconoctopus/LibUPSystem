/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class CoupleJaugeTest
{

    static CoupleJauge jauge1;
    static CoupleJauge jauge2;
    static CoupleJauge jauge3;

    @Test
    public void testJaugeSanteInit()
    {
	jauge1 = new CoupleJauge(1, 1, 1, 1);
	jauge2 = new CoupleJauge(3, 5, 7, 7);
	jauge3 = new CoupleJauge(5, 3, 4, 3);

	Assert.assertFalse(jauge1.isElimine());
	Assert.assertFalse(jauge2.isElimine());
	Assert.assertFalse(jauge3.isElimine());

	Assert.assertFalse(jauge1.isInconscient());
	Assert.assertFalse(jauge2.isInconscient());
	Assert.assertFalse(jauge3.isInconscient());

	Assert.assertFalse(jauge1.isSonne());
	Assert.assertFalse(jauge2.isSonne());
	Assert.assertFalse(jauge3.isSonne());

	Assert.assertEquals(2, jauge1.getTaille_interne());
	Assert.assertEquals(8, jauge2.getTaille_interne());
	Assert.assertEquals(8, jauge3.getTaille_interne());

	Assert.assertEquals(1, jauge1.getTaille_externe());
	Assert.assertEquals(8, jauge2.getTaille_externe());
	Assert.assertEquals(4, jauge3.getTaille_externe());

	Assert.assertEquals(0, jauge1.getRemplissage_interne());
	Assert.assertEquals(0, jauge2.getRemplissage_interne());
	Assert.assertEquals(0, jauge3.getRemplissage_interne());

	Assert.assertEquals(1, jauge1.getRemplissage_externe());
	Assert.assertEquals(8, jauge2.getRemplissage_externe());
	Assert.assertEquals(4, jauge3.getRemplissage_externe());

	jauge1.recevoirDegats(1000, 2, 2);
	jauge2.recevoirDegats(1000, 2, 2);
	jauge3.recevoirDegats(1000, 2, 2);

	Assert.assertEquals(2, jauge1.getTaille_interne());
	Assert.assertEquals(8, jauge2.getTaille_interne());
	Assert.assertEquals(8, jauge3.getTaille_interne());

	Assert.assertEquals(1, jauge1.getTaille_externe());
	Assert.assertEquals(8, jauge2.getTaille_externe());
	Assert.assertEquals(4, jauge3.getTaille_externe());

	Assert.assertEquals(2, jauge1.getRemplissage_interne());
	Assert.assertEquals(8, jauge2.getRemplissage_interne());
	Assert.assertEquals(8, jauge3.getRemplissage_interne());

	Assert.assertEquals(0, jauge1.getRemplissage_externe());
	Assert.assertEquals(0, jauge2.getRemplissage_externe());
	Assert.assertEquals(0, jauge3.getRemplissage_externe());

	Assert.assertTrue(jauge1.isElimine());
	Assert.assertTrue(jauge2.isElimine());
	Assert.assertTrue(jauge3.isElimine());

	Assert.assertTrue(jauge1.isInconscient());
	Assert.assertTrue(jauge2.isInconscient());
	Assert.assertTrue(jauge3.isInconscient());

	Assert.assertTrue(jauge1.isSonne());
	Assert.assertTrue(jauge2.isSonne());
	Assert.assertTrue(jauge3.isSonne());

	try
	{
	    new CoupleJauge(3, 2, -1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:3 volonte:2 trait minimum:-1", e.getMessage());
	}

	try
	{
	    jauge3.recevoirDegats(-1, 20, 2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:degats:-1 jet:20 volonte:2", e.getMessage());
	}
    }

    @Test
    public void testJaugeFatigueForceDAme()
    {
	jauge1 = new CoupleJauge(1, 1, 1);
	jauge2 = new CoupleJauge(3, 5, 2);
	jauge3 = new CoupleJauge(5, 3, 3);

	Assert.assertFalse(jauge1.isElimine());
	Assert.assertFalse(jauge2.isElimine());
	Assert.assertFalse(jauge3.isElimine());

	Assert.assertFalse(jauge1.isInconscient());
	Assert.assertFalse(jauge2.isInconscient());
	Assert.assertFalse(jauge3.isInconscient());

	Assert.assertFalse(jauge1.isSonne());
	Assert.assertFalse(jauge2.isSonne());
	Assert.assertFalse(jauge3.isSonne());

	Assert.assertEquals(2, jauge1.getTaille_interne());
	Assert.assertEquals(8, jauge2.getTaille_interne());
	Assert.assertEquals(8, jauge3.getTaille_interne());

	Assert.assertEquals(1, jauge1.getTaille_externe());
	Assert.assertEquals(2, jauge2.getTaille_externe());
	Assert.assertEquals(3, jauge3.getTaille_externe());

	Assert.assertEquals(0, jauge1.getRemplissage_interne());
	Assert.assertEquals(0, jauge2.getRemplissage_interne());
	Assert.assertEquals(0, jauge3.getRemplissage_interne());

	Assert.assertEquals(1, jauge1.getRemplissage_externe());
	Assert.assertEquals(2, jauge2.getRemplissage_externe());
	Assert.assertEquals(3, jauge3.getRemplissage_externe());

	jauge1.recevoirDegats(1000, 2, 2);
	jauge2.recevoirDegats(1000, 2, 2);
	jauge3.recevoirDegats(1000, 2, 2);

	Assert.assertEquals(2, jauge1.getTaille_interne());
	Assert.assertEquals(8, jauge2.getTaille_interne());
	Assert.assertEquals(8, jauge3.getTaille_interne());

	Assert.assertEquals(1, jauge1.getTaille_externe());
	Assert.assertEquals(2, jauge2.getTaille_externe());
	Assert.assertEquals(3, jauge3.getTaille_externe());

	Assert.assertEquals(2, jauge1.getRemplissage_interne());
	Assert.assertEquals(8, jauge2.getRemplissage_interne());
	Assert.assertEquals(8, jauge3.getRemplissage_interne());

	Assert.assertEquals(0, jauge1.getRemplissage_externe());
	Assert.assertEquals(0, jauge2.getRemplissage_externe());
	Assert.assertEquals(0, jauge3.getRemplissage_externe());

	Assert.assertTrue(jauge1.isElimine());
	Assert.assertTrue(jauge2.isElimine());
	Assert.assertTrue(jauge3.isElimine());

	Assert.assertTrue(jauge1.isInconscient());
	Assert.assertTrue(jauge2.isInconscient());
	Assert.assertTrue(jauge3.isInconscient());

	Assert.assertTrue(jauge1.isSonne());
	Assert.assertTrue(jauge2.isSonne());
	Assert.assertTrue(jauge3.isSonne());
    }
}
