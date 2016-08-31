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
 *
 * @author iconoctopus
 */
public class ArbreDomainesTest
{

    @Test
    public void testGetSetRangDomaine()
    {
	ArbreDomaines arbre = new ArbreDomaines();
	arbre.setRangDomaine(0, 5);
	Assert.assertEquals(5, arbre.getRangDomaine(0));

	try
	{
	    arbre.setRangDomaine(-1, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    arbre.setRangDomaine(9, 5);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    arbre.getRangDomaine(9);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	try
	{
	    arbre.getRangDomaine(-1);
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
	arbre.setRangDomaine(1, 3);
	arbre.setRangComp(1, 0, 2);
	Assert.assertEquals(2, arbre.getRangComp(1, 0));

	arbre.setRangDomaine(0, 2);

	try
	{
	    arbre.setRangComp(0, 0, 5);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang compétence > rang domaine", e.getMessage());
	}

	try
	{
	    arbre.setRangComp(0, 7, 2);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	try
	{
	    arbre.setRangComp(0, -1, 2);
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
	arbre.setRangDomaine(1, 3);
	arbre.setRangComp(1, 0, 2);
	arbre.addSpecialite(1, 0, "brossage de dent");
	arbre.addSpecialite(1, 0, "nettoyage de pot de chambre");
	arbre.addSpecialite(1, 0, "gravure sur écorce de cacahuète");

	Assert.assertEquals("brossage de dent", arbre.getSpecialites(1, 0).get(0));
	Assert.assertEquals("nettoyage de pot de chambre", arbre.getSpecialites(1, 0).get(1));
	Assert.assertEquals("gravure sur écorce de cacahuète", arbre.getSpecialites(1, 0).get(2));

	try
	{
	    arbre.removeSpecialite(1, 0, 3);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{
	}
	try
	{
	    arbre.removeSpecialite(1, 0, -1);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{
	}
	arbre.removeSpecialite(1, 0, 1);
	Assert.assertEquals("brossage de dent", arbre.getSpecialites(1, 0).get(0));
	Assert.assertEquals("gravure sur écorce de cacahuète", arbre.getSpecialites(1, 0).get(1));
    }

}
