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
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 * classe représentant une compétence
 *
 * @author ykonoclast
 */
class Competence
{

    /**
     * le rang de la compétence, déclenche des jets spéciaux à 3 et 5 ; mutable
     * pour monter à l'XP ou baisser si malheur
     */
    private int m_rang;

    /**
     * la liste des spécialités de la compétence
     */
    private final ArrayList<String> m_specialites;

    /**
     * Classe encapsulant une compétence (pincipalement rang et spécialités) La
     * gestion des spécialités est encore embryonnaire : la compétence en
     * possède une liste mais celles-ci ne sont en aucun cas gérées par le
     * système
     *
     *
     * @param p_rang
     * @param p_specialites
     */
    Competence(int p_rang, ArrayList<String> p_specialites)
    {
	setRang(p_rang);
	if (p_specialites != null)
	{
	    m_specialites = p_specialites;
	}
	else
	{
	    m_specialites = new ArrayList<String>();
	}
    }

    /**
     *
     * @param p_lbl
     */
    final void addSpecialite(String p_lbl)
    {
	m_specialites.add(p_lbl);
    }

    final int getRang()
    {
	return m_rang;
    }

    /**
     *
     * @param p_rang
     */
    final void setRang(int p_rang)
    {
	if (p_rang >= 0)
	{
	    m_rang = p_rang;
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("rang") + ":" + p_rang);
	}
    }

    /**
     *
     * @return une copie seulement pour plus de sécurité, l'ajout ou la
     * suppression de spécialité n'étant possible que via l'interface dédiée
     */
    final ArrayList<String> getSpecialites()
    {
	return new ArrayList<String>(m_specialites);
    }

    /**
     *
     * @param p_indice
     */
    final void removeSpecialite(int p_indice)
    {
	m_specialites.remove(p_indice);
    }

}
