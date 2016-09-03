/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iconoctopus
 */
public class RollUtilsTest
{
//cas d'erreur sur paramétres

    @Test
    public void testLancer()
    {
	try
	{
	    RollUtils.lancer(-1, 2, true);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de dés lancés:-1 nombre de dés gardés:2", e.getMessage());
	}

	try
	{
	    RollUtils.lancer(2, -1, true);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de dés lancés:2 nombre de dés gardés:-1", e.getMessage());
	}
    }
}
