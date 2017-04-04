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
public class ArmeDist extends Arme
{

    /**
     * le nombre de coups présents dans l'arme au départ
     */
    private int m_magasinCourant;
    /**
     * la capacité du magasin de l'arme
     */
    private final int m_magasinMax;

    /**
     * le malus à portée courte
     */
    private final int m_malusCourt;
    /**
     * le malus à portée longue
     */
    private final int m_malusLong;
    /**
     * le nombre d'actions pour recharger
     */
    private final int m_nbActionsRecharge;
    /**
     * le malus à portée longue
     */
    private final int m_portee;

    /**
     * constructeur appelant dans UPReferenceSysteme les spécificités des arms à
     * distance
     *
     * @param p_indice
     * @param p_qualite
     * @param p_equilibrage
     */
    public ArmeDist(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	super(p_indice, p_qualite, p_equilibrage);
	UPReferenceArmes reference = UPReferenceArmes.getInstance();
	int modifMalus;
	int porteeEffective = reference.getPorteeArme(p_indice);

	if (p_qualite == QualiteArme.maitre)//traitement spécial des armes de maître
	{
	    modifMalus = -6;
	    porteeEffective = porteeEffective * 2;
	}
	else
	{
	    switch (p_qualite)
	    {
		case inferieure:
		    modifMalus = +3;
		    break;
		case superieure:
		    modifMalus = -3;
		    break;
		default:
		    modifMalus = 0;
		    break;
	    }
	    switch (p_equilibrage)
	    {
		case mauvais:
		    porteeEffective = porteeEffective / 2;
		    break;
		case bon:
		    porteeEffective = porteeEffective * 2;
		    break;
		default:
		    break;
	    }
	}
	m_malusCourt = reference.getMalusCourtArme(p_indice) + modifMalus;//ainsi que modifié par la qualité
	m_malusLong = reference.getMalusLongArme(p_indice) + modifMalus;//ainsi que modifié par la qualité
	m_portee = porteeEffective;//ainsi que modifiée par l'équilibrage et la qualité
	m_nbActionsRecharge = reference.getNbActionsRechargeArme(p_indice);
	m_magasinMax = reference.getMagasinArme(p_indice);
	m_magasinCourant = 0;//par défaut l'arme n'est pas chargée
    }

    /**
     *
     * @param p_nbMun
     */
    void consommerMun(int p_nbMun)
    {
	if (p_nbMun > 0 && p_nbMun <= m_magasinCourant)
	{
	    m_magasinCourant -= p_nbMun;
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("nbCoups") + ":" + p_nbMun + " " + PropertiesHandler.getInstance("libupsystem").getString("muncourantes") + ":" + m_magasinCourant);
	}
    }

    /**
     * @return the m_malusCourt
     */
    int getMalusCourt()
    {
	return m_malusCourt;
    }

    /**
     * @return the m_malusLong
     */
    int getMalusLong()
    {
	return m_malusLong;
    }

    /**
     * @return the m_magasinCourant
     */
    int getMunCourantes()
    {
	return m_magasinCourant;
    }

    /**
     * @return the m_nbActionsRecharge
     */
    int getNbActionsRecharge()
    {
	return m_nbActionsRecharge;
    }

    /**
     * @return the m_portee
     */
    int getPortee()
    {
	return m_portee;
    }

    /**
     * @return the m_magasinMax
     */
    int getTailleMAgasin()
    {
	return m_magasinMax;
    }

    /**
     * @param p_nbMun quantité de munitions à recharger
     * @return le nombre d'action que prendra la recharge
     */
    int recharger(int p_nbMun)
    {
	if (p_nbMun > 0 && (p_nbMun + m_magasinCourant) <= m_magasinMax)
	{
	    m_magasinCourant += p_nbMun;
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("nbCoups") + ":" + p_nbMun + " " + PropertiesHandler.getInstance("libupsystem").getString("taillemagasin") + ":" + m_magasinMax);
	}
	return getNbActionsRecharge();
    }
}
