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
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
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
	    Domaine.class, UPReferenceSysteme.class, Competence.class
	})
public class UnitDomaineTest
{

    private Domaine domaine;
    private Domaine domaineCac;
    private UPReferenceSysteme referenceMock;
    private static ArrayList<String> listComp = new ArrayList<>();
    private Competence competenceMock;

    @BeforeClass
    public static void setUpClass()
    {
	//on construit une liste de compétences retournée par la référence mockée
	listComp.add("comp1");
	listComp.add("comp2");
	listComp.add("comp3");
    }

    @Before
    public void setUp() throws Exception
    {
	//on mocke la référence
	referenceMock = PowerMockito.mock(UPReferenceSysteme.class);
	PowerMockito.mockStatic(UPReferenceSysteme.class);
	when(UPReferenceSysteme.getInstance()).thenReturn(referenceMock);
	when(referenceMock.getListComp(0)).thenReturn(listComp);//on renvoie la liste de trois compétences quand la référence est interrogée

	//On mocke une competence pour intercepter les appels
	competenceMock = PowerMockito.mock(Competence.class);
	whenNew(Competence.class).withAnyArguments().thenReturn(competenceMock);

	domaine = new Domaine(0, 4);
    }

    @Test
    public void testDomaineErreur()
    {
	try
	{
	    new Domaine(-1, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:indice:-1", e.getMessage());
	}
    }

    @Test
    public void testRangCompetenceErreur()
    {
	//Hors du tableau des compétences sur get
	try
	{
	    domaine.getRangComp(3);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//Hors du tableau des compétences sur set
	try
	{
	    domaine.setRangComp(3, 2);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//Set avec rang supérieur au domaine
	try
	{
	    domaine.setRangComp(1, 5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang compétence > rang domaine", e.getMessage());
	}
    }

    @Test
    public void testRangCompetenceNominal()
    {
	when(competenceMock.getRang()).thenReturn(7);
	Assert.assertEquals(7, domaine.getRangComp(2));
	verify(competenceMock).getRang();
	domaine.setRangComp(1, 2);
	verify(competenceMock).setRang(2);
    }

    @Test
    public void testRangErreur()
    {
	try
	{
	    domaine.setRang(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:0", e.getMessage());
	}

	try
	{
	    domaine.setRang(-3);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-3", e.getMessage());
	}
    }

    @Test
    public void testRangNominal()
    {
	Assert.assertEquals(4, domaine.getRang());
	domaine.setRang(3);
	Assert.assertEquals(3, domaine.getRang());
    }

    @Test
    public void testEffectuerJetCompErreur()
    {
	//cas d'erreur sur paramétre de compétence
	try
	{
	    domaine.effectuerJetComp(1, -1, 0, 0, 0, 0, false);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:1 domaine:4 indice compétence:-1", e.getMessage());
	}

	//cas d'erreur sur paramétre de trait
	try
	{
	    domaine.effectuerJetComp(-1, 1, 0, 0, 0, 0, false);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:-1 domaine:4 indice compétence:1", e.getMessage());
	}
    }

    @Test
    public void testSpecialites()
    {
	//test appel effectif des méthodes de la compétence, on ne teste pas le out of bounds car le nombre de compétences est déjà testé dans la méthode sur les rangs de compétence
	domaine.addSpecialite(1, "gneuh");
	verify(competenceMock).addSpecialite("gneuh");
	domaine.removeSpecialite(2, 8);
	verify(competenceMock).removeSpecialite(8);
    }
}
