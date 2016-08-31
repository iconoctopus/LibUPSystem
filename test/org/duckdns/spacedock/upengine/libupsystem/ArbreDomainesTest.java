/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * On profite de cette classe pour tester le bout en bout complet : à partir du
 * point d'entrée qu'est la classe Perso
 *
 * @author iconoctopus
 */
public class ArbreDomainesTest
{

    @Test
    public void testGetSetRangDomaine()
    {
	ArbreDomaines arbre = new ArbreDomaines();
	int[] traits =
	{
	    7, 6, 5, 4, 3
	};
	Perso persoTest = new Perso(traits, arbre);

	//on en profite pour tester que la création se passe bien côté traits même si cela n'est pas en rapport avec ce test précis
	Assert.assertEquals(7, persoTest.getTrait(0));
	Assert.assertEquals(6, persoTest.getTrait(1));
	Assert.assertEquals(5, persoTest.getTrait(2));
	Assert.assertEquals(4, persoTest.getTrait(3));
	Assert.assertEquals(3, persoTest.getTrait(4));

	persoTest.setRangDomaine(0, 5);
	Assert.assertEquals(5, persoTest.getRangDomaine(0));

	try
	{
	    persoTest.setRangDomaine(-1, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    persoTest.setRangDomaine(9, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    persoTest.getRangDomaine(9);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    persoTest.getRangDomaine(-1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
    }

    @Test
    public void testGetSetRangComp()
    {
	ArbreDomaines arbre = new ArbreDomaines();
	int[] traits =
	{
	    1, 1, 1, 1, 1
	};
	Perso persoTest = new Perso(traits, arbre);

	persoTest.setRangDomaine(1, 3);
	persoTest.setRangComp(1, 0, 2);
	Assert.assertEquals(2, arbre.getRangComp(1, 0));

	persoTest.setRangDomaine(0, 2);

	try
	{
	    persoTest.setRangComp(0, 0, 5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang compétence > rang domaine", e.getMessage());
	}

	try
	{
	    persoTest.setRangComp(0, 7, 2);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    persoTest.setRangComp(0, -1, 2);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
    }

    @Test
    public void testGetAddRemoveSpecialites()
    {
	ArbreDomaines arbre = new ArbreDomaines();
	int[] traits =
	{
	    1, 1, 1, 1, 1
	};
	Perso persoTest = new Perso(traits, arbre);

	persoTest.setRangDomaine(1, 3);
	persoTest.setRangComp(1, 0, 2);
	persoTest.addSpecialite(1, 0, "brossage de dent");
	persoTest.addSpecialite(1, 0, "nettoyage de pot de chambre");
	persoTest.addSpecialite(1, 0, "gravure sur écorce de cacahuète");

	Assert.assertEquals("brossage de dent", persoTest.getSpecialites(1, 0).get(0));
	Assert.assertEquals("nettoyage de pot de chambre", persoTest.getSpecialites(1, 0).get(1));
	Assert.assertEquals("gravure sur écorce de cacahuète", persoTest.getSpecialites(1, 0).get(2));

	try
	{
	    persoTest.removeSpecialite(1, 0, 3);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{
	}
	try
	{
	    persoTest.removeSpecialite(1, 0, -1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{
	}
	persoTest.removeSpecialite(1, 0, 1);
	Assert.assertEquals("brossage de dent", persoTest.getSpecialites(1, 0).get(0));
	Assert.assertEquals("gravure sur écorce de cacahuète", persoTest.getSpecialites(1, 0).get(1));
    }

}
