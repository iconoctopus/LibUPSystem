/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class PersoTest
{

    static Perso persoRM1;
    static Perso persoRM3;
    static Perso persoRM5;

    @Before
    public void setUp()
    {
	persoRM1 = new Perso(1);
	persoRM3 = new Perso(3);
	persoRM5 = new Perso(5);
    }

    @Test
    public void testPerso()
    {
	Assert.assertEquals("PersoRM1", persoRM1.toString());
	Assert.assertEquals(1, persoRM1.getActions().size());
	for(int i = 0; i < persoRM1.getActions().size(); i++)
	{
	    Assert.assertTrue(persoRM1.getActions().get(i) < 11 && persoRM1.getActions().get(i) > 0);
	}
	Assert.assertTrue(persoRM1.isActif(persoRM1.getActions().get(0)));
	Assert.assertEquals(1, persoRM1.getArmeCourante().getCategorie());
	Assert.assertEquals(null, persoRM1.getArmureCourante());
	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeresMentales());
	Assert.assertEquals(1, persoRM1.getListArmes().size());
	Assert.assertEquals(10, persoRM1.getNDPassif(0));
	Assert.assertEquals(0, persoRM1.getPointsDeFatigue());
	Assert.assertTrue(persoRM1.isSonne());
	Assert.assertFalse(persoRM1.isInconscient());
	Assert.assertFalse(persoRM1.isElimine());

	Assert.assertEquals("PersoRM3", persoRM3.toString());
	Assert.assertEquals(3, persoRM3.getActions().size());
	for(int i = 0; i < persoRM3.getActions().size(); i++)
	{
	    Assert.assertTrue(persoRM3.getActions().get(i) < 11 && persoRM3.getActions().get(i) > 0);
	}
	Assert.assertTrue(persoRM3.isActif(persoRM3.getActions().get(0)));
	Assert.assertEquals(1, persoRM3.getArmeCourante().getCategorie());
	Assert.assertEquals(null, persoRM3.getArmureCourante());
	Assert.assertEquals(0, persoRM3.getBlessuresGraves());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeresMentales());
	Assert.assertEquals(1, persoRM3.getListArmes().size());
	Assert.assertEquals(25, persoRM3.getNDPassif(0));
	Assert.assertEquals(0, persoRM3.getPointsDeFatigue());
	Assert.assertFalse(persoRM3.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());

	Assert.assertEquals("PersoRM5", persoRM5.toString());
	Assert.assertEquals(5, persoRM5.getActions().size());
	for(int i = 0; i < persoRM5.getActions().size(); i++)
	{
	    Assert.assertTrue(persoRM5.getActions().get(i) < 11 && persoRM5.getActions().get(i) > 0);
	}
	Assert.assertTrue(persoRM5.isActif(persoRM5.getActions().get(0)));
	Assert.assertEquals(1, persoRM5.getArmeCourante().getCategorie());
	Assert.assertEquals(null, persoRM5.getArmureCourante());
	Assert.assertEquals(0, persoRM5.getBlessuresGraves());
	Assert.assertEquals(0, persoRM5.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM5.getBlessuresLegeresMentales());
	Assert.assertEquals(1, persoRM5.getListArmes().size());
	Assert.assertEquals(35, persoRM5.getNDPassif(0));
	Assert.assertEquals(0, persoRM5.getPointsDeFatigue());
	Assert.assertFalse(persoRM3.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());
    }

    @Test
    public void testAgirEnCombat()
    {
	persoRM1.agirEnCombat(persoRM1.getActions().get(0));
	Assert.assertEquals(11, persoRM1.getActions().get(0).intValue());

	persoRM3.agirEnCombat(persoRM3.getActions().get(0));
	Assert.assertEquals(11, persoRM3.getActions().get(0).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(1));
	Assert.assertEquals(11, persoRM3.getActions().get(1).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(2));
	Assert.assertEquals(11, persoRM3.getActions().get(2).intValue());

	persoRM5.agirEnCombat(persoRM5.getActions().get(0));
	Assert.assertEquals(11, persoRM5.getActions().get(0).intValue());
	persoRM5.agirEnCombat(persoRM5.getActions().get(1));
	Assert.assertEquals(11, persoRM5.getActions().get(1).intValue());
	persoRM5.agirEnCombat(persoRM5.getActions().get(2));
	Assert.assertEquals(11, persoRM5.getActions().get(2).intValue());
	persoRM5.agirEnCombat(persoRM5.getActions().get(3));
	Assert.assertEquals(11, persoRM5.getActions().get(3).intValue());
	persoRM5.agirEnCombat(persoRM5.getActions().get(4));
	Assert.assertEquals(11, persoRM5.getActions().get(4).intValue());
    }

    @Test
    public void testAttaquer()
    {
	StatTest stat = new StatTest(persoRM1, 5);
	Assert.assertTrue(stat.nbReussites > stat.nbEchecs);
	stat = new StatTest(persoRM1, 10);
	Assert.assertFalse(stat.nbReussites > stat.nbEchecs);

	stat = new StatTest(persoRM3, 30);
	Assert.assertTrue(stat.nbReussites > stat.nbEchecs);
	stat = new StatTest(persoRM3, 35);
	Assert.assertFalse(stat.nbReussites > stat.nbEchecs);

	stat = new StatTest(persoRM5, 45);
	Assert.assertTrue(stat.nbReussites > stat.nbEchecs);
	stat = new StatTest(persoRM5, 50);
	Assert.assertFalse(stat.nbReussites > stat.nbEchecs);
    }

    @Test
    public void testDegatsEtBlessure()
    {
	StatTest stat = new StatTest(persoRM1, 30, 1);
	Assert.assertTrue(stat.nbBlessuresGraves == 1);
	Assert.assertTrue(stat.total_degats >= 16 && stat.total_degats <= 20);

	stat = new StatTest(persoRM3, 30, 3);
	Assert.assertTrue(stat.nbBlessuresGraves == 1 || stat.nbBlessuresGraves == 2);
	Assert.assertTrue(stat.total_degats == 24);

	stat = new StatTest(persoRM5, 30, 5);
	Assert.assertTrue(stat.nbBlessuresGraves == 0);
	Assert.assertTrue(stat.total_degats == 27);
    }

    private class StatTest
    {

	int nbReussites = 0;
	int nbEchecs = 0;
	int total_degats = 0;
	int nbBlessuresGraves = 0;

	StatTest(Perso p_perso, int p_nd)//test du jet d'attaque
	{
	    p_perso.genInit();
	    int compteurActions = 0;
	    for(int i = 0; i <= 999999; ++i)//un million de lancers
	    {
		if(compteurActions == p_perso.getActions().size())
		{
		    p_perso.genInit();
		    compteurActions = 0;
		}
		if(p_perso.attaquer(p_perso.getActions().get(compteurActions), p_nd).isJetReussi())
		{
		    nbReussites++;
		}
		else
		{
		    nbEchecs++;
		}
		compteurActions++;
	    }
	}

	StatTest(Perso p_perso, int p_degats, int p_rm)//test du jet de degats et des blessures
	{

	    for(int i = 0; i <= 999999; ++i)//un million de lancers
	    {
		p_perso = new Perso(p_rm);
		total_degats += p_perso.genererDegats(0).getQuantite();
		p_perso.etreBlesse(new Arme.Degats(p_degats, 0));
		nbBlessuresGraves += p_perso.getBlessuresGraves();
	    }
	    total_degats = (int) (total_degats / 1000000);
	    nbBlessuresGraves = (int) (nbBlessuresGraves / 1000000);
	}
    }

    /*
    genererDegats etreBlesse





     */
}
