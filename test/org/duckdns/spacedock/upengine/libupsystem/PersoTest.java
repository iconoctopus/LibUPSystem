/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.fail;
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
	for (int i = 0; i < persoRM1.getActions().size(); i++)
	{
	    Assert.assertTrue(persoRM1.getActions().get(i) < 11 && persoRM1.getActions().get(i) > 0);
	}
	Assert.assertTrue(persoRM1.isActif(persoRM1.getActions().get(0)));
	Assert.assertEquals(null, persoRM1.getArmeCourante());
	Assert.assertEquals(null, persoRM1.getArmureCourante());
	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeresMentales());
	Assert.assertEquals(0, persoRM1.getListArmes().size());
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, false));//TODO tester en ajoutant de l'armure
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, true));//TODO tester en ajoutant de l'armure
	Assert.assertEquals(0, persoRM1.getPointsDeFatigue());
	Assert.assertTrue(persoRM1.isSonne());
	Assert.assertFalse(persoRM1.isInconscient());
	Assert.assertFalse(persoRM1.isElimine());

	Assert.assertEquals("PersoRM3", persoRM3.toString());
	Assert.assertEquals(3, persoRM3.getActions().size());
	for (int i = 0; i < persoRM3.getActions().size(); i++)
	{
	    Assert.assertTrue(persoRM3.getActions().get(i) < 11 && persoRM3.getActions().get(i) > 0);
	}
	Assert.assertTrue(persoRM3.isActif(persoRM3.getActions().get(0)));
	Assert.assertEquals(null, persoRM3.getArmeCourante());
	Assert.assertEquals(null, persoRM3.getArmureCourante());
	Assert.assertEquals(0, persoRM3.getBlessuresGraves());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeresMentales());
	Assert.assertEquals(0, persoRM3.getListArmes().size());
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 1, false));//TODO tester en ajoutant de l'armure
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 1, true));//TODO tester en ajoutant de l'armure
	Assert.assertEquals(0, persoRM3.getPointsDeFatigue());
	Assert.assertFalse(persoRM3.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());

	Assert.assertEquals("PersoRM5", persoRM5.toString());
	Assert.assertEquals(5, persoRM5.getActions().size());
	for (int i = 0; i < persoRM5.getActions().size(); i++)
	{
	    Assert.assertTrue(persoRM5.getActions().get(i) < 11 && persoRM5.getActions().get(i) > 0);
	}
	Assert.assertTrue(persoRM5.isActif(persoRM5.getActions().get(0)));
	Assert.assertEquals(null, persoRM5.getArmeCourante());
	Assert.assertEquals(null, persoRM5.getArmureCourante());
	Assert.assertEquals(0, persoRM5.getBlessuresGraves());
	Assert.assertEquals(0, persoRM5.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM5.getBlessuresLegeresMentales());
	Assert.assertEquals(0, persoRM5.getListArmes().size());
	Assert.assertEquals(35, persoRM5.getNDPassif(0, 1, false));//TODO tester en ajoutant de l'armure
	Assert.assertEquals(35, persoRM5.getNDPassif(0, 1, true));//TODO tester en ajoutant de l'armure
	Assert.assertEquals(0, persoRM5.getPointsDeFatigue());
	Assert.assertFalse(persoRM3.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());

	try
	{
	    new Perso(-11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}

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

	try
	{
	    persoRM1.agirEnCombat(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}
    }

    @Test
    public void testAttaquer()
    {
	persoRM1.getListArmes().add(new ArmeCaC(1, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal));
	persoRM1.setArmeCourante(persoRM1.getListArmes().size() - 1);
	//cas du physique minimal insuffisant
	StatTest stat = new StatTest(persoRM1, 1);
	Assert.assertFalse(stat.nbReussites > stat.nbEchecs);

	stat = new StatTest(persoRM3, 30);
	Assert.assertTrue(stat.nbReussites > stat.nbEchecs);
	stat = new StatTest(persoRM3, 35);
	Assert.assertFalse(stat.nbReussites > stat.nbEchecs);

	stat = new StatTest(persoRM5, 45);
	Assert.assertTrue(stat.nbReussites > stat.nbEchecs);
	stat = new StatTest(persoRM5, 50);
	Assert.assertFalse(stat.nbReussites > stat.nbEchecs);

	//TODO il faudra aussi tester que le malus à l'attaque des armes est pris en compte avec l'un des deux tests ci-dessus
	//TODO on ne teste que haches et mains nues ici, ajouter une arme à distance automatique avec toutes les rafales et
    }

    @Test
    public void testDegatsEtBlessure()//TODO varier plus les armes (hache et rapière ici, ajouter les dégâts à mains nues et une arme à distance )
    {

	StatTest stat = new StatTest(30, 1);
	Assert.assertTrue(stat.nbBlessuresGraves == 1);
	Assert.assertTrue(stat.total_degats >= 16 && stat.total_degats <= 20);

	stat = new StatTest(30, 3);
	Assert.assertTrue(stat.nbBlessuresGraves == 1 || stat.nbBlessuresGraves == 2);
	Assert.assertTrue(stat.total_degats == 24);

	stat = new StatTest(30, 5);
	Assert.assertTrue(stat.nbBlessuresGraves == 0);
	Assert.assertTrue(stat.total_degats == 27);
    }

    private class StatTest
    {//TODO refactorer cette classe et les deux méthodes qui l'utilisent pour effectuer tous les tests d'un coup

	int nbReussites = 0;
	int nbEchecs = 0;
	int total_degats = 0;
	int nbBlessuresGraves = 0;

	StatTest(Perso p_perso, int p_nd)//test du jet d'attaque
	{
	    p_perso.genInit();
	    int compteurActions = 0;
	    for (int i = 0; i <= 999999; ++i)//un million de lancers
	    {
		if (compteurActions == p_perso.getActions().size())
		{
		    p_perso.genInit();
		    compteurActions = 0;
		}
		if (p_perso.attaquerCaC(p_perso.getActions().get(compteurActions), p_nd).isJetReussi())
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

	StatTest(int p_degats, int p_rm)//test du jet de degats et des blessures
	{
	    for (int i = 0; i <= 999999; ++i)//un million de lancers
	    {//on crée ici un nouveau perso pour chaque test : sinon les blessures s'accumulent entre deux boucles et ils meurrent au final...
		Perso perso = new Perso(p_rm);
		perso.getListArmes().add(new ArmeCaC(0, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal));
		perso.setArmeCourante(perso.getListArmes().size() - 1);
		total_degats += perso.genererDegats(0).getQuantite();
		perso.etreBlesse(new Arme.Degats(p_degats, 0));
		nbBlessuresGraves += perso.getBlessuresGraves();
	    }
	    total_degats = (int) (total_degats / 1000000);
	    nbBlessuresGraves = (int) (nbBlessuresGraves / 1000000);
	}
    }
}
