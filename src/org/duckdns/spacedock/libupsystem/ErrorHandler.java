/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.libupsystem;

import java.io.IOException;

/**
 *
 * @author iconoctopus
 */
public class ErrorHandler
{

    static void paramAberrant(String p_complementTexte)
    {
	String message = "";

	message = message.concat(PropertiesHandler.getInstance().getErrorMessage("param_inf_0"));
	message = message.concat(p_complementTexte);

	throw new IllegalArgumentException(message);
    }

}
