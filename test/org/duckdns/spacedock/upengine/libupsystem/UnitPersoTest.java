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

import java.util.ArrayList;
import org.duckdns.spacedock.upengine.libupsystem.Arme.Degats;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReferenceSysteme n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{//les classes final, appelant du statique et les classes subissant un whennew
	    Perso.class, UPReferenceSysteme.class, RollUtils.RollResult.class, Armure.class, Degats.class, CoupleJauge.class, Inventaire.class, ArbreDomaines.class
	})
public class UnitPersoTest
{

    private UPReferenceSysteme referenceMock;
    private CoupleJauge santeInitRM1;
    private CoupleJauge santeInitRM3;
    private CoupleJauge fatigueFARM1;
    private CoupleJauge fatigueFARM3;
    private ArbreDomaines arbreMock;
    private Inventaire inventaireMock;
    private Perso persoRM1;
    private Perso persoRM3;
    private static ArrayList<String> listComp = new ArrayList<>();
    private static ArrayList<String> listDom = new ArrayList<>();

    @BeforeClass
    public static void setupClass()
    {
	//on construit une liste de compétences retournée par la référence mockée, ce seront les mêmes pour tous les domaines
	listComp.add("comp1");
	listComp.add("comp2");
	listComp.add("comp3");

	//on construit une liste de domaines retournée par la référence mockée, quatre suffiront
	listDom.add("dom1");
	listDom.add("dom2");
	listDom.add("dom3");
	listDom.add("dom4");
	listDom.add("dom5");
    }

