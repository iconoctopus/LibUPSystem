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
import java.util.ListIterator;
import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.COORDINATION;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.MENTAL;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PHYSIQUE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PRESENCE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.VOLONTE;
import org.duckdns.spacedock.upengine.libupsystem.Perso.Degats;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
	    Perso.class, GroupeTraits.class, UPReferenceSysteme.class, RollUtils.RollResult.class, Armure.class, Degats.class, CoupleJauge.class, Inventaire.class, ArbreDomaines.class, ArmeDist.class
	})
public class UnitPersoTest
{

    private UPReferenceSysteme referenceMock;
    private CoupleJauge santeInitRM1;
    private CoupleJauge santeInitRM3;
    private CoupleJauge fatigueFARM1;
    private CoupleJauge fatigueFARM3;
    private GroupeTraits traitsRM3;
    private GroupeTraits traitsRM1;
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

	//on construit une liste de domaines retournée par la référence mockée, cinq suffiront
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

	//on mocke deux groupes de traits : un pour le perso RM1 et l'autre pour le perso RM3
	traitsRM1 = PowerMockito.mock(GroupeTraits.class);
	traitsRM3 = PowerMockito.mock(GroupeTraits.class);
	whenNew(GroupeTraits.class).withArguments(3, 4, 2, 2, 2).thenReturn(traitsRM3);
	when(traitsRM3.getTrait(Trait.PHYSIQUE)).thenReturn(3);
	when(traitsRM3.getTrait(Trait.COORDINATION)).thenReturn(4);
	when(traitsRM3.getTrait(Trait.MENTAL)).thenReturn(2);
	when(traitsRM3.getTrait(Trait.VOLONTE)).thenReturn(2);
	when(traitsRM3.getTrait(Trait.PRESENCE)).thenReturn(2);

	whenNew(GroupeTraits.class).withArguments(2, 2, 2, 2, 2).thenReturn(traitsRM1);
	when(traitsRM1.getTrait(Trait.PHYSIQUE)).thenReturn(2);
	when(traitsRM1.getTrait(Trait.COORDINATION)).thenReturn(2);
	when(traitsRM1.getTrait(Trait.MENTAL)).thenReturn(2);
	when(traitsRM1.getTrait(Trait.VOLONTE)).thenReturn(2);
	when(traitsRM1.getTrait(Trait.PRESENCE)).thenReturn(2);

