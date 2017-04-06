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

/**
 *
 * @author ykonoclast
 */
public class UnitCompetenceTest
{

    private static Competence competence;
    private static ArrayList<String> specialites;

    @BeforeClass
    public static void setUpClass()
    {
	specialites = new ArrayList<>();
	specialites.add("murmure à l'oreille des veaux");
	specialites.add("poinçonnage de tickets de métro");
	competence = new Competence(3, specialites);
    }

    @Before
    public void setUp()
    {
    }

    @Test
    public void testCompetenceErreur()
    {
	try
	{
	    new Competence(-11, null);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}
    }

    @Test
    public void testRangErreur()
    {
	try
	{
	    competence.setRang(-1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-1", e.getMessage());
	}
    }

    @Test
    public void testRangNominal()
    {
	Assert.assertEquals(3, competence.getRang());
	competence.setRang(7);
	Assert.assertEquals(7, competence.getRang());
    }

    @Test
    public void testSpecialites()
    {
	//Il n'y a que deux spécialités
	try
	{
	    competence.getSpecialites().get(2);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}
	//On en ajoute une et on vérifie qu'elle est bien ajoutée
	competence.addSpecialite("tricotage de pulls sans ovaires");
	Assert.assertEquals("tricotage de pulls sans ovaires", competence.getSpecialites().get(2));

	//On retire la dernière compétence et on vérifie qu'elle n'y est plus
	competence.removeSpecialite(2);
	try
	{
	    competence.getSpecialites().get(2);
	    fail();
	}
	catch (IndexOutOfBoundsException e)
	{

	}

	//On doit retrouver la liste originelle
	Assert.assertEquals("murmure à l'oreille des veaux", competence.getSpecialites().get(0));
	Assert.assertEquals("poinçonnage de tickets de métro", competence.getSpecialites().get(1));
    }
}
