/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.libupsystem;

import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public final class PropertiesHandlerTest
{

    private static PropertiesHandler m_handler;

    @BeforeClass
    public static void setUpClass() throws IOException
    {
	m_handler = PropertiesHandler.getInstance();
    }

    @Test
    public void testGetExceptionMessage()
    {
	Assert.assertEquals("valeur de caractéristique aberrante:", m_handler.getErrorMessage("carac_inf_0"));
    }
}
