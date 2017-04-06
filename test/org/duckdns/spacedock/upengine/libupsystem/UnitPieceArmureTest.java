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
    private static UPReferenceArmures referenceArmuresMock;

    @BeforeClass
    public static void setUpClass()
    {
	//on mocke la référence
	referenceArmuresMock = PowerMockito.mock(UPReferenceArmures.class);
	PowerMockito.mockStatic(UPReferenceArmures.class);
	when(UPReferenceArmures.getInstance()).thenReturn(referenceArmuresMock);

	//On crée les pièces de test
	when(referenceArmuresMock.getPtsPiece(0, 0)).thenReturn(3);
	when(referenceArmuresMock.getLocalisation(0)).thenReturn(0);
	when(referenceArmuresMock.getLblPiece(0)).thenReturn("heaume complet");
	when(referenceArmuresMock.getLblMateriauArmureAncienne(0)).thenReturn("plates");
	when(referenceArmuresMock.getLblTypeArmure(0)).thenReturn("ancienne");
	piece1 = new PieceArmure(0, 0, 0);

	when(referenceArmuresMock.getPtsPiece(0, 3)).thenReturn(1);
	when(referenceArmuresMock.getLblTypeArmure(1)).thenReturn("moderne");
	when(referenceArmuresMock.getLblMateriauArmure(3)).thenReturn("légère");
	piece2 = new PieceArmure(0, 1, 3);

	when(referenceArmuresMock.getPtsPiece(7, 0)).thenReturn(6);
	when(referenceArmuresMock.getLblPiece(7)).thenReturn("cuirasse");
	when(referenceArmuresMock.getLblTypeArmure(2)).thenReturn("avec blindage");
	when(referenceArmuresMock.getLocalisation(7)).thenReturn(1);
	when(referenceArmuresMock.getLblMateriauArmure(0)).thenReturn("très lourde");
	piece3 = new PieceArmure(7, 2, 0);

	when(referenceArmuresMock.getPtsPiece(7, 2)).thenReturn(3);
	when(referenceArmuresMock.getLblTypeArmure(3)).thenReturn("énergétique");
	when(referenceArmuresMock.getLblMateriauArmure(2)).thenReturn("moyenne");
	piece4 = new PieceArmure(7, 3, 2);

	when(referenceArmuresMock.getPtsPiece(4, 1)).thenReturn(2);
	when(referenceArmuresMock.getLblPiece(4)).thenReturn("brassière");
	when(referenceArmuresMock.getLblMateriauArmureAncienne(1)).thenReturn("lamelles ou maille");
	when(referenceArmuresMock.getLocalisation(4)).thenReturn(2);
	piece5 = new PieceArmure(4, 0, 1);
    }

    @Test
    public void testPoints()
    {
	Assert.assertEquals(3, piece1.getNbPoints());
	Assert.assertEquals(1, piece2.getNbPoints());
	Assert.assertEquals(6, piece3.getNbPoints());
	Assert.assertEquals(3, piece4.getNbPoints());
	Assert.assertEquals(2, piece5.getNbPoints());
    }

    @Test
    public void testType()
    {

	Assert.assertEquals(0, piece1.getType());
	Assert.assertEquals(1, piece2.getType());
	Assert.assertEquals(2, piece3.getType());
	Assert.assertEquals(3, piece4.getType());
	Assert.assertEquals(0, piece5.getType());
    }

    @Test
    public void testLocalisation()
    {

	Assert.assertEquals(0, piece1.getLocalisation());
	Assert.assertEquals(0, piece2.getLocalisation());
	Assert.assertEquals(1, piece3.getLocalisation());
	Assert.assertEquals(1, piece4.getLocalisation());
	Assert.assertEquals(2, piece5.getLocalisation());
    }

    @Test
    public void testLibelle()
    {
	Assert.assertEquals("heaume complet en plates", piece1.toString());
	Assert.assertEquals("heaume complet moderne de facture légère", piece2.toString());
	Assert.assertEquals("cuirasse avec blindage de facture très lourde", piece3.toString());
	Assert.assertEquals("cuirasse énergétique de facture moyenne", piece4.toString());
	Assert.assertEquals("brassière en lamelles ou maille", piece5.toString());
    }
}
