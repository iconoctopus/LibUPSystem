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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Exceptionellement on ne mocke pas UPReferenceSysteme car il est impossible de
 * mocker les membres de la sous-classe, c'est donc en partie un test
 * d'intégration
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReferenceSysteme n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{//les classes final, appelant du statique et les classes subissant un whennew
	    PieceArmure.class, UPReferenceArmures.class,
	})
public class UnitPieceArmureTest
{

    static PieceArmure piece1;
    static PieceArmure piece2;
    static PieceArmure piece3;
    static PieceArmure piece4;
    static PieceArmure piece5;
    static PieceArmure bouclier1;
    static PieceArmure bouclier2;
    private static UPReferenceArmures referenceArmuresMock;

    @BeforeClass
    public static void setUpClass()
    {
	//on mocke la référence
	referenceArmuresMock = PowerMockito.mock(UPReferenceArmures.class);
	PowerMockito.mockStatic(UPReferenceArmures.class);
	when(UPReferenceArmures.getInstance()).thenReturn(referenceArmuresMock);

	//On crée les pièces de test
	when(referenceArmuresMock.getPtsArmure(0, 0, false)).thenReturn(3);
	when(referenceArmuresMock.getMalusEsquive(0, 0, false)).thenReturn(0);
	when(referenceArmuresMock.getMalusParade(0, 0, false)).thenReturn(0);
	when(referenceArmuresMock.getLocalisation(0, false)).thenReturn(0);
	when(referenceArmuresMock.getLblPiece(0, false)).thenReturn("heaume complet");
	when(referenceArmuresMock.getLblMateriauArmure(0, false)).thenReturn("plates");
	when(referenceArmuresMock.getLblTypeArmure(0)).thenReturn("ancienne");
	piece1 = new PieceArmure(0, 0, 0, false);

	when(referenceArmuresMock.getPtsArmure(0, 3, false)).thenReturn(1);
	when(referenceArmuresMock.getMalusEsquive(0, 3, false)).thenReturn(0);
	when(referenceArmuresMock.getMalusParade(0, 3, false)).thenReturn(0);
	when(referenceArmuresMock.getLblTypeArmure(1)).thenReturn("moderne");
	piece2 = new PieceArmure(0, 1, 3, false);

	when(referenceArmuresMock.getPtsArmure(7, 0, false)).thenReturn(6);
	when(referenceArmuresMock.getMalusEsquive(7, 0, false)).thenReturn(10);
	when(referenceArmuresMock.getMalusParade(7, 0, false)).thenReturn(0);
	when(referenceArmuresMock.getLblPiece(7, false)).thenReturn("cuirasse");
	when(referenceArmuresMock.getLblTypeArmure(2)).thenReturn("avec blindage");
	when(referenceArmuresMock.getLocalisation(7, false)).thenReturn(1);
	piece3 = new PieceArmure(7, 2, 0, false);

	when(referenceArmuresMock.getPtsArmure(7, 3, false)).thenReturn(2);
	when(referenceArmuresMock.getMalusEsquive(7, 3, false)).thenReturn(1);
	when(referenceArmuresMock.getMalusParade(7, 3, false)).thenReturn(0);
	when(referenceArmuresMock.getLblTypeArmure(3)).thenReturn("énergétique");
	piece4 = new PieceArmure(7, 3, 3, false);

	when(referenceArmuresMock.getPtsArmure(4, 1, false)).thenReturn(2);
	when(referenceArmuresMock.getMalusEsquive(4, 1, false)).thenReturn(0);
	when(referenceArmuresMock.getMalusParade(4, 1, false)).thenReturn(0);
	when(referenceArmuresMock.getLblPiece(4, false)).thenReturn("brassière");
	when(referenceArmuresMock.getLblMateriauArmure(1, false)).thenReturn("lamelles ou maille");
	when(referenceArmuresMock.getLocalisation(4, false)).thenReturn(2);
	piece5 = new PieceArmure(4, 0, 1, false);

	when(referenceArmuresMock.getPtsArmure(3, 1, true)).thenReturn(4);
	when(referenceArmuresMock.getMalusEsquive(3, 1, true)).thenReturn(0);
	when(referenceArmuresMock.getMalusParade(3, 1, true)).thenReturn(0);
	when(referenceArmuresMock.getLocalisation(3, true)).thenReturn(3);
	when(referenceArmuresMock.getLblPiece(3, true)).thenReturn("pavois");
	bouclier1 = new PieceArmure(3, 3, 1, true);

	when(referenceArmuresMock.getPtsArmure(0, 0, true)).thenReturn(2);
	when(referenceArmuresMock.getMalusEsquive(0, 0, true)).thenReturn(0);
	when(referenceArmuresMock.getMalusParade(0, 0, true)).thenReturn(0);
	when(referenceArmuresMock.getLblMateriauArmure(0, true)).thenReturn("métal");
	when(referenceArmuresMock.getLocalisation(0, true)).thenReturn(3);
	when(referenceArmuresMock.getLblPiece(0, true)).thenReturn("targe");
	bouclier2 = new PieceArmure(0, 0, 0, true);
    }

