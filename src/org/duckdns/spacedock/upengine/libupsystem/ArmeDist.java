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
     * constructeur identique à celui de la superclasse, celle-ci emploie la
     * qualité et l'équilibrage pour le nommage, ici on en applique les effets
     * le constructeur appelle aussi dans UPReferenceSysteme les spécificités
     * des armes à distance
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
     * ajoute la compétence d'arme au trait corrdination
     *
     * @param p_Traits
     * @param p_arbreDomComp
     * @return
     */
    @Override
    int extractBonusCarac(GroupeTraits p_Traits, ArbreDomaines p_arbreDomComp)
    {
	int domaine = 4;//distance
	int competence = getCategorie();//comp d'arme

	return (p_arbreDomComp.getRangComp(domaine, competence) + p_Traits.getTrait(GroupeTraits.Trait.COORDINATION));
    }

    /**
     *
     * @param p_nbMun
     */
    public void consommerMun(int p_nbMun)
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
     * méthode appelée par le perso avant qu'il n'effectue son attaque, elle va
     * se charger des particularismes des armes à distance pour présenter les
     * bonus/malus finaux agglomérés qu'il faudra appliquer à l'attaque en
     * fonction de la portée et du nombre de munitions employées. Elle se
     * chargera aussi de définir si l'arme peut opérer selon ces paramétres.
     *
     * @param p_distance
     * @param p_nbCoups
     * @return
     */
    DistReport verifPreAttaque(int p_distance, int p_nbCoups)
    {
	DistReport result = new DistReport(0, 0, 0, true);//échec auto par défaut
	int modDist = 0;
	if (p_distance >= 0 && p_nbCoups > 0 && p_nbCoups <= 20)
	{
	    consommerMun(p_nbCoups);//on consomme les coups, une exception sera levée si il n'y a pas assez de munitions, le code appelant devrait vérifier systématiquement cela
	    if (p_distance <= getPortee())//échec auto si distance > portée
	    {
		if (p_distance <= (int) Math.round((double) getPortee() / (double) 2))
		{//portée courte
		    modDist -= getMalusCourt();
		}
		else
		{//portée longue
		    modDist -= getMalusLong();
		}
		//conditions initiales remplies, maintenant il faut évaluer l'éventuelle rafale
		int bonusDesLancesRafale = 0;
		int bonusDesGardesRafale = 0;

		if (p_nbCoups > 1)
		{
		    if (getCategorie() == 4)//rafales acceptées, sinon lever une exception
		    {
			if (p_nbCoups >= 3)//les bonus commmencent à partir de 3 balles
			{
			    if (p_nbCoups < 4)//rafale courte
			    {
				bonusDesLancesRafale = 2;
			    }
			    else
			    {
				if (p_nbCoups < 10)//rafale moyenne
				{
				    int preResult = (p_nbCoups / 3);//division entre int donc troncature
				    bonusDesLancesRafale = preResult * 2;
				}
				else//rafale longue
				{
				    bonusDesLancesRafale = bonusDesGardesRafale = (p_nbCoups / 5);//division entre int donc troncature
				}
			    }
			}
		    }
		    else
		    {
			recharger(p_nbCoups);//on restaure les coups retirés alors qu'en fait le tir n'a pas eu lieu car l'arme ne peut tier en rafale
			ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("nbCoups") + ":" + p_nbCoups);
		    }
		}
		result = new DistReport(bonusDesLancesRafale, bonusDesGardesRafale, modDist, false);
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("distance") + ":" + p_distance + " " + PropertiesHandler.getInstance("libupsystem").getString("nbCoups") + ":" + p_nbCoups);
	}
	return result;
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
    public int recharger(int p_nbMun)
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

    /**
     * classe interne utilisée pour encapsuler l'évaluation pré-déclenchement
     * d'une attaque à distance
     */
    public static final class DistReport
    {

	private final int m_modDesGardes;
	private final int m_modDesLances;
	private final int m_modJet;
	private final boolean m_echecAuto;

	/**
	 * constructeur de cet objet de retour
	 *
	 * @param p_lances
	 * @param p_gardes
	 * @param p_modif
	 * @param p_echec
	 */
	public DistReport(int p_lances, int p_gardes, int p_modif, boolean p_echec)
	{
	    m_modDesGardes = p_gardes;
	    m_modDesLances = p_lances;
	    m_modJet = p_modif;
	    m_echecAuto = p_echec;
	}

	public int getModDesGardes()
	{
	    return m_modDesGardes;
	}

	public int getModDesLances()
	{
	    return m_modDesLances;
	}

	public int getModJet()
	{
	    return m_modJet;
	}

	public boolean isEchecAuto()
	{
	    return m_echecAuto;
	}
    }
}
