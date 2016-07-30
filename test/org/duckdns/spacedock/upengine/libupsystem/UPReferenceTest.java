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
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public final class UPReferenceTest
{

    private static UPReference m_reference;

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
	Assert.assertEquals(0, m_reference.getArmureRedDegats(0, 0, 0));
	Assert.assertEquals(0, m_reference.getArmureRedDegats(2, 4, 3));
	Assert.assertEquals(15, m_reference.getArmureRedDegats(8, 0, 3));
	Assert.assertEquals(10, m_reference.getArmureRedDegats(103, 4, 0));//arrondi 103/5 = 21
	Assert.assertEquals(5, m_reference.getArmureRedDegats(21, 2, 1));//arrondi 21/2 = 11
	Assert.assertEquals(10, m_reference.getArmureRedDegats(32, 3, 2));
	Assert.assertEquals(5, m_reference.getArmureRedDegats(19, 3, 1));//arrondi 19/3 = 6
    }

    @Test
    public void testGetBonusNDNominal()
    {
	Assert.assertEquals(0, m_reference.getArmureBonusND(0, 0, 0));
	Assert.assertEquals(5, m_reference.getArmureBonusND(2, 4, 3));
	Assert.assertEquals(15, m_reference.getArmureBonusND(8, 0, 3));
	Assert.assertEquals(15, m_reference.getArmureBonusND(103, 4, 0));//arrondi 103/5 = 21
	Assert.assertEquals(10, m_reference.getArmureBonusND(21, 2, 1));//arrondi 21/2 = 11
	Assert.assertEquals(10, m_reference.getArmureBonusND(32, 3, 2));
	Assert.assertEquals(5, m_reference.getArmureBonusND(19, 3, 1));//arrondi 19/3 = 6
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
	Assert.assertEquals(2, m_reference.getNbLancesArme(0));
	Assert.assertEquals(3, m_reference.getNbLancesArme(20));
        Assert.assertEquals(2, m_reference.getNbLancesArme(42));
    }

    @Test
    public void testGetNbGardesArme()
    {
	Assert.assertEquals(3, m_reference.getNbGardesArme(0));
	Assert.assertEquals(3, m_reference.getNbGardesArme(20));
        Assert.assertEquals(2, m_reference.getNbGardesArme(42));
    }

    @Test
    public void testGetBonusInitArme()
    {
	Assert.assertEquals(0, m_reference.getBonusInitArme(0));
	Assert.assertEquals(0, m_reference.getBonusInitArme(20));
        Assert.assertEquals(2, m_reference.getBonusInitArme(42));
    }

    @Test
    public void testGetMalusAttaqueArme()
    {
	Assert.assertEquals(0, m_reference.getMalusAttaqueArme(0));
	Assert.assertEquals(0, m_reference.getMalusAttaqueArme(20));
        Assert.assertEquals(0, m_reference.getMalusAttaqueArme(42));
    }

    @Test
    public void testGetPhysMinArme()
    {
	Assert.assertEquals(0, m_reference.getPhysMinArme(0));
	Assert.assertEquals(0, m_reference.getPhysMinArme(20));
        Assert.assertEquals(0, m_reference.getPhysMinArme(42));
    }

    @Test
    public void testGetCategorieArme()
    {
	Assert.assertEquals(1, m_reference.getCategorieArme(0));
	Assert.assertEquals(3, m_reference.getCategorieArme(20));
        Assert.assertEquals(5, m_reference.getCategorieArme(42));
	Assert.assertEquals(0, m_reference.getCategorieArme(60));
    }

    @Test
    public void testGetTypeArme()
    {
	Assert.assertEquals(0, m_reference.getTypeArme(0));
	Assert.assertEquals(0, m_reference.getTypeArme(20));
        Assert.assertEquals(0, m_reference.getTypeArme(42));
        Assert.assertEquals(1, m_reference.getTypeArme(73));
    }

    @Test
    public void testGetLblTypeArme()
    {
	Assert.assertEquals("simple", m_reference.getLblTypeArme(0));
	Assert.assertEquals("perce-armure", m_reference.getLblTypeArme(1));
    }

    @Test
    public void testGetModArme()
    {
	Assert.assertEquals(0, m_reference.getModArme(0));
	Assert.assertEquals(1, m_reference.getModArme(60));
    }

    @Test
    public void testGetLblModArme()
    {
	Assert.assertEquals("corps à corps", m_reference.getLblModArme(0));
	Assert.assertEquals("distance", m_reference.getLblModArme(1));
    }

    @Test
    public void testGetLblCatArmeCaC()
    {
	Assert.assertEquals("mains nues", m_reference.getLblCatArmeCaC(0));
	Assert.assertEquals("armes à lame longue", m_reference.getLblCatArmeCaC(2));
    }

    @Test
    public void testGetLblCatArmeDist()
    {
	Assert.assertEquals("armes lourdes", m_reference.getLblCatArmeDist(4));
	Assert.assertEquals("armes à énergie", m_reference.getLblCatArmeDist(2));
    }

    @Test
    public void testGetMalusCourtArme()
    {
	Assert.assertEquals(5, m_reference.getMalusCourtArme(73));
    }

    @Test
    public void testGetMalusLongArme()
    {
	Assert.assertEquals(10, m_reference.getMalusLongArme(73));
    }

    @Test
    public void testGetPorteeArme()
    {
	Assert.assertEquals(300, m_reference.getPorteeArme(73));
    }

    @Test
    public void testGetNbActionsRechargeArme()
    {
	Assert.assertEquals(1, m_reference.getNbActionsRechargeArme(73));
    }

    @Test
    public void testGetMagasinArme()
    {
	Assert.assertEquals(1, m_reference.getNbActionsRechargeArme(73));
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
	ArrayList<String> dist = new ArrayList<>();
	ArrayList<String> social = new ArrayList<>();

	analyse.add("empathie");
	analyse.add("investigation");
	analyse.add("politique");

	cac.add("mains nues");
	cac.add("armes d'escrime");
	cac.add("armes à lame longue");
	cac.add("haches");
	cac.add("armes contondantes");
	cac.add("petites armes");
	cac.add("armes d'hast");
        cac.add("armes à chaînes");

	dist.add("armes de jet");
	dist.add("armes à feu");
	dist.add("armes à énergie");
	dist.add("armes automatiques");
	dist.add("armes lourdes");

	social.add("art oratoire");
	social.add("étiquette");
	social.add("séduction");
	social.add("subterfuge");

	Assert.assertEquals(analyse, m_reference.getListComp(0));
	Assert.assertEquals(cac, m_reference.getListComp(3));
	Assert.assertEquals(dist, m_reference.getListComp(4));
	Assert.assertEquals(social, m_reference.getListComp(8));
    }

    @Test
    public void testGetLblDomaine()
    {
	Assert.assertEquals("analyse", m_reference.getLblDomaine(0));
	Assert.assertEquals("social", m_reference.getLblDomaine(8));
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
