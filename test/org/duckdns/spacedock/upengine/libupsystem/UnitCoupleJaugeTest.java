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

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReferenceSysteme n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{//les classes final, appelant du statique et les classes subissant un whennew
	    UPReferenceSysteme.class, CoupleJauge.class, RollUtils.RollResult.class, Perso.class
	})
public class UnitCoupleJaugeTest
{

    static CoupleJauge jaugeSI;
    static CoupleJauge jaugeFFA;
    private Perso persoMock;
    private UPReferenceSysteme referenceMock;
    private RollUtils.RollResult resultMock;

    @Before
    public void setUp()
    {
	//on mocke la référence
	referenceMock = PowerMockito.mock(UPReferenceSysteme.class);
	PowerMockito.mockStatic(UPReferenceSysteme.class);
	when(UPReferenceSysteme.getInstance()).thenReturn(referenceMock);

	//On mocke un personnage
	persoMock = PowerMockito.mock(Perso.class);

	//On fabrique un mock de résultat de jet
	resultMock = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);

	//On mocke les deux jauges avec les modificateurs issus de la référence
	when(referenceMock.getInitModCoord(1)).thenReturn(2);
	when(referenceMock.getInitModMental(1)).thenReturn(-1);
	jaugeSI = new CoupleJauge(1, 1, 1, 1);
	verify(referenceMock).getInitModCoord(1);
	verify(referenceMock).getInitModMental(1);
	jaugeFFA = new CoupleJauge(5, 3, 3);
    }

    @Test
    public void testGettersPostCreation()
    {
	//Jauge de santé/initiative
	Assert.assertFalse(jaugeSI.isElimine());
	Assert.assertFalse(jaugeSI.isInconscient());
	Assert.assertFalse(jaugeSI.isSonne());
	Assert.assertEquals(2, jaugeSI.getTaille_interne());
	Assert.assertEquals(1, jaugeSI.getTaille_externe());
	Assert.assertEquals(0, jaugeSI.getRemplissage_interne());
	Assert.assertEquals(1, jaugeSI.getRemplissage_externe());

	//Jauge de fatigue/force d'âme
	Assert.assertFalse(jaugeFFA.isElimine());
	Assert.assertFalse(jaugeFFA.isInconscient());
	Assert.assertFalse(jaugeFFA.isSonne());
	Assert.assertEquals(8, jaugeFFA.getTaille_interne());
	Assert.assertEquals(3, jaugeFFA.getTaille_externe());
	Assert.assertEquals(0, jaugeFFA.getRemplissage_interne());
	Assert.assertEquals(3, jaugeFFA.getRemplissage_externe());
    }

    @Test
    public void testCreaJaugeFatigueForceDameErreur()
    {
	try
	{
	    new CoupleJauge(3, 2, -1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:3 volonté:2 trait le plus faible:-1", e.getMessage());
	}
	try
	{
	    new CoupleJauge(-3, 2, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:-3 volonté:2 trait le plus faible:1", e.getMessage());
	}
	try
	{
	    new CoupleJauge(3, -2, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:3 volonté:-2 trait le plus faible:1", e.getMessage());
	}
    }

    @Test
    public void testCreaJaugeSanteInitErreur()
    {
	try
	{
	    new CoupleJauge(3, -2, 1, 5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:3 volonté:-2 mental:1 coordination:5", e.getMessage());
	}
	try
	{
	    new CoupleJauge(-3, 2, 1, 5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:-3 volonté:2 mental:1 coordination:5", e.getMessage());
	}
	try
	{
	    new CoupleJauge(3, 2, -1, 5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:3 volonté:2 mental:-1 coordination:5", e.getMessage());
	}
	try
	{
	    new CoupleJauge(3, 2, 1, -5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:physique:3 volonté:2 mental:1 coordination:-5", e.getMessage());
	}
    }

    @Test
    public void gestionDegatsErreur()
    {
	try
	{
	    jaugeFFA.recevoirDegats(-1, persoMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:dégâts:-1", e.getMessage());
	}
    }

    @Test
    public void gesTionDegatsNominalReussite()
    {
	//Cas du jet d'absorption réussi : on doit retrouver des blessures légères
	when(resultMock.isJetReussi()).thenReturn(true);

	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 20)).thenReturn(resultMock);
	jaugeFFA.recevoirDegats(20, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 20);
	Assert.assertEquals(20, jaugeFFA.getBlessuresLegeres());
	Assert.assertEquals(0, jaugeFFA.getRemplissage_interne());//pas encore affectée
	Assert.assertEquals(3, jaugeFFA.getRemplissage_externe());//pas encore affectée
	Assert.assertFalse(jaugeFFA.isElimine());//tout va bien
	Assert.assertFalse(jaugeFFA.isInconscient());//tout va bien
	Assert.assertFalse(jaugeFFA.isSonne());//tout va bien

	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 50)).thenReturn(resultMock);
	jaugeSI.recevoirDegats(50, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 50);
	Assert.assertEquals(50, jaugeSI.getBlessuresLegeres());
	Assert.assertEquals(0, jaugeSI.getRemplissage_interne());//pas encore affectée
	Assert.assertEquals(1, jaugeSI.getRemplissage_externe());//pas encore affectée
	Assert.assertFalse(jaugeSI.isElimine());//tout va bien
	Assert.assertFalse(jaugeSI.isInconscient());//la jauge est plus petite, le personnage est sonné
    }

    @Test
    public void gesTionDegatsNominalEchecSimpleEtSonne()
    {
	//Cas du jet raté de moins de 10
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(21);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 30)).thenReturn(resultMock);

	//d'abord la jauge de Fatigue/force d'âme : très grosse, elle encaissera bien
	jaugeFFA.recevoirDegats(30, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 30);
	Assert.assertEquals(0, jaugeFFA.getBlessuresLegeres());
	Assert.assertEquals(1, jaugeFFA.getRemplissage_interne());//on doit retrouver une blessure grave
	Assert.assertEquals(3, jaugeFFA.getRemplissage_externe());//pas encore affectée
	Assert.assertFalse(jaugeFFA.isElimine());//tout va bien
	Assert.assertFalse(jaugeFFA.isInconscient());//tout va bien
	Assert.assertFalse(jaugeFFA.isSonne());//tout va bien

	//Ensuite la petite jauge de santé/init
	when(resultMock.getScoreBrut()).thenReturn(16);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 20)).thenReturn(resultMock);
	jaugeSI.recevoirDegats(20, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 20);
	Assert.assertEquals(0, jaugeSI.getBlessuresLegeres());
	Assert.assertEquals(1, jaugeSI.getRemplissage_interne());//on doit retrouver une blessure grave
	Assert.assertEquals(1, jaugeSI.getRemplissage_externe());//pas encore affectée
	Assert.assertFalse(jaugeSI.isElimine());//tout va bien
	Assert.assertFalse(jaugeSI.isInconscient());//la jauge est plus petite, le personnage est sonné

	//Cas du jet raté de plus de 10 sans chute dans l'inconscience
	when(resultMock.getScoreBrut()).thenReturn(7);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 23)).thenReturn(resultMock);
	jaugeFFA.recevoirDegats(23, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 23);
	Assert.assertEquals(0, jaugeFFA.getBlessuresLegeres());
	Assert.assertEquals(3, jaugeFFA.getRemplissage_interne());//on doit retrouver deux blessures graves de plus
	Assert.assertEquals(3, jaugeFFA.getRemplissage_externe());//pas encore affectée
	Assert.assertFalse(jaugeFFA.isElimine());//tout va bien
	Assert.assertFalse(jaugeFFA.isInconscient());//tout va bien
	Assert.assertTrue(jaugeFFA.isSonne());//le personnage est sonné car volonté de 3
    }

    @Test
    public void gesTionDegatsNominalEchecInconscience()
    {
	//On rajoute deux blessures graves à la petite jauge de santé/init pour générer une insconscience auto sans mort
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(29);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 40)).thenReturn(resultMock);

	RollUtils.RollResult resultMockMort = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMockMort.isJetReussi()).thenReturn(true);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 10)).thenReturn(resultMockMort);//réussite du jet de mort, le perso reste en vie

	jaugeSI.recevoirDegats(40, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 40);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 10);
	Assert.assertEquals(0, jaugeSI.getBlessuresLegeres());
	Assert.assertEquals(2, jaugeSI.getRemplissage_interne());//on doit retrouver deux blessures graves
	Assert.assertEquals(0, jaugeSI.getRemplissage_externe());//au tapis...
	Assert.assertFalse(jaugeSI.isElimine());//point encore...
	Assert.assertTrue(jaugeSI.isInconscient());//même si le jet était réussi, la jauge est remplie
	Assert.assertTrue(jaugeSI.isSonne());//inconscient donc sonné

	//Inconscience sur jet raté
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(4);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 55)).thenReturn(resultMock);

	RollUtils.RollResult resultMockInconscience = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMockInconscience.isJetReussi()).thenReturn(false);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.VOLONTE, 30)).thenReturn(resultMockInconscience);//échec du jet d'inconscience

	when(resultMockMort.isJetReussi()).thenReturn(true);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 30)).thenReturn(resultMockMort);//réussite du jet de mort, le perso reste en vie

	jaugeFFA.recevoirDegats(55, persoMock);//on inflige un total de 6 blessures graves pour voir les effets sur la jauge externe
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 55);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.VOLONTE, 30);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 30);
	Assert.assertEquals(0, jaugeFFA.getBlessuresLegeres());
	Assert.assertEquals(6, jaugeFFA.getRemplissage_interne());//on doit retrouver six blessures graves en tout
	Assert.assertEquals(2, jaugeFFA.getRemplissage_externe());//affectée d'un rang
	Assert.assertFalse(jaugeFFA.isElimine());//tout va bien
	Assert.assertTrue(jaugeFFA.isInconscient());//oui car jet raté même si jauge non remplie
	Assert.assertTrue(jaugeFFA.isSonne());//oui car point de choc dépassé
    }

    @Test
    public void gesTionDegatsNominalEchecMort()
    {
	//mort auto
	//On rajoute trois blessures graves à la petite jauge de santé/init pour générer un débordement donc mort auto
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(19);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 40)).thenReturn(resultMock);

	jaugeSI.recevoirDegats(40, persoMock);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 40);
	Assert.assertEquals(0, jaugeSI.getBlessuresLegeres());
	Assert.assertEquals(2, jaugeSI.getRemplissage_interne());//on doit retrouver deux blessures graves car le total est ramené à la taille de la jauge
	Assert.assertEquals(0, jaugeSI.getRemplissage_externe());//au tapis...
	Assert.assertTrue(jaugeSI.isElimine());//oui, perso mort
	Assert.assertTrue(jaugeSI.isInconscient());//même si le jet était réussi, la jauge déborde
	Assert.assertTrue(jaugeSI.isSonne());//inconscient donc sonné

	//mort sur jet échoué
	when(resultMock.isJetReussi()).thenReturn(false);
	when(resultMock.getScoreBrut()).thenReturn(4);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 55)).thenReturn(resultMock);

	RollUtils.RollResult resultMockInconscience = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMockInconscience.isJetReussi()).thenReturn(false);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.VOLONTE, 30)).thenReturn(resultMockInconscience);//échec du jet d'inconscience

	RollUtils.RollResult resultMockMort = PowerMockito.mock(RollUtils.RollResult.class);
	PowerMockito.mockStatic(RollUtils.RollResult.class);
	when(resultMockMort.isJetReussi()).thenReturn(false);
	when(persoMock.effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 30)).thenReturn(resultMockMort);//échec du jet de mort, le perso trépasse

	jaugeFFA.recevoirDegats(55, persoMock);//on inflige un total de 6 blessures graves pour voir les effets sur la jauge externe
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 55);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.VOLONTE, 30);
	verify(persoMock).effectuerJetTrait(GroupeTraits.Trait.PHYSIQUE, 30);
	Assert.assertEquals(0, jaugeFFA.getBlessuresLegeres());
	Assert.assertEquals(6, jaugeFFA.getRemplissage_interne());//on doit retrouver six blessures graves en tout
	Assert.assertEquals(2, jaugeFFA.getRemplissage_externe());//affectée d'un rang
	Assert.assertTrue(jaugeFFA.isElimine());//échec au jet donc mort
	Assert.assertTrue(jaugeFFA.isInconscient());//oui car jet raté même si jauge non remplie
	Assert.assertTrue(jaugeFFA.isSonne());//oui car point de choc dépassé
    }
}
