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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * On profite de cette classe pour tester le bout en bout complet : à partir du
 * point d'entrée qu'est la classe Perso
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReferenceSysteme n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{
	    UPReferenceSysteme.class, ArbreDomaines.class, Domaine.class
	})
public class UnitArbreDomainesTest
{

    private UPReferenceSysteme referenceMock;
    private Domaine domaineMock;
    private ArbreDomaines arbreTest;
    private static ArrayList<String> listDom = new ArrayList<>();

    @BeforeClass
    public static void setupClass()
    {
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
	when(referenceMock.getListDomaines()).thenReturn(listDom);

	//on mocke un domaine
	domaineMock = PowerMockito.mock(Domaine.class);
	whenNew(Domaine.class).withAnyArguments().thenReturn(domaineMock);

	arbreTest = new ArbreDomaines();
	verify(referenceMock).getListDomaines();
    }

    @Test
    public void testGetSetRangDomaineErreur()
    {
	//Cas set
	try
	{
	    arbreTest.setRangDomaine(-1, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    arbreTest.setRangDomaine(9, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//Cas get
	try
	{
	    arbreTest.getRangDomaine(9);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbreTest.getRangDomaine(-1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
    }

    @Test
    public void testGetSetRangDomaineNominal()
    {
	//Cas set
	arbreTest.setRangDomaine(0, 5);
	verify(domaineMock).setRang(5);

	//Cas get
	when(domaineMock.getRang()).thenReturn(7);
	Assert.assertEquals(7, arbreTest.getRangDomaine(2));
	verify(domaineMock).getRang();
    }

    @Test
    public void testGetSetRangCompErreur()
    {
	//Cas set
	try
	{
	    arbreTest.setRangComp(9, 0, 0);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbreTest.setRangComp(-1, 0, 0);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//Cas get
	try
	{
	    arbreTest.getRangComp(9, 0);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbreTest.getRangComp(-1, 0);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
    }

    @Test
    public void testGetSetRangCompNominal()
    {
	//Cas set
	arbreTest.setRangComp(1, 0, 2);
	verify(domaineMock).setRangComp(0, 2);

	//Cas get
	when(domaineMock.getRangComp(3)).thenReturn(6);
	Assert.assertEquals(6, arbreTest.getRangComp(1, 3));
	verify(domaineMock).getRangComp(3);
    }

    @Test
    public void testGetAddRemoveSpecialitesErreur()
    {
	//Cas add
	try
	{
	    arbreTest.addSpecialite(9, 1, "bla");
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbreTest.addSpecialite(-1, 1, "bla");
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//Cas get
	try
	{
	    arbreTest.getSpecialites(9, 1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbreTest.getSpecialites(-1, 1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//Cas remove
	try
	{
	    arbreTest.removeSpecialite(9, 1, 1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbreTest.removeSpecialite(-1, 1, 1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
    }

    @Test
    public void testGetAddRemoveSpecialitesNominal()
    {
	//Cas add
	arbreTest.addSpecialite(1, 0, "brossage de dent");
	verify(domaineMock).addSpecialite(0, "brossage de dent");
	arbreTest.addSpecialite(2, 3, "nettoyage de pot de chambre");
	verify(domaineMock).addSpecialite(3, "nettoyage de pot de chambre");
	arbreTest.addSpecialite(4, 6, "gravure sur écorce de cacahuète");
	verify(domaineMock).addSpecialite(6, "gravure sur écorce de cacahuète");

	//Cas get
	ArrayList<String> listSpeTest = new ArrayList<String>();
	listSpeTest.add("danse avec les pingouins");
	listSpeTest.add("récurrage de string");
	listSpeTest.add("échelle à deux mains");
	when(domaineMock.getSpecialites(3)).thenReturn(listSpeTest);
	Assert.assertEquals("danse avec les pingouins", arbreTest.getSpecialites(1, 3).get(0));
	verify(domaineMock).getSpecialites(3);
	Assert.assertEquals("récurrage de string", arbreTest.getSpecialites(2, 3).get(1));//peu importe le domaine interrogé c'est le même mock qui répond
	Assert.assertEquals("échelle à deux mains", arbreTest.getSpecialites(4, 3).get(2));//idem que ligne ci-dessus

	//Cas remove
	arbreTest.removeSpecialite(1, 0, 1);
	verify(domaineMock).removeSpecialite(0, 1);
    }
}
