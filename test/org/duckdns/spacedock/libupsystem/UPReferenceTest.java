/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.libupsystem;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author iconoctopus
 */
public final class UPReferenceTest
{

    private static UPReference m_reference;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass()
    {
	m_reference = UPReference.getInstance();
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Test
    public void testGetInitModCoordNominal()
    {
	Assert.assertEquals(5, m_reference.getInitModCoord(6));
	Assert.assertEquals(3, m_reference.getInitModCoord(3));
	Assert.assertEquals(2, m_reference.getInitModCoord(2));
	Assert.assertEquals(6, m_reference.getInitModCoord(7));
    }

    @Test
    public void testGetInitModCoordErreur()
    {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("paramétre aberrant:trait:-1");
	m_reference.getInitModCoord(-1);
	thrown.expectMessage("paramétre aberrant:trait:0");
	m_reference.getInitModCoord(0);
	thrown.expectMessage("paramétre aberrant:trait:8");
	m_reference.getInitModCoord(8);
    }

    @Test
    public void testGetInitModMentalNominal()
    {
	Assert.assertEquals(2, m_reference.getInitModMental(6));
	Assert.assertEquals(0, m_reference.getInitModMental(3));
	Assert.assertEquals(0, m_reference.getInitModMental(2));
	Assert.assertEquals(1, m_reference.getInitModMental(5));
	Assert.assertEquals(2, m_reference.getInitModMental(7));
    }

    @Test
    public void testGetInitModMentalErreur()
    {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("paramétre aberrant:trait:-1");
	m_reference.getInitModMental(-1);
	thrown.expectMessage("paramétre aberrant:trait:0");
	m_reference.getInitModMental(0);
	thrown.expectMessage("paramétre aberrant:trait:8");
	m_reference.getInitModMental(8);
    }

    @Test
    public void testGetRedDegatsNominal()
    {
	Assert.assertEquals(0, m_reference.getArmureRedDegats(0));
	Assert.assertEquals(0, m_reference.getArmureRedDegats(1));
	Assert.assertEquals(15, m_reference.getArmureRedDegats(32));
	Assert.assertEquals(10, m_reference.getArmureRedDegats(25));
	Assert.assertEquals(5, m_reference.getArmureRedDegats(11));
	Assert.assertEquals(10, m_reference.getArmureRedDegats(16));
	Assert.assertEquals(5, m_reference.getArmureRedDegats(6));
    }

    @Test
    public void testGetRedDegatsErreur()
    {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("paramétre aberrant:points d'armure:-1");
	m_reference.getArmureRedDegats(-1);
    }

    @Test
    public void testGetBonusNDNominal()
    {
	Assert.assertEquals(0, m_reference.getArmureBonusND(0));
	Assert.assertEquals(5, m_reference.getArmureBonusND(1));
	Assert.assertEquals(15, m_reference.getArmureBonusND(32));
	Assert.assertEquals(15, m_reference.getArmureBonusND(25));
	Assert.assertEquals(10, m_reference.getArmureBonusND(11));
	Assert.assertEquals(10, m_reference.getArmureBonusND(16));
	Assert.assertEquals(5, m_reference.getArmureBonusND(6));
    }

    @Test
    public void testGetBonusNDErreur()
    {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("paramétre aberrant:points d'armure:-1");
	m_reference.getArmureBonusND(-1);
    }

    @Test
    public void testGetPointsArmureEffectifsNominal()
    {
	Assert.assertEquals(0.5, m_reference.getCoeffArmeArmure(4, 3), 0);
	Assert.assertEquals(1, m_reference.getCoeffArmeArmure(0, 0), 0);
	Assert.assertEquals(4, m_reference.getCoeffArmeArmure(0, 3), 0);
	Assert.assertEquals(0.2, m_reference.getCoeffArmeArmure(4, 0), 0);
	Assert.assertEquals(0.5, m_reference.getCoeffArmeArmure(2, 1), 0);
	Assert.assertEquals(0.5, m_reference.getCoeffArmeArmure(3, 2), 0);
	Assert.assertEquals(0.33, m_reference.getCoeffArmeArmure(3, 1), 0);
    }

    @Test
    public void testGetPointsArmureEffectifsErreur()
    {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("paramétre aberrant:type d'arme:-1 type d'armure:0");
	m_reference.getCoeffArmeArmure(-1, 0);
	thrown.expectMessage("paramétre aberrant:type d'arme:-1 type d'armure:0");
	m_reference.getCoeffArmeArmure(0, -1);
    }

    @Test
    public void testGetLibelleTraitNominal()
    {
	Assert.assertEquals("physique", m_reference.getLibelleTrait(0));
	Assert.assertEquals("mental", m_reference.getLibelleTrait(2));
	Assert.assertEquals("sociabilité", m_reference.getLibelleTrait(4));

    }

    @Test
    public void testGetLibelleTrait()
    {
	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("paramétre aberrant:indice:-1");
	m_reference.getLibelleTrait(-1);
    }
}