    @Before
    public void setup() throws Exception
    {
	//on mocke la référence
	referenceMock = PowerMockito.mock(UPReferenceSysteme.class);
	PowerMockito.mockStatic(UPReferenceSysteme.class);
	when(UPReferenceSysteme.getInstance()).thenReturn(referenceMock);

	//on mocke deux groupes de jauges, un pour un perso RM1, l'autre pour un perso RM3
	santeInitRM1 = PowerMockito.mock(CoupleJauge.class);
	santeInitRM3 = PowerMockito.mock(CoupleJauge.class);
	fatigueFARM1 = PowerMockito.mock(CoupleJauge.class);
	fatigueFARM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0, 1).thenReturn(santeInitRM1);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2, 3).thenReturn(santeInitRM3);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0).thenReturn(fatigueFARM1);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2).thenReturn(fatigueFARM3);

	//La référence mockée renvoie les listes de domaines et compétence prédéfinies
	when(referenceMock.getListDomaines()).thenReturn(listDom);
	when(referenceMock.getListComp(2)).thenReturn(listComp);
	when(referenceMock.getListComp(3)).thenReturn(listComp);
	when(referenceMock.getListComp(4)).thenReturn(listComp);

	//les jauges mockées renvoient des valeurs d'init
	when(santeInitRM1.getRemplissage_externe()).thenReturn(1);
	when(santeInitRM3.getRemplissage_externe()).thenReturn(3);

	//on mocke un arbre de domaines
	arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	//On mocke un inventaire
	inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);

	//On crée les persos pour le test
	persoRM1 = new Perso(1);
	persoRM3 = new Perso(3);
    }

    @Test
    public void testPersoParCaracsErreur()
    {
	//cas d'erreur : pas assez de traits
	try
	{
	    int[] traits =
	    {
		1, 1
	    };
	    new Perso(traits, arbreMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de traits:2", e.getMessage());
	}

	//cas d'erreur : trop de traits
	try
	{
	    int[] traits =
	    {
		1, 1, 1, 1, 1, 1
	    };
	    new Perso(traits, arbreMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de traits:6", e.getMessage());
	}

	//cas d'erreur : trait négatif
	try
	{
	    int[] traits =
	    {
		0, 1, -12, 1, 1
	    };
	    new Perso(traits, arbreMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:-12", e.getMessage());
	}
    }

    @Test
    public void testPersoParCaracsNominal() throws Exception
    {
	int[] traits =
	{
	    3, 4, 2, 1, 4
	};
	whenNew(CoupleJauge.class).withArguments(3, 1, 2, 4).thenReturn(santeInitRM3);//on n'utilisera pas la jauge mais il en faut bien une pour éviter les NullPointerException à la création
	Perso perso = new Perso(traits, arbreMock);
	Assert.assertEquals(traits[0], perso.getTrait(0));
	Assert.assertEquals(traits[1], perso.getTrait(1));
	Assert.assertEquals(traits[2], perso.getTrait(2));
	Assert.assertEquals(traits[3], perso.getTrait(3));
	Assert.assertEquals(traits[4], perso.getTrait(4));
    }

    @Test
    public void testPersoParRMErreur()
    {
	//Cas d'erreur : RM négatif
	try
	{
	    new Perso(-11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}

	//Cas d'erreur : RM nul
	try
	{
	    new Perso(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:0", e.getMessage());
	}
    }

    @Test
    public void testPersoParRMNominal() throws Exception
    {
	//Cas nominal RM4 : vérification des appels d'ArbreDomaines
	whenNew(CoupleJauge.class).withArguments(4, 3, 3, 4).thenReturn(santeInitRM3);//on n'utilisera pas la jauge mais il en faut bien une pour éviter les NullPointerException à la création
	new Perso(4);

	verify(arbreMock).setRangDomaine(3, 4);
	verify(arbreMock).setRangComp(3, 2, 4);//la liste mock contient à ce moment 3 items
	verify(arbreMock, never()).setRangComp(3, 3, 4);//et donc on ne va pas jusqu'à un éventuel quatrième
	verify(arbreMock).setRangDomaine(4, 4);
	verify(arbreMock).setRangDomaine(2, 4);
	verify(arbreMock).setRangComp(2, 0, 4);
	verify(arbreMock, never()).setRangComp(2, 1, 4);//seule esquive est montée dans ce domaine
    }

    @Test
    public void testAddSpecialite()
    {
	//test appel effectif des méthodes de l'ArbreDomaines
	persoRM3.addSpecialite(3, 1, "gneuh");
	verify(arbreMock).addSpecialite(3, 1, "gneuh");
    }

    @Test
    public void testAgirEnCombatErreur()
    {
	try
	{
	    persoRM1.agirEnCombat(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}

	try
	{
	    persoRM1.agirEnCombat(11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:11", e.getMessage());
	}
    }

    @Test
    public void testAgirEnCombatNominal()
    {
	//On teste la "consommation" des actions pour des persos ayant des valeurs d'initiative différentes
	persoRM1.agirEnCombat(persoRM1.getActions().get(0));
	Assert.assertEquals(11, persoRM1.getActions().get(0).intValue());

	persoRM3.agirEnCombat(persoRM3.getActions().get(0));
	Assert.assertEquals(11, persoRM3.getActions().get(0).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(1));
	Assert.assertEquals(11, persoRM3.getActions().get(1).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(2));
	Assert.assertEquals(11, persoRM3.getActions().get(2).intValue());
    }

    @Test
    public void testAttaquerCaCAvecArme()
    {
	//On mocke une arme avec physique min de 2 et malus aux jets de 1
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(armeMock.getCategorie()).thenReturn(3);
	when(armeMock.getphysMin()).thenReturn(2);
	when(armeMock.getMalusAttaque()).thenReturn(1);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(0);
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(10);
	when(arbreMock.effectuerJetComp(1, 3, 6, 12, -1, 0, -10, true)).thenReturn(resultMock);

	//On mocke le isSonne() des deux jauges avec
	when(santeInitRM1.isSonne()).thenReturn(false);
	when(fatigueFARM1.isSonne()).thenReturn(true);

	//On fait effectuer une attaque à la hache à un perso RM1 en prenant en compte le physique minimal de 2 et le malus à l'attaque de 1
	RollUtils.RollResult resultat = persoRM1.attaquerCaC(persoRM1.getActions().get(0), 12);

	Assert.assertEquals(0, resultat.getNbIncrements());
	Assert.assertEquals(10, resultat.getScoreBrut());
	Assert.assertEquals(false, resultat.isJetReussi());

	verify(arbreMock).effectuerJetComp(1, 3, 6, 12, -1, 0, -10, true);
    }

    @Test
    public void testAttaquerCaCMainsNues()
    {
	//On mocke un inventaire vide pour forcer le combat à mains nues
	when(inventaireMock.getArmeCourante()).thenReturn(null);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(3);
	when(resultMock.isJetReussi()).thenReturn(true);
	when(resultMock.getScoreBrut()).thenReturn(28);
	when(arbreMock.effectuerJetComp(3, 3, 0, 30, 0, 0, 0, false)).thenReturn(resultMock);

	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(false);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On fait effectuer une attaque à mains nues à un perso RM3
	RollUtils.RollResult resultat = persoRM3.attaquerCaC(persoRM3.getActions().get(0), 30);

	Assert.assertEquals(3, resultat.getNbIncrements());
	Assert.assertEquals(28, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());

	verify(arbreMock).effectuerJetComp(3, 3, 0, 30, 0, 0, 0, false);
    }

    @Test
    public void testAttaquerDistErreur()
    {
	//On mocke un inventaire contenant un mock d'arme
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getCategorie()).thenReturn(3);
	when(armeMock.getPortee()).thenReturn(100);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//cas d'erreur : rafale avec arme ayant plusieurs munitions mais incapable de tirer en mode automatique (pistolet)
	try
	{
	    persoRM3.attaquerDist(persoRM3.getActions().get(0), 0, 10, 9);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:9", e.getMessage());
	}

	//NOTE : les cas suivants fonctionnent car ces tests sont effectués avant qu'il ne soit vérifié si l'arme peut tirer en rafale, si l'ordre des vérifications change dans le code il faudra modifier le mock
	//cas d'erreur : portée insufissante entraînant échec auto
	persoRM3.genInit();
	Assert.assertFalse(persoRM3.attaquerDist(persoRM3.getActions().get(0), 0, 200, 1).isJetReussi());

	//cas d'erreur : plus de 20 balles
	persoRM3.genInit();
	try
	{
	    persoRM3.attaquerDist(persoRM3.getActions().get(0), 0, 100, 21);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:100 nombre de coups:21", e.getMessage());
	}

	//cas d'erreur : nb de balles nul
	persoRM3.genInit();
	try
	{
	    persoRM3.attaquerDist(persoRM3.getActions().get(0), 0, 0, 0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:0 nombre de coups:0", e.getMessage());
	}

	//cas d'erreur : distance négative
	persoRM3.genInit();
	try
	{
	    persoRM3.attaquerDist(persoRM3.getActions().get(0), 0, -1, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:distance:-1 nombre de coups:1", e.getMessage());
	}

	//cas d'erreur : hors portée donc échec auto
	persoRM3.genInit();
	Assert.assertFalse(persoRM3.attaquerDist(persoRM3.getActions().get(0), 0, 150, 1).isJetReussi());
    }

    @Test
    public void testAttaquerDistNominal()
    {
	//On mocke un inventaire contenant un mock d'arme capable de tirer en mode automatique
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getCategorie()).thenReturn(4);
	when(armeMock.getPortee()).thenReturn(100);
	when(armeMock.getMalusCourt()).thenReturn(-5);
	when(armeMock.getMalusLong()).thenReturn(10);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(false);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(1);
	when(resultMock.isJetReussi()).thenReturn(true);
	when(resultMock.getScoreBrut()).thenReturn(22);
	when(arbreMock.effectuerJetComp(3, 4, 4, 50, 0, 0, +5, false)).thenReturn(resultMock);

	//Cas coup par coup, portée courte (pile la moitié pour vérifier l'arrondi et le "inférieur ou égal"
	RollUtils.RollResult resultat = persoRM3.attaquerDist(persoRM3.getActions().get(0), 50, 20, 1);

	Assert.assertEquals(1, resultat.getNbIncrements());
	Assert.assertEquals(22, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());
	verify(arbreMock).effectuerJetComp(3, 4, 4, 50, 0, 0, +5, false);

	//On ne teste plus les retours : on sait qu'il fonctionne
	//Cas rafale courte, portée longue (juste 1 au dessus de la moitié)
	persoRM3.genInit();
	persoRM3.attaquerDist(persoRM3.getActions().get(0), 20, 51, 3);
	verify(arbreMock).effectuerJetComp(3, 4, 4, 20, 2, 0, -10, false);

	//Cas rafale moyenne (8 coups donc 2 tranches entières de 6), portée courte
	persoRM3.genInit();
	persoRM3.attaquerDist(persoRM3.getActions().get(0), 12, 25, 8);
	verify(arbreMock).effectuerJetComp(3, 4, 4, 12, 4, 0, +5, false);

	//Cas rafale longue(19 coups donc 3 tranches entières de 5), portée longue, physique minimal dépassé, malus au jet d'attaque
	when(armeMock.getphysMin()).thenReturn(5);
	when(armeMock.getMalusAttaque()).thenReturn(2);

	persoRM3.genInit();
	persoRM3.attaquerDist(persoRM3.getActions().get(0), 17, 75, 19);
	verify(arbreMock).effectuerJetComp(3, 4, 4, 17, 1, 3, -30, false);
    }

    @Test
    public void effectuerJetCompTest()
    {
	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(true);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	new Perso(3).effectuerJetComp(4, 1, 1, 49, -2, +1, +7);
	verify(arbreMock).effectuerJetComp(2, 1, 1, 49, -2, +1, +7, true);
    }

    @Test
    public void testEtreBlesseErreur()
    {
	//cas d'erreur : dégâts négatifs
	Degats degatsMock1 = PowerMockito.mock(Degats.class);
	when(degatsMock1.getQuantite()).thenReturn(-2);
	when(degatsMock1.getTypeArme()).thenReturn(2);
	try
	{
	    persoRM3.etreBlesse(degatsMock1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:-2 type:2", e.getMessage());
	}

	//cas d'erreur : type d'arme négatif
	Degats degatsMock2 = PowerMockito.mock(Degats.class);
	when(degatsMock2.getQuantite()).thenReturn(33);
	when(degatsMock2.getTypeArme()).thenReturn(-42);
	try
	{
	    persoRM3.etreBlesse(degatsMock2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:33 type:-42", e.getMessage());
	}
    }

    @Test
    public void testEtreBlesseNominal()
    {
	//On mocke un inventaire contenant une armure
	Armure armureMock = PowerMockito.mock(Armure.class);
	when(armureMock.getRedDegats(2)).thenReturn(5);
	when(inventaireMock.getArmure()).thenReturn(armureMock);

	Degats degatsMock = PowerMockito.mock(Degats.class);
	when(degatsMock.getQuantite()).thenReturn(23);
	when(degatsMock.getTypeArme()).thenReturn(2);
	persoRM3.etreBlesse(degatsMock);

	verify(santeInitRM3).recevoirDegats(18, persoRM3);

	//cas limite : les dégâts à 0 pasent sans causer d'effet
	when(degatsMock.getQuantite()).thenReturn(0);
	when(degatsMock.getTypeArme()).thenReturn(0);
	persoRM1.etreBlesse(degatsMock);
	verify(santeInitRM1, never()).recevoirDegats(0, persoRM1);
	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());
    }

    @Test
    public void testToutesMethodesInitErreur()
    {
	//cas d'erreur sur isActif
	try
	{
	    persoRM3.isActif(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}

	try
	{
	    persoRM3.isActif(11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:11", e.getMessage());
	}
    }

    @Test
    public void testToutesMethodesInitNominal()//petite exception aux règles des tests unitaires car il est difficile de mocker les méthodes statiques de la classe statique RollUtils, on vérifie donc de façon statistique que les résultats sont dans les normes
    {
	for (int j = 0; j <= 4; j++)//5 tests de suite pour être sur
	{
	    persoRM3.genInit();
	    ArrayList<Integer> result = persoRM3.getActions();
	    for (int i = 0; i <= 2; i++)//for classique car l'on veut forcer la vérification de trois cases du tableau
	    {
		Assert.assertTrue(result.get(i) < 11 && result.get(i) > 0); //on vérifie que les init générées ne sont pas ridicules.
	    }
	}

	Assert.assertEquals((int) persoRM1.getActions().get(0), (int) persoRM1.getInitTotale());//son init de base
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);//on ajoute une arme avec bonus d'init de 2
	when(armeMock.getBonusInit()).thenReturn(2);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);
	Assert.assertEquals((int) persoRM1.getActions().get(0) + 10, (int) persoRM1.getInitTotale());//son init améliorée par un bonus d'init de 2

	//test de isActif()
	Assert.assertTrue(persoRM1.isActif(persoRM1.getActions().get(0)));
	Assert.assertTrue(persoRM3.isActif(persoRM3.getActions().get(0)));
    }

    @Test
    public void testGenererDegatsErreur()
    {
	//cas d'erreur : incréments négatifs
	try
	{
	    persoRM1.genererDegats(-1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:incréments:-1", e.getMessage());
	}
    }

    @Test
    public void testGenererDegatsCaCNominal()
    {
	//On mocke le isSonne() des deux jauges
	when(santeInitRM1.isSonne()).thenReturn(false);
	when(fatigueFARM1.isSonne()).thenReturn(false);

	//On mocke un inventaire contenant un mock d'arme
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//Cas nominal
	persoRM1.genererDegats(3);
	verify(armeMock).genererDegats(3, 1, false);
    }

    @Test
    public void testGenererDegatsDist()
    {
	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(true);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On mocke un inventaire contenant un mock d'arme
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getMode()).thenReturn(1);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//Cas nominal
	persoRM3.genererDegats(3);
	verify(armeMock).genererDegats(3, true);
    }

    @Test
    public void testGetNDPassif()
    {
	//On mocke un inventaire contenant une armure
	Armure armureMock = PowerMockito.mock(Armure.class);
	when(armureMock.getBonusND(3)).thenReturn(7);
	when(armureMock.getMalusEsquive()).thenReturn(4);
	when(armureMock.getMalusParade()).thenReturn(2);
	when(inventaireMock.getArmure()).thenReturn(armureMock);

	//esquive
	when(arbreMock.getRangComp(2, 0)).thenReturn(2);//compétence possédée sans bonus de rang
	Assert.assertEquals(18, persoRM1.getNDPassif(3, 0, true));
	when(arbreMock.getRangComp(2, 0)).thenReturn(3);//compétence possédée avec bonus de rang
	Assert.assertEquals(28, persoRM3.getNDPassif(3, 0, true));
	when(arbreMock.getRangComp(2, 0)).thenReturn(0);//compétence non possédée
	when(arbreMock.getRangDomaine(2)).thenReturn(1);
	Assert.assertEquals(8, persoRM3.getNDPassif(3, 0, true));

	//parade
	when(arbreMock.getRangComp(3, 3)).thenReturn(1);//compétence possédée sans bonus de rang
	Assert.assertEquals(15, persoRM1.getNDPassif(3, 1, false));
	when(arbreMock.getRangComp(3, 3)).thenReturn(3);//compétence possédée avec bonus de rang
	Assert.assertEquals(30, persoRM3.getNDPassif(3, 1, false));
	when(arbreMock.getRangComp(3, 3)).thenReturn(0);//compétence non possédée
	when(arbreMock.getRangDomaine(3)).thenReturn(3);
	Assert.assertEquals(15, persoRM3.getNDPassif(3, 1, false));
    }

    @Test
    public void testGetSetDiversErreur()
    {//si non déjà testés dans les autres méthodes de cette classe
	try
	{
	    persoRM3.setTrait(-1, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    persoRM3.setTrait(6, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    persoRM3.getTrait(-1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    persoRM3.getTrait(6);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
    }

    @Test
    public void testGetSetDiversNominal()
    {//si non déjà testés dans les autres méthodes de cette classe

	//méthodes liées aux jauges
	when(santeInitRM3.getRemplissage_interne()).thenReturn(2);
	Assert.assertEquals(2, persoRM3.getBlessuresGraves());

	when(santeInitRM3.getBlessuresLegeres()).thenReturn(23);
	Assert.assertEquals(23, persoRM3.getBlessuresLegeres());

	when(fatigueFARM3.getRemplissage_interne()).thenReturn(5);
	Assert.assertEquals(5, persoRM3.getPointsDeFatigue());

	when(fatigueFARM3.getBlessuresLegeres()).thenReturn(27);
	Assert.assertEquals(27, persoRM3.getBlessuresLegeresMentales());

	when(fatigueFARM3.isElimine()).thenReturn(true);
	when(santeInitRM3.isElimine()).thenReturn(true);
	Assert.assertTrue(persoRM3.isElimine());

	when(fatigueFARM3.isElimine()).thenReturn(true);
	when(santeInitRM3.isElimine()).thenReturn(false);
	Assert.assertTrue(persoRM3.isElimine());

	when(fatigueFARM3.isElimine()).thenReturn(false);
	when(santeInitRM3.isElimine()).thenReturn(true);
	Assert.assertTrue(persoRM3.isElimine());

	when(fatigueFARM3.isElimine()).thenReturn(false);
	when(santeInitRM3.isElimine()).thenReturn(false);
	Assert.assertFalse(persoRM3.isElimine());

	when(fatigueFARM3.isInconscient()).thenReturn(true);
	when(santeInitRM3.isInconscient()).thenReturn(true);
	Assert.assertTrue(persoRM3.isInconscient());

	when(fatigueFARM3.isInconscient()).thenReturn(true);
	when(santeInitRM3.isInconscient()).thenReturn(false);
	Assert.assertTrue(persoRM3.isInconscient());

	when(fatigueFARM3.isInconscient()).thenReturn(false);
	when(santeInitRM3.isInconscient()).thenReturn(true);
	Assert.assertTrue(persoRM3.isInconscient());

	when(fatigueFARM3.isInconscient()).thenReturn(false);
	when(santeInitRM3.isInconscient()).thenReturn(false);
	Assert.assertFalse(persoRM3.isInconscient());

	when(fatigueFARM3.isSonne()).thenReturn(true);
	when(santeInitRM3.isSonne()).thenReturn(true);
	Assert.assertTrue(persoRM3.isSonne());

	when(fatigueFARM3.isSonne()).thenReturn(true);
	when(santeInitRM3.isSonne()).thenReturn(false);
	Assert.assertTrue(persoRM3.isSonne());

	when(fatigueFARM3.isSonne()).thenReturn(false);
	when(santeInitRM3.isSonne()).thenReturn(true);
	Assert.assertTrue(persoRM3.isSonne());

	when(fatigueFARM3.isSonne()).thenReturn(false);
	when(santeInitRM3.isSonne()).thenReturn(false);
	Assert.assertFalse(persoRM3.isSonne());

	//méthodes liées à l'inventaire
	Assert.assertEquals(inventaireMock, persoRM3.getInventaire());

	//méthodes de libellés
	Assert.assertEquals("PersoRM3", persoRM3.toString());
	persoRM3.setLibellePerso("Gongnafrug");
	Assert.assertEquals("Gongnafrug", persoRM3.toString());

	//méthodes liées à l'ArbreDomaines
	when(arbreMock.getRangComp(2, 3)).thenReturn(2);
	Assert.assertEquals(2, persoRM3.getRangComp(2, 3));

	when(arbreMock.getRangDomaine(5)).thenReturn(5);
	Assert.assertEquals(5, persoRM3.getRangDomaine(5));

	ArrayList<String> listSpe = new ArrayList<>();
	listSpe.add("spe1");
	listSpe.add("spe2");
	when(arbreMock.getSpecialites(1, 1)).thenReturn(listSpe);
	Assert.assertEquals(listSpe, persoRM3.getSpecialites(1, 1));

	persoRM3.removeSpecialite(5, 7, 8);
	verify(arbreMock).removeSpecialite(5, 7, 8);

	persoRM3.setRangComp(1, 2, 3);
	verify(arbreMock).setRangComp(1, 2, 3);

	persoRM3.setRangDomaine(78, 72);
	verify(arbreMock).setRangDomaine(78, 72);

	//méthodes liées aux traits
	Assert.assertEquals(3, persoRM3.getTrait(0));
	Assert.assertEquals(3, persoRM3.getTrait(1));
	Assert.assertEquals(2, persoRM3.getTrait(2));
	Assert.assertEquals(2, persoRM3.getTrait(3));
	Assert.assertEquals(2, persoRM3.getTrait(4));

	Assert.assertEquals(1, persoRM1.getTrait(0));
	Assert.assertEquals(1, persoRM1.getTrait(1));
	Assert.assertEquals(0, persoRM1.getTrait(2));
	Assert.assertEquals(0, persoRM1.getTrait(3));
	Assert.assertEquals(0, persoRM1.getTrait(4));

	persoRM3.setTrait(1, 1);
	Assert.assertEquals(1, persoRM3.getTrait(1));
    }
}
