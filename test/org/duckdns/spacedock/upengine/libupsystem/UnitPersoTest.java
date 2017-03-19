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
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReference n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{
	    Perso.class, UPReference.class, RollUtils.RollResult.class, Armure.class, Degats.class
	})
public class UnitPersoTest
{

    private UPReference referenceMock;
    private CoupleJauge santeInitRM1;
    private CoupleJauge santeInitRM3;
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
	referenceMock = PowerMockito.mock(UPReference.class);
	PowerMockito.mockStatic(UPReference.class);
	when(UPReference.getInstance()).thenReturn(referenceMock);

	//on mocke deux jauges, une pour un perso RM1, l'autre pour un perso RM3
	santeInitRM1 = PowerMockito.mock(CoupleJauge.class);
	santeInitRM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0, 1).thenReturn(santeInitRM1);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2, 3).thenReturn(santeInitRM3);

	//La référence mockée renvoie les listes de domaines et compétence prédéfinies
	when(referenceMock.getListDomaines()).thenReturn(listDom);
	when(referenceMock.getListComp(2)).thenReturn(listComp);
	when(referenceMock.getListComp(3)).thenReturn(listComp);
	when(referenceMock.getListComp(4)).thenReturn(listComp);

	//les jauges mockées renvoient des valeurs d'init
	when(santeInitRM1.getRemplissage_externe()).thenReturn(1);
	when(santeInitRM3.getRemplissage_externe()).thenReturn(3);
    }

    @Test
    public void testPersoParCaracs() throws Exception
    {
	//on mocke un arbre de domaines
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

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
    public void testPersoParRM() throws Exception
    {
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

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

	//Cas nominal RM3 : vérification des appels d'ArbreDomaines et de coupleJauge
	new Perso(3);

	verify(arbreMock).setRangDomaine(3, 3);
	verify(arbreMock).setRangComp(3, 2, 3);//la liste mock contient à ce moment 3 items
	verify(arbreMock, never()).setRangComp(3, 3, 3);//et donc on ne va pas jusqu'à un éventuel quatrième
	verify(arbreMock).setRangDomaine(4, 3);
	verify(arbreMock).setRangDomaine(2, 3);
	verify(arbreMock).setRangComp(2, 0, 3);
	verify(arbreMock, never()).setRangComp(2, 1, 3);//seule esquive est montée dans ce domaine
    }

    @Test
    public void testAddSpecialite() throws Exception
    {
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	Perso perso = new Perso(3);

	//test appel effectif des méthodes de l'ArbreDomaines
	perso.addSpecialite(3, 1, "gneuh");
	verify(arbreMock).addSpecialite(3, 1, "gneuh");
    }

    @Test
    public void testAgirEnCombat() throws Exception
    {
	Perso persoRM1 = new Perso(1);
	Perso persoRM3 = new Perso(3);

	//On teste la "consommation" des actions pour des persos ayant des valeurs d'initiative différentes
	persoRM1.agirEnCombat(persoRM1.getActions().get(0));
	Assert.assertEquals(11, persoRM1.getActions().get(0).intValue());

	persoRM3.agirEnCombat(persoRM3.getActions().get(0));
	Assert.assertEquals(11, persoRM3.getActions().get(0).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(1));
	Assert.assertEquals(11, persoRM3.getActions().get(1).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(2));
	Assert.assertEquals(11, persoRM3.getActions().get(2).intValue());

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
    public void testAttaquerCaCAvecArme() throws Exception
    {
	//On mocke un inventaire contenant un mock d'arme avec physique min de 2 et malus aux jets de 1
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(armeMock.getCategorie()).thenReturn(3);
	when(armeMock.getphysMin()).thenReturn(2);
	when(armeMock.getMalusAttaque()).thenReturn(1);

	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//On mocke un arbre domaine
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(0);
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(10);
	when(arbreMock.effectuerJetComp(1, 3, 6, 12, -1, 0, -10, true)).thenReturn(resultMock);

	//On mocke une jauge de fatigue/force d'âme et le isSonne() des deux jauges avec
	CoupleJauge fatigueFARM1 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0).thenReturn(fatigueFARM1);
	when(santeInitRM1.isSonne()).thenReturn(false);
	when(fatigueFARM1.isSonne()).thenReturn(true);

	//On crée un perso RM1 et on lui fait effectuer une attaque à la hache en prenant en compte le physique minimal de 2 et le malus à l'attaque de 1
	Perso persoRM1 = new Perso(1);
	RollUtils.RollResult resultat = persoRM1.attaquerCaC(persoRM1.getActions().get(0), 12);

	Assert.assertEquals(0, resultat.getNbIncrements());
	Assert.assertEquals(10, resultat.getScoreBrut());
	Assert.assertEquals(false, resultat.isJetReussi());

	verify(arbreMock).effectuerJetComp(1, 3, 6, 12, -1, 0, -10, true);
    }

    @Test
    public void testAttaquerCaCMainsNues() throws Exception
    {
	//On mocke un inventaire vide pour forcer le combat à mains nues
	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmeCourante()).thenReturn(null);

	//On mocke un arbre domaine
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(3);
	when(resultMock.isJetReussi()).thenReturn(true);
	when(resultMock.getScoreBrut()).thenReturn(28);
	when(arbreMock.effectuerJetComp(3, 3, 0, 30, 0, 0, 0, false)).thenReturn(resultMock);

	//On mocke une jauge de fatigue/force d'âme et le isSonne() des deux jauges avec
	CoupleJauge fatigueFARM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2).thenReturn(fatigueFARM3);
	when(santeInitRM3.isSonne()).thenReturn(false);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On crée un perso RM3 et on lui fait effectuer une attaque à mains nues
	Perso persoRM3 = new Perso(3);
	RollUtils.RollResult resultat = persoRM3.attaquerCaC(persoRM3.getActions().get(0), 30);

	Assert.assertEquals(3, resultat.getNbIncrements());
	Assert.assertEquals(28, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());

	verify(arbreMock).effectuerJetComp(3, 3, 0, 30, 0, 0, 0, false);
    }

    @Test
    public void testAttaquerDistErreur() throws Exception
    {
	//On mocke un inventaire contenant un mock d'arme
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getCategorie()).thenReturn(3);
	when(armeMock.getPortee()).thenReturn(100);

	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	Perso persoRM3 = new Perso(3);

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
    public void testAttaquerDistNominal() throws Exception
    {
	//On mocke un inventaire contenant un mock d'arme capable de tirer en mode automatique
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getCategorie()).thenReturn(4);
	when(armeMock.getPortee()).thenReturn(100);
	when(armeMock.getMalusCourt()).thenReturn(-5);
	when(armeMock.getMalusLong()).thenReturn(10);

	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//On mocke un arbre domaine
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	//On mocke une jauge de fatigue/force d'âme et le isSonne() des deux jauges avec
	CoupleJauge fatigueFARM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2).thenReturn(fatigueFARM3);
	when(santeInitRM3.isSonne()).thenReturn(false);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(1);
	when(resultMock.isJetReussi()).thenReturn(true);
	when(resultMock.getScoreBrut()).thenReturn(22);
	when(arbreMock.effectuerJetComp(3, 4, 4, 50, 0, 0, +5, false)).thenReturn(resultMock);

	Perso persoRM3 = new Perso(3);

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
    public void effectuerJetCompTest() throws Exception
    {
	//On mocke un arbre domaine
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	//On mocke une jauge de fatigue/force d'âme et le isSonne() des deux jauges avec
	CoupleJauge fatigueFARM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0).thenReturn(fatigueFARM3);
	when(santeInitRM3.isSonne()).thenReturn(true);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	new Perso(3).effectuerJetComp(4, 1, 1, 49, -2, +1, +7);
	verify(arbreMock).effectuerJetComp(2, 1, 1, 49, -2, +1, +7, true);
    }

    @Test
    public void testEtreBlesse() throws Exception
    {
	//On mocke un inventaire contenant une armure
	Armure armureMock = PowerMockito.mock(Armure.class);
	when(armureMock.getRedDegats(2)).thenReturn(5);

	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmure()).thenReturn(armureMock);

	Perso perso = new Perso(3);

	//cas d'erreur : dégâts négatifs
	Degats degatsMock1 = PowerMockito.mock(Degats.class);
	when(degatsMock1.getQuantite()).thenReturn(-2);
	when(degatsMock1.getTypeArme()).thenReturn(2);
	try
	{
	    perso.etreBlesse(degatsMock1);
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
	    perso.etreBlesse(degatsMock2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:33 type:-42", e.getMessage());
	}
	//cas nominal
	Degats degatsMock = PowerMockito.mock(Degats.class);
	when(degatsMock.getQuantite()).thenReturn(23);
	when(degatsMock.getTypeArme()).thenReturn(2);
	perso.etreBlesse(degatsMock);

	verify(santeInitRM3).recevoirDegats(18, perso);
    }

    @Test
    public void testGetGenInit()//petite exception aux règles des tests unitaires car il est difficile de mocker les méthodes statiques de la classe statique RollUtils, on vérifie donc de façon statistique que les résultats sont dans les normes
    {
	Perso perso = new Perso(3);
	for (int j = 0; j <= 4; j++)//5 tests de suite pour être sur
	{
	    perso.genInit();
	    ArrayList<Integer> result = perso.getActions();
	    for (int i = 0; i <= 2; i++)//for classique car l'on veut forcer la vérification de trois cases du tableau
	    {
		Assert.assertTrue(result.get(i) < 11 && result.get(i) > 0); //on vérifie que les init générées ne sont pas ridicules.
	    }
	}
    }

    @Test
    public void testGenererDegatsCaC() throws Exception
    {
	//On mocke une jauge de fatigue/force d'âme et le isSonne() des deux jauges avec
	CoupleJauge fatigueFARM1 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0).thenReturn(fatigueFARM1);
	when(santeInitRM1.isSonne()).thenReturn(false);
	when(fatigueFARM1.isSonne()).thenReturn(false);

	//On mocke un inventaire contenant un mock d'arme
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);

	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	Perso persoRM1 = new Perso(1);

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

	//Cas nominal
	persoRM1.genererDegats(3);
	verify(armeMock).genererDegats(3, 1, false);
    }

    @Test
    public void testGenererDegatsDist() throws Exception
    {
	//On mocke une jauge de fatigue/force d'âme et le isSonne() des deux jauges avec
	CoupleJauge fatigueFARM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2).thenReturn(fatigueFARM3);
	when(santeInitRM3.isSonne()).thenReturn(true);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On mocke un inventaire contenant un mock d'arme
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(armeMock.getMode()).thenReturn(1);

	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	Perso persoRM3 = new Perso(3);

	//Cas nominal
	persoRM3.genererDegats(3);
	verify(armeMock).genererDegats(3, true);
    }

}
