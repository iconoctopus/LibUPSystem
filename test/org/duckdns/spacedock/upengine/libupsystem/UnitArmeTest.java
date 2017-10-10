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

import java.util.EnumMap;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * On va en fait tester trois classes dans ce test "unitaires" mais c'est dû au
 * fait qu'il est impossible d'implémenter la superclasse (Arme) abstraite et
 * que les deux sous-classes ont au final peu de code différencié
 *
 * Exceptionellement on ne mocke pas UPReferenceSysteme car il est impossible de
 * mocker les membres de sa sous-classe, c'est donc en partie un test
 * d'intégration
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReferenceSysteme n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{//les classes final, appelant du statique et les classes subissant un whennew
	    Arme.class, UPReferenceArmes.class
	})
public class UnitArmeTest
{

    private static ArmeCaC arme1;
    private static ArmeCaC arme2;
    private static ArmeCaC arme3;
    private static ArmeCaC arme4;
    private static ArmeDist arme5;
    private static ArmeDist arme6;
    private static ArmeDist arme7;
    private static ArmeDist arme8;
    private static UPReferenceArmes referenceMock;
    private static EnumMap<Arme.QualiteArme, String> listQualite;
    private static EnumMap<Arme.EquilibrageArme, String> listEquilibrage;

    @BeforeClass
    public static void setUpClass()
    {
	//on mocke la référence
	referenceMock = PowerMockito.mock(UPReferenceArmes.class);
	PowerMockito.mockStatic(UPReferenceArmes.class);
	when(UPReferenceArmes.getInstance()).thenReturn(referenceMock);
	listEquilibrage = new EnumMap<>(Arme.EquilibrageArme.class);
	listEquilibrage.put(Arme.EquilibrageArme.mauvais, "mauvais");
	listEquilibrage.put(Arme.EquilibrageArme.normal, "normal");
	listEquilibrage.put(Arme.EquilibrageArme.bon, "bon");
	listQualite = new EnumMap<>(Arme.QualiteArme.class);
	listQualite.put(Arme.QualiteArme.inferieure, "inférieure");
	listQualite.put(Arme.QualiteArme.moyenne, "moyenne");
	listQualite.put(Arme.QualiteArme.superieure, "supérieure");
	listQualite.put(Arme.QualiteArme.maitre, "de maître");
	when(referenceMock.getListQualiteArme()).thenReturn(listQualite);
	when(referenceMock.getListEquilibrage()).thenReturn(listEquilibrage);

	when(referenceMock.getVDArme(3)).thenReturn(2);
	when(referenceMock.getBonusInitArme(3)).thenReturn(1);
	when(referenceMock.getCategorieArme(3)).thenReturn(1);
	when(referenceMock.getTypeArme(3)).thenReturn(4);
	when(referenceMock.getMalusAttaqueArme(3)).thenReturn(0);
	when(referenceMock.getPhysMinArme(3)).thenReturn(0);
	when(referenceMock.isArme2Mains(3)).thenReturn(false);
	when(referenceMock.getModArme(3)).thenReturn(0);
	when(referenceMock.getLblArme(3)).thenReturn("rapière");
	arme1 = new ArmeCaC(3, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);

	when(referenceMock.getVDArme(10)).thenReturn(6);
	when(referenceMock.getBonusInitArme(10)).thenReturn(0);
	when(referenceMock.getCategorieArme(10)).thenReturn(3);
	when(referenceMock.getTypeArme(10)).thenReturn(0);
	when(referenceMock.getMalusAttaqueArme(10)).thenReturn(1);
	when(referenceMock.getPhysMinArme(10)).thenReturn(2);
	when(referenceMock.isArme2Mains(10)).thenReturn(false);
	when(referenceMock.getModArme(10)).thenReturn(0);
	when(referenceMock.getLblArme(10)).thenReturn("hache d'arme");
	arme2 = new ArmeCaC(10, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon);
	arme3 = new ArmeCaC(10, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais);
	arme4 = new ArmeCaC(10, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);

	when(referenceMock.getVDArme(34)).thenReturn(3);
	when(referenceMock.getBonusInitArme(34)).thenReturn(0);
	when(referenceMock.getCategorieArme(34)).thenReturn(0);
	when(referenceMock.getTypeArme(34)).thenReturn(0);
	when(referenceMock.getMalusAttaqueArme(34)).thenReturn(0);
	when(referenceMock.getPhysMinArme(34)).thenReturn(0);
	when(referenceMock.isArme2Mains(34)).thenReturn(true);
	when(referenceMock.getModArme(34)).thenReturn(1);
	when(referenceMock.getLblArme(34)).thenReturn("arc");

	when(referenceMock.getMalusCourtArme(34)).thenReturn(5);
	when(referenceMock.getMalusLongArme(34)).thenReturn(10);
	when(referenceMock.getMagasinArme(34)).thenReturn(3);
	when(referenceMock.getNbActionsRechargeArme(34)).thenReturn(1);
	when(referenceMock.getPorteeArme(34)).thenReturn(150);

	arme5 = new ArmeDist(34, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
	arme6 = new ArmeDist(34, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon);
	arme7 = new ArmeDist(34, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);

	when(referenceMock.getVDArme(512)).thenReturn(5);
	when(referenceMock.getBonusInitArme(512)).thenReturn(2);
	when(referenceMock.getCategorieArme(512)).thenReturn(4);
	when(referenceMock.getTypeArme(512)).thenReturn(2);
	when(referenceMock.getMalusAttaqueArme(512)).thenReturn(1);
	when(referenceMock.getPhysMinArme(512)).thenReturn(3);
	when(referenceMock.isArme2Mains(512)).thenReturn(false);
	when(referenceMock.getModArme(512)).thenReturn(1);
	when(referenceMock.getLblArme(512)).thenReturn("SupaShooter");

	when(referenceMock.getMalusCourtArme(512)).thenReturn(5);
	when(referenceMock.getMalusLongArme(512)).thenReturn(10);
	when(referenceMock.getMagasinArme(512)).thenReturn(19);
	when(referenceMock.getNbActionsRechargeArme(512)).thenReturn(1);
	when(referenceMock.getPorteeArme(512)).thenReturn(50);

	arme8 = new ArmeDist(512, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais);
    }

    @Test
    public void testBonusInit()
    {
	Assert.assertEquals(0, arme1.getBonusInit());
	Assert.assertEquals(1, arme2.getBonusInit());
	Assert.assertEquals(1, arme3.getBonusInit());
	Assert.assertEquals(0, arme4.getBonusInit());
	Assert.assertEquals(0, arme5.getBonusInit());
	Assert.assertEquals(0, arme6.getBonusInit());
	Assert.assertEquals(0, arme7.getBonusInit());
	Assert.assertEquals(2, arme8.getBonusInit());
    }

    @Test
    public void testCategorie()
    {
	Assert.assertEquals(1, arme1.getCategorie());
	Assert.assertEquals(3, arme2.getCategorie());
	Assert.assertEquals(3, arme3.getCategorie());
	Assert.assertEquals(3, arme4.getCategorie());
	Assert.assertEquals(0, arme5.getCategorie());
	Assert.assertEquals(0, arme6.getCategorie());
	Assert.assertEquals(0, arme7.getCategorie());
	Assert.assertEquals(4, arme8.getCategorie());
    }

    @Test
    public void testVD()
    {
	Assert.assertEquals(5, arme1.getVD());
	Assert.assertEquals(3, arme2.getVD());
	Assert.assertEquals(12, arme3.getVD());
	Assert.assertEquals(6, arme4.getVD());
	Assert.assertEquals(3, arme5.getVD());
	Assert.assertEquals(3, arme6.getVD());
	Assert.assertEquals(3, arme7.getVD());
	Assert.assertEquals(5, arme8.getVD());
    }

    @Test
    public void testMalusAttaque()
    {
	Assert.assertEquals(0, arme1.getMalusAttaque());
	Assert.assertEquals(1, arme2.getMalusAttaque());
	Assert.assertEquals(1, arme3.getMalusAttaque());
	Assert.assertEquals(1, arme4.getMalusAttaque());
	Assert.assertEquals(0, arme5.getMalusAttaque());
	Assert.assertEquals(0, arme6.getMalusAttaque());
	Assert.assertEquals(0, arme7.getMalusAttaque());
	Assert.assertEquals(1, arme8.getMalusAttaque());
    }

    @Test
    public void testTypeArme()
    {
	Assert.assertEquals(4, arme1.getTypeArme());
	Assert.assertEquals(0, arme2.getTypeArme());
	Assert.assertEquals(0, arme3.getTypeArme());
	Assert.assertEquals(0, arme4.getTypeArme());
	Assert.assertEquals(0, arme5.getTypeArme());
	Assert.assertEquals(0, arme6.getTypeArme());
	Assert.assertEquals(0, arme7.getTypeArme());
	Assert.assertEquals(2, arme8.getTypeArme());
    }

    @Test
    public void testPhysMin()
    {
	Assert.assertEquals(0, arme1.getphysMin());
	Assert.assertEquals(2, arme2.getphysMin());
	Assert.assertEquals(2, arme3.getphysMin());
	Assert.assertEquals(2, arme4.getphysMin());
	Assert.assertEquals(0, arme5.getphysMin());
	Assert.assertEquals(0, arme6.getphysMin());
	Assert.assertEquals(0, arme7.getphysMin());
	Assert.assertEquals(3, arme8.getphysMin());
    }

    @Test
    public void testNbMains()
    {
	Assert.assertFalse(arme1.isArme2Mains());
	Assert.assertFalse(arme2.isArme2Mains());
	Assert.assertFalse(arme3.isArme2Mains());
	Assert.assertFalse(arme4.isArme2Mains());
	Assert.assertTrue(arme5.isArme2Mains());
	Assert.assertTrue(arme6.isArme2Mains());
	Assert.assertTrue(arme7.isArme2Mains());
	Assert.assertFalse(arme8.isArme2Mains());
    }

    @Test
    public void testLbl()
    {
	Assert.assertEquals("rapière de qualité supérieure et équilibrage mauvais", arme1.toString());
	Assert.assertEquals("hache d'arme de qualité inférieure et équilibrage bon", arme2.toString());
	Assert.assertEquals("hache d'arme de maître", arme3.toString());
	Assert.assertEquals("hache d'arme de qualité moyenne et équilibrage normal", arme4.toString());
	Assert.assertEquals("arc de qualité moyenne et équilibrage normal", arme5.toString());
	Assert.assertEquals("arc de qualité inférieure et équilibrage bon", arme6.toString());
	Assert.assertEquals("arc de qualité supérieure et équilibrage mauvais", arme7.toString());
	Assert.assertEquals("SupaShooter de maître", arme8.toString());
    }

    @Test
    public void testGetSpecifiqueDistance()
    {
	Assert.assertEquals(5, arme5.getMalusCourt());
	Assert.assertEquals(10, arme5.getMalusLong());
	Assert.assertEquals(0, arme5.getMunCourantes());
	Assert.assertEquals(3, arme5.getTailleMAgasin());
	Assert.assertEquals(1, arme5.getNbActionsRecharge());
	Assert.assertEquals(150, arme5.getPortee());

	Assert.assertEquals(8, arme6.getMalusCourt());
	Assert.assertEquals(13, arme6.getMalusLong());
	Assert.assertEquals(0, arme6.getMunCourantes());
	Assert.assertEquals(3, arme6.getTailleMAgasin());
	Assert.assertEquals(1, arme6.getNbActionsRecharge());
	Assert.assertEquals(300, arme6.getPortee());

	Assert.assertEquals(2, arme7.getMalusCourt());
	Assert.assertEquals(7, arme7.getMalusLong());
	Assert.assertEquals(0, arme7.getMunCourantes());
	Assert.assertEquals(3, arme7.getTailleMAgasin());
	Assert.assertEquals(1, arme7.getNbActionsRecharge());
	Assert.assertEquals(75, arme7.getPortee());

	Assert.assertEquals(-1, arme8.getMalusCourt());
	Assert.assertEquals(4, arme8.getMalusLong());
	Assert.assertEquals(0, arme8.getMunCourantes());
	Assert.assertEquals(19, arme8.getTailleMAgasin());
	Assert.assertEquals(1, arme8.getNbActionsRecharge());
	Assert.assertEquals(100, arme8.getPortee());
    }

    @Test
    public void testGestionMunitionsErreur()
    {
	//On recharge trop
	try
	{
	    arme5.recharger(4);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:4 taille du magasin:3", e.getMessage());
	}

	//On consomme à vide
	try
	{
	    arme5.consommerMun(1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:1 munitions courantes:0", e.getMessage());
	}
    }

    @Test
    public void testGestionMunitionsNominal()
    {
	Assert.assertEquals(1, arme5.recharger(2));
	arme5.consommerMun(1);
	Assert.assertEquals(1, arme5.getMunCourantes());
	arme5.consommerMun(1);
	Assert.assertEquals(0, arme5.getMunCourantes());
    }

    @Test
    public void testVerifPreAttaqueErreur()
    {
	arme8.recharger(19);
	arme5.recharger(3);
	//cas d'erreur : rafale avec arme ayant plusieurs munitions mais incapable de tirer en mode automatique (pistolet)
	try
	{
	    arme5.verifPreAttaque(10, 3);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:3", e.getMessage());
	}
	arme5.consommerMun(3);

	//cas d'erreur : portée insufissante entraînant échec auto
	Assert.assertTrue(arme8.verifPreAttaque(200, 10).isEchecAuto());
	assertEquals(9, arme8.getMunCourantes());//on vérifie que bien qu'il y ait eu échec auto, l'attaque hors portée ne soit pas annulée et les munitions bien consommées
	arme8.recharger(10);//on remplit les munitions consommées

	//cas d'erreur : plus de 20 balles
	try
	{
	    arme8.verifPreAttaque(100, 21);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:100 nombre de coups:21", e.getMessage());
	}

	//cas d'erreur : nb de balles nul
	try
	{
	    arme8.verifPreAttaque(0, 0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:0 nombre de coups:0", e.getMessage());
	}

	//cas d'erreur : distance négative
	try
	{
	    arme8.verifPreAttaque(-1, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:-1 nombre de coups:1", e.getMessage());
	}
	arme8.consommerMun(19);//on consomme toutes les munitions rechargées pour se retrouver en état nominal
    }

    @Test
    public void testVerifdistNominal()
    {
	arme8.recharger(19);

	//Cas coup par coup, portée courte (pile la moitié pour vérifier l'arrondi et le "inférieur ou égal", physique minimal dépassé, malus au jet d'attaque
	ArmeDist.DistReport report = arme8.verifPreAttaque(50, 1);
	assertEquals(1, report.getModJet());
	assertEquals(0, report.getModDesGardes());
	assertEquals(0, report.getModDesLances());
	arme8.recharger(1);

	//Cas rafale courte, portée longue (juste 1 au dessus de la moitié)
	report = arme8.verifPreAttaque(51, 3);
	assertEquals(-4, report.getModJet());
	assertEquals(0, report.getModDesGardes());
	assertEquals(2, report.getModDesLances());
	arme8.recharger(3);

	//Cas rafale moyenne (8 coups donc 2 tranches entières de 3), portée courte
	report = arme8.verifPreAttaque(25, 8);
	assertEquals(1, report.getModJet());
	assertEquals(0, report.getModDesGardes());
	assertEquals(4, report.getModDesLances());
	arme8.recharger(8);

	//Cas rafale longue(19 coups donc 3 tranches entières de 5), portée longue
	report = arme8.verifPreAttaque(75, 19);
	assertEquals(-4, report.getModJet());
	assertEquals(3, report.getModDesGardes());
	assertEquals(3, report.getModDesLances());
    }

    /*
    @Test
    public void testGenererDegatsCaCNominal()
    {
	//On prévoie les appels aux domaines et compétences
	when(arbreMock.getRangComp(3, 0)).thenReturn(3);
	when(arbreMock.getRangComp(3, 2)).thenReturn(3);
	when(arbreMock.getRangDomaine(3)).thenReturn(3);

	//Cas mains nues
	Degats result = persoRM3.genererDegats(3, null);
	verify(arbreMock).getRangComp(3, 0);
	verify(arbreMock).getRangDomaine(3);
	assertEquals(18, result.getQuantite());
	assertEquals(0, result.getTypeArme());

	//On mocke une arme
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(armeMock.getVD()).thenReturn(7);
	when(armeMock.getTypeArme()).thenReturn(3);
	when(armeMock.getMode()).thenReturn(0);
	when(armeMock.getCategorie()).thenReturn(1);

	//Cas nominal
	result = persoRM3.genererDegats(2, armeMock);
	verify(armeMock).getVD();
	verify(armeMock).getTypeArme();
	verify(armeMock).getMode();
	verify(armeMock).getCategorie();
	verify(arbreMock).getRangComp(3, 2);
	verify(arbreMock, times(2)).getRangDomaine(3);
	verify(traitsRM3, times(3)).getTrait(PHYSIQUE);
	assertEquals(20, result.getQuantite());
	assertEquals(3, result.getTypeArme());
    }

    @Test
    public void testGenererDegatsDistNominal()
    {
	//On prévoie les appels aux domaines et compétences
	when(arbreMock.getRangComp(4, 2)).thenReturn(1);
	when(arbreMock.getRangDomaine(4)).thenReturn(1);

	//On mocke un inventaire contenant un mock d'arme
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getMode()).thenReturn(1);
	when(armeMock.getVD()).thenReturn(5);
	when(armeMock.getTypeArme()).thenReturn(2);
	when(armeMock.getMode()).thenReturn(1);
	when(armeMock.getCategorie()).thenReturn(2);

	//Cas nominal
	Degats result = persoRM1.genererDegats(0, armeMock);
	verify(armeMock).getVD();
	verify(armeMock).getTypeArme();
	verify(armeMock).getMode();
	verify(armeMock).getCategorie();
	verify(arbreMock).getRangComp(4, 2);
	verify(arbreMock).getRangDomaine(4);
	assertEquals(7, result.getQuantite());
	assertEquals(2, result.getTypeArme());
    }


     */
}
