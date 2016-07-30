/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

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
    public void testGetInitTotale()
    {
	Assert.assertEquals((int) persoRM1.getActions().get(0), (int) persoRM1.getInitTotale());//son init de base

	persoRM1.getListArmes().add(new ArmeCaC(0, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.bon));
	persoRM1.setArmeCourante(persoRM1.getListArmes().size() - 1);

	Assert.assertEquals((int) persoRM1.getActions().get(0) + 10, (int) persoRM1.getInitTotale());//son init améliorée par une rapière bien équilibrée
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
    public void testIsActif()
    {
	try
	{
	    persoRM1.isActif(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}

	try
	{
	    persoRM1.isActif(11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:11", e.getMessage());
	}
    }

    @Test
    public void testAttaquer()//sert aussi pour tester tout le système de jets de compétence : il se trouve qu'attaquer est la forme la plus complexe du jet de compétence, une fois tous les bonus et malus pris en compte
    {
	//cas du physique minimal insuffisant avec une hache et un physique de 1
	persoRM1.getListArmes().add(new ArmeCaC(1, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal));
	persoRM1.setArmeCourante(persoRM1.getListArmes().size() - 1);
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM1, 1, 0, 0));

	//attaque à mains nues avec RM3
	Assert.assertTrue(reussiteStatistiqueAttaque(persoRM3, 30, 0, 0));
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM3, 35, 0, 0));

	//attaque en prenant en compte le malus à l'attaque du sabre et RM3
	persoRM3.getListArmes().add(new ArmeCaC(2, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon));//la qualité ne devrait pas influer, l'équilibrage non plus, ni ici ni dans les autres tests de cette méthode
	persoRM3.setArmeCourante(persoRM3.getListArmes().size() - 1);
	Assert.assertTrue(reussiteStatistiqueAttaque(persoRM3, 27, 0, 0));
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM3, 32, 0, 0));

	//attaque à distance au fusil d'assaut coup par coup avec RM5 et portée courte
	persoRM5.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais));
	persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	((ArmeDist) persoRM5.getArmeCourante()).recharger(30);
	Assert.assertTrue(reussiteStatistiqueAttaque(persoRM5, 42, 150, 1));//pile la portée, donc courte
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM5, 46, 150, 2));//deux balles ne devraient rien changer, on n'est pas au seuil de rafale courte

	//attaque à distance au fusil d'assaut en rafale courte avec RM3 et portée courte
	persoRM3.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal));
	persoRM3.setArmeCourante(persoRM3.getListArmes().size() - 1);
	((ArmeDist) persoRM3.getArmeCourante()).recharger(30);
	Assert.assertTrue(reussiteStatistiqueAttaque(persoRM3, 27, 100, 3));
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM3, 32, 20, 3));

	//attaque à distance au fusil d'assaut en rafale moyenne avec RM3 et portée longue
	persoRM3.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais));
	persoRM3.setArmeCourante(persoRM3.getListArmes().size() - 1);
	((ArmeDist) persoRM3.getArmeCourante()).recharger(30);
	Assert.assertTrue(reussiteStatistiqueAttaque(persoRM3, 24, 200, 8));//donc deux groupes entiers de trois balles
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM3, 28, 270, 8));

	//attaque à distance au fusil d'assaut en rafale longue avec RM5 et portée courte
	persoRM5.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	((ArmeDist) persoRM5.getArmeCourante()).recharger(30);
	Assert.assertTrue(reussiteStatistiqueAttaque(persoRM5, 56, 120, 13));//deux groupes entiers de 5 balles
	Assert.assertFalse(reussiteStatistiqueAttaque(persoRM5, 60, 40, 13));

	//TODO ajouter cas d'erreur : rafale avec arme ayant plusieurs munitions mais incapable de tirer en mode automatique (comme un pistolet)
	//cas d'erreur : plus de 20 balles
	try
	{
	    persoRM5.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	    persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	    ((ArmeDist) persoRM5.getArmeCourante()).recharger(30);
	    persoRM5.genInit();
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 100, 21);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:100 nombre de coups:21", e.getMessage());
	}

	//cas d'erreur : nb de balles nul
	try
	{
	    persoRM5.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	    persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	    ((ArmeDist) persoRM5.getArmeCourante()).recharger(30);
	    persoRM5.genInit();
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 0, 0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:0 nombre de coups:0", e.getMessage());
	}

	//cas d'erreur : distance négative
	try
	{
	    persoRM5.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	    persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	    ((ArmeDist) persoRM5.getArmeCourante()).recharger(30);
	    persoRM5.genInit();
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, -1, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:-1 nombre de coups:1", e.getMessage());
	}

	//cas d'erreur hors portée sur arc
	persoRM5.getListArmes().add(new ArmeDist(3, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	((ArmeDist) persoRM5.getArmeCourante()).recharger(1);
	persoRM5.genInit();
	Assert.assertFalse(persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 200, 1).isJetReussi());

	//cas d'erreur arme pas assez chargée pour faire feu : rafale trop grosse
	try
	{
	    persoRM5.getListArmes().add(new ArmeDist(4, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	    persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	    ((ArmeDist) persoRM5.getArmeCourante()).recharger(15);
	    persoRM5.genInit();
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 0, 20);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:20 munitions courantes:15", e.getMessage());
	}

	//cas d'erreur arme pas assez chargée pour faire feu : arme vide
	try
	{
	    persoRM5.getListArmes().add(new ArmeDist(3, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon));
	    persoRM5.setArmeCourante(persoRM5.getListArmes().size() - 1);
	    persoRM5.genInit();
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 0, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:1 munitions courantes:0", e.getMessage());
	}
    }

    @Test
    public void testGenererDegats()
    {
	//Test en corps à corps avec rapière de maître sans incréments, le perso est sonné
	Arme arme = new ArmeCaC(0, Arme.QualiteArme.maitre, Arme.EquilibrageArme.normal);
	int resultVariant = degatsStatistiques(1, arme, 0);
	Assert.assertTrue(22 == resultVariant || resultVariant == 21);

	//test en corps avec hache de mauvaise qualité et deux incréments
	arme = new ArmeCaC(1, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.normal);
	Assert.assertEquals(31, degatsStatistiques(3, arme, 2));

	//test en corps à corps à mains nues sans incréments
	Assert.assertEquals(11, degatsStatistiques(5, null, 0));

	//test à distance avec arc de bonne qualité et un incrément
	arme = new ArmeDist(3, Arme.QualiteArme.superieure, Arme.EquilibrageArme.normal);
	Assert.assertEquals(21, degatsStatistiques(5, arme, 1));

	//cas d'erreur : incréments négatifs
	try
	{
	    persoRM5.genererDegats(-1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:incréments:-1", e.getMessage());
	}
    }

    @Test
    public void testEtreBlesse()
    {
	int nbGraves = 0;

	nbGraves = nbBlessuresGravesStatistique(30, 1);
	Assert.assertTrue(nbGraves == 1);

	nbGraves = nbBlessuresGravesStatistique(30, 3);
	Assert.assertTrue(nbGraves == 1 || nbGraves == 2);

	nbGraves = nbBlessuresGravesStatistique(30, 5);
	Assert.assertTrue(nbGraves == 0);

	//cas limite : les dégâts à 0 pasent sans causer d'effet
	persoRM1.etreBlesse(new Arme.Degats(0, 0));
	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());

	//cas d'erreur : dégâts négatifs
	try
	{
	    persoRM1.etreBlesse(new Arme.Degats(-1, 0));
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:-1 type:0", e.getMessage());
	}
    }

    private boolean reussiteStatistiqueAttaque(Perso p_perso, int p_nd, int p_distance, int p_nbCoups)
    {
	p_perso.genInit();
	int nbReussites = 0;
	int nbEchecs = 0;
	int compteurActions = 0;
	for (int i = 0; i <= 999999; ++i)//un million de lancers
	{
	    boolean reussite = false;

	    if (compteurActions == p_perso.getActions().size())
	    {
		p_perso.genInit();
		compteurActions = 0;
	    }

	    if (p_distance > 0)
	    {
		if (p_perso.attaquerDist(p_perso.getActions().get(compteurActions), p_nd, p_distance, p_nbCoups).isJetReussi())
		{
		    reussite = true;
		}
		((ArmeDist) p_perso.getArmeCourante()).recharger(p_nbCoups);
	    }
	    else
	    {
		if (p_perso.attaquerCaC(p_perso.getActions().get(compteurActions), p_nd).isJetReussi())
		{
		    reussite = true;
		}

	    }
	    if (reussite)
	    {
		nbReussites++;
	    }
	    else
	    {
		nbEchecs++;
	    }

	    compteurActions++;
	}
	return (nbReussites > nbEchecs);
    }

    private int degatsStatistiques(int p_rm, Arme p_arme, int p_increments)
    {
	int total_degats = 0;
	Perso perso = new Perso(p_rm);
	perso.getListArmes().add(p_arme);
	perso.setArmeCourante(perso.getListArmes().size() - 1);
	for (int i = 0; i <= 999999; ++i)//un million de lancers
	{
	    total_degats += perso.genererDegats(p_increments).getQuantite();
	}
	return (int) (total_degats / 1000000);
    }

    private int nbBlessuresGravesStatistique(int p_degats, int p_rm)
    {
	int nbBlessuresGraves = 0;
	for (int i = 0; i <= 999999; ++i)//un million de lancers
	{//on crée ici un nouveau perso pour chaque test : sinon les blessures s'accumulent entre deux boucles et ils meurrent au final...
	    Perso perso = new Perso(p_rm);
	    perso.etreBlesse(new Arme.Degats(p_degats, 0));
	    nbBlessuresGraves += perso.getBlessuresGraves();
	}
	return (int) (nbBlessuresGraves / 1000000);
    }
}
