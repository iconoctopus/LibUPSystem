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
import org.duckdns.spacedock.upengine.libupsystem.EnsembleJauges.EtatVital;
import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.COORDINATION;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.MENTAL;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PHYSIQUE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PRESENCE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.VOLONTE;
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
	    ArmeMainsNues.class, Perso.class, EtatVital.class, GroupeTraits.class, UPReferenceSysteme.class, RollGenerator.RollResult.class, Armure.class, Degats.class, EnsembleJauges.class, ArbreDomaines.class, ArmeDist.class
	})
public class UnitPersoTest
{

    private UPReferenceSysteme referenceMock;
    private EtatVital etatRM1;
    private EtatVital etatRM3;
    private EnsembleJauges jaugesRM1;
    private EnsembleJauges jaugesRM3;
    private GroupeTraits traitsRM3;
    private GroupeTraits traitsRM1;
    private ArbreDomaines arbreMock;
    private Perso persoRM1;
    private Perso persoRM3;
    private static final ArrayList<String> listComp = new ArrayList<>();
    private static final ArrayList<String> listDom = new ArrayList<>();
    private static final ArrayList<Integer> listRM1 = new ArrayList<>();
    private static final ArrayList<Integer> listRM3 = new ArrayList<>();

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

	//on construit deux listes d'actions retournées par les ensembles de jauges mockés
	listRM1.add(2);
	listRM3.add(1);
	listRM3.add(2);
	listRM3.add(3);
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
	jaugesRM1 = PowerMockito.mock(EnsembleJauges.class);
	jaugesRM3 = PowerMockito.mock(EnsembleJauges.class);
	whenNew(EnsembleJauges.class).withArguments(traitsRM1).thenReturn(jaugesRM1);
	whenNew(EnsembleJauges.class).withArguments(traitsRM3).thenReturn(jaugesRM3);

	//les jauges mockées renvoient des états vitaux
	etatRM1 = PowerMockito.mock(EtatVital.class);
	when(jaugesRM1.getEtatVital()).thenReturn(etatRM1);
	etatRM3 = PowerMockito.mock(EtatVital.class);
	when(jaugesRM3.getEtatVital()).thenReturn(etatRM3);

	//les jauges mockées gèrent l'init
	when(jaugesRM1.getActions()).thenReturn(listRM1);
	when(jaugesRM1.agirEnCombat(2)).thenReturn(true);

	when(jaugesRM3.getActions()).thenReturn(listRM3);
	when(jaugesRM3.agirEnCombat(1)).thenReturn(true);
	when(jaugesRM3.agirEnCombat(2)).thenReturn(true);
	when(jaugesRM3.agirEnCombat(3)).thenReturn(true);

	//La référence mockée renvoie les listes de domaines et compétence prédéfinies
	when(referenceMock.getListDomaines()).thenReturn(listDom);
	when(referenceMock.getListComp(2)).thenReturn(listComp);
	when(referenceMock.getListComp(3)).thenReturn(listComp);
	when(referenceMock.getListComp(4)).thenReturn(listComp);

