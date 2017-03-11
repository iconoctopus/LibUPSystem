/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class ArmeTest
{

    static ArmeCaC arme1;
    static ArmeCaC arme2;
    static ArmeCaC arme3;
    static ArmeDist arme4;

    @BeforeClass
    public static void setUpClass()
    {
	arme1 = new ArmeCaC(3, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);
	arme2 = new ArmeCaC(10, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon);
	arme3 = new ArmeCaC(10, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais);
	arme4 = new ArmeDist(34, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
    }

    @Test
    public void testCreationArme()
    {
	Assert.assertEquals(0, arme1.getBonusInit());
	Assert.assertEquals(1, arme1.getCategorie());
	Assert.assertEquals(3, arme1.getDesGardes());
	Assert.assertEquals(3, arme1.getDesLances());
	Assert.assertEquals(0, arme1.getMalusAttaque());
	Assert.assertEquals(0, arme1.getTypeArme());
	Assert.assertEquals(0, arme1.getphysMin());
	Assert.assertEquals(0, arme1.getMode());
	Assert.assertEquals("rapière de qualité supérieure et équilibrage mauvais", arme1.toString());

	Assert.assertEquals(1, arme2.getBonusInit());
	Assert.assertEquals(3, arme2.getCategorie());
	Assert.assertEquals(3, arme2.getDesGardes());
	Assert.assertEquals(5, arme2.getDesLances());
	Assert.assertEquals(1, arme2.getMalusAttaque());
	Assert.assertEquals(0, arme2.getTypeArme());
	Assert.assertEquals(2, arme2.getphysMin());
	Assert.assertEquals(0, arme2.getMode());
	Assert.assertEquals("hache d'arme de qualité inférieure et équilibrage bon", arme2.toString());

	Assert.assertEquals(1, arme3.getBonusInit());
	Assert.assertEquals(3, arme3.getCategorie());
	Assert.assertEquals(4, arme3.getDesGardes());
	Assert.assertEquals(7, arme3.getDesLances());
	Assert.assertEquals(1, arme3.getMalusAttaque());
	Assert.assertEquals(0, arme3.getTypeArme());
	Assert.assertEquals(2, arme3.getphysMin());
	Assert.assertEquals(0, arme3.getMode());
	Assert.assertEquals("hache d'arme de maître", arme3.toString());

	Assert.assertEquals(0, arme4.getBonusInit());
	Assert.assertEquals(0, arme4.getCategorie());
	Assert.assertEquals(3, arme4.getDesGardes());
	Assert.assertEquals(2, arme4.getDesLances());
	Assert.assertEquals(0, arme4.getMalusAttaque());
	Assert.assertEquals(0, arme4.getTypeArme());
	Assert.assertEquals(0, arme4.getphysMin());
	Assert.assertEquals(5, arme4.getMalusCourt());
	Assert.assertEquals(10, arme4.getMalusLong());
	Assert.assertEquals(0, arme4.getMunCourantes());
	Assert.assertEquals(1, arme4.getTailleMAgasin());
	Assert.assertEquals(1, arme4.getNbActionsRecharge());
	Assert.assertEquals(150, arme4.getPortee());
	Assert.assertEquals(1, arme4.getMode());
	Assert.assertEquals("arc de qualité moyenne et équilibrage normal", arme4.toString());
    }

    @Test
    public void testBlindageMethodesEtClasseDegats()
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
    public void testGestionMunitions()
    {

	try
	{
	    arme4.recharger(2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:2 taille du magasin:1", e.getMessage());
	}

	try
	{
	    arme4.consommerMun(1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:1 munitions courantes:0", e.getMessage());
	}

	Assert.assertEquals(1, arme4.recharger(1));
	arme4.consommerMun(1);
    }
}
