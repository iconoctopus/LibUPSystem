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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReferenceSysteme n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{//les classes final, appelant du statique et les classes subissant un whennew
	    Bouclier.class, UPReferenceArmures.class,
	})
public class UnitBouclierTest
{

    static Bouclier bouclier1;
    static Bouclier bouclier2;
    private static UPReferenceArmures referenceArmuresMock;

    @BeforeClass
    public static void setUpClass()
    {
	//on mocke la référence
	referenceArmuresMock = PowerMockito.mock(UPReferenceArmures.class);
	PowerMockito.mockStatic(UPReferenceArmures.class);
	when(UPReferenceArmures.getInstance()).thenReturn(referenceArmuresMock);

	//On crée les pièces de test
	when(referenceArmuresMock.getPtsBouclier(3)).thenReturn(4);
	when(referenceArmuresMock.getLblBouclier(3)).thenReturn("pavois");
	when(referenceArmuresMock.getLblTypeArmure(3)).thenReturn("énergétique");
	bouclier1 = new Bouclier(3, 3);

	when(referenceArmuresMock.getPtsBouclier(0)).thenReturn(2);
	when(referenceArmuresMock.getLblBouclier(0)).thenReturn("targe");
	bouclier2 = new Bouclier(0, 0);
    }

    @Test
    public void testPoints()
    {
	Assert.assertEquals(4, bouclier1.getNbPoints());
	Assert.assertEquals(2, bouclier2.getNbPoints());
    }

    @Test
    public void testType()
    {
	Assert.assertEquals(3, bouclier1.getType());
	Assert.assertEquals(0, bouclier2.getType());
    }

    @Test
    public void testLibelle()
    {
	Assert.assertEquals("pavois énergétique", bouclier1.toString());
	Assert.assertEquals("targe", bouclier2.toString());
    }
}
