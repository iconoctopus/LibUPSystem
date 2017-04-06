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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
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
	    EnsembleJauges.EtatVital.class, GroupeTraits.class, RollUtils.RollResult.class, EnsembleJauges.class, CoupleJauges.class
	})
public class EnsembleJaugesTest
{

    private EnsembleJauges jaugesRM1;
    private EnsembleJauges jaugesRM3;
    private GroupeTraits traitsRM3Mock;
    private GroupeTraits traitsRM1Mock;
    private CoupleJauges santeInitRM1Mock;
    private CoupleJauges santeInitRM3Mock;
    private CoupleJauges fatigueFARM1Mock;
    private CoupleJauges fatigueFARM3Mock;

    @Before
    public void setUp() throws Exception
    {
	//on mocke deux groupes de traits
	traitsRM3Mock = PowerMockito.mock(GroupeTraits.class);
	when(traitsRM3Mock.getTrait(GroupeTraits.Trait.PHYSIQUE)).thenReturn(3);
	when(traitsRM3Mock.getTrait(GroupeTraits.Trait.COORDINATION)).thenReturn(4);
	when(traitsRM3Mock.getTrait(GroupeTraits.Trait.MENTAL)).thenReturn(2);
	when(traitsRM3Mock.getTrait(GroupeTraits.Trait.VOLONTE)).thenReturn(2);
	when(traitsRM3Mock.getTrait(GroupeTraits.Trait.PRESENCE)).thenReturn(2);

	traitsRM1Mock = PowerMockito.mock(GroupeTraits.class);
	when(traitsRM1Mock.getTrait(GroupeTraits.Trait.PHYSIQUE)).thenReturn(2);
	when(traitsRM1Mock.getTrait(GroupeTraits.Trait.COORDINATION)).thenReturn(2);
	when(traitsRM1Mock.getTrait(GroupeTraits.Trait.MENTAL)).thenReturn(2);
	when(traitsRM1Mock.getTrait(GroupeTraits.Trait.VOLONTE)).thenReturn(2);
	when(traitsRM1Mock.getTrait(GroupeTraits.Trait.PRESENCE)).thenReturn(2);

	//on mocke les jauges associées
	santeInitRM1Mock = PowerMockito.mock(CoupleJauges.class);
	santeInitRM3Mock = PowerMockito.mock(CoupleJauges.class);
	fatigueFARM1Mock = PowerMockito.mock(CoupleJauges.class);
	fatigueFARM3Mock = PowerMockito.mock(CoupleJauges.class);
	whenNew(CoupleJauges.class).withArguments(2, 2, 2, 2).thenReturn(santeInitRM1Mock);
	whenNew(CoupleJauges.class).withArguments(3, 2, 2, 4).thenReturn(santeInitRM3Mock);
	whenNew(CoupleJauges.class).withArguments(2, 2, 2).thenReturn(fatigueFARM1Mock);
	whenNew(CoupleJauges.class).withArguments(3, 2, 2).thenReturn(fatigueFARM3Mock);

	//on mocke les réponses des jauges pour le genInit
	when(santeInitRM1Mock.getRemplissage_externe()).thenReturn(1);//pas la bonne valeur pour ce RM dans l'absolu mais on s'en moque, c'est du mock
	when(santeInitRM3Mock.getRemplissage_externe()).thenReturn(3);

	//on crée les ensembles de test à partir de ce qui a été défini ci-dessus
	jaugesRM3 = new EnsembleJauges(traitsRM3Mock);
	jaugesRM1 = new EnsembleJauges(traitsRM1Mock);
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testAgirEnCombatErreur()
    {
	try
	{
	    jaugesRM1.agirEnCombat(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}

	try
	{
	    jaugesRM1.agirEnCombat(11);
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
	jaugesRM1.agirEnCombat(jaugesRM1.getActions().get(0));
	Assert.assertEquals(11, jaugesRM1.getActions().get(0).intValue());

	jaugesRM3.agirEnCombat(jaugesRM3.getActions().get(0));
	Assert.assertEquals(11, jaugesRM3.getActions().get(0).intValue());
	jaugesRM3.agirEnCombat(jaugesRM3.getActions().get(1));
	Assert.assertEquals(11, jaugesRM3.getActions().get(1).intValue());
	jaugesRM3.agirEnCombat(jaugesRM3.getActions().get(2));
	Assert.assertEquals(11, jaugesRM3.getActions().get(2).intValue());
    }

    @Test
    public void testToutesMethodesInitErreur()
    {
	//cas d'erreur sur isActif
	try
	{
	    jaugesRM3.isActif(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}

	try
	{
	    jaugesRM3.isActif(11);
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
	    jaugesRM3.genInit();
	    ArrayList<Integer> result = jaugesRM3.getActions();
	    for (int i = 0; i <= 2; i++)//for classique car l'on veut forcer la vérification de trois cases du tableau
	    {
		Assert.assertTrue(result.get(i) < 11 && result.get(i) > 0); //on vérifie que les init générées ne sont pas ridicules.
	    }
	}

	Assert.assertEquals((int) jaugesRM1.getActions().get(0), (int) jaugesRM1.getInitTotale(null));//son init de base
	ArmeCaC armeMock = PowerMockito.mock(ArmeCaC.class);//on ajoute une arme avec bonus d'init de 2
	when(armeMock.getBonusInit()).thenReturn(2);
	Assert.assertEquals((int) jaugesRM1.getActions().get(0) + 10, (int) jaugesRM1.getInitTotale(armeMock));//son init améliorée par un bonus d'init de 2

	//test de isActif()
	Assert.assertTrue(jaugesRM3.isActif(jaugesRM3.getActions().get(0)));
	ListIterator<Integer> listActions = jaugesRM3.getActions().listIterator();

	int phaseCourante = 1;
	while (phaseCourante < 11)
	{
	    if (listActions.hasNext() && phaseCourante == (int) listActions.next())
	    {
		assertTrue(jaugesRM3.isActif(phaseCourante));
		jaugesRM3.agirEnCombat(phaseCourante);//on consomme l'action
		if (listActions.hasNext() && phaseCourante == (int) listActions.next())//deuxième vérification car il peut très bien y avoir deux actions dans la même phase
		{
		    --phaseCourante;//on annule la progression de phase qui va avoir lieu
		}
		listActions.previous();//on annule le next du test que l'on vient de faire
	    }
	    else
	    {
		listActions.previous();//on annule le next() indu car la phase courante n'était pas une phase d'action du perso, il ne faut donc pas dépasser une action légitime
		assertFalse(jaugesRM3.isActif(phaseCourante));
	    }
	    ++phaseCourante;
	}
    }

    @Test
    public void testGetSetDiversNominal()
    {//si non déjà testés dans les autres méthodes de cette classe

	//méthodes liées aux jauges
	when(santeInitRM3Mock.getRemplissage_interne()).thenReturn(2);
	Assert.assertEquals(2, jaugesRM3.getEtatVital().getBlessuresGraves());

	when(santeInitRM3Mock.getBlessuresLegeres()).thenReturn(23);
	Assert.assertEquals(23, jaugesRM3.getEtatVital().getBlessuresLegeres());

	when(fatigueFARM3Mock.getRemplissage_interne()).thenReturn(5);
	Assert.assertEquals(5, jaugesRM3.getEtatVital().getPtsFatigue());

	when(fatigueFARM3Mock.getBlessuresLegeres()).thenReturn(27);
	Assert.assertEquals(27, jaugesRM3.getEtatVital().getBlessuresLegeresMentales());

	when(fatigueFARM3Mock.isElimine()).thenReturn(true);
	when(santeInitRM3Mock.isElimine()).thenReturn(true);
	Assert.assertTrue(jaugesRM3.getEtatVital().isElimine());

	when(fatigueFARM3Mock.isElimine()).thenReturn(true);
	when(santeInitRM3Mock.isElimine()).thenReturn(false);
	Assert.assertTrue(jaugesRM3.getEtatVital().isElimine());

	when(fatigueFARM3Mock.isElimine()).thenReturn(false);
	when(santeInitRM3Mock.isElimine()).thenReturn(true);
	Assert.assertTrue(jaugesRM3.getEtatVital().isElimine());

	when(fatigueFARM3Mock.isElimine()).thenReturn(false);
	when(santeInitRM3Mock.isElimine()).thenReturn(false);
	Assert.assertFalse(jaugesRM3.getEtatVital().isElimine());

	when(fatigueFARM3Mock.isInconscient()).thenReturn(true);
	when(santeInitRM3Mock.isInconscient()).thenReturn(true);
	Assert.assertTrue(jaugesRM3.getEtatVital().isInconscient());

	when(fatigueFARM3Mock.isInconscient()).thenReturn(true);
	when(santeInitRM3Mock.isInconscient()).thenReturn(false);
	Assert.assertTrue(jaugesRM3.getEtatVital().isInconscient());

	when(fatigueFARM3Mock.isInconscient()).thenReturn(false);
	when(santeInitRM3Mock.isInconscient()).thenReturn(true);
	Assert.assertTrue(jaugesRM3.getEtatVital().isInconscient());

	when(fatigueFARM3Mock.isInconscient()).thenReturn(false);
	when(santeInitRM3Mock.isInconscient()).thenReturn(false);
	Assert.assertFalse(jaugesRM3.getEtatVital().isInconscient());

	when(fatigueFARM3Mock.isSonne()).thenReturn(true);
	when(santeInitRM3Mock.isSonne()).thenReturn(true);
	Assert.assertTrue(jaugesRM3.getEtatVital().isSonne());

	when(fatigueFARM3Mock.isSonne()).thenReturn(true);
	when(santeInitRM3Mock.isSonne()).thenReturn(false);
	Assert.assertTrue(jaugesRM3.getEtatVital().isSonne());

	when(fatigueFARM3Mock.isSonne()).thenReturn(false);
	when(santeInitRM3Mock.isSonne()).thenReturn(true);
	Assert.assertTrue(jaugesRM3.getEtatVital().isSonne());

	when(fatigueFARM3Mock.isSonne()).thenReturn(false);
	when(santeInitRM3Mock.isSonne()).thenReturn(false);
	Assert.assertFalse(jaugesRM3.getEtatVital().isSonne());
    }
}
