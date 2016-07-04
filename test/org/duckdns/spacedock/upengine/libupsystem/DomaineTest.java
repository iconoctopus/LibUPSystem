/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class DomaineTest
{

    @Test
    public void testDomaine()
    {
	Domaine domaine1 = new Domaine(0, 1);
	Domaine domaine2 = new Domaine(8, 5);

	Assert.assertEquals(1, domaine1.getRang());
	Assert.assertEquals(3, domaine1.getCompetences().size());

	Assert.assertEquals(5, domaine2.getRang());
	Assert.assertEquals(4, domaine2.getCompetences().size());

	Domaine domaine3;

	try
	{
	    domaine3 = new Domaine(-1, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:indice:-1", e.getMessage());
	}

	try
	{
	    domaine1.setRang(-11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}

	try
	{
	    new Competence(-11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}

	try
	{
	    new CompCac(1, -11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}
    }
}