	//on mocke un arbre de domaines
	arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

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
    public void testAttaquerCaCAvecArme()
    {
	//On mocke une arme avec physique min de 3 et malus aux jets de 1
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	when(armeMock.getCategorie()).thenReturn(3);
	when(armeMock.getphysMin()).thenReturn(3);
	when(armeMock.getMalusAttaque()).thenReturn(1);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollGenerator.RollResult resultMock = PowerMockito.mock(RollGenerator.RollResult.class);
	PowerMockito.mockStatic(RollGenerator.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(0);
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(10);
	when(arbreMock.effectuerJetComp(2, 3, 6, 12, -1, 0, -10, true)).thenReturn(resultMock);

	//On mocke le isSonne()
	when(etatRM1.isSonne()).thenReturn(true);

	//On fait effectuer une attaque à la hache à un perso RM1 en prenant en compte le physique minimal de 2 et le malus à l'attaque de 1
	RollGenerator.RollResult resultat = persoRM1.attaquerCaC(persoRM1.getActions().get(0), 12, armeMock);
	verify(etatRM1).isSonne();
	Assert.assertEquals(0, resultat.getNbIncrements());
	Assert.assertEquals(10, resultat.getScoreBrut());
	Assert.assertEquals(false, resultat.isJetReussi());

	verify(arbreMock).effectuerJetComp(2, 3, 6, 12, -1, 0, -10, true);
	verify(traitsRM1).getTrait(COORDINATION);
    }

    @Test
    public void testAttaquerCaCMainsNues()
    {
	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollGenerator.RollResult resultMock = PowerMockito.mock(RollGenerator.RollResult.class);
	PowerMockito.mockStatic(RollGenerator.RollResult.class);
	when(resultMock.getNbIncrements()).thenReturn(3);
	when(resultMock.isJetReussi()).thenReturn(true);
	when(resultMock.getScoreBrut()).thenReturn(28);
	when(arbreMock.effectuerJetComp(4, 3, 0, 30, 0, 0, 0, false)).thenReturn(resultMock);

	//On mocke le isSonne()
	when(etatRM3.isSonne()).thenReturn(false);

	//On fait effectuer une attaque à mains nues à un perso RM3
	RollGenerator.RollResult resultat = persoRM3.attaquerCaC(persoRM3.getActions().get(0), 30, null);

	Assert.assertEquals(3, resultat.getNbIncrements());
	Assert.assertEquals(28, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());
	verify(etatRM3).isSonne();
	verify(traitsRM3).getTrait(COORDINATION);
	verify(arbreMock).effectuerJetComp(4, 3, 0, 30, 0, 0, 0, false);
    }

    @Test
    public void testAttaquerDist()
    {
	//On créé mock d'arme ainsi qu'un mock de rapport de distance
	ArmeDist armeMock = PowerMockito.mock(ArmeDist.class);
	ArmeDist.DistReport reportMock = PowerMockito.mock(ArmeDist.DistReport.class);

	//On mocke le isSonne()
	when(etatRM3.isSonne()).thenReturn(false);

	//On mocke un retour pour vérifier qu'il traverse correctement les couches private et on le fait retourner par arbreMock
	RollGenerator.RollResult resultMock = PowerMockito.mock(RollGenerator.RollResult.class);
	PowerMockito.mockStatic(RollGenerator.RollResult.class);
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
	RollGenerator.RollResult resultat = persoRM3.attaquerDist(persoRM3.getActions().get(0), 50, 20, 17, armeMock);//cas limite important : l'arme est de catégorie 0 mais elle n'est pas "mains nues" car elle est à distance, les vérifications de "mains nues" doivent ne pas pêter

	verify(armeMock).verifPreAttaque(20, 17);
	verify(reportMock).isEchecAuto();
	verify(reportMock).getModDesGardes();
	verify(reportMock).getModDesLances();
	verify(reportMock).getModJet();
	verify(traitsRM3).getTrait(COORDINATION);
	verify(arbreMock).effectuerJetComp(4, 4, 0, 50, -2, 0, -15, false);//+5 pour portée courte et -10 pour défaut de physique
	Assert.assertEquals(1, resultat.getNbIncrements());
	Assert.assertEquals(22, resultat.getScoreBrut());
	Assert.assertEquals(true, resultat.isJetReussi());

	//cas de l'échec auto, toutes choses étant égales par ailleurs
	when(reportMock.isEchecAuto()).thenReturn(true);
	resultat = persoRM3.attaquerDist(persoRM3.getActions().get(1), 50, 20, 17, armeMock);
	Assert.assertEquals(0, resultat.getNbIncrements());
	Assert.assertEquals(0, resultat.getScoreBrut());
	Assert.assertEquals(false, resultat.isJetReussi());
    }

    @Test
    public void effectuerJetCompTest()
    {
	//On mocke le isSonne() des deux jauges
	when(etatRM3.isSonne()).thenReturn(true);

	new Perso(3).effectuerJetComp(Trait.PRESENCE, 1, 1, 49, -2, +1, +7);
	verify(arbreMock).effectuerJetComp(2, 1, 1, 49, -2, +1, +7, true);
	verify(traitsRM3).getTrait(PRESENCE);
	verify(etatRM3).isSonne();
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
	    persoRM3.etreBlesse(degatsMock1, null);
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
	    persoRM3.etreBlesse(degatsMock2, null);
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

	Degats degatsMock = PowerMockito.mock(Degats.class);
	when(degatsMock.getQuantite()).thenReturn(23);
	when(degatsMock.getTypeArme()).thenReturn(2);
	persoRM3.etreBlesse(degatsMock, armureMock);

	verify(jaugesRM3).recevoirDegatsPhysiques(18);

	//cas limite : les dégâts à 0 pasent sans causer d'effet
	when(degatsMock.getQuantite()).thenReturn(0);
	when(degatsMock.getTypeArme()).thenReturn(0);
	persoRM1.etreBlesse(degatsMock, armureMock);
	verify(jaugesRM1, never()).recevoirDegatsPhysiques(0);
	Assert.assertEquals(0, persoRM1.getEtatVital().getBlessures());
	Assert.assertEquals(0, persoRM1.getEtatVital().getPointsDegatsPhysiques());
    }

    @Test
    public void testEffectuerJetTrait()
    {
	persoRM3.effectuerJetTrait(MENTAL, 7);
	verify(traitsRM3).effectuerJetTrait(MENTAL, 7, false);
	when(etatRM3.isSonne()).thenReturn(true);
	persoRM3.effectuerJetTrait(MENTAL, 7);
	verify(traitsRM3).effectuerJetTrait(MENTAL, 7, true);
    }

    @Test
    public void testGenererDegatsErreur()
    {
	//cas d'erreur : incréments négatifs
	try
	{
	    persoRM1.genererDegats(-1, null);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:incréments:-1", e.getMessage());
	}
    }

    @Test
    public void testGenererDegatsNominal() throws Exception
    {
	//On prévoie les appels aux domaines et compétences
	when(arbreMock.getRangComp(3, 0)).thenReturn(3);
	when(arbreMock.getRangComp(3, 2)).thenReturn(3);
	when(arbreMock.getRangDomaine(3)).thenReturn(3);

	//Cas standard
	//On mocke une arme et un résultat de dégâts
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);
	Degats degatsMock = PowerMockito.mock(Degats.class);
	when(armeMock.genererDegats(traitsRM3, arbreMock, 3)).thenReturn(degatsMock);

	Degats result = persoRM3.genererDegats(3, armeMock);
	verify(armeMock).genererDegats(traitsRM3, arbreMock, 3);
	assertEquals(degatsMock, result);

	//Cas mains nues
	assertEquals(0, result.getTypeArme());

	ArmeMainsNues mainsNuesMock = PowerMockito.mock(ArmeMainsNues.class);
	when(mainsNuesMock.genererDegats(traitsRM3, arbreMock, 7)).thenReturn(degatsMock);
	whenNew(ArmeMainsNues.class).withArguments(traitsRM3).thenReturn(mainsNuesMock);
	result = persoRM3.genererDegats(7, null);
	assertEquals(result, degatsMock);
    }

