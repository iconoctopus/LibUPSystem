/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
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
	Assert.assertEquals(/*5*/6, m_reference.getInitModCoord(6));
	Assert.assertEquals(3, m_reference.getInitModCoord(3));
	Assert.assertEquals(2, m_reference.getInitModCoord(2));
	Assert.assertEquals(6, m_reference.getInitModCoord(7));
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
    public void testGetPointsArmureEffectifsNominal()
    {
	Assert.assertEquals(2, m_reference.getPtsArmureEffectifs(4, 3, 4), 0);
	Assert.assertEquals(10, m_reference.getPtsArmureEffectifs(0, 0, 10), 0);
	Assert.assertEquals(8, m_reference.getPtsArmureEffectifs(0, 3, 2), 0);
	Assert.assertEquals(1, m_reference.getPtsArmureEffectifs(4, 0, 7), 0);
	Assert.assertEquals(2, m_reference.getPtsArmureEffectifs(2, 1, 3), 0);
	Assert.assertEquals(3, m_reference.getPtsArmureEffectifs(3, 2, 6), 0);
	Assert.assertEquals(3, m_reference.getPtsArmureEffectifs(3, 1, 9), 0);
    }

    @Test
    public void testGetLibelleTraitNominal()
    {
	Assert.assertEquals("physique", m_reference.getLibelleTrait(0));
	Assert.assertEquals("mental", m_reference.getLibelleTrait(2));
	Assert.assertEquals("sociabilité", m_reference.getLibelleTrait(4));

    }

    @Test
    public void testGetNbLancesArme()
    {
	Assert.assertEquals(2, m_reference.getNbLancesArmeCac(0));
	Assert.assertEquals(6, m_reference.getNbLancesArmeCac(1));
    }

    @Test
    public void testGetNbGardesArme()
    {
	Assert.assertEquals(3, m_reference.getNbGardesArmeCac(0));
	Assert.assertEquals(3, m_reference.getNbGardesArmeCac(1));
    }

    @Test
    public void testGetBonusInitArme()
    {
	Assert.assertEquals(1, m_reference.getBonusInitArmeCac(0));
	Assert.assertEquals(0, m_reference.getBonusInitArmeCac(1));
    }

    @Test
    public void testGetMalusAttaqueArme()
    {
	Assert.assertEquals(0, m_reference.getMalusAttaqueArmeCac(0));
	Assert.assertEquals(0, m_reference.getMalusAttaqueArmeCac(1));
    }

    @Test
    public void testGetPhysMinArme()
    {
	Assert.assertEquals(0, m_reference.getPhysMinArmeCac(0));
	Assert.assertEquals(2, m_reference.getPhysMinArmeCac(1));
    }

    @Test
    public void testGetCategorieArme()
    {
	Assert.assertEquals(1, m_reference.getCategorieArmeCac(0));
	Assert.assertEquals(3, m_reference.getCategorieArmeCac(1));
    }

    @Test
    public void testGetTypeArme()
    {
	Assert.assertEquals(0, m_reference.getTypeArmeCac(0));
	Assert.assertEquals(0, m_reference.getTypeArmeCac(1));
    }

    @Test
    public void testGetLblTypeArme()
    {
	Assert.assertEquals("simple", m_reference.getLblTypeArmeCac(0));
	Assert.assertEquals("perce-armure", m_reference.getLblTypeArmeCac(1));
    }

    @Test
    public void testGetListCatArmeCac()
    {
	ArrayList<String> listTest = new ArrayList<>();
	listTest.add("mains nues");
	listTest.add("armes d'escrime");
	listTest.add("armes à lame longue");
	listTest.add("haches");
	listTest.add("armes contondantes");
	listTest.add("petites armes");
	listTest.add("armes d'hast");

	Assert.assertEquals(listTest, m_reference.getListCatArmeCac());
    }

    @Test
    public void testGetNbPointsBouclier()
    {
	Assert.assertEquals(2, m_reference.getNbPointsBouclier(0, 0));
	Assert.assertEquals(1, m_reference.getNbPointsBouclier(0, 1));
	Assert.assertEquals(5, m_reference.getNbPointsBouclier(3, 0));
	Assert.assertEquals(4, m_reference.getNbPointsBouclier(3, 1));
    }

    @Test
    public void testGetListComp()
    {
	ArrayList<String> analyse = new ArrayList<>();
	ArrayList<String> cac = new ArrayList<>();
	ArrayList<String> social = new ArrayList<>();

	analyse.add("empathie");
	analyse.add("investigation");
	analyse.add("politique");

	social.add("art oratoire");
	social.add("étiquette");
	social.add("séduction");
	social.add("subterfuge");

	Assert.assertEquals(analyse, m_reference.getListComp(0));
	Assert.assertEquals(cac, m_reference.getListComp(3));
	Assert.assertEquals(social, m_reference.getListComp(8));
    }

    @Test
    public void testGetLblDomaine()
    {
	Assert.assertEquals("analyse", m_reference.getLblDomaine(0));
	Assert.assertEquals("social", m_reference.getLblDomaine(8));
    }

    @Test
    public void testGetLblComp()
    {
	Assert.assertEquals("culture", m_reference.getLblComp(5, 0));
	Assert.assertEquals("occultisme", m_reference.getLblComp(5, 1));
	Assert.assertEquals("sciences", m_reference.getLblComp(5, 2));
	Assert.assertEquals("stratégie", m_reference.getLblComp(5, 3));
    }

    @Test
    public void testGetPtsArmure()
    {
	Assert.assertEquals(3, m_reference.getPtsArmure(0, 0));
	Assert.assertEquals(1, m_reference.getPtsArmure(0, 3));
	Assert.assertEquals(6, m_reference.getPtsArmure(7, 0));
	Assert.assertEquals(2, m_reference.getPtsArmure(7, 3));
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
	Assert.assertEquals("heaume complet", m_reference.getLblPiece(0));
	Assert.assertEquals("cuirasse", m_reference.getLblPiece(7));
    }

    @Test
    public void testGetMalusEsquive()
    {
	Assert.assertEquals(0, m_reference.getMalusEsquive(0, 0));
	Assert.assertEquals(0, m_reference.getMalusEsquive(0, 3));
	Assert.assertEquals(10, m_reference.getMalusEsquive(7, 0));
	Assert.assertEquals(1, m_reference.getMalusEsquive(7, 3));
    }

    @Test
    public void testGetMalusParade()
    {
	Assert.assertEquals(0, m_reference.getMalusParade(0, 0));
	Assert.assertEquals(0, m_reference.getMalusParade(0, 3));
	Assert.assertEquals(0, m_reference.getMalusParade(7, 0));
	Assert.assertEquals(0, m_reference.getMalusParade(7, 3));
	Assert.assertEquals(3, m_reference.getMalusParade(4, 0));
	Assert.assertEquals(2, m_reference.getMalusParade(4, 2));
    }

    @Test
    public void testGetNbMaxPieces()
    {
	Assert.assertEquals(1, m_reference.getNbMaxPieces(0));
	Assert.assertEquals(1, m_reference.getNbMaxPieces(7));
	Assert.assertEquals(2, m_reference.getNbMaxPieces(4));
	Assert.assertEquals(2, m_reference.getNbMaxPieces(5));
    }
}