	//on mocke deux groupes de jauges, un pour un perso RM1, l'autre pour un perso RM3
	santeInitRM1 = PowerMockito.mock(CoupleJauge.class);
	santeInitRM3 = PowerMockito.mock(CoupleJauge.class);
	fatigueFARM1 = PowerMockito.mock(CoupleJauge.class);
	fatigueFARM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(2, 2, 2, 2).thenReturn(santeInitRM1);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2, 4).thenReturn(santeInitRM3);
	whenNew(CoupleJauge.class).withArguments(2, 2, 2).thenReturn(fatigueFARM1);
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
    public void testPersoParCaracsNominal() throws Exception
    {
	GroupeTraits traits = PowerMockito.mock(GroupeTraits.class);

	when(traits.getTrait(Trait.PHYSIQUE)).thenReturn(3);
	when(traits.getTrait(Trait.COORDINATION)).thenReturn(4);
	when(traits.getTrait(Trait.MENTAL)).thenReturn(2);
	when(traits.getTrait(Trait.VOLONTE)).thenReturn(1);
	when(traits.getTrait(Trait.PRESENCE)).thenReturn(4);

	whenNew(CoupleJauge.class).withArguments(3, 1, 2, 4).thenReturn(santeInitRM3);//on n'utilisera pas la jauge mais il en faut bien une pour éviter les NullPointerException à la création
	Perso perso = new Perso(traits, arbreMock);
	Assert.assertEquals(3, perso.getTrait(Trait.PHYSIQUE));
	Assert.assertEquals(4, perso.getTrait(Trait.COORDINATION));
	Assert.assertEquals(2, perso.getTrait(Trait.MENTAL));
	Assert.assertEquals(1, perso.getTrait(Trait.VOLONTE));
	Assert.assertEquals(4, perso.getTrait(Trait.PRESENCE));
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

	//Cas d'erreur : RM >9
	try
	{
	    new Perso(10);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:10", e.getMessage());
	}
    }

    @Test
    public void testPersoParRMNominal() throws Exception
    {
	//Cas nominal RM3 : vérification des appels d'ArbreDomaines et GroupeTraits
	Perso persoTest = new Perso(3);

	verify(arbreMock, times(2)).setRangDomaine(3, 4);
	verify(arbreMock, times(2)).setRangComp(3, 2, 4);//la liste mock contient à ce moment 3 items
	verify(arbreMock, never()).setRangComp(3, 3, 4);//et donc on ne va pas jusqu'à un éventuel quatrième
	verify(arbreMock, times(2)).setRangDomaine(4, 4);

	assertEquals(3, persoTest.getTrait(PHYSIQUE));
	assertEquals(4, persoTest.getTrait(COORDINATION));
	assertEquals(2, persoTest.getTrait(MENTAL));
	assertEquals(2, persoTest.getTrait(VOLONTE));
	assertEquals(2, persoTest.getTrait(PRESENCE));
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
	//On mocke une arme avec physique min de 3 et malus aux jets de 1
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(armeMock.getCategorie()).thenReturn(3);
	when(armeMock.getphysMin()).thenReturn(3);
	when(armeMock.getMalusAttaque()).thenReturn(1);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(0);
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(10);
	when(arbreMock.effectuerJetComp(2, 3, 6, 12, -1, 0, -10, true)).thenReturn(resultMock);

	//On mocke le isSonne() des deux jauges avec
	when(santeInitRM1.isSonne()).thenReturn(false);
	when(fatigueFARM1.isSonne()).thenReturn(true);

	//On fait effectuer une attaque à la hache à un perso RM1 en prenant en compte le physique minimal de 2 et le malus à l'attaque de 1
	RollUtils.RollResult resultat = persoRM1.attaquerCaC(persoRM1.getActions().get(0), 12);

	Assert.assertEquals(0, resultat.getNbIncrements());
	Assert.assertEquals(10, resultat.getScoreBrut());
	Assert.assertEquals(false, resultat.isJetReussi());

	verify(arbreMock).effectuerJetComp(2, 3, 6, 12, -1, 0, -10, true);
	verify(traitsRM3, times(2)).getTrait(COORDINATION);
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
	when(arbreMock.effectuerJetComp(4, 3, 0, 30, 0, 0, 0, false)).thenReturn(resultMock);

	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(false);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On fait effectuer une attaque à mains nues à un perso RM3
	RollUtils.RollResult resultat = persoRM3.attaquerCaC(persoRM3.getActions().get(0), 30);

	Assert.assertEquals(3, resultat.getNbIncrements());
	Assert.assertEquals(28, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());

	verify(traitsRM3, times(3)).getTrait(COORDINATION);
	verify(arbreMock).effectuerJetComp(4, 3, 0, 30, 0, 0, 0, false);
    }

    @Test
    public void testAttaquerDist()
    {
	//On mocke un inventaire contenant un mock d'arme ainsi qu'un mock de rapport de distance
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);
	ArmeDist.DistReport reportMock = PowerMockito.mock(ArmeDist.DistReport.class);

	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(false);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollUtils.RollResult resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(1);
	when(resultMock.isJetReussi()).thenReturn(true);
	when(resultMock.getScoreBrut()).thenReturn(22);
	when(arbreMock.effectuerJetComp(4, 4, 0, 50, -2, 0, -15, false)).thenReturn(resultMock);

	//nombre aberrant de coups (17) mais traité comme coup par coup (juste là pour vérifier le bon passage des arguments), physique minimal dépassé, malus au jet d'attaque
	when(armeMock.getphysMin()).thenReturn(5);
	when(armeMock.getMalusAttaque()).thenReturn(2);
	when(armeMock.verifPreAttaque(20, 17)).thenReturn(reportMock);
	when(reportMock.isEchecAuto()).thenReturn(false);
	when(reportMock.getModDesGardes()).thenReturn(0);
	when(reportMock.getModDesLances()).thenReturn(0);
	when(reportMock.getModJet()).thenReturn(+5);
	RollUtils.RollResult resultat = persoRM3.attaquerDist(persoRM3.getActions().get(0), 50, 20, 17);//cas limite important : l'arme est de catégorie 0 mais elle n'est pas "mains nues" car elle est à distance, les vérifications de "mains nues" doivent ne pas pêter

	verify(armeMock).verifPreAttaque(20, 17);
	verify(reportMock).isEchecAuto();
	verify(reportMock).getModDesGardes();
	verify(reportMock).getModDesLances();
	verify(reportMock).getModJet();
	verify(traitsRM3, times(3)).getTrait(COORDINATION);
	verify(arbreMock).effectuerJetComp(4, 4, 0, 50, -2, 0, -15, false);//+5 pour portée courte et -10 pour défaut de physique
	Assert.assertEquals(1, resultat.getNbIncrements());
	Assert.assertEquals(22, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());

	//cas de l'échec auto, toutes choses étant égales par ailleurs
	when(reportMock.isEchecAuto()).thenReturn(true);
	resultat = persoRM3.attaquerDist(persoRM3.getActions().get(1), 50, 20, 17);
	Assert.assertEquals(0, resultat.getNbIncrements());
	Assert.assertEquals(0, resultat.getScoreBrut());
	Assert.assertEquals(false, resultat.isJetReussi());
    }

    @Test
    public void effectuerJetCompTest()
    {
	//On mocke le isSonne() des deux jauges
	when(santeInitRM3.isSonne()).thenReturn(true);
	when(fatigueFARM3.isSonne()).thenReturn(false);

	new Perso(3).effectuerJetComp(Trait.PRESENCE, 1, 1, 49, -2, +1, +7);
	verify(arbreMock).effectuerJetComp(2, 1, 1, 49, -2, +1, +7, true);
	verify(traitsRM3, times(3)).getTrait(PRESENCE);
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
    public void testEffectuerJetTrait()
    {
	persoRM3.effectuerJetTrait(MENTAL, 7);
	verify(traitsRM3).effectuerJetTrait(MENTAL, 7, false);
	when(fatigueFARM3.isSonne()).thenReturn(true);
	persoRM3.effectuerJetTrait(MENTAL, 7);
	verify(traitsRM3).effectuerJetTrait(MENTAL, 7, true);
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
    public void testToutesMethodesInitNominal()//petite exception aux règles des tests unitaires car il est difficile de mocker les méthodes statiques de la classe statique RollUtils, on vérifie donc simplement que les résultats sont dans les normes
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
	Assert.assertTrue(persoRM3.isActif(persoRM3.getActions().get(0)));
	ListIterator<Integer> listActions = persoRM3.getActions().listIterator();

	int phaseCourante = 1;
	while (phaseCourante < 11)
	{
	    if (listActions.hasNext() && phaseCourante == (int) listActions.next())
	    {
		assertTrue(persoRM3.isActif(phaseCourante));
		persoRM3.agirEnCombat(phaseCourante);//on consomme l'action
		if (listActions.hasNext() && phaseCourante == (int) listActions.next())//deuxième vérification car il peut très bien y avoir deux actions dans la même phase
		{
		    --phaseCourante;//on annule la progression de phase qui va avoir lieu
		}
		listActions.previous();//on annule le next du test que l'on vient de faire
	    }
	    else
	    {
		listActions.previous();//on annule le next() indu car la phase courante n'était pas une phase d'action du perso, il ne faut donc pas dépasser une action légitime
		assertFalse(persoRM3.isActif(phaseCourante));
	    }
	    ++phaseCourante;
	}
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
	//On prévoie les appels aux domaines et compétences
	when(arbreMock.getRangComp(3, 0)).thenReturn(3);
	when(arbreMock.getRangComp(3, 2)).thenReturn(3);
	when(arbreMock.getRangDomaine(3)).thenReturn(3);

	//Cas mains nues
	Degats result = persoRM3.genererDegats(3);
	verify(inventaireMock).getArmeCourante();
	verify(arbreMock).getRangComp(3, 0);
	verify(arbreMock).getRangDomaine(3);
	assertEquals(18, result.getQuantite());
	assertEquals(0, result.getTypeArme());

	//On mocke un inventaire contenant un mock d'arme
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);
	when(armeMock.getVD()).thenReturn(7);
	when(armeMock.getTypeArme()).thenReturn(3);
	when(armeMock.getMode()).thenReturn(0);
	when(armeMock.getCategorie()).thenReturn(1);

	//Cas nominal
	result = persoRM3.genererDegats(2);
	verify(armeMock).getVD();
	verify(armeMock).getTypeArme();
	verify(armeMock).getMode();
	verify(armeMock).getCategorie();
	verify(arbreMock).getRangComp(3, 2);
	verify(arbreMock, times(2)).getRangDomaine(3);
	verify(traitsRM3, times(7)).getTrait(PHYSIQUE);
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
	when(inventaireMock.getArmeCourante()).thenReturn(armeMock);
	when(armeMock.getVD()).thenReturn(5);
	when(armeMock.getTypeArme()).thenReturn(2);
	when(armeMock.getMode()).thenReturn(1);
	when(armeMock.getCategorie()).thenReturn(2);

	//Cas nominal
	Degats result = persoRM1.genererDegats(0);
	verify(armeMock).getVD();
	verify(armeMock).getTypeArme();
	verify(armeMock).getMode();
	verify(armeMock).getCategorie();
	verify(arbreMock).getRangComp(4, 2);
	verify(arbreMock).getRangDomaine(4);
	assertEquals(7, result.getQuantite());
	assertEquals(2, result.getTypeArme());
    }

    @Test
    public void testGetNDPassifErreur()
    {
	try
	{
	    persoRM3.getDefense(0, -1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    assertEquals("paramétre aberrant:nombre d'adversaires au delà du premier:-1", e.getMessage());
	}
    }

    @Test
    public void testGetNDPassifNominal()
    {
	//On mocke un inventaire contenant une armure
	Armure armureMock = PowerMockito.mock(Armure.class);
	when(armureMock.getBonusND(3)).thenReturn(7);
	when(inventaireMock.getArmure()).thenReturn(armureMock);

	//le perso de RM3 est sonné pour vérifier que la règle est bien appliquée
	when(fatigueFARM3.isSonne()).thenReturn(true);

	//sans adversaire supplémentaire
	Assert.assertEquals(22, persoRM1.getDefense(3, 0));
	verify(armureMock).getBonusND(3);
	Assert.assertEquals(27, persoRM3.getDefense(3, 0));

	//avec adversaires supplémentaire (mais calcul normal)
	Assert.assertEquals(20, persoRM1.getDefense(3, 1));
	Assert.assertEquals(21, persoRM3.getDefense(3, 3));

	//avec adversaires supplémentaire entraînant défense <5 donc application du minimum
	Assert.assertEquals(5, persoRM1.getDefense(3, 9));//positif mais < 5
	Assert.assertEquals(5, persoRM3.getDefense(3, 52));//négatif
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
	Assert.assertEquals(3, persoRM3.getTrait(Trait.PHYSIQUE));
	verify(traitsRM3, times(5)).getTrait(PHYSIQUE);
	Assert.assertEquals(4, persoRM3.getTrait(Trait.COORDINATION));
	verify(traitsRM3, times(3)).getTrait(COORDINATION);
	Assert.assertEquals(2, persoRM3.getTrait(Trait.MENTAL));
	verify(traitsRM3, times(3)).getTrait(MENTAL);
	Assert.assertEquals(2, persoRM3.getTrait(Trait.VOLONTE));
	verify(traitsRM3, times(4)).getTrait(VOLONTE);
	Assert.assertEquals(2, persoRM3.getTrait(Trait.PRESENCE));

	Assert.assertEquals(2, persoRM1.getTrait(Trait.PHYSIQUE));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.COORDINATION));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.MENTAL));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.VOLONTE));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.PRESENCE));

	persoRM3.setTrait(Trait.COORDINATION, 1);
	verify(traitsRM3).setTrait(COORDINATION, 1);
    }

    @Test
    public void testDegatsErreur()
    {
	try
	{
	    new Degats(-1, 2);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:-1 type:2", e.getMessage());
	}

	try
	{
	    new Degats(2, -1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:2 type:-1", e.getMessage());
	}
    }
}