    @Test
    public void testGetDefenseErreur()
    {
	try
	{
	    persoRM3.getDefense(0, -1, null);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    assertEquals("paramétre aberrant:nombre d'adversaires au delà du premier:-1", e.getMessage());
	}
    }

    @Test
    public void testGetDefenseNominal()
    {
	//On mocke un inventaire contenant une armure
	Armure armureMock = PowerMockito.mock(Armure.class);
	when(armureMock.getBonusND(3)).thenReturn(7);

	//le perso de RM3 est sonné pour vérifier que la règle est bien appliquée
	when(etatRM3.isSonne()).thenReturn(true);

	//sans adversaire supplémentaire
	Assert.assertEquals(22, persoRM1.getDefense(3, 0, armureMock));
	verify(armureMock).getBonusND(3);
	Assert.assertEquals(27, persoRM3.getDefense(3, 0, armureMock));

	//avec adversaires supplémentaire (mais calcul normal)
	Assert.assertEquals(20, persoRM1.getDefense(3, 1, armureMock));
	Assert.assertEquals(21, persoRM3.getDefense(3, 3, armureMock));

	//avec adversaires supplémentaire entraînant défense <5 donc application du minimum
	Assert.assertEquals(5, persoRM1.getDefense(3, 9, armureMock));//positif mais < 5
	Assert.assertEquals(5, persoRM3.getDefense(3, 52, armureMock));//négatif
    }

    @Test
    public void testGetSetDiversNominal()
    {//si non déjà testés dans les autres méthodes de cette classe

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
	verify(traitsRM3).getTrait(PHYSIQUE);
	Assert.assertEquals(4, persoRM3.getTrait(Trait.COORDINATION));
	verify(traitsRM3).getTrait(COORDINATION);
	Assert.assertEquals(2, persoRM3.getTrait(Trait.MENTAL));
	verify(traitsRM3).getTrait(MENTAL);
	Assert.assertEquals(2, persoRM3.getTrait(Trait.VOLONTE));
	verify(traitsRM3).getTrait(VOLONTE);
	Assert.assertEquals(2, persoRM3.getTrait(Trait.PRESENCE));

	Assert.assertEquals(2, persoRM1.getTrait(Trait.PHYSIQUE));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.COORDINATION));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.MENTAL));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.VOLONTE));
	Assert.assertEquals(2, persoRM1.getTrait(Trait.PRESENCE));

	persoRM3.setTrait(Trait.COORDINATION, 1);
	verify(traitsRM3).setTrait(COORDINATION, 1);

	//méthodes liées aux jauges
	persoRM3.getInitTotale(null);
	verify(jaugesRM3).getInitTotale(null);//cas sans arme équipée
	Arme armeMock = PowerMockito.mock(Arme.class);
	persoRM3.getInitTotale(armeMock);
	verify(jaugesRM3).getInitTotale(armeMock);//cas avec arme équipée

	persoRM1.getEtatVital();
	verify(jaugesRM1).getEtatVital();

	ArrayList<Integer> actions = persoRM3.getActions();
	verify(jaugesRM3).getActions();
	assertEquals(1, actions.get(0).intValue());
	assertEquals(2, actions.get(1).intValue());
	assertEquals(3, actions.get(2).intValue());
	assertEquals(2, persoRM1.getActions().get(0).intValue());

	persoRM1.genInit();
	verify(jaugesRM1).genInit();

	when(jaugesRM1.isActif(2)).thenReturn(true);
	assertTrue(persoRM1.isActif(2));
	assertFalse(persoRM1.isActif(3));
	verify(jaugesRM1).isActif(2);

	persoRM3.agirEnCombat(6);
	verify(jaugesRM3).agirEnCombat(6);
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