    @Test
    public void testMalusEsquive()
    {

	Assert.assertEquals(0, piece1.getMalusEsquive());
	Assert.assertEquals(0, piece2.getMalusEsquive());
	Assert.assertEquals(10, piece3.getMalusEsquive());
	Assert.assertEquals(1, piece4.getMalusEsquive());
	Assert.assertEquals(0, piece5.getMalusEsquive());
	Assert.assertEquals(0, bouclier1.getMalusEsquive());
	Assert.assertEquals(0, bouclier2.getMalusEsquive());
    }

    @Test
    public void testMalusParade()
    {

	Assert.assertEquals(0, piece1.getMalusParade());
	Assert.assertEquals(0, piece2.getMalusParade());
	Assert.assertEquals(0, piece3.getMalusParade());
	Assert.assertEquals(0, piece4.getMalusParade());
	Assert.assertEquals(0, piece5.getMalusParade());
	Assert.assertEquals(0, bouclier1.getMalusParade());
	Assert.assertEquals(0, bouclier2.getMalusParade());
    }

    @Test
    public void testPoints()
    {
	Assert.assertEquals(3, piece1.getNbpoints());
	Assert.assertEquals(1, piece2.getNbpoints());
	Assert.assertEquals(6, piece3.getNbpoints());
	Assert.assertEquals(2, piece4.getNbpoints());
	Assert.assertEquals(2, piece5.getNbpoints());
	Assert.assertEquals(4, bouclier1.getNbpoints());
	Assert.assertEquals(2, bouclier2.getNbpoints());
    }

    @Test
    public void testType()
    {

	Assert.assertEquals(0, piece1.getType());
	Assert.assertEquals(1, piece2.getType());
	Assert.assertEquals(2, piece3.getType());
	Assert.assertEquals(3, piece4.getType());
	Assert.assertEquals(0, piece5.getType());
	Assert.assertEquals(3, bouclier1.getType());
	Assert.assertEquals(0, bouclier2.getType());
    }

    @Test
    public void testLocalisation()
    {

	Assert.assertEquals(0, piece1.getLocalisation());
	Assert.assertEquals(0, piece2.getLocalisation());
	Assert.assertEquals(1, piece3.getLocalisation());
	Assert.assertEquals(1, piece4.getLocalisation());
	Assert.assertEquals(2, piece5.getLocalisation());
	Assert.assertEquals(3, bouclier1.getLocalisation());
	Assert.assertEquals(3, bouclier2.getLocalisation());
    }

    @Test
    public void testLibelle()
    {
	Assert.assertEquals("heaume complet en plates", piece1.toString());
	Assert.assertEquals("heaume complet moderne", piece2.toString());
	Assert.assertEquals("cuirasse avec blindage", piece3.toString());
	Assert.assertEquals("cuirasse énergétique", piece4.toString());
	Assert.assertEquals("brassière en lamelles ou maille", piece5.toString());
	Assert.assertEquals("pavois énergétique", bouclier1.toString());
	Assert.assertEquals("targe en métal", bouclier2.toString());
    }
}
