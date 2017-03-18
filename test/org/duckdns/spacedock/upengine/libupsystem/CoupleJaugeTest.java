/* 
 * Copyright (C) 2017 ykonoclast
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author ykonoclast
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

	Perso persotest = new Perso(3);

	jauge1.recevoirDegats(1000, persotest);
	jauge2.recevoirDegats(1000, persotest);
	jauge3.recevoirDegats(1000, persotest);

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
	    Assert.assertEquals("paramétre aberrant:physique:3 volonté:2 trait le plus faible:-1", e.getMessage());
	}

	try
	{
	    jauge3.recevoirDegats(-1, persotest);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:-1", e.getMessage());
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

	Perso persotest = new Perso(3);

	jauge1.recevoirDegats(1000, persotest);
	jauge2.recevoirDegats(1000, persotest);
	jauge3.recevoirDegats(1000, persotest);

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
