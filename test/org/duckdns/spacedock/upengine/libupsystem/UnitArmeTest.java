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
 * mocker les membres de la sous-classe, c'est donc en partie un test
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
    private static ArmeDist arme4;
    private static UPReferenceArmes referenceMock;
    private static EnumMap listQualite;
    private static EnumMap listEquilibrage;

    @BeforeClass
    public static void setUpClass()
    {
	//on mocke la référence
	referenceMock = PowerMockito.mock(UPReferenceArmes.class);
	PowerMockito.mockStatic(UPReferenceArmes.class);
	when(UPReferenceArmes.getInstance()).thenReturn(referenceMock);
	listEquilibrage = new EnumMap(Arme.EquilibrageArme.class);
	listEquilibrage.put(Arme.EquilibrageArme.mauvais, "mauvais");
	listEquilibrage.put(Arme.EquilibrageArme.normal, "normal");
	listEquilibrage.put(Arme.EquilibrageArme.bon, "bon");
	listQualite = new EnumMap(Arme.QualiteArme.class);
	listQualite.put(Arme.QualiteArme.inferieure, "inférieure");
	listQualite.put(Arme.QualiteArme.moyenne, "moyenne");
	listQualite.put(Arme.QualiteArme.superieure, "supérieure");
	listQualite.put(Arme.QualiteArme.maitre, "de maître");
	when(referenceMock.getListQualiteArme()).thenReturn(listQualite);
	when(referenceMock.getListEquilibrage()).thenReturn(listEquilibrage);

	when(referenceMock.getNbLancesArme(3)).thenReturn(2);
	when(referenceMock.getNbGardesArme(3)).thenReturn(3);
	when(referenceMock.getBonusInitArme(3)).thenReturn(1);
	when(referenceMock.getCategorieArme(3)).thenReturn(1);
	when(referenceMock.getTypeArme(3)).thenReturn(0);
	when(referenceMock.getMalusAttaqueArme(3)).thenReturn(0);
	when(referenceMock.getPhysMinArme(3)).thenReturn(0);
	when(referenceMock.getNbMainsArme(3)).thenReturn(1);
	when(referenceMock.getModArme(3)).thenReturn(0);
	when(referenceMock.getLblArme(3)).thenReturn("rapière");
	arme1 = new ArmeCaC(3, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);

	when(referenceMock.getNbLancesArme(10)).thenReturn(6);
	when(referenceMock.getNbGardesArme(10)).thenReturn(3);
	when(referenceMock.getBonusInitArme(10)).thenReturn(0);
	when(referenceMock.getCategorieArme(10)).thenReturn(3);
	when(referenceMock.getTypeArme(10)).thenReturn(0);
	when(referenceMock.getMalusAttaqueArme(10)).thenReturn(1);
	when(referenceMock.getPhysMinArme(10)).thenReturn(2);
	when(referenceMock.getNbMainsArme(10)).thenReturn(1);
	when(referenceMock.getModArme(10)).thenReturn(0);
	when(referenceMock.getLblArme(10)).thenReturn("hache d'arme");
	arme2 = new ArmeCaC(10, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon);
	arme3 = new ArmeCaC(10, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais);

	when(referenceMock.getNbLancesArme(34)).thenReturn(2);
	when(referenceMock.getNbGardesArme(34)).thenReturn(3);
	when(referenceMock.getBonusInitArme(34)).thenReturn(0);
	when(referenceMock.getCategorieArme(34)).thenReturn(0);
	when(referenceMock.getTypeArme(34)).thenReturn(0);
	when(referenceMock.getMalusAttaqueArme(34)).thenReturn(0);
	when(referenceMock.getPhysMinArme(34)).thenReturn(0);
	when(referenceMock.getNbMainsArme(34)).thenReturn(2);
	when(referenceMock.getModArme(34)).thenReturn(1);
	when(referenceMock.getLblArme(34)).thenReturn("arc");

	when(referenceMock.getMalusCourtArme(34)).thenReturn(5);
	when(referenceMock.getMalusLongArme(34)).thenReturn(10);
	when(referenceMock.getMagasinArme(34)).thenReturn(1);
	when(referenceMock.getNbActionsRechargeArme(34)).thenReturn(1);
	when(referenceMock.getPorteeArme(34)).thenReturn(150);

	arme4 = new ArmeDist(34, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
    }

    @Test
    public void testBonusInit()
    {
	Assert.assertEquals(0, arme1.getBonusInit());
	Assert.assertEquals(1, arme2.getBonusInit());
	Assert.assertEquals(1, arme3.getBonusInit());
	Assert.assertEquals(0, arme4.getBonusInit());
    }

    @Test
    public void testCategorie()
    {
	Assert.assertEquals(1, arme1.getCategorie());
	Assert.assertEquals(3, arme2.getCategorie());
	Assert.assertEquals(3, arme3.getCategorie());
	Assert.assertEquals(0, arme4.getCategorie());
    }

    @Test
    public void testDesLances()
    {
	Assert.assertEquals(3, arme1.getDesLances());
	Assert.assertEquals(5, arme2.getDesLances());
	Assert.assertEquals(7, arme3.getDesLances());
	Assert.assertEquals(2, arme4.getDesLances());
    }

    @Test
    public void testDesGardes()
    {
	Assert.assertEquals(3, arme1.getDesGardes());
	Assert.assertEquals(3, arme2.getDesGardes());
	Assert.assertEquals(4, arme3.getDesGardes());
	Assert.assertEquals(3, arme4.getDesGardes());
    }

    @Test
    public void testMalusAttaque()
    {
	Assert.assertEquals(0, arme1.getMalusAttaque());
	Assert.assertEquals(1, arme2.getMalusAttaque());
	Assert.assertEquals(1, arme3.getMalusAttaque());
	Assert.assertEquals(0, arme4.getMalusAttaque());
    }

    @Test
    public void testTypeArme()
    {
	Assert.assertEquals(0, arme1.getTypeArme());
	Assert.assertEquals(0, arme2.getTypeArme());
	Assert.assertEquals(0, arme3.getTypeArme());
	Assert.assertEquals(0, arme4.getTypeArme());
    }

    @Test
    public void testPhysMin()
    {
	Assert.assertEquals(0, arme1.getphysMin());
	Assert.assertEquals(2, arme2.getphysMin());
	Assert.assertEquals(2, arme3.getphysMin());
	Assert.assertEquals(0, arme4.getphysMin());
    }

    @Test
    public void testMode()
    {
	Assert.assertEquals(0, arme1.getMode());
	Assert.assertEquals(0, arme2.getMode());
	Assert.assertEquals(0, arme3.getMode());
	Assert.assertEquals(1, arme4.getMode());
    }

    @Test
    public void testLbl()
    {
	Assert.assertEquals("rapière de qualité supérieure et équilibrage mauvais", arme1.toString());
	Assert.assertEquals("hache d'arme de qualité inférieure et équilibrage bon", arme2.toString());
	Assert.assertEquals("hache d'arme de maître", arme3.toString());
	Assert.assertEquals("arc de qualité moyenne et équilibrage normal", arme4.toString());
    }

    @Test
    public void testGetSpecifiqueDistancet()
    {
	Assert.assertEquals(5, arme4.getMalusCourt());
	Assert.assertEquals(10, arme4.getMalusLong());
	Assert.assertEquals(0, arme4.getMunCourantes());
	Assert.assertEquals(1, arme4.getTailleMAgasin());
	Assert.assertEquals(1, arme4.getNbActionsRecharge());
	Assert.assertEquals(150, arme4.getPortee());
    }

    @Test
    public void testDegatsErreur()
    {
	try
	{
	    new Arme.Degats(-1, 2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:-1 type:2", e.getMessage());
	}

	try
	{
	    new Arme.Degats(2, -1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:2 type:-1", e.getMessage());
	}
    }

    @Test
    public void testGenererDegatsErreur()
    {
	try
	{
	    arme1.genererDegats(2, -1, true);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:incréments:2 physique:-1", e.getMessage());
	}

	try
	{
	    arme2.genererDegats(-2, 1, true);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:incréments:-2 physique:1", e.getMessage());
	}
    }

    @Test
    public void testGestionMunitionsErreur()
    {
	//On recharge trop
	try
	{
	    arme4.recharger(2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:2 taille du magasin:1", e.getMessage());
	}

	//On consomme à vide
	try
	{
	    arme4.consommerMun(1);
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
	Assert.assertEquals(1, arme4.recharger(1));
	arme4.consommerMun(1);
	Assert.assertEquals(0, arme4.getMunCourantes());
    }
}
