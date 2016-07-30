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
public class CompetenceTest
{

    @Test
    public void testCompetence()
    {
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
