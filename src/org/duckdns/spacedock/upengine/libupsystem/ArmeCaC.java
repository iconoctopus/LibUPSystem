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

import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 *
 * @author ykonoclast
 */
public class ArmeCaC extends Arme
{

    /**
     * constructeur identique à celui de la superclasse
     *
     * @param p_indice
     * @param p_qualite
     * @param p_equilibrage
     */
    public ArmeCaC(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	super(p_indice, p_qualite, p_equilibrage);
    }

    /**
     * génère les dégâts infligés avec cette arme en combat au corps à corps
     *
     * @param p_nbIncrements
     * @param p_physique le physique du personnage
     * @param p_isSonne
     * @return
     */
    Degats genererDegats(int p_nbIncrements, int p_physique, boolean p_isSonne)
    {
	int degatsBruts = 0;
	if (p_nbIncrements >= 0 && p_physique >= 0)
	{
	    degatsBruts = (RollUtils.lancer(super.getDesLances() + p_nbIncrements + p_physique, super.getDesGardes(), p_isSonne));
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("increments") + ":" + p_nbIncrements + " " + PropertiesHandler.getInstance("libupsystem").getString("physique") + ":" + p_physique);
	}
	return new Degats(degatsBruts, super.getTypeArme());
    }
}
