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
    public void testGetInitModCoord()
    {
	Assert.assertEquals(5, m_reference.getInitModCoord(6));
	Assert.assertEquals(3, m_reference.getInitModCoord(3));
	Assert.assertEquals(2, m_reference.getInitModCoord(2));
	Assert.assertEquals(6, m_reference.getInitModCoord(7));
    }

    @Test
    public void testGetInitModMental()
    {
	Assert.assertEquals(2, m_reference.getInitModMental(6));
	Assert.assertEquals(0, m_reference.getInitModMental(3));
	Assert.assertEquals(0, m_reference.getInitModMental(2));
	Assert.assertEquals(1, m_reference.getInitModMental(5));
	Assert.assertEquals(2, m_reference.getInitModMental(7));
    }

    @Test
    public void testGetRedDegats()
    {
	Assert.assertEquals(0, m_reference.getArmureRedDegats(0, 0, 0));
	Assert.assertEquals(0, m_reference.getArmureRedDegats(2, 4, 3));//2/2 = 1
	Assert.assertEquals(5, m_reference.getArmureRedDegats(8, 4, 3));//8/2 = 4
	Assert.assertEquals(5, m_reference.getArmureRedDegats(12, 4, 3));//12/2 = 6
	Assert.assertEquals(15, m_reference.getArmureRedDegats(8, 0, 3));
	Assert.assertEquals(15, m_reference.getArmureRedDegats(138, 4, 0));//arrondi 138/5 = 27
	Assert.assertEquals(15, m_reference.getArmureRedDegats(103, 4, 0));//arrondi 103/5 = 21
	Assert.assertEquals(10, m_reference.getArmureRedDegats(87, 4, 0));//arrondi 88/5 = 18
	Assert.assertEquals(5, m_reference.getArmureRedDegats(21, 2, 1));//arrondi 21/2 = 11
	Assert.assertEquals(10, m_reference.getArmureRedDegats(32, 3, 2));
	Assert.assertEquals(5, m_reference.getArmureRedDegats(19, 3, 1));//arrondi 19/3 = 6
    }

    @Test
    public void testGetBonusND()
    {
	Assert.assertEquals(0, m_reference.getArmureBonusND(0, 0, 0));
	Assert.assertEquals(0, m_reference.getArmureBonusND(2, 4, 3));//2/2 = 1
	Assert.assertEquals(0, m_reference.getArmureBonusND(8, 4, 3));//8/2 = 1
	Assert.assertEquals(5, m_reference.getArmureBonusND(12, 4, 3));//12/2 = 6
	Assert.assertEquals(15, m_reference.getArmureBonusND(8, 0, 3));
	Assert.assertEquals(15, m_reference.getArmureBonusND(138, 4, 0));//arrondi 138/5 = 27
	Assert.assertEquals(10, m_reference.getArmureBonusND(103, 4, 0));//arrondi 103/5 = 21
	Assert.assertEquals(10, m_reference.getArmureBonusND(88, 4, 0));//arrondi 88/5 = 18
	Assert.assertEquals(10, m_reference.getArmureBonusND(21, 2, 1));//arrondi 21/2 = 11
	Assert.assertEquals(10, m_reference.getArmureBonusND(32, 3, 2));
	Assert.assertEquals(5, m_reference.getArmureBonusND(19, 3, 1));//arrondi 19/3 = 6
    }

    @Test
    public void testGetLibelleTrait()
    {
	Assert.assertEquals("physique", m_reference.getLibelleTrait(0));
	Assert.assertEquals("mental", m_reference.getLibelleTrait(2));
	Assert.assertEquals("sociabilité", m_reference.getLibelleTrait(4));

    }

    @Test
    public void testGetNbLancesArme()
    {
	Assert.assertEquals(4, m_reference.getNbLancesArme(0));
	Assert.assertEquals(2, m_reference.getNbLancesArme(20));
	Assert.assertEquals(4, m_reference.getNbLancesArme(23));
	Assert.assertEquals(5, m_reference.getNbLancesArme(42));
    }

    @Test
    public void testGetNbGardesArme()
    {
	Assert.assertEquals(3, m_reference.getNbGardesArme(0));
	Assert.assertEquals(2, m_reference.getNbGardesArme(20));
	Assert.assertEquals(2, m_reference.getNbGardesArme(23));
	Assert.assertEquals(4, m_reference.getNbGardesArme(42));
    }

    @Test
    public void testGetBonusInitArme()
    {
	Assert.assertEquals(0, m_reference.getBonusInitArme(0));
	Assert.assertEquals(2, m_reference.getBonusInitArme(20));
	Assert.assertEquals(2, m_reference.getBonusInitArme(23));
	Assert.assertEquals(1, m_reference.getBonusInitArme(42));
    }

    @Test
    public void testGetMalusAttaqueArme()
    {
	Assert.assertEquals(1, m_reference.getMalusAttaqueArme(0));
	Assert.assertEquals(0, m_reference.getMalusAttaqueArme(20));
	Assert.assertEquals(1, m_reference.getMalusAttaqueArme(23));
	Assert.assertEquals(0, m_reference.getMalusAttaqueArme(42));
    }

    @Test
    public void testGetPhysMinArme()
    {
	Assert.assertEquals(2, m_reference.getPhysMinArme(0));
	Assert.assertEquals(0, m_reference.getPhysMinArme(20));
	Assert.assertEquals(0, m_reference.getPhysMinArme(23));
	Assert.assertEquals(0, m_reference.getPhysMinArme(42));
    }

    @Test
    public void testGetCategorieArme()
    {
	Assert.assertEquals(1, m_reference.getCategorieArme(0));
	Assert.assertEquals(3, m_reference.getCategorieArme(8));
	Assert.assertEquals(5, m_reference.getCategorieArme(20));
	Assert.assertEquals(5, m_reference.getCategorieArme(23));
	Assert.assertEquals(2, m_reference.getCategorieArme(42));
	Assert.assertEquals(5, m_reference.getCategorieArme(49));
    }

    @Test
    public void testGetTypeArme()
    {
	Assert.assertEquals(0, m_reference.getTypeArme(0));
	Assert.assertEquals(0, m_reference.getTypeArme(20));
	Assert.assertEquals(0, m_reference.getTypeArme(23));
	Assert.assertEquals(1, m_reference.getTypeArme(42));
	Assert.assertEquals(1, m_reference.getTypeArme(38));
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
	Assert.assertEquals(1, m_reference.getModArme(39));
    }

    @Test
    public void testGetNbMainsArme()
    {
	Assert.assertEquals(1, m_reference.getNbMainsArme(4));
	Assert.assertEquals(2, m_reference.getNbMainsArme(14));
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
	Assert.assertEquals("armes lourdes", m_reference.getLblCatArmeDist(5));
	Assert.assertEquals("armes à énergie", m_reference.getLblCatArmeDist(3));
    }

    @Test
    public void testGetMalusCourtArme()
    {
	Assert.assertEquals(5, m_reference.getMalusCourtArme(38));
    }

    @Test
    public void testGetMalusLongArme()
    {
	Assert.assertEquals(10, m_reference.getMalusLongArme(38));
    }

    @Test
    public void testGetPorteeArme()
    {
	Assert.assertEquals(300, m_reference.getPorteeArme(38));
    }

    @Test
    public void testGetNbActionsRechargeArme()
    {
	Assert.assertEquals(1, m_reference.getNbActionsRechargeArme(38));
    }

    @Test
    public void testGetMagasinArme()
    {
	Assert.assertEquals(1, m_reference.getNbActionsRechargeArme(38));
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
    public void testGetListComp()
    {
	ArrayList<String> analyse = new ArrayList<>();
	ArrayList<String> cac = new ArrayList<>();
	ArrayList<String> dist = new ArrayList<>();
	ArrayList<String> social = new ArrayList<>();

	analyse.add("empathie");
	analyse.add("investigation");
	analyse.add("politique");

	cac.add("attaque mains nues");
	cac.add("parade mains nues");
	cac.add("attaque armes d'escrime");
	cac.add("parade armes d'escrime");
	cac.add("attaque armes à lame longue");
	cac.add("parade armes à lame longue");
	cac.add("attaque haches");
	cac.add("parade haches");
	cac.add("attaque armes contondantes");
	cac.add("parade armes contondantes");
	cac.add("attaque petites armes");
	cac.add("parade petites armes");
	cac.add("attaque armes d'hast");
	cac.add("parade armes d'hast");
	cac.add("attaque armes spéciales");
	cac.add("parade armes spéciales");
	cac.add("attaque armes énergétiques");
	cac.add("parade armes énergétiques");
	cac.add("attaque armes lourdes");
	cac.add("parade armes lourdes");

	dist.add("archerie");
	dist.add("armes de jet");
	dist.add("armes à feu");
	dist.add("armes à énergie");
	dist.add("armes automatiques");
	dist.add("armes lourdes");
	dist.add("armes de trait");
	dist.add("lanceurs");

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
    public void testGetListDomaine()
    {
	Assert.assertEquals("analyse", m_reference.getListDomaines().get(0));
	Assert.assertEquals("social", m_reference.getListDomaines().get(8));
	Assert.assertEquals(9, m_reference.getListDomaines().size());
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

    public void testGetListLblArme()
    {
	Assert.assertEquals("épée à deux mains", m_reference.getListLblArme().get(14));
	Assert.assertEquals("rapière", m_reference.getListLblArme().get(7));
    }

    public void testGetListLblPieceArmure()
    {
	Assert.assertEquals("masque facial", m_reference.getListLblPieceArmure().get(2));
	Assert.assertEquals("jambière", m_reference.getListLblPieceArmure().get(5));
    }

    public void testGetListLblMateriauArmure()
    {
	Assert.assertEquals("lamelles ou maille", m_reference.getListLblMateriauArmure().get(1));
	Assert.assertEquals("cuir clouté", m_reference.getListLblMateriauArmure().get(2));
    }

    public void testGetListLblTypeArmure()
    {
	Assert.assertEquals("moderne", m_reference.getListLblTypeArmure().get(1));
	Assert.assertEquals("avec blindage", m_reference.getListLblTypeArmure().get(2));
    }

    public void testGetListLblTypeArme()
    {
	Assert.assertEquals("perce-armure", m_reference.getListLblTypeArme().get(1));
	Assert.assertEquals("perce-blindage", m_reference.getListLblTypeArme().get(3));
    }

    public void testGetListLblMateriauBouclier()
    {
	Assert.assertEquals("métal", m_reference.getListLblMateriauBouclier().get(1));
	Assert.assertEquals("bois", m_reference.getListLblMateriauBouclier().get(0));
    }

    public void testGetListLblBouclier()
    {
	Assert.assertEquals("bouclier", m_reference.getListLblBouclier().get(1));
	Assert.assertEquals("targe", m_reference.getListLblBouclier().get(0));
    }
}
