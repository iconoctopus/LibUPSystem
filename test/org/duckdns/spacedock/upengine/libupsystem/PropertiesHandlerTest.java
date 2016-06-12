/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.duckdns.spacedock.upengine.libupsystem.PropertiesHandler;
import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author iconoctopus
 */
public final class PropertiesHandlerTest
{

    private static PropertiesHandler m_handler;

    public PropertiesHandlerTest()
    {

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws IOException
    {
	m_handler = PropertiesHandler.getInstance();
    }

    @Test
    public void testGetExceptionMessageNominal()
    {
	Assert.assertEquals("paramétre aberrant:", m_handler.getErrorMessage("param_inf_0"));
    }
}
