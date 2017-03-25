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
import org.junit.Test;

/**
 *
 * @author ykonoclast
 */
public class DomaineTest
{

    /*
    try
	{
	    arbreTest.setRangComp(0, 0, 5);
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

     */
    @Test
    public void testDomaine()
    {
	Domaine domaine1 = new Domaine(0, 1);
	Domaine domaine2 = new Domaine(8, 5);
	Domaine domaineCac = new Domaine(3, 3);

	Assert.assertEquals(1, domaine1.getRang());
	domaine1.getRangComp(2);
	try
	{
	    domaine1.getRangComp(3);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	Assert.assertEquals(5, domaine2.getRang());
	domaine2.getRangComp(3);
	try
	{
	    domaine2.getRangComp(4);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	Assert.assertEquals(3, domaineCac.getRang());
	domaineCac.getRangComp(15);
	try
	{
	    domaine2.getRangComp(16);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

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

    }

    //cas nominaux et d'erreur sur paramétres
    @Test
    public void testGetSetRang()
    {
	Domaine domaine = new Domaine(0, 4);

	Assert.assertEquals(4, domaine.getRang());

	domaine.setRang(3);
	Assert.assertEquals(3, domaine.getRang());
	try
	{
	    domaine.setRang(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:0", e.getMessage());
	}

    }

    //cas d'erreur sur paramétres
    @Test
    public void testEffectuerJetComp()
    {
	Domaine domaine = new Domaine(0, 1);
	//cas d'erreur sur paramétre de compétence
	try
	{
	    domaine.effectuerJetComp(-1, 1, 0, 0, 0, 0, false);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:1 domaine:1 indice compétence:-1", e.getMessage());
	}

	//cas d'erreur sur paramétre de trait
	try
	{
	    domaine.effectuerJetComp(1, -1, 0, 0, 0, 0, false);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:-1 domaine:1 indice compétence:1", e.getMessage());
	}
    }
}
