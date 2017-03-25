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
import org.junit.Test;
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
	    Armure.class, UPReferenceArmures.class
	})
public class UnitArmureTest
{

    @Test
    public void testGetMalus()
    {
	Armure armure = new Armure(2, 12, 1, 3);
	Assert.assertEquals(1, armure.getMalusEsquive());
	Assert.assertEquals(3, armure.getMalusParade());
    }

    @Test
    public void testGetReactionDegatsEntrants()
    {//on mocke la référence
	UPReferenceArmures referenceMock = PowerMockito.mock(UPReferenceArmures.class);
	PowerMockito.mockStatic(UPReferenceArmures.class);
	when(UPReferenceArmures.getInstance()).thenReturn(referenceMock);
	when(referenceMock.getBonusND(12, 3, 2)).thenReturn(5);
	when(referenceMock.getRedDegats(14, 6, 7)).thenReturn(12);

	Assert.assertEquals(5, (new Armure(12, 2, 0, 5)).getBonusND(3));
	Assert.assertEquals(12, (new Armure(14, 7, 0, 7)).getRedDegats(6));
    }
}
