/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 *
 * @author iconoctopus
 */
public class UPErrorHandler
{

    public static void ajoutPieceArmure(String p_lblPiece)
    {
	ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("ajout").concat(p_lblPiece));
    }

    public static void retraitPieceArmure(String p_lblPiece)
    {
	ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("retrait").concat(p_lblPiece));
    }

    public static void mauvaisModeAttaque()
    {
	ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("mode_att"));
    }
}
