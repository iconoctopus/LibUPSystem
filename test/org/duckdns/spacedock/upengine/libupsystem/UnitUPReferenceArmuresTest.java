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

/**
 *
 * @author ykonoclast
 */
public class UnitUPReferenceArmuresTest
{

    private static UPReferenceArmures m_reference;

    @BeforeClass
    public static void setUpClass()
    {
	m_reference = UPReferenceArmures.getInstance();
    }

    @Test
    public void testGetRedDegats()
    {
	Assert.assertEquals(0, m_reference.getRedDegats(0, 0, 0));
	Assert.assertEquals(0, m_reference.getRedDegats(2, 4, 3));//2/2 = 1
	Assert.assertEquals(5, m_reference.getRedDegats(8, 4, 3));//8/2 = 4
	Assert.assertEquals(5, m_reference.getRedDegats(12, 4, 3));//12/2 = 6
	Assert.assertEquals(15, m_reference.getRedDegats(8, 0, 3));
	Assert.assertEquals(15, m_reference.getRedDegats(138, 4, 0));//arrondi 138/5 = 27
	Assert.assertEquals(15, m_reference.getRedDegats(103, 4, 0));//arrondi 103/5 = 21
	Assert.assertEquals(10, m_reference.getRedDegats(87, 4, 0));//arrondi 88/5 = 18
	Assert.assertEquals(5, m_reference.getRedDegats(21, 2, 1));//arrondi 21/2 = 11
	Assert.assertEquals(10, m_reference.getRedDegats(32, 3, 2));
	Assert.assertEquals(5, m_reference.getRedDegats(19, 3, 1));//arrondi 19/3 = 6
    }

    @Test
    public void testGetBonusND()
    {
	Assert.assertEquals(0, m_reference.getBonusND(0, 0, 0));
	Assert.assertEquals(0, m_reference.getBonusND(2, 4, 3));//2/2 = 1
	Assert.assertEquals(0, m_reference.getBonusND(8, 4, 3));//8/2 = 1
	Assert.assertEquals(5, m_reference.getBonusND(12, 4, 3));//12/2 = 6
	Assert.assertEquals(15, m_reference.getBonusND(8, 0, 3));
	Assert.assertEquals(15, m_reference.getBonusND(138, 4, 0));//arrondi 138/5 = 27
	Assert.assertEquals(10, m_reference.getBonusND(103, 4, 0));//arrondi 103/5 = 21
	Assert.assertEquals(10, m_reference.getBonusND(88, 4, 0));//arrondi 88/5 = 18
	Assert.assertEquals(10, m_reference.getBonusND(21, 2, 1));//arrondi 21/2 = 11
	Assert.assertEquals(10, m_reference.getBonusND(32, 3, 2));
	Assert.assertEquals(5, m_reference.getBonusND(19, 3, 1));//arrondi 19/3 = 6
    }

    @Test
    public void testGetPointsBouclier()
    {
	Assert.assertEquals(2, m_reference.getPtsArmure(0, 0, true));
	Assert.assertEquals(1, m_reference.getPtsArmure(0, 1, true));
	Assert.assertEquals(5, m_reference.getPtsArmure(3, 0, true));
	Assert.assertEquals(4, m_reference.getPtsArmure(3, 1, true));
    }

    @Test
    public void testGetLblBouclier()
    {
	Assert.assertEquals("targe", m_reference.getLblPiece(0, true));
	Assert.assertEquals("bouclier", m_reference.getLblPiece(1, true));
	Assert.assertEquals("écu", m_reference.getLblPiece(2, true));
	Assert.assertEquals("pavois", m_reference.getLblPiece(3, true));
    }

    @Test
    public void testGetPtsArmure()
    {
	Assert.assertEquals(3, m_reference.getPtsArmure(0, 0, false));
	Assert.assertEquals(1, m_reference.getPtsArmure(0, 3, false));
	Assert.assertEquals(6, m_reference.getPtsArmure(7, 0, false));
	Assert.assertEquals(2, m_reference.getPtsArmure(7, 3, false));
    }

    @Test
    public void testGetLblTypeArmure()
    {
	Assert.assertEquals("ancienne", m_reference.getLblTypeArmure(0));
	Assert.assertEquals("énergétique", m_reference.getLblTypeArmure(3));
    }

    @Test
    public void testGetLblPiece()
    {
	Assert.assertEquals("heaume complet", m_reference.getLblPiece(0, false));
	Assert.assertEquals("cuirasse", m_reference.getLblPiece(7, false));
    }

    @Test
    public void testGetMalusEsquive()
    {
	Assert.assertEquals(0, m_reference.getMalusEsquive(0, 0, false));
	Assert.assertEquals(0, m_reference.getMalusEsquive(0, 0, true));
	Assert.assertEquals(0, m_reference.getMalusEsquive(0, 3, false));
	Assert.assertEquals(10, m_reference.getMalusEsquive(7, 0, false));
	Assert.assertEquals(1, m_reference.getMalusEsquive(7, 3, false));
	Assert.assertEquals(0, m_reference.getMalusEsquive(1, 1, true));
    }

    @Test
    public void testGetMalusParade()
    {
	Assert.assertEquals(0, m_reference.getMalusParade(0, 0, false));
	Assert.assertEquals(0, m_reference.getMalusParade(0, 3, false));
	Assert.assertEquals(0, m_reference.getMalusParade(7, 0, false));
	Assert.assertEquals(0, m_reference.getMalusParade(7, 3, false));
	Assert.assertEquals(0, m_reference.getMalusParade(4, 0, false));
	Assert.assertEquals(0, m_reference.getMalusParade(4, 2, false));
    }

    @Test
    public void testGetLocalisation()
    {
	Assert.assertEquals(0, m_reference.getLocalisation(0, false));
	Assert.assertEquals(1, m_reference.getLocalisation(7, false));
	Assert.assertEquals(2, m_reference.getLocalisation(4, false));
	Assert.assertEquals(4, m_reference.getLocalisation(5, false));
	Assert.assertEquals(3, m_reference.getLocalisation(0, true));
    }

    @Test
    public void testGetLblLoca()
    {
	Assert.assertEquals("tête", m_reference.getLblLoca(0));
	Assert.assertEquals("main", m_reference.getLblLoca(3));
    }

    public void testGetListPieceArmure()
    {
	Assert.assertEquals("masque facial", m_reference.getListPieceArmure().get(2));
	Assert.assertEquals("jambière", m_reference.getListPieceArmure().get(5));
    }

    public void testGetListMateriauArmure()
    {
	Assert.assertEquals("lamelles ou maille", m_reference.getListMateriauArmure().get(1));
	Assert.assertEquals("cuir clouté", m_reference.getListMateriauArmure().get(2));
    }

    public void testGetListTypeArmure()
    {
	Assert.assertEquals("moderne", m_reference.getListTypeArmure().get(1));
	Assert.assertEquals("avec blindage", m_reference.getListTypeArmure().get(2));
    }

    public void testGetListMateriauBouclier()
    {
	Assert.assertEquals("métal", m_reference.getListMateriauBouclier().get(1));
	Assert.assertEquals("bois", m_reference.getListMateriauBouclier().get(0));
    }

    public void testGetListBouclier()
    {
	Assert.assertEquals("bouclier", m_reference.getListBouclier().get(1));
	Assert.assertEquals("targe", m_reference.getListBouclier().get(0));
    }
}
